
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="admin"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Adicionar administrador - Zera</title>

    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">

    <style>
        /* ========================================
           FORMULÁRIO DE CADASTRO DO ADMINISTRADOR
        ======================================== */

        .adicao.adicao-admin {
            display: block;
            min-width: 0;
            box-sizing: border-box;
        }

        .adicao-admin .form-crud {
            display: flex;
            flex-direction: column;
            width: 100%;
            gap: 28px;
        }

        .adicao-admin .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 24px 28px;
            width: 100%;
        }

        .adicao-admin .campo-grupo {
            display: flex;
            flex-direction: column;
            min-width: 0;
            width: 100%;
            gap: 8px;
        }

        .adicao-admin .campo-grupo label {
            margin: 0;
            color: #152D6B;
            font-size: 20px;
            font-weight: 400;
        }

        .adicao-admin .campo-grupo input {
            display: block;
            width: 100%;
            min-width: 0;
            height: 54px;
            padding: 0 18px;
            box-sizing: border-box;
            border: 2px solid #152D6B;
            border-radius: 10px;
            background: #FFFFFF;
            color: #152D6B;
            font-family: var(--fonte1);
            font-size: 16px;
        }

        .adicao-admin .campo-grupo input:focus {
            outline: none;
            border-color: #2958D1;
            box-shadow: 0 0 0 2px rgba(41, 88, 209, 0.15);
        }

        .adicao-admin .campo-grupo input::placeholder {
            color: rgba(21, 45, 107, 0.45);
        }

        .adicao-admin .form-rodape {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            width: 100%;
            margin-top: 12px;
        }

        .adicao-admin #botao-salvar {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 160px;
            height: 54px;
            padding: 0 25px;
            border: 2px solid #2958D1;
            border-radius: 20px;
            background: #2958D1;
            color: #FFFFFF;
            font-family: var(--fonte1);
            font-size: 17px;
            font-weight: 700;
            cursor: pointer;
            transition: background-color 0.2s, color 0.2s;
        }

        .adicao-admin #botao-salvar:hover {
            background: transparent;
            color: #2958D1;
        }

        .adicao-admin .alerta-erro {
            width: 100%;
            box-sizing: border-box;
        }

        /* ========================================
           RESPONSIVIDADE
        ======================================== */

        @media (max-width: 900px) {
            .adicao-admin .form-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }
        }

        @media (max-width: 600px) {
            .adicao-admin .form-rodape {
                justify-content: stretch;
            }

            .adicao-admin #botao-salvar {
                width: 100%;
            }
        }
    </style>
</head>

<body>
<div class="tela-auth">

    <%-- MENU LATERAL DO ADMINISTRADOR --%>
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${ctx}/images/logo_completa.svg"
                 alt="Zera"
                 class="logo-zera">

            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
        </div>
    </aside>

    <%-- CONTEÚDO PRINCIPAL --%>
    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png"
             alt=""
             class="z-marca-dagua">

        <h1 class="nome-da-area">Adicionar administrador</h1>

        <section class="adicao adicao-admin">
            <form action="${ctx}/admin/inserir"
                  method="post"
                  class="form-crud"
                  id="form-admin">

                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">

                    <%-- NOME DO ADMINISTRADOR --%>
                    <div class="campo-grupo">
                        <label for="nome">Nome completo</label>

                        <input type="text"
                               id="nome"
                               name="nome"
                               placeholder="Nome do administrador"
                               autocomplete="name"
                               required>
                    </div>

                    <%-- EMAIL DO ADMINISTRADOR --%>
                    <div class="campo-grupo">
                        <label for="email">E-mail</label>

                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="admin@zera.com"
                               autocomplete="off"
                               required>
                    </div>

                    <%-- SENHA DO ADMINISTRADOR --%>
                    <div class="campo-grupo">
                        <label for="senha">Senha</label>

                        <input type="password"
                               id="senha"
                               name="senha"
                               placeholder="Digite a senha"
                               minlength="8"
                               autocomplete="new-password"
                               required>
                    </div>

                    <%-- CONFIRMAÇÃO DA SENHA --%>
                    <div class="campo-grupo">
                        <label for="confirmacao">Confirmar senha</label>

                        <input type="password"
                               id="confirmacao"
                               name="confirmacao"
                               placeholder="Confirme a senha"
                               minlength="8"
                               autocomplete="new-password"
                               required>
                    </div>
                </div>

                <%-- MENSAGEM DE VALIDAÇÃO --%>
                <p id="erro-senha"
                   class="alerta-erro"
                   role="alert"
                   hidden>
                    As senhas informadas não coincidem.
                </p>

                <%-- BOTÃO DE CADASTRO --%>
                <div class="form-rodape">
                    <button type="submit" id="botao-salvar">
                        Cadastrar
                    </button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    // VALIDAÇÃO DA CONFIRMAÇÃO DE SENHA

    const formularioAdmin = document.getElementById("form-admin");
    const campoSenha = document.getElementById("senha");
    const campoConfirmacao = document.getElementById("confirmacao");
    const erroSenha = document.getElementById("erro-senha");

    function validarSenhas() {
        const coincidem = campoSenha.value === campoConfirmacao.value;

        campoConfirmacao.setCustomValidity(
            coincidem ? "" : "As senhas não coincidem."
        );

        erroSenha.hidden = coincidem;

        return coincidem;
    }

    campoConfirmacao.addEventListener("input", validarSenhas);
    campoSenha.addEventListener("input", function() {
        if (campoConfirmacao.value !== "") {
            validarSenhas();
        }
    });

    formularioAdmin.addEventListener("submit", function(evento) {
        if (!validarSenhas()) {
            evento.preventDefault();
            campoConfirmacao.reportValidity();
        }
    });
</script>
</body>
</html>
