
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar unidade - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
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

        <h1 class="nome-da-area">Editar unidade</h1>

        <c:choose>
            <c:when test="${empty unidade}">
                <p>Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/unidade/alterar" method="post">
                        <input type="hidden" name="codigo" value="${unidade.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-coluna">
                            <div class="filtrador">
                                <label for="codOrganizacao">Organização</label>

                                <select id="codOrganizacao" name="codOrganizacao"
                                        class="form-campo-total" required>
                                    <option value="">Selecione</option>

                                    <c:forEach var="item" items="${organizacoes}">
                                        <option value="${item.codigo}"
                                            ${unidade.codOrganizacao == item.codigo ? "selected" : ""}>
                                            <c:out value="${item.nome}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="filtrador">
                                <label for="cnpj">CNPJ</label>

                                <input type="text" id="cnpj" name="cnpj"
                                       class="form-campo-total"
                                       value="<c:out value='${unidade.cnpj}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="email">E-mail</label>

                                <input type="email" id="email" name="email"
                                       class="form-campo-total"
                                       value="<c:out value='${unidade.email}'/>"
                                       placeholder="" required>
                            </div>
                        </div>

                        <button type="submit" id="botao-salvar">Salvar</button>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>
</body>
</html>
