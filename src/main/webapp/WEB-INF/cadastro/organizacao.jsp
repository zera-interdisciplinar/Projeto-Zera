<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Cadastro - passo 2/5: organização nova.

    Envia para o CadastroOrganizacaoServlet (POST /cadastro/organizacao): nome, email, cnpj.
    Quem já tem organização vai pelo link "Já possui organização?" para /cadastro/cnpj.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - nome, email, cnpj: o que já tinha sido digitado
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro da organização - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <%-- descobre em qual campo foi o erro, pelo começo da mensagem --%>
    <c:set var="erroNome" value="${fn:startsWith(erroInterno, 'Informe o nome')}"/>
    <c:set var="erroEmail" value="${fn:startsWith(erroInterno, 'Email')}"/>
    <c:set var="erroCnpj" value="${fn:startsWith(erroInterno, 'CNPJ') or fn:startsWith(erroInterno, 'Já existe')}"/>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/cadastro/gestor" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <span class="indicador-etapa">2/5</span>

                <h1>Dados da organização</h1>
                <p class="subtitulo">Preencha os dados para prosseguir</p>

                <form action="${ctx}/cadastro/organizacao" method="post" novalidate>
                    <div class="campo<c:if test="${erroNome}"> erro</c:if>">
                        <label for="nome">Nome da organização</label>
                        <input type="text" id="nome" name="nome" placeholder="Digite o nome da organização"
                               value="<c:out value='${nome}'/>">
                        <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                    </div>

                    <div class="campo<c:if test="${erroEmail}"> erro</c:if>">
                        <label for="email">Email da organização</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="emailorganizacao@exemplo.com"
                                   value="<c:out value='${email}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>
                    </div>

                    <div class="campo<c:if test="${erroCnpj}"> erro</c:if>">
                        <label for="cnpj">CNPJ</label>
                        <input type="text" id="cnpj" name="cnpj" placeholder="00.000.000/0000-00"
                               inputmode="numeric" maxlength="18" value="<c:out value='${cnpj}'/>">
                        <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Continuar</button>
                    </div>

                    <a href="${ctx}/cadastro/cnpj" class="link-pequeno">Já possui organização?</a>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>
