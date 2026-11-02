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

        <%-- Seção de alteração do perfil --%>
        <section class="edicao secao-config">
            <h2 class="subtitulo-secao">Meu perfil</h2>

            <form method="post" action="${ctx}/admin/alterar" class="form-crud">
                <div class="form-grid">
                    <div class="campo-grupo">
                        <label for="nome">Nome</label>
                        <input
                                type="text"
                                id="nome"
                                name="nome"
                                value="<c:out value='${sessionScope.adminLogado.nome}'/>"
                                placeholder=""
                                required>
                    </div>

                    <div class="campo-grupo">
                        <label for="email">E-mail</label>
                        <input
                                type="email"
                                id="email"
                                name="email"
                                value="<c:out value='${sessionScope.adminLogado.email}'/>"
                                placeholder=""
                                required>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar">Salvar perfil</button>
                </div>
            </form>
        </section>

        <%-- Seção de alteração de senha --%>
        <section class="edicao secao-config">
            <h2 class="subtitulo-secao">Alterar senha</h2>

            <form method="post" action="${ctx}/admin/alterarSenha" class="form-crud">
                <div class="form-grid">
                    <div class="campo-grupo campo-cheio">
                        <label for="senhaAtual">Senha atual</label>
                        <input
                                type="password"
                                id="senhaAtual"
                                name="senhaAtual"
                                placeholder=""
                                required>
                    </div>

                    <div class="campo-grupo">
                        <label for="novaSenha">Nova senha</label>
                        <input
                                type="password"
                                id="novaSenha"
                                name="novaSenha"
                                placeholder=""
                                required>
                    </div>

                    <div class="campo-grupo">
                        <label for="confirmacao">Confirmar nova senha</label>
                        <input
                                type="password"
                                id="confirmacao"
                                name="confirmacao"
                                placeholder=""
                                required>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar-senha">Alterar senha</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>