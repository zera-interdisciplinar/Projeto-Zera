
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="estoque"/>

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
            <form class="tabela-filtros" action="${ctx}/estoque/lista" method="get">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" name="buscar" id="buscar" value="<c:out value='${param.buscar}'/>" placeholder="Código ou espaço">
                        <button type="submit" class="btn-lupa" aria-label="Buscar">
                            <img src="${ctx}/images/icone-lupa.svg" alt="">
                        </button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="codOrganizacao">Organização</label>
                    <select id="codOrganizacao" name="codOrganizacao">
                        <option value="">Todas</option>
                        <c:forEach var="org" items="${organizacoes}">
                            <option value="${org.codigo}" ${param.codOrganizacao eq org.codigo.toString() ? 'selected' : ''}>
                                <c:out value="${org.nome}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="codUnidade">Unidade</label>
                    <select id="codUnidade" name="codUnidade">
                        <option value="">Todas</option>
                        <c:forEach var="unidade" items="${unidades}">
                            <option value="${unidade.codigo}" ${param.codUnidade eq unidade.codigo.toString() ? 'selected' : ''}>
                                <c:out value="${unidade.cnpj}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filtrador">
                    <label for="codResponsavel">Responsável</label>
                    <select id="codResponsavel" name="codResponsavel">
                        <option value="">Todos</option>
                        <c:forEach var="gestor" items="${gestores}">
                            <option value="${gestor.codigo}" ${param.codResponsavel eq gestor.codigo.toString() ? 'selected' : ''}>
                                <c:out value="${gestor.nome}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="adicionar">Filtrar</button>
                <a href="${ctx}/estoque/lista" class="adicionar">Limpar</a>
                <a href="${ctx}/estoque/perfil" class="adicionar">
                    <img src="${ctx}/images/icone-adicionar.svg" alt="">
                    Novo estoque
                </a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>Espaço na unidade</th>
                        <th>Organização</th>
                        <th>Unidade</th>
                        <th>Responsável</th>
                        <th class="col-acoes">Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty estoques}">
                            <c:forEach var="item" items="${estoques}">
                                <c:set var="nomeOrganizacao" value=""/>
                                <c:set var="nomeUnidade" value=""/>
                                <c:set var="nomeResponsavel" value=""/>
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
                                <c:forEach var="gestor" items="${gestores}">
                                    <c:if test="${gestor.codigo == item.codResponsavel}">
                                        <c:set var="nomeResponsavel" value="${gestor.nome}"/>
                                    </c:if>
                                </c:forEach>
                                <tr>
                                    <td><c:out value="${item.codigo}"/></td>
                                    <td><c:out value="${item.espacoNaUnidade}"/></td>
                                    <td><c:out value="${nomeOrganizacao}" default="Não informada"/></td>
                                    <td><c:out value="${nomeUnidade}" default="Não informada"/></td>
                                    <td><c:out value="${nomeResponsavel}" default="Não informado"/></td>
                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/estoque/perfil?codigo=${item.codigo}" title="Editar">
                                                <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                            </a>
                                            <form action="${ctx}/estoque/excluir" method="post" onsubmit="return confirm('Confirma a exclusão deste estoque?');">
                                                <input type="hidden" name="codigo" value="${item.codigo}">
                                                <button type="submit" aria-label="Excluir">
                                                    <img src="${ctx}/images/icone-excluir.svg" alt="">
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="6">Nenhum estoque encontrado para os filtros selecionados.</td></tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
</body>
</html>
