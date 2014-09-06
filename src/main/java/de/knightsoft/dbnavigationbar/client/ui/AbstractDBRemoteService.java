/**
 * This file is part of DBNavigation.
 * 
 * RiPhone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * RiPhone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.user.client.rpc.XsrfProtectedService;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDataBaseDomain;
import de.knightsoft.dbnavigationbar.shared.exceptions.ServerErrorException;

/**
 * 
 * The <code>AbstractDBRemoteService</code> class is the synchronous
 * interface template for DataBase handling.
 * 
 * @param <E>
 *        database domain structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract interface AbstractDBRemoteService<E extends AbstractDataBaseDomain> extends XsrfProtectedService
{
  /**
   * save entry to database.
   * 
   * @param currentEntry
   *        entry to save
   * @return saved entry
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E saveEntry(E currentEntry) throws ServerErrorException;

  /**
   * delete a entry from database.
   * 
   * @param currentEntry
   *        entry to delete
   * @return entry to display after deletion
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E deleteEntry(String currentEntry) throws ServerErrorException;

  /**
   * read on entry by key from database.
   * 
   * @param entry
   *        database key
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E readEntry(String entry) throws ServerErrorException;

  /**
   * read first database entry.
   * 
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E readFirstEntry() throws ServerErrorException;

  /**
   * read previous entry.
   * 
   * @param currentEntry
   *        key of current entry
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E readPreviousEntry(String currentEntry) throws ServerErrorException;

  /**
   * read next entry.
   * 
   * @param currentEntry
   *        key of current entry
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E readNextEntry(String currentEntry) throws ServerErrorException;

  /**
   * read the last entry of the database.
   * 
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E readLastEntry() throws ServerErrorException;

  /**
   * find a entry in the database, start with the first one.
   * 
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E findFirstEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry) throws ServerErrorException;

  /**
   * find a entry in the database backward, start with the current one.
   * 
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @param currentEntry
   *        key of the current entry
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E findPreviousEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, String currentEntry)
      throws ServerErrorException;

  /**
   * find a entry in the database forward, start with the current one.
   * 
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @param currentEntry
   *        key of the current entry
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E findNextEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, String currentEntry)
      throws ServerErrorException;

  /**
   * find a entry in the database backward, start with the last one.
   * 
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @return entry read from database
   * @throws ServerErrorException
   *         thrown on errors on server
   */
  E findLastEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry) throws ServerErrorException;
}
