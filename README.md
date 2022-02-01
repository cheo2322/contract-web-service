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
4. Perform the respective request using the postman collection
attached to the project at the root directory.

## Database model

Using MySQl Workbench, the next database model has been designed:

![db_model](/assets/image1.png)