
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
            <form class="tabela-filtros" id="form-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input id="buscar" name="buscar" type="search" placeholder="Nome, CNPJ ou e-mail">
                </div>
                <div class="filtrador">
                    <label for="cadastro">Cadastro</label>
                    <select id="cadastro" name="cadastro">
                        <option value="todos">Todos</option>
                        <option value="30">Últimos 30 dias</option>
                        <option value="90">Últimos 90 dias</option>
                    </select>
                </div>
                <button type="submit" class="adicionar">Filtrar</button>
                <button type="button" class="adicionar" id="limpar-filtros">Limpar</button>
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
                        <th>E-mail</th>
                        <th>Cadastrada em</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="org" items="${organizacoes}">
                        <tr class="linha-organizacao" data-cadastro="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : ''}">
                            <td><c:out value="${org.nome}"/></td>
                            <td><c:out value="${org.cnpj}"/></td>
                            <td><c:out value="${org.email}"/></td>
                            <td><c:out value="${org.criadoEm != null ? fn:substring(org.criadoEm,0,10) : 'Não informada'}"/></td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <button type="button" class="ver-organizacao" title="Visualizar">Ver</button>
                                    <c:url var="urlEditarCnpj" value="/organizacao/perfil">
                                        <c:param name="cnpj" value="${org.cnpj}"/>
                                    </c:url>
                                    <a href="${urlEditarCnpj}" title="Editar pelo CNPJ">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>
                                    <form method="post" action="${ctx}/organizacao/excluir" class="form-excluir-organizacao">
                                        <input type="hidden" name="codigo" value="<c:out value='${org.codigo}'/>">
                                        <input type="hidden" name="cnpj" value="<c:out value='${org.cnpj}'/>">
                                        <input type="hidden" class="nome-organizacao" value="<c:out value='${org.nome}'/>">
                                        <button type="submit">Excluir</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr id="sem-resultados" hidden>
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
            <p>E-mail: <span id="detalhe-email"></span></p>
            <form method="dialog">
                <button type="submit">Fechar</button>
            </form>
        </dialog>
    </main>
</div>

<script>
    let busca = document.getElementById("buscar");
    let cadastro = document.getElementById("cadastro");
    let linhas = document.querySelectorAll(".linha-organizacao");
    let aviso = document.getElementById("sem-resultados");

    function filtrar() {
        let termo = busca.value.toLowerCase().trim();
        let dias = cadastro.value;
        let quantidade = 0;

        for (let i = 0; i < linhas.length; i++) {
            let linha = linhas[i];
            let texto = linha.innerText.toLowerCase();
            let dataCadastro = linha.getAttribute("data-cadastro");
            let buscaOk = texto.includes(termo);
            let dataOk = true;

            if (dias != "todos") {
                let limite = new Date();
                limite.setHours(0, 0, 0, 0);
                limite.setDate(limite.getDate() - Number(dias));

                if (dataCadastro == "") {
                    dataOk = false;
                } else {
                    let data = new Date(dataCadastro + "T00:00:00");
                    dataOk = data >= limite;
                }
            }

            if (buscaOk && dataOk) {
                linha.hidden = false;
                quantidade++;
            } else {
                linha.hidden = true;
            }
        }

        if (quantidade == 0) {
            aviso.hidden = false;
        } else {
            aviso.hidden = true;
        }
    }

    function limparFiltros() {
        busca.value = "";
        cadastro.value = "todos";
        filtrar();
    }

    function visualizarOrganizacao(botao) {
        let linha = botao.closest("tr");
        document.getElementById("detalhe-nome").textContent = linha.cells[0].textContent;
        document.getElementById("detalhe-cnpj").textContent = linha.cells[1].textContent;
        document.getElementById("detalhe-email").textContent = linha.cells[2].textContent;
        document.getElementById("modal-visualizacao").showModal();
    }

    function confirmarExclusao(evento) {
        let formulario = evento.target;
        let nome = formulario.querySelector(".nome-organizacao").value;

        if (!confirm("Deseja excluir a organização " + nome + " e todos os dados relacionados?")) {
            evento.preventDefault();
        }
    }

    function enviarFiltros(evento) {
        evento.preventDefault();
        filtrar();
    }

    let botoesVisualizar = document.querySelectorAll(".ver-organizacao");
    for (let i = 0; i < botoesVisualizar.length; i++) {
        botoesVisualizar[i].addEventListener("click", function() {
            visualizarOrganizacao(this);
        });
    }

    let formulariosExcluir = document.querySelectorAll(".form-excluir-organizacao");
    for (let i = 0; i < formulariosExcluir.length; i++) {
        formulariosExcluir[i].addEventListener("submit", confirmarExclusao);
    }

    document.getElementById("form-filtros").addEventListener("submit", enviarFiltros);
    document.getElementById("limpar-filtros").addEventListener("click", limparFiltros);
    busca.addEventListener("input", filtrar);
    cadastro.addEventListener("change", filtrar);

    filtrar();
</script>
</body>
</html>
