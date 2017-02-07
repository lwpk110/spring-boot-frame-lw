alter table mail_delivery_channels
    add column sequence integer default 0 after name;