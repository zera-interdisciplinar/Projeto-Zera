<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

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
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
</head>

<body>

<%@ include file="menu.jspf" %>
<main class="informacoes tela-scroll">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
    <h1 class="nome-da-area">Olá, <c:out value="${primeiroNome}"/>!</h1>
    <p class="subtitulo-area">Gerencie os dados da sua unidade pelo seu painel.</p>

    <!-- 3 Cards superiores ocupando 100% da largura -->
    <section class="home-cards-container" aria-label="Resumo do painel">
        <div class="home-card">
            <span class="titulo-metrica">Estoques</span>

            <span class="numero-metrica">
                <c:out value="${empty estoquesNaUnidade ? 0 : estoquesNaUnidade}"/>
            </span>

            <span class="descricao-metrica">
                Na sua unidade
            </span>
        </div>

        <div class="home-card borda-amarela">
            <span class="titulo-metrica">Unidades</span>

            <span class="numero-metrica">
                <c:out value="${empty totalUnidadesOrganizacao ? '—' : totalUnidadesOrganizacao}"/>
            </span>

            <span class="descricao-metrica">
                Na sua organização
            </span>
        </div>

        <div class="home-card">
            <span class="titulo-metrica">Plano</span>
            <span class="plano-metrica"><c:out value="${empty plano ? 'Sem plano' : plano.nome}"/></span>
            <span class="descricao-metrica">
                <c:choose>
                    <c:when test="${not empty plano and plano.duracaoEmMeses == 1}">Pago mensalmente</c:when>
                    <c:when test="${not empty plano and plano.duracaoEmMeses == 12}">Pago anualmente</c:when>
                </c:choose>
            </span>
        </div>

    </section>

    <!-- Painel de Vigência da Assinatura -->
    <section class="home-painel-inferior home-painel-inferior-unico">
        <article class="home-painel">
            <div class="grafico-cabecalho">
                <h2 class="titulo-grafico-vigencia">Vigência da assinatura</h2>
                <a href="${ctx}/assinatura/perfil" class="botao-ver-azul">Ver assinatura &rsaquo;</a>
            </div>

            <div class="vigencia-conteudo">
                <!-- Caixa de Início e Término -->
                <div class="card-datas-vigencia">
                    <div class="bloco-data">
                        <span class="legenda-data">Início</span>
                        <span class="valor-data" id="campo-data-inicio"><c:out value="${empty assinatura ? '—' : assinatura.dataInicio}"/></span>
                    </div>
                    <hr class="divisor-data">
                    <div class="bloco-data">
                        <span class="legenda-data">Término</span>
                        <span class="valor-data" id="campo-data-termino"><c:out value="${empty assinatura ? '—' : assinatura.dataFim}"/></span>
                    </div>
                </div>

                <!-- Gráfico Donut -->
                <div class="grafico-rosca-wrapper">
                    <div class="grafico-rosca" id="grafico-rosca-donut"
                         data-inicio="${assinatura.dataInicio}"
                         data-fim="${assinatura.dataFim}">
                        <div class="rosca-centro">
                            <span class="rosca-numero" id="rosca-valor">0</span>
                            <span class="rosca-rotulo" id="rosca-legenda">dias gastos</span>
                        </div>

                        <!-- Datas de Início e Término -->
                        <div class="home-vigencia-datas-container">
                            <div class="bloco-data">
                                <span class="bloco-data-label">Início</span>
                                <strong class="bloco-data-valor">
                                    <c:out value="${fn:substring(assinatura.dataInicio,8,10)}"/>/<c:out value="${fn:substring(assinatura.dataInicio,5,7)}"/>/<c:out value="${fn:substring(assinatura.dataInicio,0,4)}"/>
                                </strong>
                            </div>

                            <div class="divisor-datas"></div>

                            <div class="bloco-data">
                                <span class="bloco-data-label">Término</span>
                                <strong class="bloco-data-valor">
                                    <c:out value="${fn:substring(assinatura.dataFim,8,10)}"/>/<c:out value="${fn:substring(assinatura.dataFim,5,7)}"/>/<c:out value="${fn:substring(assinatura.dataFim,0,4)}"/>
                                </strong>
                            </div>
                        </div>

                        <!-- Botão com seta -->
                        <a href="${ctx}/assinatura/perfil" class="btn-ver-assinatura">
                            <span>Ver assinatura</span>
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="icone-seta">
                                <path d="M9 18l6-6-6-6"/>
                            </svg>
                        </a>

                    </div>
                </div>

                <!-- Legenda Lateral Interativa -->
                <div class="vigencia-legenda">
                    <div class="item-legenda interativo" data-tipo="gastos">
                        <span class="indicador-cor cor-gastos"></span>
                        <div class="detalhes-legenda">
                            <span class="texto-legenda">Dias gastos</span>
                            <small class="subtexto-legenda" id="qtd-dias-gastos">0 dias</small>
                        </div>
                    </div>
                    <div class="item-legenda interativo" data-tipo="restantes">
                        <span class="indicador-cor cor-restantes"></span>
                        <div class="detalhes-legenda">
                            <span class="texto-legenda">Dias restantes</span>
                            <small class="subtexto-legenda" id="qtd-dias-restantes">0 dias</small>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </section>

</main>

</div>

<!-- SCRIPT QUE PROCESSA AS DATAS DO BACKEND E ALINHA O GRÁFICO -->
<script>
(function() {
    function parseLocalDate(str) {
        if (!str || str === "—") return null;
        str = String(str).trim();
        if (str.indexOf("-") !== -1) {
            var p = str.split("-");
            return new Date(parseInt(p[0], 10), parseInt(p[1], 10) - 1, parseInt(p[2], 10));
        } else if (str.indexOf("/") !== -1) {
            var p = str.split("/");
            return new Date(parseInt(p[2], 10), parseInt(p[1], 10) - 1, parseInt(p[0], 10));
        }
        return null;
    }

    function formatarParaBR(date) {
        if (!date) return "—";
        var dia = String(date.getDate()).padStart(2, '0');
        var mes = String(date.getMonth() + 1).padStart(2, '0');
        var ano = date.getFullYear();
        return dia + "/" + mes + "/" + ano;
    }

    function processarVigencia() {
        try {
            var rosca = document.getElementById("grafico-rosca-donut");
            var elInicio = document.getElementById("campo-data-inicio");
            var elTermino = document.getElementById("campo-data-termino");
            if (!rosca) return;

            var elNumero = document.getElementById("rosca-valor");
            var elLegenda = document.getElementById("rosca-legenda");
            var elQtdGastos = document.getElementById("qtd-dias-gastos");
            var elQtdRestantes = document.getElementById("qtd-dias-restantes");
            var itens = document.querySelectorAll(".item-legenda.interativo");

            var dtInicio = parseLocalDate(rosca.getAttribute("data-inicio"));
            var dtFim = parseLocalDate(rosca.getAttribute("data-fim"));

            if (!dtInicio || !dtFim) {
                if (elNumero) elNumero.textContent = "0";
                if (elQtdGastos) elQtdGastos.textContent = "0 dias";
                if (elQtdRestantes) elQtdRestantes.textContent = "0 dias";
                rosca.style.background = "#dfddd6";
                return;
            }

            if (elInicio) elInicio.textContent = formatarParaBR(dtInicio);
            if (elTermino) elTermino.textContent = formatarParaBR(dtFim);

            dtInicio.setHours(0, 0, 0, 0);
            dtFim.setHours(0, 0, 0, 0);
            var hoje = new Date();
            hoje.setHours(0, 0, 0, 0);

            var msPorDia = 1000 * 60 * 60 * 24;
            var totalDias = Math.round((dtFim.getTime() - dtInicio.getTime()) / msPorDia);
            if (totalDias <= 0) totalDias = 1;

            var diasGastos = Math.round((hoje.getTime() - dtInicio.getTime()) / msPorDia);
            if (diasGastos < 0) diasGastos = 0;
            if (diasGastos > totalDias) diasGastos = totalDias;

            var diasRestantes = totalDias - diasGastos;
            var pctGastos = (diasGastos / totalDias) * 100;

            rosca.style.background = "conic-gradient(#F59E0B 0% " + pctGastos + "%, #2958D1 " + pctGastos + "% 100%)";

            if (elNumero) elNumero.textContent = diasGastos;
            if (elLegenda) elLegenda.textContent = "dias gastos";
            if (elQtdGastos) elQtdGastos.textContent = diasGastos + " dias";
            if (elQtdRestantes) elQtdRestantes.textContent = diasRestantes + " dias";

            itens.forEach(function(item) {
                var tipo = item.getAttribute("data-tipo");
                item.onmouseenter = function() {
                    if (tipo === "restantes") {
                        if (elNumero) elNumero.textContent = diasRestantes;
                        if (elLegenda) elLegenda.textContent = "dias restantes";
                    } else {
                        if (elNumero) elNumero.textContent = diasGastos;
                        if (elLegenda) elLegenda.textContent = "dias gastos";
                    }
                };
                item.onmouseleave = function() {
                    if (elNumero) elNumero.textContent = diasGastos;
                    if (elLegenda) elLegenda.textContent = "dias gastos";
                };
            });
        } catch (e) {
            console.error("Erro no processamento da vigência:", e);
        }
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", processarVigencia);
    } else {
        processarVigencia();
    }
})();
</script>

<script src="${ctx}/js/popup.js" defer></script>

</body>
</html>