$.myDataTable = function (newOptions) {

    var searchResult = $('.uiSearchResult');
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
    $('uiSearchForm').submit(function(){

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

};


$.myCommonUIEvent = function() {

    $('.uiAddRow').click(showDetail);
    $('.uiGroupCancel').click(hideDetail);

    $('.uiSearchResult').on('click', '.update', function () {

        var $targetForm = $('.uiDetailForm');
        if ($targetForm.length > 1) {
            $targetForm = $($(this).data('targetForm')); // customize the form while multiple form
        }

        var $row = $(this).closest('tr');
        showDetail("update");
        copyInfoToForm($row, $targetForm);
        return false;
    });

    // others
    function copyInfoToForm($row, $detailForm) {
        $('#username', $detailForm).val( $('.the-username', $row).text() );
    }

    function showDetail(mode) {
        $('.uiSearchPage').hide();
        $('.uiDetailPage').show();
        $('.uiDetailForm').resetForm();

        if (mode==='update') {
            $('.uiUpdateGroup').show();
            $('.uiAddGroup').hide()
        } else {
            $('.uiUpdateGroup').hide();
            $('.uiAddGroup').show()
        }
    }
    function hideDetail() {
        $('.uiSearchPage').show();
        $('.uiDetailPage').hide();
    }
};