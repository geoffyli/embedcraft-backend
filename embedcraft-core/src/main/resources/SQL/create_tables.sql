/* Create user table. */
CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `account` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

create table model
(
    model_id        varchar(255) not null,
    window_size     int          null,
    user_id         int          not null,
    name            varchar(255) null,
    tag             varchar(255) null,
    algorithm       varchar(255) null,
    min_count       int          null,
    vector_size     int          null,
    epochs          int          null,
    training_time   int          null,
    vocabulary_size int          null,
    model_file_path varchar(255) null,
    loss_over_time  varchar(255) not null,
    constraint pk
        primary key (model_id)
);