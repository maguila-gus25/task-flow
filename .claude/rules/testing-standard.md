# Regra: Padrão de testes

Toda mudança de comportamento exige teste. Cobertura mínima recomendada: **services e controllers do backend**.

## Princípios

- Padrão **AAA**: Arrange, Act, Assert.
- Um conceito por teste; nome descritivo do cenário.
- Testes independentes e determinísticos (sem depender de ordem, rede externa ou relógio real).
- Cubra três frentes: **caminho feliz**, **borda** (vazio, limites) e **erro** (input inválido, recurso ausente).

## Backend — JUnit 5 + Mockito

- **Service:** unitário, mockando o repository.
  ```java
  @ExtendWith(MockitoExtension.class)
  class TaskServiceTest {
      @Mock TaskRepository repository;
      @InjectMocks TaskService service;
  }
  ```
- **Controller:** `@WebMvcTest` + `MockMvc`, validando status, JSON e validação de input.
- **Repository (query custom):** `@DataJpaTest`.
- Nomes: `metodo_condicao_resultadoEsperado` → `findById_idInexistente_lancaNotFound`.
- Rodar: `mvn test`.

## Frontend — Jasmine + Karma

- **Service:** `HttpClientTestingModule` + `HttpTestingController`; verifique URL, método HTTP e corpo.
  ```ts
  it('deve buscar tarefas via GET /api/tasks', () => { /* ... */ });
  ```
- **Component:** `TestBed` com services mockados; teste render e interações do usuário.
- Rodar: `ng test --watch=false --browsers=ChromeHeadless`.

## Antes de abrir PR

- Toda a suíte verde (`mvn test` e `ng test`).
- Sem testes ignorados (`@Disabled` / `xit`) sem justificativa no PR.
