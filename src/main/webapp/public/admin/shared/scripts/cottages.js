$.cloudinary.config({cloud_name: 'teamasdasd'});
var dialogComponent = window.Clubby.Dialog();
var alertComponent = window.Clubby.Alert();
var dashboardMessage = $("#dashboard-message-box");
var modal = $("#add-cottage-modal");


$(function () {
    initializeFileUpload();
    $("#loading").hide();
    $("#add-service").click(addService);
    $("#add-cottage").click(function () {
        $("#currentImage").attr('src', "");
        $("#services-table-body").find("tr").remove();
        $("form").find("input").val("");
        $(".preview").html("");
        $("#modal-message-box").html("");
        $("#save").off('click').click(handleCreate);
        var template = _.template($("#services-table-template").html());
        $("#services").html(template());
        $('.remove-service').click(function (e) {
            $(this).closest('tr').remove();
        });
        var modal = $("#add-cottage-modal");
        modal.find("#myModalLabel").html("Add cottage");
        $("#add-service").click(addService);
    });
    load();
});

function handleCreate() {

    $("#loading").show();
    $("#save").prop("disabled", true);
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
        "price": parseInt(price.val()*100),
        "availableFrom": availableFrom.val(),
        "availableTo": availableTo.val(),
        services: []
    };
    _.forEach($("#services-table-body").find("tr"), function (row) {
        var inputs = $(row).find("input");
        var description = $(inputs.get(0)).val();
        var price = $(inputs.get(1)).val();
        var maxCount = $(inputs.get(2)).val();
        var service = {
            "description": description,
            "price": price,
            "maxCount": maxCount
        };
        request.services.push(service);
    });

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
        $("#loading").hide();
        $("#save").prop("disabled", false);
        load();
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error creating cottage!";
        modalMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        $("#loading").hide();
        $("#save").prop("disabled", false);
    });
}

function handleEdit(event) {
    $("#currentImage").attr('src', "");
    $("#services-table-body").find("tr").remove();
    $("form").find("input").val("");
    $(".preview").html("");
    $("#modal-message-box").html("");
    $("#save").off('click').click(sendUpdate);
    var id = $(event.target).attr("data-cottage-id");

    window.id = id;
    var modal = $("#add-cottage-modal");
    modal.find("#myModalLabel").html("Edit cottage");

    var modalMessage = modal.find("#edit-modal-message-box");

    var title = modal.find("#title");
    var bedcount = modal.find("#bedcount");
    var imageUrl = modal.find("#currentImage");
    var description = modal.find("#description");
    var price = modal.find("#price");
    var availableFrom = modal.find("#availableFrom");
    var availableTo = modal.find("#availableTo");
    $.ajax({
        type: "GET",
        url: "/api/cottage/" + id,
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (response) {
        modalMessage.html("");
        window.version = response.cottage.version;
        title.val(response.cottage.title);
        bedcount.val(response.cottage.beds);
        imageUrl.attr('src', response.cottage.image);
        modal.find("#image").val(response.cottage.image);
        description.val(response.cottage.description);
        price.val(response.cottage.price/100);
        availableFrom.val(response.cottage.availableFrom);
        availableTo.val(response.cottage.availableTo);

        var template = _.template($("#services-table-template").html());
        $("#services").html(template(response.cottage));
        $('.remove-service').click(function (e) {
            $(this).closest('tr').remove();
        });

        $("#add-service").click(addService);
        $("#loading").hide();
        $("#save").prop("disabled", false);
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error editing cottage!";
        modalMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        $("#loading").hide();
        $("#save").prop("disabled", false);
    });
}
function sendUpdate() {
    $("#loading").show();
    $("#save").prop("disabled", true);
    $("#modal-message-box").html("");
    var id = window.id;
    var modal = $("#add-cottage-modal");
    modal.find("#ModalLabel").html("Edit cottage");
    var modalMessage = modal.find("#modal-message-box");
    var title = modal.find("#title");
    var bedcount = modal.find("#bedcount");
    var imageUrl = modal.find("#image");
    var description = modal.find("#description");
    var price = modal.find("#price");
    var availableFrom = modal.find("#availableFrom");
    var availableTo = modal.find("#availableTo");
    var cottage = {
        "version": window.version,
        "id": id,
        "title": title.val(),
        "beds": bedcount.val(),
        "image": imageUrl.val(),
        "description": description.val(),
        "price": parseInt(price.val().replace(',','.')*100),
        "availableFrom": availableFrom.val(),
        "availableTo": availableTo.val(),
        services: []
    };

    _.each($("#services-table-body").find("tr"), function (row) {
        var inputs = $(row).find("input");
        var description = $(inputs.get(0)).val();
        var price = parseInt($(inputs.get(1)).val().replace(',','.')*100);
        var maxCount = $(inputs.get(2)).val();
        var service = {
            "description": description,
            "price": price,
            "maxCount": maxCount
        };
        cottage.services.push(service);
    });
    request = {"cottage": cottage};
    $.ajax({
        type: "PUT",
        url: "/api/cottage",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    }).done(function (response) {
        modalMessage.html(alertComponent({title: "Success!", message: "Cottage updated.", severity: "success"}));
        modal.modal("hide");
        $("#loading").hide();
        $("#save").prop("disabled", false);
        load();
    }).fail(function (response) {
        modalMessage.html("");

        var message = getErrorMessageFromResponse(response) || "Error editing cottage!";
        modalMessage.html(alertComponent({title: "Error!", message: message, severity: "danger"}));
        $("#loading").hide();
        $("#save").prop("disabled", false);
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
function addService() {

    $("#services-table-body").append('\
            <tr><td><input class="form-control" type="text" ></td>\
                <td><input class="form-control" type="number" value="0"></td>\
                <td><input class="form-control" type="number"></td>\
                <td><a class="btn btn-default remove-service">\
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>\
                    <span style="font-size: 16px;"> Remove</span></a></td>\
            </tr>');
    $('.remove-service').click(function (e) {
        $(this).closest('tr').remove();
    });
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
            $("#currentImage").attr('src', "");
            var image = $.cloudinary.image(data.result.public_id, {
                width: 300, height: 300,
            }).addClass("img-thumbnail img-responsive");

            $('.preview').html(image);

            modal.find("#image").val(data.result.url);
        }
    );

    fileUpload.bind('fileuploadprogress', function (e, data) {
        var percent = Math.round((data.loaded * 100.0) / data.total);
        var tpl = _.template('<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="<%= percentage %>" aria-valuemin="0" aria-valuemax="100" style="width: <%= percentage %>%;"><%= percentage %>%</div></div>');
        $("#image-upload").find("#progressbar").html(tpl({percentage: percent}));
    });
}
