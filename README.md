# Oracle Advanced Tablespace Check

See http://uptimesoftware.github.io for more information.

### Tags 
 plugin   database   oracle  

### Category

{ page.category }}

### Version Compatibility


  
    * Oracle Advanced Tablespace Check 3.0 - 7.1, 7.0
  

  
    * Oracle Advanced Tablespace Check 2.0 - 6.0
  


### Description
Allows for better monitoring of Oracle tablespaces.


### Supported Monitoring Stations

7.2, 7.1

### Supported Agents
None; no agent required

### Installation Notes
<p><a href="https://github.com/uptimesoftware/uptime-plugin-manager">Install using the up.time Plugin Manager</a></p>

<h1>Oracle Client Driver Installation</h1>

<p>Steps that need to be performed before the plugin is installed:</p>

<h2>Windows</h2>

<ol>
<li>Install the Oracle Instant Client drivers or have the Oracle Client installed on the monitoring station. To get the Instant Client Download for Oracle, download the 64 bit drive:</li>
</ol>


<p>http://www.oracle.com/technetwork/database/features/instant-client/index-097480.html</p>

<ol>
<li><p>Create a new directory, for example, C:\instantclient_11_2. Unzip the downloaded file into the new directory.</p></li>
<li><p>Edit the Windows environment and add the location of the Oracle Instant Client files, C:\instantclient_11_2, to the PATH environment variable, before any other Oracle directories. For example, on Windows XP, go to Start -> Settings -> Control Panel -> System -> Advanced -> Environment Variables, and edit PATH in the System Variables list. Reboot to make this take effect.</p></li>
<li><p>Uncomment/add "extension=php_oci8_11g.dll" to php.ini</p></li>
<li><p>Restart the server to have the environment variables update as well this will update the apache settings</p></li>
<li><p>Install the plugin and setup your monitors.</p></li>
</ol>


<h2>Linux</h2>

<ol>
<li>Install the Oracle Instant Client drivers or have the Oracle Client installed on the monitoring station. To get the Instant Client Download for Oracle remember we use 64 bit apache and php so get the 64 bit driver:</li>
</ol>


<p>http://www.oracle.com/technetwork/database/features/instant-client/index-097480.html</p>

<ol>
<li><p>Run rpm -i oracle-instantclient11.2-basic-11.2.0.3.0-1.x86_64.rpm</p></li>
<li><p>Create "/etc/ld.so.conf.d/oracle.conf" with the path to the client files of "/usr/lib/oracle/11.2/client64/lib/" (default location)</p></li>
</ol>


<p>Then run “ldconfig”</p>

<ol>
<li>Install OCI8.so and add "extension=[path_to_OCI8.so]/oci8.so" to php.ini. The RPM can be downloaded here:</li>
</ol>


<p>https://oss.oracle.com/projects/php/files/EL5/x86_64/</p>

<p>The filename is similar to php54-oci8-11gR2-5.4.7-1.el5.x86_64.rpm, depending on the version numbers.</p>

<ol>
<li><p>Restart the web server to have the apache settings and environment updates applied.</p></li>
<li><p>Install the plugin and setup your monitors.</p></li>
</ol>



### Dependencies
<p>n/a</p>


### Input Variables
* Username; username to be used to connect to the HMC* Password (Windows only); password to be used to connect to the HMC

### Output Variables

* Chassis CPU Utilization (%)* Chassis CPU Utilization (CPU unit)* LPAR CPU Utilization (%)* LPAR CPU Utilization (CPU unit)* Response Time (ms)

### Languages Used
* PHP

