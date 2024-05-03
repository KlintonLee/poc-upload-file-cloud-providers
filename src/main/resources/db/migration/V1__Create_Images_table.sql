CREATE TABLE images (
    id CHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    location VARCHAR(500) NOT NULL
);