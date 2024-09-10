alter table order_stock.orders
add total_price int not null check ( total_price > 0 );