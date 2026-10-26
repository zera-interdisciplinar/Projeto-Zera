<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
    Cadastro - passo 5/5: escolha do plano (só para organização nova).

    Recebe do EscolherPlanoServlet (GET /cadastro/plano):
    - planos: lista de Plano cadastrados no banco

    Envia para o EscolherPlanoServlet (POST /cadastro/plano): codPlano.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
    - planos: a lista de novo
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%-- preço no formato brasileiro: 99,90 --%>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Planos - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

    <%@ include file="painel.jspf" %>

    <div class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card container-planos">
            <a href="${ctx}/cadastro/endereco" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card card-planos">
                <span class="indicador-etapa">5/5</span>

                <h1>Planos</h1>
                <p class="subtitulo">Selecione seu plano de assinatura</p>

                <form action="${ctx}/cadastro/plano" method="post" novalidate>
                    <div class="planos-lista">
                        <%-- um card para cada plano do banco --%>
                        <c:forEach var="p" items="${planos}">
                            <label class="plano-opcao">
                                    <%-- o value é o código do plano, que o servlet lê em codPlano --%>
                                <input type="radio" name="codPlano" value="${p.codigo}">
                                <div class="plano-card">
                                    <h3><c:out value="${p.nome}"/></h3>
                                    <p class="plano-desc">Plano completo e exclusivo para empresas que buscam ir além.</p>

                                        <%-- duracaoEmMeses chama o getDuracaoEmMeses() do Plano --%>
                                    <div class="plano-preco">
                                        R$<fmt:formatNumber value="${p.valor}" minFractionDigits="2" maxFractionDigits="2"/>
                                        <c:if test="${p.duracaoEmMeses == 12}"><span>/ Ano</span></c:if>
                                        <c:if test="${p.duracaoEmMeses != 12}"><span>/ Mês</span></c:if>
                                    </div>
                                    <div class="plano-divisor"></div>

                                        <%-- os recursos não ficam no banco, então vêm pelo nome do plano --%>
                                    <ul class="plano-recursos">
                                        <c:choose>
                                            <c:when test="${fn:toLowerCase(p.nome) == 'free'}">
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Scanner</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Dashboard</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Estoque (até 50 itens)</li>
                                            </c:when>
                                            <c:when test="${fn:toLowerCase(p.nome) == 'profissional'}">
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Tudo do plano Free</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Estoque (250 itens)</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Relatórios</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Alertas</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Multiusuários</li>
                                            </c:when>
                                            <c:when test="${fn:toLowerCase(p.nome) == 'empresarial'}">
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Tudo do plano Profissional</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Estoque ilimitado</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Suporte dedicado</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">Scanner multidispositivo</li>
                                                <li><img src="${ctx}/images/check.svg" class="plano-check-icone" alt="">E muito mais!</li>
                                            </c:when>
                                        </c:choose>
                                    </ul>
                                </div>
                            </label>
                        </c:forEach>
                    </div>

                    <%-- erro: nenhum plano selecionado --%>
                    <c:if test="${not empty erroInterno}">
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
