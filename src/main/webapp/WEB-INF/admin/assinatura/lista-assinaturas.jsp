
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
        <h1 class="nome-da-area">Assinaturas</h1>
        <section class="tabela">
            <form class="tabela-filtros" id="form-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="buscar" name="buscar" placeholder="Organização ou plano">
                        <button type="submit" class="btn-lupa" aria-label="Buscar">
                            <img src="${ctx}/images/icone-lupa.svg" alt="">
                        </button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="situacao">Situação</label>
                    <select id="situacao" name="situacao">
                        <option value="todos">Todas</option>
                        <option value="ativa">Ativa</option>
                        <option value="cancelada">Cancelada</option>
                        <option value="expirada">Expirada</option>
                        <option value="vencendo">Vencendo em 30 dias</option>
                    </select>
                </div>
                <a href="${ctx}/assinatura/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    Nova assinatura
                </a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>Organização</th>
                        <th>Plano</th>
                        <th>Início</th>
                        <th>Fim</th>
                        <th>Status</th>
                        <th class="col-acoes">Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${assinaturas}">
                        <tr class="linha-assinatura" data-status="<c:out value='${fn:toLowerCase(item.status)}'/>" data-fim="${item.dataFim}">
                            <td><c:out value="${item.codigo}"/></td>
                            <td>
                                <c:set var="organizacaoEncontrada" value="false"/>
                                <c:forEach var="org" items="${organizacoes}">
                                    <c:if test="${org.codigo == item.codOrganizacao}">
                                        <c:out value="${org.nome}"/>
                                        <c:set var="organizacaoEncontrada" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${not organizacaoEncontrada}"><c:out value="${item.codOrganizacao}"/></c:if>
                            </td>
                            <td>
                                <c:set var="planoEncontrado" value="false"/>
                                <c:forEach var="plano" items="${planos}">
                                    <c:if test="${plano.codigo == item.codPlano}">
                                        <c:out value="${plano.nome}"/>
                                        <c:set var="planoEncontrado" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${not planoEncontrado}"><c:out value="${item.codPlano}"/></c:if>
                            </td>
                            <td><c:out value="${item.dataInicio}"/></td>
                            <td><c:out value="${item.dataFim}"/></td>
                            <td><c:out value="${item.status}"/></td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="${ctx}/assinatura/perfil?codigo=${item.codigo}" title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>
                                    <form action="${ctx}/assinatura/excluir" method="post" onsubmit="return confirm('Confirma a exclusão desta assinatura?');">
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
                        <td colspan="7">Nenhuma assinatura encontrada.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<script>
    let busca = document.getElementById("buscar");
    let situacao = document.getElementById("situacao");
    let linhas = document.querySelectorAll(".linha-assinatura");
    let aviso = document.getElementById("sem-resultados");

    function filtrar() {
        let termo = busca.value.toLowerCase().trim();
        let filtro = situacao.value;
        let quantidade = 0;

        let hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        let limite = new Date();
        limite.setHours(0, 0, 0, 0);
        limite.setDate(limite.getDate() + 30);

        for (let i = 0; i < linhas.length; i++) {
            let linha = linhas[i];
            let texto = linha.innerText.toLowerCase();
            let status = linha.getAttribute("data-status").toLowerCase().trim();
            let dataFim = linha.getAttribute("data-fim");

            let buscaOk = texto.includes(termo);
            let situacaoOk = false;

            if (filtro == "todos") {
                situacaoOk = true;
            } else if (filtro == "ativa") {
                situacaoOk = status == "ativa";
            } else if (filtro == "cancelada") {
                situacaoOk = status == "cancelada";
            } else if (filtro == "expirada") {
                situacaoOk = status == "expirada";
            } else if (filtro == "vencendo") {
                if (dataFim != null && dataFim != "") {
                    let data = new Date(dataFim + "T00:00:00");
                    if (status == "ativa" && data >= hoje && data <= limite) {
                        situacaoOk = true;
                    }
                }
            }

            if (buscaOk && situacaoOk) {
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
    situacao.addEventListener("change", filtrar);

    filtrar();
</script>
</body>
</html>
