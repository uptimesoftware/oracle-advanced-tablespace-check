@echo off

REM echo rexec %UPTIME_PASSWORD% %UPTIME_REMOTE_SCRIPT% %UPTIME_POOL% | nc %UPTIME_HOSTNAME% %UPTIME_PORT%

..\..\apache\php\php.exe ..\..\plugins\scripts\MonitorOracleTablespaceCheck\php_oracle_tablespace.php
