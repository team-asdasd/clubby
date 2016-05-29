var alertDialog = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");

$(function () {
    loadUsers();
    loadCottagesAndReservations();
});

function loadUsers() {
    var template = _.template($("#users-stats-template").html());
    var table = $("#users-stats");
    table.html("<div class='loader'></div>");

    $.ajax({
        type: "GET",
        url: "/api/user",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        var users = response.users;

        var data = {
            candidateCount: getRole(users, "candidate") + getRole(users, "potentialCandidate"),
            candidateOnlineCount: getOnlineRole(users, "candidate") + getOnlineRole(users, "potentialCandidate"),
            memberCount: getRole(users, "member"),
            memberOnlineCount: getOnlineRole(users, "member"),
            adminCount: getRole(users, "admin"),
            adminOnlineCount: getOnlineRole(users, "admin")
        };

        table.html(template(data));
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";
        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });

    function getRole(users, role) {
        return _.filter(users, function (user) {
            return _.contains(user.roles, role);
        }).length
    }

    function getOnlineRole(users, role) {
        return _.chain(users).filter(function (user) {
            return _.contains(user.roles, role);
        }).filter(function (user) {
            return user.online === true;
        }).value().length
    }
}

function loadCottagesAndReservations() {
    var template = _.template($("#other-stats-template").html());
    var table = $("#other-stats");
    table.html("<div class='loader'></div>");

    var cottages = $.ajax({
        type: "GET",
        url: "/api/cottage",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    var uReservations = $.ajax({
        type: "GET",
        url: "/api/reservation?category=upcoming",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });
    var pReservations = $.ajax({
        type: "GET",
        url: "/api/reservation?category=passed",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    $.when(cottages, uReservations, pReservations).done(function (cResponse, uResponse, pResponse) {
        var cottages = cResponse[0].cottages;
        var uReservations = uResponse[0].reservations;
        var pReservations = pResponse[0].reservations;

        var data = {
            cottageCount: cottages.length,
            upcomingReservationCount: uReservations.length,
            passedReservationCount: pReservations.length
        };

        table.html(template(data));
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";
        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}