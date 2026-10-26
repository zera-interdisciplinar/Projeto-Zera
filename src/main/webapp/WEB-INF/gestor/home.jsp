<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Home do gestor.

    Recebe do HomeGestorServlet (GET /gestor/home):
    - nomeGestor: nome do gestor logado
    - estoquesNaUnidade: quantidade de estoques da unidade
    - assinatura: assinatura ativa da organização (pode ser null)
    - plano: plano da assinatura (só vem quando existe assinatura)
--%>
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

<%@ include file="menu.jspf" %>

<%-- primeiro nome para a saudação --%>
<c:set var="partesSaudacao" value="${fn:split(nomeGestor, ' ')}"/>
<c:set var="primeiroNome" value="${partesSaudacao[0]}"/>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Olá, <c:out value="${primeiroNome}"/></h1>
    <p class="subtitulo-area">Gerencie os dados da sua unidade pelo seu painel.</p>

    <section class="home-cards-container">
        <div class="card-metrica">
            <span class="titulo-metrica">Estoques na<br>unidade</span>
            <span class="numero-metrica">${estoquesNaUnidade}</span>
        </div>

        <div class="card-metrica">
            <span class="titulo-metrica">Plano<br>atual</span>
            <c:if test="${not empty plano}">
                <span class="numero-metrica"><c:out value="${plano.nome}"/></span>
            </c:if>
            <c:if test="${empty plano}">
                <span class="numero-metrica">-</span>
            </c:if>
        </div>

        <div class="card-metrica borda-amarela">
            <span class="titulo-metrica">Assinatura<br>válida até</span>
            <c:if test="${not empty assinatura}">
                <%-- LocalDate vira texto aaaa-mm-dd; aqui reorganiza para dd/mm/aaaa --%>
                <c:set var="fim" value="${assinatura.dataFim}"/>
                <span class="numero-metrica">${fn:substring(fim, 8, 10)}/${fn:substring(fim, 5, 7)}/${fn:substring(fim, 0, 4)}</span>
            </c:if>
            <c:if test="${empty assinatura}">
                <span class="numero-metrica">Nenhuma assinatura ativa</span>
            </c:if>
        </div>
    </section>

    <section class="home-painel-inferior"></section>
</main>
</div>

</body>
</html>
