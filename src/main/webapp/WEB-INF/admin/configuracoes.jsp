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
                <a href="home.jsp">Home</a>
                <a href="organizacao/lista-organizacoes.jsp">Organizações</a>
                <a href="assinatura/lista-assinaturas.jsp">Assinaturas</a>
                <a href="plano/lista-planos.jsp">Planos</a>
                <a href="unidade/lista-unidades.jsp">Unidades</a>
                <a href="endereco/lista-enderecos.jsp">Endereços</a>
                <a href="gestor/lista-gestores.jsp">Gestores</a>
                <a href="estoque/lista-estoques.jsp">Estoques</a>
                <a href="novo-admin.jsp">Admin</a>
                <hr class="barra-azul">
                <a href="configuracoes.jsp" class="botao-ativo">Configurações</a>
                <a href="../login/login.jsp">Sair</a>
            </nav>
            </div>
        </div>
        <!--Área de informações-->
        <div class = informacoes>
            <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
            <h3 class = nome-da-area>Configurações</h3>
            <div id="perfil-admin">
                <h4>Meu perfil</h4>
                <form action="editar" method="post">
                    <h5>Nome</h5>
                    <h5>E-mail</h5>
                    <div>
                        <input type="text" name="nome" id="nome" placeholder="Admin Zera" required>
                    </div>
                    <div>
                        <input type="email" name="email" id="email" placeholder="adminzera@gmail.com" required>
                        <span class="mensagem-erro">E-mail inválido, ele precisa conter @ e o domínio</span>
                    </div>
                    <button type="submit">Salvar perfil</button>
                </form>
                
            </div>
            <div id="alterar-senha">
                <h4>Alterar senha</h4>
                <form action="editar" method="post">
                    <label class="senha-atual" for="senha-atual">Senha atual</label>
                    <input type="password" id="senha-atual" name="senha-atual" placeholder="Digite sua senha atual" required>
                    <span class="mensagem-erro">A senha está incorreta</span>
                    <label class="nova-senha" for="nova-senha">Nova senha</label>
                    <label class="confirmar-nova-senha" for="confirmar-nova-senha">Confirmar nova senha</label>
                    <input type="password" id="nova-senha" name="nova-senha" placeholder="Digite sua nova senha" required>
                    <span class="mensagem-erro">A senha precisa ter pelo menos 8 caractéres, um número e um caractére especial</span>
                    <input type="password" id="confirmar-nova-senha" name="confirmar-nova-senha" placeholder="Digite sua nova senha novamente" required>
                    <span class="mensagem-erro">A confirmação está diferente da senha informada</span>
                    <button type="submit">Alterar senha</button>
                </form>
            </div>
        </div>
</body>
</html>