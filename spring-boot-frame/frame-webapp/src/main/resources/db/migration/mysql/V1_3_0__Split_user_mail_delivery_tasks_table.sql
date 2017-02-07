create table user_mail_delivery_tasks_recipients (
	recipient_count integer not null default 0,
	recipients longtext not null,
	id BINARY(16) not null,
	primary key (id)
);

 alter table user_mail_delivery_tasks_recipients
	add constraint FK_b913apyjht8aly6qplfyulxgb foreign key (id)
	references user_mail_delivery_tasks (id);

create table user_mail_delivery_tasks_templates (
	body longtext not null,
	is_html bit not null,
	subject varchar(255) not null,
	id BINARY(16) not null,
	primary key (id)
);
alter table user_mail_delivery_tasks_templates
	add constraint FK_dr298wcc7q0buamcdk3wbg92p foreign key (id)
	references user_mail_delivery_tasks (id);