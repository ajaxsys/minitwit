var USE_MOCK=true;

$(function () {
    function mockURL() {
        if (!USE_MOCK) {
            return;
        }

        if (window.location.href.indexOf('minitwit') >= 0) {
            $('a').each(function () {
                var href = $(this).attr('href');

                href = href.replace(/^\/admin/g, '.');

                if (!href.endsWith('.html') && !href.endsWith('#')) {
                    href += '.html';
                }

                $(this).attr('href', href);
            })
        }
    }

    function addHoverEffectToMenu(menu) {
        //Add Hover effect to menus
        $('ul.nav li.dropdown', menu).hover(function() {
            $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn();
        }, function() {
            $(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut();
        });
    }

    $('div[include]').each(function () {
        $this = $(this);
        $this.load($this.attr('include'), function () {
            mockURL();
            addHoverEffectToMenu($this);
        })
    });

    // mock ajax
    $.mockjax({
        url: "/admin/admin/list",
        responseText: genMockData("umtk", 10)
    });
    $.mockjax({
        url: "/admin/hosa/list",
        responseText: genMockData("uintk", 10)
    });


    // formatStr:
    // u - user; m - message text; t - timestamp; e - email; i - ID; n - a number; k - 許可、拒否
    function genMockData (formatStr, number) {
        var rows = [];
        for (var n = 0; n < number; n++) {
            var row = [];

            for (var i = 0; i < formatStr.length; i++) {

                var c = formatStr.charAt(i);
                switch (c){
                    case 'u':
                        row.push('保サ　太郎' + n);
                        break;
                    case 'm':
                        row.push('太郎からのメッセージテキスト' + n);
                        break;
                    case 't':
                        row.push(formatYMDHMS(new Date(2016, 1, 1, 1, 1, 1 + n)));
                        break;
                    case 'e':
                        row.push('email_'+n+'@gmail.com');
                        break;
                    case 'i':
                        row.push('ID_'+n);
                        break;
                    case 'n':
                        row.push(n);
                        break;
                    case 'k':
                        row.push('許可');
                        break;
                }
            }
            rows.push(row);
        }

        // iTotalRecords = total records without any filtering/limits
        // iTotalDisplayRecords = filtered result count
        return  {
            "iTotalRecords":number * 10,
            "iTotalDisplayRecords":number * 10,
            "aaData": rows
        }
    };


    function formatYMDHMS(date) {
        var dateFormat = {
            fmt : {
                "yyyy": function(date) { return date.getFullYear() + ''; },
                "MM": function(date) { return ('0' + (date.getMonth() + 1)).slice(-2); },
                "dd": function(date) { return ('0' + date.getDate()).slice(-2); },
                "hh": function(date) { return ('0' + date.getHours()).slice(-2); },
                "mm": function(date) { return ('0' + date.getMinutes()).slice(-2); },
                "ss": function(date) { return ('0' + date.getSeconds()).slice(-2); }
            },
            format:function dateFormat (date, format) {
                var result = format;
                for (var key in this.fmt)
                    result = result.replace(key, this.fmt[key](date));
                return result;
            }
        };

        return dateFormat.format(date, 'yyyyMMdd hh:mm:ss');
    }


})