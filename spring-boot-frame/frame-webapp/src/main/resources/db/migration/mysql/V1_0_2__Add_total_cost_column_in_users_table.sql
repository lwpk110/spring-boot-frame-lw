alter table users
    add column total_cost bigint after balance;
    
update users set total_cost=0;

alter table users
    modify total_cost bigint not null;