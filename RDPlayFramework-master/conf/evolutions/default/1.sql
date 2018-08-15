# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table groups (
<<<<<<< HEAD
  id                            number(19) not null,
=======
  id                            varchar2(255) not null,
>>>>>>> 5c54caa2ab502b213c9917813b5c433d8a45ab2f
  groupsname                    varchar2(255) not null,
  status                        varchar2(1) not null,
  description                   varchar2(255),
  constraint pk_groups primary key (id)
<<<<<<< HEAD
=======
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
>>>>>>> 5c54caa2ab502b213c9917813b5c433d8a45ab2f
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


# --- !Downs

drop table groups cascade constraints purge;
<<<<<<< HEAD
drop sequence GROUPS_SEQ;

drop table member cascade constraints purge;
drop sequence MEMBER_SEQ;

drop table membersgroup cascade constraints purge;
drop sequence MEMBERSGROUP_SEQ;
=======

drop table member cascade constraints purge;

drop table members_group cascade constraints purge;
>>>>>>> 5c54caa2ab502b213c9917813b5c433d8a45ab2f

