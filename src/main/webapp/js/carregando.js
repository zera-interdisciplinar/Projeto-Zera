
"use strict";

function mostrarCarregamento() {
    const tela = document.getElementById("tela-carregamento");
    if (tela) tela.hidden = false;
}

function esconderCarregamento() {
    const tela = document.getElementById("tela-carregamento");
    if (tela) tela.hidden = true;
}

window.addEventListener("pageshow", esconderCarregamento);
window.addEventListener("load", esconderCarregamento);

document.addEventListener("submit", function(event) {
    const formulario = event.target;

    if (!(formulario instanceof HTMLFormElement)) return;
    if (!formulario.checkValidity()) return;
    if (formulario.target && formulario.target !== "_self") return;
    if (formulario.dataset.semCarregamento !== undefined) return;

    setTimeout(function() {
        if (!event.defaultPrevented) {
            mostrarCarregamento();
        }
    }, 0);
});
