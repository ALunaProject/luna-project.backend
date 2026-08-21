# luna-project.backend
Luna`s backend :)

```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── seuprojeto/
    │           └── api/                      # Pacote raiz (ex: com.gamematch.api)
    │               │
    │               ├── GamematchApplication.java   # Classe principal
    │               │
    │               ├── config/               # Configurações gerais
    │               │   ├── AppConfig.java    # Beans gerais (ex: ModelMapper, RestTemplate)
    │               │   ├── SwaggerConfig.java # OpenAPI/Swagger
    │               │   └── CorsConfig.java   # Configuração de CORS
    │               │
    │               ├── security/             # Autenticação e Autorização
    │               │   ├── jwt/
    │               │   │   ├── JwtAuthenticationFilter.java
    │               │   │   ├── JwtTokenProvider.java
    │               │   │   └── JwtAuthenticationEntryPoint.java
    │               │   ├── service/
    │               │   │   └── CustomUserDetailsService.java
    │               │   └── SecurityConfig.java   # Configuração do Spring Security
    │               │
    │               ├── domain/               # Camada de domínio (entidades e regras de negócio)
    │               │   ├── entity/           # Entidades JPA (mapeamento das tabelas)
    │               │   │   ├── User.java
    │               │   │   ├── Game.java
    │               │   │   ├── Post.java
    │               │   │   ├── Comment.java
    │               │   │   └── GameList.java  # Lista de jogos (ex: favoritos, wishlist)
    │               │   │
    │               │   ├── enums/            # Enumeradores
    │               │   │   ├── UserRole.java
    │               │   │   ├── PostStatus.java
    │               │   │   └── GameGenre.java
    │               │   │
    │               │   └── repository/       # Interfaces Spring Data JPA
    │               │       ├── UserRepository.java
    │               │       ├── GameRepository.java
    │               │       ├── PostRepository.java
    │               │       ├── CommentRepository.java
    │               │       └── GameListRepository.java
    │               │
    │               ├── application/          # Camada de aplicação (orquestração)
    │               │   ├── dto/              # Data Transfer Objects
    │               │   │   ├── auth/
    │               │   │   │   ├── LoginRequestDTO.java
    │               │   │   │   └── RegisterRequestDTO.java
    │               │   │   ├── user/
    │               │   │   │   ├── UserResponseDTO.java
    │               │   │   │   └── UpdateUserDTO.java
    │               │   │   └── post/
    │               │   │       ├── CreatePostDTO.java
    │               │   │       └── PostResponseDTO.java
    │               │   │
    │               │   ├── mapper/           # Mapeadores (Entity ↔ DTO)
    │               │   │   ├── UserMapper.java      (usando MapStruct)
    │               │   │   ├── PostMapper.java
    │               │   │   └── GameMapper.java
    │               │   │
    │               │   └── service/          # Camada de Serviços (regras de negócio)
    │               │       ├── AuthService.java
    │               │       ├── UserService.java
    │               │       ├── GameService.java
    │               │       ├── PostService.java
    │               │       └── CommentService.java
    │               │
    │               └── interfaces/           # Camada de entrada (controllers)
    │                   └── controllers/      # REST Controllers
    │                       ├── AuthController.java
    │                       ├── UserController.java
    │                       ├── GameController.java
    │                       ├── PostController.java
    │                       └── CommentController.java
    │
    └── resources/
        ├── application.yml                   # Configurações (DB, JPA, JWT, etc.)
        ├── application-dev.yml               # Perfil de desenvolvimento
        ├── application-prod.yml              # Perfil de produção
        ├── db/
        │   └── migration/                    # Scripts Flyway/Liquibase (se usado)
        ├── static/                           # Arquivos estáticos (imagens, etc.)
        └── templates/                        # (opcional) se usar Thymeleaf
```
