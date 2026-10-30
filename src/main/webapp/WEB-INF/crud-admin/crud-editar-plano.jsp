<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.com.zera.model.Plano" %>

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
    Plano plano = (Plano) request.getAttribute("plano");

    if (plano == null) {
        response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Plano não encontrado."
        );
        return;
    }

    String contexto = request.getContextPath();
    String tempoAtual = plano.getTempo() == null
            ? ""
            : plano.getTempo().trim();

    boolean mensal = "mensal".equalsIgnoreCase(tempoAtual);
    boolean anual = "anual".equalsIgnoreCase(tempoAtual);
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Editar plano - Zera</title>

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

                <a
                        class="botao-ativo"
                        href="<%= contexto %>/plano/lista"
                >
                    <span>Planos</span>
                </a>

                <a href="<%= contexto %>/pages/crud-admin/crud-unidades-admin.jsp">
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

                <a href="<%= contexto %>/pages/crud-admin/crud-admins-admin.jp">
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

        <a href="<%= contexto %>/plano/lista" class="voltar">
            <img
                    src="<%= contexto %>/images/seta-voltar.svg"
                    alt="Ícone de seta de voltar"
            >
            Voltar
        </a>

        <h3 class="nome-da-area">Editar plano</h3>

        <div class="edicao">
            <form
                    id="form-editar-plano"
                    action="<%= contexto %>/plano/alterar"
                    method="post"
            >
                <input
                        type="hidden"
                        name="codigo"
                        value="<%= plano.getCodigo() %>"
                >

                <h5>
                    <label for="plano">Nome</label>
                </h5>

                <div id="nome">
                    <input
                            type="text"
                            name="nome"
                            id="plano"
                            placeholder="Profissional"
                            value="<%= escaparHtml(plano.getNome()) %>"
                            required
                    >
                </div>

                <h5>
                    <label for="valor">Valor</label>
                </h5>

                <h5>
                    <label for="periodo">Período</label>
                </h5>

                <div class="infos-de-mesma-linha">
                    <input
                            type="number"
                            name="valor"
                            id="valor"
                            placeholder="99,90"
                            value="<%= plano.getValor() == null ? "" : plano.getValor().toPlainString() %>"
                            min="0"
                            step="0.01"
                            inputmode="decimal"
                            required
                    >
                </div>

                <div class="infos-de-mesma-linha">
                    <select name="tempo" id="periodo" required>
                        <option value="">Selecione</option>

                        <option
                                value="mensal"
                                <%= mensal ? "selected" : "" %>
                        >
                            Mensal
                        </option>

                        <option
                                value="anual"
                                <%= anual ? "selected" : "" %>
                        >
                            Anual
                        </option>

                        <% if (!mensal && !anual && !tempoAtual.isEmpty()) { %>
                        <option
                                value="<%= escaparHtml(tempoAtual) %>"
                                selected
                        >
                            <%= escaparHtml(tempoAtual) %> meses
                        </option>
                        <% } %>
                    </select>
                </div>

                <button type="submit" id="botao-salvar">
                    Salvar
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    const formulario = document.getElementById("form-editar-plano");
    const campoNome = document.getElementById("plano");
    const botaoSalvar = document.getElementById("botao-salvar");

    let enviando = false;

    campoNome.addEventListener("input", function () {
        campoNome.setCustomValidity("");
    });

    formulario.addEventListener("submit", function (evento) {
        if (enviando) {
            evento.preventDefault();
            return;
        }

        campoNome.value = campoNome.value.trim();

        campoNome.setCustomValidity(
            campoNome.value ? "" : "Informe o nome do plano."
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
