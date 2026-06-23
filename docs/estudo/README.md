# 📚 Guia de Estudo Full-Stack — Angular + Java/Spring

Material de estudo para a vaga de estágio full-stack, construído em cima do **TaskFlow**
(o projeto real deste repositório). A vantagem: cada conceito aponta para um arquivo que
você pode abrir e ver funcionando.

## Como usar este guia

1. Leia os módulos **na ordem** — cada um assume o anterior.
2. Em cada módulo, abra os arquivos citados no código (caminhos como `backend/src/.../Task.java`).
3. No fim de cada módulo há **exercícios** e o **gabarito**. Tente responder **antes** de olhar a correção.
4. Termine pelo Módulo 5 (entrevista) — ele junta tudo em perguntas e respostas-modelo.

## Índice

| # | Arquivo                                                 | Tema                                                              |
| - | ------------------------------------------------------- | ----------------------------------------------------------------- |
| 1 | [01-fundamentos-e-setup.md](01-fundamentos-e-setup.md)     | Arquitetura cliente-servidor, o que cada projeto contém          |
| 2 | [02-backend-java-spring.md](02-backend-java-spring.md)     | Camadas, anotações, JPA, REST — uma requisição linha a linha |
| 3 | [03-frontend-angular.md](03-frontend-angular.md)           | Componentes, services, HttpClient, signals, data binding          |
| 4 | [04-integracao-full-stack.md](04-integracao-full-stack.md) | Como front e back conversam: CORS, contrato, debug                |
| 5 | [05-entrevista.md](05-entrevista.md)                       | Perguntas frequentes + respostas-modelo + dicas                   |

## Glossário-relâmpago

Termos que você vai ver o tempo todo:

- **API REST** — conjunto de endpoints HTTP que expõem dados/ações (ex.: `GET /api/tasks`).
- **Endpoint** — uma URL + método HTTP que faz uma coisa (ex.: `POST /api/tasks` cria tarefa).
- **JSON** — formato de texto para troca de dados entre front e back: `{"title":"Estudar"}`.
- **DTO** (Data Transfer Object) — objeto usado só para transportar dados na API.
- **Entity** — classe Java que representa uma tabela do banco.
- **Injeção de Dependência (DI)** — o framework cria e "entrega" os objetos pra você.
- **CRUD** — Create, Read, Update, Delete (as 4 operações básicas).
- **Bean** — no Spring, qualquer objeto gerenciado pelo framework.
- **Observable** — no Angular/RxJS, um "fluxo" de dados que chega ao longo do tempo (ex.: a resposta de um HTTP).

## Como rodar o projeto (para experimentar)

```bash
# Backend (porta 8080)
cd backend && ./mvnw spring-boot:run

# Frontend (porta 4200) — em outro terminal
cd frontend && npm install && npm start
```
