<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../images/favicon.png">
    <link rel="stylesheet" href="../../css/style.css">
    <link rel="stylesheet" href="../../css/crud.css">
    <title>Endereços - Zera</title>
</head>
<body>

<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="../../images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="crud-home-admin.jsp"><span>Home</span></a>
                <a href="crud-organizacoes-admin-feito.jsp"><span>Organizações</span></a>
                <a href="crud-assinaturas-admin.jsp"><span>Assinaturas</span></a>
                <a href="crud-planos-admin.jsp"><span>Planos</span></a>
                <a href="crud-unidades-admin.jsp"><span>Unidades</span></a>
                <a class = botao-ativo href="crud-enderecos-admin.jsp"><span>Endereços  </span></a>
                <a href="crud-gestores-admin.jsp"><span>Gestores</span></a>
                <a href="crud-estoques-admin.jsp"><span>Estoques</span></a>
                <a href="crud-novo-admin-feito.jsp"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="crud-configuracoes-admin.jsp"><span>Configurações</span></a>
                <a href="../login/login.jsp"><span>Sair</span></a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <h3 class = nome-da-area>Endereços</h3>
        <div class = tabela>
            <form action="crud-enderecos-admin.jsp" method="GET" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input type="text" id="buscar" name="buscar" placeholder="Organização ou cidade">
                </div>
                <div class="filtrador">
                    <label for="organizacao">Organização</label>
                    <select name="Organização" id="organizacao">
                        <option value="todos">Todas</option>
                        <option value="jbs-alimentos">JBS alimentos</option>
                        <option value="seara">Seara</option>
                    </select>
                </div>
                <a href="" id="adicionar-invisivel"><img src="../../images/adicionar.svg" alt="">Nova organização</a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <colgroup>
                        <col style="width: 25%">
                        <col style="width: 25%">
                        <col style="width: 15%">
                        <col style="width:20%">
                        <col style="width: 15%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Organização</th>
                        <th>Logradouro</th>
                        <th>CEP</th>
                        <th>Cidade/UF</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>JBS Alimentos</td>
                        <td>Rua João Bertozi, 67</td>
                        <td>12345678</td>
                        <td>Lins/SP</td>
                        <td class="acoes">
                            <a href="editar-endereco-admin-feito.jsp"><img src="../../images/editar.svg" alt=""></a>
                            <a href="../../pages/popups/popup-excluir.html"><img src="../../images/excluir.svg" alt=""></a>
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