
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

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

        <h1 class="nome-da-area">Nova assinatura</h1>

        <section class="edicao">
            <form action="${ctx}/assinatura/inserir" method="post">

                <c:if test="${not empty erroInterno}">
                    <p role="alert">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-coluna">
                    <div class="filtrador">
                        <label for="codOrganizacao">Organização</label>

                        <select id="codOrganizacao" name="codOrganizacao"
                                class="form-campo-total" required>
                            <option value="">Selecione</option>

                            <c:forEach var="item" items="${organizacoes}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filtrador">
                        <label for="codPlano">Plano</label>

                        <select id="codPlano" name="codPlano"
                                class="form-campo-total" required>
                            <option value="">Selecione</option>

                            <c:forEach var="item" items="${planos}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filtrador">
                        <label for="dataFim">Data de término</label>

                        <input type="date" id="dataFim" name="dataFim"
                               class="form-campo-total" placeholder="" required>
                    </div>

                    <div class="filtrador">
                        <label for="status">Status</label>

                        <select id="status" name="status" required>
                            <option value="Ativa">Ativa</option>
                            <option value="Inativa">Inativa</option>
                            <option value="Vencida">Vencida</option>
                        </select>
                    </div>
                </div>

                <button type="submit" id="botao-salvar">Cadastrar</button>
            </form>
        </section>
    </main>
</div>
</body>
</html>
