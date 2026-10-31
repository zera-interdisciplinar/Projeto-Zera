<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar gestor - Zera</title>
    <link rel="icon" type="image/png" href="../../../images/favicon.png">
    <link rel="stylesheet" href="../../../css/style.css">
    <link rel="stylesheet" href="../../../css/crud.css">
</head>
<body>
<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="../../../images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="../home.jsp"><span>Home</span></a>
                <a href="../organizacao/lista-organizacoes.jsp"><span>Organizações</span></a>
                <a href="../assinatura/lista-assinaturas.jsp"><span>Assinaturas</span></a>
                <a href="../plano/lista-planos.jsp"><span>Planos</span></a>
                <a href="../unidade/lista-unidades.jsp"><span>Unidades</span></a>
                <a href="../endereco/lista-enderecos.jsp"><span>Endereços</span></a>
                <a class = botao-ativo href="lista-gestores.jsp"><span>Gestores</span></a>
                <a href="../estoque/lista-estoques.jsp"><span>Estoques</span></a>
                <a href="../novo-admin.jsp"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="../configuracoes.jsp"><span>Configurações</span></a>
                <a href="../../login/login.jsp"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <a href="lista-gestores.jsp" class="voltar">
            <img src="../../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
            Voltar
        </a>
        <h3 class = nome-da-area>Editar gestor</h3>
        <div class="edicao">
            <form action="${pageContext.request.contextPath}/editar" method="post">
                <h5>Nome do gestor</h5>
                <div id="nome">
                    <input type="text" name="nome-do-gestor" id="nome-do-gestor" placeholder="Mariana Almeida">
                </div>
                <h5>Email do gestor</h5>
                <h5>senha</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="email" id="email" placeholder="gestor@organizacao.com">
                    <span class="mensagem-erro">Email inválido, ele precisa conter @ e o domínio</span>
                </div>
                <div class="infos-de-mesma-linha">
                    <input type="password" name="senha" id="senha" placeholder="senha123@">
                    <span class="mensagem-erro">A senha precisa ter pelo menos 8 caractéres, um número e um caractére especial</span>
                </div>
                <h5>Telefone do gestor</h5>
                <h5>Unidade</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="telefone" id="telefone" placeholder="+55 (11) 99812-4410">
                    <span class="mensagem-erro">Telefone inválido, ele precisa conter o DDD + nove números</span>
                </div>
                <div class="infos-de-mesma-linha">
                    <select name="unidade" id="unidade">
                        <option value="jbs-alimentos-lins-sp">JBS ALimentos (Lins/SP)
                        </option>
                    </select>
                </div>
                <button type="submit">Salvar</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>