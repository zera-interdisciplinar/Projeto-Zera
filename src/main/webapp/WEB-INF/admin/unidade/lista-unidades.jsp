<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../../images/favicon.png">
    <link rel="stylesheet" href="../../../css/style.css">
    <link rel="stylesheet" href="../../../css/crud.css">
    <title>Unidades - Zera</title>
</head>
<body>
<div class = tela-auth>
    <!--Painel esquerdo-->
    <div class = painel-esquerda>
        <div class = painel-conteudo>
            <img src="../../../images/logo_completa.svg" alt="Zera" class="logo-zera">
            <nav>
                <a href="crud-home-admin-feito.jsp"><span>Home</span></a>
                <a href="../organizacao/lista-organizacoes.jsp"><span>Organizações</span></a>
                <a href="../assinatura/lista-assinaturas.jsp"><span>Assinaturas</span></a>
                <a href="../plano/lista-planos.jsp"><span>Planos</span></a>
                <a class = botao-ativo href="lista-unidades.jsp"><span>Unidades</span></a>
                <a href="../endereco/lista-enderecos.jsp"><span>Endereços  </span></a>
                <a href="../gestor/lista-gestores.jsp"><span>Gestores</span></a>
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
        <h3 class = nome-da-area>Unidades</h3>
        <div class = tabela>
            <form action="lista-unidades.jsp" method="GET" class="tabela-filtros">
                <div class="filtrador busca">
                    <label for="buscar">Buscar</label>
                    <input type="text" id="buscar" name="buscar" placeholder="CNPJ, e-mail ou cidade">
                </div>
                <div class="filtrador">
                    <label for="cadastro">Cadastro</label>
                    <select name="cadastro" id="cadastro">
                        <option value="todos">Todos</option>
                        <option value="">Últimos 30 dias</option>
                        <option value="ultimos-90-dias">Últimos 90 dias</option>
                    </select>
                </div>
                <a href="nova-unidade.jsp" class="adicionar"><img src="../../../images/adicionar.svg" alt="">Nova unidade</a>
            </form>
            <div class="tabela-rolagem">
                <table class="itens-tabela">
                    <colgroup>
                        <col style="width: 22%">
                        <col style="width: 20%">
                        <col style="width: 28%">
                        <col style="width: 18%">
                        <col style="width: 12%">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>Email</th>
                        <th>Cadastrada em</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>JBS Alimentos</td>
                        <td>11.222.333/0001-81</td>
                        <td>tijbs@exemplo.com</td>
                        <td>01/03/2026</td>
                        <td class="acoes">
                            <a href="editar-unidade-admin-feito.jsp"><img src="../../../images/editar.svg" alt=""></a>
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