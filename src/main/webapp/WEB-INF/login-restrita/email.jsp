
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
    Recuperação de senha do administrador.
    Envia o email para o ForgotSenhaAdminServlet (POST /admin/senha/esqueci).
    Em caso de erro, recebe erroInterno e email.
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Redefinir senha - Zera</title>
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
            <a href="${ctx}/admin/login" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Informe seu e-mail para receber um código de verificação</p>

                <form action="${ctx}/admin/senha/esqueci" method="post" novalidate>

                    <div class="campo<c:if test='${not empty erroInterno}'> erro</c:if>">
                        <label for="email">Email</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="email@exemplo.com"
                                   autocomplete="email" value="<c:out value='${email}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Continuar</button>
                    </div>

                </form>
            </div>
        </div>
    </section>
</main>

</body>
</html>
