<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar endereço - Zera</title>
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
                <a href="crud-home-admin.jsp">Home</a>
                <a href="crud-organizacoes-admin.jsp">Organizações</a>
                <a href="crud-assinaturas-admin.jsp">Assinaturas</a>
                <a href="crud-planos-admin.jsp">Planos</a>
                <a href="crud-unidades-admin.jsp">Unidades</a>
                <a class = botao-ativo href="crud-enderecos-admin.jsp">Endereços</a>
                <a href="crud-gestores-admin.jsp">Gestores</a>
                <a href="crud-estoques-admin.jsp">Estoques</a>
                <a href="crud-novo-admin.jsp">Admin</a>
                <hr class = barra-azul>
                <a href="crud-configuracoes-admin.jsp">Configurações</a>
                <a href="../login/login.jsp">Sair</a>
            </nav>
        </div>
    </div>
    <!--Área de informações-->
    <div class = informacoes>
        <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
        <a href="crud-enderecos-admin.html" class="voltar">
            <img src="../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
            Voltar
        </a>
        <h3 class = nome-da-area>Editar endereço</h3>
        <div class="edicao">
            <form action="${pageContext.request.contextPath}/editar" method="post">
                <h5>Logradouro</h5>
                <h5>Estado</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="logradouro" id="logradouro" placeholder="Rua José Irineu">
                </div>
                <div class="infos-de-mesma-linha">
                    <select name="estado" id="estado">
                        <option value="ac">AC</option>
                        <option value="al">AL</option>
                        <option value="ap">AP</option>
                        <option value="am">AM</option>
                        <option value="ba">BA</option>
                        <option value="ce">CE</option>
                        <option value="es">ES</option>
                        <option value="go">GO</option>
                        <option value="ma">MA</option>
                        <option value="mt">MT</option>
                        <option value="ms">MS</option>
                        <option value="mg">MG</option>
                        <option value="pa">PA</option>
                        <option value="pb">PB</option>
                        <option value="pr">PR</option>
                        <option value="pe">PE</option>
                        <option value="pi">PI</option>
                        <option value="rj">RJ</option>
                        <option value="rn">RN</option>
                        <option value="rs">RS</option>
                        <option value="ro">RO</option>
                        <option value="rr">RR</option>
                        <option value="sc">SC</option>
                        <option value="sp">SP</option>
                        <option value="se">SE</option>
                        <option value="to">TO</option>
                    </select>
                </div>
                <h5>número</h5>
                <h5>Cidade</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="numero" id="numero" placeholder="123">
                </div>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="cidade" id="cidade" placeholder="São Paulo">
                </div>
                <h5>CEP</h5>
                <h5>Bairro</h5>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="cep" id="cep" placeholder="00000000">
                </div>
                <div class="infos-de-mesma-linha">
                    <input type="text" name="bairro" id="bairro" placeholder="Vila Jaguara">
                </div>
                <button type="submit">Salvar</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>