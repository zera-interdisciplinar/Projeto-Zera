<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- HomeGestorServlet: GET /gestor/home
     Atributos existentes: nomeGestor, estoquesNaUnidade, assinatura, plano.
     Opcionais para os gráficos: estoquesPorLocal e unidadesPorEstado,
     ambos Map<String, Number>. totalUnidadesOrganizacao também é opcional.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ativo" value="home" />
<c:set var="nomeExibicao" value="${empty nomeGestor ? 'Gestor' : nomeGestor}" />
<c:set var="primeiroNome" value="${fn:split(nomeExibicao, ' ')[0]}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
</head>
<body>
<%@ include file="menu.jspf" %>
<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
    <h1 class="nome-da-area">Olá, <c:out value="${primeiroNome}"/>!</h1>
    <p class="subtitulo-area">Gerencie os dados da sua unidade pelo seu painel.</p>

    <section class="home-cards-container" aria-label="Resumo do painel">
        <div class="home-card">
            <span class="titulo-metrica">Estoques</span>
            <span class="numero-metrica"><c:out value="${empty estoquesNaUnidade ? 0 : estoquesNaUnidade}"/></span>
            <span class="descricao-metrica">Na sua unidade</span>
        </div>
        <div class="home-card borda-amarela">
            <span class="titulo-metrica">Unidades</span>
            <span class="numero-metrica"><c:out value="${empty totalUnidadesOrganizacao ? '—' : totalUnidadesOrganizacao}"/></span>
            <span class="descricao-metrica">Na sua organização</span>
        </div>
        <div class="home-card">
            <span class="titulo-metrica">Plano</span>
            <span class="plano-metrica"><c:out value="${empty plano ? 'Sem plano' : plano.nome}"/></span>
            <span class="descricao-metrica">Plano atual</span>
        </div>
        <div class="home-card borda-amarela">
            <span class="titulo-metrica">Assinatura válida até</span>
            <c:choose>
                <c:when test="${not empty assinatura and not empty assinatura.dataFim}">
                    <c:set var="fim" value="${assinatura.dataFim}"/>
                    <span class="plano-metrica"><c:out value="${fn:substring(fim, 8, 10)}"/>/<c:out value="${fn:substring(fim, 5, 7)}"/>/<c:out value="${fn:substring(fim, 0, 4)}"/></span>
                </c:when>
                <c:otherwise><span class="plano-metrica">—</span></c:otherwise>
            </c:choose>
            <span class="descricao-metrica">${empty assinatura ? 'Nenhuma assinatura ativa' : 'Data de vencimento'}</span>
        </div>
    </section>

    <section class="home-painel-inferior">
        <article class="home-painel">
            <h2>Estoques por local</h2>
            <div class="lista-locais" id="lista-locais">
                <c:choose>
                    <c:when test="${not empty estoquesPorLocal}">
                        <c:forEach var="entrada" items="${estoquesPorLocal}">
                            <div class="local-item" data-quantidade="${fn:escapeXml(entrada.value)}">
                                <div class="local-info"><span><c:out value="${entrada.key}"/></span><span><c:out value="${entrada.value}"/> estoque(s)</span></div>
                                <div class="barra-fundo"><div class="barra-preenchimento" style="width:0%"></div></div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise><p>Distribuição por local ainda não disponível.</p></c:otherwise>
                </c:choose>
            </div>
            <div class="home-painel-rodape"><a href="${ctx}/gestor/estoques" class="botao-ver">Ver todos &rsaquo;</a></div>
        </article>
        <article class="home-painel">
            <div class="grafico-cabecalho">
                <h2>Unidades da organização por estado</h2>
                <a href="${ctx}/gestor/organizacao" class="botao-ver">Ver organização &rsaquo;</a>
            </div>
            <c:choose>
                <c:when test="${not empty unidadesPorEstado}">
                    <div class="grafico-colunas" id="grafico-colunas" role="img" aria-label="Quantidade de unidades por estado">
                        <div class="grafico-eixo-y" id="grafico-eixo-y"></div>
                        <div class="grafico-area">
                            <div class="grafico-linhas" id="grafico-linhas"></div>
                            <div class="grafico-barras" id="grafico-barras">
                                <c:forEach var="entrada" items="${unidadesPorEstado}">
                                    <div class="grafico-coluna-item" data-quantidade="${fn:escapeXml(entrada.value)}">
                                        <div class="grafico-barra" style="height:0%" title="${fn:escapeXml(entrada.key)}: ${fn:escapeXml(entrada.value)} unidade(s)"></div>
                                        <span class="grafico-legenda"><c:out value="${entrada.key}"/></span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise><p>Distribuição por estado ainda não disponível.</p></c:otherwise>
            </c:choose>
        </article>
    </section>
</main>
</div> <%-- fecha o contêiner aberto pelo menu.jspf, como no JSP original --%>
<script>
"use strict";
// Renderiza somente os dados reais fornecidos pelo HomeGestorServlet.
document.addEventListener("DOMContentLoaded", () => {
  const locais = [...document.querySelectorAll("#lista-locais .local-item")];
  const quantidadesLocais = locais.map(el => Math.max(0, Number(el.dataset.quantidade) || 0));
  const maiorLocal = Math.max(1, ...quantidadesLocais);
  locais.forEach((el, i) => {
    el.querySelector(".barra-preenchimento").style.width = `${100 * quantidadesLocais[i] / maiorLocal}%`;
  });

  const colunas = [...document.querySelectorAll("#grafico-barras .grafico-coluna-item")];
  if (!colunas.length) return;
  const quantidades = colunas.map(el => Math.max(0, Number(el.dataset.quantidade) || 0));
  const maior = Math.max(1, ...quantidades);
  const teto = Math.max(4, Math.ceil(maior / 4) * 4);
  const eixo = document.getElementById("grafico-eixo-y");
  const linhas = document.getElementById("grafico-linhas");
  for (let i = 4; i >= 0; i--) {
    const marca = document.createElement("span");
    marca.textContent = String(teto * i / 4);
    eixo.append(marca);
    linhas.append(document.createElement("span"));
  }
  colunas.forEach((el, i) => {
    el.querySelector(".grafico-barra").style.height = `${100 * quantidades[i] / teto}%`;
  });
});
</script>
<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>
