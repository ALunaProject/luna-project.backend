# Plano de Testes — Luna Project Backend

**Versão analisada:** `7f6bb50` (2026-11-12)
**Stack:** Java 21, Spring Boot 3.5.7, Spring Security + JWT, Spring Data JPA, MySQL, Cloudinary
**Base URL local:** `http://localhost:8080`
**Documentação viva:** `http://localhost:8080/swagger-ui.html`

---

## 1. Objetivo e escopo

Validar os fluxos funcionais, as regras de negócio, a autorização e os contratos HTTP da API do Luna Project.

**Em escopo:** autenticação JWT, usuários e perfil, upload de imagem, posts e tags, comentários e moderação, listas de jogos, avaliações (ratings), cache de jogos, catálogo de tags, CORS e tratamento de erros.

**Fora de escopo:** interface do front-end, integração real com a Steam, SLA do Cloudinary, testes de carga além de smoke, pentest formal e testes de acessibilidade.

> Observação importante: o README está desatualizado em vários pontos. Os casos abaixo usam os endpoints **reais** do código. A seção 7 lista as divergências.

---

## 2. Legenda

### Prioridade

| Prioridade | Critério |
|---|---|
| **Alta** | Caminho crítico ou falha de segurança; bloqueia release. |
| **Média** | Regra de negócio relevante ou erro recuperável; deve ser corrigido no ciclo. |
| **Baixa** | Borda, usabilidade, documentação ou melhoria. |

### Técnicas

| Sigla | Técnica |
|---|---|
| **PE** | Partição de Equivalência |
| **AVL** | Análise de Valor Limite |
| **TD** | Tabela de Decisão |
| **TTE** | Teste de Transição de Estados |
| **Matriz** | Matriz de Autorização (perfil × recurso) |
| **Contrato** | Teste de Contrato de API (status, schema, headers) |
| **Integração** | Teste de Integração (controller → service → repositório) |
| **Exceção** | Teste de Exceção / Caminho de Falha |
| **Segurança** | Teste de Segurança (injeção, mass assignment, exposição de dados) |
| **Regressão** | Reexecução de casos após mudança |
| **Smoke** | Verificação rápida de disponibilidade |

---

## 3. Massa de teste

| Apelido | Dado |
|---|---|
| **U1** | `luna.tester@example.com` / `Senha@123` — role `USER` |
| **U2** | `outro.tester@example.com` / `Senha@123` — role `USER` |
| **A1** | `admin.tester@example.com` — role `ADMIN` (promovido via SQL; não há endpoint) |
| **P1 / P2** | Posts criados por U1 / U2 |
| **G1 / G2** | Jogos `appid=730` (CS2) e `appid=570` (Dota 2) |
| **T1** | 5 IDs válidos de `src/main/resources/steam-tags.json` |
| **Listas padrão** | `Quero Jogar` (WISHLIST), `Jogando` (PLAYING), `Jogado` (PLAYED) |
| **Token** | JWT retornado por `POST /auth/login`, header `Authorization: Bearer <token>` |

**Preparação:** subir MySQL `luna_db`, configurar `.env` do Cloudinary e criar U1/U2 via `POST /api/users`. Para cooldown de comentário, aguardar 2 minutos ou ajustar `creation_date` via SQL.

---

## 4. Contrato real × README

| Operação | README | Implementação real |
|---|---|---|
| Login | `POST /api/auth/login` | `POST /auth/login` |
| Cadastro | `POST /api/auth/register` | `POST /api/users` |
| Refresh token | `POST /api/auth/refresh` | Não existe |
| Posts | `/api/posts` | `/api/post` |
| Listas | `/api/gameCache-lists` | `/api/lists` |
| `GET /api/users` | ADMIN | Público |
| `POST /api/games` | ADMIN | Público |
| Banco | PostgreSQL + Flyway + H2 | MySQL + `ddl-auto=update`, Flyway desligado |
| Actuator | `/actuator/health` | Dependência não incluída no `pom.xml` |

---

## 5. Casos de teste

### 5.1 Autenticação e sessão (AUT)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| AUT-01 | Login | Credenciais válidas de U1 | RF-AUT-01 Login retorna JWT e username | PE | Alta | `200 OK` com `token` JWT e `username`; token com `iss=luna-project-api` e `sub=id` de U1 |
| AUT-02 | Login | Senha incorreta | RF-AUT-02 Credenciais inválidas são rejeitadas | PE | Alta | `401` com mensagem genérica e sem token (ver R3) |
| AUT-03 | Login | E-mail não cadastrado | RF-AUT-02 Credenciais inválidas são rejeitadas | PE | Alta | `401` com mensagem genérica e sem token (ver R3) |
| AUT-04 | Login | E-mail com maiúsculas/espaços | RF-AUT-03 E-mail é normalizado no login | PE | Média | `200 OK`; autenticação funciona via `findByEmailIgnoreCase` + `trim` |
| AUT-05 | Login | Corpo nulo, campos vazios ou e-mail inválido | RF-AUT-04 Entrada inválida retorna 400 | AVL + PE | Média | `400` com `{"message": "..."}`; não deve retornar `500` (ver R3) |
| AUT-06 | Autorização | Requisição protegida sem header `Authorization` | RF-AUT-05 Rotas protegidas exigem JWT | Matriz | Alta | `401 Unauthorized` |
| AUT-07 | Autorização | Token malformado, vazio, `"undefined"` ou com assinatura inválida | RF-AUT-05 Token inválido é rejeitado sem erro interno | Exceção | Alta | `401`; nenhuma exceção `UUID`/`500` |
| AUT-08 | Autorização | Token válido de usuário removido do banco | RF-AUT-06 Token de usuário inexistente é rejeitado | Exceção | Média | `401`; contexto de segurança não autenticado |
| AUT-09 | Sessão | Duas requisições autenticadas consecutivas | RF-AUT-07 API é stateless | Contrato | Média | `200` nas duas; sem `Set-Cookie`/`JSESSIONID`; token exigido em ambas |
| AUT-10 | CORS | Preflight `OPTIONS` de `http://localhost:3000` e de origem não permitida | RF-AUT-08 Apenas origem configurada é aceita | Contrato | Média | Origem permitida: `200` com `Access-Control-Allow-Origin`; outra origem: `403 Invalid CORS request` |

### 5.2 Usuários e perfil (USR)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| USR-01 | Cadastro | Criar usuário com username, e-mail e senha válidos | RF-USR-01 Cadastro cria USER com senha BCrypt e 3 listas padrão | PE | Alta | `201 Created` com `id`, `username`, `email`, `role=user`; senha não retornada; 3 listas padrão criadas |
| USR-02 | Cadastro | E-mail já cadastrado, variando caixa e espaços (`LUNA.TESTER@EXAMPLE.COM `) | RF-USR-02 E-mail é único e normalizado | PE | Alta | `409 Conflict` com `{"message": "Este e-mail já está cadastrado..."}` |
| USR-03 | Cadastro | Username já usado por outro usuário | RF-USR-03 Username é único | PE | Alta | `409 Conflict` com mensagem de username em uso (ver R4) |
| USR-04 | Cadastro | Usernames reservados: `login`, `Login`, `games`, `api`, `undefined` | RF-USR-04 Username não pode colidir com rotas do front | TD | Alta | `400 Bad Request` com "Este username é reservado pelo sistema" |
| USR-05 | Cadastro | Username com espaço, vazio, ou senha fraca (curta, sem maiúscula, sem número, sem especial) | RF-USR-05 Validação de username e senha forte | AVL + TD | Alta | `400` com a primeira mensagem de validação; senha válida de 8 caracteres retorna `201` |
| USR-06 | Cadastro | Senha com 100 e 101 caracteres | RF-USR-05 Senha tem de 8 a 100 caracteres | AVL | Média | 100 caracteres: `201`; 101 caracteres: `400` |
| USR-07 | Cadastro | E-mail sem `@`, sem domínio ou vazio | RF-USR-05 E-mail válido é obrigatório | PE | Média | `400` com "O campo [email] deve conter um e-mail válido" |
| USR-08 | Consulta | Buscar U1 por ID; buscar UUID inexistente | RF-USR-06 Consulta de perfil por ID | PE | Alta | Existente: `200` com `id`, `username`, `email`, `profilePicUrl`, `bannerUrl`, `role`, `bio`; inexistente: `404` |
| USR-09 | Consulta | Listar todos os usuários sem token | RF-USR-07 Listagem pública (conforme código) | Contrato | Média | `200` com lista; nenhum campo `password`; atenção ao e-mail exposto (ver R13) |
| USR-10 | Atualização | U1 altera apenas `bio`, enviando `username` atual e campos de imagem nulos | RF-USR-08 Atualização parcial preserva campos não enviados | TD | Alta | `200`; `bio` alterada; `profilePicUrl` e `bannerUrl` anteriores preservados |
| USR-11 | Atualização | U1 tenta alterar U2; A1 tenta alterar U2 | RF-USR-09 Apenas dono ou ADMIN altera | Matriz | Alta | U1: `403` "Você não tem permissão..."; A1: `200` |
| USR-12 | Atualização | U1 troca para username já usado ou reservado | RF-USR-10 Novo username é validado | PE | Média | Username em uso: `409`; reservado: `400`; manter o próprio username: `200` |
| USR-13 | Exclusão | U1 exclui a própria conta; U1 exclui U2; A1 exclui U2; excluir UUID inexistente | RF-USR-11 Exclusão apenas dono ou ADMIN | Matriz | Alta | Dono: `200`; U1 sobre U2: `403`; A1 sobre U2: `200`; inexistente: `404` (ver R7) |
| USR-14 | Exclusão | Excluir usuário que possui posts, comentários e listas | RF-USR-11 Exclusão não deve gerar erro interno | Integração | Média | `204`/`200` com remoção em cascata ou `409` com mensagem clara; não deve retornar `500` por FK (ver R7) |
| USR-15 | Comentários | Consultar comentários de U1; consultar UUID inexistente | RF-USR-12 Comentários do usuário são públicos | PE | Média | `200` com lista em ordem decrescente; inexistente: `404` |

### 5.3 Upload de imagem (IMG)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| IMG-01 | Foto de perfil | U1 envia JPG ou PNG de até 2 MB em `POST /api/users/{id}/profile-picture` | RF-IMG-01 Upload válido pelo dono | AVL | Alta | `200` com `id` e `username`; URL do Cloudinary salva em `profilePicUrl` |
| IMG-02 | Banner | U1 envia JPG de 2 MB em `POST /api/users/{id}/banner` | RF-IMG-01 Upload válido pelo dono | AVL | Alta | `200`; URL salva em `bannerUrl` e retornada na consulta do perfil |
| IMG-03 | Validação | Enviar arquivo vazio, 2 MB + 1 byte, 3 MB, GIF, PDF ou SVG | RF-IMG-02 Apenas JPG/PNG até 2 MB | AVL + PE | Média | `400` (ou `413` para limite do multipart) com mensagem específica; nunca `200` |
| IMG-04 | Autorização | U1 envia imagem para U2; ADMIN envia para U2; enviar para UUID inexistente | RF-IMG-03 Apenas dono ou ADMIN altera imagem | Matriz | Alta | U1 sobre U2: `403`; A1 sobre U2: `200`; inexistente: `404` |
| IMG-05 | Segurança | Enviar arquivo HTML/scripts declarando `Content-Type: image/jpeg` | RF-IMG-04 Tipo real do arquivo é validado | Segurança | Baixa | Upload rejeitado; validação não deve confiar apenas no `Content-Type` (ver R15) |

### 5.4 Posts e tags (PST)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| PST-01 | Criação | U1 cria post com conteúdo e exatamente 5 tags válidas | RF-PST-01 Post autenticado com até 5 tags | AVL | Alta | `201 Created` com `id`, `content`, `creation_date`, `createdAt`, `user_id`, `author` e `tags` resolvidas |
| PST-02 | Criação | Criar post sem token | RF-PST-02 Criação exige autenticação | Matriz | Alta | `401 Unauthorized` |
| PST-03 | Criação | Post com 6 tags; post com tag inexistente (ex.: `999999`); post com `tagIds` nulo | RF-PST-03 Tags são validadas contra o catálogo | AVL + PE | Média | 6 tags ou tag inválida: `400`; sem tags: `201` com lista vazia |
| PST-04 | Criação | Post com conteúdo nulo, vazio ou apenas espaços | RF-PST-04 Conteúdo é obrigatório | PE | Média | `400` com mensagem de conteúdo obrigatório (ver R10) |
| PST-05 | Consulta | Listar posts; buscar P1 por ID; buscar UUID inexistente | RF-PST-05 Consulta de posts é pública | PE | Alta | Lista: `200`; P1: `200` com autor e tags; inexistente: `404` |
| PST-06 | Edição | U1 altera o conteúdo do próprio post | RF-PST-06 Autor edita o próprio post | PE | Alta | `200` com "Post updated successfully"; novo conteúdo verificável via `GET /api/post/{id}`; `tagIds` preservados quando não enviados |
| PST-07 | Edição | U1 tenta editar P2 (de U2); A1 edita P2 | RF-PST-07 Apenas autor ou ADMIN edita | Matriz | Alta | U1: `403`; A1: `200` (ver R1 — hoje qualquer autenticado edita) |
| PST-08 | Exclusão | U1 exclui P1; U1 exclui P2; A1 exclui P2 | RF-PST-08 Apenas autor ou ADMIN exclui | Matriz | Alta | Dono: `200`; U1 sobre P2: `403`; A1 sobre P2: `200` (ver R1) |
| PST-09 | Exceção | Editar e excluir post com UUID inexistente | RF-PST-09 Recurso inexistente retorna 404 | Exceção | Média | `404 Not Found` (ver R8 — hoje retorna `200` com texto) |
| PST-10 | Edição | Enviar `tagIds` novos na edição | RF-PST-10 Tags podem ser atualizadas na edição | TD | Baixa | Tags substituídas pelas enviadas (ver R10 — hoje ignoradas) |
| PST-11 | Integração | Consultar `GET /api/post/{postId}/comments` de P1 | RF-PST-11 Comentários do post são públicos | PE | Média | `200` com lista em ordem decrescente de criação |
| PST-12 | Segurança | Criar post com payload de XSS/SQL injection no conteúdo | RF-PST-12 Conteúdo é persistido sem executar comandos | Segurança | Média | `201`; conteúdo retorna como texto no JSON; nenhum comando SQL é executado |
| PST-13 | Integração | Excluir P1 que possui comentários e consultar comentários do alvo | RF-PST-13 Exclusão não deixa dados órfãos | Integração | Média | Comentários removidos ou inacessíveis após a exclusão (ver R11) |

### 5.5 Comentários e moderação (COM)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| COM-01 | Criação | U1 comenta em P1 via `POST /api/comments/POST/{postId}` | RF-COM-01 Comentário em post | PE | Alta | `201` com `id`, `content` sem espaços nas pontas, `userId`, `username`, `targetType=POST`, `targetId`, `creationDate` |
| COM-02 | Criação | U1 comenta no próprio perfil via `USER_PROFILE/{userId}` | RF-COM-02 Comentário em perfil | PE | Alta | `201` com `targetType=USER_PROFILE` |
| COM-03 | Criação | U1 comenta em jogo via `GAME/730` | RF-COM-03 Comentário em jogo | Integração | Alta | `201`; jogo `730` criado/reutilizado no cache |
| COM-04 | Criação | U1 comenta em notícia via `NEWS/{id}` | RF-COM-04 Comentário em notícia | PE | Baixa | `201`; `targetId` armazenado como enviado |
| COM-05 | Criação | Criar comentário sem token | RF-COM-05 Criação exige autenticação | Matriz | Alta | `401 Unauthorized` |
| COM-06 | Validação | Conteúdo vazio, nulo ou apenas espaços | RF-COM-06 Conteúdo é obrigatório | PE | Alta | `400` com "O conteúdo do comentário não pode estar vazio" |
| COM-07 | Validação | Conteúdo com 1000 e 1001 caracteres | RF-COM-06 Limite de 1000 caracteres | AVL | Média | 1000: `201`; 1001: `400` com mensagem de tamanho |
| COM-08 | Cooldown | U1 faz o primeiro comentário e tenta o segundo imediatamente | RF-COM-07 Cooldown de 2 minutos por autor | TTE | Alta | Segundo: `429 Too Many Requests` com "Aguarde N segundos..." |
| COM-09 | Cooldown | Após 2 minutos, U1 repete conteúdo idêntico no mesmo alvo | RF-COM-08 Comentário duplicado é bloqueado | TTE + PE | Alta | `409 Conflict` com "Você já fez um comentário idêntico aqui" |
| COM-10 | Cooldown | Após 2 minutos, U1 envia conteúdo diferente no mesmo alvo | RF-COM-07 Cooldown libera novo comentário | TTE | Alta | `201 Created` |
| COM-11 | Duplicidade | Após cooldown, enviar mesmo texto variando maiúsculas/minúsculas | RF-COM-08 Duplicidade é case-insensitive | PE | Média | `409 Conflict` |
| COM-12 | Alvo inválido | Comentar em post inexistente ou usuário inexistente | RF-COM-09 Alvo deve existir | PE | Alta | `404 Not Found` |
| COM-13 | Alvo inválido | UUID malformado; `GAME/0`; `GAME/-1`; `GAME/abc`; `NEWS` com ID > 100 caracteres | RF-COM-09 Alvo e formato são validados | TD | Média | `400 Bad Request` com "Id inválido", "appid inválido" ou "Id do alvo inválido" |
| COM-14 | Consulta | Listar comentários de P1; listar por usuário; buscar comentário por ID; buscar ID inexistente | RF-COM-10 Consulta e ordenação | PE | Média | Listas: `200` em ordem decrescente; ID existente: `200`; inexistente: `404` |
| COM-15 | Edição | Autor edita; ADMIN edita; outro usuário tenta editar | RF-COM-11 Apenas autor ou ADMIN edita | Matriz | Alta | Autor: `200`; ADMIN: `200`; outro: `403` "Você não tem permissão para alterar este comentário" |
| COM-16 | Exclusão | Autor exclui; ADMIN exclui; dono do mural exclui comentário no próprio perfil; outro tenta excluir | RF-COM-12 Autor, ADMIN ou dono do mural exclui | Matriz | Alta | Autor/ADMIN/dono do mural: `200` com "Comment deleted successfully"; outro: `403` |

### 5.6 Listas de jogos (LST)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| LST-01 | Listas padrão | Cadastrar U2 e consultar `GET /api/lists/{userId}` | RF-LST-01 Todo usuário recebe 3 listas padrão | Integração | Alta | Exatamente `Quero Jogar` (WISHLIST), `Jogando` (PLAYING) e `Jogado` (PLAYED) |
| LST-02 | Criação | U1 cria lista custom com nome de 2 a 36 caracteres | RF-LST-02 Criar lista custom | PE + AVL | Alta | `201` com `type=CUSTOM`, nome sem espaços nas pontas e usuário U1 |
| LST-03 | Validação | Nome com 1, 2, 36 e 37 caracteres; nome nulo; nome duplicado (inclusive de lista padrão) | RF-LST-02 Nome tem 2–36 caracteres e é único | AVL + PE | Média | 1/37/nulo: `400`; 2/36: `201`; duplicado: `409` |
| LST-04 | Autorização | Criar lista sem token | RF-LST-03 Criação exige autenticação | Matriz | Alta | `401 Unauthorized` |
| LST-05 | Jogos | U1 adiciona G1 à lista custom, enviando `appid` e `assets.library_capsule` | RF-LST-04 Adicionar jogo à própria lista | Integração | Alta | `200` com lista contendo G1; G1 criado no cache se não existir |
| LST-06 | Jogos | U1 adiciona jogo à lista de U2 | RF-LST-04 Apenas o dono altera a lista | Matriz | Alta | `403 Forbidden` (ver R5 — hoje pode retornar `500`) |
| LST-07 | Jogos | U1 adiciona G1 duas vezes à mesma lista | RF-LST-04 Jogo não é duplicado | PE | Baixa | `200`; G1 aparece uma única vez (coleção `Set`) |
| LST-08 | Jogos | U1 remove G1 da própria lista; remove G1 que não está na lista; remove appid inexistente no cache | RF-LST-05 Remover jogo da lista | PE | Média | Remoção válida: `200`; já ausente: `200` sem alteração; appid inexistente: `404` |
| LST-09 | Exclusão | Excluir lista padrão; excluir lista custom do dono; U1 exclui lista de U2; excluir lista inexistente | RF-LST-06 Listas padrão não podem ser excluídas | Matriz | Alta | Padrão: `400` "Default lists can't be deleted"; dono: `200`; outro: `403`; inexistente: `404` |
| LST-10 | Renomeação | Renomear lista custom do dono; renomear lista padrão; U1 renomeia lista de U2 | RF-LST-07 Apenas dono renomeia lista custom | Matriz | Alta | Custom do dono: `200`; padrão: `400` "Default lists can't be modified"; outro: `403` |
| LST-11 | Renomeação | Renomear lista custom para nome já usado por outra lista do mesmo usuário | RF-LST-07 Nome da lista é único | PE | Média | `409 Conflict` (ver R9 — hoje permite duplicar) |
| LST-12 | Ordenação | Consultar listas de U1 com 3 padrão + 3 custom | RF-LST-08 Ordem de exibição | PE | Média | Padrão primeiro (WISHLIST, PLAYING, PLAYED) e custom em ordem alfabética pt-BR |
| LST-13 | Exceção | Consultar listas de UUID inexistente | RF-LST-08 Consulta por usuário | Exceção | Baixa | `404 Not Found` (ver R14 — hoje retorna `200 []`) |

### 5.7 Avaliações de jogos (RTG)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| RTG-01 | Nota válida | U1 avalia G1 com `0.0` e depois com `10.0` | RF-RTG-01 Nota entre 0 e 10 | AVL | Alta | `200` com `score`, `targetType=GAME`, `targetId=730` |
| RTG-02 | Nota inválida | Enviar `-0.1`, `10.1`, `5.55` e `null` | RF-RTG-01 Nota entre 0 e 10 com 1 casa decimal | AVL + PE | Alta | `400` com "A nota mínima é 0", "A nota máxima é 10", "Use no máximo 1 casa decimal" ou "A nota é obrigatória" |
| RTG-03 | Nota válida | Enviar `5.5` | RF-RTG-01 Uma casa decimal é aceita | AVL | Média | `200` |
| RTG-04 | Atualização | U1 avalia G1, depois envia nova nota para G1 | RF-RTG-02 Upsert por usuário + jogo | TTE | Alta | `200`; mesmo `id`; `score` e `updatedDate` atualizados; sem registro duplicado |
| RTG-05 | Consulta | `GET /ratings/games/730/me` com e sem avaliação | RF-RTG-03 Consultar avaliação do usuário | PE | Alta | Com nota: `200` com dados; sem nota: `204 No Content` |
| RTG-06 | Exclusão | `DELETE /ratings/games/730` e repetir a exclusão | RF-RTG-04 Remoção idempotente | TTE | Média | Ambas: `204 No Content`; nota deixa de existir |
| RTG-07 | Isolamento | U1 e U2 avaliam G1; U1 avalia G1 e G2 | RF-RTG-05 Avaliações são isoladas por usuário e jogo | Matriz | Alta | Cada usuário/jogo possui sua própria nota; alteração de U1 não afeta U2 |
| RTG-08 | Autorização | Avaliar, consultar e remover sem token | RF-RTG-06 Ratings exigem autenticação | Matriz | Alta | `401 Unauthorized` em todos |
| RTG-09 | Borda | Avaliar com `appId` não numérico (`abc`) | RF-RTG-01 AppId do jogo é válido | PE | Baixa | `400 Bad Request` (ver R16 — hoje aceita qualquer string) |

### 5.8 Cache de jogos (GAM)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| GAM-01 | Criação | `POST /api/games` com G1 inexistente e `assets.library_capsule` preenchido | RF-GAM-01 Criar jogo no cache | PE | Alta | `200` com `appid=730`, mensagem "Game added to cache!" e assets; registro persistido |
| GAM-02 | Criação | Repetir o `POST` de G1 com nova capsule | RF-GAM-01 Jogo existente é atualizado | TTE | Média | `200` com "This game already exists in cache."; capsule atualizada sem duplicar `appid` |
| GAM-03 | Validação | Enviar sem `appid`, sem `assets` ou com `library_capsule` em branco | RF-GAM-02 `appid` e assets são obrigatórios | TD | Média | `400 Bad Request` com mensagem de validação |
| GAM-04 | Consulta | `GET /api/games?appid=730`; `GET /api/games?appid=999999` | RF-GAM-03 Consultar jogo no cache | PE | Alta | Existente: `200` com dados do cache; inexistente: `404` "Game not found on cache" |
| GAM-05 | Consulta | `GET /api/games` sem `appid` | RF-GAM-03 Ausência de `appid` retorna erro tratado | Exceção | Média | `400` com mensagem de `appid` obrigatório (ver R6 — hoje pode retornar `500`) |
| GAM-06 | Acesso | Criar e consultar jogo sem token | RF-GAM-04 Cache é público conforme `SecurityConfig` | Contrato | Baixa | `200`; divergente do README, que indica ADMIN (ver R13) |

### 5.9 Catálogo de tags (TAG)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| TAG-01 | Consulta | `GET /api/tags` sem token | RF-TAG-01 Catálogo público | Contrato | Média | `200` com lista não vazia de `{id, name}` carregada de `steam-tags.json` |
| TAG-02 | Integração | Criar post com IDs válidos em ordem invertida e com repetição | RF-TAG-02 Tags são deduplicadas e ordenadas pelo catálogo | Integração | Baixa | `tags` sem duplicatas, na ordem do catálogo (mais populares primeiro) |

### 5.10 Contrato, erros e segurança transversal (API)

| ID | Cenário | Caso de teste | Requisito | Técnica | Prioridade | Resultado esperado |
|---|---|---|---|---|---|---|
| API-01 | Erro de validação | Enviar dois campos inválidos em um DTO | RF-API-01 Erros de validação têm contrato único | Contrato | Alta | `400` com `{"message": "<primeira mensagem>"}`; sem stack trace |
| API-02 | Documentação | Acessar `/swagger-ui/index.html` e `/v3/api-docs` sem token | RF-API-02 Documentação pública | Smoke | Baixa | `200`; OpenAPI lista os endpoints reais |
| API-03 | Rotas | Acessar rota inexistente sem token e com token | RF-API-03 Rota inexistente | Matriz | Média | Sem token: `401`; com token: `404` |
| API-04 | Métodos | `PATCH /api/post` com token; `POST /api/post` com token e `Content-Type: text/plain` | RF-API-04 Métodos e mídia suportados | Contrato | Média | `405 Method Not Allowed`; `415 Unsupported Media Type` |
| API-05 | JSON inválido | Enviar JSON malformado para criação de post | RF-API-05 Corpo malformado retorna 400 | Exceção | Média | `400 Bad Request`; API permanece disponível |
| API-06 | Mass assignment | Criar usuário enviando `role: "ADMIN"`, `id` e `passwordHash` extras | RF-API-06 Campos não previstos são ignorados | Segurança | Média | `201`; usuário criado com `role=user` e `id` gerado; campos extras ignorados |
| API-07 | Exposição de dados | Inspecionar respostas de usuários, posts, comentários e erros | RF-API-07 Dados sensíveis não são expostos | Segurança | Alta | Nenhuma resposta contém `password`, hash, stack trace ou SQL |
| API-08 | Concorrência | Duas requisições simultâneas de cadastro com o mesmo e-mail | RF-API-08 E-mail é único sob concorrência | Concorrência | Média | Uma retorna `201` e a outra `409`; sem `500` (ver R4 — e-mail não tem constraint única no banco) |
| API-09 | Força bruta | 20 tentativas de login com senha errada para U1 | RF-API-09 Login possui proteção contra força bruta | Segurança | Média | Após N tentativas: `429 Too Many Requests` (ver R19 — não implementado) |

---

## 6. Matriz de autorização consolidada

| Recurso / operação | Anônimo | Dono (USER) | Outro USER | ADMIN |
|---|---|---|---|---|
| `POST /api/users` | Permitido | — | — | — |
| `GET /api/users` e `/api/users/{id}` | Permitido | Permitido | Permitido | Permitido |
| `PUT/DELETE /api/users/{id}` | `401` | `200` | `403` | `200` |
| `POST /api/users/{id}/profile-picture` e `/banner` | `401` | `200` | `403` | `200` |
| `GET /api/post` e `/api/post/{id}` | Permitido | Permitido | Permitido | Permitido |
| `POST /api/post` | `401` | `201` | `201` | `201` |
| `PUT/DELETE /api/post/{id}` | `401` | `200` | `403` (hoje `200`) | `200` |
| `POST /api/comments/{type}/{id}` | `401` | `201` | `201` | `201` |
| `PUT /api/comments/{id}` | `401` | `200` (autor) | `403` | `200` |
| `DELETE /api/comments/{id}` | `401` | `200` (autor) | `403` | `200`; dono do mural também pode |
| `GET /api/lists/{userId}` | Permitido | Permitido | Permitido | Permitido |
| `POST/PUT/DELETE /api/lists/**` | `401` | `200/201` | `403` | `403` (sem bypass de ADMIN) |
| `GET /api/tags` | Permitido | Permitido | Permitido | Permitido |
| `POST/GET /api/games` | Permitido | Permitido | Permitido | Permitido |
| `GET/PUT/DELETE /ratings/games/**` | `401` | `200/204` | `200/204` | `200/204` |

---

## 7. Desvios entre código e requisito (riscos para a execução)

| ID | Severidade | Divergência | Evidência | Casos impactados |
|---|---|---|---|---|
| R1 | **Alta** | `PUT/DELETE /api/post/{id}` não verifica autor nem ADMIN; qualquer usuário autenticado edita/apaga post alheio | `PostController.java:46-54`, `PostService.java:51-69` | PST-07, PST-08 |
| R2 | **Alta** | JWT sem `expiresAt` e segredo com fallback hardcoded `my-secret-key`; `.env` não define o segredo | `TokenService.java:14-27` | AUT-01, AUT-06 |
| R3 | **Alta** | `BadCredentialsException` sem handler e `AuthenticationDTO` sem validação; login inválido tende a `500` em vez de `401/400` | `AuthenticationController.java:26-33`, `ValidationExceptionHandler.java` | AUT-02, AUT-03, AUT-05 |
| R4 | **Alta** | Cadastro não verifica username duplicado antes do `save`; e-mail não tem constraint única no banco | `UserService.java:36-68`, `User.java:26-28` | USR-03, API-08 |
| R5 | **Alta** | `java.nio.file.AccessDeniedException` nas listas não é mapeada para `403`; tende a `500` | `GameListServices.java:54-114` | LST-06, LST-09, LST-10 |
| R6 | **Média** | `GET /api/games` sem `appid` chama `findById(null)`; consulta retorna assets da requisição, não do cache | `GameController.java:24-27`, `GameCacheService.java:52-58` | GAM-04, GAM-05 |
| R7 | **Média** | Exclusão de usuário não trata posts/comentários/listas relacionados; FK pode gerar `500`; UUID inexistente retorna `200`/`403` em vez de `404` | `UserService.java:122-131`, entidades sem cascade | USR-13, USR-14 |
| R8 | **Média** | `PUT/DELETE` de post inexistente retorna `200` com texto, não `404` | `PostService.java:39-44,51-69` | PST-09 |
| R9 | **Média** | Renomeação de lista não valida duplicidade; criação valida | `GameListServices.java:38-52,84-97` | LST-11 |
| R10 | **Média** | `PostRequestDto.content` sem `@NotBlank`; edição ignora `tagIds` e só altera `content` | `PostRequestDto.java:12-16`, `PostService.java:60-69` | PST-04, PST-10 |
| R11 | **Média** | Comentários usam `targetId` textual; exclusão de post deixa comentários órfãos | `Comment.java:30-32`, `PostService.java:51-58` | PST-13 |
| R12 | **Média** | README desatualizado: endpoints, roles, refresh token, Flyway, PostgreSQL/H2 e Actuator não correspondem ao código | `README.md` vs `SecurityConfig.java`, `pom.xml`, `application.properties` | Todos os módulos |
| R13 | **Média** | `GET /api/users` e `/api/users/{id}` são públicos e expõem e-mail; `POST /api/games` é público, mas README indica ADMIN | `SecurityConfig.java:39-66`, `UserResponseDto.java` | USR-09, GAM-06 |
| R14 | **Baixa** | `GET /api/lists/{userId}` de usuário inexistente retorna `200 []` | `GameListServices.java:116-128` | LST-13 |
| R15 | **Baixa** | Upload valida apenas o `Content-Type` declarado, não os magic bytes do arquivo | `ImageUploadService.java:37-49` | IMG-05 |
| R16 | **Baixa** | Ratings aceitam `appId` string arbitrária (`abc`) sem validar o jogo | `RatingController.java:31-39`, `RatingService.java:29-41` | RTG-09 |
| R17 | **Alta** | Só existe `contextLoads`; não há testes unitários/integrados, perfil de teste, H2 ou Testcontainers | `src/test/java/.../ApplicationTests.java`, `pom.xml` | Automação |
| R18 | **Média** | CORS fixo em `http://localhost:3000`; não há configuração por ambiente | `SecurityConfig.java:80-91` | AUT-10 |
| R19 | **Média** | Login sem rate limiting ou bloqueio por tentativas | `AuthenticationController.java` | API-09 |
| R20 | **Baixa** | `POST /api/games` retorna `200` mesmo ao criar recurso; contrato poderia usar `201` | `GameController.java:18-22` | GAM-01 |

---

## 8. Estratégia de execução e automação

### Ordem sugerida

1. **Smoke:** aplicação sobe, `/v3/api-docs` responde, login de U1 funciona (AUT-01, API-02).
2. **Funcional e regras:** USR-01, PST-01, COM-01, LST-02, LST-05, RTG-01, GAM-01.
3. **Negativos e bordas:** validações, limites, inexistentes e duplicidades.
4. **Segurança e autorização:** matriz da seção 6, mass assignment, JWT e CORS.
5. **Regressão:** todos os casos Alta + os corrigidos no ciclo.

### Pirâmide de automação

| Nível | Ferramenta | Alvo |
|---|---|---|
| Unitário | JUnit 5 + Mockito | `UserService`, `CommentService`, `GameListServices`, `RatingService`, `TagCatalog`, `TokenService` |
| Integração | `@SpringBootTest` + MockMvc + Testcontainers (MySQL) | Controllers, segurança, validações, persistência e ordenação |
| Contrato | springdoc + validação de schema OpenAPI | Status, campos e content-type |
| E2E/API | REST Assured ou Postman/Newman | Fluxos completos: cadastro → login → post → comentário → lista → rating |
| CI | GitHub Actions + `mvn verify` + JaCoCo | Bloqueio de merge com falha em teste Alta |

### Critérios de entrada

- Aplicação compilando (`mvn clean install`).
- Banco disponível e massa de teste criada.
- Credenciais do Cloudinary configuradas para IMG.

### Critérios de saída

- 100% dos casos **Alta** executados.
- Nenhum defeito **Alta** ou **Crítico** aberto.
- Casos **Média** corrigidos ou com aceite formal.
- Cobertura de serviços ≥ 80% (meta inicial).

---

## 9. Resumo de cobertura

| Módulo | Casos | Alta | Média | Baixa |
|---|---:|---:|---:|---:|
| Autenticação (AUT) | 10 | 5 | 5 | 0 |
| Usuários (USR) | 15 | 9 | 6 | 0 |
| Imagens (IMG) | 5 | 3 | 1 | 1 |
| Posts (PST) | 13 | 6 | 6 | 1 |
| Comentários (COM) | 16 | 11 | 4 | 1 |
| Listas (LST) | 13 | 7 | 4 | 2 |
| Ratings (RTG) | 9 | 6 | 2 | 1 |
| Cache de jogos (GAM) | 6 | 2 | 3 | 1 |
| Tags (TAG) | 2 | 0 | 1 | 1 |
| Transversal (API) | 9 | 2 | 6 | 1 |
| **Total** | **98** | **51** | **38** | **9** |

> Os totais consideram a prioridade atribuída em cada linha; ajuste conforme o risco do release.
