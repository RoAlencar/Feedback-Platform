# 🎯 Feedback Platform - Tech Challenge Fase 4

![Java](https://img.shields.io/badge/Java-21-blue)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-red)
![Architecture](https://img.shields.io/badge/Architecture-Serverless-green)
![Cloud](https://img.shields.io/badge/Cloud-Ready-blueviolet)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

---

## 📌 Visão geral

Plataforma para coleta e análise de feedbacks de estudantes, com foco em:

- Arquitetura serverless
- Escalabilidade
- Baixo acoplamento
- Processamento orientado a eventos

---

## 🧠 Arquitetura da solução

A solução adota princípios importantes de engenharia de software:

- **Responsabilidade Única (SRP)**
- **Arquitetura orientada a eventos**
- **Computação serverless**

---

## 🧩 Módulos

| Módulo                  | Responsabilidade                            |
| ----------------------- | ------------------------------------------- |
| `feedback-function`     | Receber e persistir feedbacks               |
| `notification-function` | Enviar notificações para feedbacks críticos |
| `analytics-function`    | Gerar relatórios e métricas                 |
| `shared-lib`            | Contratos e DTOs compartilhados             |

---

## 🏗️ Estrutura do projeto

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

## 🔄 Diagrama de arquitetura

```text
                ┌───────────────┐
                │   API Gateway │
                └───────┬───────┘
                        │
                        ▼
              ┌───────────────────┐
              │ feedback-function │
              │ (recebe feedback) │
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

## 🔄 Fluxo da aplicação

1. Cliente envia um feedback (`POST /avaliacao`).
2. `feedback-function`:
   - valida o payload;
   - persiste o registro;
   - publica um evento (`feedback.created`).
3. `notification-function`:
   - consome o evento;
   - aplica regras de notificação e envia e-mails para casos críticos/altos.
4. `analytics-function`:
   - consome o evento feedback.created via Kafka;
   - registra o evento no analytics_platform;
   - atualiza os relatórios semanais automaticamente.

---

## ⚙️ Tecnologias principais

- Java 21
- Quarkus 3.x
- Mensageria (Kafka)
- PostgreSQL (Flyway para migrações)

---

## ☁️ Modelo de implantação (visão)

- API Gateway
- Functions (Lambda / Azure Functions / Cloud Run)
- Mensageria (Kafka / PubSub)
- Banco gerenciado
- Processamento assíncrono via Kafka

---

## 🔐 Segurança

- Autenticação e autorização (JWT para endpoints administrativos)
- Proteção de dados em trânsito

---

## ✅ Execução local

O repositório possui um `docker-compose.yml` na raiz que agrega os `docker-compose.yml` de cada módulo e inicializa a infraestrutura local necessária para executar e verificar a aplicação.

Ao rodar o compose a partir da raiz, serão iniciados (resumo):

- Broker Kafka (topic `feedback.created`) em `localhost:9092`;
- `feedback-function` e seu Postgres em `localhost:5434`, app em `localhost:8080`;
- `notification-function`, Postgres em `localhost:5436`, Mailpit em `localhost:1025` (SMTP) e `http://localhost:8025` (UI), app em `localhost:8081`;
- `analytics-function` e seu Postgres em `localhost:5435`, app em `localhost:8082`.

Como iniciar (a partir da raiz do repositório):

```bash
docker compose up --build
# ou
docker-compose up --build
```

Como encerrar e limpar o ambiente local ao final dos testes:

```bash
docker compose down -v
# ou
docker-compose down -v
```

Esse comando remove os containers e também os volumes criados pelo Compose, evitando sobras de banco/mensageria entre execuções locais.

O que verificar:

- Aguardar logs de inicialização das aplicações Quarkus e a conclusão das migrações Flyway;
- Abrir `http://localhost:8025` para visualizar e-mails no Mailpit (perfil de desenvolvimento usa Mailpit);
- Verificar que o container `kafka` está em execução (`docker ps`) e que a porta `9092` está mapeada;

- Para testar, importe a coleção Postman em `postman/feedback-platform-current-state.postman_collection.json` ou utilize os payloads presentes na seção de checklist deste README.

---

## ✅ Checklist de verificação (cenários obrigatórios)

A coleção Postman em `postman/feedback-platform-current-state.postman_collection.json` contém todos esses cenários prontos para execução.

---

### 1. Criação de feedback válido

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "22222222-2222-2222-2222-222222222222",
  "courseId": "bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb",
  "description": "Conteúdo bom, exercícios adequados",
  "score": 9
}
```

- Resultado esperado: **HTTP 201** — registro persistido no banco do `feedback-function`.

---

### 2. Feedback crítico gera notificação

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "22222222-2222-2222-2222-222222222222",
  "courseId": "bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb",
  "description": "Conteudo péssimo e exercicios inadequados",
  "score": 2
}
```

- Resultado esperado: **HTTP 201** — `notification-function` consome o evento `feedback.created` e Mailpit (`http://localhost:8025`) exibe os e-mails enviados aos administradores.

---

### 3. Estudante inexistente

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "99999999-9999-9999-9999-999999999999",
  "courseId": "aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa",
  "description": "Aluno inexistente",
  "score": 6
}
```

- Resultado esperado: **HTTP 400** — validação de existência do estudante.

---

### 4. Curso inexistente

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "11111111-1111-1111-1111-111111111111",
  "courseId": "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee",
  "description": "Curso inexistente",
  "score": 6
}
```

- Resultado esperado: **HTTP 400** — validação de existência do curso.

---

### 5. Matrícula inválida (estudante não matriculado no curso)

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "11111111-1111-1111-1111-111111111111",
  "courseId": "bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb",
  "description": "Tentativa com matricula inexistente",
  "score": 5
}
```

> Ana (`11111111-...`) está matriculada em Arquitetura de Software, **não** em Banco de Dados.

- Resultado esperado: **HTTP 400** — validação de matrícula/inscrição.

---

### 6. Payload inválido — falta `studentId`

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "courseId": "aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa",
  "description": "Payload sem studentId",
  "score": 7
}
```

- Resultado esperado: **HTTP 400** — campo obrigatório ausente.

---

### 7. Score fora do intervalo

- Método: `POST http://localhost:8080/avaliacao`
- Payload:

```json
{
  "studentId": "11111111-1111-1111-1111-111111111111",
  "courseId": "aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa",
  "description": "Score invalido",
  "score": 11
}
```

- Resultado esperado: **HTTP 400** — score deve estar entre 0 e 10.

---

### 8. Endpoints protegidos (analytics/admin)

- Método: `GET http://localhost:8082/admin/reports/weekly`
- Sem token (nenhum header de autorização).
- Com token de estudante: `Authorization: Bearer <STUDENT_JWT>`
- Com token ADMIN: `Authorization: Bearer <ADMIN_JWT>`

- Resultado esperado:
  - Sem token → **HTTP 401**
  - Token de estudante → **HTTP 403**
  - Token ADMIN válido → **HTTP 200** com relatório

---

### 9. Processamento analítico automático

- Enviar um feedback em `POST http://localhost:8080/avaliacao`
- Validar no log do `analytics-function` o consumo do evento `feedback.created`
- Consultar `GET http://localhost:8082/admin/reports/weekly` com token ADMIN

Resultado esperado:
- evento salvo em `analytics_feedback_events`;
- relatório atualizado em `weekly_reports`;
- itens atualizados em `daily_report_items` e `urgency_report_items`.

### 10. Observabilidade das notificações

- Passos: enviar o payload do cenário 2 (feedback crítico gera notificação) e abrir `http://localhost:8025`.
- Resultado esperado: e-mail registrado no Mailpit com conteúdo e destinatários configurados.

---

## 📡 Endpoint

### POST /avaliacao

Request body (exemplo):

```json
{
  "studentId": "<uuid>",
  "courseId": "<uuid>",
  "description": "string",
  "score": 0
}
```

---

## 🚨 Regra de urgência da avaliação

A urgência do feedback é calculada automaticamente a partir da nota informada:

| Faixa da nota | Urgência |
| ------------- | -------- |
| 0 a 2         | CRITICAL |
| 3 a 4         | HIGH     |
| 5 a 7         | MEDIUM   |
| 8 a 10        | LOW      |

Quanto menor a nota, maior a urgência. Esse valor é persistido internamente no campo `urgency_level` e não é retornado no response público do endpoint `POST /avaliacao`.

---

## 🔔 Notificações

Disparadas quando:

- Nota ≤ 4

Dados enviados nas notificações:

- Descrição
- Urgência
- Data

---

## 📊 Relatórios

Relatórios semanais incluem:

- média de notas;
- total de feedbacks;
- volume por dia;
- volume por urgência.

---

## 🧪 Monitoramento

- Execução das funções
- Falhas e retries
- Eventos críticos

---

## 📚 Decisões arquiteturais

- Separação por domínio
- Comunicação via eventos
- Serverless para escalabilidade
- `shared-lib` para padronização de contratos

---

## 🎥 Demonstração

Vídeo demonstrando o fluxo completo, as funções e o ambiente local/cloud.

---

## 👨‍💻 Autores

Projeto desenvolvido para o Tech Challenge - Fase 4.

- Everton Barbosa
- Felipe Tiburcio
- Lucas Novaes
- Luís Fernando Nascimento
- Rodrigo de Alencar Xavier

---

## 💡 Considerações finais

Arquitetura preparada para evolução futura e migração para microserviços com mínimo impacto.
