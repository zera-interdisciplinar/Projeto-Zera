<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Endereço da unidade do gestor: dados e edição.

    Recebe do ReadEnderecoServlet (GET /endereco/perfil):
    - endereco: endereço da unidade do gestor (pode ser null se a unidade não tiver)

    O servlet sempre manda para esta página:
    - /endereco/perfil            tabela com os dados
    - /endereco/perfil?editar=1   formulário de edição

    Envia para o UpdateEnderecoServlet (POST /endereco/alterar):
    logradouro, numero, cep, estado, cidade, bairro.
    O CEP precisa ter hífen (00000-000), porque é o formato que o CEP_REGEX aceita.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="endereco"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereço - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <%-- só dá para editar se a unidade já tiver endereço --%>
    <c:if test="${not empty param.editar and not empty endereco}">
        <a href="${ctx}/endereco/perfil" class="voltar">
            <img src="${ctx}/images/seta-voltar.svg" alt="">
            <span>Voltar</span>
        </a>

        <h1 class="nome-da-area">Editar endereço</h1>

        <section class="tabela">
            <label class="tabela-titulo">Endereço</label>

            <form action="${ctx}/endereco/alterar" method="post">
                <div class="form-grid-duplo">
                    <div class="filtrador">
                        <label for="logradouro">Logradouro</label>
                        <input type="text" id="logradouro" name="logradouro" class="form-campo-total"
                               value="<c:out value='${endereco.logradouro}'/>">
                    </div>
                    <div class="filtrador">
                        <label for="estado">Estado</label>
                        <select id="estado" name="estado" class="form-campo-total">
                                <%-- forTokens percorre a lista de siglas separadas por vírgula --%>
                            <c:forTokens var="uf" items="AC,AL,AP,AM,BA,CE,DF,ES,GO,MA,MT,MS,MG,PA,PB,PR,PE,PI,RJ,RN,RS,RO,RR,SC,SP,SE,TO" delims=",">
                                <option value="${uf}"<c:if test="${uf == endereco.estado}"> selected</c:if>>${uf}</option>
                            </c:forTokens>
                        </select>
                    </div>

                    <div class="filtrador">
                        <label for="numero">Número</label>
                        <input type="text" id="numero" name="numero" class="form-campo-total" inputmode="numeric"
                               value="${endereco.numero}">
                    </div>
                    <div class="filtrador">
                        <label for="cidade">Cidade</label>
                        <input type="text" id="cidade" name="cidade" class="form-campo-total"
                               value="<c:out value='${endereco.cidade}'/>">
                    </div>

                    <div class="filtrador">
                        <label for="cep">CEP</label>
                        <input type="text" id="cep" name="cep" class="form-campo-total"
                               inputmode="numeric" maxlength="9" placeholder="00000-000"
                               value="<c:out value='${endereco.cep}'/>">
                    </div>
                    <div class="filtrador">
                        <label for="bairro">Bairro</label>
                        <input type="text" id="bairro" name="bairro" class="form-campo-total"
                               value="<c:out value='${endereco.bairro}'/>">
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

    <c:if test="${empty param.editar or empty endereco}">
        <h1 class="nome-da-area">Endereço</h1>

        <section class="tabela">
            <label class="tabela-titulo">Dados endereço</label>

            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <thead>
                    <tr>
                        <th>Logradouro</th>
                        <th>CEP</th>
                        <th>Cidade/UF</th>
                        <th class="col-acoes"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${not empty endereco}">
                        <tr>
                            <td><c:out value="${endereco.logradouro}"/>, ${endereco.numero} - <c:out value="${endereco.bairro}"/></td>
                            <td><c:out value="${endereco.cep}"/></td>
                            <td><c:out value="${endereco.cidade}"/>/<c:out value="${endereco.estado}"/></td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="${ctx}/endereco/perfil?editar=1" title="Editar">
                                        <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${empty endereco}">
                        <tr>
                            <td colspan="4">Esta unidade ainda não tem endereço cadastrado.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </c:if>
</main>
</div>

<script>
    // máscara do CEP: deixa só números e coloca o hífen depois do 5º dígito
    const campoCep = document.getElementById('cep');
    if (campoCep) {
        campoCep.addEventListener('input', () => {
            let numeros = campoCep.value.replace(/\D/g, '').slice(0, 8);
            if (numeros.length > 5) {
                numeros = numeros.slice(0, 5) + '-' + numeros.slice(5);
            }
            campoCep.value = numeros;
        });
    }
</script>

</body>
</html>
