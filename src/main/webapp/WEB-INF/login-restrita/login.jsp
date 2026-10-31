
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
    Login da área restrita do administrador.

    Envia para o LoginAdminServlet (POST /admin/login): email e senha.
    Se o login for válido, o servlet guarda o administrador na sessão
    e redireciona para /admin/home.

    Em caso de erro, recebe:
    - erroInterno: mensagem de erro
    - email: e-mail anteriormente digitado

    A senha nunca é devolvida ao formulário.
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="erroEmail" value="${not empty erroInterno}"/>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrar na Área Restrita - Zera</title>
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
            <a href="${ctx}/" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card card-restrita">
                <h1>Entrar na área restrita</h1>
                <p class="subtitulo">Preencha seus dados para entrar na sua conta</p>

                <form action="${ctx}/admin/login" method="post" novalidate>

                    <!-- Campo de email -->
                    <div class="campo<c:if test='${erroEmail}'> erro</c:if>">
                        <label for="email">Email</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="email@exemplo.com"
                                   autocomplete="username" value="<c:out value='${email}'/>">
                        </div>
                    </div>

                    <!-- Campo de senha -->
                    <div class="campo<c:if test='${not empty erroInterno}'> erro</c:if>">
                        <label for="senha">Senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="senha" name="senha" class="com-olho"
                                   placeholder="Digite sua senha" autocomplete="current-password">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>

                        <a href="${ctx}/admin/senha/esqueci" class="link-pequeno link-esqueci">Esqueci minha senha</a>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Entrar</button>
                    </div>

                </form>
            </div>
        </div>
    </section>
</main>

<script>
    document.querySelectorAll('.btn-olho').forEach((botao) => {
        botao.addEventListener('click', () => {
            const campo = botao.parentElement.querySelector('input');
            campo.type = campo.type === 'password' ? 'text' : 'password';
            botao.classList.toggle('visivel');
            botao.setAttribute('aria-label', campo.type === 'password' ? 'Mostrar senha' : 'Ocultar senha');
        });
    });
</script>

</body>
</html>
