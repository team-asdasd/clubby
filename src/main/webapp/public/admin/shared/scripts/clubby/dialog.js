window.Clubby = window.Clubby || {};

window.Clubby.Dialog = window.Clubby.Dialog ||
    function () {
        var dialog = '<div class="modal fade" id="diaog-modal" tabindex="-1" role="dialog">' +
            ' <div class="modal-dialog modal-lg" role="document">' +
            ' <div class="modal-content"><div class="modal-header">' +
            '   <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
            '   <h4 class="modal-title"><%= title %></h4>' +
            ' </div>' +
            '   <div class="modal-body">' +
            '       <%= body %>' +
            '   </div>' +
            '   <div class="modal-footer">' +
            '       <button type="button" class="btn btn-default dialog-no">No</button>' +
            '       <button type="button" class="btn btn-primary dialog-yes">Yes</button>' +
            '   </div>' +
            '</div>' +
            '</div>' +
            '</div>';

        return _.template(dialog);
    };





