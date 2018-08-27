# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table groups (
  id                            number(19) not null,
  groupsname                    varchar2(255) not null,
  status                        varchar2(1) not null,
  description                   varchar2(255),
  constraint pk_groups primary key (id)
);
create sequence GROUPS_SEQ increment by 1;

create table member (
  id                            number(19) not null,
  username                      varchar2(20) not null,
  password                      varchar2(20) not null,
  status                        varchar2(2),
  role                          varchar2(20) not null,
  avatar                        varchar2(255),
  description                   varchar2(255),
  constraint pk_member primary key (id)
);
create sequence MEMBER_SEQ increment by 1;

create table membersgroup (
  id                            number(19) not null,
  groupid                       number(19) not null,
  memberid                      number(19) not null,
  status                        varchar2(255) not null,
  constraint pk_membersgroup primary key (id)
);
create sequence MEMBERSGROUP_SEQ increment by 1;

create table relationship (
  id                            number(19) not null,
  memberfrom                    number(19) not null,
  memberto                      number(19) not null,
  name                          varchar2(255) not null,
  constraint pk_relationship primary key (id)
);
create sequence RELATIONSHIP_SEQ increment by 1;


# --- !Downs

drop table groups cascade constraints purge;
drop sequence GROUPS_SEQ;

drop table member cascade constraints purge;
drop sequence MEMBER_SEQ;

drop table membersgroup cascade constraints purge;
drop sequence MEMBERSGROUP_SEQ;

drop table relationship cascade constraints purge;
drop sequence RELATIONSHIP_SEQ;

