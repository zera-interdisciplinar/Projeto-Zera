<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Cadastro - passo 4/5: endereço da unidade.

    Envia para o CadastroEnderecoServlet (POST /cadastro/endereco):
    logradouro, numero, cep, estado, cidade, bairro.
    Depois daqui: organização nova vai para os planos, organização existente vai direto para a confirmação.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - logradouro, numero, cep, estado, cidade, bairro: o que já tinha sido digitado
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereço da unidade - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <%-- descobre em qual campo foi o erro, pelo começo da mensagem --%>
    <c:set var="erroNumero" value="${fn:startsWith(erroInterno, 'Número')}"/>
    <c:set var="erroCep" value="${fn:startsWith(erroInterno, 'CEP')}"/>
    <%-- "Preencha todos os campos" não é de um campo só, então aparece embaixo do formulário --%>
    <c:set var="erroGeral" value="${fn:startsWith(erroInterno, 'Preencha')}"/>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/cadastro/unidade" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card">
                <span class="indicador-etapa">4/5</span>

                <h1>Endereço da unidade</h1>
                <p class="subtitulo">Preencha os dados para prosseguir</p>

                <form action="${ctx}/cadastro/endereco" method="post" novalidate>
                    <div class="campo">
                        <label for="logradouro">Logradouro</label>
                        <input type="text" id="logradouro" name="logradouro" placeholder="Digite o endereço (rua, avenida...)"
                               value="<c:out value='${logradouro}'/>">
                    </div>

                    <div class="linha-campos">
                        <div class="campo campo-numero<c:if test="${erroNumero}"> erro</c:if>">
                            <label for="numero">Número</label>
                            <input type="text" id="numero" name="numero" placeholder="123" inputmode="numeric"
                                   value="<c:out value='${numero}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>

                        <div class="campo campo-cep<c:if test="${erroCep}"> erro</c:if>">
                            <label for="cep">CEP</label>
                            <input type="text" id="cep" name="cep" placeholder="00000000" inputmode="numeric" maxlength="9"
                                   value="<c:out value='${cep}'/>">
                            <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>
                        </div>

                        <div class="campo campo-estado">
                            <label for="estado">Estado</label>
                            <input type="text" id="estado" name="estado" placeholder="SP" maxlength="2"
                                   value="<c:out value='${estado}'/>">
                        </div>
                    </div>

                    <div class="campo">
                        <label for="cidade">Cidade</label>
                        <input type="text" id="cidade" name="cidade" placeholder="Digite a cidade"
                               value="<c:out value='${cidade}'/>">
                    </div>

                    <div class="campo">
                        <label for="bairro">Bairro</label>
                        <input type="text" id="bairro" name="bairro" placeholder="Digite o bairro"
                               value="<c:out value='${bairro}'/>">
                    </div>

                    <c:if test="${erroGeral}">
                        <span class="mensagem-erro" style="display: block"><c:out value="${erroInterno}"/></span>
                    </c:if>

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
