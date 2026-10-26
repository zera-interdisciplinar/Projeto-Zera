<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%--
    Configurações do gestor.

    Ainda não existe servlet para esta tela: é preciso criar um que responda
    GET /gestor/configuracoes e faça forward para esta JSP.

    Tema: não passa pelo servidor. A escolha fica salva no navegador (localStorage)
    e vira o atributo data-tema na tag <html>. Para o modo escuro aparecer,
    o crud.css precisa ter regras para html[data-tema="escuro"].

    Excluir conta: envia para o DeleteContaServlet (POST /gestor/excluirConta): senha.
    O servlet confere a senha, apaga a unidade e tudo dela, encerra a sessão
    e volta para a página inicial.
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="configuracoes"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configurações - Zera</title>
    <link rel="icon" type="image/png" href="${ctx}/images/favicon.png">
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/crud.css">
</head>
<body>

<%@ include file="menu.jspf" %>

<main class="informacoes">
    <img src="${ctx}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <h1 class="nome-da-area">Configurações</h1>

    <section class="tabela">
        <%-- o tema é salvo pelo JavaScript, então o formulário não é enviado ao servidor --%>
        <form id="form-tema" class="tabela-filtros form-configuracoes">
            <div class="filtrador busca">
                <label for="tema">Tema</label>
                <select id="tema" name="tema" class="form-campo-total">
                    <option value="claro">Modo Claro</option>
                    <option value="escuro">Modo Escuro</option>
                </select>
            </div>

            <button type="submit" class="adicionar botao-submit">
                Salvar
            </button>
        </form>
    </section>

    <section class="tabela tabela-segunda">
        <label class="tabela-titulo">Excluir conta</label>
        <p class="subtitulo-area">
            Apaga a sua unidade, os estoques, os gestores e o endereço dela.
            Se a organização ficar sem unidades, ela e as assinaturas também são apagadas.
            Esta ação não pode ser desfeita.
        </p>

        <form action="${ctx}/gestor/excluirConta" method="post"
              onsubmit="return confirm('Tem certeza? Todos os dados da sua unidade serão apagados.');">
            <div class="form-grid-duplo">
                <div class="filtrador">
                    <label for="senha">Confirme com sua senha</label>
                    <input type="password" id="senha" name="senha" class="form-campo-total"
                           placeholder="Digite sua senha">
                </div>
            </div>

            <div class="alinhar-direita">
                <button type="submit" class="adicionar botao-submit">
                    Excluir conta
                </button>
            </div>
        </form>
    </section>
</main>
</div>

<script>
    // tema salvo no navegador; o try/catch evita erro se o navegador bloquear o localStorage
    const seletorTema = document.getElementById('tema');

    try {
        const temaSalvo = localStorage.getItem('zera-tema');
        if (temaSalvo) {
            seletorTema.value = temaSalvo;
        }
    } catch (erro) {
        // sem localStorage: fica no tema claro
    }

    document.getElementById('form-tema').addEventListener('submit', (evento) => {
        evento.preventDefault();
        try {
            localStorage.setItem('zera-tema', seletorTema.value);
        } catch (erro) {
            // sem localStorage: só aplica nesta página
        }
        document.documentElement.dataset.tema = seletorTema.value;
    });
</script>
</body>
</html>
