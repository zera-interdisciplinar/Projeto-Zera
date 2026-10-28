<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Recuperação de senha - passo 2/2: criar a nova senha.

    Só abre depois que o e-mail e o telefone foram confirmados no passo anterior.
    Se alguém abrir /senha/nova direto, o ResetSenhaServlet manda de volta para /senha/esqueci.

    Envia para o ResetSenhaServlet (POST /senha/nova): senha, confirmacao.
    O e-mail não vai no formulário: o servlet pega da sessão, para ninguém trocar a senha de outra pessoa.
    Se der certo, o servlet mostra a senha-alterada.jsp.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro (as senhas nunca voltam preenchidas)
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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

    <%@ include file="/WEB-INF/cadastro/painel.jspf" %>

    <%-- descobre em qual campo foi o erro, pelo começo da mensagem --%>
    <c:set var="erroSenha" value="${fn:startsWith(erroInterno, 'A senha')}"/>
    <c:set var="erroConfirmacao" value="${fn:startsWith(erroInterno, 'A confirmação')}"/>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/senha/esqueci" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Crie uma nova senha para acessar sua conta.</p>

                <form action="${ctx}/senha/nova" method="post" novalidate>
                    <div class="campo<c:if test="${erroSenha}"> erro</c:if>">
                        <label for="senha">Nova senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="senha" name="senha" class="com-olho" placeholder="Crie uma nova senha">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="campo<c:if test="${erroConfirmacao}"> erro</c:if>">
                        <label for="confirmacao">Confirmar nova senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="confirmacao" name="confirmacao" class="com-olho" placeholder="Repita sua senha">

                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>

                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
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
</body>
</html>
