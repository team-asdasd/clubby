$.cloudinary.config({cloud_name: 'teamasdasd'})
$("div[class^='uploadinput']").append($.cloudinary.unsigned_upload_tag("syjxlwur"))
    .bind('cloudinarydone', function (e, data) {
            $("div[class^='thumbnail']").append($.cloudinary.image(data.result.public_id,
                {
                    width: 100, height: 100,
                    crop: 'thumb', gravity: 'face', radius: 'max'
                }))
            $('#photo').attr('value', data.result.url);
            $('.progress').hide();
        }
    ).bind('fileuploadprogress', function (e, data) {
    $('.progress').show();
    $('.progress-bar').css('width', Math.round((data.loaded * 100.0) / data.total) + '%');
});


$.getJSON("/api/form", function (data) {
    for (var i = 0, len = data.fields.length; i < len; i++) {
        var field = data.fields[i];
        if (field.name != 'photo')
            $('<div>', {class: "col-xs-6 col-sm-6 col-md-6 form-group"}).append($('<input>').attr({
                type: field.type, name: field.name, required: field.required,
                placeholder: field.description, class: "form-control input-sm", pattern: field.validationRegex
            })).prependTo('form')
        else {
            $('.photo').show();
        }
        $('.panel-body').show();
        $('.loader').hide();
    }
})
$('#submit').click(function () {
    var request = {fields: []};
    $('input').each(function () {
        var input = $(this);
        if (input.attr('name') != 'file') {
            var field = {};
            field["name"] = input.attr('name');
            field["value"] = input.val();
            request.fields.push(field);
        }
    })
    $.ajax({
        type: "POST",
        url: "/api/form",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(request)
    })
        .done(function (response) {
            var messageBox = $('#message-box');
            messageBox.removeClass("hidden alert-danger alert-success");
            messageBox.addClass("alert-success");
            messageBox.html("Success!");

        }).fail(function (response) {
        var messageBox = $('#message-box');
        messageBox.removeClass("hidden alert-danger alert-success");
        messageBox.addClass("alert-danger");

        var message = "Error submitting form!";

        if (response && response.responseJSON && response.responseJSON.Errors) {
            var errors = response.responseJSON.Errors;
            message = "<ul>";

            for (var i = 0; i < errors.length; i++) {
                message += "<li>" + errors[i].Message + "</li>";
            }
            message += "</ul>";
        }
        messageBox.html(message);
    })
})