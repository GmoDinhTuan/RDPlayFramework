# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  id                            varchar(255) not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  status                        varchar(1),
  role                          varchar(10) not null,
  avatar                        varchar(255),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists user;

