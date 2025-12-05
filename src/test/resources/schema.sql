CREATE TABLE sidos (
    sido_code INT PRIMARY KEY,
    sido_name VARCHAR(255)
);

CREATE TABLE guguns (
    gugun_code INT PRIMARY KEY,
    gugun_name VARCHAR(255),
    sido_code INT
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth DATE
);

CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    password_updated_at TIMESTAMP,
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE free_board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT,
    title VARCHAR(255),
    content TEXT,
    view_cnt INT DEFAULT 0,
    is_removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE notice_board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT,
    title VARCHAR(255),
    content TEXT,
    view_cnt INT DEFAULT 0,
    is_removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id)
);


CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE attractions (
    no BIGINT AUTO_INCREMENT PRIMARY KEY,
    area_code BIGINT,
    si_gun_gu_code BIGINT,
    content_id INT,
    content_type_id INT,
    title VARCHAR(255),
    first_image1 VARCHAR(255),
    first_image2 VARCHAR(255),
    homepage VARCHAR(255),
    latitude DECIMAL(20, 17),
    longitude DECIMAL(20, 17),
    map_level INT,
    overview TEXT,
    tel VARCHAR(255)
);

CREATE TABLE plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255),
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attraction_id BIGINT,
    author_id BIGINT,
    content VARCHAR(255),
    is_removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (attraction_id) REFERENCES attractions(no),
    FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE attraction_tag (
    attraction_id INT,
    tag_id BIGINT,
    PRIMARY KEY (attraction_id, tag_id)
);

CREATE TABLE free_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT,
    author_id BIGINT,
    content VARCHAR(255),
    is_removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES free_board(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE notice_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notice_board_id BIGINT,
    author_id BIGINT,
    content VARCHAR(255),
    is_removed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (notice_board_id) REFERENCES notice_board(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);
