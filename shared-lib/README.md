# shared-lib

Biblioteca Java **compartilhada** entre os módulos `feedback-function`, `notification-function` e `analytics-function`.

Ela concentra tudo o que é **comum ao negócio** — entidades, regras de validação, DTOs e contratos (interfaces) — para que cada função serverless só precise se preocupar com sua própria responsabilidade e com como se conecta ao mundo externo (HTTP, banco, fila, etc.).

> Importante: esta lib NÃO é uma aplicação Quarkus. Ela é um **JAR puro** (`packaging=jar`). Não tem endpoints, não sobe servidor, não tem `application.properties`. Só classes Java para serem reusadas.

---

## 1. Conceitos rápidos (sem enrolação)

Usamos o padrão **Hexagonal / Ports & Adapters**. Na prática, só 3 coisas importam:

| Camada | O que é | Onde mora |
|---|---|---|
| **Domain** | O "coração" do negócio. Não depende de nada externo. | `domain/` |
| **Application** | Os **casos de uso** e as **interfaces** (portas) que descrevem o que precisa existir lá fora (banco, fila, etc.) | `application/` |
| **Adapter / Infrastructure** | **Não mora aqui.** É cada módulo que implementa as portas (ex: `feedback-function` cria um `FeedbackRepositoryMongo` que implementa `FeedbackRepositoryPort`). | Nos módulos consumidores |

Regra de ouro: **o shared-lib só define contratos**. Quem implementa (quem fala com o banco, com o Kafka, com o SQS) é cada módulo.

---

## 2. O que tem aqui dentro

```
br.com.fiap.shared
├── domain
│   ├── entity
│   │   └── Feedback                  # entidade principal
│   ├── valueobject
│   │   ├── Nota                      # int 0..10 + isCritica()
│   │   ├── Descricao                 # string não vazia
│   │   └── Urgencia                  # enum CRITICA / NORMAL
│   ├── event
│   │   └── FeedbackCriadoEvent       # evento publicado quando um feedback é criado
│   └── exception
│       ├── DomainException
│       └── ValidationException
├── application
│   ├── dto
│   │   ├── FeedbackRequest           # entrada da API (com @NotBlank / @Min / @Max)
│   │   ├── FeedbackResponse          # saída da API
│   │   └── FeedbackEventDTO          # payload serializável do evento
│   └── port
│       ├── input
│       │   └── CriarFeedbackUseCase  # contrato do caso de uso
│       └── output
│           ├── FeedbackRepositoryPort # contrato de persistência
│           └── EventPublisherPort     # contrato de publicação de eventos
└── common
    └── DateUtils
```

### Regras de negócio já embutidas

- `Nota` só aceita valores de **0 a 10** (qualquer coisa fora disso lança `ValidationException`).
- `Nota.isCritica()` retorna `true` quando `valor <= 4`.
- `Descricao` não pode ser nula nem em branco.
- `Feedback.criar(descricao, nota)` gera `id` (UUID), `criadoEm` (timestamp) e calcula a `Urgencia` automaticamente a partir da nota.

---

## 3. Como usar em cada módulo

### 3.1. Adicionar como dependência Maven

No `pom.xml` do módulo (ex: `feedback-function/pom.xml`), dentro de `<dependencies>`:

```xml
<dependency>
    <groupId>br.com.fiap</groupId>
    <artifactId>shared-lib</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Antes de rodar o módulo pela primeira vez, instale a lib no seu repositório Maven local:

```bash
cd shared-lib
./mvnw clean install
```

Isso publica o `shared-lib-1.0-SNAPSHOT.jar` em `~/.m2/repository`, de onde os outros módulos vão baixar.

### 3.2. Exemplo — `feedback-function` (cria e persiste o feedback)

```java
// 1) Adapter de entrada (REST) recebe o JSON e chama o caso de uso
@Path("/feedbacks")
public class FeedbackResource {

    @Inject CriarFeedbackUseCase criarFeedback;

    @POST
    public FeedbackResponse criar(@Valid FeedbackRequest request) {
        return criarFeedback.executar(request);
    }
}

// 2) Implementação do caso de uso (application service) vive no próprio feedback-function
@ApplicationScoped
public class CriarFeedbackService implements CriarFeedbackUseCase {

    @Inject FeedbackRepositoryPort repositorio;
    @Inject EventPublisherPort publicador;

    @Override
    public FeedbackResponse executar(FeedbackRequest request) {
        Feedback feedback = Feedback.criar(request.descricao(), request.nota());
        repositorio.salvar(feedback);

        publicador.publicar(new FeedbackCriadoEvent(
                feedback.getId(),
                feedback.getDescricao().valor(),
                feedback.getNota().valor(),
                feedback.getUrgencia().name(),
                feedback.getCriadoEm()));

        return FeedbackResponse.fromDomain(feedback);
    }
}

// 3) Adapters de saída implementam as portas (ex: Mongo + Kafka)
@ApplicationScoped
public class FeedbackMongoRepository implements FeedbackRepositoryPort { /* ... */ }

@ApplicationScoped
public class KafkaEventPublisher implements EventPublisherPort { /* ... */ }
```

### 3.3. Exemplo — `notification-function` (consome o evento e avisa quando é crítico)

```java
@ApplicationScoped
public class NotificacaoCriticaConsumer {

    @Incoming("feedbacks")
    public void consumir(FeedbackEventDTO evento) {
        if ("CRITICA".equals(evento.urgencia())) {
            // enviar e-mail / slack / push / etc.
        }
    }
}
```

O `notification-function` **não precisa** reimplementar o que é "crítico": o próprio shared-lib já derivou essa informação quando o feedback foi criado, e ela vem no DTO.

### 3.4. Exemplo — `analytics-function` (relatório agregado)

```java
@ApplicationScoped
public class RelatorioSemanalJob {

    @Inject FeedbackRepositoryPort repositorio;   // mesma interface, outro adapter

    @Scheduled(cron = "0 0 8 * * MON")
    public void gerar() {
        List<Feedback> todos = repositorio.buscarTodos();
        long criticos = todos.stream()
                .filter(f -> f.getUrgencia() == Urgencia.CRITICA)
                .count();
        // ... montar o relatório
    }
}
```

---

## 4. Fluxo resumido

```
 cliente
    │  POST /feedbacks { descricao, nota }
    ▼
 feedback-function  ──►  FeedbackRepositoryPort (salva no banco)
    │                ──►  EventPublisherPort   (publica FeedbackCriadoEvent)
    ▼
 barramento de mensageria (Kafka / SQS / PubSub)
    │
    ├──►  notification-function  (alerta se urgencia = CRITICA)
    └──►  analytics-function     (agrega métricas)
```

Todos consomem o **mesmo** `FeedbackEventDTO` definido aqui — assim ninguém precisa inventar o seu próprio formato e os módulos continuam desacoplados.

---

## 5. Build e testes

```bash
# Na raiz do shared-lib
./mvnw test          # roda os testes unitários (JUnit 5 puro)
./mvnw install       # publica o jar no ~/.m2 para os outros módulos consumirem
```

Requer **Java 21**.

---

## 6. Perguntas frequentes

**Por que não usar `quarkus-rest` aqui?**
Porque shared-lib não é uma aplicação — é só uma biblioteca. Endpoints ficam no módulo que os expõe (`feedback-function`).

**Posso colocar código que fala com banco / Kafka aqui?**
Não. Só contratos (interfaces). A implementação concreta (adapter) mora no módulo que precisa dela. Isso mantém a lib leve e evita acoplamento.

**Preciso de CDI (`@ApplicationScoped`, `@Inject`) para usar a lib?**
Não. A lib é Java puro. Os módulos consumidores (que são aplicações Quarkus) é que usam CDI para montar seus serviços e adapters.

**Onde valido o `FeedbackRequest`?**
No controller REST, anotando o parâmetro com `@Valid`. As anotações `@NotBlank`, `@Min(0)`, `@Max(10)` já estão no record.
