<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="plano"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novo plano - Zera</title>
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

        <a href="${ctx}/plano/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Novo plano</h1>

        <section class="adicao">
            <form action="${ctx}/plano/inserir" method="post" class="form-crud">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">
                    <div class="campo-grupo campo-cheio">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome"
                               placeholder="Nome do plano (Ex: Plano Básico)" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="valor">Valor (R$)</label>
                        <input type="number" id="valor" name="valor"
                               min="0" step="0.01" placeholder="0,00" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="tempo">Período</label>
                        <select id="tempo" name="tempo" required>
                            <option value="" disabled selected>Selecione a frequência</option>
                            <option value="1 mês">Mensal</option>
                            <option value="1 ano">Anual</option>
                        </select>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar">Cadastrar</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>