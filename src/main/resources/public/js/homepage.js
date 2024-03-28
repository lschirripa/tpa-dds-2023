$(document).ready(function() {
    $(window).scroll(function() {
        // Check the scroll position
        var scrollPos = $(window).scrollTop();

        // Define the position at which each section appears
        var introPos = $('.intro').offset().top;
        var chapter1Pos = $('.chapter-1').offset().top;
        var chapter2Pos = $('.chapter-2').offset().top;

        // Show/hide sections based on scroll position
        if (scrollPos >= introPos) {
            // Display intro section
            $('.intro').addClass('show');
        } else {
            $('.intro').removeClass('show');
        }

        if (scrollPos >= chapter1Pos) {
            // Display chapter 1
            $('.chapter-1').addClass('show');
        } else {
            $('.chapter-1').removeClass('show');
        }

    });
});
