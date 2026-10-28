<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="../../images/favicon.png">
    <link rel="stylesheet" href="../../css/style.css">
    <link rel="stylesheet" href="../../css/crud-usuario.css">
    <title>Assinaturas</title>
</head>
<body>
<header class="header-crud">
    <div class="logo-topo">
        <img src="../../images/logo_completa.svg" alt="ZERA" class="logo">
    </div>
    <div class="avatar-topo">AD</div>
</header>
    <div class="tela-auth">
        <!--Painel esquerdo-->
        <div class="painel-esquerda">
            <div class="painel-conteudo">
                <img src="../../images/logo_completa.svg" alt="Zera" class="logo-zera">
                <nav>
                    <a href="crud-perfil-admin.html" class="perfil-usuario">
                        <div class="avatar">AD</div>
                        <span class="nome-usuario">Administrador</span>
                    </a>
                    <a href="crud-home-admin.html">Home</a>
                    <a href="crud-organizacoes-admin.html">Organizações</a>
                    <a href="crud-assinaturas-admin.jsp" class="botao-ativo">Assinaturas</a>
                    <a href="crud-planos-admin.html">Planos</a>
                    <a href="crud-unidades-admin.html">Unidades</a>
                    <a href="crud-enderecos-admin.html">Endereços</a>
                    <a href="crud-gestores-admin.html">Gestores</a>
                    <a href="crud-estoques-admin.html">Estoques</a>
                    <hr class=barra-azul>
                    <a href="#">Configurações</a>
                    <a href="../login/log.html">Sair</a>
                </nav>
    </div>
    </div>
        <!--Área de informações-->
        <main class="informacoes">
            <img src="../../images/z-transparente.png" alt="" class="z-marca-dagua">
            <h1 class="nome-da-area">Assinaturas</h1>
            <section class="tabela">
                <form action="crud-assinaturas-admin.jsp" method="GET" class="tabela-filtros">
                    <div class="filtrador busca">
                        <label for="busca">Buscar</label>
                        <div class="campo-busca">
                            <input type="search" id="busca" name="busca" placeholder="Organização ou plano">
                            <button type="submit" class="btn-lupa" aria-label="Buscar">
                                <img src="../../images/icone-lupa.svg" alt="">
                            </button>
                        </div>
                    </div>
                    <div class="filtrador">
                        <label for="situacao">Situação</label>
                        <select id="situacao" name="situacao">
                            <option value="todos">Todos</option>
                            <option value="ativa">Ativa</option>
                            <option value="cancelada">Cancelada</option>
                            <option value="expirada">Expirada</option>
                            <option value="vencendo-em-30-dias">Vencendo em 30 dias</option>
                        </select>
                    </div>                    <a href="crud-nova-assinatura-admin.html" class="adicionar">
                        <img src="../../images/icone-adicionar.svg" alt="">
                        Nova assinatura
                    </a>
                </form>
                <div class="tabela-rolagem">
                    <table class="itens-tabela">
                    <colgroup>
                        <col style="width: 22%">
                        <col style="width: 21%">
                        <col style="width: 15%">
                        <col style="width: 15%">
                        <col style="width: 15%">
                        <col style="width: 12%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>organização</th>
                            <th>Plano</th>
                            <th>início</th>
                            <th>Fim</th>
                            <th>Status</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>JBS Alimentos</td>
                            <td>Empresarial</td>
                            <td>10/10/2025</td>
                            <td>10/10/2026</td>
                            <td class="texto-verde">Ativa</td>
                            <td class="col-acoes">
                                <div class="acoes">
                                    <a href="crud-editar-assinatura-admin.html" title="Editar"><img src="../../images/icone-editar.svg" alt="Editar"></a>
                                    <a href="#" title="Excluir"><img src="../../images/icone-excluir.svg" alt="Excluir"></a>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </div>

            </section>
        </main>
    </div>


</body>
</html>