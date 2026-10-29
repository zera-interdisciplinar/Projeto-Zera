<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Cadastro - passo 3/5: dados da unidade.

    Recebe do CadastroUnidadeServlet (GET /cadastro/unidade):
    - nomeOrganizacao: nome da organização escolhida no passo anterior

    Envia para o CadastroUnidadeServlet (POST /cadastro/unidade): email, cnpj.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - email, cnpj: o que já tinha sido digitado
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro da unidade - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <%-- descobre em qual campo foi o erro, pelo começo da mensagem --%>
    <c:set var="erroEmail" value="${fn:startsWith(erroInterno, 'Email')}"/>
    <c:set var="erroCnpj" value="${fn:startsWith(erroInterno, 'CNPJ') or fn:startsWith(erroInterno, 'Já existe')}"/>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/cadastro/organizacao" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <span class="indicador-etapa">3/5</span>

                <h1>Dados da unidade</h1>
                <%-- nomeOrganizacao só vem no GET. Se voltar com erro ele não aparece --%>
                <c:if test="${not empty nomeOrganizacao}">
                    <p class="subtitulo">Unidade da organização <c:out value="${nomeOrganizacao}"/></p>
                </c:if>
                <c:if test="${empty nomeOrganizacao}">
                    <p class="subtitulo">Preencha os dados para prosseguir</p>
                </c:if>

                <form action="${ctx}/cadastro/unidade" method="post" novalidate>
                    <div class="campo<c:if test="${erroEmail}"> erro</c:if>">
                        <label for="email">Email da unidade</label>
                        <div class="campo-icone">
                            <img src="${ctx}/images/email.svg" class="icone-input" alt="">
                            <input type="email" id="email" name="email" placeholder="emailunidade@exemplo.com"
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
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>
