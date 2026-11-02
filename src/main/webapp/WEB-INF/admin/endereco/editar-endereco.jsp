<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="endereco"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar endereço - Zera</title>
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

        <a href="${ctx}/endereco/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar endereço</h1>

        <c:choose>
            <c:when test="${empty endereco}">
                <p class="mensagem-erro-registro">Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/endereco/alterar" method="post" class="form-crud">
                        <input type="hidden" name="codigo" value="${endereco.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert" class="alerta-erro">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo-grupo">
                                <label for="logradouro">Logradouro</label>
                                <input type="text" id="logradouro" name="logradouro"
                                       value="<c:out value='${endereco.logradouro}'/>"
                                       placeholder="Rua, Avenida, Alameda" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="numero">Número</label>
                                <input type="number" id="numero" name="numero"
                                       value="<c:out value='${endereco.numero}'/>"
                                       placeholder="123" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="bairro">Bairro</label>
                                <input type="text" id="bairro" name="bairro"
                                       value="<c:out value='${endereco.bairro}'/>"
                                       placeholder="Bairro" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="cidade">Cidade</label>
                                <input type="text" id="cidade" name="cidade"
                                       value="<c:out value='${endereco.cidade}'/>"
                                       placeholder="Cidade" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="estado">Estado</label>
                                <input type="text" id="estado" name="estado"
                                       value="<c:out value='${endereco.estado}'/>"
                                       placeholder="UF (Ex: SP)" maxlength="2" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="cep">CEP</label>
                                <input type="text" id="cep" name="cep"
                                       value="<c:out value='${endereco.cep}'/>"
                                       placeholder="00000-000" maxlength="9" required>
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