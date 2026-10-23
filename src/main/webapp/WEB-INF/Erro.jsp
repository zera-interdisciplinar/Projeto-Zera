<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    Página de erro do sistema.

    Os servlets mandam para cá com o ErroServlet.exibirErro(...), que grava:
    - mensagemErro: o que aconteceu

    Se a página for aberta sem mensagem, mostra só o texto geral.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro na página - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">

    <%-- o erro.css vem por último para sobrescrever o estilo do h1 --%>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>
<body class="pagina-erro">

<main class="erro-tela">
    <%-- texto do erro (topo esquerdo) --%>
    <div class="erro-conteudo">
        <h1 class="erro-titulo">Erro na página</h1>
        <p class="erro-descricao">A página está passando por algum erro, tente recarregá-la.</p>

        <%-- o que aconteceu, vindo do servlet; c:out evita que a mensagem quebre o HTML --%>
        <c:if test="${not empty mensagemErro}">
            <p class="erro-detalhe">
                <strong>O que aconteceu:</strong> <c:out value="${mensagemErro}"/>
            </p>
        </c:if>

        <%-- volta para a página anterior, onde o usuário estava antes do erro --%>
        <a href="javascript:history.back()" class="erro-voltar">Voltar à página anterior</a>
    </div>

    <%-- imagem das tomadas com a sombra (centro direita) --%>
    <div class="erro-imagem-area">
        <img src="${ctx}/images/erro_tomada.png" alt="Página com erro - tomadas desconectadas" class="erro-imagem">
    </div>
</main>

<%-- faixas diagonais inferiores --%>
<div class="erro-faixas-fundo" aria-hidden="true">
    <svg viewBox="0 0 100 100" preserveAspectRatio="none">
        <%-- faixa azul intermediária (--cor3) --%>
        <polygon points="0,83  100,66  100,71  0,88" fill="var(--cor3)"/>
        <%-- faixa azul marinho (--cor2) --%>
        <polygon points="0,88  100,71  100,100  0,100" fill="var(--cor2)"/>
    </svg>
</div>

</body>
</html>
