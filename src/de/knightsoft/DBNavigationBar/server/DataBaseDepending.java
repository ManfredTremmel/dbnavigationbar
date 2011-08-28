/**
 * This file is part of DBNavigationBar.
 *
 * RiPhone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import de.knightsoft.DBNavigationBar.shared.Constants;



/**
 *
 * The <code>DataBaseDepending</code> class contains database specific
 * SQL strings. It supports MySQL and MSSQL at the moment
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-01-02
 */
public class DataBaseDepending {

    /**
     * database number.
     */
    private final int dbnumber;

    /**
     * JDBC classes.
     */
    protected static final String[][] JDBC_CLASS =
        {
            {"MySQL", Constants.JDBC_CLASS_MYSQL,
                Constants.JDBC_CLASS_MYSQL_OLD},
            {"MSSQL", Constants.JDBC_CLASS_MSSQL}
        };

    /**
     * SQL statement to get current time.
     */
    protected static final String[] SQL_TIME_NOW =
        {
            "NOW()",
            "GETDATE()"
        };

    /**
     * SQL statement to get current time minus one second.
     */
    protected static final String[] SQL_TIME_OUTDATE =
        {
            "(NOW() - INTERVAL 1 SECOND)",
            "DATEADD(second, -1, GETDATE())"
        };

    /**
     * Constructor.
     *
     * @param jdbcClassToUse
     *            JDBC driver class to use
     * @exception Exception if the JDBCClassToUse is not supported
     */
    public DataBaseDepending(final String jdbcClassToUse
           ) throws Exception {
        int tmpDBnumber    =    -1;
        String myJDBCClassToUse = jdbcClassToUse;
        if (Constants.JDBC_CLASS_MYSQL_OLD.equalsIgnoreCase(
                myJDBCClassToUse)) {
            myJDBCClassToUse    =    Constants.JDBC_CLASS_MYSQL;
        }
        for (int i = 0; i < JDBC_CLASS.length && tmpDBnumber == -1; i++) {
            for (int j = 0; j < JDBC_CLASS[i].length
                    && tmpDBnumber == -1; j++) {
                if (myJDBCClassToUse.equals(JDBC_CLASS[i][j])) {
                    tmpDBnumber    =    i;
                }
            }
        }
        if (tmpDBnumber == -1) {
            throw new Exception("This JDBC class is not Supported");
        }
        this.dbnumber    =    tmpDBnumber;
    }

    /**
     * The <code>getJDBCClass</code> class returns the name of the JDBC Class.
     *
     * @return name of the JDBC Class
     */
    public final String getJDBCClass() {
        return JDBC_CLASS[this.dbnumber][1];
    }

    /**
     * The <code>getSQLTimeNow</code> class returns the SQL function used
     * to get current date/time for the currently used database.
     *
     * @return SQL function
     */
    public final String getSQLTimeNow() {
        return SQL_TIME_NOW[this.dbnumber];
    }

    /**
     * The <code>getSQLTimeOutdate</code> class returns SQL function to get
     * current date/time - one second to outdate entries.
     *
     * @return SQL function
     */
    public final String getSQLTimeOutdate() {
        return SQL_TIME_OUTDATE[this.dbnumber];
    }

    /**
     * The <code>getSQLDiffFromNow</code> class returns SQL function to get
     * the difference from a given date/time to now in days.
     *
     * @param compareField
     *            field to compare with current date
     * @return SQL function
     */
    public final String getSQLDiffFromNow(final String compareField) {
        String returnString    =    null;
        switch(this.dbnumber) {
            case 0:
                returnString =
                    "TO_DAYS(NOW()) - TO_DAYS(" + compareField + ")";
                break;
            default:
                returnString =
                    "DATEDIFF (day, " + compareField + ", GETDATE())";
                break;
        }
        return returnString;
    };

    /**
     * The <code>getSQLDiffFromNow</code> class returns SQL function to
     * encrypt passwords if database can do, otherwise fieldname is unchanged.
     *
     * @param encryptField
     *            field to encrypt
     * @return SQL function
     */
    public final String getSQLPassword(final String encryptField) {
        String returnString    =    null;
        switch (this.dbnumber) {
            case 0:
                returnString = "PASSWORD(" + encryptField + ")";
                break;
            default:
                returnString = encryptField;
                break;
        }
        return returnString;
    };

    /**
     * The <code>concatStrings</code> class returns SQL function to
     * concatenate strings.
     *
     * @param stringTab
     *            Table of Strings to concatenate
     * @return SQL function
     */
    public final String concatStrings(final String[] stringTab) {
        StringBuilder returnString    =    new StringBuilder();
        int i;
        switch (this.dbnumber) {
            case 0:
                returnString.append("CONCAT(");
                for (i = 0; i < stringTab.length; i++) {
                    if (i > 0) {
                        returnString.append(", ");
                    }
                    returnString.append(stringTab[i]);
                }
                returnString.append(')');
                break;
            case 1:
                returnString.append('(');
                for (i = 0; i < stringTab.length; i++) {
                    if (i > 0) {
                        returnString.append(" + ");
                    }
                    returnString.append(stringTab[i]);
                }
                returnString.append(')');
                break;
            default:
                returnString.append('(');
                for (i = 0; i < stringTab.length; i++) {
                    if (i > 0) {
                        returnString.append(" || ");
                    }
                    returnString.append(stringTab[i]);
                }
                returnString.append(')');
                break;
        }
        return returnString.toString();
    };

    /**
     * The <code>getSQLBoolean</code> class returns SQL entry for boolean
     * fields.
     *
     * @param booleanField
     *            field to transform
     * @return SQL field
     */
    public final String getSQLBoolean(final boolean booleanField) {
        String returnString    =    null;
        switch(this.dbnumber) {
            case 0:
                if (booleanField) {
                    returnString =  "'1'";
                } else {
                    returnString =  "'0'";
                }
                break;
            default:
                if (booleanField) {
                    returnString =  "1";
                } else {
                    returnString =  "0";
                }
                break;
        }
        return returnString;
    }

    /**
     * @return the dbnumber.
     */
    public final int getDbnumber() {
        return this.dbnumber;
    };
}
