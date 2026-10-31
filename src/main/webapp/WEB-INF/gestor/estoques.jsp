
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="estoques"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estoques - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>
<body>
<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>
<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
    <c:choose>
        <%-- CADASTRO --%>
        <c:when test="${not empty param.novo}">
            <a href="${ctx}/estoque/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>
            <h1 class="nome-da-area">Novo estoque</h1>
            <section class="tabela">
                <form action="${ctx}/estoque/inserir" method="post">
                    <div class="form-coluna">
                        <div class="filtrador">
                            <label for="espacoNaUnidade">Espaço na unidade</label>
                            <input type="text" id="espacoNaUnidade" name="espacoNaUnidade" placeholder="ex: Galpão 3, sala B" class="form-campo-total" required>
                        </div>
                        <div class="filtrador">
                            <label for="codResponsavel">Responsável</label>
                            <select id="codResponsavel" name="codResponsavel" class="form-campo-total" required>
                                <c:forEach var="gestor" items="${gestores}">
                                    <option value="${gestor.codigo}"><c:out value="${gestor.nome}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="alinhar-direita">
                        <button type="submit" class="adicionar botao-submit">Salvar</button>
                    </div>
                </form>
            </section>
        </c:when>
        <%-- EDIÇÃO --%>
        <c:when test="${not empty param.editar}">
            <a href="${ctx}/estoque/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>
            <h1 class="nome-da-area">Editar estoque</h1>
            <section class="tabela">
                <c:choose>
                    <c:when test="${not empty estoqueEditado}">
                        <form action="${ctx}/estoque/alterar" method="post">
                            <div class="form-coluna">
                                <div class="filtrador">
                                    <label for="espacoNaUnidade">Espaço na unidade</label>
                                    <input type="text" id="espacoNaUnidade" name="espacoNaUnidade" class="form-campo-total" value="<c:out value='${estoqueEditado.espacoNaUnidade}'/>" required>
                                </div>
                                <div class="filtrador">
                                    <label for="codResponsavel">Responsável</label>
                                    <select id="codResponsavel" name="codResponsavel" class="form-campo-total" required>
                                        <c:forEach var="gestor" items="${gestores}">
                                            <option value="${gestor.codigo}" ${gestor.codigo == estoqueEditado.codResponsavel ? 'selected' : ''}>
                                                <c:out value="${gestor.nome}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="filtrador">
                                    <label for="codigo">Código</label>
                                    <input type="text" id="codigo" name="codigo" class="form-campo-total" value="${estoqueEditado.codigo}" readonly>
                                </div>
                            </div>
                            <div class="alinhar-direita">
                                <button type="submit" class="adicionar botao-submit">Salvar</button>
                            </div>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p>Estoque não encontrado na sua unidade.</p>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:when>
        <%-- LISTAGEM E FILTROS --%>
        <c:otherwise>
            <h1 class="nome-da-area">Estoques</h1>
            <section class="tabela">
                <form action="${ctx}/estoque/perfil" method="get" class="tabela-filtros" data-sem-carregamento>
                    <div class="filtrador busca">
                        <label for="busca">Buscar</label>
                        <div class="campo-busca">
                            <input type="search" id="busca" name="busca" placeholder="Código ou espaço na unidade" value="<c:out value='${param.busca}'/>">
                            <button type="submit" class="btn-lupa" aria-label="Buscar">
                                <img src="${ctx}/images/icone-lupa.svg" alt="">
                            </button>
                        </div>
                    </div>
                    <div class="filtrador">
                        <label for="codResponsavel">Responsável</label>
                        <select id="codResponsavel" name="codResponsavel">
                            <option value="">Todos</option>
                            <c:forEach var="gestor" items="${gestores}">
                                <option value="${gestor.codigo}" ${param.codResponsavel eq gestor.codigo.toString() ? 'selected' : ''}>
                                    <c:out value="${gestor.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="adicionar">Filtrar</button>
                    <a href="${ctx}/estoque/perfil" class="adicionar">Limpar</a>
                    <a href="${ctx}/estoque/perfil?novo=1" class="adicionar">
                        <img src="${ctx}/images/icone-adicionar.svg" alt="">
                        Novo estoque
                    </a>
                </form>
                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                        <thead>
                        <tr>
                            <th class="col-cod">Código</th>
                            <th>Espaço na unidade</th>
                            <th>Responsável</th>
                            <th class="col-acoes"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty estoques}">
                                <c:forEach var="e" items="${estoques}">
                                    <c:set var="nomeResponsavel" value=""/>
                                    <c:forEach var="gestor" items="${gestores}">
                                        <c:if test="${gestor.codigo == e.codResponsavel}">
                                            <c:set var="nomeResponsavel" value="${gestor.nome}"/>
                                        </c:if>
                                    </c:forEach>
                                    <tr>
                                        <td><c:out value="${e.codigo}"/></td>
                                        <td><c:out value="${e.espacoNaUnidade}"/></td>
                                        <td><c:out value="${nomeResponsavel}" default="Não informado"/></td>
                                        <td class="col-acoes">
                                            <div class="acoes">
                                                <a href="${ctx}/estoque/perfil?editar=${e.codigo}" title="Editar">
                                                    <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                                </a>
                                                <form action="${ctx}/estoque/excluir" method="post" class="form-excluir">
                                                    <input type="hidden" name="codigo" value="${e.codigo}">
                                                    <button type="button" class="zera-botao-excluir zera-abrir-exclusao" title="Excluir estoque" aria-label="Excluir estoque">
                                                        <img src="${ctx}/images/icone-excluir.svg" alt="">
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="4">Nenhum estoque encontrado para os filtros selecionados.</td></tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</div>

<div id="zera-modal-exclusao-estoque" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true" aria-labelledby="zera-exclusao-titulo" aria-describedby="zera-exclusao-descricao">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-exclusao" aria-label="Fechar"></button>
        <h2 id="zera-exclusao-titulo">Deseja mesmo excluir?</h2>
        <p id="zera-exclusao-descricao" class="zera-modal-descricao">Essa ação pode levar à exclusão de outros itens relacionados.</p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-exclusao">Voltar</button>
            <button type="button" class="zera-modal-confirmar" id="zera-confirmar-exclusao">Excluir</button>
        </div>
    </div>
</div>

<script>
    (() => {
        const modal = document.getElementById("zera-modal-exclusao-estoque");
        const cancelar = document.getElementById("zera-cancelar-exclusao");
        const fechar = document.getElementById("zera-fechar-exclusao");
        const confirmar = document.getElementById("zera-confirmar-exclusao");
        let formularioSelecionado = null;
        let botaoAnterior = null;

        function abrir(botao) {
            formularioSelecionado = botao.closest("form");
            botaoAnterior = botao;
            confirmar.disabled = false;
            modal.hidden = false;
            document.body.style.overflow = "hidden";
            cancelar.focus();
        }

        function encerrar() {
            modal.hidden = true;
            document.body.style.overflow = "";
            formularioSelecionado = null;
            if (botaoAnterior) botaoAnterior.focus();
        }

        document.querySelectorAll(".zera-abrir-exclusao").forEach(botao => {
            botao.addEventListener("click", () => abrir(botao));
        });

        cancelar.addEventListener("click", encerrar);
        fechar.addEventListener("click", encerrar);
        modal.addEventListener("click", evento => {
            if (evento.target === modal) encerrar();
        });
        document.addEventListener("keydown", evento => {
            if (evento.key === "Escape" && !modal.hidden) encerrar();
        });
        confirmar.addEventListener("click", () => {
            if (!formularioSelecionado) return;
            confirmar.disabled = true;
            formularioSelecionado.requestSubmit();
        });
    })();
</script>
<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>
</body>
</html>
