drop table if exists attendance;
drop table if exists role_authority;
drop table if exists authority;
drop table if exists day_statistical_data;
drop table if exists study_time;
drop table if exists todo;
drop table if exists calendar;
drop table if exists email_auth;
drop table if exists hibernate_sequence;
drop table if exists month_report;
drop table if exists user_team;
drop table if exists team;
drop table if exists user_role;
drop table if exists role;
drop table if exists vacation;
drop table if exists week_report;
drop table if exists user;

create table attendance (
    attendance_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    check_in_status varchar(255) not null,
    check_out_status varchar(255) not null,
    calendar_id bigint not null,
    user_id bigint not null,
    primary key (attendance_id)
) engine=InnoDB;

create table authority (
    authority_id bigint not null,
    permission varchar(255) not null,
    primary key (authority_id)
) engine=InnoDB;

create table calendar (
    calendar_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    date date,
    day_type varchar(255) not null,
    primary key (calendar_id)
) engine=InnoDB;

create table day_statistical_data (
    day_statistical_data_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    absent_score double precision not null,
    attendance_score double precision not null,
    study_time_score double precision not null,
    todo_success_rate double precision not null,
    total_score double precision not null,
    week_absent_score double precision not null,
    calendar_id bigint,
    user_id bigint,
    primary key (day_statistical_data_id)
) engine=InnoDB;

create table email_auth (
    id bigint not null auto_increment,
    auth_token varchar(255),
    email varchar(255),
    expire_date datetime(6),
    expired bit,
    primary key (id)
) engine=InnoDB;

create table hibernate_sequence (
    next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table month_report (
    month_report_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    user_name varchar(255) not null,
    user_nick_name varchar(255) not null,
    grade integer not null,
    primary key (month_report_id)
) engine=InnoDB;

create table role (
    role_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    value varchar(255) not null,
    primary key (role_id)
) engine=InnoDB;

create table role_authority (
    role_id bigint not null,
    authority_id bigint not null,
    primary key (role_id, authority_id)
) engine=InnoDB;

create table study_time (
    study_time_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    begin_time datetime(6) not null,
    end_time datetime(6) not null,
    final_study_time varchar(255) not null,
    is_studying bit not null,
    study_score double not null,
    calendar_id bigint not null,
    user_id bigint not null,
    primary key (study_time_id)
) engine=InnoDB;

create table team (
    team_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    value varchar(255) not null,
    primary key (team_id)
) engine=InnoDB;

create table todo (
    todo_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    complete bit,
    title varchar(255),
    calendar_id bigint not null,
    user_id bigint not null,
    primary key (todo_id)
) engine=InnoDB;

create table user (
    user_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    account_non_expired bit not null,
    account_non_locked bit not null,
    api_id varchar(255) not null,
    attend_status varchar(255) not null,
    credentials_non_expired bit not null,
    email varchar(80) not null,
    email_auth bit not null,
    enabled bit not null,
    nickname varchar(20) not null,
    password varchar(255) not null,
    refresh_token varchar(255),
    username varchar(30) not null,
    primary key (user_id)
) engine=InnoDB;

create table user_role (
    user_role_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    currently_used bit not null,
    reason varchar(255),
    role_id bigint not null,
    user_id bigint not null,
    primary key (user_role_id)
) engine=InnoDB;

create table user_team (
    user_team_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    currently_used bit not null,
    reason varchar(255),
    team_id bigint not null,
    user_id bigint not null,
    primary key (user_team_id)
) engine=InnoDB;

create table vacation (
    vacation_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    added_days double not null,
    reason varchar(255),
    remaining_days double not null,
    used_days double not null,
    user_id bigint not null,
    primary key (vacation_id)
) engine=InnoDB;

create table week_report (
    week_report_id bigint not null auto_increment,
    update_id varchar(255),
    create_id varchar(255),
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP not null,
    user_name varchar(255) not null,
    user_nick_name varchar(255) not null,
    primary key (week_report_id)
) engine=InnoDB;