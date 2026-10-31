<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Meu perfil (gestor).

    Recebe do ReadGestorServlet (GET /gestor/perfil):
    - gestor: dados do gestor logado (sem a senha)

    Formulário 1 envia para o UpdateGestorServlet (POST /gestor/alterar):
    nome, email, telefone.

    Formulário 2 envia para o UpdateSenhaGestorServlet (POST /gestor/alterarSenha):
    senhaAtual, novaSenha, confirmacao.

    Os dois servlets voltam para /gestor/perfil quando dá certo.
    Se der erro, mostram a página de erro com a mensagem.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="perfil"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meu Perfil - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Meu perfil</h1>

    <section class="tabela">
        <label class="tabela-titulo">Meu perfil</label>

        <form action="${ctx}/gestor/alterar" method="post">
            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome" class="form-campo-total"
                           value="<c:out value='${gestor.nome}'/>">
                </div>
                <div class="filtrador">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email" class="form-campo-total"
                           value="<c:out value='${gestor.email}'/>">
                </div>
                <div class="filtrador">
                    <label for="telefone">Telefone</label>
                    <input type="tel" id="telefone" name="telefone" class="form-campo-total"
                           placeholder="(00) 00000-0000" value="<c:out value='${gestor.telefone}'/>">
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit" class="adicionar botao-submit">
                    Salvar perfil
                </button>
            </div>
        </form>
    </section>

    <section class="tabela tabela-segunda">
        <label class="tabela-titulo">Alterar senha</label>

        <%-- os campos de senha nunca vêm preenchidos --%>
        <form action="${ctx}/gestor/alterarSenha" method="post">
            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="senhaAtual">Senha atual</label>
                    <div class="campo-senha">
                        <input type="password" id="senhaAtual" name="senhaAtual"
                               placeholder="Digite sua senha atual" class="form-campo-total">
                        <button type="button" class="btn-olho" aria-label="Mostrar senha">
                            <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                        </button>
                    </div>
                </div>
            </div>

            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="novaSenha">Nova senha</label>
                    <div class="campo-senha">
                        <input type="password" id="novaSenha" name="novaSenha"
                               placeholder="Digite sua nova senha" class="form-campo-total">
                        <button type="button" class="btn-olho" aria-label="Mostrar senha">
                            <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                        </button>
                    </div>
                </div>
                <div class="filtrador">
                    <label for="confirmacao">Confirmar nova senha</label>
                    <div class="campo-senha">
                        <input type="password" id="confirmacao" name="confirmacao"
                               placeholder="Digite sua nova senha novamente" class="form-campo-total">
                        <button type="button" class="btn-olho" aria-label="Mostrar senha">
                            <img src="${ctx}/images/icone-ver.svg" class="icone-olho" alt="">
                        </button>
                    </div>
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit" class="adicionar botao-submit">
                    Alterar senha
                </button>
            </div>
        </form>
    </section>
</main>
</div>

<script>
    // botão do olho: mostra ou esconde a senha do campo ao lado
    // a classe "visivel" troca o ícone para o olho fechado (está no crud.css)
    document.querySelectorAll('.btn-olho').forEach((botao) => {
        botao.addEventListener('click', () => {
            const campo = botao.parentElement.querySelector('input');
            if (campo.type === 'password') {
                campo.type = 'text';
            } else {
                campo.type = 'password';
            }
            botao.classList.toggle('visivel');
        });
    });
</script>
</body>
</html>
