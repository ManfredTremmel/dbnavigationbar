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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;

/**
 * The <code>RiPhoneDBHeadPosTemplate</code> class is the server
 * side implementation template for a simple database.
 *
 * @param <E> DataBase structure type
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadPosTemplate<E extends DomainHeadPosDataBaseInt>
    extends DBHeadPosTemplateRS<E> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -8763724693538834151L;

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
    public DBHeadPosTemplate(
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
                setUpdateHeadSQL,
                setPosDataBaseTableName,
                setPosKeyfieldName,
                setInsertPosSQL,
                setUpdatePosSQL);
    }

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
            int mandator    =    thisUser.getMandator();
            String user     =    thisUser.getUser();
            Connection thisDataBase =    null;
            PreparedStatement invalidateHeadSQLStatement = null;
            PreparedStatement invalidatePosSQLStatement  = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.getLookUpDataBase());
                thisDataBase = lDataSource.getConnection();
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
                        this.insertEntry(thisDataBase, mandator, user,
                                dbEntry, true);
                    }

                    invalidatePosSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.getInvalidatePosSQL());
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
                        this.insertPositionEntry(thisDataBase, mandator, user,
                                dbEntry, true, i);
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
     * <code>readOneEntry</code> is used to read a given entry
     * from database.
     *
     * @param thisDataBase
     *             Database Connection
     * @param mandator
     *             mandator is a keyfield
     * @param entry
     *             the Entry to read
     * @param thisEntry
     *             structure to be filled
     * @return the filled structure
     * @throws SQLException
     */
    @Override
    protected final E readOneEntry(final Connection thisDataBase,
            final int mandator, final String entry, final E thisEntry) {
        PreparedStatement readPosSQLStatement    =    null;
        E returnEntry = thisEntry;
        try {
            returnEntry = super.readHeadEntry(thisDataBase, mandator,
                    entry, returnEntry);

            if (returnEntry != null && returnEntry.getKeyCur() != null) {
                readPosSQLStatement = thisDataBase.prepareStatement(
                        this.getReadPosSQL());
                readPosSQLStatement.clearParameters();
                readPosSQLStatement.setInt(1, mandator);
                readPosSQLStatement.setString(2, entry);
                ResultSet resultPos = readPosSQLStatement.executeQuery();
                returnEntry = fillPosFromResultSet(resultPos, returnEntry);
                resultPos.close();
            }
        } catch (Exception nef) {
            returnEntry    =    null;
        } finally {
            try {
                if (readPosSQLStatement != null) {
                    readPosSQLStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnEntry;
    }
}
