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
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.DBNavigationBar.shared.Constants;

/**
 * The <code>RiPhoneDBHeadPosTemplate</code> class is the server
 * side implementation template for a simple database.
 * The same as DBHeadPosTemplate but without read and save
 * implementations.
 *
 * @param <E> DataBase structure type
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadPosTemplateRS<E extends DomainHeadPosDataBaseInt>
    extends DBTemplate<E> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 5037203911862183068L;

    /**
     * SQL string to update database.
     */
    private final String updateHeadSQL;

    /**
     * sql statment to read a position.
     */
    private final String readPosSQL;

    /**
     * sql statment to invalidate/delete a position.
     */
    private final String invalidatePosSQL;

    /**
     * sql statment to insert a position.
     */
    private final String insertPosSQL;

    /**
     * sql statment to update a position.
     */
    private final String updatePosSQL;

    /**
     * Constructor, set up database connection.
     *
     * @param setType - class instance of E
     * @param setLookUpDataBase
     *          Data source for lookup
     * @param setSessionUser
     *          name of the session where user data are saved
     * @param setDataBaseTableName
     *          data base table name
     * @param setKeyFieldName
     *          name of the key field in the database
     * @param setInsertHeadSQL
     *          insert sql statement
     * @param setUpdateHeadSQL
     *          update sql statement
     * @param setPosDataBaseTableName
     *          data base table name position
     * @param setPosKeyfieldName
     *          name of the key field in the database position
     * @param setInsertPosSQL
     *          insert sql statement position
     * @param setUpdatePosSQL
     *          update sql statement position
     */
    public DBHeadPosTemplateRS(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setUpdateHeadSQL,
            final String setPosDataBaseTableName,
            final String setPosKeyfieldName,
            final String setInsertPosSQL,
            final String setUpdatePosSQL
          ) {
        super(setType,
                setLookUpDataBase,
                setSessionUser,
                setDataBaseTableName,
                setKeyFieldName,
                setInsertHeadSQL,

                "SELECT MIN(" + setKeyFieldName + ") AS min, "
              + "       MAX(" + setKeyFieldName + ") AS max "
              + "FROM   " + setDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? ;",

                "SELECT MIN(" + setKeyFieldName + ") AS dbnumber "
              + "FROM   " + setDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
              + "  AND  " + setKeyFieldName + " > ? ;",

                "SELECT MAX(" + setKeyFieldName + ") AS dbnumber "
              + "FROM   " + setDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
              + "  AND  " + setKeyFieldName + " < ? ;",

                "SELECT * "
              + "FROM   " + setDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
              + "  AND  " + setKeyFieldName + " = ? ;",

                "DELETE FROM " + setDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
              + "  AND  " + setKeyFieldName + " = ? ;"
             );

        this.readPosSQL =
                "SELECT * "
              + "FROM   " + setPosDataBaseTableName + " "
              + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
              + "  AND  " + setKeyFieldName + " = ? ;";

        this.invalidatePosSQL        =
              "DELETE FROM " + setPosDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + setPosKeyfieldName + " = ? ;";

        this.updateHeadSQL       =    setUpdateHeadSQL;
        this.insertPosSQL        =    setInsertPosSQL;
        this.updatePosSQL        =    setUpdatePosSQL;
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

        String sqlString =
              "SELECT " + minMax + "(" + this.getKeyFieldName()
                        + ") AS dbnumber "
            + "FROM   " + this.getDataBaseTableName() + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = "
            + Integer.toString(mandator) + " "
            + " AND   " + this.getKeyFieldName() + " " + dbKeyVGL
            + " " + StringToSQL.convertString(dbKey,
                    thisDataBase.getMetaData().getDatabaseProductName()) + " "
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
     * getUpdateHeadSQL.
     * @return updateHeadSQL sql statement
     */
    public final String getUpdateHeadSQL() {
        return this.updateHeadSQL;
    }

    /**
     * <code>fillUpdateHead</code> fills the parameters of the
     * update prepared statement.
     * @param updateHeadSQLStatement
     *          update sql statement
     * @param mandator
     *          mandator number
     * @param user
     *          user name
     * @param saveEntry
     *          entry to save
     * @throws SQLException if fill up fails
     */
    protected abstract void fillUpdateHead(
            PreparedStatement updateHeadSQLStatement,
            int mandator,
            String user,
            E saveEntry) throws SQLException;


    /**
     * <code>fillUpdatePos</code> fills the parameters of the update
     * prepared statement.
     * @param updatePosSQLStatement
     *          update sql statement position
     * @param mandator
     *          mandator number
     * @param user
     *          user name
     * @param saveEntry
     *          entry to save from
     * @param posNumber
     *          position number to save
     * @throws SQLException when sql error occurs
     */
    protected abstract void fillUpdatePos(
            PreparedStatement updatePosSQLStatement,
            int mandator,
            String user,
            E saveEntry,
            int posNumber) throws SQLException;



    /**
     * <code>fillInsertPos</code> fills the parameters of the insert
     * prepared statement.
     * @param insertPosSQLStatement
     *         prepared statement for inserts
     * @param mandator
     *         mandator number
     * @param user
     *         user name
     * @param saveEntry
     *         entry to save
     * @param delete
     *         delete or not delete
     * @param posNumber
     *         number of the position to save
     * @throws SQLException when error occurs
     */
    protected abstract void fillInsertPos(
            final PreparedStatement insertPosSQLStatement,
            final int mandator,
            final String user,
            final E saveEntry,
            final boolean delete,
            final int posNumber) throws SQLException;

    /**
     * <code>fillPosFromResultSet</code> set the fields in thisEntry from
     * the given resultSet.
     * @param resultPos
     *             ResultSet to read Entries from
     * @param thisEntry
     *             Entry to fill
     * @return filled Entry
     * @throws Exception when error occurs
     */
    protected abstract E fillPosFromResultSet(ResultSet resultPos,
            E thisEntry) throws Exception;

    /**
     * <code>insertPositionEntry</code> inserts a position into the database.
     * @param thisDataBase
     *          database connection
     * @param mandator
     *          mandator number
     * @param user
     *          user name
     * @param saveEntry
     *          entry to save
     * @param delete
     *          delete old entries
     * @param posNumber
     *          position number
     * @return effected database entries (should always be 1)
     * @throws SQLException when error occurs
     */
    protected final int insertPositionEntry(
            final Connection thisDataBase,
            final int mandator,
            final String user, final E saveEntry,
            final boolean delete, final int posNumber
           ) throws SQLException {
        int num = -1;
        PreparedStatement insertPosSQLStatement        =    null;
        try {
            insertPosSQLStatement = thisDataBase.prepareStatement(
                    this.insertPosSQL);
            insertPosSQLStatement.clearParameters();
            this.fillInsertPos(insertPosSQLStatement, mandator, user,
                    saveEntry, delete, posNumber);
            num =  insertPosSQLStatement.executeUpdate();
        } finally {
            if (insertPosSQLStatement != null) {
                insertPosSQLStatement.close();
            }
        }
        return num;
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
            PreparedStatement updateHeadSQLStatement     = null;
            PreparedStatement invalidatePosSQLStatement  = null;
            PreparedStatement updatePosSQLStatement      = null;

            try {
                int numPos = 0;
                if (currentEntry.getKeyPos() != null) {
                    numPos = currentEntry.getKeyPos().length;
                }
                int numDbPos = 0;
                if (dbEntry != null && dbEntry.getKeyPos() != null) {
                    numDbPos = dbEntry.getKeyPos().length;
                }
                if ((dbEntry == null) || (dbEntry.getKeyCur() == null)) {
                    // new Entry, insert a new one
                    this.insertEntry(thisDataBase, mandator, user,
                            currentEntry, false);
                    for (int i = 0; i < numPos; i++) {
                        this.insertPositionEntry(thisDataBase, mandator,
                                user, currentEntry, false, i);
                    }
                } else {
                    // Entry already exists, update it, if necessary
                    if (!currentEntry.equals(dbEntry)) {
                        if (!currentEntry.equalsEntry(dbEntry)
                         && (this.updateHeadSQL != null)) {
                            // Invalidate old entry
                            updateHeadSQLStatement =
                                    thisDataBase.prepareStatement(
                                            this.updateHeadSQL);
                            updateHeadSQLStatement.clearParameters();
                            this.fillUpdateHead(updateHeadSQLStatement,
                                    mandator, user, currentEntry);
                            updateHeadSQLStatement.executeUpdate();

                            currentEntry.setKeyCur(saveKeyString);
                        }

                        // Positions
                        invalidatePosSQLStatement =
                                thisDataBase.prepareStatement(
                                        this.getInvalidatePosSQL());
                        updatePosSQLStatement     =
                                thisDataBase.prepareStatement(
                                        this.updatePosSQL);
                        // Take a look if position differ and invalidate old
                        for (int i = 0; i < numDbPos; i++) {
                            boolean isremoved    =    true;
                            for (int j = 0; j < numPos && isremoved; j++) {
                                if (dbEntry.getKeyPos()[i].equals(
                                        currentEntry.getKeyPos()[j])) {
                                    isremoved    =    false;
                                }
                            }
                            if (isremoved) {
                                int sqlPos = 1;
                                invalidatePosSQLStatement
                                  .clearParameters();
                                invalidatePosSQLStatement
                                  .setInt(sqlPos++, mandator);
                                invalidatePosSQLStatement
                                  .setString(sqlPos++, saveKeyString);
                                invalidatePosSQLStatement
                                  .setString(sqlPos++, dbEntry.getKeyPos()[i]);
                                invalidatePosSQLStatement
                                  .executeUpdate();
                                this.insertPositionEntry(thisDataBase,
                                        mandator, user, dbEntry, true, i);
                                // Invalidate old entry
                            }
                        }
                        // Take a look if position differ, update them und
                        // insert new
                        for (int i = 0; i < numPos; i++) {
                            boolean isnew    =    true;
                            for (int j = 0; j < numDbPos && isnew; j++) {
                                if (dbEntry.getKeyPos()[j].equals(
                                        currentEntry.getKeyPos()[i])) {
                                    isnew    =    false;
                                    // Entry already exists, look for
                                    // changes
                                    if (!currentEntry.equalsPosition(
                                            dbEntry, i, j)) {
                                        // Invalidate old entry
                                        updatePosSQLStatement
                                          .clearParameters();
                                        this.fillUpdatePos(
                                                updatePosSQLStatement,
                                                mandator, user,
                                                currentEntry, i);
                                        updatePosSQLStatement
                                          .executeUpdate();
                                    }
                                }
                            }
                            if (isnew) {
                                // Insert new position
                                this.insertPositionEntry(thisDataBase,
                                        mandator, user, currentEntry,
                                        false, i);
                            }
                        }
                    }
                }
            } finally {
                if (updateHeadSQLStatement != null) {
                    updateHeadSQLStatement.close();
                }
                if (invalidatePosSQLStatement != null) {
                    invalidatePosSQLStatement.close();
                }
                if (updatePosSQLStatement != null) {
                    updatePosSQLStatement.close();
                }
            }
        }
    }


    /**
     * get invalidate pos sql string.
     * @return invalidatePosSQL
     */
    public final String getInvalidatePosSQL() {
        return invalidatePosSQL;
    }

    /**
     * get read pos sql string.
     * @return readPosSQL
     */
    public final String getReadPosSQL() {
        return readPosSQL;
    }
}
