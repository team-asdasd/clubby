var alertDialog = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");
var dialogComponent = window.Clubby.Dialog();

var modal = $("#create-user-modal");

$(function () {
    loadUsers();
    modal.find("#save").click(createUser);
});

function createUser() {
    var modalMessage = modal.find("#modal-message-box");

    var name = modal.find("#name");
    var email = modal.find("#email");
    var pass = modal.find("#password1");
    var pass2 = modal.find("#password2");

    var request = {
        "name": name.val(),
        "email": email.val(),
        "password": pass.val(),
        "passwordConfirm": pass2.val()
    };

    $.ajax({
        type: "POST",
        url: "/api/user",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        modalMessage.html("");
        dashboardMessage.html(alertDialog({title: "Success!", message: "User created.", severity: "success"}));

        name.val("");
        email.val("");
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
        $(".delete-user").click(handleDelete);
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";

        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}
function handleDelete(event) {
    var id = $(event.target).attr("data-user-id");

    var dialog = $(dialogComponent({title: "Delete user?", body: "Are you sure you want to delete this user?"}));

    dialog.find(".dialog-yes").click(function () {
        return $.ajax({
            type: "DELETE",
            url: "/api/user/" + id
        }).done(function () {
            dialog.modal("hide");

            loadUsers().done(function () {
                dashboardMessage.html(alertComponent({
                    title: "Success!",
                    message: "User deleted.",
                    severity: "success"
                }));
            });
        }).fail(function (response) {
            dialog.modal("hide");

            var message = getErrorMessageFromResponse(response) || "Unknown error.";
            dashboardMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        });
    });

    dialog.find(".dialog-no").click(function () {
        dialog.modal("hide");
    });

    dialog.modal("show");
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

