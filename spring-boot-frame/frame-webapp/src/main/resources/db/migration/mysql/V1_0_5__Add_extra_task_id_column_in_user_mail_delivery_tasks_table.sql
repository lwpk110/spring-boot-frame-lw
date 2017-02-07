alter table user_mail_delivery_tasks
    add column extra_task_id varchar(100) after channel_node_id;