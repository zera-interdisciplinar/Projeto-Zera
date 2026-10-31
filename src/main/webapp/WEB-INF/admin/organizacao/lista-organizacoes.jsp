
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organizações - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
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

        <h1 class="nome-da-area">Organizações</h1>

        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault()">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>

                    <input id="buscar" name="buscar" type="search"
                           placeholder="Nome, CNPJ ou email">
                </div>

                <div class="filtrador">
                    <label for="cadastro">Cadastro</label>

                    <select id="cadastro" name="cadastro">
                        <option value="todos">Todos</option>
                        <option value="30">Últimos 30 dias</option>
                        <option value="90">Últimos 90 dias</option>
                    </select>
                </div>

                <a href="${ctx}/organizacao/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    Nova organização
                </a>
            </form>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>Email</th>
                        <th>Cadastrada em</th>
                        <th>Ações</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="org" items="${organizacoes}">
                        <tr class="linha-organizacao"
                            data-cadastro="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : ''}">

                            <td><c:out value="${org.nome}"/></td>
                            <td><c:out value="${org.cnpj}"/></td>
                            <td><c:out value="${org.email}"/></td>

                            <td>
                                <c:out value="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : 'Não informada'}"/>
                            </td>

                            <td class="col-acoes">
                                <div class="acoes">
                                    <button type="button" class="ver-organizacao"
                                            title="Visualizar">
                                        Ver
                                    </button>

                                    <a href="${ctx}/organizacao/perfil?codigo=${org.codigo}"
                                       title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>

                                    <form method="post" action="${ctx}/organizacao/excluir"
                                          onsubmit="return confirm('Deseja excluir a organização e os dados relacionados?');">

                                        <input type="hidden" name="codigo"
                                               value="${org.codigo}">

                                        <button type="submit">Excluir</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <tr id="sem-resultados">
                        <td colspan="5">Nenhuma organização encontrada.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>

        <dialog id="modal-visualizacao">
            <h2>Dados da organização</h2>

            <p>Nome: <span id="detalhe-nome"></span></p>
            <p>CNPJ: <span id="detalhe-cnpj"></span></p>
            <p>Email: <span id="detalhe-email"></span></p>

            <form method="dialog">
                <button>Fechar</button>
            </form>
        </dialog>
    </main>
</div>

<script>
    const busca = document.getElementById('buscar');
    const cadastro = document.getElementById('cadastro');
    const linhas = [...document.querySelectorAll('.linha-organizacao')];
    const aviso = document.getElementById('sem-resultados');

    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        const dias = cadastro.value;
        let visiveis = 0;

        linhas.forEach((linha) => {
            const ok = linha.textContent.toLowerCase().includes(termo);
            let dataOk = true;

            if (dias !== 'todos') {
                const data = linha.dataset.cadastro;
                const limite = new Date();

                limite.setHours(0, 0, 0, 0);
                limite.setDate(limite.getDate() - (dias === '30' ? 30 : 90));

                dataOk = !!data && new Date(data + 'T00:00:00') >= limite;
            }

            linha.hidden = !(ok && dataOk);

            if (!linha.hidden) {
                visiveis++;
            }
        });

        aviso.hidden = visiveis !== 0;
    }

    busca?.addEventListener('input', filtrar);
    cadastro?.addEventListener('change', filtrar);

    filtrar();

    const detalhe = document.getElementById('modal-visualizacao');

    document.querySelectorAll('.ver-organizacao').forEach((botao) => {
        botao.addEventListener('click', () => {
            const linha = botao.closest('tr');

            document.getElementById('detalhe-nome').textContent =
                linha.cells[0].textContent;

            document.getElementById('detalhe-cnpj').textContent =
                linha.cells[1].textContent;

            document.getElementById('detalhe-email').textContent =
                linha.cells[2].textContent;

            detalhe.showModal();
        });
    });
</script>
</body>
</html>
