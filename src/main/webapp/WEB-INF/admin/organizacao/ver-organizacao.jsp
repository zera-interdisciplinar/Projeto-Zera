<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../../images/favicon.png">
    <link rel="stylesheet" href="../../../css/style.css">
    <link rel="stylesheet" href="../../../css/crud.css">
    <title>JBS Alimentos</title>
</head>
<body>

    <div class = tela-auth>
        <!--Painel esquerdo-->
        <div class = painel-esquerda>
            <div class = painel-conteudo>
              <img src="../../../images/logo_completa.svg" alt="Zera" class="logo-zera">
              <nav>
                <a href="crud-home-admin.html"><span>Home</span></a>
                <a class = botao-ativo href="crud-organizacoes-admin.html"><span>Organizações</span></a>
                <a href="crud-assinaturas-admin.html"><span>Assinaturas</span></a>
                <a href="crud-planos-admin.html"><span>Planos</span></a>
                <a href="crud-unidades-admin.html"><span>Unidades</span></a>
                <a href="crud-enderecos-admin.html"><span>Endereços  </span></a>
                <a href="crud-gestores-admin.html"><span>Gestores</span></a>
                <a href="crud-estoques-admin.html"><span>Estoques</span></a>
                <a href="crud-novo-admin.html"><span>Admin</span></a>
                <hr class = barra-azul>
                <a href="crud-configuracoes-admin.html"><span>Configurações</span></a>
                <a href="../login/login.html"><span>Sair</span></a>
            </nav>
            </div>
        </div>
        <!--Área de informações-->
        <div class = informacoes>
            <img src="../../../images/z-transparente.png" alt="" class="z-marca-dagua">
            <a href="lista-organizacoes.jsp" class="voltar">
                <img src="../../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
                Voltar
            </a>
            <img src="../../../images/z-transparente.png" alt="" class="z-marca-dagua">
            <div class = visualizacao>
                <div class="tabela-rolagem">
                    <table class="itens-organizacao">
                    <colgroup>
                        <col style="width: 25%">
                        <col style="width: 25%">
                        <col style="width: 25%">
                        <col style="width: 25%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>CNPJ</th>
                            <th>E-mail</th>
                            <th>Cadastrada em</th>
                            <th>Última atualização</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>ti@jbs.exemplo.com</td>
                            <td>24/03/2026</td>
                            <td>05/10/2026</td>
                        </tr>
                    </tbody>
                </table>
                </div>
                <a class="editar" href="crud-editar-organizacao-admin.html"><img src="../../../images/editar.svg" alt=""></a>
                <a class="excluir" href=""><img src="../../../images/excluir.svg" alt=""></a>
                <hr>
                <h4>Assinatura</h4>
                <div class="tabela-rolagem">
                    <table class="itens-organizacao">
                    <colgroup>
                        <col style="width: 25%">
                        <col style="width: 25%">
                        <col style="width: 25%">
                        <col style="width: 25%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>Plano</th>
                            <th>Valor</th>
                            <th>Vigência</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Corporativo</td>
                            <td>R$ 1.799,00</td>
                            <td>01/11/2025 a 01/11/2026</td>
                            <td><div class="status">Ativa</div></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="alerta-vencimento">Vence em breve</td>
                        </tr>
                    </tbody>
                </table>
                </div>
                <a class="editar" href="crud-editar-assinatura-admin.html"><img src="../../../images/editar.svg" alt=""></a>
                <a class="cancelar" href=""><img src="../../../images/X.svg" alt="">Cancelar assinatura</a>
                <hr>
                <h4 id="quantidade-unidades">Unidades (2)</h4>
                <a href="crud-nova-unidade-admin.html" class="adicionar"><img src="../../../images/adicionar.svg" alt="">Nova unidade</a>
                <div class="unidades-rolagem">
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
                            <th>CNPJ</th>
                            <th>Cidade/UF</th>
                            <th>Gestores</th>
                            <th>Estoques</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
                            </td>
                        </tr>
                        <tr>
                            <td>11.222.333/0001-81</td>
                            <td>Lins/SP</td>
                            <td>2</td>
                            <td>2</td>
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
