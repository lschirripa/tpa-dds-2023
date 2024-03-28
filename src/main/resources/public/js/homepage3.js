document.addEventListener('DOMContentLoaded', () => {
    const scrollImage = document.getElementById('scrollImage');
    const images = [
        '/resources/background.jpg',
        '/resources/image-1.jpg',
        '/resources/image-2.jpg',
        '/resources/image-3.jpg'
        // Add more image paths as needed
    ];

    window.addEventListener('scroll', () => {
        const { scrollTop, scrollHeight, clientHeight } = document.documentElement || document.body;

        // Change the background image when scrolling
        const maxScroll = scrollHeight - clientHeight;
        const imageIndex = Math.min(Math.floor(scrollTop / (maxScroll / images.length)), images.length - 1);
        scrollImage.style.backgroundImage = `url(${images[imageIndex]})`;
    });
});
