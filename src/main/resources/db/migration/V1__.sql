drop table "user";
CREATE TABLE "user"
(
    user_id   BIGINT NOT NULL,
    email     VARCHAR(255),
    password  VARCHAR(255),
    name      VARCHAR(255),
    social_id VARCHAR(255),
    provider  VARCHAR(255),
    role      VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

