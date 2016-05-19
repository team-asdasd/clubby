var alertDialog = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");

$(function () {
    loadUsers();
    $("#create-user-modal").find("#save").click(createUser);
});

function createUser() {
    var modal = $("#create-user-modal");

    var modalMessage = modal.find("#modal-message-box");

    var username = modal.find("#username");
    var email = modal.find("#email");
    var firstName = modal.find("#firstName");
    var lastName = modal.find("#lastName");
    var pass = modal.find("#password1");
    var pass2 = modal.find("#password2");

    var request = {
        "userName": username.val(),
        "email": email.val(),
        "firstName": firstName.val(),
        "lastName": lastName.val(),
        "password": pass.val(),
        "passwordConfirm": pass2.val()
    };

    $.ajax({
        type: "POST",
        url: "/api/user/create",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        modalMessage.html("");
        dashboardMessage.html(alertDialog({title: "Success!", message: "User created.", severity: "success"}));

        username.val("");
        email.val("");
        firstName.val("");
        lastName.val("");
        pass.val("");
        pass2.val("");

        modal.modal("hide");

        loadUsers();
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error creating user!";
        modalMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}

function loadUsers() {
    var template = _.template($("#users-table-template").html());
    var table = $("#users-table");
    table.html("<div class='loader'></div>");

    $.ajax({
        type: "GET",
        url: "/api/user",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        table.html(template(response));
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";

        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}

function getErrorMessageFromResponse(response) {
    var message;

    if (response && response.responseJSON && response.responseJSON.Errors) {
        message = _.reduce(response.responseJSON.Errors, function (memo, error) {
            return memo + "<li>" + error.Message + "</li>";
        }, "<ul>");

        message += "</ul>";
    }

    return message;
}

