
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="assinatura"/>

<fmt:setLocale value="pt_BR"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Assinatura - Zera</title>

    <link rel="icon" href="${ctx}/images/favicon.png" type="image/png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">

    <style>
        .itens-tabela td.status-ativa {
            color: #10B981;
            font-weight: 700;
        }

        .itens-tabela td.status-cancelada {
            color: #DC2626;
            font-weight: 700;
        }
    </style>
</head>

<body>

<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>

<main class="informacoes">

    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <c:choose>

        <c:when test="${param.editar == '1'}">

            <a href="${ctx}/assinatura/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>

            <h1 class="nome-da-area">Editar assinatura</h1>

            <section class="tabela">

                <c:if test="${not empty erro}">
                    <p role="alert">
                        <c:out value="${erro}"/>
                    </p>
                </c:if>

                <c:choose>

                    <c:when test="${empty assinatura}">
                        <p>Não há assinatura para editar.</p>
                    </c:when>

                    <c:otherwise>

                        <form
                            id="form-assinatura"
                            action="${ctx}/assinatura/perfil"
                            method="POST"
                        >

                            <input type="hidden" name="acao" value="editar">

                            <c:if test="${not empty csrfToken}">
                                <input
                                    type="hidden"
                                    name="csrfToken"
                                    value="<c:out value='${csrfToken}'/>"
                                >
                            </c:if>

                            <div class="form-coluna form-coluna-junto">

                                <div class="filtrador">
                                    <label for="organizacao">Organização</label>

                                    <input
                                        id="organizacao"
                                        class="form-campo-total"
                                        type="text"
                                        value="<c:out value='${nomeOrganizacao}'/>"
                                        readonly
                                    >
                                </div>

                            </div>

                            <div class="form-grid-duplo">

                                <div class="filtrador">

                                    <label for="plano">Plano</label>

                                    <c:choose>

                                        <c:when test="${not empty planos}">

                                            <select
                                                id="plano"
                                                name="plano"
                                                class="form-campo-total"
                                                required
                                            >

                                                <c:forEach var="opcao" items="${planos}">

                                                    <option
                                                        value="<c:out value='${opcao.id}'/>"
                                                        ${opcao.id == plano.id ? 'selected' : ''}
                                                    >
                                                        <c:out value="${opcao.nome}"/>
                                                    </option>

                                                </c:forEach>

                                            </select>

                                        </c:when>

                                        <c:otherwise>

                                            <input
                                                id="plano"
                                                class="form-campo-total"
                                                type="text"
                                                value="<c:out value='${plano.nome}'/>"
                                                readonly
                                            >

                                            <p>
                                                Para permitir a troca do plano, o Servlet deve fornecer a lista de planos disponíveis.
                                            </p>

                                        </c:otherwise>

                                    </c:choose>

                                </div>

                                <div class="filtrador">

                                    <label for="data-inicio">Data de início</label>

                                    <input
                                        type="date"
                                        id="data-inicio"
                                        class="form-campo-total"
                                        value="<c:out value='${assinatura.dataInicio}'/>"
                                        readonly
                                    >

                                </div>

                                <div class="filtrador">

                                    <label for="status">Status da assinatura</label>

                                    <select
                                        id="status"
                                        name="status"
                                        class="form-campo-total"
                                        required
                                    >

                                        <option
                                            value="Ativa"
                                            ${assinatura.status == 'Ativa' ? 'selected' : ''}
                                        >
                                            Ativa
                                        </option>

                                        <option
                                            value="Pendente"
                                            ${assinatura.status == 'Pendente' ? 'selected' : ''}
                                        >
                                            Pendente
                                        </option>

                                        <option
                                            value="Cancelada"
                                            ${assinatura.status == 'Cancelada' ? 'selected' : ''}
                                        >
                                            Cancelada
                                        </option>

                                    </select>

                                </div>

                                <div class="filtrador">

                                    <label for="data-termino">Data de término</label>

                                    <input
                                        type="date"
                                        id="data-termino"
                                        name="data_termino"
                                        value="<c:out value='${assinatura.dataFim}'/>"
                                        class="form-campo-total"
                                        required
                                    >

                                    <span
                                        id="erro-data"
                                        class="mensagem-erro"
                                        role="alert"
                                        style="display:none"
                                    >
                                        A data de término deve ser posterior à data de início.
                                    </span>

                                </div>

                            </div>

                            <div class="alinhar-direita">

                                <button
                                    type="submit"
                                    class="adicionar botao-submit"
                                >
                                    Salvar
                                </button>

                            </div>

                        </form>

                    </c:otherwise>

                </c:choose>

            </section>

        </c:when>

        <c:otherwise>

            <h1 class="nome-da-area">Assinatura</h1>

            <section class="tabela">

                <label class="tabela-titulo">Dados assinatura</label>

                <c:if test="${not empty param.sucesso}">
                    <p role="status" class="texto-verde">
                        Assinatura atualizada com sucesso.
                    </p>
                </c:if>

                <c:if test="${not empty erro}">
                    <p role="alert">
                        <c:out value="${erro}"/>
                    </p>
                </c:if>

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

                            <c:choose>

                                <c:when test="${not empty assinatura}">

                                    <c:set var="inicio" value="${assinatura.dataInicio}"/>
                                    <c:set var="fim" value="${assinatura.dataFim}"/>

                                    <c:set
                                        var="periodo"
                                        value="a cada ${plano.duracaoEmMeses} meses"
                                    />

                                    <c:if test="${plano.duracaoEmMeses == 1}">
                                        <c:set var="periodo" value="por mês"/>
                                    </c:if>

                                    <c:if test="${plano.duracaoEmMeses == 12}">
                                        <c:set var="periodo" value="por ano"/>
                                    </c:if>

                                    <tr>

                                        <td>
                                            <c:out
                                                value="${plano.nome}"
                                                default="Não informado"
                                            />
                                        </td>

                                        <td>

                                            <c:choose>

                                                <c:when test="${not empty plano}">
                                                    R$
                                                    <fmt:formatNumber
                                                        value="${plano.valor}"
                                                        minFractionDigits="2"
                                                        maxFractionDigits="2"
                                                    />
                                                    <c:out value="${periodo}"/>
                                                </c:when>

                                                <c:otherwise>
                                                    Não informado
                                                </c:otherwise>

                                            </c:choose>

                                        </td>

                                        <td>
                                            <c:out value="${fn:substring(inicio,8,10)}/${fn:substring(inicio,5,7)}/${fn:substring(inicio,0,4)}"/>
                                            a
                                            <c:out value="${fn:substring(fim,8,10)}/${fn:substring(fim,5,7)}/${fn:substring(fim,0,4)}"/>
                                        </td>

                                        <td class="${assinatura.status == 'Ativa' ? 'status-ativa' : assinatura.status == 'Cancelada' ? 'status-cancelada' : ''}">
                                            <c:out value="${assinatura.status}"/>
                                        </td>

                                        <td class="col-acoes">

                                            <div class="acoes">

                                                <a
                                                    href="${ctx}/assinatura/perfil?editar=1"
                                                    title="Editar assinatura"
                                                    aria-label="Editar assinatura"
                                                >
                                                    <img
                                                        src="${ctx}/images/icone-editar.svg"
                                                        alt="Editar"
                                                    >
                                                </a>

                                            </div>

                                        </td>

                                    </tr>

                                </c:when>

                                <c:otherwise>

                                    <tr>
                                        <td colspan="5">
                                            Sua organização não tem assinatura.
                                        </td>
                                    </tr>

                                </c:otherwise>

                            </c:choose>

                        </tbody>

                    </table>

                </div>

            </section>

        </c:otherwise>

    </c:choose>

</main>

</div>

<script>
(() => {

    const form = document.getElementById('form-assinatura');

    if (!form) return;

    const inicio = document.getElementById('data-inicio');
    const fim = document.getElementById('data-termino');
    const erro = document.getElementById('erro-data');

    if (inicio.value) {
        fim.min = inicio.value;
    }

    function validar() {

        const valido = Boolean(fim.value) &&
            (!inicio.value || fim.value > inicio.value);

        erro.style.display = valido ? 'none' : 'block';

        fim.setCustomValidity(
            valido
                ? ''
                : 'A data de término deve ser posterior à data de início.'
        );

        return valido;
    }

    fim.addEventListener('input', validar);

    form.addEventListener('submit', event => {

        if (!validar()) {
            event.preventDefault();
            fim.reportValidity();
        }

    });

})();
</script>

<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>

</body>
</html>
