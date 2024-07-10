create sequence order_stock.orders_id_seq increment by 5;

create table order_stock.orders
(
    id               bigint primary key default nextval('order_stock.orders_id_seq'),
    state            varchar                                                                                  not null,
    delivery_address varchar                                                                                  not null,
    user_id          character varying references public.user_entity (id) on update cascade on delete cascade not null
);

alter sequence order_stock.orders_id_seq
    owned by order_stock.orders.id;

create sequence order_stock.warehouse_id_seq increment by 5;

create table order_stock.warehouse
(
    id      bigint  not null primary key default nextval('order_stock.warehouse_id_seq'),
    name    varchar not null,
    address varchar not null
);

alter sequence order_stock.warehouse_id_seq
    owned by order_stock.warehouse.id;

create sequence order_stock.product_id_seq increment by 5;

create table order_stock.product
(
    id    bigint primary key default nextval('order_stock.product_id_seq'),
    name  varchar not null,
    price int     not null,
    constraint product_min_price check ( price > 0 )
);

alter sequence order_stock.product_id_seq
    owned by order_stock.product.id;

create table order_stock.order_product
(
    amount     int                                                                            not null,
    order_id   bigint references order_stock.orders (id) on update cascade on delete cascade  not null,
    product_id bigint references order_stock.product (id) on update cascade on delete cascade not null,
    constraint product_order_positive_amount check ( amount >= 0 )
);

create table order_stock.warehouse_product
(
    amount       int                                                                              not null,
    warehouse_id bigint references order_stock.warehouse (id) on update cascade on delete cascade not null,
    product_id   bigint references order_stock.product (id) on update cascade on delete cascade   not null,
    constraint product_warehouse_positive_amount check ( amount >= 0 )
);
