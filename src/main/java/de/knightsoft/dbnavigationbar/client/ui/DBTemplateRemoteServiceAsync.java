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

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainDBBasics;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * The <code>DBTemplateRemoteServiceAsync</code> class is the asynchronous interface template for
 * DataBase Head.
 *
 * @param <E> database structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public interface DBTemplateRemoteServiceAsync<E extends AbstractDomainDBBasics> {
  /**
   * save entry to database.
   *
   * @param pCurrentEntry entry to save
   * @param pCallback class which gets back response
   */
  void saveEntry(E pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * delete a entry from database.
   *
   * @param pCurrentEntry entry to delete
   * @param pCallback class which gets back response
   */
  void deleteEntry(String pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * read on entry by key from database.
   *
   * @param pEntry database key
   * @param pCallback class which gets back response
   */
  void readEntry(String pEntry, AsyncCallback<E> pCallback);

  /**
   * read first database entry.
   *
   * @param pCallback class which gets back response
   */
  void readFirstEntry(AsyncCallback<E> pCallback);

  /**
   * read previous entry.
   *
   * @param pCurrentEntry key of current entry
   * @param pCallback class which gets back response
   */
  void readPreviousEntry(String pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * read next entry.
   *
   * @param pCurrentEntry key of current entry
   * @param pCallback class which gets back response
   */
  void readNextEntry(String pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * read the last entry of the database.
   *
   * @param pCallback class which gets back response
   */
  void readLastEntry(AsyncCallback<E> pCallback);

  /**
   * find a entry in the database, start with the first one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCallback class which gets back response
   */
  void findFirstEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry,
      AsyncCallback<E> pCallback);

  /**
   * find a entry in the database backward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @param pCallback class which gets back response
   */
  void findPreviousEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry,
      String pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * find a entry in the database forward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @param pCallback class which gets back response
   */
  void findNextEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry,
      String pCurrentEntry, AsyncCallback<E> pCallback);

  /**
   * find a entry in the database backward, start with the last one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCallback class which gets back response
   */
  void findLastEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry,
      AsyncCallback<E> pCallback);
}
