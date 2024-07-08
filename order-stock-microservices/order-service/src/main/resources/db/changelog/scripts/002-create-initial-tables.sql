create table order_stock.orders
(
    id               bigint generated by default as identity primary key,
    order_state      varchar,
    delivery_address varchar,
    user_id          character varying references public.user_entity (id) on update cascade on delete cascade not null
);

create table order_stock.warehouse
(
    id      bigint generated by default as identity primary key,
    name    varchar,
    address varchar
);

create table order_stock.product
(
    id    bigint generated by default as identity primary key,
    name  varchar,
    price int
);

create table order_stock.order_product
(
    amount     int,
    order_id   bigint references order_stock.orders (id) on update cascade on delete cascade  not null,
    product_id bigint references order_stock.product (id) on update cascade on delete cascade not null
);

create table order_stock.warehouse_product
(
    amount       int,
    warehouse_id bigint references order_stock.warehouse (id) on update cascade on delete cascade not null,
    product_id   bigint references order_stock.product (id) on update cascade on delete cascade   not null
);
