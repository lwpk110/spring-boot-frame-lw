create table mail_delivery_channel_nodes (
    id integer not null auto_increment,
    created_date datetime,
    last_modified_date datetime,
    config_props varchar(2000) not null,
    disabled bit not null,
    name varchar(50) not null,
    server_key varchar(50) not null,
    version bigint not null,
    channel_id integer not null,
    primary key (id)
);

create table mail_delivery_channels (
    id integer not null auto_increment,
    created_date datetime,
    last_modified_date datetime,
    description varchar(255),
    disabled bit not null,
    fee integer not null,
    max_num_limit integer not null,
    name varchar(100) not null,
    version bigint not null,
    primary key (id)
);

create table user_deposit_order_details (
    id bigint not null auto_increment,
    created_date datetime,
    last_modified_date datetime,
    amount decimal(19,2) not null,
    credits integer not null,
    deposit_method varchar(20),
    status integer not null,
    trade_no varchar(50) not null,
    version bigint not null,
    user_id bigint not null,
    primary key (id)
);

create table user_mail_delivery_settings (
    id bigint not null auto_increment,
    created_date datetime,
    last_modified_date datetime,
    checked bit not null,
    reply_email varchar(255),
    reply_name varchar(50),
    sender_email varchar(255) not null,
    sender_name varchar(50) not null,
    version bigint not null,
    user_id bigint not null,
    primary key (id)
);

create table user_mail_delivery_tasks (
    id BINARY(16) not null,
    created_date datetime,
    last_modified_date datetime,
    reply_email varchar(255),
    reply_name varchar(50),
    sender_email varchar(255) not null,
    sender_name varchar(50) not null,
    delivery_status integer not null,
    description varchar(255),
    name varchar(100) not null,
    parent_user_id bigint not null,
    parent_username varchar(255),
    recipient_count integer not null,
    recipients longtext not null,
    scheduled bit not null,
    scheduled_date datetime,
    body longtext not null,
    is_html bit not null,
    subject varchar(255) not null,
    total_fee integer not null,
    version bigint not null,
    channel_id integer not null,
    channel_node_id integer,
    user_id bigint not null,
    primary key (id)
);

create table user_mail_recipient_groups (
    id BINARY(16) not null,
    created_date datetime,
    last_modified_date datetime,
    name varchar(100) not null,
    recipient_count integer not null,
    recipients longtext not null,
    source varchar(50),
    user_id bigint not null,
    primary key (id)
);

create table user_mail_templates (
    id bigint not null auto_increment,
    created_date datetime,
    last_modified_date datetime,
    name varchar(100) not null,
    body longtext not null,
    is_html bit not null,
    subject varchar(255) not null,
    use_count bigint not null,
    version bigint not null,
    user_id bigint not null,
    primary key (id)
);

create table user_transaction_details (
    id bigint not null auto_increment,
    comment varchar(255),
    created_date datetime,
    credits integer not null,
    transaction_type varchar(50) not null,
    user_id bigint not null,
    primary key (id)
);

create table users (
    id bigint not null,
    created_date datetime,
    last_modified_date datetime,
    balance integer not null,
    total_deposit bigint not null,
    username varchar(255) not null,
    version bigint not null,
    primary key (id)
);

alter table user_deposit_order_details
    add constraint UK_i8pxggo8l40200nmelv3dkbf7  unique (trade_no);

alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6  unique (username);

alter table mail_delivery_channel_nodes
    add constraint FK_cwybnrdy3xqk77uwu97jecmj5 foreign key (channel_id)
    references mail_delivery_channels (id);

alter table user_deposit_order_details
    add constraint FK_aqcxt2lax3h14h2kvmgthl6sf foreign key (user_id)
    references users (id);

alter table user_mail_delivery_settings
    add constraint FK_sawc9omk65py07hc9qrvffemi foreign key (user_id)
    references users (id);

alter table user_mail_delivery_tasks
    add constraint FK_2r023u9mvou3cx0xs1u6gcoab foreign key (channel_id)
    references mail_delivery_channels (id);

alter table user_mail_delivery_tasks
    add constraint FK_anbu6t2trolxac06rxk2smwfa foreign key (channel_node_id)
    references mail_delivery_channel_nodes (id);

alter table user_mail_delivery_tasks
    add constraint FK_t8ywol1v3t1dudvj6qbcurd7q foreign key (user_id)
    references users (id);

alter table user_mail_recipient_groups
    add constraint FK_lq4nlf44avu3ie2wnl2ipwdg0 foreign key (user_id)
    references users (id);

alter table user_mail_templates
    add constraint FK_dchu1xwsj0gmdqgqregvh2qmn foreign key (user_id)
    references users (id);

alter table user_transaction_details
    add constraint FK_67l32dakgi0clvgsrdqmyi33t foreign key (user_id)
    references users (id);

