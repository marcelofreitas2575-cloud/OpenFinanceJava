# Open Finance Java (fake MVP)

Portfólio técnico: compartilhamento bancário com consentimento, em microsserviços Java.

## Visão rápida

- **Problema:** simular Open Finance — o cliente autoriza uma instituição (iniciadora) a acessar **contas e extrato** mantidos em outra instituição (detentora mock), com rastreio de consentimento e APIs para consulta.
- **Fluxo:** app → **POST consent** → Kafka → sync de **contas** no mock → Kafka → sync de **transações** → app consulta **GET accounts/transactions** com JWT + `X-Consent-Id`.
- **Tecnologias:** Java 17, Spring Boot 3.4, Spring Kafka, Spring Cloud Gateway, MongoDB, Redis, Docker Compose, Keycloak (JWT). Monorepo Maven, lib compartilhada `openfinance-events`.
- **Como rodar:** `docker compose up --build` → token Keycloak (`demo` / `demo123`) → Postman ou passos abaixo. Kafka UI: `http://localhost:8099`.
- **O que foi desenhado:** **HTTP síncrono** na borda (gateway, APIs Open Banking, chamadas ao mock-institution) e **Kafka assíncrono** entre serviços internos (consent → account → transaction), com retry/DLQ nos consumers — desacoplando consentimento da sincronização pesada.

### Código (Clean Code + SOLID)

- **Hexagonal (ports & adapters):** regras em `application/*UseCase` e `domain`; HTTP, Kafka, Mongo e Redis ficam nos adapters — o núcleo não conhece Spring nem detalhes de infra.
- **SOLID na prática:** *Single Responsibility* (um use case por operação, ex. `SyncAccountsUseCase`); *Dependency Inversion* (domínio depende de `*Port`, implementações em `adapter/out/*`); *Open/Closed* (trocar mock por API real = novo adapter, mesma port).
- **Clean Code:** nomes diretos (`ConsentCreatedKafkaConsumer`, `InstitutionPort`), controllers/consumers finos (só traduzem entrada e delegam), validações e fluxo de negócio fora dos adapters web.

> Projeto pessoal; não representa conformidade regulatória Open Finance Brasil.

## Serviços

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| api-gateway | 8080 | Entrada única |
| keycloak | 8180 | IdP (realm `openfinance`) — emissão de JWT |
| consent-service | 8082 | Consentimentos + Kafka |
| account-service | 8083 | Contas e saldos |
| transaction-service | 8084 | Extrato |
| mock-institution-service | 8090 | Banco fictício (interno) |
| kafka-ui | 8099 | UI do Kafka |

## Subir com Docker

```bash
docker compose up --build
```

Infra exposta: MongoDB `27017`, Redis `6379`, Kafka `9094` (host), Kafka UI `8099`, Keycloak `8180` (admin `admin` / `admin`).

**Diagramas:** [docs/comunicacao.md](docs/comunicacao.md) · [docs/comunicacao-visual.html](docs/comunicacao-visual.html) (HTTP, Kafka, sequência, DLQ)

## Fluxo feliz (via gateway)

### 1. Token (Keycloak)

`POST http://localhost:8080/realms/openfinance/protocol/openid-connect/token`  
Body `x-www-form-urlencoded`:

- `grant_type=password`
- `client_id=openfinance-app`
- `username=demo`
- `password=demo123`

Use o `access_token` nas chamadas abaixo (claim `customerId=customer-demo-001`).

### 2. Consentimento e consultas

1. **Consent:** `POST http://localhost:8080/open-banking/v1/consents` — Bearer + body `{ "permissions": ["ACCOUNTS", "TRANSACTIONS"] }`
2. Aguarde alguns segundos (consumo Kafka + sync).
3. **Accounts:** `GET http://localhost:8080/open-banking/v1/accounts` — Bearer + `X-Consent-Id: {consentId}`
4. **Transactions:** `GET http://localhost:8080/open-banking/v1/accounts/acc-checking-001/transactions` — mesmos headers.

Coleção pronta: [docs/postman/openfinance-mvp.postman_collection.json](docs/postman/openfinance-mvp.postman_collection.json)

## Build local (apps fora do Docker)

```bash
mvn clean package -DskipTests
```

Infra apenas: `docker compose up mongodb redis kafka kafka-ui keycloak -d` — depois `spring-boot:run` em cada serviço.

## Documentação

- [docs/architecture.md](docs/architecture.md) — visão geral e tópicos Kafka
- [docs/comunicacao.md](docs/comunicacao.md) — matriz HTTP vs eventos
