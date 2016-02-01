# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        bigint not null,
  name                      varchar(255),
  folder                    varchar(255),
  constraint pk_project primary key (id))
;

create table session (
  id                        bigint not null,
  user_email                varchar(255),
  token                     varchar(255),
  start_time                varchar(255),
  end_time                  varchar(255),
  is_valid                  boolean,
  constraint pk_session primary key (id))
;

create table task (
  id                        bigint not null,
  title                     varchar(255),
  done                      boolean,
  due_date                  timestamp,
  assigned_to_email         varchar(255),
  folder                    varchar(255),
  project_id                bigint,
  constraint pk_task primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;


create table project_user (
  project_id                     bigint not null,
  user_email                     varchar(255) not null,
  constraint pk_project_user primary key (project_id, user_email))
;

create table session_user (
  session_id                     bigint not null,
  user_email                     varchar(255) not null,
  constraint pk_session_user primary key (session_id, user_email))
;
create sequence project_seq;

create sequence session_seq;

create sequence task_seq;

create sequence user_seq;

alter table task add constraint fk_task_assignedTo_1 foreign key (assigned_to_email) references user (email) on delete restrict on update restrict;
create index ix_task_assignedTo_1 on task (assigned_to_email);
alter table task add constraint fk_task_project_2 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_task_project_2 on task (project_id);



alter table project_user add constraint fk_project_user_project_01 foreign key (project_id) references project (id) on delete restrict on update restrict;

alter table project_user add constraint fk_project_user_user_02 foreign key (user_email) references user (email) on delete restrict on update restrict;

alter table session_user add constraint fk_session_user_session_01 foreign key (session_id) references session (id) on delete restrict on update restrict;

alter table session_user add constraint fk_session_user_user_02 foreign key (user_email) references user (email) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists project;

drop table if exists project_user;

drop table if exists session;

drop table if exists session_user;

drop table if exists task;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists project_seq;

drop sequence if exists session_seq;

drop sequence if exists task_seq;

drop sequence if exists user_seq;

