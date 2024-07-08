alter table order_stock.warehouse_product drop constraint if exists positive_amount;

alter table order_stock.warehouse_product
    add constraint positive_amount check ( amount > 0 );