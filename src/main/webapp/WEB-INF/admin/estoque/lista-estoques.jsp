
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="estoque"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estoques - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">

    <style>
        /* ORGANIZAÇÃO DOS FILTROS */

        .tabela-filtros.estoques-filtros {
            display: flex !important;
            flex-direction: column !important;
            align-items: stretch !important;
            gap: 18px !important;
            width: 100% !important;
            margin-bottom: 30px !important;
            box-sizing: border-box;
        }

        /* BOTÃO NOVO ESTOQUE EM CIMA */

        .estoques-filtros .acoes-filtros {
            order: -1;
            display: flex;
            justify-content: flex-end;
            width: 100%;
            margin-bottom: 10px;
        }

        .estoques-filtros .acoes-filtros .adicionar {
            display: inline-flex !important;
            align-items: center !important;
            justify-content: center !important;
            min-width: 190px !important;
            height: 53px !important;
            padding: 0 20px !important;
            margin: 0 !important;
        }

        /* CAMPOS DOS FILTROS EMBAIXO */

        .estoques-filtros .campos-filtros {
            order: 1;
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 18px;
            width: 100%;
        }

        .estoques-filtros .filtrador,
        .estoques-filtros .filtrador.busca {
            width: 100% !important;
            min-width: 0 !important;
            max-width: none !important;
            margin: 0 !important;
        }

        /* RESPONSIVIDADE */

        @media (max-width: 1300px) {
            .estoques-filtros .campos-filtros {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 650px) {
            .estoques-filtros .campos-filtros {
                grid-template-columns: 1fr;
            }

            .estoques-filtros .acoes-filtros .adicionar {
                width: 100% !important;
            }
        }
    </style>
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

        <h1 class="nome-da-area">Estoques</h1>

        <section class="tabela">

            <%-- FILTROS AUTOMÁTICOS DOS ESTOQUES --%>
            <form class="tabela-filtros estoques-filtros"
                  id="formulario-filtros"
                  method="get"
                  action="${ctx}/estoque/lista">

                <div class="campos-filtros">

                    <%-- BUSCA POR CÓDIGO OU ESPAÇO --%>
                    <div class="filtrador busca">
                        <label for="buscar">Buscar</label>

                        <input id="buscar"
                               name="buscar"
                               type="search"
                               value="${fn:escapeXml(param.buscar)}"
                               placeholder="Código ou espaço"
                               autocomplete="off">

                        <button type="submit"
                                class="btn-lupa-busca"
                                aria-label="Buscar">
                            <img src="${ctx}/images/icone-lupa.svg" alt="">
                        </button>
                    </div>

                    <%-- FILTRO POR ORGANIZAÇÃO --%>
                    <div class="filtrador">
                        <label for="codOrganizacao">Organização</label>

                        <select id="codOrganizacao" name="codOrganizacao">
                            <option value="">Todas</option>

                            <c:forEach var="org" items="${organizacoes}">
                                <option value="${org.codigo}"
                                    ${param.codOrganizacao == org.codigo ? 'selected' : ''}>
                                    <c:out value="${org.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- FILTRO POR UNIDADE --%>
                    <div class="filtrador">
                        <label for="codUnidade">Unidade (CNPJ)</label>

                        <select id="codUnidade" name="codUnidade">
                            <option value="">Todas</option>

                            <c:forEach var="uni" items="${unidades}">
                                <option value="${uni.codigo}"
                                    ${param.codUnidade == uni.codigo ? 'selected' : ''}>
                                    <c:out value="${uni.cnpj}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- FILTRO POR RESPONSÁVEL --%>
                    <div class="filtrador">
                        <label for="codResponsavel">Responsável</label>

                        <select id="codResponsavel" name="codResponsavel">
                            <option value="">Todos</option>

                            <c:forEach var="gestor" items="${gestores}">
                                <option value="${gestor.codigo}"
                                    ${param.codResponsavel == gestor.codigo ? 'selected' : ''}>
                                    <c:out value="${gestor.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <%-- BOTÃO DE CADASTRO --%>
                <div class="acoes-filtros">
                    <a href="${ctx}/estoque/perfil" class="adicionar">
                        <img src="${ctx}/images/icone-adicionar.svg" alt="">
                        <span>Novo estoque</span>
                    </a>
                </div>
            </form>

            <%-- LISTAGEM DE ESTOQUES --%>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>Espaço na unidade</th>
                        <th>Unidade (CNPJ)</th>
                        <th>Responsável</th>
                        <th class="col-acoes-th"></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${not empty estoques}">
                            <c:forEach var="item" items="${estoques}">
                                <tr>
                                    <td>
                                        <c:out value="${item.codigo}"/>
                                    </td>

                                    <td>
                                        <c:out value="${item.espacoNaUnidade}"/>
                                    </td>

                                    <td>
                                        <c:set var="unidadeExibida"
                                               value="${item.codUnidade}"/>

                                        <c:forEach var="uni" items="${unidades}">
                                            <c:if test="${uni.codigo == item.codUnidade}">
                                                <c:set var="unidadeExibida"
                                                       value="${uni.cnpj}"/>
                                            </c:if>
                                        </c:forEach>

                                        <c:out value="${unidadeExibida}"/>
                                    </td>

                                    <td>
                                        <c:set var="responsavelExibido"
                                               value="${item.codResponsavel}"/>

                                        <c:forEach var="gestor" items="${gestores}">
                                            <c:if test="${gestor.codigo == item.codResponsavel}">
                                                <c:set var="responsavelExibido"
                                                       value="${gestor.nome}"/>
                                            </c:if>
                                        </c:forEach>

                                        <c:out value="${responsavelExibido}"/>
                                    </td>

                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/estoque/perfil?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Editar estoque"
                                               aria-label="Editar estoque">
                                                <img src="${ctx}/images/icone-editar.svg" alt="">
                                            </a>

                                            <form method="post"
                                                  action="${ctx}/estoque/excluir"
                                                  class="form-excluir">

                                                <input type="hidden"
                                                       name="codigo"
                                                       value="${item.codigo}">

                                                <button type="button"
                                                        class="btn-icone zera-botao-excluir"
                                                        data-modal-exclusao
                                                        data-excluir-tipo="estoque"
                                                        data-excluir-id="${item.codigo}"
                                                        data-excluir-titulo="Deseja mesmo excluir?"
                                                        data-excluir-descricao="Esta ação excluirá o estoque selecionado permanentemente."
                                                        title="Excluir estoque"
                                                        aria-label="Excluir estoque">
                                                    <img src="${ctx}/images/icone-excluir.svg" alt="">
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="sem-dados">
                                    Nenhum estoque encontrado.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<script>
    // FILTROS AUTOMÁTICOS DOS ESTOQUES

    const formularioFiltros = document.getElementById("formulario-filtros");
    const campoBusca = document.getElementById("buscar");
    const camposSelect = formularioFiltros.querySelectorAll("select");

    let tempoBusca;
    let enviandoFiltros = false;

    function aplicarFiltros() {
        if (enviandoFiltros) return;

        enviandoFiltros = true;
        formularioFiltros.requestSubmit();
    }

    camposSelect.forEach(function(campo) {
        campo.addEventListener("change", function() {
            clearTimeout(tempoBusca);
            aplicarFiltros();
        });
    });

    campoBusca.addEventListener("input", function() {
        clearTimeout(tempoBusca);

        tempoBusca = setTimeout(function() {
            aplicarFiltros();
        }, 600);
    });

    // CONFIRMAÇÃO DE EXCLUSÃO

    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        if (!evento.detail || evento.detail.tipo !== "estoque") {
            return;
        }

        const botao = evento.detail.botao;
        const formulario = botao ? botao.closest(".form-excluir") : null;

        if (!formulario) return;

        const codigo = formulario.querySelector('input[name="codigo"]');

        if (!codigo || codigo.value !== evento.detail.id) {
            return;
        }

        botao.disabled = true;
        formulario.submit();
    });
</script>

<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>
