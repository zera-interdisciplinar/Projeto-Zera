<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="plano"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Planos - Zera</title>
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

        <h1 class="nome-da-area">Planos</h1>

        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault()">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input type="search" name="buscar" id="buscar"
                           placeholder="Código, nome ou valor">
                    <button type="button" class="btn-lupa-busca" onclick="filtrar()" aria-label="Buscar">
                        <img src="${ctx}/images/icone-lupa.svg" alt="Buscar">
                    </button>
                </div>

                <div class="filtrador">
                    <label for="filtroTempo">Tempo</label>
                    <select id="filtroTempo" name="tempo">
                        <option value="todos">Todos</option>
                        <option value="mensal">Mensal</option>
                        <option value="anual">Anual</option>
                    </select>
                </div>

                <a href="${ctx}/plano/novo" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    <span>Novo plano</span>
                </a>
            </form>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>Nome</th>
                        <th>Valor</th>
                        <th>Período</th>
                        <th class="col-acoes-th"></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${not empty planos}">
                            <c:forEach var="item" items="${planos}">
                                <%-- Tradução do valor vindo do backend (1 mês/1 ano) --%>
                                <c:set var="periodoExibicao" value="${item.tempo}"/>
                                <c:set var="tempoNormalizado" value="${fn:toLowerCase(item.tempo)}"/>
                                <c:choose>
                                    <c:when test="${fn:contains(tempoNormalizado, 'mês') or fn:contains(tempoNormalizado, 'mes') or tempoNormalizado == '1'}">
                                        <c:set var="periodoExibicao" value="Mensal"/>
                                        <c:set var="dataTag" value="mensal"/>
                                    </c:when>
                                    <c:when test="${fn:contains(tempoNormalizado, 'ano') or tempoNormalizado == '12'}">
                                        <c:set var="periodoExibicao" value="Anual"/>
                                        <c:set var="dataTag" value="anual"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="dataTag" value="${tempoNormalizado}"/>
                                    </c:otherwise>
                                </c:choose>

                                <tr class="linha-plano" data-tempo="${dataTag}">
                                    <td><c:out value="${item.codigo}"/></td>
                                    <td><c:out value="${item.nome}"/></td>
                                    <td><c:out value="${item.valor}"/></td>
                                    <td><c:out value="${periodoExibicao}"/></td>

                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/plano/ver?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Visualizar">
                                                <img src="${ctx}/images/icone-ver.svg" alt="Visualizar">
                                            </a>

                                            <a href="${ctx}/plano/editar?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Editar">
                                                <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                            </a>

                                            <form action="${ctx}/plano/excluir" method="post" class="form-excluir">
                                                <input type="hidden" name="codigo" value="${item.codigo}">
                                                <button type="button"
                                                        class="btn-icone zera-abrir-exclusao"
                                                        title="Excluir plano"
                                                        aria-label="Excluir plano">
                                                    <img src="${ctx}/images/icone-excluir.svg" alt="Excluir">
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="sem-dados">Nenhum plano cadastrado.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    <tr id="sem-resultados" hidden>
                        <td colspan="5" class="sem-dados">Nenhum plano encontrado.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<div id="zera-modal-exclusao" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-exclusao" aria-label="Fechar"></button>
        <h2>Deseja mesmo excluir?</h2>
        <p class="zera-modal-descricao">Essa ação excluirá o plano selecionado e não poderá ser desfeita.</p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-exclusao">Voltar</button>
            <button type="button" class="zera-modal-confirmar" id="zera-confirmar-exclusao">Excluir</button>
        </div>
    </div>
</div>

<script>
    const busca = document.getElementById('buscar');
    const filtroTempo = document.getElementById('filtroTempo');
    const linhas = [...document.querySelectorAll('.linha-plano')];
    const aviso = document.getElementById('sem-resultados');

    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        const tempoSelecionado = filtroTempo.value.toLowerCase();
        let visiveis = 0;

        linhas.forEach((linha) => {
            const conteudoLinha = linha.textContent.toLowerCase();
            const tempoLinha = (linha.dataset.tempo || '').toLowerCase();

            const textoOk = conteudoLinha.includes(termo);
            const tempoOk = tempoSelecionado === 'todos' || tempoLinha === tempoSelecionado;

            linha.hidden = !(textoOk && tempoOk);
            if (!linha.hidden) visiveis++;
        });

        if (aviso) aviso.hidden = (visiveis !== 0 || linhas.length === 0);
    }

    busca?.addEventListener('input', filtrar);
    filtroTempo?.addEventListener('change', filtrar);

    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-aberto'));
        sel.addEventListener('blur', () => sel.classList.remove('select-aberto'));
        sel.addEventListener('change', () => sel.classList.remove('select-aberto'));
    });
</script>
<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>