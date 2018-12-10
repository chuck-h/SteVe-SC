# SteVe-SC
This project targets an Electric Vehicle "Load Management" use case for [OCPP1.6](https://www.openchargealliance.org/protocols/ocpp-16/) charging equipment. It combines a LoadManagement module (responsible for setting charge current limits on the connected vehicles) with SteVe, which is a Central System software module for OCPP EV charging. This project is explained further on the [wiki](https://github.com/chuck-h/SteVe-SC/wiki)

[SteVe](https://github.com/RWTH-i5-IDSG/steve/blob/master/README.md) is developed and maintained by [RWTH-Aachen University](https://github.com/RWTH-i5-IDSG). More information on the SteVe module may be found in the steve subdirectory.

The LoadManagement module is independently developed by chuck-h and is motivated by serving a small residential multi-charger installation in Seattle, WA, USA.

### Text below is placeholder to be replaced

### System Requirements

SteVe-SC requires 
* JDK 11 (both Oracle JDK and OpenJDK are supported)
* Maven 
* At least MySQL 5.6.4 (MariaDB 10.0 or later works as well) as database

to build and run. 

SteVe-SC is designed to run standalone, a java servlet container / web server (e.g. Apache Tomcat), is **not** required.

# Configuration and Installation

1. Database preparation:
* for SteVe, follow instructions in steve/README.md
* for LoadManager TBD
2. Download and extract tarball (or git clone --recurse-submodules)
3. Configure SteVe **before** building:
* for SteVe, follow instructions in steve/README.md
* for LoadManager TBD
4. Build SteVe-SC:
    To compile SteVe-SC simply use Maven.
    ```
    cd Steve-SC
    mvn package
    ```
     A runnable `jar` file containing the SteVe application and configuration will be created in the subdirectory `steve/target`. A runnable `jar` file containing the LoadManager application and configuration will be created in the subdirectory `LoadManager/target`.

5. Run SteVe:
    To start the SteVe application run (please do not run SteVe as root):
    ```
    java -jar target/steve.jar
    ```
6. Run LoadManager:
    To start the LoadManager application run (please do not run SteVe as root):
    ```
    java -jar target/steve.jar
    ```

