# contract-web-service

## Run the service:

1. Install Java 11 in your system (https://www.oracle.com/java/technologies/downloads/).
2. Build the project using the gradle wrapper (it is not necessary to install Gradle):
    ```
    $ ./gradlew clean build
    ```
    This command will clean, build and run the tests.
3. To perform functional tests to the service, you should use:
   ```
   $ ./gradlew bootRun
   ```
4. Perform the respective request using the Postman collection
attached to the project at the root directory (ContractApi.postman_collection.json).
The available requests are detailed next:
 
   * Post a new contract
   * Get a contract by id.
   * Post a new invoice.
   * Get an invoice by id.

## Endpoints

1. **Create & issue a new invoice on a contract**

   In order to fulfill this requirement, some data is introduced into database
   automatically when the project starts. It is achieved using the ```data.sql```
   file found in resources, which is conformed by next commands:

   ```mysql-sql
   insert into clients (username) values ('test-user')

   insert into vendors (username) values ('test-vendor')

   insert into contracts (approved, terms, contract_value, client_id, vendor_id) values (1, '5465726d732e', 100.0, 1, 1)
   ```
   To test the functionality of this endpoint it is used the next request:

   ```shell
   curl --location --request POST 'localhost:8080/contract-api/invoices' \
   --header 'Content-Type: application/json' \
   --data-raw '{
       "vendor": {
           "username": "test-vendor"
       },
        "contract": {
           "id": 1
       },
       "invoice": {
           "timeInHours": 10,
           "hourCost": 4.9,
           "otherMaterials": "Materials",
           "otherMaterialsCost": 1.0,
           "total": 50.0
       }
   }'
   ```
   This request could be located as ```PostInvoice``` into the Postman collection file.


2. **Mark an invoice as void**

   Assuming _void_ as a new status of invoices, it is just a matter of patching an existing invoice using:

   ```shell
   curl --location --request PATCH 'localhost:8080/contract-api/invoices' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "id": 1,
   "status": "VOID"
   }'
   ```
   
   You could use the ```PatchInvoiceStatus``` request from the Postman collection.


3. **Submitting an endpoint**

   This requirement is some difficult to understand. In any case, all endpoints from the Postman
   collection are so clear and useful, feel free to test them manually, I will resolve all your doubts
   and comments, just chat me to sergio.hidalgo.e@gmail.com
   

## Database model

Using MySQl Workbench, the next database model has been designed:

![db_model](/assets/ERM.png)

## OpenApi

You are able to find the OpenApi Specification which details the
endpoints and payloads of this project. This document has been
generated with Swagger and is located at /src/main/resources/openapi.yml

![openapi](/assets/openapi.png)

Additionally, it could be generated from Spring Boot at any time while
the service is running, just execute ```$ ./gradlew bootRun```
and visit http://localhost:8080/swagger-ui.html from a web browser.