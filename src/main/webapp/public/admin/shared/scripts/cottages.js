$.cloudinary.config({cloud_name: 'teamasdasd'});

var dialogComponent = window.Clubby.Dialog();
var alertComponent = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");

$(function () {
    initializeFileUpload();

    $("#add-cottage-modal").find("#save").click(handleCreate);

    load();
});

function handleCreate() {
    var modal = $("#add-cottage-modal");
    modal.find("#modal-title").html("Add Cottage");

    var modalMessage = modal.find("#modal-message-box");

    var title = modal.find("#title");
    var bedcount = modal.find("#bedcount");
    var imageUrl = modal.find("#image");
    var description = modal.find("#description");
    var price = modal.find("#price");
    var availableFrom = modal.find("#availableFrom");
    var availableTo = modal.find("#availableTo");

    var request = {
        "title": title.val(),
        "beds": bedcount.val(),
        "image": imageUrl.val(),
        "description": description.val(),
        "price": price.val(),
        "availableFrom": availableFrom.val(),
        "availableTo": availableTo.val(),
    };

    $.ajax({
        type: "POST",
        url: "/api/cottage",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        modalMessage.html("");
        dashboardMessage.html(alertComponent({title: "Success!", message: "Cottage created.", severity: "success"}));

        title.val("");
        bedcount.val("");
        imageUrl.val("");
        $('.preview').html("");

        modal.modal("hide");

        load();
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error creating cottage!";
        modalMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
    });
}

function handleEdit(event) {
    var id = $(event.target).attr("data-cottage-id");
    var modal = $("#add-cottage-modal");
    modal.find("#modal-title").html("Add Cottage");

    var modalMessage = modal.find("#modal-message-box");

    var title = modal.find("#title");
    var bedcount = modal.find("#bedcount");
    var imageUrl = modal.find("#image");

    var request = {
        "title": title.val(),
        "bedcount": bedcount.val(),
        "imageurl": imageUrl.val()
    };

    $.ajax({
        type: "POST",
        url: "/api/cottage",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function () {
        modalMessage.html("");
        dashboardMessage.html(alertComponent({title: "Success!", message: "Cottage created.", severity: "success"}));

        title.val("");
        bedcount.val("");
        imageUrl.val("");
        $('.preview').html("");

        modal.modal("hide");

        load();
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error creating cottage!";
        modalMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
    });
}

function handleDelete(event) {
    var id = $(event.target).attr("data-cottage-id");

    var dialog = $(dialogComponent({title: "Delete cottage?", body: "Are you sure you want to delete cottage?"}));

    dialog.find(".dialog-yes").click(function () {
        return $.ajax({
            type: "DELETE",
            url: "/api/cottage/" + id
        }).done(function () {
            dialog.modal("hide");

            load().done(function () {
                dashboardMessage.html(alertComponent({
                    title: "Success!",
                    message: "Cottage deleted.",
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

function load() {
    var template = _.template($("#cottages-table-template").html());
    var table = $("#cottages-table");
    table.html("<div class='loader'></div>");

    return $.ajax({
        type: "GET",
        url: "/api/cottage",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        table.html(template(response));
        $("#cottages-table").find(".remove-cottage").click(handleDelete);
        $("#cottages-table").find(".edit-cottage").click(handleEdit);
    }).fail(function (response) {
        table.html("");
        var message = getErrorMessageFromResponse(response) || "Unknown error.";

        dashboardMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
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

function initializeFileUpload() {
    var fileUpload = $("div[class^='uploadinput']").append($.cloudinary.unsigned_upload_tag("syjxlwur"));
    fileUpload.children().first().attr("accept", "image/*");
    fileUpload.bind('cloudinarydone', function (e, data) {
            $("#image-upload").find("#progressbar").html("");

            var image = $.cloudinary.image(data.result.public_id, {
                crop: 'thumb', gravity: 'face', effect: 'saturation:50'
            }).addClass("img-thumbnail img-responsive");

            $('.preview').html(image);

            $("#create-cottage-modal").find("#image").val(data.result.url);
        }
    );

    fileUpload.bind('fileuploadprogress', function (e, data) {
        var percent = Math.round((data.loaded * 100.0) / data.total);
        var tpl = _.template('<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="<%= percentage %>" aria-valuemin="0" aria-valuemax="100" style="width: <%= percentage %>%;"><%= percentage %>%</div></div>');
        $("#image-upload").find("#progressbar").html(tpl({percentage: percent}));
    });
}
