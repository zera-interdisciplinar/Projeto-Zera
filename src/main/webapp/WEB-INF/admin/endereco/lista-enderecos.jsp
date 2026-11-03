
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="endereco"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereços - Zera</title>
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
        <h1 class="nome-da-area">Endereços</h1>

        <section class="tabela">
            <form class="tabela-filtros" onsubmit="event.preventDefault()">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input id="buscar" name="buscar" type="search" placeholder="Logradouro, bairro, cidade ou CEP">
                    <button type="button" class="btn-lupa-busca" onclick="filtrar()" aria-label="Buscar">
                        <img src="${ctx}/images/icone-lupa.svg" alt="Buscar">
                    </button>
                </div>
            </form>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Logradouro</th>
                        <th>Número</th>
                        <th>Bairro</th>
                        <th>Cidade/UF</th>
                        <th>CEP</th>
                        <th class="col-acoes-th"></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${not empty enderecos}">
                            <c:forEach var="item" items="${enderecos}">
                                <tr class="linha-endereco">
                                    <td><c:out value="${item.logradouro}"/></td>
                                    <td><c:out value="${item.numero}"/></td>
                                    <td><c:out value="${item.bairro}"/></td>
                                    <td><c:out value="${item.cidade}"/>/<c:out value="${item.estado}"/></td>
                                    <td><c:out value="${item.cep}"/></td>

                                    <td class="col-acoes">
                                        <div class="acoes">
                                            <a href="${ctx}/endereco/perfil?codigo=${item.codigo}"
                                               class="btn-icone"
                                               title="Editar endereço"
                                               aria-label="Editar endereço">
                                                <img src="${ctx}/images/icone-editar.svg" alt="">
                                            </a>

                                            <form method="post" action="${ctx}/endereco/excluir" class="form-excluir">
                                                <input type="hidden" name="codigo" value="${item.codigo}">
                                                <button type="button"
                                                        class="btn-icone zera-botao-excluir"
                                                        data-modal-exclusao
                                                        data-excluir-id="${item.codigo}"
                                                        data-excluir-tipo="endereco"
                                                        data-excluir-titulo="Deseja mesmo excluir?"
                                                        data-excluir-descricao="Essa ação excluirá o endereço selecionado e não poderá ser desfeita."
                                                        title="Excluir endereço"
                                                        aria-label="Excluir endereço">
                                                    <img src="${ctx}/images/icone-excluir.svg" alt="">
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="sem-dados">Nenhum endereço cadastrado.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>

                    <tr id="sem-resultados" hidden>
                        <td colspan="6" class="sem-dados">Nenhum endereço encontrado.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>

<script>
    // BUSCA DOS ENDEREÇOS
    const busca = document.getElementById("buscar");
    const linhas = document.querySelectorAll(".linha-endereco");
    const aviso = document.getElementById("sem-resultados");

    function filtrar() {
        const termo = busca.value.trim().toLowerCase();
        let visiveis = 0;

        linhas.forEach(function(linha) {
            const encontrado = linha.textContent.toLowerCase().includes(termo);
            linha.hidden = !encontrado;
            if (encontrado) visiveis++;
        });

        if (aviso) aviso.hidden = visiveis !== 0 || linhas.length === 0;
    }

    if (busca) busca.addEventListener("input", filtrar);
    filtrar();

    // CONFIRMAÇÃO DE EXCLUSÃO
    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        const detalhes = evento.detail;
        if (!detalhes || detalhes.tipo !== "endereco") return;

        const botao = detalhes.botao;
        if (!botao) return;

        const formulario = botao.closest(".form-excluir");
        if (!formulario) return;

        const campoCodigo = formulario.querySelector('input[name="codigo"]');
        if (!campoCodigo || campoCodigo.value !== detalhes.id) return;

        botao.disabled = true;
        formulario.submit();
    });
</script>
</body>
</html>
