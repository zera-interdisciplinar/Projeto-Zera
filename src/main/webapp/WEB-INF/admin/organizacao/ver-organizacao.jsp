<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dados da organização - Zera</title>
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

        <a href="${ctx}/organizacao/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Dados da organização</h1>

        <c:choose>
            <c:when test="${empty organizacao}">
                <p class="mensagem-erro-registro">Organização não encontrada.</p>
            </c:when>

            <c:otherwise>
                <section class="visualizacao">
                    <div class="form-crud">
                        <div class="form-grid">
                            <div class="campo-grupo campo-cheio">
                                <label for="nome">Nome</label>
                                <input type="text" id="nome" name="nome"
                                       value="<c:out value='${organizacao.nome}'/>"
                                       readonly>
                            </div>

                            <div class="campo-grupo">
                                <label for="cnpj">CNPJ</label>
                                <input type="text" id="cnpj" name="cnpj"
                                       value="<c:out value='${organizacao.cnpj}'/>"
                                       placeholder="00.000.000/0000-00"
                                       readonly>
                            </div>

                            <div class="campo-grupo">
                                <label for="email">E-mail</label>
                                <input type="email" id="email" name="email"
                                       value="<c:out value='${organizacao.email}'/>"
                                       placeholder="contato@empresa.com"
                                       readonly>
                            </div>
                        </div>

                        <div class="form-rodape">
                            <a href="${ctx}/organizacao/editar?codigo=${organizacao.codigo}" id="botao-salvar">
                                Editar
                            </a>
                        </div>
                    </div>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>
</body>
</html>