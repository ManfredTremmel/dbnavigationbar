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

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDataBaseDomain;
import de.knightsoft.dbnavigationbar.shared.exceptions.ServerErrorException;

import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 *
 * The <code>AbstractDBRemoteService</code> class is the synchronous interface template for DataBase handling.
 *
 * @param <E> database domain structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface AbstractDBRemoteService<E extends AbstractDataBaseDomain> extends XsrfProtectedService {
  /**
   * save entry to database.
   *
   * @param pCurrentEntry entry to save
   * @return saved entry
   * @throws ServerErrorException thrown on errors on server
   */
  E saveEntry(E pCurrentEntry) throws ServerErrorException;

  /**
   * delete a entry from database.
   *
   * @param pCurrentEntry entry to delete
   * @return entry to display after deletion
   * @throws ServerErrorException thrown on errors on server
   */
  E deleteEntry(String pCurrentEntry) throws ServerErrorException;

  /**
   * read on entry by key from database.
   *
   * @param pEntry database key
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E readEntry(String pEntry) throws ServerErrorException;

  /**
   * read first database entry.
   *
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E readFirstEntry() throws ServerErrorException;

  /**
   * read previous entry.
   *
   * @param pCurrentEntry key of current entry
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E readPreviousEntry(String pCurrentEntry) throws ServerErrorException;

  /**
   * read next entry.
   *
   * @param pCurrentEntry key of current entry
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E readNextEntry(String pCurrentEntry) throws ServerErrorException;

  /**
   * read the last entry of the database.
   *
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E readLastEntry() throws ServerErrorException;

  /**
   * find a entry in the database, start with the first one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E findFirstEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry) throws ServerErrorException;

  /**
   * find a entry in the database backward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E findPreviousEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry, String pCurrentEntry)
      throws ServerErrorException;

  /**
   * find a entry in the database forward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E findNextEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry, String pCurrentEntry)
      throws ServerErrorException;

  /**
   * find a entry in the database backward, start with the last one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @return entry read from database
   * @throws ServerErrorException thrown on errors on server
   */
  E findLastEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry) throws ServerErrorException;
}
