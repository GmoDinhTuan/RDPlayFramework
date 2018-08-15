# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table groups (
  id                            varchar2(255) not null,
  groupsname                    varchar2(255) not null,
  status                        varchar2(1) not null,
  description                   varchar2(255),
  constraint pk_groups primary key (id)
);

create table member (
  id                            varchar2(255) not null,
  username                      varchar2(20) not null,
  password                      varchar2(20) not null,
  status                        varchar2(2),
  role                          varchar2(20) not null,
  avatar                        varchar2(255),
  description                   varchar2(255),
  constraint pk_member primary key (id)
);

create table members_group (
  id                            varchar2(255),
  group_id                      varchar2(255),
  member_id                     varchar2(255),
  status                        varchar2(255),
  username                      varchar2(255)
);


# --- !Downs

drop table groups cascade constraints purge;

drop table member cascade constraints purge;

drop table members_group cascade constraints purge;

