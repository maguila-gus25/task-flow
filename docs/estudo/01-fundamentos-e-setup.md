# 📘 Módulo 1 — Fundamentos e Setup

> Objetivo: entender **como as peças se conectam** e o que cada projeto contém.

## 1.1 O modelo mental: cliente ↔ servidor

Full-stack são **dois programas separados** rodando ao mesmo tempo, que conversam por **HTTP**:

```
┌─────────────────┐         HTTP (JSON)         ┌──────────────────────┐
│   FRONTEND       │  ──── GET /api/tasks ────►  │   BACKEND            │
│   Angular        │                             │   Spring Boot        │
│   (navegador)    │  ◄─── [{id,title,...}] ───  │   (porta 8080)       │
│   porta 4200     │                             │          │ SQL       │
└─────────────────┘                             └──────────┼───────────┘
                                                            ▼
                                                    ┌───────────────┐
                                                    │  Banco (H2)   │
                                                    └───────────────┘
```

- O **frontend** é o que o usuário vê. Ele **não acessa o banco** — pede dados ao backend.
- O **backend** é o "cérebro": recebe pedidos, aplica regras, fala com o banco, devolve **JSON**.
- São independentes: portas diferentes (4200 e 8080), linguagens diferentes (TypeScript e Java).

**Por que separar?**
1. **Segurança** — as credenciais do banco ficam só no backend, nunca chegam ao navegador.
2. **Reuso** — o mesmo backend serve web, mobile e parceiros externos, todos por HTTP.
3. **Modularidade** — cada lado evolui e é testado no seu próprio ritmo.

## 1.2 O fluxo de uma requisição no TaskFlow

Ao marcar uma tarefa como concluída, isto acontece (arquivos reais do projeto):

```
1. UI: checkbox dispara toggle(task)          frontend/src/app/task/task-list/task-list.ts
2. Service Angular faz PUT /api/tasks/1        frontend/src/app/task/task.service.ts
        │  viaja pela rede (HTTP) ▼
3. Controller recebe o PUT                     backend/src/main/java/com/taskflow/task/TaskController.java
4. Service aplica a regra de negócio           backend/src/main/java/com/taskflow/task/TaskService.java
5. Repository salva no banco                   backend/src/main/java/com/taskflow/task/TaskRepository.java
6. Resposta JSON volta o mesmo caminho até a tela
```

**Memorize esse desenho.** Todo o resto do guia é detalhar uma dessas 6 caixas.

## 1.3 Setup do BACKEND (Java + Spring Boot)

Peças essenciais na pasta `backend/`:

| Arquivo | O que é |
|---------|---------|
| `pom.xml` | Manifesto do projeto Java: **dependências** + como construir. Equivalente ao `package.json`. |
| `mvnw` / `mvnw.cmd` | **Maven Wrapper** — roda o Maven sem instalá-lo. Por isso usamos `./mvnw`. |
| `src/main/java/...` | O código-fonte Java. |
| `src/main/resources/application.properties` | Configuração (banco, porta, CORS). |
| `src/test/java/...` | Os testes. |

**O ponto de partida** — `backend/src/main/java/com/taskflow/TaskflowApplication.java`:

```java
@SpringBootApplication               // ① liga a auto-configuração do Spring
public class TaskflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskflowApplication.class, args);  // ② sobe o servidor web
    }
}
```

- **①** `@SpringBootApplication` combina 3 anotações: `@EnableAutoConfiguration` (configura tudo sozinho),
  `@ComponentScan` (varre o pacote procurando seus `@Service`, `@RestController`…) e `@Configuration`.
  Importante: ela fica na **raiz do pacote** (`com.taskflow`) porque o scan varre dali pra baixo.
- **②** `SpringApplication.run` sobe um servidor **Tomcat embutido** dentro do `.jar`. Por isso você não
  instala servidor separado — `./mvnw spring-boot:run` já põe a API no ar na 8080.

**Maven** = ferramenta de **build**: gerencia dependências **e** compila, testa e empacota (`compile → test → package`).

**Rodar:** `cd backend && ./mvnw spring-boot:run` → `http://localhost:8080/api/tasks`.

## 1.4 Setup do FRONTEND (Angular + TypeScript)

Peças essenciais na pasta `frontend/`:

| Arquivo | O que é |
|---------|---------|
| `package.json` | Manifesto do projeto JS: dependências + **scripts** (`npm start`, `npm test`). |
| `angular.json` | Configuração do build do Angular. |
| `src/main.ts` | Ponto de partida do app. |
| `src/app/` | Onde a aplicação vive (componentes, services). |
| `src/environments/environment.ts` | Configuração de ambiente (ex.: URL da API). |

**O ponto de partida** — `frontend/src/main.ts`:

```ts
bootstrapApplication(App, appConfig)   // "ligue o app começando pelo componente App"
```

**Serviços globais** — `frontend/src/app/app.config.ts`: foi aqui que adicionamos `provideHttpClient()`
para poder fazer requisições. É o paralelo da injeção de dependência do Spring, no Angular.

**A unidade básica é o componente** — um trio que sempre anda junto:
- `task-list.ts` → **lógica** (classe TypeScript)
- `task-list.html` → **template** (o que aparece)
- `task-list.scss` → **estilo** (CSS com superpoderes — variáveis, aninhamento)

**TypeScript** = JavaScript **com tipos** (`id: number`, `title: string`). O navegador não entende TS direto —
o Angular **compila** TS → JS (é o que `npm run build` faz). *npm* baixa libs e roda scripts; o **build** em si é
feito pelo **Angular CLI**.

**Rodar:** `cd frontend && npm install && npm start` → `http://localhost:4200`.

---

## 🏋️ Exercícios — Módulo 1

1. Explique, em 2-3 frases, por que o frontend não acessa o banco diretamente.
2. O que a anotação `@SpringBootApplication` faz e por que ela fica na raiz do pacote?
3. Qual a diferença entre **Maven** e **npm**? (cuidado com a armadilha)
4. Quais os 3 arquivos que formam um componente Angular e qual o papel de cada um?
5. Em que portas rodam backend e frontend, e por que precisam ser diferentes?
6. (Aplicação) Desenhe de memória o caminho de um `GET /api/tasks`, citando as 6 etapas.

---

## ✅ Gabarito — Módulo 1

**1.** Por **segurança** (as credenciais do banco ficam só no servidor, nunca no navegador),
**reuso** (o mesmo backend atende web/mobile/terceiros via HTTP) e **modularidade** (cada lado evolui
e é testado separadamente). O front pede dados ao backend por HTTP; quem fala com o banco é só o backend.

**2.** Ela liga a **auto-configuração** do Spring e combina `@EnableAutoConfiguration` + `@ComponentScan` +
`@Configuration`. Fica na **raiz do pacote** porque o `@ComponentScan` varre a partir do pacote dela **pra baixo** —
se estivesse num subpacote, não enxergaria as classes acima.

**3.** Ambos gerenciam dependências, mas **Maven é uma ferramenta de build completa** (baixa libs **e** compila,
testa e empacota o `.jar`), enquanto **npm é gerenciador de pacotes + executor de scripts** — o build do Angular
em si é feito pelo **Angular CLI**, não pelo npm. Maven é do ecossistema Java; npm, do JavaScript.

**4.** `.ts` (lógica/classe), `.html` (template/o que aparece na tela) e `.scss`/`.css` (estilo).

**5.** Backend na **8080**, frontend na **4200**. São **dois processos/servidores separados** e dois programas
não podem ocupar a mesma porta na mesma máquina. Como ficam em "origens" diferentes, é por isso que precisamos
configurar **CORS** (Módulo 4).

**6.** `GET /api/tasks`:
1. componente chama `service.list()` → 2. `TaskService` (Angular) faz `http.get('/api/tasks')` →
3. `TaskController.list()` recebe → 4. `TaskService.findAll()` (Java) aplica a lógica →
5. `TaskRepository.findAll()` busca no banco → 6. lista vira JSON e volta até o `signal` que a tela exibe.
