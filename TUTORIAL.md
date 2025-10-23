# 🧩 Tutorial — Migração para PrimeFaces (p:dataTable)

## 🎯 Objetivo
Substituir as listagens simples em `<ui:repeat>` por um componente moderno do **PrimeFaces** (`p:dataTable`) que oferece paginação, ordenação e filtros automáticos.

---

## ⚙️ 1. Adicionando PrimeFaces ao Projeto Quarkus

Abra o arquivo `pom.xml` e adicione as dependências abaixo dentro de `<dependencies>`:

```xml
<!-- PrimeFaces -->
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>13.0.0</version>
</dependency>
```

Essas dependências funcionam junto com o MyFaces já existente no projeto.

Execute novamente o build para atualizar o Quarkus:
```bash
mvn -U clean compile quarkus:dev
```

---

## 🖼️ 2. Atualizando o `index.xhtml`

Abra `src/main/resources/META-INF/resources/index.xhtml` e adicione o namespace do PrimeFaces no topo da página:

```xhtml
xmlns:p="http://primefaces.org/ui"
```

Em seguida, substitua a listagem de **Livros** que antes usava `<ui:repeat>` por esta tabela:

```xhtml
<h2 class="section-title">Livros</h2>
<h:form id="formLivros">
    <p:dataTable id="tblLivros"
                 value="#{bibliotecaBean.livros}"
                 var="l"
                 paginator="true"
                 rows="5"
                 rowsPerPageTemplate="5,10,20"
                 paginatorPosition="bottom"
                 responsiveLayout="scroll"
                 sortMode="single"
                 filterDelay="400">

        <p:column headerText="Título" sortBy="#{l.titulo}" filterBy="#{l.titulo}" filterMatchMode="contains">
            <h:outputText value="#{l.titulo}" />
        </p:column>

        <p:column headerText="Autor" sortBy="#{l.autor.nome}" filterBy="#{l.autor.nome}" filterMatchMode="contains">
            <h:outputText value="#{l.autor.nome}" />
        </p:column>

        <p:column headerText="ISBN" sortBy="#{l.isbn}" filterBy="#{l.isbn}" filterMatchMode="contains">
            <h:outputText value="#{l.isbn}" />
        </p:column>

        <p:column headerText="Status" sortBy="#{l.disponivel}">
            <h:outputText value="Disponível" rendered="#{l.disponivel}" styleClass="ok"/>
            <h:outputText value="Emprestado" rendered="#{!l.disponivel}" styleClass="bad"/>
        </p:column>

        <p:column headerText="Ações" exportable="false" style="width:160px; text-align:center">
            <h:commandButton value="Excluir" action="#{bibliotecaBean.excluirLivro(l.id)}" styleClass="btn" />
        </p:column>

    </p:dataTable>
</h:form>
```

Essa versão usa:
- **Paginação** (`paginator="true"`)
- **Ordenação e Filtros** (`sortBy`, `filterBy`)
- **Layout Responsivo** (`responsiveLayout="scroll"`)

---

## 🧠 3. Testando

Execute o projeto novamente:
```bash
mvn quarkus:dev
```
Acesse:
```
http://localhost:8080/index.xhtml
```

Você verá a lista de livros com **paginação, filtros e ordenação** já funcionando.