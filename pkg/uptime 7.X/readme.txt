Oracle Instant Client ODBC Driver Installation Steps
=================================
This plugin relies on Oracle's Instant Client & ODBC drivers.
As such you will need to install these drivers on your monitoring station.
Below are steps for installing these drivers on both Windows & Linux based monitoring stations.

Windows
-------

1. Install the Oracle Instant Client drivers or have the Oracle Client installed on the monitoring station.
   To get the Instant Client Download for Oracle, download the 64 bit drive:
  
   http://www.oracle.com/technetwork/database/features/instant-client/index.html
   
   ie. instantclient-basic-windows.x64-12.1.0.1.0.zip 

2. Create a new directory C:\Oracle\. Unzip the downloaded file into the new directory. You should now have C:\Oracle\instantclient_12_1 which contains a bunch of .dll & .sym files.

3. Download the 'Instant Client Package - ODBC' from the same page above. ie. instantclient-odbc-windows.x64-12.1.0.1.0.zip . Extract this zip into the same C:\Oracle\instantclient_12_1 path.

4. Open a command prompt in the C:\Oracle\instantclient_12_1 directory and run the odbc_install.exe which will install the Oracle ODBC drivers and setup the required Environment variables.

5. Run the 'Data Sources (ODBC)' utility from the Windows 'Administrative Tools'. Click on the 'Drivers' tab, and confirm that you have an  'Oracle in instantclient' driver listed, and note the name of the driver, as this is required as the 'ODBC Driver Name' when setting up the service monitor. (Likely it will be 'Oracle in instantclient_12_1' or 'Oracle in OraClient12Home1'). 

6. Install the plugin and setup your monitors. Remember to change the 'ODBC Driver Name' value in the service monitor config to match the name of the driver as it shows up in the Data Sources (ODBC) utility. The curly Braces { } are important here.

If your having trouble with installing just the Oracle InstantClient & ODBC , another option is to install these drivers as part of the 'Oracle Data Access Components' which is a bundle of Oracle drivers full fleged installer compared to the zips mentioned above. This bundle can be found on the Oracle website here: http://www.oracle.com/technetwork/database/windows/downloads/index.html 

Linux
-----
(These steps refer to the .rpm versions of the various Oracle drivers, but they also provide .zip versions that can be installed on SUSE)

1. Install unixODBC via your package manager (ie. yum install unixodbc) . This will install unixODBC into your '/usr/lib64/' directory.

2. Download and Install the latest version of the 'Oracle Instant Client Basic' package, available from Oracle here( http://www.oracle.com/technetwork/database/features/instant-client/index.html). Keep in mind that up.time 7.2 comes bundled with 64bit Apache/PHP so you will need the Linux x86-64 package. ie. oracle-instantclient12.1-basic-12.1.0.1.0-1.x86_64.rpm . (The 12.1 package works with 11g Databases as well). This package will install some of the required binaries/drivers for Oracle into /usr/lib/oracle/<version>/client64/lib/

3.  Download and install the ' ODBC: Additional libraries' package from Oracle (ie. oracle-instantclient12.1-odbc-12.1.0.1.0-1.x86_64.rpm  ). This provides the actual libsqora.so ODBC Driver we need from Oracle.

4. Go into the directory where the Oracle ODBC drivers were installed and note the path for libsqora.so. (Likely it will be /usr/lib/oracle/12.1/client64/lib/libsqora.so.12.1 if you installed the 12_1 version of the instant client). This is required when setting up the service monitor.

5. Edit the oracle_tablespace_check.sh script that should be located in the <uptime_dir>/scripts/MonitorOracleTablespaceCheck/ directory and confirm that the LD_LIBRARY_PATH environment variable matches the location where the Oracle Drivers were  installed in step #2 above.


