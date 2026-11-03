(function () {
    "use strict";

    function formatarCnpj(valor) {
        var caracteres = valor.toUpperCase().replace(/[^A-Z0-9]/g, "").slice(0, 14);
        var resultado = caracteres.slice(0, 2);
        if (caracteres.length > 2) resultado += "." + caracteres.slice(2, 5);
        if (caracteres.length > 5) resultado += "." + caracteres.slice(5, 8);
        if (caracteres.length > 8) resultado += "/" + caracteres.slice(8, 12);
        if (caracteres.length > 12) resultado += "-" + caracteres.slice(12, 14);
        return resultado;
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll('input[name="cnpj"]').forEach(function (campo) {
            campo.setAttribute("maxlength", "18");
            campo.setAttribute("autocomplete", "off");
            campo.addEventListener("input", function () {
                campo.value = formatarCnpj(campo.value);
            });
            campo.value = formatarCnpj(campo.value);
        });
    });
})();
