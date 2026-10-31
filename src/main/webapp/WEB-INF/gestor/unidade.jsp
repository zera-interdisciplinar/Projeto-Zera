
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%--
    Unidade do gestor.

    GET /unidade/perfil:
    - unidade: unidade do gestor logado
    - organizacao: organização vinculada à unidade

    GET /unidade/perfil?editar=1:
    - formulário de edição

    POST /unidade/alterar:
    - cnpj
    - email

    A organização não pode ser alterada pelo gestor.
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>
<c:set var="modoEdicao" value="${param.editar == '1' and not empty unidade}"/>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Unidade - Zera</title>

    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">

    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
</head>

<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">

    <img
        src="${ctx}/images/z-transparente.png"
        alt=""
        class="z-marca-dagua"
    >

    <c:choose>

        <%-- ========================================
             EDIÇÃO DA UNIDADE
        ======================================== --%>

        <c:when test="${modoEdicao}">

            <a href="${ctx}/unidade/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>

            <h1 class="nome-da-area">Editar unidade</h1>

            <section class="tabela">

                <form
                    id="form-unidade"
                    action="${ctx}/unidade/alterar"
                    method="post"
                >

                    <div class="form-coluna">

                        <%-- Organização --%>

                        <div class="filtrador">

                            <label for="organizacao">
                                Organização
                            </label>

                            <input
                                type="text"
                                id="organizacao"
                                class="form-campo-total"
                                value="<c:out value='${organizacao.nome}'/>"
                                readonly
                            >

                        </div>

                        <%-- Email --%>

                        <div class="filtrador">

                            <label for="email">
                                Email da unidade
                            </label>

                            <input
                                type="email"
                                id="email"
                                name="email"
                                class="form-campo-total"
                                value="<c:out value='${unidade.email}'/>"
                                maxlength="254"
                                required
                            >

                            <span
                                id="erro-email"
                                class="mensagem-erro"
                                style="display: none;"
                                role="alert"
                            >
                                Email inválido, ele precisa conter @ e o domínio.
                            </span>

                        </div>

                        <%-- CNPJ --%>

                        <div class="filtrador">

                            <label for="cnpj">
                                CNPJ da unidade
                            </label>

                            <input
                                type="text"
                                id="cnpj"
                                name="cnpj"
                                class="form-campo-total"
                                inputmode="numeric"
                                maxlength="18"
                                placeholder="00.000.000/0000-00"
                                value="<c:out value='${unidade.cnpj}'/>"
                                required
                            >

                            <span
                                id="erro-cnpj"
                                class="mensagem-erro"
                                style="display: none;"
                                role="alert"
                            >
                                CNPJ inválido. Verifique os 14 dígitos.
                            </span>

                        </div>

                    </div>

                    <div class="alinhar-direita">

                        <button
                            type="submit"
                            class="adicionar botao-submit"
                        >
                            Salvar
                        </button>

                    </div>

                </form>

            </section>

        </c:when>

        <%-- ========================================
             VISUALIZAÇÃO DA UNIDADE
        ======================================== --%>

        <c:otherwise>

            <h1 class="nome-da-area">Unidade</h1>

            <section class="tabela">

                <label class="tabela-titulo">
                    Dados unidade
                </label>

                <div class="tabela-rolagem">

                    <table class="itens-tabela">

                        <thead>
                            <tr>
                                <th>Organização</th>
                                <th>CNPJ</th>
                                <th>E-mail de contato</th>
                                <th class="col-acoes"></th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:choose>

                                <c:when test="${not empty unidade}">

                                    <tr>

                                        <td>
                                            <c:out value="${organizacao.nome}"/>
                                        </td>

                                        <td>
                                            <c:out value="${unidade.cnpj}"/>
                                        </td>

                                        <td>
                                            <c:out value="${unidade.email}"/>
                                        </td>

                                        <td class="col-acoes">

                                            <div class="acoes">

                                                <a
                                                    href="${ctx}/unidade/perfil?editar=1"
                                                    title="Editar unidade"
                                                    aria-label="Editar unidade"
                                                >
                                                    <img
                                                        src="${ctx}/images/icone-editar.svg"
                                                        alt="Editar"
                                                    >
                                                </a>

                                            </div>

                                        </td>

                                    </tr>

                                </c:when>

                                <c:otherwise>

                                    <tr>
                                        <td colspan="4">
                                            Nenhuma unidade encontrada.
                                        </td>
                                    </tr>

                                </c:otherwise>

                            </c:choose>

                        </tbody>

                    </table>

                </div>

            </section>

        </c:otherwise>

    </c:choose>

</main>

</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {

        const formulario = document.getElementById("form-unidade");

        if (!formulario) {
            return;
        }

        const campoCnpj = document.getElementById("cnpj");
        const campoEmail = document.getElementById("email");

        const erroCnpj = document.getElementById("erro-cnpj");
        const erroEmail = document.getElementById("erro-email");

        function aplicarMascaraCnpj(valor) {

            const numeros = valor.replace(/\D/g, "").slice(0, 14);

            let resultado = numeros;

            if (numeros.length > 2) {
                resultado =
                    numeros.slice(0, 2) +
                    "." +
                    numeros.slice(2);
            }

            if (numeros.length > 5) {
                resultado =
                    numeros.slice(0, 2) +
                    "." +
                    numeros.slice(2, 5) +
                    "." +
                    numeros.slice(5);
            }

            if (numeros.length > 8) {
                resultado =
                    numeros.slice(0, 2) +
                    "." +
                    numeros.slice(2, 5) +
                    "." +
                    numeros.slice(5, 8) +
                    "/" +
                    numeros.slice(8);
            }

            if (numeros.length > 12) {
                resultado =
                    numeros.slice(0, 2) +
                    "." +
                    numeros.slice(2, 5) +
                    "." +
                    numeros.slice(5, 8) +
                    "/" +
                    numeros.slice(8, 12) +
                    "-" +
                    numeros.slice(12);
            }

            return resultado;
        }

        function validarCnpj(valor) {

            const cnpj = valor.replace(/\D/g, "");

            if (cnpj.length !== 14) {
                return false;
            }

            if (/^(\d)\1{13}$/.test(cnpj)) {
                return false;
            }

            function calcularDigito(tamanho) {

                let soma = 0;
                let peso = tamanho - 7;

                for (let i = 0; i < tamanho; i++) {

                    soma += Number(cnpj[i]) * peso;

                    peso--;

                    if (peso < 2) {
                        peso = 9;
                    }
                }

                const resto = soma % 11;

                return resto < 2 ? 0 : 11 - resto;
            }

            const primeiroDigito = calcularDigito(12);
            const segundoDigito = calcularDigito(13);

            return (
                primeiroDigito === Number(cnpj[12]) &&
                segundoDigito === Number(cnpj[13])
            );
        }

        function validarEmail(valor) {

            const email = valor.trim();

            return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
        }

        function mostrarErro(campo, mensagem, invalido) {

            mensagem.style.display = invalido ? "block" : "none";

            campo.setAttribute(
                "aria-invalid",
                invalido ? "true" : "false"
            );
        }

        campoCnpj.value = aplicarMascaraCnpj(campoCnpj.value);

        campoCnpj.addEventListener("input", () => {

            campoCnpj.value = aplicarMascaraCnpj(campoCnpj.value);

            mostrarErro(campoCnpj, erroCnpj, false);
        });

        campoEmail.addEventListener("input", () => {

            mostrarErro(campoEmail, erroEmail, false);
        });

        campoCnpj.addEventListener("blur", () => {

            const invalido = !validarCnpj(campoCnpj.value);

            mostrarErro(campoCnpj, erroCnpj, invalido);
        });

        campoEmail.addEventListener("blur", () => {

            const invalido = !validarEmail(campoEmail.value);

            mostrarErro(campoEmail, erroEmail, invalido);
        });

        formulario.addEventListener("submit", (evento) => {

            const cnpjValido = validarCnpj(campoCnpj.value);

            const emailValido = validarEmail(campoEmail.value);

            mostrarErro(campoCnpj, erroCnpj, !cnpjValido);

            mostrarErro(campoEmail, erroEmail, !emailValido);

            if (!cnpjValido || !emailValido) {

                evento.preventDefault();

                if (!emailValido) {
                    campoEmail.focus();
                } else {
                    campoCnpj.focus();
                }
            }
        });

    });
</script>

<script src="${ctx}/js/popup.js" defer></script>

</body>
</html>
