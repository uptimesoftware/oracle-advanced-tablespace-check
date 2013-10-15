<?php
$agent_hostname = getenv('UPTIME_HOSTNAME');
$servicename  = getenv('UPTIME_SERVICENAME');
$oracle_user     = getenv('UPTIME_USERNAME');
$oracle_password = getenv('UPTIME_PASSWORD');

$dbname = $agent_hostname."/".$servicename;

$c = oci_connect($oracle_user, $oracle_password, $dbname);
 if (!$c) {
$e = oci_error();
 trigger_error('Could not connect to database: '. $e['message'],E_USER_ERROR);
}
 $s = oci_parse($c, "select nvl(b.tablespace_name, nvl(a.tablespace_name,'UNKOWN')) Tablespace_Name, 
kbytes_alloc Available_KB, 
kbytes_alloc-nvl(kbytes_free,0) Used_KB, 
nvl(kbytes_free,0) Free_KB, 
to_char(((kbytes_alloc-nvl(kbytes_free,0)) / kbytes_alloc)*100,999.99)||'%' PCT_used 
from 
(select sum(bytes)/1024 Kbytes_free,tablespace_name from sys.dba_free_space group by tablespace_name) a, 
(select sum(bytes)/1024 Kbytes_alloc, tablespace_name from sys.dba_data_files group by tablespace_name 
union all 
select sum(bytes)/1024 Kbytes_alloc, tablespace_name from sys.dba_temp_files group by tablespace_name )b 
where a.tablespace_name (+) = b.tablespace_name order by Tablespace_Name");
 if (!$s) {
$e = oci_error($c);
 trigger_error('Could not parse statement: '. $e['message'], E_USER_ERROR);
}
$r = oci_execute($s);
if (!$r) {
$e = oci_error($s);
 trigger_error('Could not execute statement: '. $e['message'], E_USER_ERROR);
}
 while (($row = oci_fetch_array($s, OCI_ASSOC+OCI_RETURN_NULLS)) != false) {
 echo $row["TABLESPACE_NAME"].".available ".$row["AVAILABLE_KB"] . "\n";
 echo $row["TABLESPACE_NAME"].".used ".$row["USED_KB"] . "\n";
 echo $row["TABLESPACE_NAME"].".free ".$row["FREE_KB"] . "\n";
 echo $row["TABLESPACE_NAME"].".percent ".trim($row["PCT_USED"], '%'). "\n";
}
?>