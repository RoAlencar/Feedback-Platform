# shared-lib

Biblioteca compartilhada da Feedback Platform.

A `shared-lib` centraliza classes reutilizadas pelos módulos da aplicação, como DTOs, eventos, entidades de domínio, value objects, exceptions e contratos de portas.

Ela é utilizada para evitar duplicidade de código e manter os contratos consistentes entre os módulos:

- `feedback-function`
- `notification-function`
- `analytics-function`

---

## Responsabilidade

A responsabilidade principal da `shared-lib` é fornecer contratos e objetos compartilhados entre os módulos da plataforma.

Ela não representa uma aplicação independente e não possui endpoint próprio.

---

## Estrutura principal

```text
shared-lib/
└── src/main/java/br/com/fiap/shared
    ├── application
    │   ├── dto
    │   ├── port
    │   │   ├── input
    │   │   └── output
    │   └── usecase
    ├── common
    └── domain
        ├── entity
        ├── event
        ├── exception
        └── valueObject
```