<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>

<%-- Redirecionamento interno para ver-organizacao.jsp caso o parâmetro ver=true seja passado --%>
<c:if test="${param.ver == 'true'}">
    <jsp:forward page="ver-organizacao.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar organização - Zera</title>
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

        <a href="${ctx}/organizacao/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar organização</h1>

        <c:choose>
            <c:when test="${empty organizacao}">
                <p class="mensagem-erro-registro">Registro não encontrado. Volte para a listagem.</p>
            </c:when>

            <c:otherwise>
                <section class="edicao">
                    <form action="${ctx}/organizacao/alterar" method="post" class="form-crud">
                        <input type="hidden" name="codigo" value="${organizacao.codigo}">

                        <c:if test="${not empty erroInterno}">
                            <p role="alert" class="alerta-erro">
                                <c:out value="${erroInterno}"/>
                            </p>
                        </c:if>

                        <div class="form-grid">
                            <div class="campo-grupo campo-cheio">
                                <label for="nome">Nome</label>
                                <input type="text" id="nome" name="nome"
                                       value="<c:out value='${organizacao.nome}'/>"
                                       placeholder="Digite o nome da organização"
                                       required>
                            </div>

                            <div class="campo-grupo">
                                <label for="cnpj">CNPJ</label>
                                <input type="text" id="cnpj" name="cnpj"
                                       value="<c:out value='${organizacao.cnpj}'/>"
                                       placeholder="00.000.000/0000-00"
                                       maxlength="18"
                                       required>
                            </div>

                            <div class="campo-grupo">
                                <label for="email">Email</label>
                                <input type="email" id="email" name="email"
                                       value="<c:out value='${organizacao.email}'/>"
                                       placeholder="exemplo@organizacao.com"
                                       required>
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
</script>
    <script src="${ctx}/js/validacao-formularios.js" defer></script>
</body>
</html>