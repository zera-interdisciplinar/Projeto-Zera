<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Login do gestor.

    Envia para o LoginServlet (POST /login): email, senha.
    Se der certo, o servlet guarda o gestor e a unidade na sessão e manda para /gestor/home.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: "Preencha o e-mail e a senha." ou "E-mail ou senha inválidos."
    - email: o que já tinha sido digitado (a senha nunca volta)

    A mensagem de senha errada não diz qual dos dois está errado de propósito,
    para ninguém descobrir quais e-mails estão cadastrados.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrar - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>
<body>

<main class="tela-auth">
    <%@ include file="/WEB-INF/componentes/carregando.jspf" %>
    <%@ include file="../cadastro/painel.jspf" %>

    <%-- e-mail fica vermelho só quando ficou vazio; a mensagem aparece embaixo da senha --%>
    <c:set var="erroEmail" value="${not empty erroInterno and empty fn:trim(email)}"/>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <h1>Entrar</h1>
                <p class="subtitulo">Preencha seus dados para entrar na sua conta</p>

                <form action="${ctx}/login" method="post" novalidate>
                    <div class="campo<c:if test="${erroEmail}"> erro</c:if>">
                        <label for="email">Email</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="email@exemplo.com"
                                   value="<c:out value='${email}'/>">
                        </div>
                    </div>

                    <div class="campo<c:if test="${not empty erroInterno}"> erro</c:if>">
                        <label for="senha">Senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="senha" name="senha" class="com-olho" placeholder="Digite sua senha">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                        <a href="${ctx}/senha/esqueci" class="link-pequeno link-esqueci">Esqueci minha senha</a>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Entrar</button>
                    </div>

                    <a href="${ctx}/cadastro/gestor" class="link-pequeno">Não tem cadastro?</a>
                </form>
            </div>
        </div>
    </section>
</main>

<script>
    // botão do olho: mostra ou esconde a senha do campo ao lado
    document.querySelectorAll('.btn-olho').forEach((botao) => {
        botao.addEventListener('click', () => {
            const campo = botao.parentElement.querySelector('input');
            if (campo.type === 'password') {
                campo.type = 'text';
            } else {
                campo.type = 'password';
            }
            botao.classList.toggle('visivel');
        });
    });
</script>
<script src="${ctx}/js/carregando.js" defer></script>
</body>
</html>
