<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adicionar endereço da unidade - Zera</title>
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

        <a href="${ctx}/unidade/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Adicionar endereço da unidade</h1>

        <section class="adicao">
            <form action="${ctx}/endereco/inserir" method="post" class="form-crud">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">
                    <div class="campo-grupo campo-cheio">
                        <label for="codUnidade">Unidade</label>
                        <select id="codUnidade" name="codUnidade" required>
                            <option value="" disabled selected>Selecione a unidade</option>
                            <c:forEach var="item" items="${unidades}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo">
                        <label for="logradouro">Logradouro</label>
                        <input type="text"
                               id="logradouro"
                               name="logradouro"
                               placeholder="Rua, Avenida, Alameda"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="numero">Número</label>
                        <input type="number"
                               id="numero"
                               name="numero"
                               placeholder="123"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="bairro">Bairro</label>
                        <input type="text"
                               id="bairro"
                               name="bairro"
                               placeholder="Bairro"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="cidade">Cidade</label>
                        <input type="text"
                               id="cidade"
                               name="cidade"
                               placeholder="Cidade"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="estado">Estado</label>
                        <input type="text"
                               id="estado"
                               name="estado"
                               placeholder="UF (Ex: SP)"
                               maxlength="2"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="cep">CEP</label>
                        <input type="text"
                               id="cep"
                               name="cep"
                               placeholder="00000-000"
                               maxlength="9"
                               required>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar">Cadastrar</button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>