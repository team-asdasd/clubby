var alertComponent = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");
var modal = $("#field-modal");
var modalMessageBox = modal.find("#modal-message-box");
$(function () {
    load();
    $("#add-field").click(function () {
        $('#modal-table').find('input').val('');
        $('.modal-title').html("Add new field");
        $('#name').prop('disabled', false);
        $(modalMessageBox).html("");
        modal.find('#save').off().click(handleAdd);
    });
});

function load() {
    var template = _.template($("#field-table-template").html());
    var table = $("#fields-table");
    table.html("<div class='loader'></div>");

    return $.ajax({
        type: "GET",
        url: "/api/form",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        table.html(template(response));
        table.find(".edit-field").click(handleEdit);
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";

        dashboardMessage.html(alertDialog({title: "Error!", message: message, severity: "danger"}));
    });
}
function handleAdd() {
    var button = modal.find("#save");
    button.button('loading');
    var request = {
        name: $('#name').val(),
        type: $('#type').val(),
        validationRegex: $('#validationRegex').val(),
        description: $('#description').val(),
        required: $('#required').is(":checked"),
        visible: $('#visible').is(":checked")
    };
    modal.modal('show', true);
    return $.ajax({
        type: "POST",
        url: "/api/form/field",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        button.button('reset');
        modal.modal("hide");

        load().done(function () {
            dashboardMessage.html(alertComponent({
                title: "Success!",
                message: "Field added.",
                severity: "success"
            }));
        });
    }).fail(function (response) {
        button.button('reset');

        var message = getErrorMessageFromResponse(response) || "Unknown error.";
        modalMessageBox.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
    })
}
function handleEdit(event) {
    var data = $(event.target).closest('tr').find('td');

    $('.modal-title').html("Edit field");
    $(modalMessageBox).html("");
    modal.find('input').val('');

    var button = modal.find("#save");

    $('#name').val($(data[0]).html());
    $('#name').prop('disabled', true);
    $('#type').val($(data[1]).html());
    $('#validationRegex').val($(data[2]).html());
    $('#description').val($(data[3]).html());
    $('#required').prop('checked', ($(data[4]).find('input').is(":checked")));
    $('#visible').prop('checked', ($(data[5]).find('input').is(":checked")));
    button.off().click(function () {
        button.button('loading');

        var request = {
            name: $('#name').val(),
            type: $('#type').val(),
            validationRegex: $('#validationRegex').val(),
            description: $('#description').val(),
            required: $('#required').is(":checked"),
            visible: $('#visible').is(":checked")
        };

        return $.ajax({
            type: "PUT",
            url: "/api/form/field",
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
            var message = getErrorMessageFromResponse(response) || "Unknown error.";
            modalMessageBox.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        })
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

