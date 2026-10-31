
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
    <title>Novo estoque - Zera</title>
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

        <h1 class="nome-da-area">Novo estoque</h1>

        <section class="edicao">
            <form action="${ctx}/estoque/inserir" method="post">
                <c:if test="${not empty erroInterno}">
                    <p role="alert">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-coluna">
                    <div class="filtrador">
                        <label for="codUnidade">Unidade</label>

                        <select id="codUnidade" name="codUnidade"
                                class="form-campo-total" required>
                            <option value="">Selecione</option>

                            <c:forEach var="item" items="${unidades}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.cnpj}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filtrador">
                        <label for="espacoNaUnidade">Espaço na unidade</label>

                        <input type="text" id="espacoNaUnidade" name="espacoNaUnidade"
                               class="form-campo-total"
                               placeholder="Galpão 3, sala B" required>
                    </div>

                    <div class="filtrador">
                        <label for="codResponsavel">Gestor responsável</label>

                        <select id="codResponsavel" name="codResponsavel"
                                class="form-campo-total" required>
                            <option value="">Selecione</option>

                            <c:forEach var="item" items="${gestores}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="submit" id="botao-salvar">Cadastrar</button>
            </form>
        </section>
    </main>
</div>
</body>
</html>
