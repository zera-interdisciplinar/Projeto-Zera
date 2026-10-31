<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Home - Zera</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/crud.css">
</head>

<body>
<div class="tela-auth">
    <div class="painel-esquerda">
        <div class="painel-conteudo">
            <img src="${pageContext.request.contextPath}/images/logo_completa.svg" alt="Zera" class="logo">
            <nav>
                <a class="botao-ativo" href="${pageContext.request.contextPath}/admin/home">
                    <span>Home</span>
                </a>
                <a href="${pageContext.request.contextPath}/organizacao/lista">
                    <span>Organizações</span>
                </a>
                <a href="${pageContext.request.contextPath}/assinatura/lista">
                    <span>Assinaturas</span>
                </a>
                <a href="${pageContext.request.contextPath}/plano/lista">
                    <span>Planos</span>
                </a>
                <a href="${pageContext.request.contextPath}/unidade/lista">
                    <span>Unidades</span>
                </a>
                <a href="${pageContext.request.contextPath}/endereco/lista">
                    <span>Endereços</span>
                </a>
                <a href="${pageContext.request.contextPath}/gestor/lista">
                    <span>Gestores</span>
                </a>
                <a href="${pageContext.request.contextPath}/estoque/lista">
                    <span>Estoques</span>
                </a>
                <a href="${pageContext.request.contextPath}/admin/novo">
                    <span>Admin</span>
                </a>
                <hr class="barra-azul">
                <a href="${pageContext.request.contextPath}/configuracoes/admin">
                    <span>Configurações</span>
                </a>
                <a href="${pageContext.request.contextPath}/logout">
                    <span>Sair</span>
                </a>
            </nav>
        </div>
    </div>
    <div id="informacoes-gerais">
        <img src="${pageContext.request.contextPath}/images/z-transparente.png"
                alt=""
                class="z-marca-dagua">
        <h3 id="home">Home</h3>
        <div class="card">
            <a href="${pageContext.request.contextPath}/organizacao/lista">
                <div class="dado">
                    <p class="nome-do-dado">Organizações</p>
                    <br>
                    <p class="numero-do-dado">${totalOrganizacoes}</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/assinatura/lista">
                <div class="dado">
                    <p class="nome-do-dado">
                        Assinaturas<br>ativas
                    </p>
                    <br>
                    <p class="numero-do-dado">${assinaturasAtivas}</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/assinatura/lista">
                <div id="alerta" class="dado alerta">
                    <p class="nome-do-dado">
                        Vencendo em<br>30 dias
                    </p>
                    <br>
                    <p class="numero-do-dado">${assinaturasVencendo}</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/unidade/lista">
                <div class="dado">
                    <p class="nome-do-dado">Unidades</p>
                    <br>
                    <p class="numero-do-dado">${totalUnidades}</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/estoque/lista">
                <div class="dado">
                    <p class="nome-do-dado">Estoques</p>
                    <br>
                    <p class="numero-do-dado">${totalEstoques}</p>
                </div>
            </a>
        </div>
    </div>
</div>
</body>
</html>