<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Endereço da unidade - Zera</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cadastro.css">
</head>
<body>
<div class="tela-auth">

  <!-- Painel Esquerdo -->
  <div class="painel-esquerda">
    <div class="painel-esquerda-conteudo">
      <img href="${pageContext.request.contextPath}/images/logo_completa.svg" alt="Zera" class="logo-zera">
      <p class="slogan">TRANSFORMANDO <span class="texto-laranja">DESCARTE</span><br>EM SOLUÇÃO</p>
    </div>

    <!-- Redes Sociais no rodapé do painel -->
    <div class="painel-contatos">
      <a href="https://www.instagram.com/zera.app_/" target="_blank" class="contato-item">
        <img href="${pageContext.request.contextPath}/images/instagram.svg" class="contato-icone" alt="">
        <span>@zera.app</span>
      </a>

      <a href="https://wa.me/5511933045181?text=Ol%C3%A1,%20gostaria%20de%20saber%20mais%20sobre%20o%20projeto%20Zera!" target="_blank" class="contato-item">
        <img href="${pageContext.request.contextPath}/images/whatsapp.svg" class="contato-icone" alt="">
        <span>+55 (11) 93304-5181</span>
      </a>

      <a href="mailto:zera.institutojef@gmail.com" class="contato-item">
        <img href="${pageContext.request.contextPath}/images/email.svg" class="contato-icone" alt="">
        <span>zera.institutojef@gmail.com</span>
      </a>
    </div>
  </div>

  <!-- Painel Direito -->
  <div class="painel-direita">
    <img href="${pageContext.request.contextPath}/images/z-transparente.png" alt="" class="z-marca-dagua">

    <div class="container-card">
      <a href="${pageContext.request.contextPath}/index.html" class="voltar">
        <img href="${pageContext.request.contextPath}/images/seta-voltar.svg" alt="">
        <span>Voltar</span>
      </a>

      <div class="card">
        <h1>Endereço da unidade</h1>
        <p class="subtitulo">Preencha os dados para prosseguir</p>

        <form action="${pageContext.request.contextPath}src/main/webapp/pages/cadastro/cadastro-endereco.jsp" method="post">
        <c:if test="${not empty erro}">
          <p class="mensagem-erro"><c:out value="${erro}"/></p>
        </c:if>
          <div class="campo">
            <label for="logradouro">Logradouro</label>
            <input type="text" id="logradouro" name="logradouro" placeholder="Digite o endereço (rua, avenida...)" value="<c:out value='${param.logradouro}'/>">
          </div>

          <!-- Número, CEP e Estado lado a lado -->
          <div class="linha-campos">
            <div class="campo campo-numero">
              <label for="numero">Número</label>
              <input type="text" id="numero" name="numero" inputmode="numeric" placeholder="123" value="<c:out value='${param.numero}'/>">
            </div>

            <div class="campo campo-cep">
              <label for="cep">CEP</label>
              <input type="text" id="cep" name="cep" inputmode="numeric" maxlength="8" placeholder="00000000" value="<c:out value='${param.cep}'/>">
            </div>

            <div class="campo campo-estado">
              <label for="estado">Estado</label>
              <input type="text" id="estado" name="estado" maxlength="2" placeholder="SP" value="<c:out value='${param.estado}'/>">
              <span class = "mensagem-erro">Estado inexistente</span>
            </div>
          </div>

          <div class="campo">
            <label for="cidade">Cidade</label>
            <input type="text" id="cidade" name="cidade" placeholder="Digite a cidade" value="<c:out value='${param.cidade}'/>">
            <span class="mensagem-erro">Cidade inexistente</span>
          </div>

          <div class="campo">
            <label for="bairro">Bairro</label>
            <input type="text" id="bairro" name="bairro" placeholder="Digite o bairro" value="<c:out value='${param.bairro}'/>">
          </div>

          <div class="linha-botao">
            <button type="submit" class="botao-primario">Continuar</button>
          </div>
        </form>
      </div>
    </div>

  </div>

</div>
</body>
</html>