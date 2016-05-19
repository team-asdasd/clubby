window.Clubby = window.Clubby || {};

window.Clubby.Alert = window.Clubby.Alert ||
    function () {
        var alert = '<div class="alert alert-<%= severity %> alert-dismissible" role="alert">' +
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '<span aria-hidden="true">&times;</span></button>' +
            '<strong><%= title %></strong> <%= message %>' +
            '</div>';

        return _.template(alert);
    };





