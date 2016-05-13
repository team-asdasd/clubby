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
    parseFields(data);
}).fail(function () {
    alert("error");
})
function parseFields(data) {
    for (var i = 0, len = data.fields.length; i < len; i++) {
        var field = data.fields[i];
        $('<div>', {class: "col-xs-6 col-sm-6 col-md-6 form-group"}).append($('<input>').attr({
            type: field.type, name: field.name, required: field.required,
            placeholder: field.description, class: "form-control input-sm", pattern: field.validationRegex
        })).prependTo('.form')
    }
}
$('#submit').click(function () {
    $('input').each(function () {
        var input = $(this);
        console.log(input.attr('name'));
        console.log(input.val());
    })
})