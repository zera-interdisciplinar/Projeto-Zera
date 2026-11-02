<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="gestor"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novo gestor - Zera</title>
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

        <h1 class="nome-da-area">Novo gestor</h1>

        <section class="adicao">
            <form action="${ctx}/gestor/inserir" method="post" class="form-crud">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <div class="form-grid">
                    <div class="campo-grupo">
                        <label for="nome">Nome completo</label>
                        <input type="text" id="nome" name="nome"
                               placeholder="Nome do gestor" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="email">E-mail</label>
                        <input type="email" id="email" name="email"
                               placeholder="gestor@empresa.com" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="telefone">Telefone</label>
                        <input type="tel" id="telefone" name="telefone"
                               placeholder="(00) 00000-0000" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="codUnidade">Unidade</label>
                        <select id="codUnidade" name="codUnidade" required>
                            <option value="" disabled selected>Selecione a unidade</option>
                            <c:forEach var="item" items="${unidades}">
                                <option value="${item.codigo}">
                                    <c:out value="${item.cnpj}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo">
                        <label for="senha">Senha</label>
                        <input type="password" id="senha" name="senha"
                               placeholder="••••••••" required>
                    </div>

                    <div class="campo-grupo">
                        <label for="confirmacao">Confirmar senha</label>
                        <input type="password" id="confirmacao" name="confirmacao"
                               placeholder="••••••••" required>
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" id="botao-salvar">Cadastrar</button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>