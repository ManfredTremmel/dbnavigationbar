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

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainDBBasics;

import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 *
 * The <code>DBTemplateRemoteService</code> class is the synchronous interface template for DataBase Head.
 *
 * @param <E> database structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public interface DBTemplateRemoteService<E extends AbstractDomainDBBasics> extends XsrfProtectedService {
  /**
   * save entry to database.
   *
   * @param pCurrentEntry entry to save
   * @return saved entry
   */
  E saveEntry(E pCurrentEntry);

  /**
   * delete a entry from database.
   *
   * @param pCurrentEntry entry to delete
   * @return entry to display after deletion
   */
  E deleteEntry(String pCurrentEntry);

  /**
   * read on entry by key from database.
   *
   * @param pEntry database key
   * @return entry read from database
   */
  E readEntry(String pEntry);

  /**
   * read first database entry.
   *
   * @return entry read from database
   */
  E readFirstEntry();

  /**
   * read previous entry.
   *
   * @param pCurrentEntry key of current entry
   * @return entry read from database
   */
  E readPreviousEntry(String pCurrentEntry);

  /**
   * read next entry.
   *
   * @param pCurrentEntry key of current entry
   * @return entry read from database
   */
  E readNextEntry(String pCurrentEntry);

  /**
   * read the last entry of the database.
   *
   * @return entry read from database
   */
  E readLastEntry();

  /**
   * find a entry in the database, start with the first one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @return entry read from database
   */
  E findFirstEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry);

  /**
   * find a entry in the database backward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @return entry read from database
   */
  E findPreviousEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry, String pCurrentEntry);

  /**
   * find a entry in the database forward, start with the current one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @param pCurrentEntry key of the current entry
   * @return entry read from database
   */
  E findNextEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry, String pCurrentEntry);

  /**
   * find a entry in the database backward, start with the last one.
   *
   * @param pSearchField database field to search for
   * @param pSearchMethodEntry method to use for searching
   * @param pSearchFieldEntry search text
   * @return entry read from database
   */
  E findLastEntry(String pSearchField, String pSearchMethodEntry, String pSearchFieldEntry);
}
