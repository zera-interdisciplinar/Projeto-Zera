<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- ReadEnderecoServlet: GET /endereco/perfil, atributo endereco.
     UpdateEnderecoServlet: POST /endereco/alterar. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ativo" value="endereco" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereço - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud-usuario.css">
    <link rel="stylesheet" href="${ctx}/css/popup.css">
    <link rel="stylesheet" href="${ctx}/css/erro.css">
</head>
<body>
<%@ include file="/WEB-INF/componentes/carregando.jspf" %>
<%@ include file="menu.jspf" %>
<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">
    <c:choose>
        <c:when test="${param.editar eq '1' and not empty endereco}">
            <a href="${ctx}/endereco/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>
            <h1 class="nome-da-area">Editar endereço</h1>
            <section class="tabela">
                <label class="tabela-titulo">Endereço</label>
                <form id="form-endereco" action="${ctx}/endereco/alterar" method="post">
                    <div class="form-grid-duplo">
                        <div class="filtrador">
                            <label for="logradouro">Logradouro</label>
                            <input type="text" id="logradouro" name="logradouro" class="form-campo-total" maxlength="150" required value="${fn:escapeXml(endereco.logradouro)}">
                        </div>
                        <div class="filtrador">
                            <label for="estado">Estado</label>
                            <select id="estado" name="estado" class="form-campo-total" required>
                                <c:forTokens var="uf" items="AC,AL,AP,AM,BA,CE,DF,ES,GO,MA,MT,MS,MG,PA,PB,PR,PE,PI,RJ,RN,RS,RO,RR,SC,SP,SE,TO" delims=",">
                                    <option value="${uf}" ${uf eq endereco.estado ? 'selected="selected"' : ''}>${uf}</option>
                                </c:forTokens>
                            </select>
                        </div>
                        <div class="filtrador">
                            <label for="numero">Número</label>
                            <input type="text" id="numero" name="numero" class="form-campo-total" inputmode="numeric" pattern="[0-9]+" maxlength="10" required value="${fn:escapeXml(endereco.numero)}">
                        </div>
                        <div class="filtrador">
                            <label for="cidade">Cidade</label>
                            <input type="text" id="cidade" name="cidade" class="form-campo-total" maxlength="100" required value="${fn:escapeXml(endereco.cidade)}">
                        </div>
                        <div class="filtrador">
                            <label for="cep">CEP</label>
                            <input type="text" id="cep" name="cep" class="form-campo-total" inputmode="numeric" maxlength="9" pattern="[0-9]{5}-[0-9]{3}" placeholder="00000-000" title="Digite o CEP no formato 00000-000" required value="${fn:escapeXml(endereco.cep)}">
                        </div>
                        <div class="filtrador">
                            <label for="bairro">Bairro</label>
                            <input type="text" id="bairro" name="bairro" class="form-campo-total" maxlength="100" required value="${fn:escapeXml(endereco.bairro)}">
                        </div>
                    </div>
                    <div class="alinhar-direita">
                        <button type="submit" class="adicionar botao-submit">Salvar</button>
                    </div>
                </form>
            </section>
        </c:when>
        <c:otherwise>
            <h1 class="nome-da-area">Endereço</h1>
            <section class="tabela">
                <label class="tabela-titulo">Dados endereço</label>
                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                        <thead><tr><th>Logradouro</th><th>CEP</th><th>Cidade/UF</th><th class="col-acoes"></th></tr></thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty endereco}">
                                    <tr>
                                        <td><c:out value="${endereco.logradouro}"/>, <c:out value="${endereco.numero}"/> - <c:out value="${endereco.bairro}"/></td>
                                        <td><c:out value="${endereco.cep}"/></td>
                                        <td><c:out value="${endereco.cidade}"/>/<c:out value="${endereco.estado}"/></td>
                                        <td class="col-acoes"><div class="acoes"><a href="${ctx}/endereco/perfil?editar=1" title="Editar endereço"><img src="${ctx}/images/icone-editar.svg" alt="Editar"></a></div></td>
                                    </tr>
                                </c:when>
                                <c:otherwise><tr><td colspan="4">Esta unidade ainda não tem endereço cadastrado.</td></tr></c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</div>
<script>
(function () {
    const campoCep = document.getElementById('cep');
    const formulario = document.getElementById('form-endereco');
    if (!campoCep || !formulario) return;
    function formatarCep() {
        const numeros = campoCep.value.replace(/\D/g, '').slice(0, 8);
        campoCep.value = numeros.length > 5 ? numeros.slice(0, 5) + '-' + numeros.slice(5) : numeros;
    }
    campoCep.addEventListener('input', formatarCep);
    formatarCep();
    formulario.addEventListener('submit', function (event) {
        formatarCep();
        if (!/^[0-9]{5}-[0-9]{3}$/.test(campoCep.value)) {
            campoCep.setCustomValidity('Informe um CEP válido no formato 00000-000.');
            campoCep.reportValidity();
            event.preventDefault();
        }
    });
    campoCep.addEventListener('input', function () { campoCep.setCustomValidity(''); });
})();
</script>
<script src="${ctx}/js/popup.js" defer></script>
<script src="${ctx}/js/carregando.js" defer></script>
</body>
</html>
