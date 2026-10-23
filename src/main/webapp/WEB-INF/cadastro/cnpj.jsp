<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Cadastro - passo 2/5 (outra opção): entrar numa organização que já existe.

    Envia para o BuscarOrganizacaoServlet (POST /cadastro/cnpj): cnpj.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - cnpj: o que já tinha sido digitado
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CNPJ da organização - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/cadastro/gestor" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <span class="indicador-etapa">2/5</span>

                <h1>CNPJ da organização</h1>
                <p class="subtitulo">Insira o CNPJ da organização</p>

                <form action="${ctx}/cadastro/cnpj" method="post" novalidate>
                    <%-- só tem um campo, então qualquer erro é dele --%>
                    <div class="campo<c:if test="${not empty erroInterno}"> erro</c:if>">
                        <label for="cnpj">CNPJ</label>
                        <input type="text" id="cnpj" name="cnpj" placeholder="00.000.000/0000-00"
                               inputmode="numeric" maxlength="18" value="<c:out value='${cnpj}'/>">
                        <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                    </div>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Confirmar</button>
                    </div>

                    <a href="${ctx}/cadastro/organizacao" class="link-pequeno">Não possui organização?</a>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>
