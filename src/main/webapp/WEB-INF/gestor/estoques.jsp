<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Estoques da unidade do gestor: lista, novo estoque e editar estoque.

    Recebe do ReadEstoqueServlet (GET /estoque/perfil):
    - estoques: estoques da unidade do gestor, já filtrados pela busca
      (o filtro é feito no banco, pelo EstoqueDAO.findByUnidade)
    - gestores: gestores da mesma unidade (sem a senha)

    O servlet sempre manda para esta página. Qual tela aparece depende do link:
    - /estoque/perfil              lista (aceita ?busca=)
    - /estoque/perfil?novo=1       formulário de novo estoque
    - /estoque/perfil?editar=5     formulário de edição do estoque 5

    Envia para:
    - InsertEstoqueServlet (POST /estoque/inserir): espacoNaUnidade
      (o responsável fica sendo o gestor logado)
    - UpdateEstoqueServlet (POST /estoque/alterar): codigo, espacoNaUnidade
      (o responsável continua o mesmo)
    - DeleteEstoqueServlet (POST /estoque/excluir): codigo
--%>
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
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <c:choose>

        <%-- ===== NOVO ESTOQUE ===== --%>
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
                            <input type="text" id="espacoNaUnidade" name="espacoNaUnidade"
                                   placeholder="ex: Galpão 3, sala B" class="form-campo-total">
                        </div>
                    </div>

                    <div class="alinhar-direita">
                        <button type="submit" class="adicionar botao-submit">
                            Salvar
                        </button>
                    </div>
                </form>
            </section>
        </c:when>

        <%-- ===== EDITAR ESTOQUE ===== --%>
        <c:when test="${not empty param.editar}">
            <%-- procura na lista o estoque do link; compara como texto para não dar erro se vier letra --%>
            <c:forEach var="e" items="${estoques}">
                <c:if test="${fn:trim(e.codigo) == fn:trim(param.editar)}">
                    <c:set var="estoqueEditado" value="${e}"/>
                </c:if>
            </c:forEach>

            <a href="${ctx}/estoque/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>

            <h1 class="nome-da-area">Editar estoque</h1>

            <section class="tabela">
                <c:if test="${empty estoqueEditado}">
                    <p class="subtitulo-area">Estoque não encontrado na sua unidade.</p>
                </c:if>

                <c:if test="${not empty estoqueEditado}">
                    <form action="${ctx}/estoque/alterar" method="post">
                        <div class="form-coluna">
                            <div class="filtrador">
                                <label for="espacoNaUnidade">Espaço na unidade</label>
                                <input type="text" id="espacoNaUnidade" name="espacoNaUnidade" class="form-campo-total"
                                       value="<c:out value='${estoqueEditado.espacoNaUnidade}'/>">
                            </div>

                            <div class="filtrador">
                                <label for="codigo">Código</label>
                                    <%-- readonly: o valor vai no formulário, mas o usuário não edita --%>
                                <input type="text" id="codigo" name="codigo" class="form-campo-total"
                                       value="${estoqueEditado.codigo}" readonly>
                            </div>
                        </div>

                        <div class="alinhar-direita">
                            <button type="submit" class="adicionar botao-submit">
                                Salvar
                            </button>
                        </div>
                    </form>
                </c:if>
            </section>
        </c:when>

        <%-- ===== LISTA ===== --%>
        <c:otherwise>
            <h1 class="nome-da-area">Estoques</h1>

            <section class="tabela">
                <form action="${ctx}/estoque/perfil" method="get" class="tabela-filtros">
                    <div class="filtrador busca">
                        <label for="busca">Buscar</label>
                        <div class="campo-busca">
                            <input type="search" id="busca" name="busca" placeholder="Código ou espaço na unidade"
                                   value="<c:out value='${param.busca}'/>">
                            <button type="submit" class="btn-lupa" aria-label="Buscar">
                                <img src="${ctx}/images/icone-lupa.svg" alt="">
                            </button>
                        </div>
                    </div>

                    <a href="${ctx}/estoque/perfil?novo=1" class="adicionar">
                        <img src="${ctx}/images/icone-adicionar.svg" alt="">
                        Novo estoque
                    </a>
                </form>

                    <%-- usado só para escolher a mensagem quando a lista vem vazia --%>
                <c:set var="busca" value="${fn:trim(param.busca)}"/>

                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                        <thead>
                        <tr>
                            <th class="col-cod">Código</th>
                            <th>Espaço na unidade</th>
                            <th class="col-acoes"></th>
                        </tr>
                        </thead>
                        <tbody>
                            <%-- a lista já vem filtrada do banco (EstoqueDAO.findByUnidade) --%>
                        <c:forEach var="e" items="${estoques}">
                            <tr>
                                <td>${e.codigo}</td>
                                <td><c:out value="${e.espacoNaUnidade}"/></td>
                                <td>
                                    <div class="acoes">
                                        <a href="${ctx}/estoque/perfil?editar=${e.codigo}" title="Editar">
                                            <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                        </a>
                                            <%-- excluir é POST, então é um formulário pequeno com um botão --%>
                                        <form action="${ctx}/estoque/excluir" method="post" class="form-excluir"
                                              onsubmit="return confirm('Excluir este estoque?');">
                                            <input type="hidden" name="codigo" value="${e.codigo}">
                                            <button type="submit" class="botao-acao" title="Excluir">
                                                <img src="${ctx}/images/icone-excluir.svg" alt="Excluir">
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty estoques}">
                            <tr>
                                <td colspan="3">
                                    <c:if test="${empty busca}">Nenhum estoque cadastrado nesta unidade.</c:if>
                                    <c:if test="${not empty busca}">Nenhum estoque encontrado para essa busca.</c:if>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:otherwise>

    </c:choose>
</main>
</div>

</body>
</html>
