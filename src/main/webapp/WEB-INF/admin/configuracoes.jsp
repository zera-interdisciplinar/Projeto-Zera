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
                                placeholder="Digite seu nome completo"
                                required>
                    </div>

                    <div class="campo-grupo">
                        <label for="email">E-mail</label>
                        <input
                                type="email"
                                id="email"
                                name="email"
                                value="<c:out value='${sessionScope.adminLogado.email}'/>"
                                placeholder="Digite seu e-mail"
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
                        <div class="campo-senha-container">
                            <input
                                    type="password"
                                    id="senhaAtual"
                                    name="senhaAtual"
                                    placeholder="Digite sua senha atual"
                                    required>
                            <button type="button" class="btn-olho-senha" aria-label="Mostrar ou ocultar senha">
                                <img src="${ctx}/images/icone-ver.svg"
                                     data-icone-ver="${ctx}/images/icone-ver.svg"
                                     data-icone-naover="${ctx}/images/icone-naover.svg"
                                     alt="Ver senha">
                            </button>
                        </div>
                    </div>

                    <div class="campo-grupo">
                        <label for="novaSenha">Nova senha</label>
                        <div class="campo-senha-container">
                            <input
                                    type="password"
                                    id="novaSenha"
                                    name="novaSenha"
                                    placeholder="Digite a nova senha"
                                    required>
                            <button type="button" class="btn-olho-senha" aria-label="Mostrar ou ocultar senha">
                                <img src="${ctx}/images/icone-ver.svg"
                                     data-icone-ver="${ctx}/images/icone-ver.svg"
                                     data-icone-naover="${ctx}/images/icone-naover.svg"
                                     alt="Ver senha">
                            </button>
                        </div>
                    </div>

                    <div class="campo-grupo">
                        <label for="confirmacao">Confirmar nova senha</label>
                        <div class="campo-senha-container">
                            <input
                                    type="password"
                                    id="confirmacao"
                                    name="confirmacao"
                                    placeholder="Confirme a nova senha"
                                    required>
                            <button type="button" class="btn-olho-senha" aria-label="Mostrar ou ocultar senha">
                                <img src="${ctx}/images/icone-ver.svg"
                                     data-icone-ver="${ctx}/images/icone-ver.svg"
                                     data-icone-naover="${ctx}/images/icone-naover.svg"
                                     alt="Ver senha">
                            </button>
                        </div>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar-senha">Alterar senha</button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.querySelectorAll('.btn-olho-senha').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const container = btn.closest('.campo-senha-container');
            const input = container.querySelector('input');
            const img = btn.querySelector('img');

            const iconeVer = img.getAttribute('data-icone-ver');
            const iconeNaoVer = img.getAttribute('data-icone-naover');

            if (input.type === 'password') {
                input.type = 'text';
                img.src = iconeNaoVer;
                img.alt = 'Ocultar senha';
            } else {
                input.type = 'password';
                img.src = iconeVer;
                img.alt = 'Ver senha';
            }
        });
    });
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>