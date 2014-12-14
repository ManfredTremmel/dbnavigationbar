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
 * --
 * Name Date Change
 */
package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainDBBasics;

/**
 *
 * The <code>DBTemplateRemoteServiceAsync</code> class is the asynchronous
 * interface template for DataBase Head.
 *
 * @param <E>
 *        database structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public interface DBTemplateRemoteServiceAsync<E extends AbstractDomainDBBasics>
{
  /**
   * save entry to database.
   *
   * @param currentEntry
   *        entry to save
   * @param callback
   *        class which gets back response
   */
  void saveEntry(E currentEntry, AsyncCallback<E> callback);

  /**
   * delete a entry from database.
   *
   * @param currentEntry
   *        entry to delete
   * @param callback
   *        class which gets back response
   */
  void deleteEntry(String currentEntry, AsyncCallback<E> callback);

  /**
   * read on entry by key from database.
   *
   * @param entry
   *        database key
   * @param callback
   *        class which gets back response
   */
  void readEntry(String entry, AsyncCallback<E> callback);

  /**
   * read first database entry.
   *
   * @param callback
   *        class which gets back response
   */
  void readFirstEntry(AsyncCallback<E> callback);

  /**
   * read previous entry.
   *
   * @param currentEntry
   *        key of current entry
   * @param callback
   *        class which gets back response
   */
  void readPreviousEntry(String currentEntry, AsyncCallback<E> callback);

  /**
   * read next entry.
   *
   * @param currentEntry
   *        key of current entry
   * @param callback
   *        class which gets back response
   */
  void readNextEntry(String currentEntry, AsyncCallback<E> callback);

  /**
   * read the last entry of the database.
   *
   * @param callback
   *        class which gets back response
   */
  void readLastEntry(AsyncCallback<E> callback);

  /**
   * find a entry in the database, start with the first one.
   *
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @param callback
   *        class which gets back response
   */
  void findFirstEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, AsyncCallback<E> callback);

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
   * @param callback
   *        class which gets back response
   */
  void findPreviousEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, String currentEntry,
      AsyncCallback<E> callback);

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
   * @param callback
   *        class which gets back response
   */
  void findNextEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, String currentEntry,
      AsyncCallback<E> callback);

  /**
   * find a entry in the database backward, start with the last one.
   *
   * @param searchField
   *        database field to search for
   * @param searchMethodEntry
   *        method to use for searching
   * @param searchFieldEntry
   *        search text
   * @param callback
   *        class which gets back response
   */
  void findLastEntry(String searchField, String searchMethodEntry,
      String searchFieldEntry, AsyncCallback<E> callback);
}
