
# 🚀 TaskFlow — Sistema de Gerenciamento de Tarefas

Este é um projeto full-stack desenvolvido para consolidar e demonstrar a integração prática entre um ecossistema backend em **Java (Spring Boot)** e um frontend dinâmico em **Angular**.

O objetivo principal é aplicar os conceitos fundamentais de uma arquitetura cliente-servidor através de um sistema clássico de gerenciamento de tarefas (To-Do List).

---

## 🛠️ Tecnologias Utilizadas

### **Backend**

* **Java 17+**
* **Spring Boot**: Estruturação da API REST.
* **Spring Data JPA**: Abstração da camada de persistência.
* **H2 Database / MySQL**: Banco de dados para armazenamento das tarefas.

### **Frontend**

* **Angular**
* **TypeScript**
* **RxJS**: Gerenciamento de fluxos de dados assíncronos e requisições HTTP.

---

## 🧠 Conceitos Abordados e Praticados

O desenvolvimento deste projeto cobriu pilares essenciais do desenvolvimento de software moderno:

* **Arquitetura RESTful:** Criação de endpoints estruturados para operações completas de CRUD (Create, Read, Update, Delete).
* **Injeção de Dependência:** Utilização de inversão de controle tanto no ecossistema Spring quanto nos Services do Angular.
* **Consumo de Serviços HTTP:** Integração do cliente Angular com a API utilizando o `HttpClientModule` para manipulação de dados em tempo real.
* **Mapeamento de Rotas e Componentização:** Organização de telas, fluxo de navegação e reutilização de componentes no ecossistema Angular.
* **Persistência de Dados:** Modelagem e relacionamento de entidades utilizando bancos relacionais.

---

## 📋 Funcionalidades

* [ ] **Criar Tarefas:** Adicionar novas pendências com título e descrição.
* [ ] **Listar Tarefas:** Visualização clara das tarefas pendentes e concluídas.
* [ ] **Atualizar Status:** Marcar e desmarcar tarefas como concluídas com um único clique.
* [ ] **Deletar Tarefas:** Remover itens de forma definitiva do banco de dados.

---

## 🏁 Como Executar o Projeto

### Pré-requisitos

* **Java JDK 21+** (o backend usa o Maven Wrapper — não é necessário instalar o Maven)
* **Node.js 20+** e **npm**

### Estrutura do repositório

```
backend/    → API REST em Spring Boot (Maven)
frontend/   → SPA em Angular
```

### Passo a Passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/task-flow.git
   cd task-flow
   ```

2. Suba o **backend** (porta 8080):
   ```bash
   cd backend
   ./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
   ```
   - API disponível em `http://localhost:8080/api/tasks`
   - Console do H2 em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:taskflow`, usuário `sa`, sem senha)

3. Em outro terminal, suba o **frontend** (porta 4200):
   ```bash
   cd frontend
   npm install
   npm start                     # equivale a: ng serve
   ```
   - Aplicação em `http://localhost:4200`

### Testes

```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm test
```

---

## 🌐 Endpoints da API

| Método | Rota              | Descrição                  | Sucesso |
|--------|-------------------|----------------------------|---------|
| GET    | `/api/tasks`      | Lista todas as tarefas     | 200     |
| GET    | `/api/tasks/{id}` | Busca uma tarefa pelo id   | 200     |
| POST   | `/api/tasks`      | Cria uma nova tarefa       | 201     |
| PUT    | `/api/tasks/{id}` | Atualiza título/descrição/status | 200 |
| DELETE | `/api/tasks/{id}` | Remove uma tarefa          | 204     |

Exemplo de corpo (POST/PUT):

```json
{ "title": "Estudar Spring Boot", "description": "Revisar JPA", "done": false }
```

---

## 🤖 Desenvolvimento com Claude Code

Este repositório já vem configurado para o [Claude Code](https://claude.com/claude-code).
A pasta `.claude/` contém agents, comandos (`/scaffold`, `/review`, `/deploy`), hooks,
skills, regras e workflows. O arquivo `CLAUDE.md` na raiz descreve o projeto e as
convenções que o assistente deve seguir.
