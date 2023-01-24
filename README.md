# server-practice

## Usage

### build
```
$ ./gradlew build
```
### environment variables 설정
- `CONF_JWT_ISSUER` : jwt 발행자
- `CONF_JWT_SECRET` : jwt에 sign할 때의 필요한 secret
- `CONF_JWT_ACCESS_EXPIRY` : access token의 유효시간 (seconds)

- `CONF_DB_HOST` : Database Host
- `CONF_DB_PORT` : Database Port
- `CONF_DB_USERNAME` : Database에 connect 하기 위한 username
- `CONF_DB_PASSWORD` : `CONF_DB_USERNAME`에 대응하는 비밀번호
- `CONF_DB_SCHEMA` : Database schema (postgresql 기준)

### run 
```
$ ./gradlew run
```

## Database Setting
### Users Table
```
CREATE TABLE users (
	id varchar NOT NULL,
	email varchar NOT NULL,
	password_hash varchar NOT NULL,
	"role" varchar NOT NULL,
	CONSTRAINT users_email UNIQUE (email),
	CONSTRAINT users_pk PRIMARY KEY (id)
);
```
### Products Table
```
CREATE TABLE products (
	id varchar NOT NULL,
	price float8 NOT NULL,
	commission float8 NULL,
	is_examined bool NOT NULL,
	CONSTRAINT products_pk PRIMARY KEY (id)
);
```
### Product Descriptions Table
```
CREATE TABLE product_descriptions (
	id varchar NOT NULL,
	product_id varchar NOT NULL,
	"language" varchar NOT NULL,
	title varchar NOT NULL,
	"content" varchar NOT NULL,
	CONSTRAINT product_descriptions_pk PRIMARY KEY (id)
);
```
