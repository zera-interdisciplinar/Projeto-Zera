<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="estoques"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Estoques - Zera</title>
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
        <h1 class="nome-da-area">Estoques</h1>
        <section class="tabela">
            <form action="${ctx}/estoque/lista" method="get" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="busca">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="busca" name="busca" placeholder="Código ou espaço" value="<c:out value='${param.busca}'/>">
                        <button type="submit" class="btn-lupa" aria-label="Buscar"><img src="${ctx}/images/icone-lupa.svg" alt=""></button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="responsavel">Responsável</label>
                    <select name="responsavel" id="responsavel">
                        <option value="">Todos</option>
                        <c:forEach var="gestor" items="${gestores}">
                            <option value="${gestor.codigo}" ${param.responsavel == gestor.codigo ? 'selected' : ''}><c:out value="${gestor.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="organizacao">Organização</label>
                    <select name="organizacao" id="organizacao">
                        <option value="">Todas</option>
                        <c:forEach var="org" items="${organizacoes}">
                            <option value="${org.codigo}" ${param.organizacao == org.codigo ? 'selected' : ''}><c:out value="${org.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="ordem">Ordenação</label>
                    <select name="ordem" id="ordem">
                        <option value="az" ${param.ordem != 'za' ? 'selected' : ''}>A–Z</option>
                        <option value="za" ${param.ordem == 'za' ? 'selected' : ''}>Z–A</option>
                    </select>
                </div>
                <button type="submit" class="adicionar">Filtrar</button>
                <a href="${ctx}/estoque/lista">Limpar</a>
                <a href="${ctx}/estoque/perfil" class="adicionar"><img src="${ctx}/images/icone-adicionar.svg" alt="">Novo estoque</a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead><tr><th>Código</th><th>Espaço na unidade</th><th>Unidade (CNPJ)</th><th>Organização</th><th>Responsável</th><th class="col-acoes">Ações</th></tr></thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty estoques}">
                            <c:forEach var="estoque" items="${estoques}">
                                <tr>
                                    <td><c:out value="${estoque.codigo}"/></td>
                                    <td><c:out value="${estoque.espacoNaUnidade}"/></td>
                                    <td><c:out value="${nomesUnidades[estoque.codUnidade]}"/></td>
                                    <td><c:out value="${nomesOrganizacoes[estoque.codUnidade]}"/></td>
                                    <td><c:out value="${nomesResponsaveis[estoque.codResponsavel]}"/></td>
                                    <td class="col-acoes"><div class="acoes">
                                        <a href="${ctx}/estoque/perfil?codigo=${estoque.codigo}" title="Editar"><img src="${ctx}/images/icone-editar.svg" alt="Editar"></a>
                                        <form action="${ctx}/estoque/excluir" method="post" onsubmit="return confirm('Deseja excluir este estoque?');">
                                            <input type="hidden" name="codigo" value="${estoque.codigo}">
                                            <button type="submit" title="Excluir"><img src="${ctx}/images/icone-excluir.svg" alt="Excluir"></button>
                                        </form>
                                    </div></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise><tr><td colspan="6">Nenhum estoque encontrado.</td></tr></c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
</body>
</html>
