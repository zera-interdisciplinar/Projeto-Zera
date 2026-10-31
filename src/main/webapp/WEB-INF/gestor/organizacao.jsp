<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Organização: consulta e edição no mesmo JSP. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="organizacao"/>
<c:set var="editando" value="${param.editar == '1' and not empty organizacao}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Organização - Zera</title>
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
        <c:when test="${editando}">
            <a href="${ctx}/organizacao/perfil" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="">
                <span>Voltar</span>
            </a>
            <h1 class="nome-da-area">Editar organização</h1>
            <section class="tabela">
                <form id="form-organizacao" action="${ctx}/organizacao/alterar" method="post">
                    <div class="form-coluna form-coluna-junto">
                        <div class="filtrador">
                            <label for="nome">Nome</label>
                            <input type="text" id="nome" name="nome" class="form-campo-total" maxlength="150" required value="${fn:escapeXml(organizacao.nome)}">
                        </div>
                    </div>
                    <div class="form-grid-duplo">
                        <div class="filtrador">
                            <label for="cnpj">CNPJ</label>
                            <input type="text" id="cnpj" name="cnpj" class="form-campo-total" inputmode="numeric" maxlength="18" placeholder="00.000.000/0000-00" required value="${fn:escapeXml(organizacao.cnpj)}" aria-describedby="erro-cnpj">
                            <span class="mensagem-erro" id="erro-cnpj" style="display:none">CNPJ inválido. Verifique os 14 dígitos.</span>
                        </div>
                        <div class="filtrador">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-campo-total" maxlength="254" required value="${fn:escapeXml(organizacao.email)}" aria-describedby="erro-email">
                            <span class="mensagem-erro" id="erro-email" style="display:none">Informe um e-mail válido.</span>
                        </div>
                    </div>
                    <div class="alinhar-direita">
                        <button type="submit" class="adicionar botao-submit">Salvar</button>
                    </div>
                </form>
            </section>
        </c:when>
        <c:otherwise>
            <h1 class="nome-da-area">Organização</h1>
            <section class="tabela">
                <label class="tabela-titulo">Dados organização</label>
                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                        <thead>
                            <tr><th>Nome</th><th>CNPJ</th><th>E-mail</th><th class="col-acoes"></th></tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty organizacao}">
                                    <tr>
                                        <td><c:out value="${organizacao.nome}"/></td>
                                        <td><c:out value="${organizacao.cnpj}"/></td>
                                        <td><c:out value="${organizacao.email}"/></td>
                                        <td class="col-acoes">
                                            <div class="acoes">
                                                <a href="${ctx}/organizacao/perfil?editar=1" title="Editar" aria-label="Editar organização">
                                                    <img src="${ctx}/images/icone-editar.svg" alt="Editar">
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="4">Nenhuma organização encontrada.</td></tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</div>
<script src="${ctx}/js/popup.js" defer></script>
<script>
(() => {
    const form = document.getElementById('form-organizacao');
    if (!form) return;
    const cnpj = document.getElementById('cnpj');
    const email = document.getElementById('email');
    const erroCnpj = document.getElementById('erro-cnpj');
    const erroEmail = document.getElementById('erro-email');
    const mascara = valor => {
        const n = valor.replace(/\D/g, '').slice(0, 14);
        return n.replace(/^(\d{2})(\d)/, '$1.$2')
                .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
                .replace(/\.(\d{3})(\d)/, '.$1/$2')
                .replace(/(\d{4})(\d)/, '$1-$2');
    };
    const cnpjValido = valor => {
        const n = valor.replace(/\D/g, '');
        if (n.length !== 14 || /^(\d)\1{13}$/.test(n)) return false;
        const digito = tamanho => {
            let soma = 0;
            let peso = tamanho - 7;
            for (let i = 0; i < tamanho; i++) {
                soma += Number(n[i]) * peso--;
                if (peso < 2) peso = 9;
            }
            const resto = soma % 11;
            return resto < 2 ? 0 : 11 - resto;
        };
        return digito(12) === Number(n[12]) && digito(13) === Number(n[13]);
    };
    cnpj.value = mascara(cnpj.value);
    cnpj.addEventListener('input', () => {
        cnpj.value = mascara(cnpj.value);
        erroCnpj.style.display = 'none';
        cnpj.setCustomValidity('');
    });
    email.addEventListener('input', () => {
        erroEmail.style.display = 'none';
        email.setCustomValidity('');
    });
    form.addEventListener('submit', evento => {
        const validoCnpj = cnpjValido(cnpj.value);
        const validoEmail = email.validity.valid && email.value.trim() !== '';
        erroCnpj.style.display = validoCnpj ? 'none' : 'block';
        erroEmail.style.display = validoEmail ? 'none' : 'block';
        cnpj.setCustomValidity(validoCnpj ? '' : 'CNPJ inválido');
        email.setCustomValidity(validoEmail ? '' : 'E-mail inválido');
        if (!validoCnpj || !validoEmail) {
            evento.preventDefault();
            (validoCnpj ? email : cnpj).reportValidity();
        }
    });
})();
</script>
<script src="${ctx}/js/carregando.js" defer></script>
</body>
</html>
