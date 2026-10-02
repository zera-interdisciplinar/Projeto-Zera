<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastro da Unidade - Zera</title>
  <link rel="icon" type="image/png" href="<c:url value='/images/favicon.png'/>">
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/cadastro.css'/>">
</head>
<body>

<div class="tela-auth">

  <!-- Painel Esquerdo -->
  <div class="painel-esquerda">
    <div class="painel-esquerda-conteudo">
      <img src="<c:url value='/images/logo_completa.svg'/>" alt="Zera" class="logo-zera">
      <p class="slogan">TRANSFORMANDO <span class="texto-laranja">DESCARTE</span><br>EM SOLUÇÃO</p>
    </div>

    <!-- Redes Sociais no rodapé do painel -->
    <div class="painel-contatos">
      <a href="https://www.instagram.com/zera.app_/" target="_blank" class="contato-item">
        <img src="<c:url value='/images/instagram.svg'/>" class="contato-item" alt="">
        <span>@zera.app</span>
      </a>

      <a href="https://wa.me/5511933045181?text=Ol%C3%A1,%20gostaria%20de%20saber%20mais%20sobre%20o%20projeto%20Zera!" target="_blank" class="contato-item">
        <img src="<c:url value='/images/whatsapp.svg'/>" class="contato-item" alt="">
        <span>+55 (11) 93304-5181</span>
      </a>

      <a href="mailto:zera.institutojef@gmail.com" class="contato-item">
        <img src="<c:url value='/images/email.svg'/>" class="contato-item" alt="">
        <span>zera.institutojef@gmail.com</span>
      </a>
    </div>
  </div>

  <!-- Painel Direito -->
  <div class="painel-direita">
    <img src="<c:url value='/images/z-transparente.png'/>" alt="" class="z-marca-dagua">

    <div class="container-card">
      <a href="<c:url value='/cadastro/organizacao'/>" class="voltar">
        <img src="<c:url value='/images/seta-voltar.svg'/>" alt="Ícone de seta de voltar">
        <span>Voltar</span>
      </a>

      <div class="card">
        <h1>Dados da unidade</h1>
        <p class="subtitulo">Preencha os dados para prosseguir</p>

        <c:if test="${not empty erros}">
          <ul class="erros">
            <c:forEach var="erro" items="${erros}">
              <li><c:out value="${erro}"/></li>
            </c:forEach>
          </ul>
        </c:if>

        <form action="<c:url value='/endereco/signIn'/>" method="post">
          <div class="campo">
            <label for="email">Email da unidade</label>
            <div class="campo-icone">
              <img src="<c:url value='/images/email.svg'/>" class="icone-input" alt="">
              <input type="email" id="email" name="email"  value="<c:out value='${param.email}'/>" placeholder="emailunidade@exemplo.com">
            </div>
          </div>

          <div class="campo">
            <label for="cnpjUnidade">CNPJ</label>
            <input type="text" id="cnpj" name="cnpj"  value="<c:out value='${param.cnpj}'/>" placeholder="00.000.000/0000-00">
          </div>

          <div class="campo">
            <label for="logradouro">Logradouro</label>
            <input type="text" id="logradouro" name="logradouro"  value="<c:out value='${param.logradouro}'/>" placeholder="Digite o endereço (rua, avenida...)">
          </div>

          <div class="linha-campos">
              <div class="campo campo-numero">
                  <label for="numero">Número</label>
                  <input type="text" id="numero" name="numero"  value="<c:out value='${param.numero}'/>" placeholder="123">
              </div>

              <div class="campo campo-cep">
                  <label for="cep">CEP</label>
                  <input type="text" id="cep" name="cep"  value="<c:out value='${param.cep}'/>" placeholder="00000000">
              </div>

              <div class="campo campo-estado">
                  <label for="estado">Estado</label>
                  <input type="text" id="estado" name="estado"  value="<c:out value='${param.estado}'/>" placeholder="SP">
              </div>
          </div>

          <div class="campo">
            <label for="cidade">Cidade</label>
            <input type="text" id="cidade" name="cidade"  value="<c:out value='${param.cidade}'/>" placeholder="Digite a cidade">
          </div>

          <div class="campo">
            <label for="bairro">Bairro</label>
            <input type="text" id="bairro" name="bairro"  value="<c:out value='${param.bairro}'/>" placeholder="Digite o bairro">
          </div>

          <div class="linha-botao">
            <button class="botao-primario" type="submit">Continuar</button>
          </div>
        </form>
      </div>
    </div>

  </div>

</div>
</body>
</html>