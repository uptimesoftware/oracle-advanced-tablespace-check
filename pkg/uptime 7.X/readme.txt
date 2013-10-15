Oracle Client Driver Installation
=================================
Steps that need to be performed before the plugin is installed:

Windows
-------

1. Install the Oracle Instant Client drivers or have the Oracle Client installed on the monitoring station.
   To get the Instant Client Download for Oracle, download the 64 bit drive:
  
   http://www.oracle.com/technetwork/database/features/instant-client/index-097480.html

2. Create a new directory, for example, C:\instantclient_11_2. Unzip the downloaded file into the new directory.

3. Edit the Windows environment and add the location of the Oracle Instant Client files, C:\instantclient_11_2, 
   to the PATH environment variable, before any other Oracle  directories. For example, on Windows XP, 
   go to Start -> Settings -> Control Panel -> System -> Advanced -> Environment Variables, and edit PATH in the 
   System Variables list. Reboot to make this take effect. 
   
4. Uncomment/add "extension=php_oci8_11g.dll" to php.ini

5. Restart the server to have the environment variables update as well this will update the apache settings

6. Install the plugin and setup your monitors.

Linux
-----

1. Install the Oracle Instant Client drivers or have the Oracle Client installed on the monitoring station.
   To get the Instant Client Download for Oracle remember we use 64 bit apache and php so get the 64 bit driver:

   http://www.oracle.com/technetwork/database/features/instant-client/index-097480.html

2. Run rpm -i oracle-instantclient11.2-basic-11.2.0.3.0-1.x86_64.rpm 

3. Create "/etc/ld.so.conf.d/oracle.conf" with the path to the client files of 
   "/usr/lib/oracle/11.2/client64/lib/" (default location)
   
   Then run “ldconfig”

4. Install OCI8.so and add "extension=[path_to_OCI8.so]/oci8.so" to php.ini.  The RPM can be 
   downloaded here:

       https://oss.oracle.com/projects/php/files/EL5/x86_64/

   The filename is similar to php54-oci8-11gR2-5.4.7-1.el5.x86_64.rpm

5. Restart the web server to have the apache settings and environment updates applied.

6. Install the plugin and setup your monitors.
