/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.DomainDataBaseInterface;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The <code>AbstractDBTemplate</code> class is the interface for the database templates.
 *
 * @param <E> DataBase Domain structure
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DBTemplateInterface<E extends DomainDataBaseInterface> {

  /**
   * <code>fillInsertHead</code> fills the parameters of the insert prepared statement.
   *
   * @param pInsertHeadSQLStatement prepared statement
   * @param pMandator number
   * @param pUser name
   * @param pSaveEntry entry to save
   * @param pDelete entry
   * @throws SQLException if insert fails
   */
  void fillInsertHead(final PreparedStatement pInsertHeadSQLStatement, final int pMandator,
      final String pUser, final E pSaveEntry, final boolean pDelete) throws SQLException;

  /**
   * <code>allowedToSee</code> checks if user is allowed to see entries.
   *
   * @return allowance to see true/false
   */
  boolean allowedToSee();

  /**
   * <code>allowedToChange</code> checks if user is allowed to change entries.
   *
   * @return allowance to change true/false
   */
  boolean allowedToChange();

  /**
   * <code>deleteEntry</code> deletes one entry from database.
   *
   * @param pCurrentEntry the entry to delete
   * @return entry to display after one is deleted
   */
  E deleteEntry(final String pCurrentEntry);

  /**
   * <code>findFirstEntry</code> is called to search for the first entry which fulfills the search
   * parameters.
   *
   * @param pSearchField field to search for
   * @param pSearchMethodeEntry method to use
   * @param pSearchFieldEntry entry to search for
   * @return the found entry or null if none is found
   */
  E findFirstEntry(final String pSearchField, final String pSearchMethodeEntry,
      final String pSearchFieldEntry);

  /**
   * <code>findLastEntry</code> is called to search for the last entry which fulfills the search
   * parameters.
   *
   * @param pSearchField field to search for
   * @param pSearchMethodeEntry method to use
   * @param pSearchFieldEntry entry to search for
   * @return the found entry or null if none is found
   */
  E findLastEntry(final String pSearchField, final String pSearchMethodeEntry,
      final String pSearchFieldEntry);

  /**
   * <code>findNextEntry</code> is called to search for the next entry which fulfills the search
   * parameters.
   *
   * @param pSearchField field to search for
   * @param pSearchMethodeEntry method to use
   * @param pSearchFieldEntry entry to search for
   * @param pCurrentEntry the currently displayed entry
   * @return the found entry or null if none is found
   */
  E findNextEntry(final String pSearchField, final String pSearchMethodeEntry,
      final String pSearchFieldEntry, final String pCurrentEntry);

  /**
   * <code>findPreviousEntry</code> is called to search for the previous entry which fulfills the
   * search parameters.
   *
   * @param pSearchField field to search for
   * @param pSearchMethodeEntry method to use
   * @param pSearchFieldEntry entry to search for
   * @param pCurrentEntry the currently displayed entry
   * @return the found entry or null if none is found
   */
  E findPreviousEntry(final String pSearchField, final String pSearchMethodeEntry,
      final String pSearchFieldEntry, final String pCurrentEntry);

  /**
   * <code>readEntry</code> is used to read a given entry from database.
   *
   * @param pEntry the entry to read
   * @return the filled structure
   */
  E readEntry(final String pEntry);

  /**
   * <code>readFirstEntry</code> is used to read the first entry from database.
   *
   * @return the filled structure
   */
  E readFirstEntry();

  /**
   * <code>readLastEntry</code> is used to read the last entry from database.
   *
   * @return the filled structure
   */
  E readLastEntry();

  /**
   * <code>readNextEntry</code> is used to read the next entry from database.
   *
   * @param pCurrentEntry the currently displayed entry
   * @return the filled structure
   */
  E readNextEntry(final String pCurrentEntry);

  /**
   * <code>readPreviousEntry</code> is used to read the previous entry from database.
   *
   * @param pCurrentEntry the currently displayed entry
   * @return the filled user structure
   */
  E readPreviousEntry(final String pCurrentEntry);

  /**
   * <code>saveEntry</code> saves or inserts a entry to database.
   *
   * @param pCurrentEntry entry that has to be saved
   * @return entry after saving
   */
  E saveEntry(final E pCurrentEntry);

}
