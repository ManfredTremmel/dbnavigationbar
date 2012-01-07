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
 *  Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import java.sql.Connection;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBaseInterface;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side
 * implementation template for a simple database.
 *
 * @param <E> structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadDateTemplate<E extends DomainHeadDataBaseInterface>
    extends DBHeadDateTemplateR<E> {

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
    public DBHeadDateTemplate(
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
    public DBHeadDateTemplate(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL
          ) {
        super(setType,
             setLookUpDataBase,
             setSessionUser,
             setDataBaseTableName,
             setKeyFieldName,
             setInsertHeadSQL);
    }

    /**
     * <code>readOneEntry</code> is used to read a
     * given entry from database.
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
            final int mandator, final String entry,
            final E thisEntry) {
        return super.readHeadEntry(thisDataBase, mandator, entry, thisEntry);
    }
}
