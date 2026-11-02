<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="gestor"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar gestor - Zera</title>
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
        <a href="${ctx}/gestor/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>
        <h1 class="nome-da-area">Editar gestor</h1>

        <c:choose>
            <c:when test="${empty gestor}">
                <p class="mensagem-erro-registro">Registro não encontrado. Volte para a listagem.</p>
            </c:when>
            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/gestor/alterar" method="post" class="form-crud" id="form-editar-gestor">
                        <input type="hidden" name="codigo" value="${gestor.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert" class="alerta-erro">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo-grupo">
                                <label for="nome">Nome completo</label>
                                <input type="text" id="nome" name="nome"
                                       value="<c:out value='${gestor.nome}'/>"
                                       placeholder="Digite o nome completo" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="email">E-mail</label>
                                <input type="email" id="email" name="email"
                                       value="<c:out value='${gestor.email}'/>"
                                       placeholder="gestor@empresa.com" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="telefone">Telefone</label>
                                <input type="tel" id="telefone" name="telefone"
                                       value="<c:out value='${gestor.telefone}'/>"
                                       placeholder="(00) 00000-0000" required>
                            </div>

                            <div class="campo-grupo">
                                <label for="codUnidade">Unidade</label>
                                <select id="codUnidade" name="codUnidade" required>
                                    <option value="" disabled>Selecione a unidade</option>
                                    <c:forEach var="item" items="${unidades}">
                                        <option value="${item.codigo}"
                                            ${gestor.codUnidade == item.codigo ? "selected" : ""}>
                                            <c:out value="${item.cnpj}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="campo-grupo">
                                <label for="senha">Nova senha (opcional)</label>
                                <div class="campo-senha-container">
                                    <input type="password" id="senha" name="senha"
                                           placeholder="Digite uma nova senha"
                                           aria-describedby="erro-senha">
                                    <button type="button" class="btn-olho-senha" aria-label="Mostrar ou ocultar senha">
                                        <img src="${ctx}/images/icone-ver.svg"
                                             data-icone-ver="${ctx}/images/icone-ver.svg"
                                             data-icone-naover="${ctx}/images/icone-naover.svg"
                                             alt="Ver senha">
                                    </button>
                                </div>
                                <p id="erro-senha" role="alert" hidden
                                   style="color:#c62828;font-size:14px;margin-top:6px;"></p>
                            </div>

                            <div class="campo-grupo">
                                <label for="confirmacao">Confirmar nova senha</label>
                                <div class="campo-senha-container">
                                    <input type="password" id="confirmacao" name="confirmacao"
                                           placeholder="Confirme a nova senha"
                                           aria-describedby="erro-confirmacao">
                                    <button type="button" class="btn-olho-senha" aria-label="Mostrar ou ocultar senha">
                                        <img src="${ctx}/images/icone-ver.svg"
                                             data-icone-ver="${ctx}/images/icone-ver.svg"
                                             data-icone-naover="${ctx}/images/icone-naover.svg"
                                             alt="Ver senha">
                                    </button>
                                </div>
                                <p id="erro-confirmacao" role="alert" hidden
                                   style="color:#c62828;font-size:14px;margin-top:6px;"></p>
                            </div>
                        </div>

                        <div class="form-rodape">
                            <button type="submit" id="botao-salvar">Salvar</button>
                        </div>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>

<script>
    document.querySelectorAll("select").forEach(function(sel) {
        sel.addEventListener("click", function() {
            sel.classList.toggle("select-girar");
        });
        sel.addEventListener("blur", function() {
            sel.classList.remove("select-girar");
        });
        sel.addEventListener("change", function() {
            sel.classList.remove("select-girar");
        });
    });

    document.querySelectorAll(".btn-olho-senha").forEach(function(btn) {
        btn.addEventListener("click", function(evento) {
            evento.preventDefault();

            const container = btn.closest(".campo-senha-container");
            const input = container.querySelector("input");
            const img = btn.querySelector("img");
            const iconeVer = img.getAttribute("data-icone-ver");
            const iconeNaoVer = img.getAttribute("data-icone-naover");

            if (input.type === "password") {
                input.type = "text";
                img.src = iconeNaoVer;
                img.alt = "Ocultar senha";
            } else {
                input.type = "password";
                img.src = iconeVer;
                img.alt = "Ver senha";
            }
        });
    });

    const formulario = document.getElementById("form-editar-gestor");

    if (formulario !== null) {
        const senha = document.getElementById("senha");
        const confirmacao = document.getElementById("confirmacao");
        const erroSenha = document.getElementById("erro-senha");
        const erroConfirmacao = document.getElementById("erro-confirmacao");

        function mostrarErro(campo, elementoErro, mensagem) {
            elementoErro.textContent = mensagem;
            elementoErro.hidden = false;
            campo.style.borderColor = "#c62828";
            campo.setAttribute("aria-invalid", "true");
        }

        function limparErro(campo, elementoErro) {
            elementoErro.textContent = "";
            elementoErro.hidden = true;
            campo.style.borderColor = "";
            campo.removeAttribute("aria-invalid");
        }

        function validarSenhas() {
            limparErro(senha, erroSenha);
            limparErro(confirmacao, erroConfirmacao);

            const novaSenha = senha.value;
            const confirmarSenha = confirmacao.value;

            if (novaSenha === "" && confirmarSenha === "") {
                return true;
            }

            if (novaSenha === "") {
                mostrarErro(senha, erroSenha, "Digite a nova senha.");
                return false;
            }

            if (confirmarSenha === "") {
                mostrarErro(confirmacao, erroConfirmacao, "Confirme a nova senha.");
                return false;
            }

            if (novaSenha.length < 8) {
                mostrarErro(senha, erroSenha, "A senha deve ter pelo menos 8 caracteres.");
                return false;
            }

            if (!/[0-9]/.test(novaSenha)) {
                mostrarErro(senha, erroSenha, "A senha deve conter pelo menos um número.");
                return false;
            }

            if (!/[^a-zA-Z0-9]/.test(novaSenha)) {
                mostrarErro(senha, erroSenha, "A senha deve conter pelo menos um caractere especial.");
                return false;
            }

            if (novaSenha !== confirmarSenha) {
                mostrarErro(confirmacao, erroConfirmacao, "As senhas não coincidem.");
                return false;
            }

            return true;
        }

        formulario.addEventListener("submit", function(evento) {
            if (!validarSenhas()) {
                evento.preventDefault();
            }
        });

        senha.addEventListener("input", function() {
            limparErro(senha, erroSenha);
            limparErro(confirmacao, erroConfirmacao);
        });

        confirmacao.addEventListener("input", function() {
            limparErro(confirmacao, erroConfirmacao);
        });
    }
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>