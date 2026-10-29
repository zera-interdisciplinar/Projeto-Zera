<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    Recuperação de senha - passo 2/3: digitar o código recebido por e-mail.

    Recebe do VerifyCodigoServlet (GET /senha/codigo):
    - email: e-mail para onde o código foi enviado

    Envia para o VerifyCodigoServlet (POST /senha/codigo): codigo.
    Os 6 quadradinhos não têm name: o JavaScript junta os dígitos no campo
    escondido "codigo" antes de enviar.

    Se der erro, o servlet volta para esta página com:
    - erroInterno: mensagem do erro
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Código de verificação - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>

<main class="tela-auth">

    <%@ include file="/WEB-INF/cadastro/painel.jspf" %>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/senha/esqueci" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <%-- a classe "erro" no card deixa os quadradinhos vermelhos e mostra a mensagem --%>
            <div class="card card-central<c:if test="${not empty erroInterno}"> erro</c:if>">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Um código foi enviado para <c:out value="${email}"/></p>

                <p class="instrucao-codigo">Insira o código de verificação</p>

                <form action="${ctx}/senha/codigo" method="post" id="form-codigo" novalidate>
                    <div class="codigo-verificacao">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 1">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 2">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 3">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 4">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 5">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 6">
                    </div>

                    <%-- recebe os 6 dígitos juntos; é este que o servlet lê --%>
                    <input type="hidden" name="codigo" id="codigo">

                    <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Confirmar</button>
                    </div>

                    <a href="${ctx}/senha/esqueci" class="link-pequeno">Não recebeu? Pedir um novo código</a>
                </form>
            </div>
        </div>
    </section>
</main>

<script>
    const quadrados = document.querySelectorAll('.codigo-verificacao input');

    quadrados.forEach((quadrado, posicao) => {
        // aceita só número e pula para o próximo quadrado
        quadrado.addEventListener('input', () => {
            quadrado.value = quadrado.value.replace(/\D/g, '');
            if (quadrado.value !== '' && posicao < quadrados.length - 1) {
                quadrados[posicao + 1].focus();
            }
        });

        // Backspace num quadrado vazio volta para o anterior
        quadrado.addEventListener('keydown', (evento) => {
            if (evento.key === 'Backspace' && quadrado.value === '' && posicao > 0) {
                quadrados[posicao - 1].focus();
            }
        });

        // colar o código inteiro (ex: 048213) preenche todos os quadrados
        quadrado.addEventListener('paste', (evento) => {
            evento.preventDefault();
            const colado = evento.clipboardData.getData('text').replace(/\D/g, '');
            for (let i = 0; i < quadrados.length; i++) {
                if (i < colado.length) {
                    quadrados[i].value = colado[i];
                }
            }
        });
    });

    // antes de enviar, junta os 6 dígitos no campo escondido
    document.getElementById('form-codigo').addEventListener('submit', () => {
        let codigo = '';
        quadrados.forEach((quadrado) => {
            codigo += quadrado.value;
        });
        document.getElementById('codigo').value = codigo;
    });

    // já começa com o cursor no primeiro quadrado
    quadrados[0].focus();
</script>
</body>
</html>
