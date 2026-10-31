
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%--
    Redefinição de senha do administrador.
    Envia senha e confirmacao para o ResetSenhaAdminServlet (POST /admin/senha/nova).
    O servlet só permite a alteração após a validação do código.
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="erroSenha" value="${fn:startsWith(erroInterno, 'A senha')}"/>
<c:set var="erroConfirmacao" value="${fn:startsWith(erroInterno, 'A confirmação')}"/>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova senha - Zera</title>
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
            <a href="${ctx}/admin/senha/esqueci" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Crie uma nova senha para acessar sua conta</p>

                <form action="${ctx}/admin/senha/nova" method="post" novalidate>

                    <!-- Nova senha -->
                    <div class="campo<c:if test='${erroSenha}'> erro</c:if>">
                        <label for="senha">Nova senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="senha" name="senha" class="com-olho"
                                   placeholder="Digite sua nova senha" autocomplete="new-password">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <c:if test="${erroSenha}">
                                <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                            </c:if>
                        </div>
                    </div>

                    <!-- Confirmar senha -->
                    <div class="campo<c:if test='${erroConfirmacao}'> erro</c:if>">
                        <label for="confirmacao">Confirmar nova senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="confirmacao" name="confirmacao" class="com-olho"
                                   placeholder="Confirme sua nova senha" autocomplete="new-password">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <c:if test="${erroConfirmacao}">
                                <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                            </c:if>
                        </div>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Confirmar</button>
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
