
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
    <title>Dados da organização - Zera</title>
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

        <h1 class="nome-da-area">Dados da organização</h1>

        <c:choose>
            <c:when test="${empty organizacao}">
                <p>Organização não encontrada.</p>
            </c:when>

            <c:otherwise>
                <section class="tabela">
                    <table class="itens-tabela">
                        <tbody>
                        <tr>
                            <th>Nome</th>
                            <td><c:out value="${organizacao.nome}"/></td>
                        </tr>

                        <tr>
                            <th>CNPJ</th>
                            <td><c:out value="${organizacao.cnpj}"/></td>
                        </tr>

                        <tr>
                            <th>E-mail</th>
                            <td><c:out value="${organizacao.email}"/></td>
                        </tr>
                        </tbody>
                    </table>

                    <a href="${ctx}/organizacao/perfil?codigo=${organizacao.codigo}">
                        Editar organização
                    </a>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>
</body>
</html>
