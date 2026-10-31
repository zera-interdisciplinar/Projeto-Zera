
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
            <form class="tabela-filtros" id="form-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="buscar" placeholder="Nome, e-mail ou telefone">
                        <button type="submit" class="btn-lupa" aria-label="Buscar">
                            <img src="${ctx}/images/icone-lupa.svg" alt="">
                        </button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="filtrarUnidade">Unidade</label>
                    <select id="filtrarUnidade">
                        <option value="">Todas</option>
                        <c:forEach var="unidade" items="${unidades}">
                            <option value="${unidade.codigo}"><c:out value="${unidade.cnpj}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="adicionar">Filtrar</button>
                <a href="${ctx}/gestor/lista" class="adicionar">Limpar</a>
                <a href="${ctx}/gestor/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    Novo gestor
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
                        <th>Organização</th>
                        <th class="col-acoes">Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${gestores}">
                        <c:set var="nomeUnidade" value=""/>
                        <c:set var="nomeOrganizacao" value=""/>
                        <c:forEach var="unidade" items="${unidades}">
                            <c:if test="${unidade.codigo == item.codUnidade}">
                                <c:set var="nomeUnidade" value="${unidade.cnpj}"/>
                                <c:forEach var="org" items="${organizacoes}">
                                    <c:if test="${org.codigo == unidade.codOrganizacao}">
                                        <c:set var="nomeOrganizacao" value="${org.nome}"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        <tr class="linha-gestor" data-unidade="${item.codUnidade}">
                            <td><c:out value="${item.nome}"/></td>
                            <td><c:out value="${item.email}"/></td>
                            <td><c:out value="${item.telefone}"/></td>
                            <td><c:out value="${nomeUnidade}" default="Não informada"/></td>
                            <td><c:out value="${nomeOrganizacao}" default="Não informada"/></td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="${ctx}/gestor/perfil?codigo=${item.codigo}" title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>
                                    <form action="${ctx}/gestor/excluir" method="post" onsubmit="return confirm('Confirma a exclusão deste gestor?');">
                                        <input type="hidden" name="codigo" value="${item.codigo}">
                                        <button type="submit" aria-label="Excluir">
                                            <img src="${ctx}/images/icone-excluir.svg" alt="">
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr id="sem-resultados" hidden>
                        <td colspan="6">Nenhum gestor encontrado.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
<script>
    let busca = document.getElementById("buscar");
    let unidade = document.getElementById("filtrarUnidade");
    let linhas = document.querySelectorAll(".linha-gestor");
    let aviso = document.getElementById("sem-resultados");

    function filtrar() {
        let termo = busca.value.toLowerCase().trim();
        let unidadeSelecionada = unidade.value;
        let quantidade = 0;

        for (let i = 0; i < linhas.length; i++) {
            let linha = linhas[i];
            let texto = linha.innerText.toLowerCase();
            let codigoUnidade = linha.getAttribute("data-unidade");

            let buscaOk = texto.includes(termo);
            let unidadeOk = unidadeSelecionada == "" || codigoUnidade == unidadeSelecionada;

            if (buscaOk && unidadeOk) {
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

    function enviarFiltros(evento) {
        evento.preventDefault();
        filtrar();
    }

    document.getElementById("form-filtros").addEventListener("submit", enviarFiltros);
    busca.addEventListener("input", filtrar);
    unidade.addEventListener("change", filtrar);

    filtrar();
</script>
</body>
</html>
