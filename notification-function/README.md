# 📩 Notification Function

Módulo da **Feedback Platform** responsável por consumir eventos de feedback e processar notificações para administradores.

O `notification-function` consome eventos do tópico Kafka `feedback.created`, verifica a criticidade do feedback e, quando aplicável, envia e-mail para administradores ativos e registra a notificação no banco `notification_platform`.

---

## 🚀 Tech Stack

- Java 21
- Quarkus 3.x
- PostgreSQL
- Flyway
- Hibernate ORM / JPA
- Kafka / Redpanda
- SmallRye Reactive Messaging
- Quarkus Mailer
- Mailpit

---

## 📌 Overview

O sistema de notificações foi criado para:

- Consumir eventos de feedback via Kafka
- Identificar feedbacks críticos
- Enviar e-mail para administradores ativos
- Registrar notificações no banco de dados
- Controlar status de envio
- Manter rastreabilidade por timestamps
- Permitir análise de falhas de envio

---

## ⚙️ Porta da aplicação

```text
http://localhost:8081
```

---

## 🗄️ Banco de dados

Banco utilizado pelo módulo:

```text
notification_platform
```

Conexão local:

```text
Host: localhost
Port: 5436
Database: notification_platform
Username: tech_challenge4
```

Tabelas principais:

- `admins`
- `notifications`

---

## 📡 Kafka

O `notification-function` atua como **consumer** do tópico:

```text
feedback.created
```

Consumer group:

```text
notification-function
```

Configuração principal:

```properties
mp.messaging.incoming.feedback-created.connector=smallrye-kafka
mp.messaging.incoming.feedback-created.topic=feedback.created
mp.messaging.incoming.feedback-created.group.id=notification-function
```

---

## 🔄 Fluxo de processamento

```text
feedback-function
→ publica evento feedback.created
→ notification-function consome o evento
→ verifica a criticidade
→ busca administradores ativos
→ envia e-mail
→ persiste a notificação
```

---

## 🧠 Regra de notificação

As notificações são persistidas e enviadas apenas quando o feedback for classificado como `CRITICAL`.

Feedbacks com urgência `MEDIUM`, `HIGH` ou `LOW` podem ser consumidos pelo Kafka, mas não necessariamente geram registro de notificação.

---

## 🧠 Notification Lifecycle

Cada notificação segue um fluxo de status:

```text
PENDING → SENT → FAILED
```

### Descrição dos status

| Status | Descrição |
|---|---|
| `PENDING` | Notificação criada e aguardando envio |
| `SENT` | Notificação enviada com sucesso |
| `FAILED` | Falha ao tentar enviar a notificação |

---

## 🔁 Retry Strategy

As notificações com falha podem ser identificadas pela seguinte consulta:

```sql
SELECT *
FROM notifications
WHERE send_status = 'FAILED'
  AND attempts < 3
ORDER BY updated_at ASC;
```

Regras previstas:

- Máximo de tentativas: `3`
- Processamento em ordem de atualização (`updated_at`)
- O contador `attempts` é incrementado quando ocorre falha no envio

---

## 🗄️ Database Model

### `admins`

Tabela responsável por armazenar os administradores que podem receber notificações.

| Column | Description |
|---|---|
| `id` | Primary key do administrador |
| `name` | Nome do administrador |
| `email` | E-mail do administrador |
| `is_active` | Indica se o administrador está ativo |

---

### `notifications`

Tabela responsável por armazenar as notificações geradas.

| Column | Description |
|---|---|
| `id` | Primary key da notificação |
| `feedback_id` | Referência lógica ao feedback criado no `feedback-function` |
| `receiver_id` | FK para `admins.id` |
| `channel` | Canal da notificação, atualmente `EMAIL` |
| `send_status` | Status da notificação: `PENDING`, `SENT` ou `FAILED` |
| `attempts` | Contador de tentativas |
| `message` | Conteúdo da notificação |
| `sent_at` | Data/hora do envio |
| `created_at` | Data/hora de criação |
| `updated_at` | Data/hora da última atualização |

> Importante: `feedback_id` não é uma FK física para a tabela `feedbacks`, pois o módulo de Notification possui banco próprio. Ele funciona como referência lógica ao feedback original criado no `feedback-function`.

---

## 🔐 Data Integrity

O sistema aplica regras de integridade no banco, incluindo:

- FK `receiver_id` → `admins.id`
- Campos obrigatórios com `NOT NULL`
- Valores padrão para campos operacionais
- Restrições de status e canal

Valores padrão principais:

```text
channel = EMAIL
send_status = PENDING
attempts = 0
created_at = CURRENT_TIMESTAMP
updated_at = CURRENT_TIMESTAMP
```

---

## 📧 Envio de e-mail em ambiente local

Em ambiente local, o envio de e-mail é feito via **Mailpit**.

Mesmo que o log mostre `Notificação enviada`, o e-mail não será entregue diretamente no Gmail real. Ele deverá ser visualizado na interface local do Mailpit:

```text
http://localhost:8025
```

Configuração SMTP local:

```text
Host: localhost
Port: 1025
```

---

## ⚙️ Running the Application

Antes de executar o módulo, garanta que a infraestrutura esteja ativa na raiz do projeto:

```bash
docker compose up -d
```

Depois, dentro da pasta `notification-function`, execute:

```bash
mvn quarkus:dev
```

---

## ✅ Logs esperados

Ao iniciar corretamente:

```text
notification-function started
Listening on: http://localhost:8081
Kafka consumer ... connected to Kafka brokers 'localhost:9092'
```

Ao consumir um evento:

```text
Evento recebido: feedbackId=<feedbackId> urgência=<urgency>
```

Ao enviar uma notificação:

```text
Notificação enviada para admin <email> - feedbackId: <feedbackId>
```

---

## 🧪 Consultas úteis

Ver administradores ativos:

```sql
SELECT *
FROM admins
WHERE is_active = TRUE;
```

Ver todas as notificações:

```sql
SELECT *
FROM notifications;
```

Ver notificações com falha:

```sql
SELECT *
FROM notifications
WHERE send_status = 'FAILED'
ORDER BY updated_at ASC;
```

---

## 📡 API

O `notification-function` não possui fluxo principal baseado em endpoint REST.

O processamento ocorre automaticamente por meio do consumo do tópico Kafka `feedback.created`.

---

## 🧩 Architecture Notes

- O módulo possui banco próprio: `notification_platform`
- O módulo não consulta diretamente o banco `feedback_platform`
- O campo `feedback_id` é uma referência lógica ao feedback original
- A comunicação com o Feedback ocorre via Kafka
- O envio local de e-mail ocorre via Mailpit
- O processamento de notificações é desacoplado do cadastro de feedbacks

---

## 💡 Key Features Implemented

- Consumo de eventos Kafka
- Processamento de feedbacks críticos
- Envio de e-mail para administradores ativos
- Persistência de notificações
- Controle de status de envio
- Controle de tentativas
- Auditoria com timestamps
- Banco independente por domínio

---

## 📚 References

- https://quarkus.io/
- https://flywaydb.org/
- https://kafka.apache.org/
- https://mailpit.axllent.org/