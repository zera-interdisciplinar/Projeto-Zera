<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.com.zera.model.Organizacao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

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
    List<Organizacao> organizacoes =
            (List<Organizacao>) request.getAttribute("organizacoes");

    if (organizacoes == null) {
        organizacoes = Collections.emptyList();
    }

    String contexto = request.getContextPath();
    DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organizações - Zera</title>
    <link rel="icon" type="image/png" href="<%= contexto %>/images/favicon.png">
    <link rel="stylesheet" href="<%= contexto %>/css/style.css">
    <link rel="stylesheet" href="<%= contexto %>/css/crud-usuario.css">
</head>
<body>

<header class="header-crud">
<div class="tela-auth">
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${pageContext.request.contextPath}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="<%= contexto %>/admin/home">Home</a>
                <a href="<%= contexto %>/organizacao/lista" class="botao-ativo">Organizações</a>
                <a href="<%= contexto %>/assinatura/lista">Assinaturas</a>
                <a href="<%= contexto %>/plano/lista">Planos</a>
                <a href="<%= contexto %>/unidade/lista">Unidades</a>
                <a href="<%= contexto %>/endereco/lista">Endereços</a>
                <a href="<%= contexto %>/gestor/lista">Gestores</a>
                <a href="<%= contexto %>/estoque/lista">Estoques</a>

                <hr>

                <a href="#">Configurações</a>
                <a href="<%= contexto %>/logout">Sair</a>
            </nav>

            <a href="<%= contexto %>/admin/perfil" class="perfil-usuario">
                <div class="avatar">AD</div>
                <span class="nome-usuario">Administrador</span>
            </a>
        </div>
    </aside>

    <main class="informacoes">
        <img src="<%= contexto %>/images/z-transparente.png" alt="" class="z-marca-dagua">

        <h1 class="nome-da-area">Organizações</h1>

        <section class="tabela">
            <form id="form-filtros" action="<%= contexto %>/organizacao/lista" method="GET" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <div class="campo-busca">
                        <input type="search" id="buscar" name="buscar" placeholder="Nome, CNPJ ou email">
                        <button type="submit" class="btn-lupa" aria-label="Buscar">
                            <img src="<%= contexto %>/images/icone-lupa.svg" alt="">
                        </button>
                    </div>
                </div>

                <div class="filtrador">
                    <label for="cadastro">Cadastro</label>
                    <select name="cadastro" id="cadastro">
                        <option value="todos">Todos</option>
                        <option value="ultimos-30-dias">Últimos 30 dias</option>
                        <option value="ultimos-90-dias">Últimos 90 dias</option>
                    </select>
                </div>

                <a href="<%= contexto %>/organizacao/perfil" class="adicionar">
                    <img src="<%= contexto %>/images/icone-adicionar.svg" alt="">
                    Nova organização
                </a>
            </form>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>Email</th>
                        <th>Cadastrada em</th>
                        <th class="col-acoes"></th>
                    </tr>
                    </thead>
                    <tbody id="lista-organizacoes">
                    <% for (Organizacao organizacao : organizacoes) { %>
                    <tr class="linha-organizacao">
                        data-cadastro="<%= organizacao.getCriadoEm() == null ? "" : organizacao.getCriadoEm().toLocalDate() %>"
                        <td><%= escaparHtml(organizacao.getNome()) %></td>
                        <td><%= escaparHtml(organizacao.getCnpj()) %></td>
                        <td><%= escaparHtml(organizacao.getEmail()) %></td>
                        <td>
                            <%= organizacao.getCriadoEm() == null
                                    ? "Não informada"
                                    : organizacao.getCriadoEm().format(formatoData) %>
                        </td>
                        <td class="col-acoes">
                            <div class="acoes">
                                <a href="crud-ver-organizacao-admin.jsp" class="ver-organizacao" title="Visualizar">
                                    <img src="<%= contexto %>/images/icone-ver.svg" alt="Visualizar">
                                </a>
                                <a href="<%= contexto %>/organizacao/perfil?codigo=<%= organizacao.getCodigo() %>" title="Editar">
                                    <img src="<%= contexto %>/images/icone-editar.svg" alt="Editar">
                                </a>
                                <a href="../../pages/popups/popup-excluir.html" class="excluir-organizacao"
                                   data-codigo="<%= organizacao.getCodigo() %>"
                                   title="Excluir">
                                    <img src="<%= contexto %>/images/icone-excluir.svg" alt="Excluir">
                                </a>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                    <tr id="sem-resultados"> <%= organizacoes.isEmpty() ? "" : "hidden" %>
                        <td colspan="5">Nenhuma organização encontrada.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<dialog id="modal-visualizacao">
    <h3>Dados da organização</h3>
    <p><strong>Nome:</strong> <span id="detalhe-nome"></span></p>
    <p><strong>CNPJ:</strong> <span id="detalhe-cnpj"></span></p>
    <p><strong>Email:</strong> <span id="detalhe-email"></span></p>
    <p><strong>Cadastrada em:</strong> <span id="detalhe-data"></span></p>
    <button type="button" id="fechar-visualizacao">Fechar</button>
</dialog>

<dialog id="modal-exclusao">
    <h3>Excluir organização</h3>

    <p id="mensagem-exclusao"></p>

    <p>
        As unidades, os dados vinculados a elas e as assinaturas
        da organização também serão excluídos.
        Esta ação não pode ser desfeita.
    </p>

    <form id="form-exclusao"
          action="<%= contexto %>/organizacao/excluir"
          method="post">
        <input type="hidden" name="codigo" id="codigo-exclusao">
        <button type="button" id="cancelar-exclusao">Cancelar</button>
        <button type="submit" id="confirmar-exclusao">
            Excluir
        </button>
    </form>
</dialog>

<script>
    const campoBusca = document.getElementById("buscar");
    const campoCadastro = document.getElementById("cadastro");
    const formFiltros = document.getElementById("form-filtros");
    const linhas = document.querySelectorAll(".linha-organizacao");
    const semResultados = document.getElementById("sem-resultados");

    const modalVisualizacao = document.getElementById(
        "modal-visualizacao"
    );

    const modalExclusao = document.getElementById("modal-exclusao");
    const formExclusao = document.getElementById("form-exclusao");
    const codigoExclusao = document.getElementById("codigo-exclusao");
    const confirmarExclusao = document.getElementById(
        "confirmar-exclusao"
    );

    let excluindo = false;

    function normalizar(texto) {
        return texto
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .toLowerCase()
            .trim();
    }

    function filtrarOrganizacoes() {
        const busca = normalizar(campoBusca.value);
        const buscaNumerica = busca.replace(/\D/g, "");
        const somenteDocumento = /^[\d.\s/-]+$/.test(busca);

        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        let limite = null;

        if (campoCadastro.value !== "todos") {
            limite = new Date(hoje);

            const dias = campoCadastro.value === "ultimos-30-dias"
                ? 30
                : 90;

            limite.setDate(limite.getDate() - dias);
        }

        let quantidadeVisivel = 0;

        linhas.forEach(function (linha) {
            const nome = normalizar(linha.cells[0].textContent);
            const cnpj = normalizar(linha.cells[1].textContent);
            const email = normalizar(linha.cells[2].textContent);

            const correspondeBusca =
                nome.includes(busca) ||
                cnpj.includes(busca) ||
                email.includes(busca) ||
                (
                    somenteDocumento &&
                    buscaNumerica.length > 0 &&
                    cnpj.replace(/\D/g, "").includes(buscaNumerica)
                );

            let correspondeData = true;

            if (limite) {
                const dataTexto = linha.dataset.cadastro;

                if (!dataTexto) {
                    correspondeData = false;
                } else {
                    const partes = dataTexto.split("-").map(Number);
                    const data = new Date(
                        partes[0],
                        partes[1] - 1,
                        partes[2]
                    );

                    correspondeData = data >= limite && data <= hoje;
                }
            }

            linha.hidden = !(correspondeBusca && correspondeData);

            if (!linha.hidden) {
                quantidadeVisivel++;
            }
        });

        semResultados.hidden = quantidadeVisivel > 0;
    }

    campoBusca.addEventListener("input", filtrarOrganizacoes);
    campoCadastro.addEventListener("change", filtrarOrganizacoes);

    formFiltros.addEventListener("submit", function (evento) {
        evento.preventDefault();
        filtrarOrganizacoes();
    });

    document.querySelectorAll(".ver-organizacao").forEach(function (link) {
        link.addEventListener("click", function (evento) {
            evento.preventDefault();

            const linha = link.closest("tr");

            document.getElementById("detalhe-nome").textContent =
                linha.cells[0].textContent.trim();

            document.getElementById("detalhe-cnpj").textContent =
                linha.cells[1].textContent.trim();

            document.getElementById("detalhe-email").textContent =
                linha.cells[2].textContent.trim();

            document.getElementById("detalhe-data").textContent =
                linha.cells[3].textContent.trim();

            modalVisualizacao.showModal();
        });
    });

    document.getElementById("fechar-visualizacao")
        .addEventListener("click", function () {
            modalVisualizacao.close();
        });

    document.querySelectorAll(".excluir-organizacao")
        .forEach(function (link) {
            link.addEventListener("click", function (evento) {
                evento.preventDefault();

                if (excluindo) {
                    return;
                }

                const linha = link.closest("tr");
                const nome = linha.cells[0].textContent.trim();

                codigoExclusao.value = link.dataset.codigo;

                document.getElementById("mensagem-exclusao").textContent =
                    'Deseja excluir a organização "' + nome + '"?';

                modalExclusao.showModal();
            });
        });

    document.getElementById("cancelar-exclusao")
        .addEventListener("click", function () {
            modalExclusao.close();
        });

    formExclusao.addEventListener("submit", function (evento) {
        if (excluindo || !codigoExclusao.value) {
            evento.preventDefault();
            return;
        }

        excluindo = true;
        confirmarExclusao.disabled = true;
        confirmarExclusao.textContent = "Excluindo...";
    });

    window.addEventListener("pageshow", function () {
        excluindo = false;
        confirmarExclusao.disabled = false;
        confirmarExclusao.textContent = "Excluir";

        if (modalExclusao.open) {
            modalExclusao.close();
        }

        filtrarOrganizacoes();
    });
</script>
</body>
</html>
</Organizacao>
</Organizacao>