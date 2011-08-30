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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;
import de.knightsoft.DBNavigationBar.shared.Constants;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server
 * side implementation.
 * template for a simple database
 *
 * @param <E> Structure of the database
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadPosDateTemplateR<E extends DomainHeadPosDataBaseInt>
    extends DBTemplate<E> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -4140110717360730992L;

    /**
     * read a position.
     */
    private final String readPosSQL;
    /**
     * invalidate a position.
     */
    private final String invalidatePosSQL;
    /**
     * insert a position.
     */
    private final String insertPosSQL;

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
     * @param setPosDataBaseTableName
     *          database table name (position)
     * @param setPosKeyFieldName
     *          key field of the database (position)
     * @param setInsertPosSQL
     *          sql statement to insert a new head entry (position)
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
     * @param setReadPosSQL
     *          sql statement to read position entry
     * @param setInvalidatePosSQL
     *          sql statement to invalidate position entry
     */
    public DBHeadPosDateTemplateR(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setPosDataBaseTableName,
            final String setPosKeyFieldName,
            final String setInsertPosSQL,

            final String setReadMinMaxSQL,
            final String setReadNextSQL,
            final String setReadPrevSQL,
            final String setReadHeadSQL,
            final String setInvalidateHeadSQL,

            final String setReadPosSQL,
            final String setInvalidatePosSQL
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
              setInvalidateHeadSQL
           );

        Connection thisDataBase        =    null;
        try {
            // connect to database
            InitialContext ic      = new InitialContext();
            DataSource lDataSource = (DataSource) ic.lookup(setLookUpDataBase);
            thisDataBase           = lDataSource.getConnection();
            ic.close();

            DataBaseDepending myDataBaseDepending =
                    new DataBaseDepending(thisDataBase.getMetaData()
                            .getDatabaseProductName());

            if (setReadPosSQL == null) {
                this.readPosSQL = null;
            } else {
                this.readPosSQL = setReadPosSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }

            if (setInvalidatePosSQL == null) {
                this.invalidatePosSQL = null;
            } else {
                this.invalidatePosSQL = setInvalidatePosSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }

            if (setInsertPosSQL == null) {
                this.insertPosSQL = null;
            } else {
                this.insertPosSQL = setInsertPosSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }

        } catch (Exception e) {
            throw new UnexpectedException(e.toString(), e.getCause());
        } finally {
            try {
                if (thisDataBase != null) {
                    thisDataBase.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
     * @param setPosDataBaseTableName
     *          database table name (position)
     * @param setPosKeyfieldName
     *          key field of the database (position)
     * @param setInsertPosSQL
     *          sql statement to insert a new head entry (position)
     */
    public DBHeadPosDateTemplateR(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setPosDataBaseTableName,
            final String setPosKeyfieldName,
            final String setInsertPosSQL
          ) {
        this(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL,
             setPosDataBaseTableName,
             setPosKeyfieldName,
             setInsertPosSQL,

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
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();",

             "SELECT * "
           + "FROM   " + setPosDataBaseTableName + " "
           + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
           + "  AND  " + setKeyFieldName + " = ? "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

             "UPDATE " + setPosDataBaseTableName + " "
           + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() "
           + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
           + "  AND  " + setKeyFieldName + " = ? "
           + "  AND  " + setPosKeyfieldName + " = ? "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();");
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
     * @param setPosDataBaseTableName
     *          database table name (position)
     * @param setPosKeyfieldName
     *          key field of the database (position)
     * @param setInsertPosSQL
     *          sql statement to insert a new head entry (position)
     * @param setReadHeadSQL
     *          sql statement to read head entry
     * @param setReadPosSQL
     *          sql statement to read position entry
     */
    public DBHeadPosDateTemplateR(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setPosDataBaseTableName,
            final String setPosKeyfieldName,
            final String setInsertPosSQL,
            final String setReadHeadSQL,
            final String setReadPosSQL
          ) {
        this(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL,
             setPosDataBaseTableName,
             setPosKeyfieldName,
             setInsertPosSQL,

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

             setReadHeadSQL,

             "UPDATE " + setDataBaseTableName + " "
           + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() "
           + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
           + "  AND  " + setKeyFieldName + " = ? "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();",

             setReadPosSQL,

             "UPDATE " + setPosDataBaseTableName + " "
           + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() "
           + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
           + "  AND  " + setKeyFieldName + " = ? "
           + "  AND  " + setPosKeyfieldName + " = ? "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
           + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();");
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
     * <code>deleteEntry</code> deletes one entry from database.
     *
     * @param currentEntry
     *          entry to delete
     * @return entry to display after deletion
     */
    @Override
    public final E deleteEntry(final String currentEntry) {
        E resultValue = null;
        DomainUser thisUser = this.getUser();
        if (thisUser != null) {
            int mandator = thisUser.getMandator();
            String user  = thisUser.getUser();
            Connection thisDataBase = null;
            PreparedStatement invalidateHeadSQLStatement = null;
            PreparedStatement invalidatePosSQLStatement  = null;

            try {
                // connect to database
                InitialContext ic      =  new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.getLookUpDataBase());
                thisDataBase           = lDataSource.getConnection();
                ic.close();

                if (allowedToChange()) {
                    E dbEntry    =    this.readEntry(currentEntry);
                    // invalidate head number
                    if (this.getInvalidateHeadSQL() != null) {
                        invalidateHeadSQLStatement =
                                thisDataBase.prepareStatement(
                                        this.getInvalidateHeadSQL());
                        invalidateHeadSQLStatement.clearParameters();
                        invalidateHeadSQLStatement.setInt(1, mandator);
                        invalidateHeadSQLStatement.setString(2, currentEntry);
                        invalidateHeadSQLStatement.executeUpdate();
                        this.insertEntry(thisDataBase, mandator,
                                user, dbEntry, true);
                    }

                    invalidatePosSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.invalidatePosSQL);
                    int numPos = 0;
                    if (dbEntry.getKeyPos() != null) {
                        numPos = dbEntry.getKeyPos().length;
                    }
                    for (int i = 0; i < numPos; i++) {
                        int sqlPos = 1;
                        invalidatePosSQLStatement.clearParameters();
                        invalidatePosSQLStatement.setInt(sqlPos++, mandator);
                        invalidatePosSQLStatement.setString(sqlPos++,
                                currentEntry);
                        invalidatePosSQLStatement.setString(sqlPos++,
                                dbEntry.getKeyPos()[i]);
                        invalidatePosSQLStatement.executeUpdate();
                        this.insertPositionEntry(thisDataBase, mandator,
                                user, dbEntry, true, i);
                    }
                }

                resultValue    = readNextEntry(currentEntry);
            } catch (SQLException e) {
                resultValue    =    null;
            } catch (NamingException e) {
                resultValue    =    null;
            } finally {
                try {
                    if (invalidatePosSQLStatement != null) {
                        invalidatePosSQLStatement.close();
                    }
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
            PreparedStatement invalidatePosSQLStatement  = null;
            PreparedStatement invalidateHeadSQLStatement = null;

            try {
                invalidatePosSQLStatement =
                        thisDataBase.prepareStatement(
                                this.invalidatePosSQL);

                if (!currentEntry.equalsEntry(dbEntry)
                && (this.getInvalidateHeadSQL() != null)) {
                    // Invalidate old entry
                    invalidateHeadSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.getInvalidateHeadSQL());
                    invalidateHeadSQLStatement.clearParameters();
                    invalidateHeadSQLStatement.setInt(1, mandator);
                    invalidateHeadSQLStatement.setString(2,
                            saveKeyString);
                    invalidateHeadSQLStatement.executeUpdate();

                    // Insert new entry
                    this.insertEntry(thisDataBase, mandator,
                            user, currentEntry, false);

                    currentEntry.setKeyCur(saveKeyString);
                }
                // Positions
                // Take a look if position differ and invalidate old
                int numPos = 0;
                if (currentEntry.getKeyPos() != null) {
                    numPos = currentEntry.getKeyPos().length;
                }
                int numDBPos = 0;
                if (dbEntry != null && dbEntry.getKeyPos() != null) {
                    numDBPos = dbEntry.getKeyPos().length;
                }

                for (int i = 0; i < numDBPos; i++) {
                    boolean isremoved    =    true;
                    for (int j = 0; j < numPos && isremoved; j++) {
                        if (dbEntry.getKeyPos()[i].equals(
                                currentEntry.getKeyPos()[j])) {
                            isremoved    =    false;
                        }
                    }
                    if (isremoved) {
                        int invPos = 1;
                        invalidatePosSQLStatement.clearParameters();
                        invalidatePosSQLStatement.setInt(invPos++, mandator);
                        invalidatePosSQLStatement.setString(invPos++,
                                saveKeyString);
                        invalidatePosSQLStatement.setString(invPos++,
                                dbEntry.getKeyPos()[i]);
                        invalidatePosSQLStatement.executeUpdate();
                        this.insertPositionEntry(thisDataBase,
                                mandator, user, dbEntry, true, i);
                        // Invalidate old entry
                    }
                }
                // Take a look if position differ and insert new
                for (int i = 0; i < numPos; i++) {
                    boolean isnew    =    true;
                    for (int j = 0; j < numDBPos && isnew; j++) {
                        if (dbEntry.getKeyPos()[j].equals(currentEntry
                                .getKeyPos()[i])) {
                            isnew    =    false;
                            // Entry already exists, look for changes
                            if (!currentEntry.equalsPosition(dbEntry,
                                    i, j)) {
                                int invPos = 1;
                                // Invalidate old entry
                                invalidatePosSQLStatement
                                    .clearParameters();
                                invalidatePosSQLStatement
                                    .setInt(invPos++, mandator);
                                invalidatePosSQLStatement
                                    .setString(invPos++, saveKeyString);
                                invalidatePosSQLStatement
                                    .setString(invPos++,
                                            dbEntry.getKeyPos()[i]);
                                invalidatePosSQLStatement
                                    .executeUpdate();

                                this.insertPositionEntry(
                                        thisDataBase, mandator,
                                        user, currentEntry, false, i);
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
            } finally {
                if (invalidatePosSQLStatement != null) {
                    invalidatePosSQLStatement.close();
                }
                if (invalidateHeadSQLStatement != null) {
                    invalidateHeadSQLStatement.close();
                }
            }
        }
    }

    /**
     * get read pos sql string.
     * @return readPosSQL
     */
    public final String getReadPosSQL() {
        return readPosSQL;
    }
}
