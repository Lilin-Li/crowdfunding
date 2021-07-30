use crowd_admin;
drop table if exists admin_t;

create table admin_t
(
    id int not null auto_increment, # 主鍵
    login_acct varchar(255) not null, # 登錄賬號
    user_pswd char(32) not null, # 登錄密碼
    user_name varchar(255) not null, # 昵稱
    email varchar(255) not null, # 郵件地址
    create_time char(19), # 建立時間
    primary key (id)
);