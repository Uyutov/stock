alter table order_stock.orders
    alter column order_state set not null;

alter table order_stock.orders
    alter column delivery_address set not null;