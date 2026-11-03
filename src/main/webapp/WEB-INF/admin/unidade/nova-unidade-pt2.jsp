<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="unidade"/>
<%-- A etapa anterior deve encaminhar codUnidade por parâmetro ou atributo de request. --%>
<c:set var="codigoUnidadeAtual" value="${not empty param.codUnidade ? param.codUnidade : codUnidade}"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adicionar endereço da unidade - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body class="pagina-nova-unidade pagina-endereco-unidade">
<div class="tela-auth">
    <aside class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${ctx}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <%@ include file="/WEB-INF/componentes/menu-admin.jspf" %>
        </div>
    </aside>

    <main class="informacoes">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <a href="${ctx}/unidade/lista" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Adicionar endereço da unidade</h1>

        <section class="adicao">
            <h2 class="titulo-endereco">Endereço</h2>

            <form action="${ctx}/endereco/inserir" method="post" class="form-crud" id="formEnderecoUnidade">
                <c:if test="${not empty erroInterno}">
                    <p role="alert" class="alerta-erro"><c:out value="${erroInterno}"/></p>
                </c:if>

                <c:choose>
                    <c:when test="${not empty codigoUnidadeAtual}">
                        <input type="hidden" name="codUnidade" value="<c:out value='${codigoUnidadeAtual}'/>">
                    </c:when>
                    <c:otherwise>
                        <%-- A seleção só aparece se a unidade não veio da etapa anterior. --%>
                        <div class="campo-grupo seletor-unidade">
                            <label for="codUnidade">Unidade</label>
                            <select id="codUnidade" name="codUnidade" required>
                                <option value="" disabled selected>Selecione a unidade</option>
                                <c:forEach var="item" items="${unidades}">
                                    <option value="${item.codigo}"><c:out value="${item.nome}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="form-grid endereco-grid">
                    <div class="campo-grupo campo-logradouro">
                        <label for="logradouro">Logradouro</label>
                        <input type="text" id="logradouro" name="logradouro" placeholder="ex: Rua José Irineu" value="<c:out value='${param.logradouro}'/>" required>
                    </div>

                    <div class="campo-grupo campo-estado">
                        <label for="estado">Estado</label>
                        <select id="estado" name="estado" required>
                            <option value="" disabled ${empty param.estado ? 'selected' : ''}>Selecione</option>
                            <c:forEach var="uf" items="${['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO']}">
                                <option value="${uf}" ${param.estado == uf ? 'selected' : ''}>${uf}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="campo-grupo campo-numero">
                        <label for="numero">Número</label>
                        <input type="number" id="numero" name="numero" placeholder="123" min="1" value="<c:out value='${param.numero}'/>" required>
                    </div>

                    <div class="campo-grupo campo-cidade">
                        <label for="cidade">Cidade</label>
                        <input type="text" id="cidade" name="cidade" placeholder="São Paulo" value="<c:out value='${param.cidade}'/>" required>
                    </div>

                    <div class="campo-grupo campo-cep">
                        <label for="cep">CEP</label>
                        <input type="text" id="cep" name="cep" placeholder="00000000" maxlength="9" inputmode="numeric" value="<c:out value='${param.cep}'/>" required>
                    </div>

                    <div class="campo-grupo campo-bairro">
                        <label for="bairro">Bairro</label>
                        <input type="text" id="bairro" name="bairro" placeholder="Lins" value="<c:out value='${param.bairro}'/>" required>
                    </div>
                </div>

                <div class="form-rodape">
                    <div class="indicador-etapas" aria-label="Etapa 2 de 2">
                        <span class="numero-etapa">1</span>
                        <span class="separador-etapas"></span>
                        <span class="numero-etapa ativo">2</span>
                    </div>
                    <button type="submit" id="botao-salvar">Adicionar</button>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var inputCep = document.getElementById('cep');

        function aplicarMascaraCep() {
            var numeros = inputCep.value.replace(/\D/g, '').substring(0, 8);
            inputCep.value = numeros.length > 5
                ? numeros.substring(0, 5) + '-' + numeros.substring(5)
                : numeros;
            inputCep.setCustomValidity('');
        }

        inputCep.addEventListener('input', aplicarMascaraCep);
        aplicarMascaraCep();

        document.getElementById('formEnderecoUnidade').addEventListener('submit', function (evento) {
            var numerosCep = inputCep.value.replace(/\D/g, '');
            if (numerosCep.length !== 8) {
                evento.preventDefault();
                inputCep.setCustomValidity('Informe um CEP com 8 dígitos.');
                inputCep.reportValidity();
            }
        });

        document.querySelectorAll('.pagina-endereco-unidade select').forEach(function (select) {
            select.addEventListener('focus', function () { select.classList.add('select-girar'); });
            select.addEventListener('blur', function () { select.classList.remove('select-girar'); });
            select.addEventListener('change', function () { select.classList.remove('select-girar'); });
        });
    });
</script>
</body>
</html>
