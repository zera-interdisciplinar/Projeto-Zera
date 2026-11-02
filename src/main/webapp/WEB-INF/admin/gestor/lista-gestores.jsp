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
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${ctx}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
        </div>
    </aside>
    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
        <h1 class="nome-da-area">Gestores</h1>
        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault()">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input id="buscar" name="buscar" type="search"
                           placeholder="Nome, email, telefone ou unidade">
                    <button type="button" class="btn-lupa-busca" onclick="filtrar()" aria-label="Buscar">
                        <img src="${ctx}/images/icone-lupa.svg" alt="Buscar">
                    </button>
                </div>

                <a href="${ctx}/gestor/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    <span>Novo gestor</span>
                </a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Telefone</th>
                        <th>Unidade</th>
                        <th class="col-acoes-th"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty gestores}">
                            <c:forEach var="item" items="${gestores}">
                                <tr class="linha-gestor">
                                    <td><c:out value="${item.nome}"/></td>
                                    <td><c:out value="${item.email}"/></td>
                                    <td><c:out value="${item.telefone}"/></td>
                                    <td><c:out value="${item.codUnidade}"/></td>

                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/gestor/perfil?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Visualizar">
                                                <img src="${ctx}/images/icone-ver.svg" alt="Visualizar">
                                            </a>

                                            <a href="${ctx}/gestor/perfil?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Editar">
                                                <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                            </a>

                                            <form method="post" action="${ctx}/gestor/excluir" class="form-excluir">
                                                <input type="hidden" name="codigo" value="${item.codigo}">
                                                <button type="button"
                                                        class="btn-icone zera-abrir-exclusao"
                                                        title="Excluir gestor"
                                                        aria-label="Excluir gestor">
                                                    <img src="${ctx}/images/icone-excluir.svg" alt="Excluir">
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="sem-dados">Nenhum gestor cadastrado.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    <tr id="sem-resultados" hidden>
                        <td colspan="5" class="sem-dados">Nenhum gestor encontrado.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<div id="zera-modal-exclusao" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-exclusao" aria-label="Fechar"></button>
        <h2>Deseja mesmo excluir?</h2>
        <p class="zera-modal-descricao">Essa ação excluirá o gestor selecionado e não poderá ser desfeita.</p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-exclusao">Voltar</button>
            <button type="button" class="zera-modal-confirmar" id="zera-confirmar-exclusao">Excluir</button>
        </div>
    </div>
</div>

<div id="zera-modal-sair" class="zera-modal-overlay" hidden>
    <div class="zera-modal-caixa" role="dialog" aria-modal="true">
        <button type="button" class="zera-modal-fechar" id="zera-fechar-sair" aria-label="Fechar"></button>
        <h2>Deseja realmente sair?</h2>
        <p class="zera-modal-descricao">Você será desconectado da sua conta de administrador.</p>
        <div class="zera-modal-botoes">
            <button type="button" class="zera-modal-voltar" id="zera-cancelar-sair">Voltar</button>
            <a href="${ctx}/logout" class="zera-modal-confirmar" id="zera-confirmar-sair" style="text-decoration:none; display:inline-flex; align-items:center; justify-content:center;">Sair</a>
        </div>
    </div>
</div>

<script>
    const busca = document.getElementById('buscar');
    const linhas = [...document.querySelectorAll('.linha-gestor')];
    const aviso = document.getElementById('sem-resultados');

    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        let visiveis = 0;

        linhas.forEach((linha) => {
            const ok = linha.textContent.toLowerCase().includes(termo);
            linha.hidden = !ok;
            if (!linha.hidden) visiveis++;
        });

        if (aviso) aviso.hidden = visiveis !== 0 || linhas.length === 0;
    }

    busca?.addEventListener('input', filtrar);
    filtrar();

    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });
</script>
<script src="${ctx}/js/popup.js" defer></script>
</body>
</html>