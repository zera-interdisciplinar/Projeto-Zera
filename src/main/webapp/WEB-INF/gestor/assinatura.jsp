<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
    Assinatura da organização do gestor.

    Recebe do ReadAssinaturaServlet (GET /assinatura/perfil):
    - assinatura: assinatura ativa da organização, ou a mais recente (pode ser null)
    - plano: plano da assinatura (só vem quando existe assinatura)

    O botão de editar leva para /assinatura/perfil?editar=1, que o servlet
    manda para a editar-assinatura.jsp.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="assinatura"/>
<%-- preço no formato brasileiro --%>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assinatura - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Assinatura</h1>

    <section class="tabela">
        <label class="tabela-titulo">Dados assinatura</label>

        <div class="tabela-rolagem">
            <table class="itens-tabela">
                <thead>
                <tr>
                    <th>Plano</th>
                    <th>Valor</th>
                    <th>Vigência</th>
                    <th>Status</th>
                    <th class="col-acoes"></th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${not empty assinatura}">
                    <%-- texto do período--%>
                    <c:set var="periodo" value="a cada ${plano.duracaoEmMeses} meses"/>
                    <c:if test="${plano.duracaoEmMeses == 1}">
                        <c:set var="periodo" value="por mês"/>
                    </c:if>
                    <c:if test="${plano.duracaoEmMeses == 12}">
                        <c:set var="periodo" value="por ano"/>
                    </c:if>

                    <%-- LocalDate vira texto aaaa-mm-dd; aqui reorganiza para dd/mm/aaaa --%>
                    <c:set var="inicio" value="${assinatura.dataInicio}"/>
                    <c:set var="fim" value="${assinatura.dataFim}"/>

                    <tr>
                        <td><c:out value="${plano.nome}"/></td>
                        <td>R$<fmt:formatNumber value="${plano.valor}" minFractionDigits="2" maxFractionDigits="2"/> ${periodo}</td>
                        <td>
                                ${fn:substring(inicio, 8, 10)}/${fn:substring(inicio, 5, 7)}/${fn:substring(inicio, 0, 4)}
                            a
                                ${fn:substring(fim, 8, 10)}/${fn:substring(fim, 5, 7)}/${fn:substring(fim, 0, 4)}
                        </td>
                            <%-- verde quando está ativa --%>
                        <c:if test="${assinatura.status == 'Ativa'}">
                            <td class="texto-verde"><c:out value="${assinatura.status}"/></td>
                        </c:if>
                        <c:if test="${assinatura.status != 'Ativa'}">
                            <td><c:out value="${assinatura.status}"/></td>
                        </c:if>
                        <td class="col-acoes">
                            <div class="acoes">
                                <a href="${ctx}/assinatura/perfil?editar=1" title="Editar">
                                    <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${empty assinatura}">
                    <tr>
                        <td colspan="5">Sua organização não tem assinatura.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </section>
</main>
</div>

</body>
</html>
