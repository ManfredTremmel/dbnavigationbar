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
 *  Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBaseInterface;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;
import de.knightsoft.DBNavigationBar.shared.Constants;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side
 * implementation template for a simple database.
 *
 * @param <E> structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadDateTemplateR<E extends DomainHeadDataBaseInterface>
    extends DBTemplate<E> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 3633734786925668260L;

    /**
     * Constructor, set up database connection.
     *
     * @param setType - class instance of E
     * @param setLookUpDataBase
     *          look up of the data base
     * @param setSessionUser
     *          user session key
     * @param setDataBaseTableName
     *          database table name
     * @param setKeyFieldName
     *          key field of the database
     * @param setInsertHeadSQL
     *          sql statement to insert a new head entry
     * @param setReadMinMaxSQL
     *          sql statement for min/max read
     * @param setReadNextSQL
     *          sql statement to read next key
     * @param setReadPrevSQL
     *          sql statement to read previous key
     * @param setReadHeadSQL
     *          sql statement to read head entry
     * @param setInvalidateHeadSQL
     *          sql statement to invalidate head entry
     */
    public DBHeadDateTemplateR(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setReadMinMaxSQL,
            final String setReadNextSQL,
            final String setReadPrevSQL,
            final String setReadHeadSQL,
            final String setInvalidateHeadSQL
          ) {
        super(setType,
              setLookUpDataBase,
              setSessionUser,
              setDataBaseTableName,
              setKeyFieldName,
              setInsertHeadSQL,
              setReadMinMaxSQL,
              setReadNextSQL,
              setReadPrevSQL,
              setReadHeadSQL,
              setInvalidateHeadSQL);
    }

    /**
     * Constructor, set up database connection.
     *
     * @param setType - class instance of E
     * @param setLookUpDataBase
     *          look up of the data base
     * @param setSessionUser
     *          user session key
     * @param setDataBaseTableName
     *          database table name
     * @param setKeyFieldName
     *          key field of the database
     * @param setInsertHeadSQL
     *          sql statement to insert a new head entry
     */
    public DBHeadDateTemplateR(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL
          ) {
        this(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL,

               "SELECT MIN(" + setKeyFieldName + ") AS min, "
             + "       MAX(" + setKeyFieldName + ") AS max "
             + "FROM   " + setDataBaseTableName + " "
             + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

               "SELECT MIN(" + setKeyFieldName + ") AS dbnumber "
             + "FROM   " + setDataBaseTableName + " "
             + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
             + "  AND  " + setKeyFieldName + " > ? "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

               "SELECT MAX(" + setKeyFieldName + ") AS dbnumber "
             + "FROM   " + setDataBaseTableName + " "
             + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
             + "  AND  " + setKeyFieldName + " < ? "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

               "SELECT * "
             + "FROM   " + setDataBaseTableName + " "
             + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
             + "  AND  " + setKeyFieldName + " = ? "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

               "UPDATE " + setDataBaseTableName + " "
             + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() "
             + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
             + "  AND  " + setKeyFieldName + " = ? "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
             + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();"

            );
    }

    /**
     * <code>searchSQLSelect</code> setup a part of the
     * sql statement to search for a user.
     *
     * @param thisDataBase
     *             Database connection
     * @param minMax
     *             "MIN" or "MAX" used for PhoneNumber
     * @param searchField
     *             Field to search for
     * @param searchMethodeEntry
     *             compare method for search
     *             ("<", "<=", "=", ">", ">=" or "like")
     * @param searchFieldEntry
     *             value to search for
     * @param dbKeyVGL
     *             compare method of phone number
     *             ("<", "<=", "=", ">", ">=" or "like")
     * @param dbKey
     *             comparison number
     * @return SQL-String
     * @throws Exception when error occurs
     */
    @Override
    protected final String searchSQLSelect(
            final Connection thisDataBase,
            final String minMax,
            final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry,
            final String dbKeyVGL,
            final String dbKey) throws Exception {
        int mandator         =    this.getUser().getMandator();
        DataBaseDepending myDataBaseDepending =
                new DataBaseDepending(thisDataBase.getMetaData()
                        .getDatabaseProductName());

        String sqlString =
              "SELECT " + minMax + "(" + this.getKeyFieldName()
                        + ") AS dbnumber "
            + "FROM   " + this.getDataBaseTableName() + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = "
            + Integer.toString(mandator) + " "
            + " AND   " + this.getKeyFieldName() + " " + dbKeyVGL
            + " " + StringToSQL.convertString(dbKey,
                    thisDataBase.getMetaData().getDatabaseProductName()) + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                    + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > "
                    + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND   ";

        if ("=".equals(searchMethodeEntry)) {
            sqlString += StringToSQL.searchString(searchField,
                    searchFieldEntry, thisDataBase.getMetaData()
                    .getDatabaseProductName());
        } else if ("like".equals(searchMethodeEntry)) {
            sqlString += StringToSQL.searchString(searchField,
                    "*" + searchFieldEntry + "*", thisDataBase
                    .getMetaData().getDatabaseProductName());
        } else {
            sqlString += searchField + " " + searchMethodeEntry
                    + " " +  StringToSQL.convertString(searchFieldEntry,
                       thisDataBase.getMetaData().getDatabaseProductName());
        }
        return sqlString;
    }

    /**
     * <code>deleteEntry</code> deletes one entry from database.
     *
     * @param currentEntry
     *         key of the current entry
     * @return entry to display after deletion
     */
    @Override
    public final E deleteEntry(final String currentEntry) {
        E resultValue = null;
        DomainUser thisUser  =    this.getUser();
        if (thisUser != null) {
            int mandator     =    thisUser.getMandator();
            String user      =    thisUser.getUser();
            Connection thisDataBase        =    null;
            PreparedStatement invalidateHeadSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.getLookUpDataBase());
                thisDataBase = lDataSource.getConnection();
                ic.close();

                if (allowedToChange()) {
                    E dbEntry = this.readEntry(currentEntry);
                    // invalidate head number
                    invalidateHeadSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.getInvalidateHeadSQL());
                    invalidateHeadSQLStatement.clearParameters();
                    invalidateHeadSQLStatement.setInt(1, mandator);
                    invalidateHeadSQLStatement.setString(2,
                            currentEntry);
                    invalidateHeadSQLStatement.executeUpdate();
                    this.insertEntry(thisDataBase, mandator,
                            user, dbEntry, true);
                }
                resultValue    = readNextEntry(currentEntry);
            } catch (SQLException e) {
                resultValue    =    null;
            } catch (NamingException e) {
                resultValue    =    null;
            } finally {
                try {
                    if (invalidateHeadSQLStatement != null) {
                        invalidateHeadSQLStatement.close();
                    }
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultValue;
    }

    /**
     * <code>saveEntry</code> saves or inserts a
     * entry to database.
     *
     * @param currentEntry
     *             entry that has to be saved
     * @param dbEntry
     *             entry from database to compare
     * @param thisDataBase
     *             database connection
     * @param mandator
     *             mandator number
     * @param user
     *             name of the user
     * @param saveKeyString
     *             key of the entry to save
     * @throws SQLException if something's going wrong
     */
    @Override
    protected final void saveEntry(
            final E currentEntry,
            final E dbEntry,
            final Connection thisDataBase,
            final int mandator,
            final String user,
            final String saveKeyString
           ) throws SQLException {
        // Entry already exists in Database?
        if (!currentEntry.equals(dbEntry)) {
            PreparedStatement invalidateHeadSQLStatement = null;

            try {
                // Invalidate old entry
                invalidateHeadSQLStatement =
                        thisDataBase.prepareStatement(
                                this.getInvalidateHeadSQL());
                invalidateHeadSQLStatement
                        .clearParameters();
                invalidateHeadSQLStatement
                        .setInt(1, mandator);
                invalidateHeadSQLStatement
                        .setString(2, saveKeyString);
                invalidateHeadSQLStatement
                        .executeUpdate();
            } finally {
                if (invalidateHeadSQLStatement != null) {
                    invalidateHeadSQLStatement.close();
                }
            }

            // Insert new entry
            this.insertEntry(thisDataBase,
                    mandator, user, currentEntry,
                    false);
        }
    }

}
