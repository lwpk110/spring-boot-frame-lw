alter table users
    add column login_count integer after username;
    
update users set login_count=0;

alter table users
    modify login_count integer not null;
    
alter table users
    add column last_login_at datetime after login_count;