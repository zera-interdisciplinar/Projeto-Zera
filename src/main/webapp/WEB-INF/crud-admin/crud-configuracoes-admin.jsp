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
                <a href="crud-home-admin.jsp"><span>Home</span></a>
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
            <form action="${pageContext.request.contextPath}/editar-perfil" method="post">
                <h5>Nome</h5>
                <h5>E-mail</h5>
                <div>
                    <input type="text" name="nome" id="nome" placeholder="Admin Zera">
                </div>
                <div>
                    <input type="text" name="email" id="email" placeholder="adminzera@gmail.com">
                    <span class="mensagem-erro">E-mail inválido, ele precisa conter @ e o domínio</span>
                </div>
                <button type="submit">Salvar perfil</button>
            </form>

        </div>
        <div id="alterar-senha">
            <h4>Alterar senha</h4>
            <form action="${pageContext.request.contextPath}/editar-senha" method="post">
                <label class="senha-atual" for="senha-atual">Senha atual</label>
                <input type="text" id="senha-atual" name="senha-atual" placeholder="Digite sua senha atual">
                <span class="mensagem-erro">A senha está incorreta</span>
                <label class="nova-senha" for="nova-senha">Nova senha</label>
                <label class="confirmar-nova-senha" for="confirmar-nova-senha">Confirmar nova senha</label>
                <input type="text" id="nova-senha" name="nova-senha" placeholder="Digite sua nova senha">
                <span class="mensagem-erro">A senha precisa ter pelo menos 8 caractéres, um número e um caractére especial</span>
                <input type="text" id="confirmar-nova-senha" name="confirmar-nova-senha" placeholder="Digite sua nova senha novamente">
                <span class="mensagem-erro">A confirmação está diferente da senha informada</span>
                <button type="submit">Alterar senha</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>