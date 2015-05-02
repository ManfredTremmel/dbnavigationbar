/**
 * This file is part of DBNavigation.
 *
 * RiPhone is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RiPhone. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.DomainHeadPosDataBaseInt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation. template for a simple database
 *
 * @param <E> Structure of the database
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class AbstractDBHeadPosDateTemplate<E extends DomainHeadPosDataBaseInt> extends
    AbstractDBHeadPosDateTemplateR<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 3633734786925668260L;

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   * @param pReadMinMaxSQL sql statement for min/max read
   * @param pReadNextSQL sql statement to read next key
   * @param pReadPrevSQL sql statement to read previous key
   * @param pReadHeadSQL sql statement to read head entry
   * @param pInvalidateHeadSQL sql statement to invalidate head entry
   * @param pReadPosSQL sql statement to read position entry
   * @param pInvalidatePosSQL sql statement to invalidate position entry
   */
  public AbstractDBHeadPosDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pInsertPosSQL,

      final String pReadMinMaxSQL, final String pReadNextSQL, final String pReadPrevSQL, final String pReadHeadSQL,
      final String pInvalidateHeadSQL,

      final String pReadPosSQL, final String pInvalidatePosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pInsertPosSQL,

    pReadMinMaxSQL, pReadNextSQL, pReadPrevSQL, pReadHeadSQL, pInvalidateHeadSQL,

    pReadPosSQL, pInvalidatePosSQL);
  }

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pPosDataBaseTableName database table name (position)
   * @param pPosKeyfieldName key field of the database (position)
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   */
  public AbstractDBHeadPosDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pPosDataBaseTableName,
        pPosKeyfieldName, pInsertPosSQL);
  }

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pPosDataBaseTableName database table name (position)
   * @param pPosKeyfieldName key field of the database (position)
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   * @param pReadHeadSQL sql statement to read head entry
   * @param pReadPosSQL sql statement to read position entry
   */
  public AbstractDBHeadPosDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL, final String pReadHeadSQL,
      final String pReadPosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pPosDataBaseTableName,
        pPosKeyfieldName, pInsertPosSQL, pReadHeadSQL, pReadPosSQL);
  }

  /**
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read
   * @param pThisEntry structure to be filled
   * @return the filled structure
   */
  @Override
  protected final E readOneEntry(final Connection pDataBase, final int pMandator, final String pEntry, final E pThisEntry) {
    E returnEntry = pThisEntry;
    try {
      returnEntry = super.readHeadEntry(pDataBase, pMandator, pEntry, returnEntry);

      if (returnEntry != null && returnEntry.getKeyCur() != null) {
        try (final PreparedStatement readPosSQLStatement = pDataBase.prepareStatement(this.getReadPosSQL())) {
          readPosSQLStatement.clearParameters();
          readPosSQLStatement.setInt(1, pMandator);
          readPosSQLStatement.setString(2, pEntry);
          try (ResultSet resultPos = readPosSQLStatement.executeQuery()) {
            returnEntry = this.fillPosFromResultSet(resultPos, returnEntry);
          }
        }
      }
    } catch (final Exception nef) {
      returnEntry = null;
    }
    return returnEntry;
  }
}
