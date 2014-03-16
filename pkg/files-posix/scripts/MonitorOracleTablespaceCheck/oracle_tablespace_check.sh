#!/bin/sh


UPTIMEDIR=`grep pidfile /etc/init.d/uptime_core | head -n 1 | cut -d: -f2 |  cut -c 2- | rev | cut -c 12- | rev`



MIBDIRS=$UPTIMEDIR/mibs
export MIBDIRS


#this needs to be set to the path for where Oracle Instant Client Drivers are installed. 
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/oracle/12.1/client64/lib/





/usr/local/uptime/apache/bin/php php_oracle_tablespace.php
