# SteVe-SC
This project targets an Electric Vehicle "Load Management" use case for OCPP1.6 charging equipment. It combines a LoadManagement module (responsible for setting charge current limits on the connected vehicles) with SteVe, which is a Central System software module for OCPP EV charging. This project is explained further on the [wiki](https://github.com/chuck-h/SteVe-SC/wiki)

SteVe is developed and maintained by [RWTH-Aachen University](https://github.com/RWTH-i5-IDSG). More information on this module may be found in the steve subdirectory.

## Text below is placeholder copied from SteVe, to be replaced

### System Requirements

SteVe-SC requires 
* JDK 11 (both Oracle JDK and OpenJDK are supported)
* Maven 
* At least MySQL 5.6.4 (MariaDB 10.0 or later works as well) as database

to build and run. 

SteVe-SC is designed to run standalone, a java servlet container / web server (e.g. Apache Tomcat), is **not** required.

# Configuration and Installation

1. Database preparation:


2. Download and extract tarball:


3. Configure SteVe **before** building:

    The basic configuration is defined in [main.properties](src/main/resources/config/prod/main.properties):
      - You _must_ change [database configuration](src/main/resources/config/prod/main.properties#L9-L13)
      - You _must_ change [the host](src/main/resources/config/prod/main.properties#L22) to the correct IP address of your server
      - You _must_ change [web interface credentials](src/main/resources/config/prod/main.properties#L17-L18)
      - You _can_ access the application via HTTPS, by [enabling it and setting the keystore properties](src/main/resources/config/prod/main.properties#L32-L35)
     
    For advanced configuration please see the [Configuration wiki](https://github.com/RWTH-i5-IDSG/steve/wiki/Configuration)

4. Build SteVe:

    To compile SteVe simply use Maven. A runnable `jar` file containing the application and configuration will be created in the subdirectory `steve/target`.

    ```
    # mvn package
    ```

5. Run SteVe:

    To start the application run (please do not run SteVe as root):

    ```
    # java -jar target/steve.jar
    ```

# First Steps

After SteVe has successfully started, you can access the web interface using the configured credentials under:

    http://<your-server-ip>:<port>/steve/manager
    
The default port number is 8080.

### Add a charge point

1. In order for SteVe to accept messages from a charge point, the charge point must first be registered. To add a charge point to SteVe select *Data Management* >> *Charge Points* >> *Add*. Enter the ChargeBox ID configured in the charge point and confirm.

2. The charge points must be configured to communicate with following addresses. Depending on the OCPP version of the charge point, SteVe will automatically route messages to the version-specific implementation.
    - SOAP: `http://<your-server-ip>:<port>/steve/services/CentralSystemService`
    - WebSocket/JSON: `ws://<your-server-ip>:<port>/steve/websocket/CentralSystemService/<chargeBoxId>`


As soon as a heartbeat is received, you should see the status of the charge point in the SteVe Dashboard.
 
*Have fun!*

Screenshots
-----
1. [Home](website/screenshots/home.png)
2. [Connector Status](website/screenshots/connector-status.png)
3. [Data Management - Charge Points](website/screenshots/chargepoints.png)
4. [Data Management - Users](website/screenshots/users.png)
5. [Data Management - OCPP Tags](website/screenshots/ocpp-tags.png)
6. [Data Management - Reservations](website/screenshots/reservations.png)
7. [Data Management - Transactions](website/screenshots/transactions.png)
8. [Operations - OCPP v1.2](website/screenshots/ocpp12.png)
9. [Operations - OCPP v1.5](website/screenshots/ocpp15.png)
10. [Settings](website/screenshots/settings.png)


GDPR
-----
If you are in the EU and offer vehicle charging to other people using SteVe, keep in mind that you have to comply to the General Data Protection Regulation (GDPR) as SteVe processes charging transactions, which can be considered personal data.

Are you having issues?
-----
See the [FAQ](https://github.com/RWTH-i5-IDSG/steve/wiki/FAQ)

