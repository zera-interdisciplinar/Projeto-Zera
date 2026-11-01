
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unidades - Zera</title>
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
        <h1 class="nome-da-area">Unidades</h1>
        <section class="tabela">
            <form class="tabela-filtros" id="form-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="buscar" placeholder="Código, CNPJ ou e-mail">
                        <button type="submit" class="btn-lupa" aria-label="Buscar">
                            <img src="${ctx}/images/icone-lupa.svg" alt="">
                        </button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="filtrarOrganizacao">Organização</label>
                    <select id="filtrarOrganizacao">
                        <option value="">Todas</option>
                        <c:forEach var="org" items="${organizacoes}">
                            <option value="${org.codigo}"><c:out value="${org.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="adicionar">Filtrar</button>
                <a href="${ctx}/unidade/lista" class="adicionar">Limpar</a>
                <a href="${ctx}/unidade/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    Nova unidade
                </a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>CNPJ</th>
                        <th>E-mail</th>
                        <th>Organização</th>
                        <th class="col-acoes">Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${unidades}">
                        <c:set var="nomeOrganizacao" value=""/>
                        <c:forEach var="org" items="${organizacoes}">
                            <c:if test="${org.codigo == item.codOrganizacao}">
                                <c:set var="nomeOrganizacao" value="${org.nome}"/>
                            </c:if>
                        </c:forEach>
                        <tr class="linha-unidade" data-organizacao="${item.codOrganizacao}">
                            <td><c:out value="${item.codigo}"/></td>
                            <td><c:out value="${item.cnpj}"/></td>
                            <td><c:out value="${item.email}"/></td>
                            <td><c:out value="${nomeOrganizacao}" default="Não informada"/></td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="${ctx}/unidade/perfil?codigo=${item.codigo}" title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>
                                    <form action="${ctx}/unidade/excluir" method="post" onsubmit="return confirm('Confirma a exclusão desta unidade e dos dados relacionados?');">
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
                        <td colspan="5">Nenhuma unidade encontrada.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<script>
    let busca = document.getElementById("buscar");
    let organizacao = document.getElementById("filtrarOrganizacao");
    let linhas = document.querySelectorAll(".linha-unidade");
    let aviso = document.getElementById("sem-resultados");

    function filtrar() {
        let termo = busca.value.toLowerCase().trim();
        let organizacaoSelecionada = organizacao.value;
        let quantidade = 0;

        for (let i = 0; i < linhas.length; i++) {
            let linha = linhas[i];
            let texto = linha.innerText.toLowerCase();
            let codigoOrganizacao = linha.getAttribute("data-organizacao");

            let buscaOk = texto.includes(termo);
            let organizacaoOk = organizacaoSelecionada == "" || codigoOrganizacao == organizacaoSelecionada;

            if (buscaOk && organizacaoOk) {
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
    organizacao.addEventListener("change", filtrar);

    filtrar();
</script>
</body>
</html>
