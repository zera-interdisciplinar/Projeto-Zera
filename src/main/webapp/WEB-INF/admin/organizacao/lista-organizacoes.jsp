<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacoes"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organizações - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
</head>
<body>
<div class="tela-auth">
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${ctx}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
            <a href="${ctx}/admin/perfil" class="perfil-usuario">
                <div class="avatar">AD</div><span class="nome-usuario">Administrador</span>
            </a>
        </div>
    </aside>
    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
        <h1 class="nome-da-area">Organizações</h1>
        <section class="tabela">
            <form id="form-filtros" action="${ctx}/organizacao/lista" method="get" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="buscar" name="buscar" placeholder="Nome, CNPJ ou email" value="<c:out value='${param.buscar}'/>">
                        <button type="submit" class="btn-lupa" aria-label="Buscar"><img src="${ctx}/images/icone-lupa.svg" alt=""></button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="cadastro">Cadastro</label>
                    <select name="cadastro" id="cadastro">
                        <option value="todos">Todos</option>
                        <option value="ultimos-30-dias" ${param.cadastro == 'ultimos-30-dias' ? 'selected' : ''}>Últimos 30 dias</option>
                        <option value="ultimos-90-dias" ${param.cadastro == 'ultimos-90-dias' ? 'selected' : ''}>Últimos 90 dias</option>
                    </select>
                </div>
                <a href="${ctx}/organizacao/perfil" class="adicionar"><img src="${ctx}/images/icone-adicionar.svg" alt="">Nova organização</a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead><tr><th>Nome</th><th>CNPJ</th><th>Email</th><th>Cadastrada em</th><th class="col-acoes">Ações</th></tr></thead>
                    <tbody id="lista-organizacoes">
                    <c:forEach var="org" items="${organizacoes}">
                        <tr class="linha-organizacao" data-cadastro="${fn:substring(org.criadoEm,0,10)}">
                            <td><c:out value="${org.nome}"/></td>
                            <td><c:out value="${org.cnpj}"/></td>
                            <td><c:out value="${org.email}"/></td>
                            <td><c:choose><c:when test="${not empty org.criadoEm}"><c:out value="${fn:substring(org.criadoEm,8,10)}"/>/<c:out value="${fn:substring(org.criadoEm,5,7)}"/>/<c:out value="${fn:substring(org.criadoEm,0,4)}"/></c:when><c:otherwise>Não informada</c:otherwise></c:choose></td>
                            <td class="col-acoes"><div class="acoes">
                                <button type="button" class="ver-organizacao" title="Visualizar"><img src="${ctx}/images/icone-ver.svg" alt="Visualizar"></button>
                                <a href="${ctx}/organizacao/perfil?codigo=${org.codigo}" title="Editar"><img src="${ctx}/images/icone-editar.svg" alt="Editar"></a>
                                <button type="button" class="excluir-organizacao" data-codigo="${org.codigo}" title="Excluir"><img src="${ctx}/images/icone-excluir.svg" alt="Excluir"></button>
                            </div></td>
                        </tr>
                    </c:forEach>
                    <tr id="sem-resultados" hidden><td colspan="5">Nenhuma organização encontrada.</td></tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
<dialog id="modal-visualizacao">
    <h3>Dados da organização</h3>
    <p><strong>Nome:</strong> <span id="detalhe-nome"></span></p>
    <p><strong>CNPJ:</strong> <span id="detalhe-cnpj"></span></p>
    <p><strong>Email:</strong> <span id="detalhe-email"></span></p>
    <p><strong>Cadastrada em:</strong> <span id="detalhe-data"></span></p>
    <button type="button" id="fechar-visualizacao">Fechar</button>
</dialog>
<dialog id="modal-exclusao">
    <h3>Excluir organização</h3>
    <p id="mensagem-exclusao"></p>
    <p>As unidades e os demais dados relacionados poderão ser afetados. Esta ação não pode ser desfeita.</p>
    <form id="form-exclusao" action="${ctx}/organizacao/excluir" method="post">
        <input type="hidden" name="codigo" id="codigo-exclusao">
        <button type="button" id="cancelar-exclusao">Cancelar</button>
        <button type="submit" id="confirmar-exclusao">Excluir</button>
    </form>
</dialog>
<script>
    const linhas = [...document.querySelectorAll('.linha-organizacao')];
    const busca = document.getElementById('buscar');
    const cadastro = document.getElementById('cadastro');
    const semResultados = document.getElementById('sem-resultados');
    const modalVisualizacao = document.getElementById('modal-visualizacao');
    const modalExclusao = document.getElementById('modal-exclusao');
    const formExclusao = document.getElementById('form-exclusao');
    const codigoExclusao = document.getElementById('codigo-exclusao');
    const confirmarExclusao = document.getElementById('confirmar-exclusao');
    let excluindo = false;

    function normalizar(texto) {
        return (texto || '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim();
    }

    function filtrar() {
        const termo = normalizar(busca.value);
        const textoNumerico = termo.replace(/\D/g, '');
        const dataLimite = cadastro.value === 'todos' ? null : new Date();
        if (dataLimite) dataLimite.setDate(dataLimite.getDate() - (cadastro.value === 'ultimos-30-dias' ? 30 : 90));
        let visiveis = 0;
        linhas.forEach(linha => {
            const nome = normalizar(linha.cells[0].textContent);
            const cnpj = normalizar(linha.cells[1].textContent);
            const email = normalizar(linha.cells[2].textContent);
            const coincide = nome.includes(termo) || cnpj.includes(termo) || email.includes(termo) || (textoNumerico && cnpj.replace(/\D/g, '').includes(textoNumerico));
            const data = linha.dataset.cadastro ? new Date(linha.dataset.cadastro + 'T12:00:00') : null;
            const correspondeData = !dataLimite || (data && data >= dataLimite);
            linha.hidden = !(coincide && correspondeData);
            if (!linha.hidden) visiveis++;
        });
        semResultados.hidden = visiveis > 0;
    }

    busca.addEventListener('input', filtrar);
    cadastro.addEventListener('change', filtrar);
    document.getElementById('form-filtros').addEventListener('submit', event => { event.preventDefault(); filtrar(); });
    document.querySelectorAll('.ver-organizacao').forEach(botao => botao.addEventListener('click', () => {
        const celulas = botao.closest('tr').cells;
        ['nome', 'cnpj', 'email', 'data'].forEach((campo, indice) => document.getElementById('detalhe-' + campo).textContent = celulas[indice].textContent.trim());
        modalVisualizacao.showModal();
    }));
    document.getElementById('fechar-visualizacao').addEventListener('click', () => modalVisualizacao.close());
    document.querySelectorAll('.excluir-organizacao').forEach(botao => botao.addEventListener('click', () => {
        codigoExclusao.value = botao.dataset.codigo;
        document.getElementById('mensagem-exclusao').textContent = 'Deseja excluir a organização "' + botao.closest('tr').cells[0].textContent.trim() + '"?';
        modalExclusao.showModal();
    }));
    document.getElementById('cancelar-exclusao').addEventListener('click', () => modalExclusao.close());
    formExclusao.addEventListener('submit', event => {
        if (excluindo || !codigoExclusao.value) { event.preventDefault(); return; }
        excluindo = true;
        confirmarExclusao.disabled = true;
    });
    window.addEventListener('pageshow', () => {
        excluindo = false;
        confirmarExclusao.disabled = false;
        filtrar();
    });
    filtrar();
</script>
</body>
</html>