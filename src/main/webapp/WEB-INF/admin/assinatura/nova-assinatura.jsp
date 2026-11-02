<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="assinatura"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova assinatura - Zera</title>
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

        <a href="${ctx}/assinatura/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Nova assinatura</h1>

        <section class="adicao">
            <form action="${ctx}/assinatura/inserir" method="post" class="form-crud">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">
                    <div class="campo-grupo">
                        <label for="codOrganizacao">Organização</label>
                        <select id="codOrganizacao" name="codOrganizacao" required>
                            <option value="" disabled selected>Selecione a organização</option>
                            <c:forEach var="item" items="${organizacoes}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo">
                        <label for="codPlano">Plano</label>
                        <select id="codPlano" name="codPlano" required>
                            <option value="" disabled selected>Selecione o plano</option>
                            <c:forEach var="item" items="${planos}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo">
                        <label for="dataFim">Data de término</label>
                        <input type="date" id="dataFim" name="dataFim" placeholder="dd/mm/aaaa" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="" disabled>Selecione o status</option>
                            <option value="Ativa" selected>Ativa</option>
                            <option value="Inativa">Inativa</option>
                            <option value="Vencida">Vencida</option>
                        </select>
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
</body>
</html>