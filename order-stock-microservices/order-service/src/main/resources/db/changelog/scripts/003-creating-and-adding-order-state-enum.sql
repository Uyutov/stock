create type state as enum ('PACKAGING', 'DELIVERING', 'AWAITING', 'RECEIVED');

alter table order_stock.orders
alter column state type state using state::state;