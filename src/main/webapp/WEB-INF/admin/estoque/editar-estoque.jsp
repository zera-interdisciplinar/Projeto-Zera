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
    <title>Editar estoque - Zera</title>
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

        <a href="${ctx}/estoque/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar estoque</h1>

        <c:choose>
            <c:when test="${empty estoque}">
                <p class="mensagem-erro-registro">Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/estoque/alterar" method="post" class="form-crud">
                        <input type="hidden" name="codigo" value="${estoque.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert" class="alerta-erro">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo-grupo campo-cheio">
                                <label for="codUnidade">Unidade</label>
                                <select id="codUnidade" name="codUnidade" required>
                                    <option value="" disabled>Selecione a unidade</option>
                                    <c:forEach var="item" items="${unidades}">
                                        <option value="${item.codigo}"
                                            ${estoque.codUnidade == item.codigo ? "selected" : ""}>
                                            <c:out value="${item.cnpj}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="campo-grupo">
                                <label for="espacoNaUnidade">Espaço na unidade</label>
                                <input type="text" id="espacoNaUnidade" name="espacoNaUnidade"
                                       value="<c:out value='${estoque.espacoNaUnidade}'/>"
                                       placeholder="Galpão 3, sala B" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="codResponsavel">Gestor responsável</label>
                                <select id="codResponsavel" name="codResponsavel" required>
                                    <option value="" disabled>Selecione o gestor</option>
                                    <c:forEach var="item" items="${gestores}">
                                        <option value="${item.codigo}"
                                            ${estoque.codResponsavel == item.codigo ? "selected" : ""}>
                                            <c:out value="${item.nome}"/>
                                        </option>
                                    </c:forEach>
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
</body>
</html>