
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="plano"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar plano - Zera</title>
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

        <h1 class="nome-da-area">Editar plano</h1>

        <c:choose>
            <c:when test="${empty plano}">
                <p>Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/plano/alterar" method="post">
                        <input type="hidden" name="codigo" value="${plano.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-coluna">
                            <div class="filtrador">
                                <label for="nome">Nome</label>

                                <input type="text" id="nome" name="nome"
                                       class="form-campo-total"
                                       value="<c:out value='${plano.nome}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="valor">Valor</label>

                                <input type="number" id="valor" name="valor"
                                       class="form-campo-total"
                                       value="<c:out value='${plano.valor}'/>"
                                       placeholder="" min="0" step="0.01" required>
                            </div>

                            <div class="filtrador">
                                <label for="tempo">Período</label>

                                <select id="tempo" name="tempo" required>
                                    <option value="mensal"
                                        ${fn:toLowerCase(plano.tempo) == "mensal" ? "selected" : ""}>
                                        Mensal
                                    </option>

                                    <option value="anual"
                                        ${fn:toLowerCase(plano.tempo) == "anual" ? "selected" : ""}>
                                        Anual
                                    </option>

                                    <c:if test="${not empty plano.tempo and fn:toLowerCase(plano.tempo) != 'mensal' and fn:toLowerCase(plano.tempo) != 'anual'}">
                                        <option value="${fn:escapeXml(plano.tempo)}" selected>
                                            <c:out value="${plano.tempo}"/> meses
                                        </option>
                                    </c:if>
                                </select>
                            </div>
                        </div>

                        <button type="submit" id="botao-salvar">Salvar</button>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>
</body>
</html>
