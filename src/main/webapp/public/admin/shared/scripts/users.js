$(function () {
    $("#create-user-modal #save-user").click(function () {
        var modal = $("#create-user-modal");

        var messageBox = modal.find("#message-box");

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
            })
            .done(function (response) {
                messageBox.removeClass("hidden alert-danger alert-success");
                messageBox.addClass("alert-success");
                messageBox.html("Successfully created new user!");

                username.val("");
                email.val("");
                firstName.val("");
                lastName.val("");
                pass.val("");
                pass2.val("");
            })
            .fail(function (response) {
                messageBox.removeClass("hidden alert-danger alert-success");
                messageBox.addClass("alert-danger");

                var message = "Error creating user!";

                if (response && response.responseJSON && response.responseJSON.Errors) {
                    var errors = response.responseJSON.Errors;
                    message = "<ul>";

                    for (var i = 0; i < errors.length; i++) {
                        message += "<li>" + errors[i].Message + "</li>";
                    }

                    message += "</ul>";
                }

                messageBox.html(message);
            });
    });
});