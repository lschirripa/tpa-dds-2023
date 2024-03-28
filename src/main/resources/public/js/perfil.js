document.addEventListener("DOMContentLoaded", function () {
    const editarBotones = document.querySelectorAll(".editar-btn");
    const popup = document.getElementById("editar-popup");
    const nuevoValor = document.getElementById("nuevoValor");
    const guardarEdicion = document.getElementById("guardarEdicion");
    const cerrarPopup = document.getElementById("cerrarPopup");

    editarBotones.forEach(function (boton) {
        boton.addEventListener("click", function () {
            nuevoValor.value = boton.previousElementSibling.textContent;
            popup.style.display = "block";
        });
    });

    cerrarPopup.addEventListener("click", function () {
        popup.style.display = "none";
    });

    guardarEdicion.addEventListener("click", function () {
        const valorEditado = nuevoValor.value;
        // Realiza la lógica para guardar el valor editado aquí
        // Luego, actualiza el valor en la página y cierra la ventana emergente
        popup.style.display = "none";
    });
});
