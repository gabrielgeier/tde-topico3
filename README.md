# 📚 Sistema de Biblioteca Digital (Quarkus + JSF + Hibernate)

Projeto desenvolvido em **Java 21** com **Quarkus 3.28.5**, **Jakarta Faces (MyFaces)** e **Hibernate ORM**.  
O sistema gerencia autores, livros e empréstimos, demonstrando relacionamentos JPA e arquitetura em camadas.

---

## 🚀 Tecnologias
- Quarkus 3.28.5  
- MyFaces (JSF)  
- Hibernate ORM  
- PostgreSQL  
- Jakarta EE (CDI, JPA, Faces)

---

## ⚙️ Requisitos
- Java 21  
- Maven 3.9+  
- PostgreSQL configurado localmente  

---

## ⚙️ Configuração do Banco de Dados

Edite o arquivo **src/main/resources/application.properties** conforme abaixo:

```
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=biblioteca
quarkus.datasource.password=biblioteca
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/biblioteca

quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.sql-load-script=import.sql

quarkus.myfaces.state-saving-method=client
quarkus.myfaces.number-of-views-in-session=50
quarkus.myfaces.project-stage=Development
```

---

## ▶️ Como Executar

1. Clone o repositório e entre na pasta do projeto:  
   ```bash
   git clone <url-do-repo>
   cd biblioteca
   ```

2. Execute em modo desenvolvimento:  
   ```bash
   mvn compile quarkus:dev
   ```

3. Acesse a aplicação no navegador:  
   ```
   http://localhost:8080/
   ```

Se aparecer *Forbidden* ao acessar apenas `/`, use o caminho completo `/index.xhtml`.

---

## 🧩 Estrutura do Projeto

```
biblioteca/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/biblioteca/
 │   │   │   ├── controller/
 │   │   │   ├── entity/
 │   │   │   ├── repository/
 │   │   │   └── service/
 │   │   └── resources/
 │   │       ├── META-INF/resources/index.xhtml
 │   │       ├── application.properties
 │   │       └── import.sql
 ├── pom.xml
 ├── .gitignore
 └── README.md
```