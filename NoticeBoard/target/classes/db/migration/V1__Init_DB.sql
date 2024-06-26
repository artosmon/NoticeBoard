create table message (
    id varchar(255) not null,
    created timestamp(6) with time zone,
    author varchar(255),
    text varchar(1024),
    topic_id varchar(255),
    primary key (id)
);

create table topic (
    id varchar(255) not null,
    created_at timestamp(6) with time zone,
    name varchar(255),
    primary key (id)
);

create table users (
    id varchar(255) not null,
    name varchar(255),
    password varchar(255),
    role varchar(255) check (role in ('USER','ADMIN')),
    primary key (id)
);

create table user_role (
    role varchar(255) check (role in ('USER','ADMIN')),
    user_id varchar(255) not null
);

alter table if exists message add constraint FKkjoquerqtrcvqgnxlkfn7iwv0 foreign key (topic_id) references topic
