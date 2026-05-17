# 🎯 Feedback Platform - Tech Challenge Fase 4

![Java](https://img.shields.io/badge/Java-21-blue)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-red)
![Architecture](https://img.shields.io/badge/Architecture-Serverless-green)
![Cloud](https://img.shields.io/badge/Cloud-Ready-blueviolet)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

---

## 📌 Visão Geral

Plataforma de coleta e análise de feedbacks de estudantes, com foco em:

- Serverless
- Escalabilidade
- Baixo acoplamento
- Processamento orientado a eventos

---

## 🧠 Arquitetura da Solução

A solução segue:

- **Responsabilidade Única (SRP)**
- **Event-Driven Architecture**
- **Serverless Computing**

---

## 🧩 Módulos

| Módulo                  | Responsabilidade         |
| ----------------------- | ------------------------ |
| `feedback-function`     | Recebe feedbacks         |
| `notification-function` | Notifica críticos        |
| `analytics-function`    | Gera relatórios          |
| `shared-lib`            | Contratos compartilhados |

---

## 🏗️ Estrutura do Projeto

```bash
feedback-platform/
│
├── shared-lib/
├── feedback-function/
├── notification-function/
├── analytics-function/
└── infra/
```

---

## 🔄 Diagrama de Arquitetura

```text
                ┌───────────────┐
                │   API Gateway │
                └───────┬───────┘
                        │
                        ▼
              ┌───────────────────┐
              │ feedback-function │
              │ (recebe feedback)│
              └────────┬─────────┘
                       │
                 (evento)
                       │
        ┌──────────────┴──────────────┐
        ▼                             ▼
┌────────────────────┐     ┌────────────────────┐
│ notification-func  │     │ analytics-function │
│ (alertas críticos) │     │ (relatórios)       │
└────────────────────┘     └────────────────────┘
        │                             │
        ▼                             ▼
   📧 Notificação               📊 Relatórios
```

---

## 🔄 Fluxo da Aplicação

1. Cliente envia feedback (`POST /avaliacao`)
2. `feedback-function`:
   - valida
   - salva
   - publica evento

3. `notification-function`:
   - consome evento
   - envia alerta se crítico

4. `analytics-function`:
   - roda por agendamento
   - gera relatório semanal

---

## ⚙️ Tecnologias

- Java 21
- Quarkus
- Serverless
- Mensageria (Kafka / SQS / PubSub)
- Banco de dados relacional

---

## ☁️ Modelo Cloud

- API Gateway
- Functions (Lambda / Azure / GCP)
- Mensageria
- Banco gerenciado
- Scheduler (cron)

---

## 🔐 Segurança

- Controle de acesso
- Proteção de dados
- Comunicação segura

---

## 🚀 Deploy

Deploy independente por módulo:

```bash
/infra
```

---

## 📡 Endpoint

### POST /avaliacao

```json
{
  "descricao": "string",
  "nota": 0-10
}
```

---

## 🔔 Notificações

Disparadas quando:

- Nota ≤ 4

Dados:

- Descrição
- Urgência
- Data

---

## 📊 Relatórios

Gerados semanalmente com:

- Média de notas
- Volume por dia
- Volume por urgência
- Lista de feedbacks

---

## 🧪 Monitoramento

- Execução das funções
- Falhas
- Eventos críticos

---

## 📚 Decisões Arquiteturais

- Separação por domínio
- Comunicação via eventos
- Serverless para escalabilidade
- Shared-lib para padronização

---

## 🎥 Demonstração

Vídeo mostrando:

- Fluxo completo
- Funções serverless
- Ambiente cloud

---

## 🧪 Guia de Testes — Alerta Crítico

> **Requisito:** Notificar administradores quando um feedback receber urgência `CRITICAL`.

### Serviços e Portas

| Serviço               | URL / Endereço                                                     |
| --------------------- | ------------------------------------------------------------------ |
| feedback-function     | http://localhost:8080 — DB: `localhost:5433/feedback_platform`     |
| notification-function | http://localhost:8081 — DB: `localhost:5434/notification_platform` |
| Kafka                 | `localhost:9092`                                                   |
| Mailpit SMTP          | `localhost:1025`                                                   |
| Mailpit Web UI        | http://localhost:8025                                              |

### Pré-requisitos — Subir os serviços

**1. Kafka** (raiz do projeto):

```bash
docker compose up -d
```

**2. PostgreSQL + Mailpit** da notification-function:

```bash
docker compose -f notification-function/docker-compose.yml up -d
```

**3. PostgreSQL** da feedback-function:

```bash
docker compose -f feedback-function/docker-compose.yml up -d
```

**4a. Iniciar feedback-function** (terminal separado):

```bash
cd feedback-function && ./mvnw quarkus:dev
```

**4b. Iniciar notification-function** (terminal separado):

```bash
cd notification-function && ./mvnw quarkus:dev
```

> Aguardar os logs `Listening on: http://0.0.0.0:8080` e `http://0.0.0.0:8081` antes de prosseguir.

### Condição de Disparo (`NotificationPolicy.java`)

Notificação é enviada **somente** quando `urgency == "CRITICAL"`.  
`UrgencyLevel` é derivado do campo `grade` via `Score.java`:

| grade | UrgencyLevel | Dispara notificação? |
| ----- | ------------ | -------------------- |
| 0–2   | `CRITICAL`   | ✅ sim               |
| 3–4   | `HIGH`       | ❌ não               |
| 5–7   | `MEDIUM`     | ❌ não               |
| 8–10  | `LOW`        | ❌ não               |

### UUIDs Válidos

#### Students — seed `V106__insert_initial_students_courses_enrollments.sql`

| UUID                                   | Nome           |
| -------------------------------------- | -------------- |
| `11111111-1111-1111-1111-111111111111` | Ana Souza      |
| `22222222-2222-2222-2222-222222222222` | Bruno Lima     |
| `33333333-3333-3333-3333-333333333333` | Carla Mendes   |
| `44444444-4444-4444-4444-444444444444` | Diego Oliveira |
| `55555555-5555-5555-5555-555555555555` | Fernanda Alves |

#### Courses

| UUID                                   | Nome                    |
| -------------------------------------- | ----------------------- |
| `aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa` | Arquitetura de Software |
| `bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb` | Banco de Dados          |
| `cccccccc-3333-3333-3333-cccccccccccc` | Cloud Computing         |
| `dddddddd-4444-4444-4444-dddddddddddd` | Engenharia de Software  |

#### Admins — seed `V204__insert_initial_admins.sql` (notification DB)

| UUID                                   | Nome           | Email                      |
| -------------------------------------- | -------------- | -------------------------- |
| `99999999-1111-1111-1111-999999999999` | Mariana Costa  | mariana.costa@fiap.com.br  |
| `88888888-2222-2222-2222-888888888888` | Rafael Pereira | rafael.pereira@fiap.com.br |
| `77777777-3333-3333-3333-777777777777` | Juliana Rocha  | juliana.rocha@fiap.com.br  |

### Passo 1 — Verificar/Criar Administradores

Confirmar que os admins ativos existem no banco da `notification-function`:

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "SELECT id, name, email, is_active FROM admins WHERE is_active = TRUE;"
```

**Esperado:** 3 linhas (Mariana, Rafael, Juliana).

#### Criar admin manualmente (opcional, se tabela estiver vazia)

**POST** `http://localhost:8081/admins`

```json
{
  "name": "Admin Teste",
  "email": "admin.teste@fiap.com.br"
}
```

Resposta esperada — `201 Created`:

```json
{
  "id": "<uuid gerado>",
  "name": "Admin Teste",
  "email": "admin.teste@fiap.com.br",
  "active": true,
  "type": "ADMIN"
}
```

### Passo 2 — Feedback NÃO crítico (grade 8) — não deve disparar notificação

**POST** `http://localhost:8080/students/11111111-1111-1111-1111-111111111111/courses/aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa/feedbacks`

```json
{
  "description": "Curso excelente, muito bem estruturado",
  "grade": 8
}
```

Resposta esperada — `201 Created`:

```json
{
  "id": "<uuid>",
  "description": "Curso excelente, muito bem estruturado",
  "score": 8,
  "urgency": "LOW",
  "createdAt": "<timestamp>",
  "processStatus": "PENDING"
}
```

Confirmar que **nenhuma** notificação foi gerada:

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "SELECT COUNT(*) FROM notifications;"
```

**Esperado:** `0` (ou o contador não aumentou).

### Passo 3 — Feedback CRÍTICO (grade ≤ 2) — deve disparar notificação

**POST** `http://localhost:8080/students/11111111-1111-1111-1111-111111111111/courses/aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa/feedbacks`

```json
{
  "description": "Péssimo atendimento, professor ausente e conteúdo desatualizado",
  "grade": 1
}
```

Resposta esperada — `201 Created`:

```json
{
  "id": "<uuid>",
  "description": "Péssimo atendimento, professor ausente e conteúdo desatualizado",
  "score": 1,
  "urgency": "CRITICAL",
  "createdAt": "<timestamp>",
  "processStatus": "PENDING"
}
```

> `urgency: "CRITICAL"` confirma que a condição de disparo foi ativada.  
> O sistema cria **1 registro de notificação por administrador ativo**. Com 3 admins no seed → esperar **3 linhas** no banco, todas com o mesmo `feedback_id` e `send_status = SENT`.

### Passo 4 — Verificar notificações no banco

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "
  SELECT
    n.id              AS notification_id,
    n.feedback_id,
    a.name            AS admin_name,
    a.email           AS admin_email,
    n.channel,
    n.send_status,
    n.attempts,
    n.sent_at,
    n.created_at
  FROM notifications n
  JOIN admins a ON a.id = n.receiver_id
  ORDER BY n.created_at DESC;"
```

**Esperado:** 3 linhas (uma por admin ativo), com:

| Campo         | Valor esperado       |
| ------------- | -------------------- |
| `channel`     | `EMAIL`              |
| `send_status` | `SENT`               |
| `attempts`    | `0`                  |
| `sent_at`     | timestamp preenchido |

### Passo 5 — Verificar conteúdo da notificação

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "SELECT message FROM notifications ORDER BY created_at DESC LIMIT 3;"
```

Formato esperado do campo `message`:

```
ALERTA: Feedback crítico recebido

Descrição:     Péssimo atendimento, professor ausente e conteúdo desatualizado
Urgência:      CRITICAL
Data de envio: 2026-05-15T...

Feedback ID:   <uuid>
```

**Checklist do conteúdo:**

- ✅ `descricao` → campo `Descrição: ...`
- ✅ `urgencia` → campo `Urgência: CRITICAL`
- ✅ `dataEnvio` → campo `Data de envio: ...` (`event.createdAt` do `FeedbackCreatedEvent`)

### Passo 6 — Verificar envio via Mailpit

O `%dev` profile aponta o mailer para o Mailpit (SMTP `localhost:1025`), sem mock.

#### Pré-requisito: iniciar Mailpit

```bash
docker compose -f notification-function/docker-compose.yml up -d
```

Confirmar que o container está UP:

```bash
docker compose -f notification-function/docker-compose.yml ps
```

**Esperado:** `notification-mailpit` rodando nas portas `1025` (SMTP) e `8025` (Web UI).

#### Verificar emails recebidos

Abrir no browser: **http://localhost:8025**

Verificar email com subject: **[ALERTA CRÍTICO] Feedback urgente recebido**

```
ALERTA: Feedback crítico recebido

Descrição:     Péssimo atendimento, professor ausente e conteúdo desatualizado
Urgência:      CRITICAL
Data de envio: 2026-05-15T...

Feedback ID:   <uuid do feedback>
```

> Serão exibidos **3 emails** (um para cada admin: Mariana, Rafael, Juliana).

### Passo 7 — Simular falha e validar retry

**1. Obter o ID de uma notificação:**

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "SELECT id FROM notifications ORDER BY created_at DESC LIMIT 1;"
```

**2. Simular falha** (substituir `<cole o id aqui>` pelo ID obtido acima):

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "
  UPDATE notifications
  SET send_status = 'FAILED',
      attempts    = 1,
      updated_at  = NOW()
  WHERE id = '<cole o id aqui>';"
```

**3. Verificar notificações elegíveis para retry** (attempts < 3):

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "
  SELECT id, feedback_id, send_status, attempts, updated_at
  FROM notifications
  WHERE send_status = 'FAILED'
    AND attempts < 3
  ORDER BY updated_at ASC;"
```

**Esperado:** linha com `send_status = FAILED` e `attempts = 1`.

### Passo 8 — Auditoria completa

```bash
docker exec notification-postgres psql -U tech_challenge4 -d notification_platform -c "
  SELECT
    n.id              AS notification_id,
    n.feedback_id,
    n.message,
    n.channel,
    n.send_status,
    n.attempts,
    n.sent_at,
    n.created_at,
    n.updated_at,
    a.name            AS admin_name,
    a.email           AS admin_email
  FROM notifications n
  JOIN admins a ON a.id = n.receiver_id
  ORDER BY n.created_at DESC;"
```

### Checklist de Requisitos

| Requisito                                | Como validar                                                             |
| ---------------------------------------- | ------------------------------------------------------------------------ |
| Condição de disparo (`urgency=CRITICAL`) | `grade <= 2` → campo `"urgency": "CRITICAL"` na resposta do POST         |
| Escolher canal de envio                  | `channel = EMAIL` na tabela `notifications`                              |
| Incluir `descricao` no conteúdo          | campo `message` no banco (Passo 5)                                       |
| Incluir `urgencia` no conteúdo           | campo `message` no banco (Passo 5)                                       |
| Incluir `dataEnvio` no conteúdo          | campo `message` + `sent_at` no banco (Passo 5)                           |
| Identificar destinatários admins         | 1 linha por admin ativo na tabela `notifications` (Passo 4)              |
| Registrar envio no log                   | `send_status`, `sent_at`, `attempts` na tabela `notifications` (Passo 4) |
| Tratar falha no envio                    | `send_status=FAILED`, `attempts` incrementado (Passo 7)                  |
| Testar envio simulado                    | Mailpit Web UI em http://localhost:8025 (Passo 6)                        |

### Troubleshooting

**`fk_feedbacks_student`**  
→ Use apenas os UUIDs de students listados acima. Não use UUIDs aleatórios.

**Schema validation `enrollment_date`**  
→ Já corrigido na migração `V103__create_enrollments.sql` (coluna definida como `DATE`). Com `flyway.clean-at-start=true` o schema é recriado corretamente a cada startup.

**Notificação não criada após POST crítico**  
→ Verificar logs da `notification-function` (porta 8081)  
→ Confirmar que Kafka está UP: `docker ps | grep kafka`  
→ Confirmar admins ativos: `SELECT * FROM admins WHERE is_active = TRUE;`

**`send_status = FAILED` (não simulado)**  
→ Garantir que Mailpit está UP: `docker compose -f notification-function/docker-compose.yml ps`  
→ Em dev, o mailer aponta para `localhost:1025`. Verificar se o container está rodando.  
→ Os logs da `notification-function` mostrarão o erro SMTP caso o Mailpit esteja offline.

---

## 👨‍💻 Autor(es)

Projeto desenvolvido para o Tech Challenge - Fase 4.

- Everton Barbosa
- Felipe Tiburcio
- Lucas Novaes
- Luís Fernando Nascimento
- Rodrigo de Alencar Xavier

---

## 💡 Considerações Finais

Arquitetura preparada para evolução futura, podendo escalar para microsserviços completos sem grandes refatorações.
