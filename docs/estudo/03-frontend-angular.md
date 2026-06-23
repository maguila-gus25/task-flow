# 📙 Módulo 3 — Frontend (Angular + TypeScript)

> Objetivo: entender como uma tela é montada e como ela consome a API.

## 3.1 TypeScript em 3 minutos

TypeScript = JavaScript + **tipos**. Os tipos pegam erros antes de rodar e documentam o código.

```ts
let nome: string = "Ana";
let idade: number = 20;
let ativo: boolean = true;

// Interface: o "formato" de um objeto (não gera código, só checa tipos)
interface Task {
  id: number;
  title: string;
  description?: string;   // o "?" = opcional (pode não vir)
  done: boolean;
}
```

No TaskFlow: `frontend/src/app/task/task.model.ts` define as interfaces `Task`, `CreateTaskRequest` e
`UpdateTaskRequest` — elas **espelham os DTOs do backend**. Esse espelhamento é o "contrato" entre os dois lados.

## 3.2 Componente: a unidade básica

Um componente controla um pedaço da tela. No TaskFlow, `TaskList` é a tela principal.

`frontend/src/app/task/task-list/task-list.ts`:

```ts
@Component({                               // ① decorator que define o componente
  selector: 'app-task-list',               // ② como usá-lo no HTML: <app-task-list />
  imports: [FormsModule],                  // ③ o que este componente usa (standalone)
  templateUrl: './task-list.html',         // ④ o template
  styleUrl: './task-list.scss',            // ⑤ o estilo
})
export class TaskList implements OnInit {
  private readonly service = inject(TaskService);   // ⑥ injeção de dependência

  readonly tasks = signal<Task[]>([]);     // ⑦ estado reativo (signal)
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  ngOnInit(): void { this.load(); }        // ⑧ roda quando o componente aparece
}
```

- **②** `selector` é o nome da "tag" HTML do componente.
- **③** Angular moderno usa **standalone components**: cada componente declara o que importa, sem `NgModule`.
- **⑥** `inject(TaskService)` é a **injeção de dependência** do Angular (mesmo conceito do Spring): o framework
  entrega o service pronto. Você não dá `new TaskService()`.
- **⑦ Signals** são a forma moderna de guardar **estado reativo**: quando o valor muda, a tela **re-renderiza
  sozinha**. Lê-se com `tasks()` (com parênteses) e escreve-se com `tasks.set(...)` ou `tasks.update(...)`.
- **⑧** `ngOnInit` é um **lifecycle hook** — código que roda quando o componente é criado. Ideal para carregar dados.

## 3.3 Service: onde mora a comunicação com a API

A regra do Angular: **componente cuida da tela; service cuida dos dados/HTTP.** Nunca chame HTTP direto no componente.

`frontend/src/app/task/task.service.ts`:

```ts
@Injectable({ providedIn: 'root' })        // ① service único na app inteira (singleton)
export class TaskService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/tasks`;  // http://localhost:8080/api/tasks

  list(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl);            // GET
  }
  create(request: CreateTaskRequest): Observable<Task> {
    return this.http.post<Task>(this.baseUrl, request);    // POST + corpo
  }
  update(id: number, request: UpdateTaskRequest): Observable<Task> {
    return this.http.put<Task>(`${this.baseUrl}/${id}`, request);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
```

- **①** `providedIn: 'root'` cria **uma única instância** (singleton) compartilhada por toda a app.
- `HttpClient` é o cliente HTTP do Angular. `http.get<Task[]>(...)` diz "esperar um array de `Task`".
- A URL base vem de `environment.ts` — **centralizar a URL** é boa prática (muda só num lugar).

## 3.4 Observable: o "fluxo" que chega depois

`http.get(...)` **não retorna os dados na hora** — a rede leva tempo. Ele retorna um **Observable**: uma
"promessa de fluxo" que você precisa **assinar** (`subscribe`) para receber o resultado quando chegar.

`frontend/src/app/task/task-list/task-list.ts`:

```ts
load(): void {
  this.loading.set(true);
  this.error.set(null);
  this.service.list().subscribe({              // assina o Observable
    next: (tasks) => {                          // ✅ chegou: atualiza o estado
      this.tasks.set(tasks);
      this.loading.set(false);
    },
    error: () => {                              // ❌ deu erro: mostra mensagem
      this.error.set('Não foi possível carregar as tarefas.');
      this.loading.set(false);
    },
  });
}
```

- `next` recebe os dados quando chegam; `error` trata falhas. **Nada acontece sem `subscribe`** — o Observable é "preguiçoso".
- Observable (RxJS) ≈ Promise, mas pode emitir **vários valores ao longo do tempo** e tem operadores poderosos
  (`map`, `filter`, `switchMap`…). Para HTTP simples, ele emite um valor e completa.

## 3.5 Data binding: ligando a classe ao HTML

Os 4 tipos de binding (pergunta clássica de entrevista Angular):

| Sintaxe | Tipo | Direção | Exemplo |
|---------|------|---------|---------|
| `{{ valor }}` | Interpolação | classe → tela | `{{ task.title }}` |
| `[propriedade]` | Property binding | classe → tela | `[checked]="task.done"` |
| `(evento)` | Event binding | tela → classe | `(click)="remove(task)"` |
| `[(ngModel)]` | Two-way binding | ↔ ambos | `[(ngModel)]="newTitle"` |

`frontend/src/app/task/task-list/task-list.html`:

```html
<form (ngSubmit)="add()">                         <!-- evento: submit chama add() -->
  <input [(ngModel)]="newTitle" name="title" />   <!-- two-way: input ↔ campo newTitle -->
  <button [disabled]="!newTitle.trim()">Adicionar</button>  <!-- property binding -->
</form>

@if (loading()) {                                 <!-- control flow novo do Angular -->
  <p>Carregando...</p>
} @else if (tasks().length === 0) {
  <p>Nenhuma tarefa ainda.</p>
}

<ul>
  @for (task of tasks(); track task.id) {         <!-- loop com track (performance) -->
    <li [class.done]="task.done">
      <input type="checkbox" [checked]="task.done" (change)="toggle(task)" />
      <span>{{ task.title }}</span>
      <button (click)="remove(task)">🗑️</button>
    </li>
  }
</ul>
```

- `@if` / `@for` / `@else` é o **control flow** moderno do Angular (substitui `*ngIf` / `*ngFor`).
- `track task.id` ajuda o Angular a saber qual item mudou (performance em listas).
- `(ngModel)` exige importar `FormsModule` (veja em `imports` do componente).

## 3.6 Atualizando a tela ao mudar dados

Veja `add()` em `task-list.ts`: ao criar uma tarefa, em vez de recarregar tudo do servidor, ele atualiza o
signal localmente:

```ts
this.tasks.update((list) => [...list, task]);   // adiciona a nova tarefa à lista atual
```

`signal.update()` recebe a lista atual e devolve a nova. Como é um signal, a tela re-renderiza sozinha.
(O mesmo padrão aparece em `toggle` com `map` e em `remove` com `filter`.)

## 3.7 Roteamento (visão rápida)

`frontend/src/app/app.routes.ts` liga uma URL a um componente:

```ts
export const routes: Routes = [
  { path: '', component: TaskList },   // a raiz "/" mostra a TaskList
];
```

O `<router-outlet />` em `app.html` é o "buraco" onde o componente da rota atual é renderizado.

---

## 🏋️ Exercícios — Módulo 3

1. Qual a diferença de responsabilidade entre um **componente** e um **service** no Angular?
2. O que é um **signal** e qual a vantagem dele para a tela?
3. O que é um **Observable** e por que `http.get(...)` não devolve os dados na hora?
4. Cite os 4 tipos de data binding e a direção de cada um.
5. Por que `inject(TaskService)` é melhor do que `new TaskService()`?
6. O que `track task.id` faz no `@for` e por que importa?
7. (Aplicação) Você quer um botão "Recarregar" que rebusca as tarefas do servidor.
   O que adiciona no HTML e no `.ts`?

---

## ✅ Gabarito — Módulo 3

**1.** O **componente** cuida da **tela** (o que aparece e a interação do usuário). O **service** cuida dos
**dados e da comunicação HTTP**. Componente nunca deve chamar HTTP direto — delega ao service. Isso mantém o
componente simples e o service reutilizável/testável.

**2.** Um **signal** é um contêiner de **estado reativo**: ao mudar seu valor (`set`/`update`), o Angular
**re-renderiza** automaticamente as partes da tela que o usam. Lê-se com `()` (ex.: `tasks()`).

**3.** Um **Observable** é um fluxo de dados assíncrono. `http.get` não devolve na hora porque a resposta da rede
**leva tempo**; ele devolve um Observable que você **assina** com `subscribe`, e o callback `next` recebe os dados
**quando chegam**. Sem `subscribe`, a requisição nem dispara (é "preguiçoso").

**4.** Interpolação `{{ }}` (classe→tela), property binding `[ ]` (classe→tela), event binding `( )` (tela→classe),
two-way `[( )]` (ambos).

**5.** Porque é **injeção de dependência**: o Angular entrega uma instância **singleton** já configurada (com o
`HttpClient` dentro). Dar `new` criaria um objeto solto, sem as dependências resolvidas, e quebraria a
testabilidade e o compartilhamento de estado.

**6.** `track task.id` diz ao Angular **como identificar cada item** da lista. Assim, ao mudar a lista, ele
re-renderiza só o que mudou em vez de recriar tudo — ganho de performance e preserva estado do DOM.

**7.** No HTML: `<button (click)="load()">Recarregar</button>`. No `.ts`: **nada novo** — o método `load()` já
existe e já faz `service.list().subscribe(...)`. (Reaproveitar `load()` é o ideal.)
