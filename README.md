# Zera | Aplicação Web

## Sobre o projeto

O **Zera** é um projeto interdisciplinar desenvolvido por estudantes do 1º ano do Ensino Médio Técnico em Desenvolvimento de Sistemas do Instituto J&F.

O projeto está relacionado ao **Objetivo de Desenvolvimento Sustentável (ODS) 12**, com foco na meta 12.4, que aborda a gestão ambientalmente adequada de resíduos.

Este repositório contém o desenvolvimento da **aplicação web do Zera**, responsável pelo gerenciamento de organizações, gestores, unidades, endereços, estoques, planos e assinaturas.

## Funcionalidades

A aplicação web possui funcionalidades de gerenciamento para gestores e administradores.

### Gestor

- Cadastro e autenticação.
- Visualização do painel inicial.
- Gerenciamento de estoques.
- Consulta e edição de informações da unidade.
- Consulta e edição de endereço.
- Gerenciamento de assinatura.
- Consulta e edição da organização.
- Gerenciamento do perfil e alteração de senha.

### Administrador

- Autenticação administrativa.
- Visualização do painel administrativo.
- Gerenciamento de organizações.
- Gerenciamento de gestores.
- Gerenciamento de unidades e endereços.
- Gerenciamento de estoques.
- Gerenciamento de planos e assinaturas.
- Configurações administrativas.

## Tecnologias utilizadas

| Tecnologia | Utilização |
|---|---|
| Java 21 | Desenvolvimento do backend |
| JSP e JSTL | Construção das páginas dinâmicas |
| Jakarta Servlets | Processamento das requisições HTTP |
| JDBC | Comunicação com o banco de dados |
| PostgreSQL | Persistência dos dados |
| Maven | Gerenciamento de dependências e build |
| Apache Tomcat 11 | Servidor de aplicação |
| HTML5 | Estrutura das interfaces |
| CSS3 | Estilização e responsividade |
| JavaScript | Interatividade e validações |
| Git e GitHub | Versionamento e colaboração |

## Estrutura do projeto

O backend está organizado no pacote base `br.com.zera`.

```text
src/
└── main/
    ├── java/
    │   └── br/com/zera/
    │       ├── config/
    │       ├── dao/
    │       ├── email/
    │       ├── exception/
    │       ├── model/
    │       ├── regex/
    │       ├── servlet/
    │       └── util/
    │
    └── webapp/
        ├── WEB-INF/
        │   ├── admin/
        │   ├── cadastro/
        │   ├── componentes/
        │   ├── gestor/
        │   ├── login/
        │   └── login-restrita/
        ├── css/
        ├── fonts/
        ├── images/
        ├── js/
        └── index.html
```

### Organização do backend

- **Model:** representa as entidades do sistema.
- **DAO:** realiza operações de persistência utilizando JDBC.
- **Servlet:** recebe e processa requisições HTTP.
- **Config:** gerencia configurações e variáveis de ambiente.
- **Util:** reúne funcionalidades auxiliares, incluindo a conexão com o banco.
- **Exception:** contém exceções específicas da aplicação.
- **Regex:** centraliza expressões regulares para validação.
- **Email:** contém recursos relacionados ao envio de e-mails.

## Configuração e execução

### Pré-requisitos

- JDK 21
- Maven
- Apache Tomcat 11
- Acesso a um banco PostgreSQL
- IntelliJ IDEA ou IDE compatível

### Variáveis de ambiente

A aplicação utiliza a classe `EnvConfig` para carregar configurações do arquivo `.env` local ou das variáveis do sistema operacional.

Exemplo das variáveis de conexão:

```env
DB_URL=
DB_USER=
DB_PASSWORD=
```

**Importante:** não compartilhe credenciais reais no repositório.

### Executando o projeto

1. Clone o repositório.
2. Abra o projeto na IDE.
3. Configure o JDK 21.
4. Atualize as dependências do Maven.
5. Configure as variáveis de ambiente.
6. Configure o Apache Tomcat 11.
7. Execute a aplicação e acesse o endereço local configurado.

## Banco de dados

A aplicação utiliza PostgreSQL para persistência dos dados.

A comunicação é realizada por meio de JDBC, utilizando classes DAO específicas para cada entidade.

A conexão é centralizada na classe `br.com.zera.util.Conexao`.

O banco de dados é mantido separadamente e não faz parte dos arquivos versionados neste repositório.

## Versionamento

O desenvolvimento é realizado colaborativamente utilizando Git e GitHub.

As alterações são desenvolvidas em branches e integradas por meio de Pull Requests, permitindo acompanhar o histórico de modificações e as contribuições da equipe.

## Equipe

Desenvolvido por estudantes do **1º ano do Ensino Médio Técnico em Desenvolvimento de Sistemas do Instituto J&F**.

**Integrantes:**

[Inserir nomes dos integrantes]

## Licença

Consulte o arquivo `LICENSE` presente neste repositório.