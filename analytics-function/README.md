# Analytics Function

Módulo responsável por disponibilizar relatórios consolidados de feedbacks para consulta administrativa.

## Segurança dos endpoints administrativos

Os endpoints administrativos da `analytics-function` são protegidos com Quarkus Security utilizando JWT.

A validação é feita por meio de Bearer Token, e o acesso aos relatórios é permitido apenas para usuários com o grupo/perfil `ADMIN`.

---

## Endpoints protegidos

### Consultar relatórios semanais em JSON

```http
GET /admin/reports/weekly
Authorization: Bearer <token-admin>
```

Esse endpoint retorna os relatórios semanais em formato JSON.

### Exportar relatórios semanais em CSV

```http
GET /admin/reports/weekly/export
Authorization: Bearer <token-admin>
```

Esse endpoint retorna os relatórios semanais em formato CSV para exportação/download.

---

## Configuração JWT

A aplicação utiliza a chave pública abaixo para validar os tokens JWT recebidos:

```text
analytics-function/src/main/resources/security/publicKey.pem
```

A chave privada é utilizada apenas localmente para gerar tokens de desenvolvimento e não deve ser versionada no GitHub:

```text
analytics-function/local-keys/privateKey.pem
```

A pasta `local-keys` deve permanecer no `.gitignore`.

### Configurações no `application.properties`

```properties
# JWT Security
mp.jwt.verify.publickey.location=security/publicKey.pem
mp.jwt.verify.issuer=feedback-platform
quarkus.native.resources.includes=security/publicKey.pem
```

O `issuer` configurado na aplicação precisa ser o mesmo utilizado na geração do token.

---

## Geração das chaves JWT locais

Para ambientes locais ou em caso de troca de máquina, as chaves podem ser geradas novamente por meio da classe:

```text
analytics-function/src/test/java/br/com/fiap/analytics/security/GenerateJwtKeys.java
```

Essa classe gera:

```text
analytics-function/local-keys/privateKey.pem
analytics-function/src/main/resources/security/publicKey.pem
```

A `privateKey.pem` fica apenas no ambiente local e é usada para assinar os tokens.

A `publicKey.pem` fica no projeto e é usada pela aplicação Quarkus para validar os tokens recebidos.

---
## Comando para gerar as chaves

Executar dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateJwtKeys" "-Dexec.classpathScope=test"
```

---

## Comando para gerar token ADMIN

Executar dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateAdminToken" "-Dexec.classpathScope=test" "-Dsmallrye.jwt.sign.key.location=file:local-keys/privateKey.pem"
```


---

## Comando para gerar token STUDENT

Executar dentro da pasta `analytics-function`:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=br.com.fiap.analytics.security.GenerateStudentToken" "-Dexec.classpathScope=test" "-Dsmallrye.jwt.sign.key.location=file:local-keys/privateKey.pem"
```

---


