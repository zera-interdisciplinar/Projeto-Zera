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
    <link rel="stylesheet" href="${ctx}/css/validacao-formularios.css">
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

        <a href="${ctx}/plano/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar plano</h1>

        <c:choose>
            <c:when test="${empty plano}">
                <p class="mensagem-erro-registro">Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/plano/alterar" method="post" class="form-crud">
                        <input type="hidden" name="codigo" value="${plano.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert" class="alerta-erro">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo-grupo campo-cheio">
                                <label for="nome">Nome</label>
                                <input type="text" id="nome" name="nome"
                                       value="<c:out value='${plano.nome}'/>"
                                       placeholder="Nome do plano"
                                       required>
                            </div>

                            <div class="campo-grupo">
                                <label for="valor">Valor</label>
                                <input type="number" id="valor" name="valor"
                                       value="<c:out value='${plano.valor}'/>"
                                       min="0" step="0.01"
                                       placeholder="0,00"
                                       required>
                            </div>

                            <div class="campo-grupo">
                                <label for="tempo">Período</label>
                                <select id="tempo" name="tempo" required>
                                    <option value="1 mês"
                                        ${fn:contains(fn:toLowerCase(plano.tempo), 'mês') or fn:contains(fn:toLowerCase(plano.tempo), 'mes') or plano.tempo == '1' ? "selected" : ""}>
                                        Mensal
                                    </option>

                                    <option value="1 ano"
                                        ${fn:contains(fn:toLowerCase(plano.tempo), 'ano') or plano.tempo == '12' ? "selected" : ""}>
                                        Anual
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-rodape">
                            <button type="submit" id="botao-salvar">Salvar</button>
                        </div>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>

<script>
    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>