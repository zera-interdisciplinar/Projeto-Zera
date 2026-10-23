<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Unidade do gestor: dados e edição.

    Recebe do ReadUnidadeServlet (GET /unidade/perfil):
    - unidade: unidade do gestor logado
    - organizacao: organização dona da unidade

    O servlet sempre manda para esta página:
    - /unidade/perfil            tabela com os dados
    - /unidade/perfil?editar=1   formulário de edição

    Envia para o UpdateUnidadeServlet (POST /unidade/alterar): cnpj, email.
    A organização não muda (o gestor não pode trocar), por isso o campo é só leitura e não tem name.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unidade - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <c:if test="${not empty param.editar}">
        <a href="${ctx}/unidade/perfil" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar unidade</h1>

        <section class="tabela">
            <form action="${ctx}/unidade/alterar" method="post">
                <div class="form-coluna">
                    <div class="filtrador">
                        <label for="organizacao">Organização</label>
                        <input type="text" id="organizacao" class="form-campo-total" readonly
                               value="<c:out value='${organizacao.nome}'/>">
                    </div>

                    <div class="filtrador">
                        <label for="email">Email da unidade</label>
                        <input type="email" id="email" name="email" class="form-campo-total"
                               value="<c:out value='${unidade.email}'/>">
                    </div>

                    <div class="filtrador">
                        <label for="cnpj">CNPJ da unidade</label>
                        <input type="text" id="cnpj" name="cnpj" class="form-campo-total"
                               inputmode="numeric" maxlength="18" placeholder="00.000.000/0000-00"
                               value="<c:out value='${unidade.cnpj}'/>">
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
        <h1 class="nome-da-area">Unidade</h1>

        <section class="tabela">
            <label class="tabela-titulo">Dados unidade</label>

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
                    <tr>
                        <td><c:out value="${organizacao.nome}"/></td>
                        <td><c:out value="${unidade.cnpj}"/></td>
                        <td><c:out value="${unidade.email}"/></td>
                        <td class="col-acoes">
                            <div class="acoes">
                                <a href="${ctx}/unidade/perfil?editar=1" title="Editar">
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