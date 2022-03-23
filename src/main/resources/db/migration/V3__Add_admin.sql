CREATE EXTENSION pgcrypto;

insert into users (id,active,email,nickname,password,username)
    values (1,true ,'dampcave@gmail.com', 'DampCave', crypt('Froststorm25#', gen_salt('bf', 8)),'dampcave');

insert into user_role(user_id,roles_id) values (1,1);
insert into user_role(user_id,roles_id) values (1,2);
