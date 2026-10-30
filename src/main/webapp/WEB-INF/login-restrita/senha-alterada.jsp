
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
    Página de confirmação da alteração de senha do administrador.
    Exibida pelo ResetSenhaAdminServlet após a atualização no banco.
    O botão Fazer login redireciona para /admin/login.
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

    <%@ include file="../cadastro/painel.jspf" %>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <div class="card card-central">

                <img src="${ctx}/images/sucesso.svg" alt="Sucesso" class="sucesso-icone">

                <h1>Senha alterada!</h1>
                <p class="subtitulo">Sua senha foi redefinida com sucesso.</p>

                <div class="linha-botao">
                    <a href="${ctx}/admin/login" class="botao-primario">Fazer login</a>
                </div>

            </div>
        </div>
    </section>

</main>

</body>
</html>
