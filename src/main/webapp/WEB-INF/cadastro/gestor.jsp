<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Cadastro - passo 1/5: dados do gestor.

    Envia para o CadastroGestorServlet (POST /cadastro/gestor):
    nome, email, telefone, senha, confirmacao.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - nome, email, telefone: o que já tinha sido digitado
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro do gestor - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <%-- descobre em qual campo foi o erro, pelo começo da mensagem --%>
    <c:set var="erroNome" value="${fn:startsWith(erroInterno, 'Informe o nome')}"/>
    <c:set var="erroEmail" value="${fn:startsWith(erroInterno, 'Email') or fn:startsWith(erroInterno, 'Já existe')}"/>
    <c:set var="erroTelefone" value="${fn:startsWith(erroInterno, 'Telefone')}"/>
    <c:set var="erroSenha" value="${fn:startsWith(erroInterno, 'A senha')}"/>
    <c:set var="erroConfirmacao" value="${fn:startsWith(erroInterno, 'A confirmação')}"/>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <span class="indicador-etapa">1/5</span>

                <h1>Dados do gestor</h1>
                <p class="subtitulo">Preencha seus dados para criar a conta</p>

                <form action="${ctx}/cadastro/gestor" method="post" novalidate>
                    <%-- a classe "erro" no campo deixa ele vermelho e mostra a mensagem --%>
                    <div class="campo<c:if test="${erroNome}"> erro</c:if>">
                        <label for="nome">Nome</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/pessoa.svg" class="icone-input" alt="">
                            <input type="text" id="nome" name="nome" placeholder="Digite seu nome completo"
                                   value="<c:out value='${nome}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="campo<c:if test="${erroEmail}"> erro</c:if>">
                        <label for="email">Email</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="seuemail@exemplo.com"
                                   value="<c:out value='${email}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="campo<c:if test="${erroTelefone}"> erro</c:if>">
                        <label for="telefone">Telefone</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/telefone.svg" class="icone-input" alt="">
                            <input type="tel" id="telefone" name="telefone" placeholder="(00) 00000-0000"
                                   value="<c:out value='${telefone}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <%-- senha nunca volta preenchida --%>
                    <div class="campo<c:if test="${erroSenha}"> erro</c:if>">
                        <label for="senha">Senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="senha" name="senha" placeholder="Crie uma senha">
                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="campo<c:if test="${erroConfirmacao}"> erro</c:if>">
                        <label for="confirmacao">Confirmar senha</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/cadeado.svg" class="icone-input" alt="">
                            <input type="password" id="confirmacao" name="confirmacao" placeholder="Repita sua senha">
                            <button type="button" class="btn-olho" aria-label="Mostrar senha">
                                <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                            </button>
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Continuar</button>
                    </div>

                    <a href="${ctx}/login" class="link-pequeno">Já possui cadastro?</a>
                </form>
            </div>
        </div>
    </div>

</div>

<script>
    // botão do olho: mostra ou esconde a senha do campo ao lado
    document.querySelectorAll('.btn-olho').forEach((botao) => {
        botao.addEventListener('click', () => {
            const campo = botao.parentElement.querySelector('input');
            if (campo.type === 'password') {
                campo.type = 'text';
                botao.classList.add('visivel');                       // olho riscado
                botao.setAttribute('aria-label', 'Esconder senha');
            } else {
                campo.type = 'password';
                botao.classList.remove('visivel');                    // olho aberto
                botao.setAttribute('aria-label', 'Mostrar senha');
            }
        });
    });
</script>
</body>
</html>
