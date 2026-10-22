<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
    Cadastro - última tela: revisar e confirmar.

    Recebe do ConfirmarCadastroServlet (GET /cadastro/confirmar):
    - gestor, organizacao, unidade, endereco: o que foi preenchido nos passos anteriores
    - plano: o plano escolhido (só vem quando a organização é nova)

    O botão Confirmar faz POST /cadastro/confirmar. O servlet grava tudo no banco,
    já deixa o gestor logado e manda para a home dele.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- preço no formato brasileiro: 99,90 --%>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmar cadastro - Zera</title>
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
            <%-- organização nova volta para os planos, organização existente volta para o endereço --%>
            <c:if test="${not empty plano}">
                <a href="${ctx}/cadastro/plano" class="voltar">
                    <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                    <span>Voltar</span>
                </a>
            </c:if>
            <c:if test="${empty plano}">
                <a href="${ctx}/cadastro/endereco" class="voltar">
                    <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                    <span>Voltar</span>
                </a>
            </c:if>

            <div class="card">
                <c:if test="${not empty plano}">
                    <h1>Dados da compra</h1>
                </c:if>
                <c:if test="${empty plano}">
                    <h1>Confirmar cadastro</h1>
                </c:if>
                <p class="subtitulo">Revise os dados antes de confirmar</p>

                <form action="${ctx}/cadastro/confirmar" method="post">
                    <%-- organização nova: resumo do plano --%>
                    <c:if test="${not empty plano}">
                        <%-- texto do período: mês ou ano --%>
                        <c:set var="periodo" value="mês"/>
                        <c:if test="${plano.duracaoEmMeses == 12}">
                            <c:set var="periodo" value="ano"/>
                        </c:if>

                        <div class="resumo-plano">
                            <h2 class="resumo-titulo">Resumo do plano</h2>
                            <div class="resumo-linha">
                                <p><strong>Plano selecionado:</strong> <span class="resumo-valor"><c:out value="${plano.nome}"/></span></p>
                                <a href="${ctx}/cadastro/plano" class="trocar-link">Trocar plano</a>
                            </div>
                            <p><strong>Valor:</strong> R$ <fmt:formatNumber value="${plano.valor}" minFractionDigits="2" maxFractionDigits="2"/>/${periodo}</p>
                        </div>

                        <hr class="separador">

                        <p class="total">Total: R$ <fmt:formatNumber value="${plano.valor}" minFractionDigits="2" maxFractionDigits="2"/>/${periodo}</p>
                        <p class="aviso-cobranca">Cancele quando quiser</p>
                    </c:if>

                    <%-- organização que já existia: não escolhe plano, só mostra onde vai entrar --%>
                    <c:if test="${empty plano}">
                        <div class="resumo-plano">
                            <h2 class="resumo-titulo">Resumo do cadastro</h2>
                            <p><strong>Organização:</strong> <c:out value="${organizacao.nome}"/></p>
                            <p><strong>Unidade (CNPJ):</strong> <c:out value="${unidade.cnpj}"/></p>
                            <p><strong>Endereço:</strong>
                                <c:out value="${endereco.logradouro}"/>, <c:out value="${endereco.numero}"/> -
                                <c:out value="${endereco.cidade}"/>/<c:out value="${endereco.estado}"/></p>
                            <p><strong>Gestor:</strong> <c:out value="${gestor.nome}"/></p>
                        </div>
                    </c:if>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Confirmar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>
