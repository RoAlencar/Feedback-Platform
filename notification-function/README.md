# 📩 Notification Function

This project is part of the Feedback Platform and is responsible for managing notifications lifecycle.

Built with **Quarkus**, it provides a robust and resilient notification system with retry control, status tracking, and full data integrity.

---

## 🚀 Tech Stack

- Java 21
- Quarkus 3.x
- PostgreSQL
- Flyway (database migrations)
- Hibernate ORM / JPA

---

## 📌 Overview

The notification system is designed to:

- Manage notification delivery lifecycle
- Ensure data integrity via foreign keys
- Track delivery status
- Support retry mechanisms
- Provide full auditability (timestamps)

---

## 🧠 Notification Lifecycle

Each notification follows a controlled flow:

PENDING → SENT → FAILED

### Status description:

- **PENDING** → Notification created, waiting to be processed
- **SENT** → Successfully delivered
- **FAILED** → Delivery failed (eligible for retry)

---

## 🔁 Retry Strategy

Failed notifications can be retried using:

```sql
SELECT *
FROM notifications
WHERE send_status = 'FAILED'
AND attempts < 3
ORDER BY updated_at ASC;

Rules:
    Max retry attempts: 3
    FIFO processing based on updated_at
    Attempts counter is incremented on failure

🗄️ Database Model
    notifications
    | Column      | Description                  |
    | ----------- | ---------------------------- |
    | id          | Primary key                  |
    | feedback_id | FK → feedbacks               |
    | receiver_id | FK → admins                  |
    | channel     | Notification channel (EMAIL) |
    | send_status | PENDING, SENT, FAILED        |
    | attempts    | Retry counter                |
    | message     | Notification content         |
    | sent_at     | Timestamp of sending         |
    | created_at  | Creation timestamp           |
    | updated_at  | Last update timestamp        |

🔐 Data Integrity

    The system enforces:

    FK feedback_id → feedbacks.id
    FK receiver_id → admins.user_id
    NOT NULL constraints for critical fields
    Default values:
    channel = EMAIL
    send_status = PENDING
    created_at = NOW()
    updated_at = NOW()

🧪 Integration Test Script

    A full test scenario is available via Flyway:

    V4__test_integrity.sql

    This script validates:

    Data integrity (FK constraints)
        Notification lifecycle
        Retry logic
        Failure scenarios
        Audit timestamps

    It also includes step-by-step validation queries to track database state evolution.

⚙️ Running the Application
▶️ Dev mode
    ./mvnw quarkus:dev

Dev UI available at:
    http://localhost:8080/q/dev/

📦 Package
    ./mvnw package


Run:
    java -jar target/quarkus-app/quarkus-run.jar

⚡ Native build (optional)
    ./mvnw package -Dnative

📡 API (Next Steps)
    Planned endpoints:
    POST /notifications → create notification
    GET /notifications → list notifications
    POST /notifications/retry → retry failed notifications

🧩 Architecture Notes
    Clean separation between domain and infrastructure
    Enum-based domain modeling:
        NotificationChannel
        NotificationSendStatus
    Flyway used for versioned schema evolution
    Designed for future integration with:
        Email providers
        Message queues (Kafka / RabbitMQ)

💡 Key Features Implemented

    ✔️ Notification lifecycle management
    ✔️ Retry mechanism with limit
    ✔️ Data integrity with FK constraints
    ✔️ Audit timestamps
    ✔️ Step-by-step SQL test scenario
    ✔️ Production-ready database modeling

👨‍💻 Author

    Equipe DEV
    Backend Developer | Java | Quarkus | Cloud

📚 References
    https://quarkus.io/
    https://flywaydb.org/
```
