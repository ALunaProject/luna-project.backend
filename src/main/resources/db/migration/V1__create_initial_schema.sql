-- Schema inicial: user, post e comments (espelha as entidades User, Post e Comment)

CREATE TABLE `user` (
    id BINARY(16) NOT NULL,
    username VARCHAR(255),
    email VARCHAR(255),
    bio VARCHAR(255),
    password VARCHAR(255),
    profile_pic_url VARCHAR(255),
    banner_url VARCHAR(255),
    role VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE post (
    id BINARY(16) NOT NULL,
    title VARCHAR(255),
    content VARCHAR(255),
    createion_date DATE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content VARCHAR(1000) NOT NULL,
    user_id BINARY(16) NOT NULL,
    post_id BINARY(16) NOT NULL,
    creation_date DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES `user` (id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES post (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
