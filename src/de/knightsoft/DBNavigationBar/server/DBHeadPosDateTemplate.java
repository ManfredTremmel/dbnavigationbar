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

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBaseInt;

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
public abstract class DBHeadPosDateTemplate<E extends DomainHeadPosDataBaseInt>
    extends DBHeadPosDateTemplateR<E> {

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
    public DBHeadPosDateTemplate(
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
              setPosDataBaseTableName,
              setPosKeyFieldName,
              setInsertPosSQL,

              setReadMinMaxSQL,
              setReadNextSQL,
              setReadPrevSQL,
              setReadHeadSQL,
              setInvalidateHeadSQL,

              setReadPosSQL,
              setInvalidatePosSQL
           );
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
    public DBHeadPosDateTemplate(
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
        super(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL,
             setPosDataBaseTableName,
             setPosKeyfieldName,
             setInsertPosSQL
         );
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
    public DBHeadPosDateTemplate(
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
        super(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL,
             setPosDataBaseTableName,
             setPosKeyfieldName,
             setInsertPosSQL,
             setReadHeadSQL,
             setReadPosSQL
       );
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
                readPosSQLStatement =
                        thisDataBase.prepareStatement(this.getReadPosSQL());
                readPosSQLStatement.clearParameters();
                readPosSQLStatement.setInt(1, mandator);
                readPosSQLStatement.setString(2, entry);
                ResultSet resultPos    =    readPosSQLStatement.executeQuery();
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
