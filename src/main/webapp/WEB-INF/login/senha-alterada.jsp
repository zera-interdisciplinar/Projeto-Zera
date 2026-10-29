<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    Recuperação de senha - tela final.

    O ResetSenhaServlet mostra esta página depois de gravar a nova senha.
    Não recebe nenhum atributo: só avisa que deu certo e leva para o login.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Senha alterada - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>

<main class="tela-auth">

    <%@ include file="/WEB-INF/cadastro/painel.jspf" %>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <div class="card card-central">

                <div class="sucesso-icone">
                    <img src="${ctx}/images/sucesso.svg" alt="Sucesso" class="sucesso-icone">
                </div>

                <h1>Senha alterada!</h1>

                <div class="linha-botao">
                    <a href="${ctx}/login" class="botao-primario">Fazer login</a>
                </div>

            </div>
        </div>
    </section>
</main>

</body>
</html>
