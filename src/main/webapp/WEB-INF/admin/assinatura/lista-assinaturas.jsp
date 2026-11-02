<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="assinatura"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assinaturas - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
</head>
<body>
<div class="tela-auth">
    <aside class="painel-esquerda"><div class="painel-conteudo">
        <img src="${ctx}/images/logo_completa.svg" alt="Zera" class="logo-zera">
        <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
    </div></aside>
    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
        <h1 class="nome-da-area">Assinaturas</h1>
        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault(); filtrar();">
                <div class="filtrador busca"><label for="buscar">Buscar</label><input id="buscar" type="search" placeholder="Código, organização ou plano">
                    <button type="submit" class="btn-lupa-busca" aria-label="Buscar"><img src="${ctx}/images/icone-lupa.svg" alt=""></button></div>
                <div class="filtrador"><label for="statusFiltro">Status</label><select id="statusFiltro"><option value="">Todos</option><option value="Ativa">Ativa</option><option value="Cancelada">Cancelada</option></select></div>
                <div class="filtrador"><label for="planoFiltro">Plano</label><select id="planoFiltro"><option value="">Todos</option><c:forEach var="plano" items="${planos}"><option value="${plano.codigo}"><c:out value="${plano.nome}"/></option></c:forEach></select></div>
                <div class="filtrador"><label for="orgFiltro">Organização</label><select id="orgFiltro"><option value="">Todas</option><c:forEach var="org" items="${organizacoes}"><option value="${org.codigo}"><c:out value="${org.nome}"/></option></c:forEach></select></div>
                <a href="${ctx}/assinatura/perfil" class="adicionar"><img src="${ctx}/images/icone-adicionar.svg" alt=""><span>Nova assinatura</span></a>
            </form>
            <div class="tabela-rolagem"><table class="itens-tabela"><thead><tr><th>Código</th><th>Organização</th><th>Plano</th><th>Início</th><th>Fim</th><th>Status</th><th class="col-acoes-th"></th></tr></thead><tbody>
            <c:choose><c:when test="${not empty assinaturas}"><c:forEach var="item" items="${assinaturas}">
                <tr class="linha-assinatura" data-status="${fn:escapeXml(item.status)}" data-plano="${item.codPlano}" data-organizacao="${item.codOrganizacao}">
                    <td><c:out value="${item.codigo}"/></td>
                    <td><c:set var="nomeOrganizacao" value="${item.codOrganizacao}"/><c:forEach var="org" items="${organizacoes}"><c:if test="${org.codigo == item.codOrganizacao}"><c:set var="nomeOrganizacao" value="${org.nome}"/></c:if></c:forEach><c:out value="${nomeOrganizacao}"/></td>
                    <td><c:set var="nomePlano" value="${item.codPlano}"/><c:forEach var="plano" items="${planos}"><c:if test="${plano.codigo == item.codPlano}"><c:set var="nomePlano" value="${plano.nome}"/></c:if></c:forEach><c:out value="${nomePlano}"/></td>
                    <td><c:out value="${item.dataInicio}"/></td><td><c:out value="${item.dataFim}"/></td><td><c:out value="${item.status}"/></td>
                    <td class="col-acoes"><div class="acoes">
                        <a href="${ctx}/assinatura/perfil?codigo=${item.codigo}" class="btn-icone" title="Editar" aria-label="Editar"><img src="${ctx}/images/icone-editar.svg" alt=""></a>
                        <form method="post" action="${ctx}/assinatura/excluir" class="form-excluir">
                            <input type="hidden" name="codigo" value="${item.codigo}">
                            <button type="button" class="btn-icone zera-botao-excluir" data-modal-exclusao data-excluir-tipo="assinatura" data-excluir-id="${item.codigo}" data-excluir-titulo="Deseja mesmo excluir?" data-excluir-descricao="Esta ação excluirá a assinatura permanentemente." title="Excluir" aria-label="Excluir"><img src="${ctx}/images/icone-excluir.svg" alt=""></button>
                        </form></div></td></tr>
            </c:forEach></c:when><c:otherwise><tr><td colspan="7" class="sem-dados">Nenhuma assinatura cadastrada.</td></tr></c:otherwise></c:choose>
            <tr id="sem-resultados" hidden><td colspan="7" class="sem-dados">Nenhuma assinatura encontrada.</td></tr>
            </tbody></table></div>
        </section>
    </main>
</div>
<script>
    // CONFIRMAÇÃO DE EXCLUSÃO
    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        if (!evento.detail || evento.detail.tipo !== "assinatura") return;
        const botao = evento.detail.botao;
        const formulario = botao ? botao.closest(".form-excluir") : null;
        if (!formulario) return;
        const codigo = formulario.querySelector('input[name="codigo"]');
        if (!codigo || codigo.value !== evento.detail.id) return;
        botao.disabled = true;
        formulario.submit();
    });
</script>
<script>
    // FILTROS COMBINADOS DA LISTAGEM
    const busca = document.getElementById("buscar");
    const statusFiltro = document.getElementById("statusFiltro");
    const planoFiltro = document.getElementById("planoFiltro");
    const orgFiltro = document.getElementById("orgFiltro");
    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        let encontrados = 0;
        document.querySelectorAll(".linha-assinatura").forEach(function(linha) {
            const corresponde = linha.textContent.toLowerCase().includes(termo) &&
                (!statusFiltro.value || linha.dataset.status === statusFiltro.value) &&
                (!planoFiltro.value || linha.dataset.plano === planoFiltro.value) &&
                (!orgFiltro.value || linha.dataset.organizacao === orgFiltro.value);
            linha.hidden = !corresponde;
            if (corresponde) encontrados++;
        });
        document.getElementById("sem-resultados").hidden = encontrados > 0 || document.querySelectorAll(".linha-assinatura").length === 0;
    }
    busca.addEventListener("input", filtrar);
    statusFiltro.addEventListener("change", filtrar);
    planoFiltro.addEventListener("change", filtrar);
    orgFiltro.addEventListener("change", filtrar);
    filtrar();
</script>
<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>