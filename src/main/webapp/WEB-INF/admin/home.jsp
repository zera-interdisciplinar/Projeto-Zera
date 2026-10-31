<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="home"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Zera</title>
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
    <main id="informacoes-gerais">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
        <h1 id="home">Home</h1>
        <div class="card">
            <a href="${ctx}/organizacao/lista"><div class="dado"><p class="nome-do-dado">Organizações</p><p class="numero-do-dado"><c:out value="${totalOrganizacoes}" default="0"/></p></div></a>
            <a href="${ctx}/assinatura/lista"><div class="dado"><p class="nome-do-dado">Assinaturas ativas</p><p class="numero-do-dado"><c:out value="${assinaturasAtivas}" default="0"/></p></div></a>
            <a href="${ctx}/assinatura/lista"><div id="alerta" class="dado alerta"><p class="nome-do-dado">Vencendo em 30 dias</p><p class="numero-do-dado"><c:out value="${assinaturasVencendo}" default="0"/></p></div></a>
            <a href="${ctx}/unidade/lista"><div class="dado"><p class="nome-do-dado">Unidades</p><p class="numero-do-dado"><c:out value="${totalUnidades}" default="0"/></p></div></a>
            <a href="${ctx}/estoque/lista"><div class="dado"><p class="nome-do-dado">Estoques</p><p class="numero-do-dado"><c:out value="${totalEstoques}" default="0"/></p></div></a>
        </div>
    </main>
</div>
</body>
</html>
