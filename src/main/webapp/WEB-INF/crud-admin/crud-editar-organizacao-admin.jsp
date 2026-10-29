<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.com.zera.model.Organizacao" %>

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
    Organizacao organizacao =
            (Organizacao) request.getAttribute("organizacao");

    if (organizacao == null) {
        response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Organização não encontrada."
        );
        return;
    }

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Editar organização - Zera</title>

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
                <a href="<%= contexto %>/pages/crud-admin/crud-home-admin.html">
                    <span>Home</span>
                </a>

                <a
                        class="botao-ativo"
                        href="<%= contexto %>/organizacao/lista"
                >
                    <span>Organizações</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-assinaturas-admin.jsp">
                    <span>Assinaturas</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-planos-admin.html">
                    <span>Planos</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-unidades-admin.html">
                    <span>Unidades</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-enderecos-admin.html">
                    <span>Endereços</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-gestores-admin.html">
                    <span>Gestores</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-estoques-admin.html">
                    <span>Estoques</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-admins-admin.html">
                    <span>Admin</span>
                </a>

                <hr class="barra-azul">

                <a href="<%= contexto %>/pages/login/login.html">
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

        <a href="<%= contexto %>/organizacao/lista" class="voltar">
            <img
                    src="<%= contexto %>/images/seta-voltar.svg"
                    alt="Ícone de seta de voltar"
            >
            Voltar
        </a>

        <h3 class="nome-da-area">Editar organização</h3>

        <div class="edicao">
            <form
                    id="form-editar-organizacao"
                    action="<%= contexto %>/organizacao/alterar"
                    method="post"
            >
                <input
                        type="hidden"
                        name="codigo"
                        value="<%= organizacao.getCodigo() %>"
                >

                <h5>
                    <label for="nome-da-organizacao">Nome</label>
                </h5>

                <div id="nome">
                    <input
                            type="text"
                            name="nome"
                            id="nome-da-organizacao"
                            placeholder="Nome da organização"
                            value="<%= escaparHtml(organizacao.getNome()) %>"
                            autocomplete="organization"
                            required
                    >
                </div>

                <h5>
                    <label for="cnpj">CNPJ</label>
                </h5>

                <h5>
                    <label for="email">Email</label>
                </h5>

                <div class="infos-de-mesma-linha">
                    <input
                            type="text"
                            name="cnpj"
                            id="cnpj"
                            placeholder="11.222.333/0001-81"
                            value="<%= escaparHtml(organizacao.getCnpj()) %>"
                            inputmode="numeric"
                            maxlength="18"
                            pattern="[0-9]{2}\.[0-9]{3}\.[0-9]{3}/[0-9]{4}-[0-9]{2}"
                            title="Informe o CNPJ no formato 11.222.333/0001-81."
                            required
                    >
                </div>

                <div class="infos-de-mesma-linha">
                    <input
                            type="email"
                            name="email"
                            id="email"
                            placeholder="email@organizacao.com"
                            value="<%= escaparHtml(organizacao.getEmail()) %>"
                            autocomplete="email"
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
    const formulario = document.getElementById(
        "form-editar-organizacao"
    );

    const campoNome = document.getElementById("nome-da-organizacao");
    const campoCnpj = document.getElementById("cnpj");
    const campoEmail = document.getElementById("email");
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

    campoNome.addEventListener("input", function () {
        campoNome.setCustomValidity("");
    });

    campoEmail.addEventListener("blur", function () {
        campoEmail.value = campoEmail.value.trim();
    });

    formulario.addEventListener("submit", function (evento) {
        if (enviando) {
            evento.preventDefault();
            return;
        }

        campoNome.value = campoNome.value.trim();
        campoEmail.value = campoEmail.value.trim();

        campoNome.setCustomValidity(
            campoNome.value
                ? ""
                : "Informe o nome da organização."
        );

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
