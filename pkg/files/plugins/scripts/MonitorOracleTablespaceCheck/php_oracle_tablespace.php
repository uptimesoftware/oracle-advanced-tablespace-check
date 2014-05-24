<?php
$odbc_driver = getenv('UPTIME_ODBC_DRIVER');
$oracle_db_hostname = getenv('UPTIME_HOSTNAME');
$oracle_db_port = getenv('UPTIME_DB_PORT');
$oracle_db_name = getenv('UPTIME_DB_NAME');
$oracle_db_user     = getenv('UPTIME_USERNAME');
$oracle_db_password = getenv('UPTIME_PASSWORD');

//Build a Connection string for the 'Oracle Instant Client Driver'
$odbc_connection_string = "Driver=" . $odbc_driver . ";Dbq=" . $oracle_db_hostname . ":" . $oracle_db_port . "/" . $oracle_db_name;
$c = odbc_connect($odbc_connection_string, $oracle_db_user, $oracle_db_password);
if (!$c) {
	//trigger_error('Could not connect to database: '. odbc_errormsg());
	echo 'Could not connect to database: '. odbc_errormsg();
	exit(2);
}

$s = odbc_exec($c, "select nvl(b.tablespace_name, nvl(a.tablespace_name,'UNKOWN')) Tablespace_Name,
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
	//trigger_error('Could not execute statement: '. odbc_errormsg());
	echo 'Could not execute statement: '. odbc_errormsg();
	exit(2);
}

while (odbc_fetch_row($s)) {
	echo odbc_result($s, "Tablespace_name").".available ".odbc_result($s, "AVAILABLE_KB") . "\n";
	echo odbc_result($s, "TABLESPACE_NAME").".used ".odbc_result($s, "USED_KB") . "\n";
	echo odbc_result($s, "TABLESPACE_NAME").".free ".odbc_result($s, "FREE_KB") . "\n";
	echo odbc_result($s, "TABLESPACE_NAME").".percent ".trim(odbc_result($s, "PCT_USED"), '%'). "\n";
}
?>
