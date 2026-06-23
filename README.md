🚀 TaskFlow — Sistema de Gerenciamento de Tarefas
Este é um projeto full-stack desenvolvido para consolidar e demonstrar a integração prática entre um ecossistema backend em Java (Spring Boot) e um frontend dinâmico em Angular.

O objetivo principal é aplicar os conceitos fundamentais de uma arquitetura cliente-servidor através de um sistema clássico de gerenciamento de tarefas (To-Do List).

🛠️ Tecnologias Utilizadas
Backend
Java 17+

Spring Boot: Estruturação da API REST.

Spring Data JPA: Abstração da camada de persistência.

H2 Database / MySQL: Banco de dados para armazenamento das tarefas.

Frontend
Angular (versão recente)

TypeScript

RxJS: Gerenciamento de fluxos de dados assíncronos e requisições HTTP.

Tailwind CSS ou Bootstrap: Estilização da interface.

🧠 Conceitos Abordados e Praticados
O desenvolvimento deste projeto cobriu pilares essenciais do desenvolvimento de software moderno:

Arquitetura RESTful: Criação de endpoints estruturados para operações completas de CRUD (Create, Read, Update, Delete).

Injeção de Dependência: Utilização de inversão de controle tanto no ecossistema Spring quanto nos Services do Angular.

Consumo de Serviços HTTP: Integração do cliente Angular com a API utilizando o HttpClientModule para manipulação de dados em tempo real.

Mapeamento de Rotas e Componentização: Organização de telas, fluxo de navegação e reutilização de componentes no ecossistema Angular.

Persistência de Dados: Modelagem e relacionamento de entidades utilizando bancos relacionais.

📋 Funcionalidades
[ ] Criar Tarefas: Adicionar novas pendências com título e descrição.

[ ] Listar Tarefas: Visualização clara das tarefas pendentes e concluídas.

[ ] Atualizar Status: Marcar e desmarcar tarefas como concluídas com um único clique.

[ ] Deletar Tarefas: Remover itens de forma definitiva do banco de dados.

🏁 Como Executar o Projeto
(Nota: Complete esta seção com os comandos específicos assim que estruturar as pastas do seu repositório)

Prerrequisitos
Java JDK instalado

Node.js e Angular CLI instalados

Passo a Passo
Clone o repositório:

Bash
git clone https://github.com/seu-usuario/nome-do-repositorio.git
Executando o Backend:

Navegue até a pasta do backend e execute a aplicação via sua IDE ou pelo terminal:

Bash
./mvnw spring-run
Executando o Frontend:

Navegue até a pasta do frontend, instale as dependências e inicie o servidor:

Bash
npm install
ng serve
Abra o navegador em http://localhost:4200.
