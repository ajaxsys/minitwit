$.myDataTable = function (newOptions) {

    var searchResult = $('#searchResult');
    var defaultOption = {
        // Use bootstrap customize: https://datatables.net/examples/advanced_init/dom_multiple_elements.html
        // "dom" : '<"row"<"col-sm-4"i><"col-sm-2"l><"col-sm-6 text-right"p>> <"row"<"col-sm-12"t>>',
        "dom" : '<"row"<"col-sm-6 text-left"i><"col-sm-6 text-right"p>><"row"<"col-sm-12"t>>',
        // use data-* tag instead
        /* "columnDefs": [
         { "name": "username", "targets": 0 },
         { "name": "text",     "targets": 1 },
         { "name": "time",     "targets": 2 },
         { "name": "email",    "targets": 3, "visible": false, },
         ],*/
        // Just for test bellow
        'processing': true,
        'serverSide': true,
        'ajax': searchResult.data('url'),
        'ordering': false,
        'info': true, // default: true
        'searching': true, // WARN: will display search box, also not post search parameter to server!
        'lengthChange': true,  // default: 10 25 100 show
        "displayLength": 10,// default: 10
        'language': {
            // "url": "//cdn.datatables.net/plug-ins/1.10.12/i18n/Japanese.json"
            "url": "datatables/1.10.12/i18n/Japanese.json"
        }
    };
    var options = $.extend({}, defaultOption, newOptions);


    var $table = searchResult.DataTable(options);
    $('#searchForm').submit(function(){

        $(':input', this).each(function(){
            var val = $.trim($(this).val());
            $table.
            column(
                $(this).attr('data-name') + ':name').
            search(
                val,
                false, // smart search: https://datatables.net/examples/api/regex.html
                false);
        });

        $table.draw();
        return false;
    });

}