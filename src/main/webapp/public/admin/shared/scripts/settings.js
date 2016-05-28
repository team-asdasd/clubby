var alertComponent = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");

$(function () {
    load();
});

function load() {
    var template = _.template($("#settings-table-template").html());
    var table = $("#settings-table");
    table.html("<div class='loader'></div>");

    return $.ajax({
        type: "GET",
        url: "/api/settings",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        table.html(template(response));
        table.find(".edit-setting").click(handleEdit);
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";

        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}

function handleEdit(event) {
    var key = $(event.target).attr("data-setting-key");
    var modal = $("#settings-modal");
    var data = $(event.target).closest('tr');
    modal.find('input').val('');

    modal.find("#key-label").html(data.find('.description').html());
    modal.find('input').val(data.find('.value').html());
    var button = modal.find("#save");
    button.off().click(function () {
        button.button('loading');

        var request = {
            settings: [
                {
                    key: key,
                    value: modal.find("#value").val()
                }
            ]
        };

        return $.ajax({
            type: "PUT",
            url: "/api/settings",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(request)
        }).done(function () {
            button.button('reset');
            modal.modal("hide");

            load().done(function () {
                dashboardMessage.html(alertComponent({
                    title: "Success!",
                    message: "Settings value updated.",
                    severity: "success"
                }));
            });
        }).fail(function (response) {
            button.button('reset');
            modal.modal("hide");

            var message = getErrorMessageFromResponse(response) || "Unknown error.";
            dashboardMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        }).always(function () {
            button.off();
        });
    });

    modal.modal('show', true);
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

