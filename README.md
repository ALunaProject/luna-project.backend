# 🚀 Luna Project - BackEnd

Bem-vindo ao **Luna API**, o coração do Luna Project! Esta API RESTful fornece todos os endpoints necessários para o funcionamento do site, incluindo autenticação, gerenciamento de usuários, jogos, posts, comentários e listas de jogos. Desenvolvida com **Spring Boot**, segue os princípios de uma arquitetura limpa e escalável.

_Esta versão é uma MVP (Minimum Viable Product) do que será o projeto futuramente._
_Desenvolvido por Team Luna - disponível no [Figma Community](https://www.figma.com/community/file/1559988919515428165)._

---

## 📁 Estrutura do Projeto

A estrutura do projeto é organizada em camadas para facilitar a manutenção, testes e evolução da aplicação, como exemplificado abaixo:

```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── lunaproject/
    │           └── api/                      # Pacote raiz da aplicação
    │               │
    │               ├── LunaApplication.java   # Classe principal da aplicação Spring Boot
    │               │
    │               ├── config/               # Configurações gerais da aplicação
    │               │   # Beans globais (ModelMapper, RestTemplate, ObjectMapper)
    │               │   # Configuração do Swagger/OpenAPI para documentação da API
    │               │   # Configuração de CORS para permitir requisições do front-end
    │               │   # Configurações específicas do JPA/Hibernate
    │               │   # Interceptadores e conversores HTTP
    │               │
    │               ├── security/             # Autenticação e Autorização
    │               │   # Filtro de autenticação JWT (valida tokens nas requisições)
    │               │   # Provedor de tokens (geração, validação e extração de informações)
    │               │   # Tratamento de erros de autenticação (respostas 401)
    │               │   # Implementação do UserDetailsService (carrega dados do usuário)
    │               │   # Configuração da cadeia de filtros do Spring Security
    │               │   # Definição de endpoints públicos e protegidos
    │               │
    │               ├── domain/               # Camada de domínio (entidades e acesso a dados)
    │               │   ├── entity/           # Entidades JPA que mapeiam as tabelas do banco
    │               │   │   # Classes com anotações JPA (@Entity, @Table, @Column)
    │               │   │   # Relacionamentos (@ManyToOne, @OneToMany, @ManyToMany)
    │               │   │   # Representam as tabelas: user, games, post, comments, gamelist
    │               │   │
    │               │   ├── enums/            # Enumeradores para valores fixos e imutáveis
    │               │   │   # Roles de usuário (ADMIN, USER, MODERATOR)
    │               │   │   # Status de posts (DRAFT, PUBLISHED, ARCHIVED)
    │               │   │   # Gêneros de jogos (ACTION, RPG, STRATEGY, etc.)
    │               │   │   # Status da lista de jogos (FAVORITE, WISHLIST, PLAYING)
    │               │   │
    │               │   └── repository/       # Interfaces Spring Data JPA para acesso ao banco
    │               │       # Métodos CRUD padrão (save, findById, findAll, delete)
    │               │       # Queries customizadas com @Query ou métodos nomeados
    │               │       # Exemplos: findByEmail, findByAuthor, findLatest
    │               │
    │               ├── application/          # Camada de aplicação (orquestração e regras de negócio)
    │               │   ├── dto/              # Data Transfer Objects (entrada e saída de dados)
    │               │   │   # POJOs com anotações @Data, @Builder, @Valid
    │               │   │   # Definem o contrato da API (request/response)
    │               │   │   # Isolam as entidades do mundo externo
    │               │   │   # Agrupados por funcionalidade: auth, user, game, post, comment
    │               │   │
    │               │   ├── mapper/           # Mapeadores Entity ↔ DTO usando MapStruct
    │               │   │   # Interfaces com métodos toEntity(), toDto(), toResponseDto()
    │               │   │   # Conversão automática, performática e tipada entre camadas
    │               │   │   # Evita boilerplate de setters/getters manuais
    │               │   │
    │               │   └── service/          # Camada de Serviços (regras de negócio)
    │               │       # Classes com @Service que concentram toda a lógica de negócio
    │               │       # Validações, cálculos, regras e orquestração de operações
    │               │       # Injeção de dependências (@Autowired) de repositórios e mappers
    │               │       # Tratamento de exceções e respostas personalizadas
    │               │
    │               └── interfaces/           # Camada de entrada (controllers REST)
    │                   └── controllers/      # REST Controllers (exposição de endpoints)
    │                       # Classes com @RestController e @RequestMapping
    │                       # Recebem requisições HTTP, validam DTOs (com @Valid)
    │                       # Chamam os serviços correspondentes
    │                       # Retornam respostas HTTP com códigos de status apropriados
    │                       # Agrupados por recurso: Auth, User, Game, Post, Comment
    │
    └── resources/
        ├── application.yml                   # Configuração principal da aplicação
        ├── application-dev.yml               # Configurações do perfil de desenvolvimento
        ├── application-prod.yml              # Configurações do perfil de produção
        ├── application-test.yml              # Configurações para testes
        ├── db/
        │   └── migration/                    # Scripts Flyway/Liquibase (versionamento do banco)
        │       # Scripts SQL sequenciais (V1__, V2__, etc.)
        │       # Mantêm histórico de migrações do esquema do banco
        │       # Permitem evolução controlada da base de dados
        ├── static/                           # Arquivos estáticos (imagens, etc.)
        └── templates/                        # (opcional) Templates para renderização
```

---

## 📦 Entidades e Relacionamentos

| **Entidade** | **Descrição** | **Relacionamentos** |
|--------------|---------------|---------------------|
| **User** | Usuários do sistema (id, nome, email, senha, role, dataCriacao) | Um usuário pode ter vários posts, comentários e listas de jogos |
| **Game** | Jogos cadastrados (id, nome, genero, descricao, dataLancamento) | Um jogo pode aparecer em várias listas de usuários |
| **Post** | Posts de "Looking For Game" (id, titulo, conteudo, status, autor, dataCriacao) | Pertence a um usuário; pode ter vários comentários |
| **Comment** | Comentários nos posts (id, conteudo, autor, post, dataCriacao) | Pertence a um usuário e a um post |
| **GameList** | Listas personalizadas de jogos (relacionamento N:N entre User e Game) | Relaciona um usuário a um jogo com um status (favorito, wishlist, jogando) |

---

## 🛠️ Ferramentas Utilizadas

| Área | Tecnologia |
|------|------------|
| **Linguagem** | **Java 17+** |
| **Framework** | **Spring Boot 3.x** |
| **ORM** | **Spring Data JPA / Hibernate** |
| **Segurança** | **Spring Security + JWT** |
| **Documentação API** | **SpringDoc OpenAPI (Swagger)** |
| **Mapper** | **MapStruct** |
| **Banco de Dados** | **PostgreSQL** (produção) / **H2** (desenvolvimento) |
| **Migração de Banco** | **Flyway** |
| **Validação** | **Hibernate Validator** |
| **Logging** | **SLF4J + Logback** |
| **Testes Unitários** | **JUnit 5 + Mockito** |
| **Testes de Integração** | **Spring Boot Test + Testcontainers** |
| **Build Tool** | **Maven** ou **Gradle** |
| **Monitoramento** | **Spring Actuator + Micrometer** |
| **Cache** | **Spring Cache + Redis** (opcional) |
| **Filas/Async** | **Spring AMQP / RabbitMQ** (opcional) |
| **CI/CD** | **GitHub Actions / Jenkins** |

---

## 🏗️ Instalação e Execução

### Pré-requisitos

- **Java 17** ou superior
- **Maven** 3.8+ ou **Gradle** 7.5+
- **PostgreSQL** 14+ (local ou Docker)
- **Git** (para clonagem)

### Clone o repositório

```bash
git clone https://github.com/LyanBrito/luna-project-backend.git
cd luna-project-backend
```

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE luna_db;
CREATE USER luna_user WITH PASSWORD 'luna_password';
GRANT ALL PRIVILEGES ON DATABASE luna_db TO luna_user;
```

2. Configure as variáveis de ambiente ou atualize o `application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/luna_db
    username: luna_user
    password: luna_password
  jpa:
    hibernate:
      ddl-auto: validate   # Mantém o schema via Flyway
```

### Executando o Projeto

#### Via Maven:
```bash
# Compilar e rodar os testes
mvn clean install

# Executar a aplicação
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Via Gradle:
```bash
# Compilar e rodar os testes
./gradlew clean build

# Executar a aplicação
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Via Docker (opcional):
```bash
# Build da imagem
docker build -t luna-api .

# Executar com Docker Compose (inclui banco)
docker-compose up
```

### Acessando a API

- **Aplicação:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`
- **Health Check:** `http://localhost:8080/actuator/health`

---

## 🔒 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação. O fluxo básico é:

1. **Registro:** `POST /api/auth/register` → Cria um novo usuário
2. **Login:** `POST /api/auth/login` → Retorna um token JWT
3. **Autenticação:** Envie o token no header `Authorization: Bearer <token>`
4. **Refresh:** `POST /api/auth/refresh` → Gera um novo token (se expirado)

---

## 📊 Endpoints Principais

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| **POST** | `/api/auth/register` | Cadastro de novo usuário | Público |
| **POST** | `/api/auth/login` | Login e geração de token | Público |
| **POST** | `/api/auth/refresh` | Refresh do token | Público |
| **GET** | `/api/users` | Listar todos os usuários | ADMIN |
| **GET** | `/api/users/{id}` | Buscar usuário por ID | Usuário/ADMIN |
| **PUT** | `/api/users/{id}` | Atualizar usuário | Usuário/ADMIN |
| **DELETE** | `/api/users/{id}` | Deletar usuário | ADMIN |
| **GET** | `/api/games` | Listar todos os jogos | Público |
| **GET** | `/api/games/{id}` | Buscar jogo por ID | Público |
| **POST** | `/api/games` | Criar novo jogo | ADMIN |
| **PUT** | `/api/games/{id}` | Atualizar jogo | ADMIN |
| **DELETE** | `/api/games/{id}` | Deletar jogo | ADMIN |
| **GET** | `/api/posts` | Listar todos os posts | Público |
| **GET** | `/api/posts/{id}` | Buscar post por ID | Público |
| **POST** | `/api/posts` | Criar novo post | Usuário |
| **PUT** | `/api/posts/{id}` | Atualizar post | Autor/ADMIN |
| **DELETE** | `/api/posts/{id}` | Deletar post | Autor/ADMIN |
| **GET** | `/api/comments/post/{postId}` | Listar comentários de um post | Público |
| **POST** | `/api/comments` | Criar novo comentário | Usuário |
| **DELETE** | `/api/comments/{id}` | Deletar comentário | Autor/ADMIN |
| **POST** | `/api/game-lists` | Adicionar jogo à lista do usuário | Usuário |
| **DELETE** | `/api/game-lists` | Remover jogo da lista do usuário | Usuário |
| **GET** | `/api/game-lists/user/{userId}` | Listar jogos de um usuário | Usuário/ADMIN |

---

## 🧪 Testes

### Executando testes unitários:
```bash
mvn test
```

### Executando testes de integração:
```bash
mvn verify
```

### Cobertura de testes (com JaCoCo):
```bash
mvn jacoco:report
```

---

## 🌱 Contribuições

Fique à vontade para abrir uma issue ou enviar um pull request para melhorias ou correções. Suas contribuições são bem-vindas!

---

## 🔗 Links

[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-%23000000.svg?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Swagger-%2385EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Docker](https://img.shields.io/badge/Docker-%232496ED.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-%232088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![JUnit](https://img.shields.io/badge/JUnit-%2325A162.svg?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Flyway](https://img.shields.io/badge/Flyway-%23CC0000.svg?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/)
[![MapStruct](https://img.shields.io/badge/MapStruct-%23E34F26.svg?style=for-the-badge&logo=mapstruct&logoColor=white)](https://mapstruct.org/)

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**Desenvolvido com 💜 pela Team Luna**
