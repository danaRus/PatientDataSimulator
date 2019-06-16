# PatientDataSimulator

In order to be able to run the project, the following should be installed:
- Java (at least version 8)
- Apache Maven

Before running the project, the following must be taken into account:
- The MySQL connection is made to an instance that runs on a Cloudera Virtual Machine that is configured in Virtual Box
  - Ensure that the Network of the Virtual Machine is set to "Host-only Adapter"
    a) Steps for configuring this:
      1) Select Virtual Machine
      2) Click on Settings
      3) Open Network tab
      4) In the Adapter 1 tab, for the "Attached to" dropdown select "Host-only Adapter"
  - As such, the MySQL url connection (identified through the property spring.datasource.url from the file application.properties) must be updated to match the IP of the Virtual Machine (can be found by running ifconfig in the terminal of the Virtual Box)
  - In order to be able to connect to MySQL, privileges must be granted for the connection from the host to the Virtual Machine. Steps for configuring this:
    a) In the terminal, type ipconfig (Windows) or ifconfig (Linux)
    b) Identify the entry for "Ethernet adapter VirtualBox Host-Only Network" and copy the IP of the entry "IPv4 Address"
    c) In the Virtual Machine, in a terminal run the following:
      1) mysql -uUSERNAME -pPASSWORD
        - note that USERNAME and PASSWORD must be replaced with the actual credentials
      2) create user 'root'@'IP' identified by 'PASSWORD';
        - note that IP must be replaced with the IP address copied at b)
        - note that PASSWORD myst be replaced with the actual password - the same one used for opening the connection to MySQL at step 1)
      3) grant all privileges on *.* to 'root'@'IP' with grant option;
        - note that IP must be replaced with the IP address copied at b)
      4) flush privileges;
  - The database used must be created before running the project, by using the running the following commands in a terminal opened in the Virtual Machine:
    a) mysql -uUSERNAME -pPASSWORD
      - note that USERNAME and PASSWORD must be replaced with the actual credentials
    b) create database patient_data;
    c) show databases;
      - this can be run in order to ensure that the database was successfully created

- The first time when the project is run, the value for the property "spring.jpa.hibernate.ddl-auto" from the application.properties might have to be set to "create".
