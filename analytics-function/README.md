# 📊 Analytics Function

Módulo da **Feedback Platform** responsável por consumir eventos de feedback e disponibilizar relatórios consolidados para consulta administrativa.

O `analytics-function` consome eventos do tópico Kafka `feedback.created`, registra os feedbacks recebidos em seu banco próprio e atualiza os relatórios semanais automaticamente.

---

## 🚀 Tech Stack

- Java 21
- Quarkus 3.x
- PostgreSQL
- Flyway
- Hibernate ORM / JPA
- Kafka / Redpanda
- SmallRye Reactive Messaging
- Quarkus Security
- SmallRye JWT

---

## 📌 Overview

O Analytics foi criado para:

- Consumir eventos de feedback via Kafka
- Persistir eventos analíticos recebidos
- Atualizar relatórios semanais automaticamente
- Consolidar feedbacks por dia
- Consolidar feedbacks por urgência
- Calcular média semanal de notas
- Expor relatórios em JSON
- Exportar relatórios em CSV
- Proteger endpoints administrativos com JWT

---

## ⚙️ Porta da aplicação

```text
http://localhost:8082
```

---

## 🗄️ Banco de dados

Banco utilizado pelo módulo:

```text
analytics_platform
```

Conexão local:

```text
Host: localhost
Port: 5435
Database: analytics_platform
Username: tech_challenge4
```

Tabelas principais:

- `analytics_feedback_events`
- `weekly_reports`
- `daily_report_items`
- `urgency_report_items`

---

## 📡 Kafka

O `analytics-function` atua como **consumer** do tópico:

```text
feedback.created
```

Consumer group:

```text
analytics-function
```

Configuração principal:

```properties
mp.messaging.incoming.feedback-created.connector=smallrye-kafka
mp.messaging.incoming.feedback-created.topic=feedback.created
mp.messaging.incoming.feedback-created.group.id=analytics-function
mp.messaging.incoming.feedback-created.value.deserializer=br.com.fiap.analytics.adapter.input.messaging.FeedbackEventDTODeserializer
mp.messaging.incoming.feedback-created.auto.offset.reset=earliest
```

---

## 🔄 Fluxo de processamento

```text
feedback-function
→ publica evento feedback.created
→ analytics-function consome o evento
→ salva o evento em analytics_feedback_events
→ atualiza o relatório semanal
→ disponibiliza dados em /admin/reports/weekly
```

---

## 🧠 Como o Analytics funciona

Quando um feedback é criado no `feedback-function`, um evento é publicado no Kafka.

O `analytics-function` consome esse evento e:

1. verifica se o feedback já foi processado;
2. salva o evento recebido em `analytics_feedback_events`;
3. identifica a semana correspondente ao feedback;
4. atualiza ou cria o registro em `weekly_reports`;
5. recria os itens diários em `daily_report_items`;
6. recria os itens por urgência em `urgency_report_items`.

Esse fluxo evita duplicidade e mantém os relatórios atualizados automaticamente.

---

## 🗄️ Database Model

### `analytics_feedback_events`

Tabela responsável por armazenar os eventos de feedback recebidos via Kafka.

| Column | Description |
|---|---|
| `feedback_id` | Identificador do feedback original |
| `description` | Descrição do feedback |
| `score` | Nota do feedback |
| `urgency_level` | Urgência calculada pelo `feedback-function` |
| `submitted_at` | Data/hora em que o feedback foi criado |
| `processed_at` | Data/hora em que o Analytics processou o evento |

> O campo `feedback_id` é a chave primária da tabela para evitar processamento duplicado do mesmo feedback.

---

### `weekly_reports`

Tabela responsável por armazenar o resumo semanal dos feedbacks.

| Column | Description |
|---|---|
| `id` | Identificador do relatório |
| `period_start` | Data inicial da semana |
| `period_end` | Data final da semana |
| `average_score` | Média das notas no período |
| `total_feedbacks` | Total de feedbacks no período |
| `generated_at` | Data/hora da geração ou atualização do relatório |

---

### `daily_report_items`

Tabela responsável por armazenar a quantidade de feedbacks por dia dentro de uma semana.

| Column | Description |
|---|---|
| `id` | Identificador do item |
| `weekly_report_id` | FK para `weekly_reports.id` |
| `date` | Data do item |
| `feedback_count` | Quantidade de feedbacks no dia |

---

### `urgency_report_items`

Tabela responsável por armazenar a quantidade de feedbacks por nível de urgência dentro de uma semana.

| Column | Description |
|---|---|
| `id` | Identificador do item |
| `weekly_report_id` | FK para `weekly_reports.id` |
| `urgency_level` | Nível de urgência |
| `feedback_count` | Quantidade de feedbacks com aquela urgência |

---

## 🔐 Segurança JWT

Os endpoints administrativos do Analytics são protegidos por JWT.

Apenas tokens com perfil/grupo `ADMIN` podem acessar os relatórios.

Resultado esperado:

| Cenário | Resultado |
|---|---|
| Sem token | HTTP 401 Unauthorized |
| Token sem perfil ADMIN | HTTP 403 Forbidden |
| Token ADMIN válido | HTTP 200 OK |

---

## 🔑 Configuração JWT

A aplicação utiliza chave pública para validar os tokens.

Chave pública:

```text
src/main/resources/security/publicKey.pem
```

Chave privada local usada para gerar tokens de teste:

```text
local-keys/privateKey.pem
```

> A chave privada é usada apenas localmente e não deve ser versionada.

Configuração principal:

```properties
mp.jwt.verify.publickey.location=security/publicKey.pem
mp.jwt.verify.issuer=feedback-platform
quarkus.native.resources.includes=security/publicKey.pem
```

---

## 🔑 Gerar chaves JWT locais

Execute dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateJwtKeys" "-Dexec.classpathScope=test"
```

---

## 🔑 Gerar token ADMIN

Execute dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateAdminToken" "-Dexec.classpathScope=test" "-Dsmallrye.jwt.sign.key.location=file:local-keys/privateKey.pem"
```

---

## 🔑 Gerar token STUDENT

Execute dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateStudentToken" "-Dexec.classpathScope=test" "-Dsmallrye.jwt.sign.key.location=file:local-keys/privateKey.pem"
```

---

## 📡 Endpoints

### Consultar relatórios semanais

```http
GET /admin/reports/weekly
Authorization: Bearer <ADMIN_JWT>
```

Resposta esperada:

```text
HTTP 200 OK
```

---

### Exportar relatórios semanais em CSV

```http
GET /admin/reports/weekly/export
Authorization: Bearer <ADMIN_JWT>
```

Resposta esperada:

```text
HTTP 200 OK
Content-Type: text/csv
```

---

## ⚙️ Running the Application

Antes de executar o módulo, garanta que a infraestrutura esteja ativa na raiz do projeto:

```bash
docker compose up -d
```

Depois, dentro da pasta `analytics-function`, execute:

```bash
mvn quarkus:dev
```

---

## ✅ Logs esperados

Ao iniciar corretamente:

```text
analytics-function started
Listening on: http://0.0.0.0:8082
Kafka consumer ... connected to Kafka brokers 'localhost:9092'
belongs to the 'analytics-function' consumer group
```

Ao consumir um evento:

```text
Evento recebido no analytics: feedbackId=<feedbackId> score=<score> urgência=<urgency>
Analytics atualizado para feedbackId=<feedbackId>
```

---

## 🧪 Consultas úteis

Ver eventos processados pelo Analytics:

```sql
SELECT *
FROM analytics_feedback_events;
```

Ver relatórios semanais:

```sql
SELECT *
FROM weekly_reports;
```

Ver itens por dia:

```sql
SELECT *
FROM daily_report_items;
```

Ver itens por urgência:

```sql
SELECT *
FROM urgency_report_items;
```

---

## ⚠️ Observação sobre processamento manual

O fluxo principal do Analytics não depende mais de requisição manual.

O processamento correto ocorre automaticamente por meio do consumo do tópico Kafka `feedback.created`.

Caso ainda exista o endpoint abaixo no código, ele deve ser tratado como legado ou usado apenas para testes controlados:

```http
POST /analytics/process-feedback
```

---

## ⚠️ Observação sobre dados antigos

Feedbacks antigos já salvos apenas no banco `feedback_platform` não são sincronizados automaticamente pelo Analytics.

O Analytics processa eventos recebidos via Kafka. Portanto, feedbacks antigos só serão processados se seus eventos ainda estiverem disponíveis no tópico `feedback.created`.

---

## 🧩 Architecture Notes

- O módulo possui banco próprio: `analytics_platform`
- O módulo não consulta diretamente o banco `feedback_platform`
- A integração com Feedback ocorre via Kafka
- O campo `feedback_id` é uma referência lógica ao feedback original
- O relatório é atualizado automaticamente após o consumo do evento
- A tabela `analytics_feedback_events` evita duplicidade de processamento
- Os endpoints administrativos são protegidos por JWT

---

## 💡 Key Features Implemented

- Consumo de eventos Kafka
- Persistência de eventos analíticos
- Atualização automática de relatórios semanais
- Consolidação por dia
- Consolidação por urgência
- Cálculo de média semanal
- Exportação CSV
- Segurança com JWT
- Banco independente por domínio

---

## 📚 References

- https://quarkus.io/
- https://flywaydb.org/
- https://kafka.apache.org/
- https://smallrye.io/smallrye-reactive-messaging/