$(document).ready(function() {
    $('#frameToDetach').load(function() {
        $(this).contents().find('#btnToRemoveFrame').click(function() {
            $('#frameToDetach').remove();
        });
    });
});