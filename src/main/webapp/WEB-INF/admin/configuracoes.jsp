
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="configuracoes"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configurações - Zera</title>
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

        <h1 class="nome-da-area">Configurações</h1>

        <section class="tabela">
            <h2>Meu perfil</h2>

            <form method="post" action="${ctx}/admin/alterar">
                <div class="filtrador">
                    <label for="nome">Nome</label>

                    <input
                            class="form-campo-total"
                            type="text"
                            id="nome"
                            name="nome"
                            value="<c:out value='${sessionScope.adminLogado.nome}'/>"
                            placeholder=""
                            required>
                </div>

                <div class="filtrador">
                    <label for="email">E-mail</label>

                    <input
                            class="form-campo-total"
                            type="email"
                            id="email"
                            name="email"
                            value="<c:out value='${sessionScope.adminLogado.email}'/>"
                            placeholder=""
                            required>
                </div>

                <button type="submit">Salvar perfil</button>
            </form>
        </section>

        <section class="tabela">
            <h2>Alterar senha</h2>

            <form method="post" action="${ctx}/admin/alterarSenha">
                <div class="filtrador">
                    <label for="senhaAtual">Senha atual</label>

                    <input
                            class="form-campo-total"
                            type="password"
                            id="senhaAtual"
                            name="senhaAtual"
                            placeholder=""
                            required>
                </div>

                <div class="filtrador">
                    <label for="novaSenha">Nova senha</label>

                    <input
                            class="form-campo-total"
                            type="password"
                            id="novaSenha"
                            name="novaSenha"
                            placeholder=""
                            required>
                </div>

                <div class="filtrador">
                    <label for="confirmacao">Confirmar nova senha</label>

                    <input
                            class="form-campo-total"
                            type="password"
                            id="confirmacao"
                            name="confirmacao"
                            placeholder=""
                            required>
                </div>

                <button type="submit">Alterar senha</button>
            </form>
        </section>
    </main>
</div>
</body>
</html>
