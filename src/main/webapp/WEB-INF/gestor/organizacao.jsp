<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Organização do gestor: dados e edição.

    Recebe do ReadOrganizacaoServlet (GET /organizacao/perfil):
    - organizacao: organização da unidade do gestor logado

    O servlet sempre manda para esta página:
    - /organizacao/perfil            tabela com os dados
    - /organizacao/perfil?editar=1   formulário de edição

    Envia para o UpdateOrganizacaoServlet (POST /organizacao/alterar): nome, cnpj, email.
    O código não vai no formulário: o servlet pega a organização pela unidade da sessão.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organização - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <c:if test="${not empty param.editar}">
        <a href="${ctx}/organizacao/perfil" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar organização</h1>

        <section class="tabela">
            <form action="${ctx}/organizacao/alterar" method="post">
                <div class="form-coluna form-coluna-junto">
                    <div class="filtrador">
                        <label for="nome">Nome</label>
                        <input type="text" id="nome" name="nome" class="form-campo-total"
                               value="<c:out value='${organizacao.nome}'/>">
                    </div>
                </div>

                <div class="form-grid-duplo">
                    <div class="filtrador">
                        <label for="cnpj">CNPJ</label>
                        <input type="text" id="cnpj" name="cnpj" class="form-campo-total"
                               inputmode="numeric" maxlength="18" placeholder="00.000.000/0000-00"
                               value="<c:out value='${organizacao.cnpj}'/>">
                    </div>
                    <div class="filtrador">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" class="form-campo-total"
                               value="<c:out value='${organizacao.email}'/>">
                    </div>
                </div>

                <div class="alinhar-direita">
                    <button type="submit" class="adicionar botao-submit">
                        Salvar
                    </button>
                </div>
            </form>
        </section>
    </c:if>

    <c:if test="${empty param.editar}">
        <h1 class="nome-da-area">Organização</h1>

        <section class="tabela">
            <label class="tabela-titulo">Dados organização</label>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>E-mail</th>
                        <th class="col-acoes"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><c:out value="${organizacao.nome}"/></td>
                        <td><c:out value="${organizacao.cnpj}"/></td>
                        <td><c:out value="${organizacao.email}"/></td>
                        <td class="col-acoes">
                            <div class="acoes">
                                <a href="${ctx}/organizacao/perfil?editar=1" title="Editar">
                                    <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                </a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </c:if>
</main>
</div>

</body>
</html>
