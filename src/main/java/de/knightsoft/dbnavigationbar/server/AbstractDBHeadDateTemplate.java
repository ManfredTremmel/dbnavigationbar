/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.DomainHeadDataBaseInterface;

import java.sql.Connection;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation template for a simple database.
 *
 * @param <E> structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadDateTemplate<E extends DomainHeadDataBaseInterface> extends AbstractDBHeadDateTemplateR<E> {

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
   * @param pReadMinMaxSQL sql statement for min/max read
   * @param pReadNextSQL sql statement to read next key
   * @param pReadPrevSQL sql statement to read previous key
   * @param pReadHeadSQL sql statement to read head entry
   * @param pInvalidateHeadSQL sql statement to invalidate head entry
   */
  public AbstractDBHeadDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pReadMinMaxSQL,
      final String pReadNextSQL, final String pReadPrevSQL, final String pReadHeadSQL, final String pInvalidateHeadSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pReadMinMaxSQL,
        pReadNextSQL, pReadPrevSQL, pReadHeadSQL, pInvalidateHeadSQL);
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
   */
  public AbstractDBHeadDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL);
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
    return super.readHeadEntry(pDataBase, pMandator, pEntry, pThisEntry);
  }
}
