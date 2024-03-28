document.addEventListener('DOMContentLoaded', function() {

    pdfNameGlobal = ''

    function loadPDF(pdfName) {
        const viewerContainer = document.getElementById('pdf-viewer');
        viewerContainer.innerHTML = '';

        const url = `/resources/${pdfName}`;

        pdfNameGlobal = pdfName

        pdfjsLib.getDocument(url).promise.then(function(pdf) {
            return pdf.getPage(1);
        }).then(function(page) {
            const viewport = page.getViewport({ scale: 1 });
            const canvas = document.createElement('canvas');
            const context = canvas.getContext('2d');
            canvas.height = viewport.height;
            canvas.width = viewport.width;
            viewerContainer.appendChild(canvas);

            const renderContext = {
                canvasContext: context,
                viewport: viewport
            };
            page.render(renderContext);
        });
    }

    function downloadPDF() {
        if (pdfNameGlobal) {
            const link = document.createElement('a');
            currentPDFUrl = `/resources/${pdfNameGlobal}`;
            link.href = currentPDFUrl;
            link.download = currentPDFUrl.split('/').pop();
            link.click();
        }
    }


    window.loadPDF = loadPDF;
    window.downloadPDF = downloadPDF;
});