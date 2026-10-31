<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar estoque - Zera</title>
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
                <a href="../gestor/lista-gestores.jsp"><span>Gestores</span></a>
                <a class = botao-ativo href="lista-estoques.jsp"><span>Estoques</span></a>
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
        <a href="lista-estoques.jsp" class="voltar">
            <img src="../../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
            Voltar
        </a>
        <h3 class = nome-da-area>Editar estoque</h3>
        <div class="edicao">
            <form action="${pageContext.request.contextPath}/editar" method="post">
                <h5>Unidade</h5>
                <div id ="nome"><select name="unidade" id="unidade">
                    <option value="jbs-alimentos-lins-sp">JBS Alimentos (Lins/SP)</option>
                </select>
                </div>
                <h5>Espaço na unidade</h5>
                <h5>Gestor responsável</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="espaco-na-unidade" id="-espaco-na-unidade" placeholder="Galpão 3, sala B">
                </div>
                <div class="infos-de-mesma-linha"><select name="gestor-responsavel" id="gestor-responsavel">
                    <option value="mariana-almeida">Mariana Almeida</option>
                </select>
                </div>
                <button type="submit">Salvar</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>