-- Dados de teste para os endpoints de comments.
-- Senha dos usuários mockados: password
-- Os UUIDs abaixo podem ser usados diretamente nas chamadas aos endpoints.

INSERT INTO `user` (id, username, email, bio, password, profile_pic_url, banner_url, role) VALUES
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 'Lucas', 'lucas@example.com', 'Jogador de RPG e fã de mundos abertos.', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://example.com/images/lucas.jpg', NULL, 'USER'),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 'Lyan', 'lyan@example.com', 'Curte jogos de corrida e competições online.', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://example.com/images/lyan.jpg', NULL, 'USER'),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 'Vitor', 'vitor@example.com', 'Administrador e colecionador de jogos retrô.', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://example.com/images/vitor.jpg', NULL, 'ADMIN');

INSERT INTO post (id, title, content, createion_date) VALUES
    (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), 'Qual RPG jogar primeiro?', 'Estou em dúvida entre começar um RPG clássico ou um lançamento recente. O que vocês recomendam?', '2026-09-01'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), 'Melhor pista nos jogos de corrida', 'Para mim, uma pista bem feita precisa ter curvas técnicas e boas oportunidades de ultrapassagem.', '2026-09-05'),
    (UUID_TO_BIN('cccccccc-cccc-cccc-cccc-cccccccccccc'), 'Minha lista de jogos retrô', 'Separei alguns jogos antigos que ainda valem muito a pena conhecer.', '2026-09-10');

INSERT INTO comments (content, user_id, post_id, creation_date) VALUES
    ('Eu começaria por um RPG clássico. A história costuma prender bastante.', UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), '2026-09-01 09:15:00'),
    ('Um lançamento recente pode ser mais fácil para quem está começando.', UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), '2026-09-01 09:30:00'),
    ('Depende do seu estilo: exploração, combate ou uma boa narrativa?', UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), '2026-09-01 10:05:00'),
    ('Gosto quando a pista tem mudanças de elevação e atalhos arriscados.', UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), '2026-09-05 14:00:00'),
    ('Concordo, mas uma pista bonita também faz muita diferença.', UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), '2026-09-05 14:12:00'),
    ('As melhores pistas equilibram velocidade, técnica e diversão.', UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), '2026-09-05 14:30:00');
