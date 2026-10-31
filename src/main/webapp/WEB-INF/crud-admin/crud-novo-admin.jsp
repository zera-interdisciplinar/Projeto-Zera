<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../images/favicon.png">
    <link rel="stylesheet" href="../../css/style.css">
    <link rel="stylesheet" href="../../css/crud.css">
    <title>Configurações</title>
</head>
<body>
<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="../../images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="crud-home-admin-feito.jsp"><span>Home</span></a>
                <a href="crud-organizacoes-admin.jsp"><span>Organizações</span></a>
                <a href="crud-assinaturas-admin.jsp"><span>Assinaturas</span></a>
                <a href="crud-planos-admin.jsp"><span>Planos</span></a>
                <a href="crud-unidades-admin.jsp"><span>Unidades</span></a>
                <a href="crud-enderecos-admin.jsp"><span>Endereços  </span></a>
                <a href="crud-gestores-admin.jsp"><span>Gestores</span></a>
                <a href="crud-estoques-admin.jsp"><span>Estoques</span></a>
                <a class = botao-ativo href="crud-novo-admin.jsp"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="crud-configuracoes-admin.jsp"><span>Configurações</span></a>
                <a href="../login/login.jsp"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
            <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
            <h3 class = nome-da-area>Adicionar admin</h3>
            <div class="adicao">
                <form action="adicionar" method="post">
                    <h5>Nome</h5>
                    <h5>E-mail</h5>
                    <div class="infos-de-mesma-linha">
                        <input type="text" name="nome" placeholder="Admin Zera">
                    </div>
                    <div class="infos-de-mesma-linha">
                        <input type="text" name="email" id="email" placeholder="adminzera@gmail.com">
                        <span class="mensagem-erro">Email inválido, ele precisa conter @ e o domínio</span>
                    </div>
                    <h5>Senha</h5>
                    <h5>Confirmar senha</h5>
                    <div class="infos-de-mesma-linha">
                        <input type="password" name="senha" id="senha" placeholder="Digite sua senha">
                        <span class="mensagem-erro">A senha precisa ter pelo menos 8 caractéres, um número e um caractére especial</span>
                    </div>
                    <div class="infos-de-mesma-linha">
                        <input type="password" name="confirmar-senha" id="confirmar-senha" placeholder="Digite sua senha novamente">
                        <span class="mensagem-erro">A confirmação está diferente da senha informada</span>
                    </div>
                    <button type="submit">Adicionar</button>
                </form>
            </div>
        </div>
</body>
</html>