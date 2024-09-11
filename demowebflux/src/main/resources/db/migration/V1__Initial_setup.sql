CREATE TABLE users (
	id INT auto_increment NOT NULL,
	username varchar(100) NULL,
	display_name varchar(100) NULL,
	password varchar(255) NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE todos (
	id INT auto_increment NOT NULL,
	title varchar(100) NULL,
	description varchar(100) NULL,
	completed BOOL NULL,
	username varchar(100) NULL,
	CONSTRAINT todos_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE roles (
	id INT auto_increment NOT NULL,
	name varchar(100) NULL,
	CONSTRAINT roles_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE users_roles (
	id INT auto_increment NOT NULL,
	user_id INT NULL,
	role_id INT NULL,
	CONSTRAINT users_roles_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;