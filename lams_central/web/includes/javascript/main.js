$(document).ready(function () {

    // handler for offcanvas bar minimize button
    $('.offcanvas-toggle').on('click', function () {
        $("body").toggleClass("offcanvas-hidden");
    });
    function hideOffcanvasBar() {
    	$("body").removeClass("offcanvas-hidden");
    }

    //make offcanvas bar scrollable
    $(window).bind("load", function () {
    	$('.offcanvas-scroll-area').slimScroll({
           height: '100%',
           railOpacity: 0.9
        });
    });

    // Full height of offcanvas bar
    function adjustOffcanvasBarHeight() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".offcanvasd-panel").css("min-height", heightWithoutNavbar + "px");

        var navbarHeigh = $('#offcanvas').height();
        var wrapperHeigh = $('#page-wrapper').height();

        if (navbarHeigh > wrapperHeigh) {
            $('#page-wrapper').css("min-height", navbarHeigh + "px");
        }

        if (navbarHeigh < wrapperHeigh) {
            $('#page-wrapper').css("min-height", $(window).height() + "px");
        }
    }

    $(window).bind("load resize scroll", function () {
    	adjustOffcanvasBarHeight();
    });
    adjustOffcanvasBarHeight();

});
