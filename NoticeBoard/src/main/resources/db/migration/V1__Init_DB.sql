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

alter table if exists message add constraint FKkjoquerqtrcvqgnxlkfn7iwv0 foreign key (topic_id) references topic
