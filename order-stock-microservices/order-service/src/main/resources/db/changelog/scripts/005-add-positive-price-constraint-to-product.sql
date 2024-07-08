alter table order_stock.product drop constraint if exists positive_price;

alter table order_stock.product
    add constraint positive_price check ( price > 0 )