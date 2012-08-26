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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 */
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.user.client.rpc.XsrfProtectedService;

import de.knightsoft.DBNavigationBar.client.domain.AbstractDataBaseDomain;

/**
 *
 * The <code>AbstractDBRemoteService</code> class is the synchronous
 * interface template for DataBase handling.
 *
 * @param <E> database domain structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract interface AbstractDBRemoteService
    <E extends AbstractDataBaseDomain<?>> extends XsrfProtectedService {
    /**
     * save entry to database.
     * @param currentEntry
     *         entry to save
     * @return saved entry
     */
    E saveEntry(E currentEntry);
    /**
     * delete a entry from database.
     * @param currentEntry
     *         entry to delete
     * @return entry to display after deletion
     */
    E deleteEntry(String currentEntry);
    /**
     * read on entry by key from database.
     * @param entry
     *         database key
     * @return entry read from database
     */
    E readEntry(String entry);
    /**
     * read first database entry.
     * @return entry read from database
     */
    E readFirstEntry();
    /**
     * read previous entry.
     * @param currentEntry
     *         key of current entry
     * @return entry read from database
     */
    E readPreviousEntry(String currentEntry);
    /**
     * read next entry.
     * @param currentEntry
     *         key of current entry
     * @return entry read from database
     */
    E readNextEntry(String currentEntry);
    /**
     * read the last entry of the database.
     * @return entry read from database
     */
    E readLastEntry();
    /**
     * find a entry in the database, start with the first one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @return entry read from database
     */
    E findFirstEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry);
    /**
     * find a entry in the database backward, start with the current one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @param currentEntry
     *         key of the current entry
     * @return entry read from database
     */
    E findPreviousEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, String currentEntry);
    /**
     * find a entry in the database forward, start with the current one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @param currentEntry
     *         key of the current entry
     * @return entry read from database
     */
    E findNextEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, String currentEntry);
    /**
     * find a entry in the database backward, start with the last one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @return entry read from database
     */
    E findLastEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry);
}
