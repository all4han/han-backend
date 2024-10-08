CREATE TABLE users
(
    user_id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_role         VARCHAR(255),
    email             VARCHAR(255),
    nickname          VARCHAR(255),
    gender            CHAR,
    birthdate         date,
    oauth_type        VARCHAR(255),
    profile_s3_key    VARCHAR(255),
    signup_date       TIMESTAMP WITHOUT TIME ZONE,
    deactivation_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);