<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="gestor"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestores - Zera</title>
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
        <h1 class="nome-da-area">Gestores</h1>
        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault(); filtrar();">
                <div class="filtrador busca"><label for="buscar">Buscar</label><input id="buscar" name="buscar" type="search" placeholder="Nome, e-mail ou telefone">
                    <button type="submit" class="btn-lupa-busca" aria-label="Buscar"><img src="${ctx}/images/icone-lupa.svg" alt=""></button></div>
                <div class="filtrador"><label for="unidadeFiltro">Unidade (CNPJ)</label><select id="unidadeFiltro">
                    <option value="">Todas</option><c:forEach var="uni" items="${unidades}"><option value="${uni.codigo}"><c:out value="${uni.cnpj}"/></option></c:forEach>
                </select></div>
                <a href="${ctx}/gestor/perfil" class="adicionar"><img src="${ctx}/images/icone-adicionar.svg" alt=""><span>Novo gestor</span></a>
            </form>
            <div class="tabela-rolagem"><table class="itens-tabela"><thead><tr><th>Nome</th><th>E-mail</th><th>Telefone</th><th>Unidade (CNPJ)</th><th>Organização</th><th class="col-acoes-th"></th></tr></thead><tbody>
            <c:choose><c:when test="${not empty gestores}"><c:forEach var="item" items="${gestores}">
                <tr class="linha-gestor" data-unidade="${item.codUnidade}">
                    <td><c:out value="${item.nome}"/></td><td><c:out value="${item.email}"/></td><td><c:out value="${item.telefone}"/></td>
                    <c:set var="unidadeExibida" value="${item.codUnidade}"/><c:set var="organizacaoExibida" value="Não identificada"/>
                    <c:forEach var="uni" items="${unidades}"><c:if test="${uni.codigo == item.codUnidade}">
                        <c:set var="unidadeExibida" value="${uni.cnpj}"/>
                        <c:forEach var="org" items="${organizacoes}"><c:if test="${org.codigo == uni.codOrganizacao}"><c:set var="organizacaoExibida" value="${org.nome}"/></c:if></c:forEach>
                    </c:if></c:forEach>
                    <td><c:out value="${unidadeExibida}"/></td><td><c:out value="${organizacaoExibida}"/></td>
                    <td class="col-acoes"><div class="acoes">
                        <a href="${ctx}/gestor/perfil?codigo=${item.codigo}" class="btn-icone" title="Editar" aria-label="Editar"><img src="${ctx}/images/icone-editar.svg" alt=""></a>
                        <form method="post" action="${ctx}/gestor/excluir" class="form-excluir">
                            <input type="hidden" name="codigo" value="${item.codigo}">
                            <button type="button" class="btn-icone zera-botao-excluir" data-modal-exclusao data-excluir-tipo="gestor" data-excluir-id="${item.codigo}" data-excluir-titulo="Deseja mesmo excluir?" data-excluir-descricao="Atenção! Esta ação também excluirá a unidade, o endereço e todos os estoques vinculados ao gestor." title="Excluir" aria-label="Excluir"><img src="${ctx}/images/icone-excluir.svg" alt=""></button>
                        </form></div></td></tr>
            </c:forEach></c:when><c:otherwise><tr><td colspan="6" class="sem-dados">Nenhum gestor cadastrado.</td></tr></c:otherwise></c:choose>
            <tr id="sem-resultados" hidden><td colspan="6" class="sem-dados">Nenhum gestor encontrado.</td></tr>
            </tbody></table></div>
        </section>
    </main>
</div>
<script>
    // CONFIRMAÇÃO DE EXCLUSÃO
    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        if (!evento.detail || evento.detail.tipo !== "gestor") return;
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
    const unidadeFiltro = document.getElementById("unidadeFiltro");
    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        const unidade = unidadeFiltro.value;
        let encontrados = 0;
        document.querySelectorAll(".linha-gestor").forEach(function(linha) {
            const corresponde = linha.textContent.toLowerCase().includes(termo) &&
                (!unidade || linha.dataset.unidade === unidade);
            linha.hidden = !corresponde;
            if (corresponde) encontrados++;
        });
        document.getElementById("sem-resultados").hidden = encontrados > 0 || document.querySelectorAll(".linha-gestor").length === 0;
    }
    busca.addEventListener("input", filtrar);
    unidadeFiltro.addEventListener("change", filtrar);
    filtrar();
</script>
<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>
