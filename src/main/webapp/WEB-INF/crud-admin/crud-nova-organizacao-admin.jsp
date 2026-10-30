<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova organização - Zera</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css">
</head>
<body>
<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="${pageContext.request.contextPath}/images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="${pageContext.request.contextPath}/admin/home"><span>Home</span></a>
                <a class = botao-ativo href="${pageContext.request.contextPath}/organizacao/lista"><span>Organizações</span></a>
                <a href="${pageContext.request.contextPath}/assinatura/lista"><span>Assinaturas</span></a>
                <a href="${pageContext.request.contextPath}/plano/lista"><span>Planos</span></a>
                <a href="${pageContext.request.contextPath}/unidade/lista"><span>Unidades</span></a>
                <a href="${pageContext.request.contextPath}/endereco/lista"><span>Endereços</span></a>
                <a href="${pageContext.request.contextPath}/gestor/lista"><span>Gestores</span></a>
                <a href="${pageContext.request.contextPath}/estoque/lista"><span>Estoques</span></a>
                <a href="${pageContext.request.contextPath}/admin/perfil"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="${pageContext.request.contextPath}/logout"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="${pageContext.request.contextPath}/images/z-transparente.png" alt="" class="z-marca-dagua">
        <a href="${pageContext.request.contextPath}/organizacao/lista" class="voltar">
            <img src="${pageContext.request.contextPath}/images/seta-voltar.svg" alt="Ícone de seta de voltar">
            Voltar
        </a>
        <h3 class = nome-da-area>Nova organização</h3>
        <div class="adicao">
            <form action="${pageContext.request.contextPath}/organizacao/adicionar" method="post">
                <h5>Nome</h5>
                <div id="nome">
                    <input type="text" name="nome-da-organizacao" id="nome-da-organizacao" placeholder="Nome da organização">
                </div>
                <h5>CNPJ</h5>
                <h5>Email</h5>
                <div class="infos-de-mesma-linha"><input type="text" name="cnpj" id="cnpj" placeholder="11.222.333/0001-81"></div>
                <div class="infos-de-mesma-linha"><input type="text" name="email" id="email" placeholder="email@organizacao.com"></div>
                <button type="submit">adicionar</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>