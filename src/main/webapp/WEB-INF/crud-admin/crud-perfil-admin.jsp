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
                <a href="crud-novo-admin.jsp"><span>Admin</span></a>
                <hr class = barra-azul>
                <a class = botao-ativo href="crud-configuracoes-admin.jsp"><span>Configurações</span></a>
                <a href="../login/log.jsp"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <h3 class = nome-da-area>Configurações</h3>
        <div id="perfil-admin">
            <h4>Meu perfil</h4>
            <h5>Nome</h5>
            <h5>E-mail</h5>
            <div>
                <p>Admin zera</p>
            </div>
            <div>
                <p>adminzera@gmail.com</p>
            </div>
            <button>Salvar perfil</button>
        </div>
        <form action="${pageContext.request.contextPath}/alterar-senha" method="post">
            <div id="alterar-senha">
                <h4>Alterar senha</h4>
                <label class="senha-atual" for="senha-atual">Senha atual</label>
                <input type="text" id="senha-atual" name="senha-atual" placeholder="Digite sua senha atual">
                <label for="nova-senha">Nova senha</label>
                <label for="confirmar-nova-senha">Confirmar nova senha</label>
                <input type="text" id="nova-senha" name="nova-senha" placeholder="Digite sua nova senha">
                <input type="text" id="confirmar-nova-senha" name="confirmar-nova-senha" placeholder="Digite sua nova senha novamente">
                <button>Alterar senha</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>