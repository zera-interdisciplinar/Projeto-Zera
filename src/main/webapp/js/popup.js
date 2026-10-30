
(() => {
    "use strict";

    let overlay = null;
    let titulo = null;
    let descricao = null;
    let botaoFechar = null;
    let botaoVoltar = null;
    let botaoConfirmar = null;

    let gatilhoAtual = null;
    let overflowAnterior = "";

    function criarModal() {
        if (overlay) return;

        overlay = document.createElement("div");
        overlay.className = "zera-modal-overlay";
        overlay.hidden = true;

        overlay.innerHTML = `
            <div
                class="zera-modal-caixa"
                role="dialog"
                aria-modal="true"
                aria-labelledby="zera-modal-titulo"
                aria-describedby="zera-modal-descricao"
            >
                <button
                    type="button"
                    class="zera-modal-fechar"
                    aria-label="Fechar"
                >
                    &times;
                </button>

                <h2 id="zera-modal-titulo"></h2>

                <p
                    class="zera-modal-descricao"
                    id="zera-modal-descricao"
                ></p>

                <div class="zera-modal-botoes">
                    <button
                        type="button"
                        class="zera-modal-voltar"
                    >
                        Voltar
                    </button>

                    <button
                        type="button"
                        class="zera-modal-confirmar"
                    >
                        Excluir
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(overlay);

        titulo = overlay.querySelector("#zera-modal-titulo");
        descricao = overlay.querySelector("#zera-modal-descricao");

        botaoFechar = overlay.querySelector(".zera-modal-fechar");
        botaoVoltar = overlay.querySelector(".zera-modal-voltar");
        botaoConfirmar = overlay.querySelector(".zera-modal-confirmar");

        botaoFechar.addEventListener("click", fecharModal);
        botaoVoltar.addEventListener("click", fecharModal);
        botaoConfirmar.addEventListener("click", confirmarModal);

        overlay.addEventListener("click", (evento) => {
            if (evento.target === overlay) {
                fecharModal();
            }
        });
    }

    function abrirModal(gatilho) {
        criarModal();

        gatilhoAtual = gatilho;

        const tipo = gatilho.dataset.excluirTipo || "exclusao";
        const ehSaida = tipo === "sair";

        titulo.textContent = gatilho.dataset.excluirTitulo ||
            (ehSaida ? "Deseja mesmo sair?" : "Deseja mesmo excluir?");

        descricao.textContent = gatilho.dataset.excluirDescricao ||
            (ehSaida
                ? "Você precisará entrar novamente para acessar seu painel."
                : "Essa ação pode levar à exclusão de outros itens relacionados.");

        botaoVoltar.textContent = ehSaida ? "Cancelar" : "Voltar";
        botaoConfirmar.textContent = ehSaida ? "Sair" : "Excluir";

        overflowAnterior = document.body.style.overflow;
        document.body.style.overflow = "hidden";

        overlay.hidden = false;
        botaoFechar.focus();
    }

    function fecharModal() {
        if (!overlay || overlay.hidden) return;

        overlay.hidden = true;
        document.body.style.overflow = overflowAnterior;

        const gatilhoAnterior = gatilhoAtual;
        gatilhoAtual = null;

        if (gatilhoAnterior && gatilhoAnterior.isConnected) {
            gatilhoAnterior.focus();
        }
    }

    function confirmarModal() {
        if (!gatilhoAtual) return;

        const gatilho = gatilhoAtual;
        const tipo = gatilho.dataset.excluirTipo || "exclusao";
        const id = gatilho.dataset.excluirId || "";

        if (tipo === "sair") {
            const destino = gatilho.getAttribute("href") || "log.html";

            fecharModal();

            window.location.href = destino;
            return;
        }

        fecharModal();

        document.dispatchEvent(
            new CustomEvent("zera:confirmar-exclusao", {
                detail: {
                    id,
                    tipo,
                    botao: gatilho
                }
            })
        );
    }

    document.addEventListener("click", (evento) => {
        if (!(evento.target instanceof Element)) return;

        const gatilho = evento.target.closest(
            "[data-modal-exclusao]"
        );

        if (!gatilho) return;

        evento.preventDefault();
        abrirModal(gatilho);
    });

    document.addEventListener("keydown", (evento) => {
        if (!overlay || overlay.hidden) return;

        if (evento.key === "Escape") {
            evento.preventDefault();
            fecharModal();
            return;
        }

        if (evento.key !== "Tab") return;

        const controles = [
            botaoFechar,
            botaoVoltar,
            botaoConfirmar
        ];

        const primeiro = controles[0];
        const ultimo = controles[controles.length - 1];

        if (
            evento.shiftKey &&
            document.activeElement === primeiro
        ) {
            evento.preventDefault();
            ultimo.focus();
        } else if (
            !evento.shiftKey &&
            document.activeElement === ultimo
        ) {
            evento.preventDefault();
            primeiro.focus();
        }
    });
})();
