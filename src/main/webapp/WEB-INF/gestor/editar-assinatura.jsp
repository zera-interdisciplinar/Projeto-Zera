<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Editar a assinatura da organização do gestor.

    Recebe do ReadAssinaturaServlet (GET /assinatura/perfil?editar=1):
    - assinatura: assinatura atual da organização
    - plano: plano atual
    - organizacao: organização (o nome aparece bloqueado)
    - planos: todos os planos, para o campo de seleção

    Envia para o UpdateAssinaturaServlet (POST /assinatura/alterar):
    codigo, codPlano, status, dataFim.
    - status: o gestor só pode escolher "Ativa" ou "Cancelada" (escrito igual ao banco)
    - dataFim: o input type="date" já envia no formato aaaa-mm-dd, que o LocalDate.parse entende
    - a data de início não é enviada: o servlet sempre usa a do banco
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="assinatura"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar assinatura - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>
<body>
<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <a href="${ctx}/assinatura/perfil" class="voltar">
        <img src="${ctx}/images/seta-voltar.svg" alt="">
        <span>Voltar</span>
    </a>

    <h1 class="nome-da-area">Editar assinatura</h1>

    <section class="tabela">
        <form action="${ctx}/assinatura/alterar" method="post">
            <%-- o servlet precisa saber qual assinatura alterar --%>
            <input type="hidden" name="codigo" value="${assinatura.codigo}">

            <div class="form-coluna form-coluna-junto">
                <div class="filtrador">
                    <label for="organizacao">Organização</label>
                    <input type="text" id="organizacao" class="form-campo-total" readonly
                           value="<c:out value='${organizacao.nome}'/>">
                </div>
            </div>

            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="codPlano">Plano</label>
                    <select id="codPlano" name="codPlano" class="form-campo-total">
                        <c:forEach var="p" items="${planos}">
                            <option value="${p.codigo}"<c:if test="${p.codigo == assinatura.codPlano}"> selected</c:if>>
                                <c:out value="${p.nome}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="dataInicio">Data de início</label>
                    <c:set var="inicio" value="${assinatura.dataInicio}"/>
                    <input type="text" id="dataInicio" class="form-campo-total" readonly
                           value="${fn:substring(inicio, 8, 10)}/${fn:substring(inicio, 5, 7)}/${fn:substring(inicio, 0, 4)}">
                </div>

                <div class="filtrador">
                    <label for="status">Status da assinatura</label>
                    <select id="status" name="status" class="form-campo-total">
                        <option value="Ativa"<c:if test="${assinatura.status == 'Ativa'}"> selected</c:if>>Ativa</option>
                        <option value="Cancelada"<c:if test="${assinatura.status == 'Cancelada'}"> selected</c:if>>Cancelada</option>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="dataFim">Data de término</label>
                    <%-- LocalDate imprime aaaa-mm-dd, que é o formato do input date --%>
                    <input type="date" id="dataFim" name="dataFim" class="form-campo-total"
                           value="${assinatura.dataFim}">
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit" class="adicionar botao-submit">
                    Salvar
                </button>
            </div>
        </form>
    </section>
</main>
</div>
<script src="${ctx}/js/carregando.js" defer></script>
</body>
</html>
