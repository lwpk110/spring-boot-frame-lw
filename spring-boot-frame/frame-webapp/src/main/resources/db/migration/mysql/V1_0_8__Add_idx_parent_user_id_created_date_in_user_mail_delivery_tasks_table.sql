create index idx_parent_user_id_created_date
    on user_mail_delivery_tasks (parent_user_id, created_date);