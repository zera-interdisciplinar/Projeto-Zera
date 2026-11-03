
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${organizacao.nome}" default="Organização"/> - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">

    <style>
        /* VISUALIZAÇÃO DA ORGANIZAÇÃO */

        .visualizacao.organizacao-detalhes {
            display: flex;
            flex-direction: column;
            align-items: stretch;
            gap: 0;
            min-width: 0;
        }

        .organizacao-bloco {
            display: block;
            width: 100%;
            min-width: 0;
            flex: none;
            box-sizing: border-box;
        }

        .organizacao-detalhes .tabela-rolagem {
            display: block;
            width: 100%;
            min-width: 0;
            overflow-x: auto;
        }

        .organizacao-detalhes .itens-tabela {
            width: 100%;
            min-width: 650px;
            table-layout: fixed;
            border-collapse: collapse;
        }

        .organizacao-detalhes .itens-tabela th,
        .organizacao-detalhes .itens-tabela td {
            overflow-wrap: anywhere;
            vertical-align: middle;
            text-align: left;
        }

        .organizacao-detalhes .itens-tabela td {
            line-height: 1.5;
        }

        /* BOTÕES DE EDITAR E EXCLUIR */

        .organizacao-detalhes .acoes-secao {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            flex-wrap: wrap;
            gap: 18px;
            width: 100%;
            margin: 18px 0 0;
        }

        .organizacao-detalhes .acoes-secao form {
            display: inline-flex;
            margin: 0;
            padding: 0;
            background: transparent;
        }

        .organizacao-detalhes .acoes-secao .btn-icone {
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }

        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples,
        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples:hover,
        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples:active,
        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples:focus {
            appearance: none;
            -webkit-appearance: none;
            display: inline-flex !important;
            align-items: center;
            justify-content: center;
            width: 26px !important;
            height: 26px !important;
            min-width: 26px !important;
            min-height: 26px !important;
            max-width: 26px !important;
            padding: 0 !important;
            margin: 0 !important;
            background: transparent !important;
            background-color: transparent !important;
            background-image: none !important;
            border: 0 !important;
            border-radius: 0 !important;
            box-shadow: none !important;
            outline: none;
            color: inherit;
            cursor: pointer;
            transform: none !important;
        }

        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples:focus-visible {
            outline: 2px solid #2958D1 !important;
            outline-offset: 4px;
        }

        .organizacao-detalhes .acoes-secao button.btn-lixeira-simples img {
            display: block !important;
            width: 24px !important;
            height: 24px !important;
            min-width: 24px;
            object-fit: contain;
            background: transparent !important;
            box-shadow: none !important;
        }

        /* SEPARADORES E TÍTULOS */

        .organizacao-detalhes .divisor-organizacao {
            display: block;
            width: 100%;
            height: 1px;
            border: 0;
            background: rgba(21, 45, 107, 0.25);
            margin: 30px 0;
        }

        .organizacao-detalhes .cabecalho-secao {
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 18px;
            margin-bottom: 16px;
        }

        .organizacao-detalhes .subtitulo-secao {
            color: #152D6B;
            font-size: 25px;
            font-weight: 700;
            margin: 0;
        }

        /* BOTÃO NOVA UNIDADE */

        .organizacao-detalhes .cabecalho-secao .adicionar {
            margin: 0;
            flex-shrink: 0;
            min-width: 200px;
            padding: 0 22px;
            gap: 10px;
            justify-content: center;
        }

        /* STATUS DA ASSINATURA */

        .organizacao-detalhes .status {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 12px;
            font-weight: 700;
        }

        .organizacao-detalhes .status.ativa {
            color: #168045;
            background: rgba(16, 185, 129, 0.12);
        }

        .organizacao-detalhes .status.cancelada {
            color: #C62828;
            background: rgba(198, 40, 40, 0.10);
        }

        /* RESPONSIVIDADE */

        @media (max-width: 900px) {
            .organizacao-detalhes .itens-tabela {
                min-width: 620px;
            }

            .organizacao-detalhes .subtitulo-secao {
                font-size: 22px;
            }

            .organizacao-detalhes .cabecalho-secao .adicionar {
                min-width: 180px;
            }
        }

        @media (max-width: 600px) {
            .organizacao-detalhes .cabecalho-secao {
                align-items: flex-start;
            }

            .organizacao-detalhes .acoes-secao {
                justify-content: flex-start;
            }

            .organizacao-detalhes .divisor-organizacao {
                margin: 22px 0;
            }
        }
    </style>
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

        <a href="${ctx}/organizacao/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">
            <c:out value="${organizacao.nome}" default="Dados da organização"/>
        </h1>

        <c:choose>
            <c:when test="${empty organizacao}">
                <p class="mensagem-erro-registro">
                    Organização não encontrada. Volte para a listagem.
                </p>
            </c:when>

            <c:otherwise>
                <div class="visualizacao organizacao-detalhes">

                        <%-- DADOS GERAIS DA ORGANIZAÇÃO --%>
                    <section class="organizacao-bloco">
                        <div class="tabela-rolagem">
                            <table class="itens-organizacao itens-tabela">
                                <colgroup>
                                    <col style="width: 25%">
                                    <col style="width: 25%">
                                    <col style="width: 25%">
                                    <col style="width: 25%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>CNPJ</th>
                                    <th>E-mail</th>
                                    <th>Cadastrada em</th>
                                    <th>Última atualização</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="col-cnpj">
                                        <c:choose>
                                            <c:when test="${fn:length(organizacao.cnpj) == 14}">
                                                ${fn:substring(organizacao.cnpj,0,2)}.${fn:substring(organizacao.cnpj,2,5)}.${fn:substring(organizacao.cnpj,5,8)}/${fn:substring(organizacao.cnpj,8,12)}-${fn:substring(organizacao.cnpj,12,14)}
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${organizacao.cnpj}" default="Não informado"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty organizacao.email}">
                                                <c:out value="${organizacao.email}"/>
                                            </c:when>
                                            <c:otherwise>Não informado</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:out value="${organizacao.criadoEm != null ? fn:substring(organizacao.criadoEm, 0, 10) : 'Não informado'}"/>
                                    </td>
                                    <td>
                                        <c:out value="${organizacao.atualizadoEm != null ? fn:substring(organizacao.atualizadoEm, 0, 10) : 'Não informado'}"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                            <%-- AÇÕES DA ORGANIZAÇÃO --%>
                        <div class="acoes-secao">
                            <a class="btn-icone"
                               href="${ctx}/organizacao/perfil?codigo=${organizacao.codigo}"
                               title="Editar organização"
                               aria-label="Editar organização">
                                <img src="${ctx}/images/icone-editar.svg" alt="">
                            </a>

                            <form method="post"
                                  action="${ctx}/organizacao/excluir"
                                  class="form-excluir">
                                <input type="hidden"
                                       name="codigo"
                                       value="${organizacao.codigo}">

                                <button type="button"
                                        class="btn-lixeira-simples"
                                        data-modal-exclusao
                                        data-excluir-id="${organizacao.codigo}"
                                        data-excluir-tipo="organizacao"
                                        data-excluir-titulo="Deseja mesmo excluir?"
                                        data-excluir-descricao="Essa ação excluirá a organização, suas unidades, gestores, endereços, estoques e assinaturas. Não poderá ser desfeita."
                                        title="Excluir organização"
                                        aria-label="Excluir organização">
                                    <img src="${ctx}/images/icone-excluir.svg" alt="">
                                </button>
                            </form>
                        </div>
                    </section>

                    <hr class="divisor-organizacao">

                        <%-- SEÇÃO DE ASSINATURA --%>
                    <section class="organizacao-bloco">
                        <div class="cabecalho-secao">
                            <h2 class="subtitulo-secao">Assinatura</h2>
                        </div>

                        <div class="tabela-rolagem">
                            <table class="itens-organizacao itens-tabela">
                                <colgroup>
                                    <col style="width: 25%">
                                    <col style="width: 20%">
                                    <col style="width: 35%">
                                    <col style="width: 20%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>Plano</th>
                                    <th>Valor</th>
                                    <th>Vigência</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty assinatura}">
                                        <tr>
                                            <td>
                                                <c:out value="${plano.nome}" default="${assinatura.codPlano}"/>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty plano.valor}">
                                                        R$ <c:out value="${plano.valor}"/>
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:out value="${assinatura.dataInicio}"/>
                                                a
                                                <c:out value="${assinatura.dataFim}"/>
                                            </td>
                                            <td>
                                                <span class="status ${fn:toLowerCase(assinatura.status)}">
                                                    <c:out value="${assinatura.status}"/>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" class="sem-dados">
                                                Nenhuma assinatura vinculada.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                            <%-- AÇÕES DA ASSINATURA --%>
                        <c:if test="${not empty assinatura}">
                            <div class="acoes-secao">
                                <a class="btn-icone"
                                   href="${ctx}/assinatura/perfil?codigo=${assinatura.codigo}"
                                   title="Editar assinatura"
                                   aria-label="Editar assinatura">
                                    <img src="${ctx}/images/icone-editar.svg" alt="">
                                </a>

                                <form method="post"
                                      action="${ctx}/assinatura/excluir"
                                      class="form-excluir">
                                    <input type="hidden"
                                           name="codigo"
                                           value="${assinatura.codigo}">

                                    <button type="button"
                                            class="btn-lixeira-simples"
                                            data-modal-exclusao
                                            data-excluir-id="${assinatura.codigo}"
                                            data-excluir-tipo="assinatura"
                                            data-excluir-titulo="Deseja mesmo excluir?"
                                            data-excluir-descricao="Essa ação excluirá a assinatura selecionada e não poderá ser desfeita."
                                            title="Excluir assinatura"
                                            aria-label="Excluir assinatura">
                                        <img src="${ctx}/images/icone-excluir.svg" alt="">
                                    </button>
                                </form>
                            </div>
                        </c:if>
                    </section>

                    <hr class="divisor-organizacao">

                        <%-- SEÇÃO DE UNIDADES --%>
                    <section class="organizacao-bloco">
                        <div class="cabecalho-secao">
                            <h2 class="subtitulo-secao">
                                Unidades (<c:out value="${not empty unidades ? fn:length(unidades) : 0}"/>)
                            </h2>

                            <a href="${ctx}/unidade/perfil?codOrganizacao=${organizacao.codigo}"
                               class="adicionar">
                                <img src="${ctx}/images/icone-adicionar.svg" alt="">
                                <span>Nova unidade</span>
                            </a>
                        </div>

                        <div class="unidades-rolagem tabela-rolagem">
                            <table class="itens-tabela">
                                <colgroup>
                                    <col style="width: 25%">
                                    <col style="width: 25%">
                                    <col style="width: 20%">
                                    <col style="width: 18%">
                                    <col style="width: 12%">
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>CNPJ</th>
                                    <th>Cidade/UF</th>
                                    <th>Gestores</th>
                                    <th>Estoques</th>
                                    <th class="col-acoes-th"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty unidades}">
                                        <c:forEach var="u" items="${unidades}">
                                            <tr>
                                                <td class="col-cnpj">
                                                    <c:choose>
                                                        <c:when test="${fn:length(u.cnpj) == 14}">
                                                            ${fn:substring(u.cnpj,0,2)}.${fn:substring(u.cnpj,2,5)}.${fn:substring(u.cnpj,5,8)}/${fn:substring(u.cnpj,8,12)}-${fn:substring(u.cnpj,12,14)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${u.cnpj}"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>
                                                    <c:out value="${enderecosUnidades[u.codigo]}" default="-"/>
                                                </td>

                                                <td>
                                                    <c:out value="${totalGestoresUnidade[u.codigo]}" default="0"/>
                                                </td>

                                                <td>
                                                    <c:out value="${totalEstoquesUnidade[u.codigo]}" default="0"/>
                                                </td>

                                                <td class="col-acoes">
                                                    <div class="acoes">
                                                        <a href="${ctx}/unidade/perfil?codigo=${u.codigo}"
                                                           class="btn-icone"
                                                           title="Editar unidade"
                                                           aria-label="Editar unidade">
                                                            <img src="${ctx}/images/icone-editar.svg" alt="">
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="5" class="sem-dados">
                                                Nenhuma unidade cadastrada para esta organização.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</div>

<script>
    // CONFIRMAÇÃO DE EXCLUSÃO
    document.addEventListener("zera:confirmar-exclusao", function(evento) {
        const detalhes = evento.detail;

        if (!detalhes) return;

        if (detalhes.tipo !== "organizacao" && detalhes.tipo !== "assinatura") {
            return;
        }

        const botao = detalhes.botao;
        if (!botao) return;

        const formulario = botao.closest("form");
        if (!formulario) return;

        const campoCodigo = formulario.querySelector('input[name="codigo"]');

        if (!campoCodigo || campoCodigo.value !== detalhes.id) {
            return;
        }

        botao.disabled = true;
        formulario.submit();
    });
</script>
</body>
</html>
