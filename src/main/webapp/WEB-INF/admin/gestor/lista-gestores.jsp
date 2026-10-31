<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../../images/favicon.png">
    <link rel="stylesheet" href="../../../css/style.css">
    <link rel="stylesheet" href="../../../css/crud.css">

    <title>Gestores - Zera</title>
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
                <a href="../endereco/lista-enderecos.jsp"><span>Endereços  </span></a>
                <a class = botao-ativo href="lista-gestores.jsp"><span>Gestores</span></a>
                <a href="../estoque/lista-estoques.jsp"><span>Estoques</span></a>
                <a href="../novo-admin.jsp"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="../configuracoes.jsp"><span>Configurações</span></a>
                <a href="../login/login.html"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <h3 class = nome-da-area>Gestores</h3>
        <div class = tabela>
            <form action="lista-gestores.jsp" method="GET" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input type="text" id="buscar" name="buscar" placeholder="Nome ou e-mail">
                </div>
                <div class="filtrador">
                    <label for="ordem-de-nome">Ordem de nome</label>
                    <select name="ordem" id="ordem">
                        <option value="a-z">De A-Z</option>
                        <option value="z-a">De Z-A</option>
                    </select>
                </div>
                <a href="crud-novo-gestor-admin.jsp" class="adicionar"><img src="../../../images/adicionar.svg" alt="">Novo gestor</a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <colgroup>
                        <col style="width: 24%">
                        <col style="width: 22%">
                        <col style="width: 18%">
                        <col style="width: 24%">
                        <col style="width: 12%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Telefone</th>
                        <th>Unidade</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Mariana Almeida</td>
                        <td>tijbs@exemplo.com</td>
                        <td>(11) 99812-4410</td>
                        <td>JBS Alimentos (Lins/SP)</td>
                        <td class="acoes">
                            <a href="editar-gestor.jsp"><img src="../../../images/editar.svg" alt=""></a>
                            <a href="../../../pages/popups/popup-excluir.html"><img src="../../../images/excluir.svg" alt=""></a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

</body>
</html>
