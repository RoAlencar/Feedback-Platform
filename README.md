# 🎯 Feedback Platform - Tech Challenge Fase 4

![Java](https://img.shields.io/badge/Java-21-blue)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-red)
![Architecture](https://img.shields.io/badge/Architecture-Serverless-green)
![Cloud](https://img.shields.io/badge/Cloud-Ready-blueviolet)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

---

## 📌 Visão Geral

Plataforma de coleta e análise de feedbacks de estudantes, com foco em:

* Serverless
* Escalabilidade
* Baixo acoplamento
* Processamento orientado a eventos

---

## 🧠 Arquitetura da Solução

A solução segue:

* **Responsabilidade Única (SRP)**
* **Event-Driven Architecture**
* **Serverless Computing**

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

    * valida
    * salva
    * publica evento
3. `notification-function`:

    * consome evento
    * envia alerta se crítico
4. `analytics-function`:

    * roda por agendamento
    * gera relatório semanal

---

## ⚙️ Tecnologias

* Java 21
* Quarkus
* Serverless
* Mensageria (Kafka / SQS / PubSub)
* Banco de dados relacional

---

## ☁️ Modelo Cloud

* API Gateway
* Functions (Lambda / Azure / GCP)
* Mensageria
* Banco gerenciado
* Scheduler (cron)

---

## 🔐 Segurança

* Controle de acesso
* Proteção de dados
* Comunicação segura

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

## 🚨 Regra de urgência da avaliação

A urgência do feedback será calculada automaticamente a partir da nota informada pelo estudante.

| Faixa da nota | Urgência |
|---|---|
| 0 a 2 | CRITICAL |
| 3 a 4 | HIGH |
| 5 a 7 | MEDIUM |
| 8 a 10 | LOW |

Quanto menor a nota, maior será a urgência do feedback. Essa informação será persistida internamente no campo `urgency_level`, mas não será retornada no response público do endpoint `POST /avaliacao`, pois será utilizada pelos fluxos internos de notificação, administração e analytics.

---

## 🔔 Notificações

Disparadas quando:

* Nota ≤ 4

Dados:

* Descrição
* Urgência
* Data

---

## 📊 Relatórios

Gerados semanalmente com:

* Média de notas
* Volume por dia
* Volume por urgência
* Lista de feedbacks

---

## 🧪 Monitoramento

* Execução das funções
* Falhas
* Eventos críticos

---

## 📚 Decisões Arquiteturais

* Separação por domínio
* Comunicação via eventos
* Serverless para escalabilidade
* Shared-lib para padronização

---

## 🎥 Demonstração

Vídeo mostrando:

* Fluxo completo
* Funções serverless
* Ambiente cloud

---

## 👨‍💻 Autor(es)

Projeto desenvolvido para o Tech Challenge - Fase 4.

* Everton Barbosa
* Felipe Tiburcio
* Lucas Novaes
* Luís Fernando Nascimento
* Rodrigo de Alencar Xavier

---

## 💡 Considerações Finais

Arquitetura preparada para evolução futura, podendo escalar para microserviços completos sem grandes refatorações.
