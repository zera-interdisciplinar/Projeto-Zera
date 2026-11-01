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

<main class="informacoes">

    <img
            src="${ctx}/images/z-transparente.png"
            alt=""
            class="z-marca-dagua"
    >

    <h1 class="nome-da-area">
        Olá, <c:out value="${primeiroNome}"/>!
    </h1>

    <p class="subtitulo-area">
        Gerencie os dados da sua unidade pelo seu painel.
    </p>

    <!-- Cards Superiores -->
    <section
            class="home-cards-container home-cards-container-tres"
            aria-label="Resumo do painel"
    >

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

            <span class="plano-metrica">
                <c:out value="${empty plano ? 'Sem plano' : plano.nome}"/>
            </span>

            <c:choose>
                <c:when test="${not empty plano and plano.duracaoEmMeses == 1}">
                    <span class="descricao-metrica">Pago mensalmente</span>
                </c:when>

                <c:when test="${not empty plano and plano.duracaoEmMeses == 12}">
                    <span class="descricao-metrica">Pago anualmente</span>
                </c:when>
                <c:otherwise>
                    <span class="descricao-metrica">Assinatura ativa</span>
                </c:otherwise>
            </c:choose>
        </div>

    </section>

    <!-- Vigência da Assinatura -->
    <section class="home-vigencia" aria-labelledby="titulo-vigencia">

        <h2 id="titulo-vigencia">
            Vigência da assinatura
        </h2>

        <c:choose>

            <c:when test="${not empty assinatura and not empty assinatura.dataInicio and not empty assinatura.dataFim}">

                <div
                        class="home-vigencia-conteudo"
                        id="vigencia-conteudo"
                        data-inicio="${fn:escapeXml(assinatura.dataInicio)}"
                        data-fim="${fn:escapeXml(assinatura.dataFim)}"
                >

                    <!-- Gráfico Circular em 2 Cores: Laranja (gastos) e Azul Claro (restantes) -->
                    <div class="home-vigencia-grafico">

                        <svg
                                class="home-vigencia-svg"
                                id="vigencia-grafico"
                                viewBox="0 0 240 240"
                                role="img"
                                aria-label="Calculando a vigência em dias"
                        >
                            <!-- Base: Azul Claro (dias restantes) -->
                            <circle
                                    cx="120"
                                    cy="120"
                                    r="94"
                                    fill="none"
                                    stroke="#38BDF8"
                                    stroke-width="26"
                            />

                            <!-- Preenchimento: Laranja (dias já gastos) -->
                            <circle
                                    id="vigencia-circulo-usado"
                                    cx="120"
                                    cy="120"
                                    r="94"
                                    fill="none"
                                    stroke="#F59E0B"
                                    stroke-width="26"
                                    transform="rotate(-90 120 120)"
                                    stroke-dasharray="0 1000"
                            />
                        </svg>

                        <div class="home-vigencia-centro">
                            <strong id="vigencia-dias">0</strong>
                            <span id="vigencia-rotulo">dias gastos</span>
                        </div>

                    </div>

                    <!-- Lateral: Legenda 2 cores + Datas Início/Fim + Botão "Ver assinatura" -->
                    <div class="home-vigencia-painel-lateral">

                        <!-- Legenda com contagem e cores -->
                        <div class="home-vigencia-legenda-cores">
                            <div class="legenda-cor-item">
                                <span class="badge-cor badge-laranja"></span>
                                <div class="legenda-cor-info">
                                    <span class="legenda-cor-titulo">Dias já gastos</span>
                                    <strong class="legenda-cor-valor" id="legenda-dias-gastos">0 dias</strong>
                                </div>
                            </div>

                            <div class="legenda-cor-item">
                                <span class="badge-cor badge-azul-claro"></span>
                                <div class="legenda-cor-info">
                                    <span class="legenda-cor-titulo">Dias que ainda faltam</span>
                                    <strong class="legenda-cor-valor" id="legenda-dias-restantes">0 dias</strong>
                                </div>
                            </div>
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

            </c:when>

            <c:otherwise>
                <p class="home-vigencia-indisponivel">
                    Não há dados de vigência disponíveis para esta assinatura.
                </p>
            </c:otherwise>

        </c:choose>

    </section>

</main>

</div>

<script>
    "use strict";

    document.addEventListener("DOMContentLoaded", () => {

        const conteudo = document.getElementById("vigencia-conteudo");
        if (!conteudo) return;

        const circulo = document.getElementById("vigencia-circulo-usado");
        const grafico = document.getElementById("vigencia-grafico");

        const gastosEl = document.getElementById("vigencia-dias");
        const rotuloEl = document.getElementById("vigencia-rotulo");
        const legendaGastosEl = document.getElementById("legenda-dias-gastos");
        const legendaRestantesEl = document.getElementById("legenda-dias-restantes");

        const MS_DIA = 86400000;

        function dataUtc(valor) {
            const resultado = /^(\d{4})-(\d{2})-(\d{2})/.exec(String(valor || "").trim());
            if (!resultado) return NaN;

            const ano = Number(resultado[1]);
            const mes = Number(resultado[2]);
            const dia = Number(resultado[3]);

            const numero = Date.UTC(ano, mes - 1, dia);
            const teste = new Date(numero);

            if (
                teste.getUTCFullYear() !== ano ||
                teste.getUTCMonth() !== mes - 1 ||
                teste.getUTCDate() !== dia
            ) {
                return NaN;
            }

            return numero;
        }

        const inicio = dataUtc(conteudo.dataset.inicio);
        const fim = dataUtc(conteudo.dataset.fim);
        const agora = new Date();
        const hoje = Date.UTC(agora.getFullYear(), agora.getMonth(), agora.getDate());

        if (!Number.isFinite(inicio) || !Number.isFinite(fim) || fim <= inicio) {
            if (gastosEl) gastosEl.textContent = "—";
            if (rotuloEl) rotuloEl.textContent = "dias gastos";
            if (legendaGastosEl) legendaGastosEl.textContent = "—";
            if (legendaRestantesEl) legendaRestantesEl.textContent = "—";

            if (grafico) {
                grafico.setAttribute("aria-label", "Datas inválidas para calcular a vigência");
            }
            return;
        }

        const total = Math.max(1, Math.round((fim - inicio) / MS_DIA));
        const passados = Math.round((hoje - inicio) / MS_DIA);
        const usados = Math.min(total, Math.max(0, passados));
        const restantes = Math.max(0, total - usados);

        if (gastosEl) gastosEl.textContent = String(usados);
        if (rotuloEl) rotuloEl.textContent = usados === 1 ? "dia gasto" : "dias gastos";
        if (legendaGastosEl) {
            legendaGastosEl.textContent = usados + (usados === 1 ? " dia" : " dias");
        }

        if (legendaRestantesEl) {
            legendaRestantesEl.textContent = restantes + (restantes === 1 ? " dia" : " dias");
        }

        const circunferencia = 2 * Math.PI * 94; // ~590.62
        const segmentoUsado = (circunferencia * usados) / total;

        if (circulo) {
            circulo.setAttribute(
                "stroke-dasharray",
                segmentoUsado + " " + circunferencia
            );
        }

        if (grafico) {
            grafico.setAttribute(
                "aria-label",
                usados + " dias gastos, " + restantes + " dias que ainda faltam de um total de " + total + " dias"
            );
        }

    });
</script>

<script src="${ctx}/js/popup.js" defer></script>

</body>
</html>