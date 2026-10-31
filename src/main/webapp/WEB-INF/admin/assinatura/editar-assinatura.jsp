<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Editar assinatura - Zera</title>
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
                <a href="../home.jsp">Home</a>
                <a href="../organizacao/lista-organizacoes.jsp">Organizações</a>
                <a href="lista-assinaturas.jsp" class="botao-ativo">Assinaturas</a>
                <a href="../plano/lista-planos.jsp">Planos</a>
                <a href="../unidade/lista-unidades.jsp">Unidades</a>
                <a href="../endereco/lista-enderecos.jsp">Endereços</a>
                <a href="../gestor/lista-gestores.jsp">Gestores</a>
                <a href="../estoque/lista-estoques.jsp">Estoques</a>
                <hr class="barra-azul">
                <a href="../configuracoes.jsp">Configurações</a>
                <a href="../../login/login.jsp">Sair</a>
            </nav>
    </div>
  </div>
  <!--Área de informações-->
  <div class = informacoes>
    <img src="../../../images/z-transparente.png" alt="" class="z-marca-dagua">
    <a href="lista-assinaturas.jsp" class="voltar">
      <img src="../../../images/seta-voltar.svg" alt="Ícone de seta de voltar">
      Voltar
    </a>
    <h3 class = nome-da-area>Editar assinatura</h3>
    <div class="edicao">
      <form action="${pageContext.request.contextPath}/editar" method="post">
        <h5>Organização</h5>
        <div id="nome">
          <select name="organizacao" id="organizacao">
            <option value="jbs-alimentos">JBS Alimentos</option>
            <option value="seara">Seara</option>
          </select>
        </div>
        <h5>Plano</h5>
        <h5>Data de início</h5>
        <div class="infos-de-mesma-linha"><select name="plano" id="plano">
          <option value="empresarial">Empresarial</option>
          <option value="profissional">Profissional</option>
          <option value="free">Free</option>
        </select>
        </div>
        <div class="infos-de-mesma-linha"><input type="text" id="data-inicio" name="data-inicio" placeholder="10/10/2025">
        <span class="mensagem-erro">A data deve estar no formato DD/MM/AAAA</span></div>
        <h5>Status de assinatura</h5>
        <h5>Data de término</h5>
        <div class="infos-de-mesma-linha"><select name="status" id="status">
          <option value="ativa">Ativa</option>
          <option value="cancelada">Cancelada</option>
          <option value="expirada">Expirada</option>
          <option value="vencendo-em-30-dias">Vencendo em 30 dias</option>
        </select>
        </div>
        <div class="infos-de-mesma-linha"><input type="text" id="data-termino" name="data-termino" placeholder="01/03/2026">
        <span class="mensagem-erro">A data deve estar no formato DD/MM/AAAA e deve ser maior que a data de início</span></div>
        <button type="submit">Salvar</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>