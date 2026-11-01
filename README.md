# Projeto ZERA: landing e cadastro legado

A `main` continua publicando a aplicacao Java/JSP completa: landing em `/`, cadastro em `/cadastro/gestor` e os demais servlets. O painel novo fica em `https://gestao.zeratech.online`; somente os links **Fazer login** da landing apontam para ele. Os CTAs de cadastro usam o servlet real, inclusive no menu mobile.

## Validar

Requer JDK 21 e Maven 3.8.7 ou posterior:

```sh
mvn -B verify
docker build -t zera-landing:check .
```

O repositorio ainda nao possui testes automatizados Java. `verify` compila e empacota o WAR; nao comprova o fluxo de cadastro no banco. `maven-war-plugin` 3.5.1 e `maven-compiler-plugin` 3.14.1 estao fixados porque o WAR plugin 2.2 herdado do Maven falha no JDK 21.

O Dockerfile conserva Tomcat 11/JDK 21, expande o WAR durante o build e roda com UID/GID 1000. Os manifests montam somente temp/work/logs como gravaveis; codigo e configuracao permanecem somente leitura. Nao inclui `.env`, chaves nem credenciais geradas pelo GitHub Actions.

## Ambientes e publicacao

| Ambiente | Acionamento | Namespace | Destino |
|---|---|---|---|
| QA | push na branch `qa` | `qa` | Service interno `zera-landing`; hostname publico de landing QA ainda nao definido |
| Producao | GitHub Release publicada, estavel, tag cujo commit pertence a `main` | `production` | `https://www.zeratech.online/` |

`qa.zeratech.online` pertence ao painel web QA; esta landing nao ocupa esse hostname. Push de tag isolado e push em `main` nao publicam producao. `.github/workflows/deploy-landing.yml` aplica o Deployment por digest imutavel e aguarda o rollout. O primeiro deploy exige liberar o repositorio `zera-interdisciplinar/Projeto-Zera` na condicao do provider WIF e dar ao service account de deploy acesso ao Artifact Registry/GKE.

Projeto GCP: `zera-512810`; cluster `zera-cluster`, `southamerica-east1`; imagem `southamerica-east1-docker.pkg.dev/zera-512810/repo/zera-landing`.

O workflow nao altera DNS, certificado nem Ingress. O template `deploy/landing/kong-ingress.yaml` deve entrar em `infra-gtw-kong/manifests/prod/`, controlado pelo ArgoCD, apos o Service estar pronto. Usa o mesmo padrao sem host das APIs atuais para que `/` nao ganhe prioridade sobre os prefixos delas. Preserva o caminho e nao exige JWT/API key na landing. HTTPS e o Secret `gateway-tls` devem estar provisionados no namespace antes de ativar essa rota; o template nao gera certificado.

## Configuracao do legado

| Variavel | Secret Kubernetes existente / chave |
|---|---|
| `DB_URL` | `legacy-db` / `url` |
| `DB_USER` | `legacy-db` / `user` |
| `DB_PASSWORD` | `legacy-db` / `password` |
| `EMAIL_USER` | `zera-landing-mail` / `EMAIL_USER` (opcional no boot) |
| `EMAIL_PASSWORD` | `zera-landing-mail` / `EMAIL_PASSWORD` (opcional no boot) |

`legacy-db` e suas tres chaves foram confirmados nos namespaces QA e producao, sem revelar valores. Antes de aceitar cadastro real, confirmar conectividade/esquema legado; nao apontar para o schema administrativo novo por suposicao. O cadastro persiste no banco legado via servlet, sem criar registros nas APIs modernas automaticamente.

SMTP e usado **somente na recuperacao de senha do legado**, nao no cadastro. Sem o Secret opcional `zera-landing-mail`, a landing e os formularios sobem; recuperar senha gera o erro tratado pelo servlet. O codigo atual usa Gmail `smtp.gmail.com:587` com STARTTLS e senha de aplicativo. O Secret `resend` dos microsservicos nao e uma credencial SMTP equivalente e nao e reutilizado.

Criar esse Secret por um arquivo local protegido (nao versionado), nunca por valores literais em comandos ou manifests. Exemplo estrutural:

```sh
kubectl create secret generic zera-landing-mail -n production --from-env-file=/caminho/protegido/landing-mail.env
```

Nao ha migracao de banco nem envio de e-mail no processo de deploy.

## Conferencia posterior

Conferir `/`, `/cadastro/gestor`, CSS/imagens, o link para o painel novo e a preservacao das rotas `/qa/administrative`, `/qa/inventory`, `/qa/analytics` e suas variantes de producao. Um health check em `/index.html` comprova que o WAR esta servindo, mas nao a disponibilidade do banco ou SMTP. A aplicacao usa sessao em memoria; o manifest inicia com uma replica para nao repartir as etapas do cadastro entre pods.
