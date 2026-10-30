<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova unidade - Zera</title>
    <link rel="icon" type="image/png" href="../../images/favicon.png">
    <link rel="stylesheet" href="../../css/style.css">
    <link rel="stylesheet" href="../../css/crud.css">
</head>
<body>
<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="../../images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="crud-home-admin-feito.html"><span>Home</span></a>
                <a href="crud-organizacoes-admin-feito.html"><span>Organizações</span></a>
                <a href="crud-assinaturas-admin.jsp"><span>Assinaturas</span></a>
                <a href="crud-planos-admin.jsp"><span>Planos</span></a>
                <a class = botao-ativo href="crud-unidades-admin.jsp"><span>Unidades</span></a>
                <a href="crud-enderecos-admin.html"><span>Endereços</span></a>
                <a href="crud-gestores-admin.html"><span>Gestores</span></a>
                <a href="crud-estoques-admin.html"><span>Estoques</span></a>
                <a href="crud-admins-admin.html"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="../login/login.html"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <a href="crud-unidades-admin.jsp" class="voltar">
            <img src="../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
            Voltar
        </a>
        <h3 class = nome-da-area>Nova unidade</h3>
        <div class="adicao">
            <form action="${pageContext.request.contextPath}/adicionar" method="post">
                <h5>Organização</h5>
                <div id="nome">
                    <input type="text" name="plano" id="plano" placeholder="JBS Alimentos">
                </div>
                <h5>Email da unidade</h5>
                <h5>CNPJ da unidade</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="email-da-unidade" id="email-da-unidade" placeholder="email@unidade.com">
                </div>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="cnpj-da-unidade" id="cnpj-da-unidade" placeholder="11.222.333/0001-81">
                </div>
                <a href="" id="tela-atual-primeira">1</a>
                <p id="separador">|</p>
                <a href="crud-nova-unidade-pt2-admin.jsp" id="outra-tela">2</a>
                <a href="crud-nova-unidade-pt2-admin.jsp" id="proxima-tela" onclick="this.closest('form').submit(); return false;">Próximo</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>