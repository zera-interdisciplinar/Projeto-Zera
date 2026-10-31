
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

        <h1 class="nome-da-area">Editar endereço</h1>

        <c:choose>
            <c:when test="${empty endereco}">
                <p>Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/endereco/alterar" method="post">
                        <input type="hidden" name="codigo" value="${endereco.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-coluna">
                            <div class="filtrador">
                                <label for="logradouro">Logradouro</label>
                                <input type="text" id="logradouro" name="logradouro"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.logradouro}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="numero">Número</label>
                                <input type="number" id="numero" name="numero"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.numero}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="bairro">Bairro</label>
                                <input type="text" id="bairro" name="bairro"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.bairro}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="cidade">Cidade</label>
                                <input type="text" id="cidade" name="cidade"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.cidade}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="estado">Estado</label>
                                <input type="text" id="estado" name="estado"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.estado}'/>"
                                       placeholder="" required>
                            </div>

                            <div class="filtrador">
                                <label for="cep">CEP</label>
                                <input type="text" id="cep" name="cep"
                                       class="form-campo-total"
                                       value="<c:out value='${endereco.cep}'/>"
                                       placeholder="" required>
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
