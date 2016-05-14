$.cloudinary.config({cloud_name: 'teamasdasd'})
function insertPhotoField() {
    $("div[class^='uploadinput']").append($.cloudinary.unsigned_upload_tag("syjxlwur"))
        .bind('cloudinarydone', function (e, data) {
                $('.thumbnail').append($.cloudinary.image(data.result.public_id,
                    {
                        width: 100, height: 100,
                        crop: 'thumb', gravity: 'face', radius: 'max'
                    }))
                $('#photo').attr('value', data.result.url);
                $('.photo-input').hide(200);
            }
        )
}

$.getJSON("/api/form", function (data) {
    for (var i = 0, len = data.fields.length; i < len; i++) {
        var field = data.fields[i];
        var classType = "form-group";
        if (field.required) classType = classType.concat(" required");
        if (field.name != 'photo')
            $('<div>', {class: classType}).append($('<label>').attr({
                for: field.name,
                class: "col-sm-2 control-label"
            }).text(field.description)).append($('<div>').attr({class: 'col-sm-10'})
                .append($('<input>').attr({
                    type: field.type, name: field.name, class: "form-control", required: field.required,
                    placeholder: field.description, pattern: field.validationRegex
                }))).prependTo('form')
        else if (field.visible) {
            insertPhotoField();
            $('.photo').show();
        }
        $('.panel-body').show();
        $('.loader').hide();
    }
})
$('#submit').click(function () {
    var request = {fields: []};
    var isValid = true
    $('input').each(function () {
        var input = $(this);
        if(input.attr('required') != null && input.val() === ""){
            isValid = false;
            return
        }
        if (input.attr('name') != 'file') {
            var field = {};
            field["name"] = input.attr('name');
            field["value"] = input.val();
            request.fields.push(field);
        }
    })
    if(!isValid) return false;
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