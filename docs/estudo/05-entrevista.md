# 🎯 Módulo 5 — Preparação para a Entrevista de Estágio

> Objetivo: transformar o que você estudou em respostas seguras. Use o TaskFlow como seu exemplo concreto —
> falar de um projeto **que você construiu e entende** vale mais que qualquer teoria decorada.

## 5.1 Como usar o TaskFlow a seu favor

Quando perguntarem "você já fez algo full-stack?", você tem uma história pronta:

> *"Construí o TaskFlow, um gerenciador de tarefas com backend em Spring Boot e frontend em Angular. O backend
> expõe uma API REST com CRUD completo, organizada em camadas (controller, service, repository), com validação,
> tratamento global de erros e testes. O frontend consome essa API com HttpClient e exibe as tarefas com signals.
> Configurei CORS para os dois conversarem e validei o fluxo ponta a ponta."*

Isso demonstra: arquitetura, REST, camadas, testes, integração. **Tudo verdade e tudo seu.**

## 5.2 Perguntas de fundamentos (full-stack)

**P: O que é uma API REST?**
> Um conjunto de endpoints HTTP que expõem recursos. Cada recurso (ex.: tarefas) tem uma URL (`/api/tasks`) e
> usamos os verbos HTTP para as ações: GET para ler, POST para criar, PUT para atualizar, DELETE para remover.
> A comunicação geralmente é em JSON.

**P: Por que separar frontend e backend?**
> Segurança (credenciais do banco ficam só no servidor), reuso (o mesmo backend serve web e mobile) e
> modularidade (cada lado evolui e é testado separado).

**P: O que é JSON?**
> Um formato de texto para troca de dados, com pares chave-valor. É a "língua" que front e back falam na API.

## 5.3 Perguntas de Java / Spring

**P: O que é injeção de dependência? (a pergunta mais provável)**
> É o framework criar e "entregar" os objetos de que sua classe precisa, em vez de você dar `new`. No TaskFlow,
> o `TaskService` recebe um `TaskRepository` pelo construtor e o Spring injeta a instância. Vantagens: baixo
> acoplamento e testabilidade — nos testes eu injeto um mock no lugar do repositório real.

**P: Quais as camadas do seu backend?**
> Controller (recebe/responde HTTP), Service (regra de negócio), Repository (acesso ao banco) e Entity (mapeia a
> tabela). O controller é fino; a lógica fica no service.

**P: O que `@SpringBootApplication` faz?**
> Liga a auto-configuração do Spring e combina `@EnableAutoConfiguration`, `@ComponentScan` e `@Configuration`.
> Fica na raiz do pacote porque o scan varre dali pra baixo.

**P: Diferença entre JPA e Hibernate?**
> JPA é a especificação (o padrão de mapeamento objeto-relacional); Hibernate é a implementação que gera o SQL.

**P: O que é um DTO e por que usar?**
> Um objeto que transporta dados na API, separado da entidade. Uso para não expor a tabela direto: controlo os
> campos (segurança), desacoplo a API do banco (estabilidade) e coloco a validação no DTO de entrada.

**P: Como você trata erros na API?**
> Com um `@RestControllerAdvice` global que captura exceções e devolve `ProblemDetail` com o status certo
> (404 para não encontrado, 400 para validação). Assim não vazo stack trace para o cliente.

**P: O que faz o Spring Data JPA com uma interface vazia que estende `JpaRepository`?**
> Gera a implementação em tempo de execução com todo o CRUD (save, findById, findAll, delete…). Posso ainda
> criar derived queries pelo nome do método, tipo `findByDone(boolean)`.

## 5.4 Perguntas de Angular / TypeScript

**P: Diferença entre componente e service?**
> Componente cuida da tela e da interação; service cuida dos dados e do HTTP. O componente delega ao service
> e nunca chama HTTP direto.

**P: O que é um Observable e como você consome?**
> É um fluxo assíncrono de dados (RxJS). `http.get` retorna um Observable porque a resposta da rede demora;
> eu assino com `subscribe` e trato `next` (dados) e `error`. Sem subscribe, a requisição nem dispara.

**P: O que é um signal?**
> A forma moderna de estado reativo no Angular. Quando o valor muda, a tela re-renderiza sozinha. Leio com
> `tasks()` e escrevo com `set`/`update`.

**P: Quais os tipos de data binding?**
> Interpolação `{{ }}`, property binding `[ ]`, event binding `( )` e two-way `[(ngModel)]`.

**P: TypeScript vs JavaScript?**
> TypeScript é JavaScript com tipagem estática. Pega erros em tempo de compilação e melhora autocomplete e
> legibilidade. O navegador não executa TS direto — é compilado para JS.

## 5.5 Perguntas de integração

**P: O que é CORS?**
> Uma política de segurança do navegador que bloqueia chamadas entre origens diferentes. Como meu front (4200)
> e back (8080) têm origens distintas, liberei a origem do front no backend (`CorsConfig`).

**P: Quais status HTTP você conhece?**
> 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401/403 (auth), 404 Not Found, 500 Internal Error.
> Regra: 2xx sucesso, 4xx erro do cliente, 5xx erro do servidor.

**P: A tela não atualiza após criar um item. Como investiga?**
> Testo a API com curl; depois DevTools → Network para ver se a requisição saiu e o status; Console para erros
> JS; e o terminal do Spring para stack trace se for 500. Isolo se é front, rede ou back.

## 5.6 Perguntas comportamentais (não esqueça destas!)

Estágio avalia **atitude** tanto quanto técnica. Prepare exemplos curtos (formato **STAR**: Situação, Tarefa,
Ação, Resultado):

- *"Conte sobre um problema técnico que você resolveu."* → Use um bug real do TaskFlow (ex.: erro de CORS, ou o
  ajuste de versão do Spring Boot 4 que mudou o nome dos pacotes de teste).
- *"Como você aprende algo novo?"* → Seja honesto: documentação oficial, construir um projeto pequeno, ler código.
- *"O que faz quando trava num problema?"* → Isolar, ler a mensagem de erro, pesquisar, e pedir ajuda depois de
  ter tentado e ter contexto pra mostrar.

**Dicas de ouro:**
1. **"Não sei, mas..."** é uma ótima resposta: *"Não sei de cabeça, mas eu procuraria na documentação e testaria X."*
   Honestidade + método > fingir.
2. Sempre que possível, **conecte a resposta ao TaskFlow** ("no meu projeto eu fiz assim...").
3. Pergunte de volta no fim ("como é o dia a dia do estágio?", "que stack o time usa?") — mostra interesse.

## 5.7 Checklist final (revise na véspera)

Backend:
- [ ] Sei explicar as 4 camadas e por que separar
- [ ] Sei explicar injeção de dependência por construtor
- [ ] Sei o que `@RestController`, `@Service`, `@Entity`, `@RequestBody`, `@Valid` fazem
- [ ] Sei a diferença JPA × Hibernate e o papel do DTO
- [ ] Sei os verbos HTTP e status codes principais

Frontend:
- [ ] Sei a diferença componente × service
- [ ] Sei o que é Observable e por que dar `subscribe`
- [ ] Sei o que é signal e os tipos de data binding
- [ ] Sei por que TypeScript em vez de JS puro

Integração:
- [ ] Sei explicar CORS e onde se resolve
- [ ] Tenho um roteiro de debug (front/rede/back)
- [ ] Sei contar a história do TaskFlow em 30 segundos

Comportamental:
- [ ] Tenho 2-3 histórias no formato STAR
- [ ] Tenho perguntas para fazer ao entrevistador

---

**Boa sorte! 🚀** Você construiu um projeto full-stack real, com testes e revisão de código — isso já te coloca
à frente de muita gente que só viu tutorial. Fale com confiança do que é seu.
