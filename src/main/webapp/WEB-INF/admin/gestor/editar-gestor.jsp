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
                    <form action="${ctx}/gestor/alterar" method="post" class="form-crud">
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
                                           placeholder="Digite uma nova senha">
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
                                    <input type="password" id="confirmacao" name="confirmacao"
                                           placeholder="Confirme a nova senha">
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
                            <button type="submit" id="botao-salvar">Salvar</button>
                        </div>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </main>
</div>

<script>
    // Rotação da seta do select
    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });

    // Alternância do botão de mostrar/ocultar senha
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
</body>
</html>