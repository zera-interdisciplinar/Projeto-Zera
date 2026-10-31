
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
            <form class="tabela-filtros" action="${ctx}/unidade/lista" method="get">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>

                    <input type="search" name="buscar" id="buscar"
                           value="<c:out value='${param.buscar}'/>"
                           placeholder="Pesquisar">
                </div>

                <button type="submit" class="btn-lupa">Buscar</button>

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
                    <c:choose>
                        <c:when test="${not empty unidades}">
                            <c:forEach var="item" items="${unidades}">
                                <tr>
                                    <td><c:out value="${item.codigo}"/></td>
                                    <td><c:out value="${item.cnpj}"/></td>
                                    <td><c:out value="${item.email}"/></td>
                                    <td><c:out value="${item.codOrganizacao}"/></td>

                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/unidade/perfil?codigo=${item.codigo}"
                                               title="Editar">
                                                <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                            </a>

                                            <form action="${ctx}/unidade/excluir"
                                                  method="post"
                                                  onsubmit="return confirm('Confirma a exclusão?');">

                                                <input type="hidden" name="codigo"
                                                       value="${item.codigo}">

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
                            <tr>
                                <td colspan="5">Nenhuma unidade encontrada.</td>
                            </tr>
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
