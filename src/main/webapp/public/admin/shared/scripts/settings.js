var alertComponent = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");
var dialogComponent = window.Clubby.Dialog();

$(function () {
    load();
    loadPeriods();
    $("#save-period").click(handleAddPeriod);
    $("#set-price").click(handleSetPrice)
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

function loadPeriods() {
    var template = _.template($("#reservation-period-table-template").html());
    var table = $("#reservation-periods-table");
    table.html("<div class='loader'></div>");

    return $.ajax({
        type: "GET",
        url: "/api/reservation/period",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        table.html(template(response));
        table.find(".delete-period").click(handleDeletePeriod);
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
function handleSetPrice() {

    var button = $("#set-price");
    button.button('loading');
    var request = {
        price: $("#price").val() *100
    };
    return $.ajax({
        type: "PUT",
        url: "/api/payments/membership",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        button.button('reset');
        dashboardMessage.html(alertComponent({
            title: "Success!",
            message: "Price updated.",
            severity: "success"
        }));

    }).fail(function (response) {
        button.button('reset');
        var message = getErrorMessageFromResponse(response) || "Unknown error.";
        dashboardMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
    })
}
function handleDeletePeriod(event) {
    var id = $(event.target).attr("data-period-id");

    var dialog = $(dialogComponent({title: "Delete period?", body: "Are you sure you want to delete period?"}));

    dialog.find(".dialog-yes").click(function () {
        return $.ajax({
            type: "DELETE",
            url: "/api/reservation/period?id=" + id
        }).done(function () {
            dialog.modal("hide");
            dashboardMessage.html("");
            loadPeriods().done(function () {
                dashboardMessage.html(alertComponent({
                    title: "Success!",
                    message: "Period deleted.",
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
function handleAddPeriod(event) {
    var modal = $("#add-period-modal");
    var button = modal.find("#save-period");
    button.button('loading');
    var messagebox = $("#period-modal-message-box");
    var request = {

        from: $("#start-date").val(),
        to: $("#end-date").val()

    };

    return $.ajax({
        type: "POST",
        url: "/api/reservation/period",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        button.button('reset');
        modal.modal("hide");

        loadPeriods().done(function () {
            dashboardMessage.html(alertComponent({
                title: "Success!",
                message: "Period added.",
                severity: "success"
            }));
        });
    }).fail(function (response) {
        button.button('reset');
        var message = getErrorMessageFromResponse(response) || "Unknown error.";
        messagebox.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
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

