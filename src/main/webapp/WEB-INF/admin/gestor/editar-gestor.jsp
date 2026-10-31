
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="gestor"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar gestor - Zera</title>
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

        <h1 class="nome-da-area">Editar gestor</h1>

        <c:choose>
            <c:when test="${empty gestor}">
                <p>Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/gestor/alterar" method="post">
                        <input type="hidden" name="codigo" value="${gestor.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-coluna">
                            <div class="filtrador">
                                <label for="nome">Nome</label>
                                <input type="text" id="nome" name="nome"
                                       class="form-campo-total"
                                       value="<c:out value='${gestor.nome}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="email">E-mail</label>
                                <input type="email" id="email" name="email"
                                       class="form-campo-total"
                                       value="<c:out value='${gestor.email}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="telefone">Telefone</label>
                                <input type="tel" id="telefone" name="telefone"
                                       class="form-campo-total"
                                       value="<c:out value='${gestor.telefone}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="codUnidade">Unidade</label>

                                <select id="codUnidade" name="codUnidade"
                                        class="form-campo-total" required>
                                    <option value="">Selecione</option>

                                    <c:forEach var="item" items="${unidades}">
                                        <option value="${item.codigo}"
                                            ${gestor.codUnidade == item.codigo ? "selected" : ""}>
                                            <c:out value="${item.cnpj}"/>
                                        </option>
                                    </c:forEach>
                                </select>
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
