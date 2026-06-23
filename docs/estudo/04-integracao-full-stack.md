# 🔗 Módulo 4 — Integração Full-Stack

> Objetivo: entender como front e back **conversam de verdade** e como debugar quando não conversam.

## 4.1 O contrato REST: o ponto de acoplamento

Front e back são independentes, mas precisam **concordar** no formato da conversa. Esse acordo é o **contrato REST**.
No TaskFlow ele está documentado em `.claude/rules/api-conventions.md` e implementado dos dois lados:

| Operação | Método + Rota | Backend (Java) | Frontend (Angular) |
|----------|---------------|----------------|--------------------|
| Listar | `GET /api/tasks` | `TaskController.list()` | `TaskService.list()` |
| Buscar | `GET /api/tasks/{id}` | `getById()` | `getById()` |
| Criar | `POST /api/tasks` | `create()` → 201 | `create()` |
| Atualizar | `PUT /api/tasks/{id}` | `update()` | `update()` |
| Remover | `DELETE /api/tasks/{id}` | `delete()` → 204 | `delete()` |

**Regra de ouro:** se você muda o contrato de um lado (ex.: adiciona um campo no DTO), **tem que atualizar o
outro lado**. O `TaskResponse` (Java) e a interface `Task` (TypeScript) precisam espelhar-se.

## 4.2 Verbos HTTP e status codes (decore)

**Verbos** (a "ação"):
- `GET` — ler (não muda nada)
- `POST` — criar
- `PUT` — atualizar por completo
- `PATCH` — atualizar parcialmente
- `DELETE` — remover

**Status codes** (o "resultado") — os que mais caem:
- `200 OK` — deu certo (GET/PUT)
- `201 Created` — criou (POST)
- `204 No Content` — deu certo, sem corpo (DELETE)
- `400 Bad Request` — erro do **cliente** (input inválido)
- `401 / 403` — não autenticado / sem permissão
- `404 Not Found` — recurso não existe
- `500 Internal Server Error` — erro do **servidor**

Mnemônico: **2xx** sucesso, **4xx** culpa do cliente, **5xx** culpa do servidor.

## 4.3 CORS: por que existe e como resolvemos

Lembre: front (4200) e back (8080) são **origens diferentes**. Por padrão, o navegador **bloqueia** uma página
de uma origem de chamar outra origem — é a política de segurança **Same-Origin / CORS**
(Cross-Origin Resource Sharing). Sem configurar, você veria no console:

```
Access to fetch at 'http://localhost:8080/api/tasks' from origin 'http://localhost:4200'
has been blocked by CORS policy
```

A solução fica **no backend**, dizendo "confio nesta origem" — `backend/src/main/java/com/taskflow/config/CorsConfig.java`:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")   // confia no frontend
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
    }
}
```

> Detalhe de entrevista: **CORS é uma proteção do navegador**, não do servidor. Por isso o `curl` (que não é
> navegador) chama a API sem problema, mas o Angular precisa do CORS liberado.

## 4.4 Ambientes: a URL da API não fica chumbada

`frontend/src/environments/environment.ts`:

```ts
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080/api',
};
```

Em produção, a URL do backend muda. Centralizar em `environment.ts` permite trocar **num lugar só**. O mesmo
vale no backend: segredos e URL do banco vêm de **variáveis de ambiente** (perfil `prod`), nunca chumbados no código.

## 4.5 Como debugar quando "não funciona"

Roteiro mental (vale ouro numa entrevista — mostra que você sabe investigar):

1. **O backend está no ar?** Teste direto, sem o front:
   ```bash
   curl http://localhost:8080/api/tasks
   ```
   Se isso falha, o problema é no **backend** (não subiu, erro de banco…). Olhe o log do terminal.

2. **O front chega no back?** Abra o **DevTools do navegador → aba Network**. Veja a requisição:
   - Não aparece nada → erro no **frontend** (URL errada, service não chamado).
   - Aparece com erro **CORS** → falta liberar a origem no backend.
   - Status **404** → rota errada (confira a URL e o `@RequestMapping`).
   - Status **400** → input inválido (veja a mensagem do `ProblemDetail` na resposta).
   - Status **500** → erro no backend (olhe o **stack trace no terminal do Spring**).

3. **Os dados estão certos?** Veja a aba **Console** (erros JS) e o **payload** da requisição na Network.

> A habilidade nº 1 de um dev júnior não é decorar — é **isolar o problema**: front? rede? back? banco?

## 4.6 O ciclo completo, ponta a ponta

Criar uma tarefa, do clique ao banco e de volta:

```
[Navegador]  usuário digita e clica "Adicionar"
   └─ TaskList.add()                          task-list.ts
        └─ TaskService.create(req)            task.service.ts
             └─ http.post('/api/tasks', body) ──HTTP──┐
                                                       ▼
[Spring]  TaskController.create(@Valid @RequestBody)   TaskController.java
   └─ @Valid checa o DTO (400 se inválido)
        └─ TaskService.create()                        TaskService.java
             └─ TaskRepository.save()                  → INSERT no banco (Hibernate)
   └─ devolve 201 + JSON ──HTTP──┐
                                  ▼
[Navegador]  next: tasks.update(lista => [...lista, novaTask])
   └─ signal muda → Angular re-renderiza a lista com a tarefa nova
```

---

## 🏋️ Exercícios — Módulo 4

1. O que é o "contrato REST" e o que acontece se você muda um lado e esquece o outro?
2. Qual verbo e qual status você espera para: criar, remover e buscar algo que não existe?
3. O que é CORS, por que ele existe e em qual lado (front ou back) se resolve?
4. Por que `curl` consegue chamar a API mas o Angular dá erro de CORS?
5. Você clica em "Adicionar" e nada aparece. Descreva seu roteiro de debug.
6. Por que a URL da API fica em `environment.ts` e não chumbada no service?

---

## ✅ Gabarito — Módulo 4

**1.** É o **acordo de formato** entre front e back: rotas, verbos, status e o formato do JSON (DTOs). Se você
muda um lado (ex.: renomeia um campo) e esquece o outro, a integração **quebra** — o front manda/lê um formato
que o back não entende (erros de parsing, campos `undefined`, 400).

**2.** Criar → `POST` + **201 Created**. Remover → `DELETE` + **204 No Content**. Buscar algo inexistente →
`GET` + **404 Not Found**.

**3.** CORS (Cross-Origin Resource Sharing) é a política do **navegador** que bloqueia uma página de uma origem
de chamar outra origem. Existe por **segurança**. Resolve-se no **backend**, declarando quais origens são
confiáveis (no TaskFlow, `http://localhost:4200` em `CorsConfig`).

**4.** Porque **CORS é uma restrição do navegador**, não do servidor. `curl` não é navegador → não aplica a
política. O Angular roda **dentro do navegador** → está sujeito a ela.

**5.** (a) `curl http://localhost:8080/api/tasks` para ver se o back responde; (b) DevTools → Network para ver se
a requisição saiu e qual o status (CORS? 404? 400? 500?); (c) Console para erros de JS; (d) terminal do Spring
para stack trace se for 500. Isolar a camada: front, rede ou back.

**6.** Para **centralizar** a configuração: a URL muda entre dev e produção, e mantê-la num único arquivo evita
caçar URLs pelo código. É o mesmo princípio de não chumbar segredos no backend (usar variáveis de ambiente).
