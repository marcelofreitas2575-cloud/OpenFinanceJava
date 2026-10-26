# Open Finance Java (fake MVP)

MVP Open Finance em **Java 17**: microsserviços **Spring Boot 3**, **Kafka** (eventos + DLQ), MongoDB, Redis, API Gateway e Keycloak — fluxo **consentimento → sync de contas/extrato** com instituição mock.

## Sobre o projeto

Projeto de estudo/portfólio que simula o **compartilhamento de dados bancários com consentimento**: uma instituição iniciadora solicita acesso, sincroniza **contas e transações** de uma instituição detentora mock e expõe APIs para consulta.

**Stack:** Java 17, Spring Boot 3.4, Maven (monorepo), Spring Kafka (produtores/consumidores, retry e DLQ), Spring Cloud Gateway, MongoDB, Redis, Docker Compose, Keycloak (JWT / OAuth2 resource server).

**Arquitetura:** microsserviços **hexagonais** (ports & adapters), biblioteca compartilhada de eventos (`openfinance-events`), comunicação **assíncrona** entre consent → account → transaction e **HTTP** na API pública e na integração com o mock do banco.

**Objetivo:** demonstrar desenho e implementação de APIs REST seguras, integração entre serviços via Kafka e operação local com infra containerizada — sem pretender conformidade regulatória Open Finance Brasil.

Monorepo Maven com microsserviços em arquitetura hexagonal, MongoDB, Redis, Kafka e Docker.

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

## Subir tudo com Docker

```bash
docker compose up --build
```

Infra exposta: MongoDB `27017`, Redis `6379`, Kafka `9094` (host), Kafka UI `8099`, Keycloak `8180` (admin `admin` / `admin`).

**Desenho de quem fala com quem:** [docs/comunicacao.md](docs/comunicacao.md) · **Visual interativo:** abra [docs/comunicacao-visual.html](docs/comunicacao-visual.html) no navegador (abas: HTTP, Kafka, fluxo, DLQ)

## Fluxo feliz (via gateway)

### 1. Token (Keycloak)

`POST http://localhost:8080/realms/openfinance/protocol/openid-connect/token`  
Body `x-www-form-urlencoded`:

- `grant_type=password`
- `client_id=openfinance-app`
- `username=demo`
- `password=demo123`

Use o `access_token` nas chamadas abaixo. O token inclui o claim `customerId=customer-demo-001`.

### 2–4. Open Banking

2. **Consent:** `POST http://localhost:8080/open-banking/v1/consents` com Bearer token e body `{ "permissions": ["ACCOUNTS", "TRANSACTIONS"] }`
3. Aguarde alguns segundos (sync Kafka).
4. **Accounts:** `GET http://localhost:8080/open-banking/v1/accounts` com headers `Authorization: Bearer ...` e `X-Consent-Id: {consentId}`
5. **Transactions:** `GET http://localhost:8080/open-banking/v1/accounts/acc-checking-001/transactions` com os mesmos headers.

Usuário demo: `demo` / `demo123` (customer `customer-demo-001`).

## Build local (sem Docker nas apps)

```bash
mvn clean package -DskipTests
```

Suba apenas infra: `docker compose up mongodb redis kafka kafka-ui -d` e execute cada serviço com `spring-boot:run`.

## Documentação

Ver [docs/architecture.md](docs/architecture.md), [docs/comunicacao.md](docs/comunicacao.md) e coleção Postman em [docs/postman/openfinance-mvp.postman_collection.json](docs/postman/openfinance-mvp.postman_collection.json).
