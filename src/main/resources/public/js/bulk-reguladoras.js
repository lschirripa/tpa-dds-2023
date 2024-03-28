function handleFormSubmit(event) {
    event.preventDefault();
    var form = event.target;
    var formData = new FormData(form);

    fetch(form.action, {
        method: form.method,
        body: formData
    })
        .then(async function (response) {
            if (response.status == 422) {
                throw new Error('Por favor, revise el formulario. ' + await response.text());
            }
            else if (!response.ok) {
                throw new Error("Internal server error. " + await response.text());
            }
            return response;
        })
        .then(function() {
            alert('Formulario subido correctamente');
        })
        .catch(function(error) {
            alert(error.message);
        });
}