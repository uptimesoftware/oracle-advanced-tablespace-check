package com.uptimesoftware.uptime.erdc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uptimesoftware.uptime.base.util.Imploder;
import com.uptimesoftware.uptime.base.util.Parameters;
import com.uptimesoftware.uptime.erdc.baseclass.OracleQueryingMonitor;
import com.uptimesoftware.uptime.erdc.database.RemoteConnection;
import com.uptimesoftware.uptime.erdc.database.RemoteLoader;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MonitorOracleTablespaceAdv extends OracleQueryingMonitor {

        private int warnThreshold;
        private int critThreshold;
        private Boolean ignoreAutoExt;

        @Override
        public void setParameters(Parameters params, Long instanceId) {
                super.setParameters(params, instanceId);

                warnThreshold = checkRequiredInteger("warn");
                critThreshold = checkRequiredInteger("crit");
                if (checkRequired("ignoreautoext").equalsIgnoreCase("true")) {
                    ignoreAutoExt = true;
                }
                else {
                    ignoreAutoExt = false;
                }
        }

        @Override
        protected void doWork(RemoteConnection conn) throws SQLException {
//            try {
                List<Tablespace> tablespaces = getTablespaces(conn);

                List<String> criticalTablespaces = new ArrayList<String>();
                List<String> warningTablespaces = new ArrayList<String>();

                for (Tablespace tablespace : tablespaces) {
                        int percentUsed = tablespace.percentUsed;

                        String message = tablespace.name + " " + percentUsed + "%";
                        if (isCrit(percentUsed)) {
                                criticalTablespaces.add(message);
                        } else if (isWarn(percentUsed)) {
                                warningTablespaces.add(message);
                        }
                }
                updateStateAndMessage(criticalTablespaces, warningTablespaces);
/*            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                setMessage(e.getMessage() + ": " + sw.toString());
            }

*/
        }

        private boolean isWarn(int percentUsed) {
                return percentUsed >= warnThreshold;
        }

        private boolean isCrit(int percentUsed) {
                return percentUsed >= critThreshold;
        }

        @SuppressWarnings("PMD.ConfusingTernary")
        private void updateStateAndMessage(List<String> criticalTablespaces, List<String> warningTablespaces) {
                Boolean alert = false;
                String msg = "";
                if (!warningTablespaces.isEmpty()) {
                    msg = getTablespaceMessage("WARN", warningTablespaces);
                    setState(ErdcTransientState.WARN);
                    alert = true;
                }
                if (!criticalTablespaces.isEmpty()) {
                    msg = getTablespaceMessage("CRIT", criticalTablespaces) + " " + msg;
                    setState(ErdcTransientState.CRIT);
                    alert = true;
                }
                
                if (alert) {
                    setMessage(msg);
                }
                else {
                    setMessage("All tablespaces are with in thresholds");
                }
        }

        private String getTablespaceMessage(String state, List<String> tablespaces) {
                String offenders = Imploder.implode(", ", tablespaces);
                return state + " - " + offenders;
        }

        private List<Tablespace> getTablespaces(RemoteConnection connection) {
                TablespaceLoader loader = new TablespaceLoader(connection);
                return loader.loadRemotely();
        }

        private class TablespaceLoader extends RemoteLoader<List<Tablespace>> {

                public TablespaceLoader(RemoteConnection remoteConnection) {
                        super(remoteConnection);
                }

                @Override
                protected String getRemoteSql() {
/*                        return "SELECT df.tablespace_name, SUM (df.BYTES) total, MAX(fs.total_bytes) free"
                                        + " FROM SYS.dba_data_files df, SYS.dba_free_space_coalesced fs "
                                        + " WHERE df.tablespace_name = fs.tablespace_name" + " GROUP BY df.tablespace_name"
                                        + " ORDER BY df.tablespace_name";
 */
                    return "select df.TABLESPACE_NAME name," +
                            " df.BYTES total," +
                            " (fs.BYTES) free," +
                            " df.AUTOEXTENSIBLE autoext" +
                            " from (" +
                                " select TABLESPACE_NAME," +
                                " sum(BYTES) BYTES," +
                                " AUTOEXTENSIBLE," +
                                " decode(AUTOEXTENSIBLE, 'YES', sum(MAXBYTES), sum(BYTES)) MAXBYTES" +
                                " from SYS.dba_data_files" +
                                " group by TABLESPACE_NAME," +
                                " AUTOEXTENSIBLE" +
                            " ) df, (" +
                                " select TABLESPACE_NAME," +
                                " sum(BYTES) BYTES" +
                                " from SYS.dba_free_space" +
                                " group by TABLESPACE_NAME" +
                            " ) fs" +
                            " where df.TABLESPACE_NAME = fs.TABLESPACE_NAME" +
                            " order by df.TABLESPACE_NAME asc";

                }

                @Override
                protected List<Tablespace> extractResultFrom(ResultSet rs) throws SQLException {
                        List<Tablespace> tablespaces = new ArrayList<Tablespace>();
                        while (rs.next()) {
                                String name = rs.getString("name");
                                long total = rs.getLong("total");
                                long free = rs.getLong("free");
                                // Start of changes
                                String autoext = rs.getString("autoext");
                                // skip auto-extendable tablespaces
                                if (ignoreAutoExt) {
                                    // get only tablespaces that do not have auto-extend
                                    if (autoext.equalsIgnoreCase("NO")) {
                                        Tablespace tablespace = new Tablespace(name, total, free);
                                        tablespaces.add(tablespace);
                                    }
                                }
                                else {
                                    // get all tablespaces
                                    Tablespace tablespace = new Tablespace(name, total, free);
                                    tablespaces.add(tablespace);
                                }
                                // End of changes
                        }
                        return tablespaces;
                }

        }

        private class Tablespace {
                public final String name;
                public final int percentUsed;

                public Tablespace(String name, long totalBytes, long freeBytes) {
                        super();
                        this.name = name;
                        percentUsed = getPercentUsed(totalBytes, freeBytes);
                }

                private int getPercentUsed(long totalBytes, long freeBytes) {
                        if (totalBytes == 0) {
                                return 0;
                        }
                        long used = totalBytes - freeBytes;
                        return (int) (100 * used / totalBytes);
                }
        }

}
