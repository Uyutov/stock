Before running services start docker-compose file, it should install postgresql database, keycloak authorization service and rabbitmq messaging broker.

For the proper work of keycloak realm-export.json file should be imported into the service. 
To do so:
Open localhost:9090, use admin/admin as username and password;
Click on drop down menu with keycloack writed on it, and click create realm;
Then import realm-export.json file.

For proper work of rabbitmq next steps should be made:
Open localhost:15672 and use user/user as username and password;
Create exchange "productAvailability";
Create queue "check" and bind it to "productAvailability" exchange with routing key "check".

Services should be startet in this order: config-service -> discovery-service -> gateway-service -> the rest in any order (order-service, product-inventory-servise, user-service). 

All endpoins in user-service are acceseble by unauthorized requests.

All endpoints in order-service are only acceseble by user authorized requests, except /order/change-state, it can be acccesed by packeger, delivery and pickup-manager.

All endpoints in product-inventory- service are accesed by storekeeper.