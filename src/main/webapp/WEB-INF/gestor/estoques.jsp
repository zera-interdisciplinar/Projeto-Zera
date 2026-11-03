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
    <link rel="stylesheet" href="${ctx}/css/validacao-formularios.css">
</head>
<body>
<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>
<main class="informacoes tela-scroll">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
    <c:choose>
        <c:when test="${not empty estoqueEditado or param.novo eq '1'}">
            <a href="${ctx}/estoque/perfil" class="voltar"><span>Voltar</span></a>
            <h1 class="nome-da-area"><c:choose><c:when test="${not empty estoqueEditado}">Editar estoque</c:when><c:otherwise>Novo estoque</c:otherwise></c:choose></h1>
            <section class="tabela">
                <form action="${ctx}/estoque/${not empty estoqueEditado ? 'alterar' : 'inserir'}" method="post">
                    <c:if test="${not empty estoqueEditado}">
                        <input type="hidden" name="codigo" value="${estoqueEditado.codigo}">
                    </c:if>
                    <div class="form-coluna">
                        <div class="filtrador">
                            <label for="espacoNaUnidade">Espaço na unidade</label>
                            <input class="form-campo-total" type="text" id="espacoNaUnidade" name="espacoNaUnidade" maxlength="150" required placeholder="Ex.: Galpão 3, Sala B" value="<c:out value='${estoqueEditado.espacoNaUnidade}'/>">
                        </div>
                        <div class="filtrador">
                            <label for="codResponsavel">Gestor responsável</label>
                            <select class="form-campo-total" id="codResponsavel" name="codResponsavel">
                                <option value="">Selecione o gestor responsável</option>
                                <c:forEach var="gestor" items="${gestores}">
                                    <option value="${gestor.codigo}" ${not empty estoqueEditado and gestor.codigo eq estoqueEditado.codResponsavel ? 'selected="selected"' : ''}><c:out value="${gestor.nome}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="alinhar-direita"><button class="adicionar botao-submit" type="submit">Salvar</button></div>
                </form>
            </section>
        </c:when>
        <c:otherwise>
            <h1 class="nome-da-area">Estoques</h1>
            <section class="tabela">
                <label class="tabela-titulo">Estoques da unidade</label>
                <form action="${ctx}/estoque/perfil" method="get">
                    <div class="form-grid-duplo">
                        <div class="filtrador">
                            <label for="busca">Pesquisar estoques</label>
                            <input class="form-campo-total" type="search" id="busca" name="busca" value="<c:out value='${param.busca}'/>" placeholder="Pesquisar por local">
                        </div>
                    </div>
                    <div class="alinhar-direita"><button class="adicionar botao-submit" type="submit">Pesquisar</button></div>
                </form>
                <div class="alinhar-direita"><a class="adicionar" href="${ctx}/estoque/perfil?novo=1">Novo estoque</a></div>
                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                        <thead><tr><th>Espaço na unidade</th><th>Responsável</th><th class="col-acoes">Ações</th></tr></thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty estoques}">
                                    <c:forEach var="estoque" items="${estoques}">
                                        <tr>
                                            <td><c:out value="${estoque.espacoNaUnidade}"/></td>
                                            <td>
                                                <c:forEach var="gestor" items="${gestores}">
                                                    <c:if test="${gestor.codigo eq estoque.codResponsavel}"><c:out value="${gestor.nome}"/></c:if>
                                                </c:forEach>
                                            </td>
                                            <td class="col-acoes"><a href="${ctx}/estoque/perfil?editar=${estoque.codigo}" aria-label="Editar estoque">Editar</a></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise><tr><td colspan="3">Nenhum estoque encontrado.</td></tr></c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</div>
<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>
<script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>
