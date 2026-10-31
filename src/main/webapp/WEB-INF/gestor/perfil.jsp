
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

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
    <link rel="stylesheet" href="${ctx}/css/popup.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>

<body>

<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Meu perfil</h1>

    <%-- EDITAR PERFIL --%>

    <section class="tabela">
        <label class="tabela-titulo">Meu perfil</label>

        <form id="form-perfil" action="${ctx}/gestor/alterar" method="post">
            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome"
                           class="form-campo-total"
                           value="<c:out value='${gestor.nome}'/>"
                           required maxlength="150" autocomplete="name">
                </div>

                <div class="filtrador">
                    <label for="email">E-mail</label>
                    <input type="email" id="email" name="email"
                           class="form-campo-total"
                           value="<c:out value='${gestor.email}'/>"
                           required maxlength="254" autocomplete="email">
                </div>

                <div class="filtrador">
                    <label for="telefone">Telefone</label>
                    <input type="tel" id="telefone" name="telefone"
                           class="form-campo-total"
                           placeholder="(00) 00000-0000"
                           value="<c:out value='${gestor.telefone}'/>"
                           inputmode="numeric" maxlength="15"
                           autocomplete="tel" required>

                    <span class="mensagem-erro" id="erro-telefone" hidden>
                        Informe um telefone com DDD e 10 ou 11 dígitos.
                    </span>
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit" class="adicionar botao-submit">
                    Salvar perfil
                </button>
            </div>
        </form>
    </section>

    <%-- ALTERAR SENHA --%>

    <section class="tabela tabela-segunda">
        <label class="tabela-titulo">Alterar senha</label>

        <form id="form-senha" action="${ctx}/gestor/alterarSenha" method="post">
            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="senhaAtual">Senha atual</label>

                    <div class="campo-senha">
                        <input type="password" id="senhaAtual" name="senhaAtual"
                               placeholder="Digite sua senha atual"
                               class="form-campo-total"
                               autocomplete="current-password" required>

                        <button type="button" class="btn-olho"
                                aria-label="Mostrar senha" aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>
                </div>
            </div>

            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="novaSenha">Nova senha</label>

                    <div class="campo-senha">
                        <input type="password" id="novaSenha" name="novaSenha"
                               placeholder="Digite sua nova senha"
                               class="form-campo-total"
                               autocomplete="new-password" required>

                        <button type="button" class="btn-olho"
                                aria-label="Mostrar senha" aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>
                </div>

                <div class="filtrador">
                    <label for="confirmacao">Confirmar nova senha</label>

                    <div class="campo-senha">
                        <input type="password" id="confirmacao" name="confirmacao"
                               placeholder="Digite sua nova senha novamente"
                               class="form-campo-total"
                               autocomplete="new-password" required>

                        <button type="button" class="btn-olho"
                                aria-label="Mostrar senha" aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>

                    <span class="mensagem-erro" id="erro-confirmacao" hidden>
                        As senhas não coincidem.
                    </span>
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
    // MOSTRAR E OCULTAR SENHAS

    document.querySelectorAll('.btn-olho').forEach((botao) => {
        botao.addEventListener('click', () => {
            const campo = botao.parentElement.querySelector('input');
            const mostrar = campo.type === 'password';

            campo.type = mostrar ? 'text' : 'password';
            botao.classList.toggle('visivel', mostrar);
            botao.setAttribute('aria-label', mostrar ? 'Ocultar senha' : 'Mostrar senha');
            botao.setAttribute('aria-pressed', String(mostrar));
        });
    });

    // VALIDAÇÃO DO TELEFONE

    const telefone = document.getElementById('telefone');
    const erroTelefone = document.getElementById('erro-telefone');

    function formatarTelefone() {
        const digitos = telefone.value.replace(/\D/g, '').slice(0, 11);

        if (digitos.length <= 2) {
            telefone.value = digitos.length ? '(' + digitos : '';
        } else if (digitos.length <= 6) {
            telefone.value = '(' + digitos.slice(0, 2) + ') ' + digitos.slice(2);
        } else if (digitos.length <= 10) {
            telefone.value = '(' + digitos.slice(0, 2) + ') ' +
                digitos.slice(2, -4) + '-' + digitos.slice(-4);
        } else {
            telefone.value = '(' + digitos.slice(0, 2) + ') ' +
                digitos.slice(2, 7) + '-' + digitos.slice(7);
        }
    }

    telefone.addEventListener('input', () => {
        formatarTelefone();
        erroTelefone.hidden = true;
        telefone.setCustomValidity('');
    });

    document.getElementById('form-perfil').addEventListener('submit', (evento) => {
        const digitos = telefone.value.replace(/\D/g, '');
        const valido = digitos.length === 10 || digitos.length === 11;

        erroTelefone.hidden = valido;
        telefone.setCustomValidity(valido ? '' : 'Informe um telefone válido com DDD.');

        if (!valido) {
            evento.preventDefault();
            telefone.reportValidity();
        }
    });

    // VALIDAÇÃO DA NOVA SENHA

    const novaSenha = document.getElementById('novaSenha');
    const confirmacao = document.getElementById('confirmacao');
    const erroConfirmacao = document.getElementById('erro-confirmacao');

    function limparErroSenha() {
        confirmacao.setCustomValidity('');
        erroConfirmacao.hidden = true;
    }

    novaSenha.addEventListener('input', limparErroSenha);
    confirmacao.addEventListener('input', limparErroSenha);

    document.getElementById('form-senha').addEventListener('submit', (evento) => {
        const iguais = novaSenha.value === confirmacao.value;

        erroConfirmacao.hidden = iguais;
        confirmacao.setCustomValidity(iguais ? '' : 'As senhas não coincidem.');

        if (!iguais) {
            evento.preventDefault();
            confirmacao.reportValidity();
        }
    });
</script>

<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>

</body>
</html>
