/* Create user table. */
CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `account` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

