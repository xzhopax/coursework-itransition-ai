create table public.film_actor
(
    actor_id int8 not null,
    film_id  int8 not null,
    primary key (actor_id, film_id)
);
create table public.film_producer
(
    producer_id int8 not null,
    film_id     int8 not null,
    primary key (producer_id, film_id)
);
create sequence hibernate_sequence start 1 increment 1;
create table actors
(
    id        int8 not null,
    full_name varchar(255),
    primary key (id)
);
create table comments
(
    id       int8 not null,
    filename varchar(255),
    message  varchar(255),
    time     varchar(255),
    user_id  int8,
    film_id  int8,
    primary key (id)
);
create table film_genre
(
    genre_id int8 not null,
    genres   varchar(255)
);
create table films
(
    id          int8   not null,
    budget      int8,
    description text,
    duration    int4 not null,
    picture     varchar(255),
    rating      float8,
    title       varchar(255) not null,
    url_video   varchar(255),
    year        int4,
    user_id     int8,
    primary key (id)
);
create table producers
(
    id        int8 not null,
    full_name varchar(255),
    primary key (id)
);
create table roles
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);
create table user_role
(
    user_id  int8 not null,
    roles_id int8 not null,
    primary key (user_id, roles_id)
);
create table users
(
    id       int8    not null,
    active   boolean,
    email    varchar(255),
    nickname varchar(255),
    password varchar(255) not null,
    photo    varchar(255),
    username varchar(255) not null,
    primary key (id)
);
alter table if exists public.film_actor add constraint film_actor_fk foreign key (actor_id) references actors;
alter table if exists public.film_actor add constraint actor_film_fk foreign key (film_id) references films;
alter table if exists public.film_producer add constraint film_producer_fk foreign key (producer_id) references producers;
alter table if exists public.film_producer add constraint producer_film_fk foreign key (film_id) references films;
alter table if exists comments add constraint comments_user_fk foreign key (user_id) references users;
alter table if exists comments add constraint comments_film_fk foreign key (film_id) references films;
alter table if exists film_genre add constraint film_genre_fk foreign key (genre_id) references films;
alter table if exists films add constraint films_user_fk foreign key (user_id) references users;
alter table if exists user_role add constraint user_role_fk foreign key (roles_id) references roles;
alter table if exists user_role add constraint role_user_fk foreign key (user_id) references users;