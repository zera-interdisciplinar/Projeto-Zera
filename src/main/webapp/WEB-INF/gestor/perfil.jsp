
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="perfil"/>

<c:set var="erroSenhaAtual"
       value="${param.erroSenha == 'atual' or param.erroSenha == 'atualVazia'}"/>

<c:set var="erroNovaSenha"
       value="${param.erroSenha == 'formato' or param.erroSenha == 'novaVazia'}"/>

<c:set var="erroConfirmacaoSenha"
       value="${param.erroSenha == 'confirmacao' or param.erroSenha == 'confirmacaoVazia'}"/>

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

    <style>
        .filtrador.senha-invalida .campo-senha input {
            border-color: #dc2626;
        }

        .filtrador.senha-invalida > label {
            color: #dc2626;
        }

        .filtrador .erro-senha {
            display: block;
            margin-top: 5px;
            color: #dc2626;
            font-size: 14px;
            font-weight: 400;
            line-height: 1.4;
        }

        .filtrador .erro-senha[hidden] {
            display: none;
        }
    </style>
</head>

<body>

<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>

<main class="informacoes tela-scroll">
    <img src="${ctx}/images/z-transparente.png"
         alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Meu perfil</h1>

    <%-- EDITAR PERFIL --%>

    <section class="tabela">
        <label class="tabela-titulo">Meu perfil</label>

        <form id="form-perfil"
              action="${ctx}/gestor/alterar"
              method="post">

            <div class="form-grid-duplo">

                <div class="filtrador">
                    <label for="nome">Nome</label>

                    <input type="text"
                           id="nome"
                           name="nome"
                           class="form-campo-total"
                           value="<c:out value='${gestor.nome}'/>"
                           required
                           maxlength="150"
                           autocomplete="name">
                </div>

                <div class="filtrador">
                    <label for="email">E-mail</label>

                    <input type="email"
                           id="email"
                           name="email"
                           class="form-campo-total"
                           value="<c:out value='${gestor.email}'/>"
                           required
                           maxlength="254"
                           autocomplete="email">
                </div>

                <div class="filtrador">
                    <label for="telefone">Telefone</label>

                    <input type="tel"
                           id="telefone"
                           name="telefone"
                           class="form-campo-total"
                           placeholder="(00) 00000-0000"
                           value="<c:out value='${gestor.telefone}'/>"
                           inputmode="numeric"
                           maxlength="15"
                           autocomplete="tel"
                           required>

                    <span class="mensagem-erro"
                          id="erro-telefone"
                          hidden>
                        Informe um telefone com DDD e 10 ou 11 dígitos.
                    </span>
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit"
                        class="adicionar botao-submit">
                    Salvar perfil
                </button>
            </div>
        </form>
    </section>

    <%-- ALTERAR SENHA --%>

    <section class="tabela tabela-segunda">
        <label class="tabela-titulo">Alterar senha</label>

        <form id="form-senha"
              action="${ctx}/gestor/alterarSenha"
              method="post">

            <div class="form-grid-duplo">

                <div class="filtrador<c:if test='${erroSenhaAtual}'> senha-invalida</c:if>"
                     id="grupo-senha-atual">

                    <label for="senhaAtual">Senha atual</label>

                    <div class="campo-senha">
                        <input type="password"
                               id="senhaAtual"
                               name="senhaAtual"
                               placeholder="Digite sua senha atual"
                               class="form-campo-total"
                               autocomplete="current-password"
                               aria-describedby="erro-senha-atual"
                               aria-invalid="${erroSenhaAtual}"
                               required>

                        <button type="button"
                                class="btn-olho"
                                aria-label="Mostrar senha"
                                aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>

                    <span class="erro-senha"
                          id="erro-senha-atual"
                          role="alert"
                          <c:if test="${not erroSenhaAtual}">hidden</c:if>>
                        <c:choose>
                            <c:when test="${param.erroSenha == 'atualVazia'}">
                                Informe sua senha atual.
                            </c:when>
                            <c:otherwise>
                                Senha atual incorreta.
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>

            <div class="form-grid-duplo">

                <div class="filtrador<c:if test='${erroNovaSenha}'> senha-invalida</c:if>"
                     id="grupo-nova-senha">

                    <label for="novaSenha">Nova senha</label>

                    <div class="campo-senha">
                        <input type="password"
                               id="novaSenha"
                               name="novaSenha"
                               placeholder="Digite sua nova senha"
                               class="form-campo-total"
                               autocomplete="new-password"
                               aria-describedby="erro-nova-senha"
                               aria-invalid="${erroNovaSenha}"
                               required>

                        <button type="button"
                                class="btn-olho"
                                aria-label="Mostrar senha"
                                aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>

                    <span class="erro-senha"
                          id="erro-nova-senha"
                          role="alert"
                          <c:if test="${not erroNovaSenha}">hidden</c:if>>
                        <c:choose>
                            <c:when test="${param.erroSenha == 'novaVazia'}">
                                Informe a nova senha.
                            </c:when>
                            <c:otherwise>
                                A senha precisa ter pelo menos 8 caracteres,
                                um número e um caractere especial.
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="filtrador<c:if test='${erroConfirmacaoSenha}'> senha-invalida</c:if>"
                     id="grupo-confirmacao">

                    <label for="confirmacao">
                        Confirmar nova senha
                    </label>

                    <div class="campo-senha">
                        <input type="password"
                               id="confirmacao"
                               name="confirmacao"
                               placeholder="Digite sua nova senha novamente"
                               class="form-campo-total"
                               autocomplete="new-password"
                               aria-describedby="erro-confirmacao"
                               aria-invalid="${erroConfirmacaoSenha}"
                               required>

                        <button type="button"
                                class="btn-olho"
                                aria-label="Mostrar senha"
                                aria-pressed="false">
                            <img src="${ctx}/images/icone-ver.svg"
                                 class="icone-olho" alt="">
                        </button>
                    </div>

                    <span class="erro-senha"
                          id="erro-confirmacao"
                          role="alert"
                          <c:if test="${not erroConfirmacaoSenha}">hidden</c:if>>
                        <c:choose>
                            <c:when test="${param.erroSenha == 'confirmacaoVazia'}">
                                Confirme sua nova senha.
                            </c:when>
                            <c:otherwise>
                                As senhas não coincidem.
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit"
                        class="adicionar botao-submit">
                    Alterar senha
                </button>
            </div>
        </form>
    </section>
</main>

</div>

<script>
    // MOSTRAR E OCULTAR SENHAS

    document.querySelectorAll('.btn-olho').forEach(function (botao) {
        botao.addEventListener('click', function () {
            const campo = botao.parentElement.querySelector('input');
            const mostrar = campo.type === 'password';

            campo.type = mostrar ? 'text' : 'password';

            botao.classList.toggle('visivel', mostrar);

            botao.setAttribute(
                'aria-label',
                mostrar ? 'Ocultar senha' : 'Mostrar senha'
            );

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

    telefone.addEventListener('input', function () {
        formatarTelefone();
        erroTelefone.hidden = true;
        telefone.setCustomValidity('');
    });

    document.getElementById('form-perfil').addEventListener('submit', function (evento) {
        const digitos = telefone.value.replace(/\D/g, '');
        const valido = digitos.length === 10 || digitos.length === 11;

        erroTelefone.hidden = valido;

        telefone.setCustomValidity(
            valido ? '' : 'Informe um telefone válido com DDD.'
        );

        if (!valido) {
            evento.preventDefault();
            telefone.reportValidity();
        }
    });

    // VALIDAÇÃO DAS SENHAS

    const senhaAtual = document.getElementById('senhaAtual');
    const novaSenha = document.getElementById('novaSenha');
    const confirmacao = document.getElementById('confirmacao');

    const erroSenhaAtual = document.getElementById('erro-senha-atual');
    const erroNovaSenha = document.getElementById('erro-nova-senha');
    const erroConfirmacao = document.getElementById('erro-confirmacao');

    function mostrarErroSenha(campo, mensagem, texto) {
        const grupo = campo.closest('.filtrador');

        if (texto) {
            mensagem.textContent = texto;
            mensagem.hidden = false;
            grupo.classList.add('senha-invalida');
            campo.setAttribute('aria-invalid', 'true');
        } else {
            mensagem.hidden = true;
            grupo.classList.remove('senha-invalida');
            campo.setAttribute('aria-invalid', 'false');
        }
    }

    senhaAtual.addEventListener('input', function () {
        mostrarErroSenha(senhaAtual, erroSenhaAtual, '');
    });

    novaSenha.addEventListener('input', function () {
        mostrarErroSenha(novaSenha, erroNovaSenha, '');
        mostrarErroSenha(confirmacao, erroConfirmacao, '');
    });

    confirmacao.addEventListener('input', function () {
        mostrarErroSenha(confirmacao, erroConfirmacao, '');
    });

    document.getElementById('form-senha').addEventListener('submit', function (evento) {
        const senha = novaSenha.value;
        const iguais = senha === confirmacao.value;

        const formatoValido =
            senha.length >= 8 &&
            /[0-9]/.test(senha) &&
            /[^A-Za-z0-9]/.test(senha);

        if (!formatoValido) {
            evento.preventDefault();

            mostrarErroSenha(
                novaSenha,
                erroNovaSenha,
                'A senha precisa ter pelo menos 8 caracteres, um número e um caractere especial.'
            );

            novaSenha.focus();
            return;
        }

        if (!iguais) {
            evento.preventDefault();

            mostrarErroSenha(
                confirmacao,
                erroConfirmacao,
                'As senhas não coincidem.'
            );

            confirmacao.focus();
        }
    });
</script>

<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>

</body>
</html>
