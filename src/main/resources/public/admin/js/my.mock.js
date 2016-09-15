
$(function () {
    function mockURL() {
        if (window.location.href.indexOf('minitwit') >= 0) {
            $('a').each(function () {
                var href = $(this).attr('href');

                href = href.replace(/^\/admin/g, '.');

                if (!href.endsWith('.html')) {
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
    })
})