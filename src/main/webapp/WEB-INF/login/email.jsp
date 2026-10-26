<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    Recuperação de senha - passo 1/3: informar o e-mail.

    Envia para o ForgotSenhaServlet (POST /senha/esqueci): email.
    O servlet gera um código de 6 dígitos, envia por e-mail e manda para /senha/codigo.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - email: o que já tinha sido digitado
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

    <%@ include file="/WEB-INF/cadastro/painel.jspf" %>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/login" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Informe seu e-mail para receber o código de recuperação</p>

                <form action="${ctx}/senha/esqueci" method="post" novalidate>
                    <%-- só tem um campo, então qualquer erro é dele --%>
                    <div class="campo<c:if test="${not empty erroInterno}"> erro</c:if>">
                        <label for="email">Email</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="seuemail@exemplo.com"
                                   value="<c:out value='${email}'/>">
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
