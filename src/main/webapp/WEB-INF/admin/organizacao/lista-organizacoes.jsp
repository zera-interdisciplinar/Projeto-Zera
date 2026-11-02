<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organizações - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
</head>

<body>
<div class="tela-auth">
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${ctx}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
        </div>
    </aside>

    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <h1 class="nome-da-area">Organizações</h1>

        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault()">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input id="buscar" name="buscar" type="search"
                           placeholder="Nome, CNPJ ou email">
                    <button type="button" class="btn-lupa-busca" onclick="filtrar()" aria-label="Buscar">
                        <img src="${ctx}/images/icone-lupa.svg" alt="Buscar">
                    </button>
                </div>

                <div class="filtrador">
                    <label for="cadastro">Tempo</label>
                    <select id="cadastro" name="cadastro">
                        <option value="todos">Todos</option>
                        <option value="30">Últimos 30 dias</option>
                        <option value="90">Últimos 90 dias</option>
                    </select>
                </div>

                <a href="${ctx}/organizacao/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    <span>Nova organização</span>
                </a>
            </form>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>Email</th>
                        <th>Cadastrada em</th>
                        <th class="col-acoes-th"></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="org" items="${organizacoes}">
                        <tr class="linha-organizacao"
                            data-cadastro="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : ''}">

                            <td><c:out value="${org.nome}"/></td>

                            <td class="col-cnpj">
                                <c:choose>
                                    <c:when test="${fn:length(org.cnpj) == 14}">
                                        ${fn:substring(org.cnpj,0,2)}.${fn:substring(org.cnpj,2,5)}.${fn:substring(org.cnpj,5,8)}/${fn:substring(org.cnpj,8,12)}-${fn:substring(org.cnpj,12,14)}
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${org.cnpj}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td><c:out value="${org.email}"/></td>
                            <td>
                                <c:out value="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : 'Não informada'}"/>
                            </td>

                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="${ctx}/organizacao/perfil?codigo=${org.codigo}&ver=true"
                                       class="btn-icone"
                                       title="Visualizar">
                                        <img src="${ctx}/images/icone-ver.svg" alt="Visualizar">
                                    </a>

                                    <a href="${ctx}/organizacao/perfil?codigo=${org.codigo}"
                                       class="btn-icone"
                                       title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>

                                    <form method="post" action="${ctx}/organizacao/excluir" class="form-excluir">
                                        <input type="hidden" name="codigo" value="${org.codigo}">
                                        <button type="button"
                                                class="btn-icone zera-abrir-exclusao"
                                                title="Excluir organização"
                                                aria-label="Excluir organização">
                                            <img src="${ctx}/images/icone-excluir.svg" alt="Excluir">
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <tr id="sem-resultados" hidden>
                        <td colspan="5" class="sem-dados">Nenhuma organização encontrada.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<div id="zera-modal-exclusao" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true" aria-labelledby="zera-exclusao-titulo" aria-describedby="zera-exclusao-descricao">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-exclusao" aria-label="Fechar"></button>
        <h2 id="zera-exclusao-titulo">Deseja mesmo excluir?</h2>
        <p id="zera-exclusao-descricao" class="zera-modal-descricao">
            Essa ação excluirá a organização e todos os dados relacionados a ela.
        </p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-exclusao">Voltar</button>
            <button type="button" class="zera-modal-confirmar" id="zera-confirmar-exclusao">Excluir</button>
        </div>
    </div>
</div>

<div id="zera-modal-sair" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true" aria-labelledby="zera-sair-titulo" aria-describedby="zera-sair-descricao">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-sair" aria-label="Fechar"></button>
        <h2 id="zera-sair-titulo">Deseja realmente sair?</h2>
        <p id="zera-sair-descricao" class="zera-modal-descricao">
            Você será desconectado da sua conta de administrador.
        </p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-sair">Voltar</button>
            <a href="${ctx}/logout" class="zera-modal-confirmar" id="zera-confirmar-sair" style="text-decoration:none; display:inline-flex; align-items:center; justify-content:center;">Sair</a>
        </div>
    </div>
</div>

<script>
    const busca = document.getElementById('buscar');
    const cadastro = document.getElementById('cadastro');
    const linhas = [...document.querySelectorAll('.linha-organizacao')];
    const aviso = document.getElementById('sem-resultados');

    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        const dias = cadastro.value;
        let visiveis = 0;

        linhas.forEach((linha) => {
            const ok = linha.textContent.toLowerCase().includes(termo);
            let dataOk = true;

            if (dias !== 'todos') {
                const data = linha.dataset.cadastro;
                const limite = new Date();
                limite.setHours(0, 0, 0, 0);
                limite.setDate(limite.getDate() - (dias === '30' ? 30 : 90));
                dataOk = !!data && new Date(data + 'T00:00:00') >= limite;
            }

            linha.hidden = !(ok && dataOk);
            if (!linha.hidden) visiveis++;
        });

        aviso.hidden = visiveis !== 0;
    }

    busca?.addEventListener('input', filtrar);
    cadastro?.addEventListener('change', filtrar);
    filtrar();

    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });

    (() => {
        const modalExcluir = document.getElementById("zera-modal-exclusao");
        const cancelarExcluir = document.getElementById("zera-cancelar-exclusao");
        const fecharExcluir = document.getElementById("zera-fechar-exclusao");
        const confirmarExcluir = document.getElementById("zera-confirmar-exclusao");

        let formExclusao = null;

        function abrirModal(botao) {
            formExclusao = botao.closest("form");
            confirmarExcluir.disabled = false;
            modalExcluir.hidden = false;
            document.body.style.overflow = "hidden";
            cancelarExcluir.focus();
        }

        function fecharModal() {
            modalExcluir.hidden = true;
            document.body.style.overflow = "";
            formExclusao = null;
        }

        document.querySelectorAll(".zera-abrir-exclusao").forEach(btn => {
            btn.addEventListener("click", () => abrirModal(btn));
        });

        cancelarExcluir?.addEventListener("click", fecharModal);
        fecharExcluir?.addEventListener("click", fecharModal);

        modalExcluir?.addEventListener("click", e => {
            if (e.target === modalExcluir) fecharModal();
        });

        confirmarExcluir?.addEventListener("click", () => {
            if (formExclusao) {
                confirmarExcluir.disabled = true;
                formExclusao.submit();
            }
        });
    })();

    (() => {
        const modalSair = document.getElementById("zera-modal-sair");
        const cancelarSair = document.getElementById("zera-cancelar-sair");
        const fecharSair = document.getElementById("zera-fechar-sair");
        const linkSairMenu = document.querySelector(".painel-esquerda nav a[href*='logout'], .painel-esquerda nav a.botao-sair");

        function abrirModalSair(e) {
            if (e) e.preventDefault();
            modalSair.hidden = false;
            document.body.style.overflow = "hidden";
            cancelarSair.focus();
        }

        function fecharModalSair() {
            modalSair.hidden = true;
            document.body.style.overflow = "";
        }

        if (linkSairMenu) {
            linkSairMenu.addEventListener("click", abrirModalSair);
        }

        cancelarSair?.addEventListener("click", fecharModalSair);
        fecharSair?.addEventListener("click", fecharModalSair);

        modalSair?.addEventListener("click", e => {
            if (e.target === modalSair) fecharModalSair();
        });
    })();
</script>

<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>