<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova unidade - Zera</title>
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

        <h1 class="nome-da-area">Nova unidade</h1>

        <section class="adicao">
            <form action="${ctx}/unidade/inserir" method="post" class="form-crud">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">
                    <div class="campo-grupo campo-cheio">
                        <label for="codOrganizacao">Organização</label>
                        <select id="codOrganizacao" name="codOrganizacao" required>
                            <option value="" disabled selected>Selecione uma organização</option>
                            <c:forEach var="item" items="${organizacoes}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo">
                        <label for="cnpj">CNPJ</label>
                        <input type="text"
                               id="cnpj"
                               name="cnpj"
                               placeholder="00.000.000/0000-00"
                               maxlength="18"
                               required>
                    </div>

                    <div class="campo-grupo">
                        <label for="email">E-mail</label>
                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="unidade@empresa.com"
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
    const inputCnpj = document.getElementById('cnpj');
    inputCnpj?.addEventListener('input', (e) => {
        let v = e.target.value.replace(/\D/g, '');
        if (v.length > 14) v = v.substring(0, 14);
        v = v.replace(/^(\d{2})(\d)/, '$1.$2');
        v = v.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
        v = v.replace(/\.(\d{3})(\d)/, '.$1/$2');
        v = v.replace(/(\d{4})(\d)/, '$1-$2');
        e.target.value = v;
    });

    document.querySelectorAll('select').forEach(sel => {
        sel.addEventListener('click', () => sel.classList.toggle('select-girar'));
        sel.addEventListener('blur', () => sel.classList.remove('select-girar'));
        sel.addEventListener('change', () => sel.classList.remove('select-girar'));
    });
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>