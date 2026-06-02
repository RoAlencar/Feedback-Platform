# feedback-function

Módulo responsável por receber, validar e persistir feedbacks de estudantes.

Após salvar um feedback no banco `feedback_platform`, o módulo publica um evento no tópico Kafka `feedback.created`, permitindo que os módulos `notification-function` e `analytics-function` processem o feedback de forma assíncrona.

---

## Responsabilidades

- Receber feedbacks via API REST
- Validar estudante
- Validar curso
- Validar matrícula ativa entre estudante e curso
- Criar o feedback com a regra de domínio
- Calcular a urgência do feedback
- Persistir o feedback no banco `feedback_platform`
- Publicar o evento `feedback.created` no Kafka

---

## Tecnologias

- Java 21
- Quarkus 3.x
- PostgreSQL
- Flyway
- Hibernate ORM / JPA
- Kafka / Redpanda
- SmallRye Reactive Messaging
- Maven

---

## Porta da aplicação

```text
http://localhost:8080
```