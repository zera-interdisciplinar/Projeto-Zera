
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ativo" value="estoques" />

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
    <img src="${ctx}/images/z-transparente.png"
         alt="" class="z-marca-dagua">

    <c:choose>

        <c:when test="${not empty param.editar and not empty estoqueEditado}">
            <a href="${ctx}/estoque/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>

            <h1 class="nome-da-area">Editar estoque</h1>

            <section class="tabela">
                <form action="${ctx}/estoque/alterar" method="post">
                    <input type="hidden" name="codigo"
                           value="${estoqueEditado.codigo}">

                    <div class="form-coluna">
                        <div class="filtrador">
                            <label for="espaco">Espaço na unidade</label>
                            <input type="text"
                                   id="espaco"
                                   name="espacoNaUnidade"
                                   class="form-campo-total"
                                   value="${fn:escapeXml(estoqueEditado.espacoNaUnidade)}"
                                   required>
                        </div>
                    </div>

                    <div class="alinhar-direita">
                        <button type="submit"
                                class="adicionar botao-submit">
                            Salvar
                        </button>
                    </div>
                </form>
            </section>
        </c:when>

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
                            <label for="espaco">Espaço na unidade</label>
                            <input type="text"
                                   id="espaco"
                                   name="espacoNaUnidade"
                                   placeholder="Ex: Galpão 3, sala B"
                                   class="form-campo-total"
                                   required>
                        </div>
                    </div>

                    <div class="alinhar-direita">
                        <button type="submit"
                                class="adicionar botao-submit">
                            Salvar
                        </button>
                    </div>
                </form>
            </section>
        </c:when>

        <c:otherwise>
            <h1 class="nome-da-area">Estoques</h1>

            <section class="tabela">
                <form action="${ctx}/estoque/perfil"
                      method="get"
                      class="tabela-filtros">

                    <div class="filtrador busca">
                        <label for="busca">Buscar</label>
                        <div class="campo-busca">
                            <input type="search"
                                   id="busca"
                                   name="busca"
                                   value="${fn:escapeXml(param.busca)}"
                                   placeholder="Código ou espaço na unidade">

                            <button type="submit"
                                    class="btn-lupa"
                                    aria-label="Buscar">
                                <img src="${ctx}/images/icone-lupa.svg"
                                     alt="">
                            </button>
                        </div>
                    </div>

                    <a href="${ctx}/estoque/perfil?novo=1"
                       class="adicionar">
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
                            <th class="col-acoes"></th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:choose>
                            <c:when test="${not empty estoques}">
                                <c:forEach var="estoque" items="${estoques}">
                                    <tr>
                                        <td>
                                            <c:out value="${estoque.codigo}"/>
                                        </td>

                                        <td>
                                            <c:out value="${estoque.espacoNaUnidade}"/>
                                        </td>

                                        <td class="col-acoes">
                                            <div class="acoes">
                                                <a href="${ctx}/estoque/perfil?editar=${estoque.codigo}"
                                                   title="Editar estoque">
                                                    <img src="${ctx}/images/icone-editar.svg"
                                                         alt="Editar">
                                                </a>

                                                <button type="button"
                                                        class="zera-botao-excluir"
                                                        data-modal-exclusao
                                                        data-excluir-id="${estoque.codigo}"
                                                        data-excluir-tipo="estoque"
                                                        data-excluir-titulo="Deseja mesmo excluir?"
                                                        data-excluir-descricao="Essa ação não poderá ser desfeita."
                                                        aria-label="Excluir estoque"
                                                        title="Excluir">
                                                    <img src="${ctx}/images/icone-excluir.svg"
                                                         alt="">
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <tr>
                                    <td colspan="3">
                                        Nenhum estoque encontrado.
                                    </td>
                                </tr>
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

<form id="form-excluir-estoque"
      action="${ctx}/estoque/excluir"
      method="post"
      hidden>
    <input type="hidden" name="codigo" id="codigo-excluir-estoque">
</form>

<script>
    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        if (evento.detail.tipo !== "estoque") {
            return;
        }

        var codigo = evento.detail.id;
        document.getElementById("codigo-excluir-estoque").value = codigo;
        document.getElementById("form-excluir-estoque").submit();
    });
</script>

<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>

</body>
</html>
