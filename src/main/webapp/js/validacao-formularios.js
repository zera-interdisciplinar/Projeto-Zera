(function () {
    "use strict";

    function mensagem(input) {
        var nome = (input.getAttribute("name") || input.id || "campo").toLowerCase();
        var valor = input.value.trim();
        if (input.validity.valueMissing || (input.required && !valor)) return "Preencha este campo.";
        if (input.validity.typeMismatch && input.type === "email") return "Email inválido, ele precisa conter @ e o domínio.";
        if (input.validity.typeMismatch) return "Informe um valor válido.";
        if (input.validity.tooShort) return "Informe pelo menos " + input.minLength + " caracteres.";
        if (input.validity.tooLong) return "O valor ultrapassa o limite permitido.";
        if (input.validity.patternMismatch) return "O formato informado é inválido.";
        if (input.validity.rangeUnderflow || input.validity.rangeOverflow || input.validity.stepMismatch || input.validity.badInput) return "Confira o valor informado.";
        if (nome.indexOf("confirm") >= 0) {
            var senha = input.form.querySelector('[name="senha"],[name="novaSenha"]');
            if (senha && senha.value !== input.value) return "A confirmação está diferente da senha informada.";
        }
        if (input.type === "email" && valor && !/^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$/.test(valor)) return "Email inválido, ele precisa conter @ e o domínio.";
        if (nome.indexOf("telefone") >= 0 && valor && input.value.replace(/\\D/g, "").length !== 11) return "Telefone inválido, ele precisa conter o DDD + nove números.";
        if (nome === "senha" && input.form.closest(".card") && valor && (!/^(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$/.test(valor))) return "A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.";
        return "";
    }

    function container(input) {
        return input.closest(".campo, .campo-grupo, .filtrador") || input.parentElement;
    }
    function marcar(input, texto) {
        var grupo = container(input);
        if (!grupo) return;
        var id = "zera-erro-" + (input.id || input.name || "campo").replace(/[^a-zA-Z0-9_-]/g,"-");
        var aviso = grupo.querySelector('[data-zera-erro="' + id + '"]');
        if (texto && !aviso) {
            aviso = document.createElement("span");
            aviso.className = "zera-mensagem-validacao";
            aviso.dataset.zeraErro = id;
            aviso.id = id;
            aviso.setAttribute("role", "alert");
            grupo.appendChild(aviso);
        }
        grupo.classList.toggle("zera-validacao-erro", Boolean(texto));
        input.setAttribute("aria-invalid", texto ? "true" : "false");
        if (texto) {
            aviso.textContent = texto;
            aviso.hidden = false;
            input.setAttribute("aria-describedby", id);
        } else {
            if (aviso) aviso.hidden = true;
            if (input.getAttribute("aria-describedby") === id) input.removeAttribute("aria-describedby");
        }
    }
    function controles(form) {
        return Array.from(form.querySelectorAll("input:not([type=hidden]):not([type=submit]):not([type=button]),select,textarea")).filter(function (campo) {
            return !campo.disabled && campo.type !== "checkbox" && campo.type !== "radio" && campo.type !== "file";
        });
    }
    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("form").forEach(function (form) {
            var campos = controles(form);
            if (!campos.length) return;
            form.noValidate = true;
            campos.forEach(function (campo) {
                campo.addEventListener("input", function () {
                    if (campo.getAttribute("aria-invalid") === "true") marcar(campo, mensagem(campo));
                });
                campo.addEventListener("blur", function () {
                    if (campo.value.trim() || campo.getAttribute("aria-invalid") === "true") marcar(campo, mensagem(campo));
                });
            });
            form.addEventListener("submit", function (evento) {
                var primeiro = null;
                campos.forEach(function (campo) {
                    var erro = mensagem(campo);
                    marcar(campo, erro);
                    if (erro && !primeiro) primeiro = campo;
                });
                if (primeiro) {
                    evento.preventDefault();
                    evento.stopImmediatePropagation();
                    primeiro.focus();
                }
            }, true);
        });
    });
})();
