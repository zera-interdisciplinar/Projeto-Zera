
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Nova unidade - Zera</title>

    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>

<body class="pagina-nova-unidade">

<div class="tela-auth">

    <!-- MENU LATERAL -->

    <aside class="painel-esquerda">
        <div class="painel-conteudo">

            <img
                src="${ctx}/images/logo_completa.svg"
                alt="Zera"
                class="logo-zera"
            >

            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>

        </div>
    </aside>

    <!-- CONTEÚDO PRINCIPAL -->

    <main class="informacoes">

        <!-- MARCA D'ÁGUA -->

        <img
            src="${ctx}/images/z-transparente.png"
            alt=""
            class="z-marca-dagua"
        >

        <!-- VOLTAR -->

        <a href="${ctx}/unidade/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="">
            <span>Voltar</span>
        </a>

        <!-- TÍTULO -->

        <h1 class="nome-da-area">Nova unidade</h1>

        <!-- CARD DO FORMULÁRIO -->

        <section class="adicao">

            <form
                action="${ctx}/unidade/inserir"
                method="post"
                class="form-crud"
                id="formNovaUnidade"
            >

                <!-- MENSAGEM DE ERRO -->

                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro">
                        <c:out value="${erroInterno}"/>
                    </p>
                </c:if>

                <!-- ETAPA 1: DADOS DA UNIDADE -->

                <div class="etapa-formulario" id="etapa1">

                    <div class="form-grid">

                        <!-- ORGANIZAÇÃO -->

                        <div class="campo-grupo campo-cheio">

                            <label for="codOrganizacao">
                                Organização
                            </label>

                            <select
                                id="codOrganizacao"
                                name="codOrganizacao"
                                required
                            >
                                <option
                                    value=""
                                    disabled
                                    ${empty param.codOrganizacao ? 'selected' : ''}
                                >
                                    Selecione
                                </option>

                                <c:forEach var="item" items="${organizacoes}">

                                    <option
                                        value="${item.codigo}"
                                        ${param.codOrganizacao == item.codigo ? 'selected' : ''}
                                    >
                                        <c:out value="${item.nome}"/>
                                    </option>

                                </c:forEach>

                            </select>

                        </div>

                        <!-- EMAIL DA UNIDADE -->

                        <div class="campo-grupo campo-cheio">

                            <label for="email">
                                Email da unidade
                            </label>

                            <input
                                type="email"
                                id="email"
                                name="email"
                                placeholder="contato@empresa.com"
                                value="<c:out value='${param.email}'/>"
                                maxlength="254"
                                required
                            >

                        </div>

                        <!-- CNPJ DA UNIDADE -->

                        <div class="campo-grupo campo-cheio">

                            <label for="cnpj">
                                CNPJ da unidade
                            </label>

                            <input
                                type="text"
                                id="cnpj"
                                name="cnpj"
                                placeholder="00.000.000/0000-00"
                                value="<c:out value='${param.cnpj}'/>"
                                maxlength="18"
                                inputmode="numeric"
                                required
                            >

                        </div>

                    </div>

                </div>

                <!-- RODAPÉ DO FORMULÁRIO -->

                <div class="form-rodape">
                    <!-- Indicador: etapa atual e avanço para endereço -->
                    <div class="indicador-etapas" aria-label="Etapa 1 de 2">
                        <span class="numero-etapa ativo" aria-current="step">1</span>
                        <span class="separador-etapas" aria-hidden="true"></span>
                        <button type="submit" class="numero-etapa indicador-clicavel"
                                title="Salvar unidade e ir para o endereço"
                                aria-label="Avançar para a etapa 2, endereço">2</button>
                    </div>
                    <button type="submit" class="botao-etapa botao-proximo" id="botaoProximo">
                        Próximo
                    </button>
                </div>

            </form>

        </section>

    </main>

</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var formulario = document.getElementById('formNovaUnidade');
        var inputCnpj = document.getElementById('cnpj');

        function aplicarMascaraCnpj() {
            var valor = inputCnpj.value.replace(/\D/g, '').substring(0, 14);
            valor = valor.replace(/^(\d{2})(\d)/, '$1.$2');
            valor = valor.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
            valor = valor.replace(/\.(\d{3})(\d)/, '.$1/$2');
            valor = valor.replace(/(\d{4})(\d)/, '$1-$2');
            inputCnpj.value = valor;
            inputCnpj.setCustomValidity('');
        }

        inputCnpj.addEventListener('input', aplicarMascaraCnpj);
        aplicarMascaraCnpj();

        formulario.addEventListener('submit', function (evento) {
            var numerosCnpj = inputCnpj.value.replace(/\D/g, '');
            if (numerosCnpj.length !== 14) {
                evento.preventDefault();
                inputCnpj.setCustomValidity('Informe um CNPJ com 14 dígitos.');
                inputCnpj.reportValidity();
                return;
            }
            // O servlet /unidade/inserir precisa salvar a unidade e redirecionar
            // para /unidade/etapa2?codUnidade=CODIGO_CRIADO.
        });

        document.querySelectorAll('.pagina-nova-unidade select').forEach(function (select) {
            select.addEventListener('focus', function () { select.classList.add('select-girar'); });
            select.addEventListener('blur', function () { select.classList.remove('select-girar'); });
            select.addEventListener('change', function () { select.classList.remove('select-girar'); });
        });
    });
</script>
</body>
</html>
