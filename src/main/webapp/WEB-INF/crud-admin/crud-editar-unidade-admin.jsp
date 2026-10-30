<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.com.zera.model.Unidade" %>
<%@ page import="br.com.zera.model.Organizacao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>

<%!
    private String escaparHtml(String valor) {
        if (valor == null) {
            return "";
        }

        return valor
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
%>

<%
    Unidade unidade = (Unidade) request.getAttribute("unidade");

    if (unidade == null) {
        response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Unidade não encontrada."
        );
        return;
    }

    List<Organizacao> organizacoes =
            (List<Organizacao>) request.getAttribute("organizacoes");

    if (organizacoes == null) {
        organizacoes = Collections.emptyList();
    }

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Editar unidade - Zera</title>

    <link
            rel="icon"
            type="image/png"
            href="<%= contexto %>/images/favicon.png"
    >

    <link rel="stylesheet" href="<%= contexto %>/css/style.css">
    <link rel="stylesheet" href="<%= contexto %>/css/crud.css">
</head>

<body>
<div class="tela-auth">
    <div class="painel-esquerda">
        <div class="painel-conteudo">
            <img
                    src="<%= contexto %>/images/logo_completa.svg"
                    alt="Zera"
                    class="logo-zera"
            >

            <nav>
                <a href="<%= contexto %>/pages/crud-admin/crud-home-admin-feito.jsp">
                    <span>Home</span>
                </a>

                <a href="<%= contexto %>/organizacao/lista">
                    <span>Organizações</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-assinaturas-admin.jsp">
                    <span>Assinaturas</span>
                </a>

                <a href="<%= contexto %>/plano/lista">
                    <span>Planos</span>
                </a>

                <a
                        class="botao-ativo"
                        href="<%= contexto %>/unidade/lista"
                >
                    <span>Unidades</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-enderecos-admin.jsp">
                    <span>Endereços</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-gestores-admin.jsp">
                    <span>Gestores</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-estoques-admin.jsp">
                    <span>Estoques</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-admins-admin.jsp">
                    <span>Admin</span>
                </a>

                <hr class="barra-azul">

                <a href="<%= contexto %>/pages/login/login.jsp">
                    <span>Sair</span>
                </a>
            </nav>
        </div>
    </div>

    <div class="informacoes">
        <img
                src="<%= contexto %>/images/z-transparente.png"
                alt=""
                class="z-marca-dagua"
        >

        <a href="<%= contexto %>/unidade/lista" class="voltar">
            <img
                    src="<%= contexto %>/images/seta-voltar.svg"
                    alt="Ícone de seta de voltar"
            >
            Voltar
        </a>

        <h3 class="nome-da-area">Editar unidade</h3>

        <div class="edicao">
            <form
                    id="form-editar-unidade"
                    action="<%= contexto %>/unidade/alterar"
                    method="post"
            >
                <input
                        type="hidden"
                        name="codigo"
                        value="<%= unidade.getCodigo() %>"
                >

                <h5>
                    <label for="organizacao">Organização</label>
                </h5>

                <div id="nome">
                    <select
                            name="codOrganizacao"
                            id="organizacao"
                            required
                    >
                        <option value="">Selecione</option>

                        <% for (Organizacao organizacao : organizacoes) { %>
                        <option
                                value="<%= organizacao.getCodigo() %>"
                                <%= organizacao.getCodigo() == unidade.getCodOrganizacao() ? "selected" : "" %>
                        >
                            <%= escaparHtml(organizacao.getNome()) %>
                        </option>
                        <% } %>
                    </select>
                </div>

                <h5>
                    <label for="email-da-unidade">
                        Email da unidade
                    </label>
                </h5>

                <h5>
                    <label for="cnpj-da-unidade">
                        CNPJ da unidade
                    </label>
                </h5>

                <div class="infos-de-mesma-linha">
                    <input
                            type="email"
                            name="email"
                            id="email-da-unidade"
                            placeholder="email@unidade.com"
                            value="<%= escaparHtml(unidade.getEmail()) %>"
                            autocomplete="email"
                            required
                    >
                </div>

                <div class="infos-de-mesma-linha">
                    <input
                            type="text"
                            name="cnpj"
                            id="cnpj-da-unidade"
                            placeholder="11.222.333/0001-81"
                            value="<%= escaparHtml(unidade.getCnpj()) %>"
                            inputmode="numeric"
                            maxlength="18"
                            pattern="[0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}"
                            title="Informe o CNPJ no formato 11.222.333/0001-81."
                            required
                    >
                </div>

                <button type="submit" id="botao-salvar">
                    Salvar
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    const formulario = document.getElementById("form-editar-unidade");
    const campoEmail = document.getElementById("email-da-unidade");
    const campoCnpj = document.getElementById("cnpj-da-unidade");
    const botaoSalvar = document.getElementById("botao-salvar");

    let enviando = false;

    function formatarCnpj(valor) {
        return valor
            .replace(/\D/g, "")
            .slice(0, 14)
            .replace(/^(\d{2})(\d)/, "$1.$2")
            .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
            .replace(/\.(\d{3})(\d)/, ".$1/$2")
            .replace(/(\d{4})(\d)/, "$1-$2");
    }

    campoCnpj.value = formatarCnpj(campoCnpj.value);

    campoCnpj.addEventListener("input", function () {
        campoCnpj.value = formatarCnpj(campoCnpj.value);
    });

    campoEmail.addEventListener("blur", function () {
        campoEmail.value = campoEmail.value.trim();
    });

    formulario.addEventListener("submit", function (evento) {
        if (enviando) {
            evento.preventDefault();
            return;
        }

        campoEmail.value = campoEmail.value.trim();
        campoCnpj.value = formatarCnpj(campoCnpj.value);

        if (!formulario.reportValidity()) {
            evento.preventDefault();
            return;
        }

        enviando = true;
        botaoSalvar.disabled = true;
        botaoSalvar.textContent = "Salvando...";
    });

    window.addEventListener("pageshow", function () {
        enviando = false;
        botaoSalvar.disabled = false;
        botaoSalvar.textContent = "Salvar";
    });
</script>
</body>
</html>