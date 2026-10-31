
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
    Verificação do código de recuperação do administrador.
    Recebe email e erroInterno do VerifyCodigoAdminServlet.
    Envia o código de seis dígitos para POST /admin/senha/codigo.
--%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verificar código - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/cadastro.css">
</head>
<body>

<main class="tela-auth">

    <%@ include file="../cadastro/painel.jspf" %>

    <section class="painel-direita">
        <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

        <div class="container-card">
            <a href="${ctx}/admin/senha/esqueci" class="voltar">
                <img src="${ctx}/images/seta-voltar.svg" alt="Voltar">
                <span>Voltar</span>
            </a>

            <div class="card card-central<c:if test='${not empty erroInterno}'> erro</c:if>">
                <h1>Redefinir senha</h1>
                <p class="subtitulo">Enviamos um código de verificação para <c:out value="${email}"/>.</p>
                <p class="instrucao-codigo">Insira o código de verificação</p>

                <form action="${ctx}/admin/senha/codigo" method="post" id="form-codigo" novalidate>

                    <div class="codigo-verificacao">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 1">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 2">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 3">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 4">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 5">
                        <input type="text" maxlength="1" inputmode="numeric" aria-label="Dígito 6">
                    </div>

                    <input type="hidden" name="codigo" id="codigo">
                    <span class="mensagem-erro"><c:out value="${erroInterno}"/></span>

                    <div class="linha-botao">
                        <button type="submit" class="botao-primario">Confirmar</button>
                    </div>

                    <a href="${ctx}/admin/senha/esqueci" class="link-pequeno">Não recebeu? Pedir um novo código</a>

                </form>
            </div>
        </div>
    </section>
</main>

<script>
    const campos = document.querySelectorAll('.codigo-verificacao input');

    campos.forEach((campo, indice) => {
        campo.addEventListener('input', () => {
            campo.value = campo.value.replace(/\D/g, '');
            if (campo.value && indice < campos.length - 1) campos[indice + 1].focus();
        });

        campo.addEventListener('keydown', (evento) => {
            if (evento.key === 'Backspace' && !campo.value && indice > 0) campos[indice - 1].focus();
        });

        campo.addEventListener('paste', (evento) => {
            evento.preventDefault();
            const codigo = evento.clipboardData.getData('text').replace(/\D/g, '').slice(0, campos.length);
            campos.forEach((input, i) => input.value = codigo[i] || '');
            campos[Math.min(codigo.length, campos.length - 1)].focus();
        });
    });

    document.getElementById('form-codigo').addEventListener('submit', (evento) => {
        const codigo = Array.from(campos).map(campo => campo.value).join('');
        document.getElementById('codigo').value = codigo;

        if (codigo.length !== 6) {
            evento.preventDefault();
            campos.forEach(campo => {
                if (!campo.value) campo.focus();
            });
        }
    });
</script>

</body>
</html>
