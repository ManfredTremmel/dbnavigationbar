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

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.knightsoft.DBNavigationBar.client.domain.AbstractDataBaseDomain;
import de.knightsoft.DBNavigationBar.client.exceptions.ServerErrorException;

/**
 *
 * The <code>AbstractDBRemoteServiceAsync</code> class is the asynchronous
 * interface template for DataBase handling.
 *
 * @param <E> database domain structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract interface AbstractDBRemoteServiceAsync<E extends
    AbstractDataBaseDomain> {
    /**
     * save entry to database.
     * @param currentEntry
     *         entry to save
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void saveEntry(E currentEntry, AsyncCallback<E> callback)
            throws ServerErrorException;
    /**
     * delete a entry from database.
     * @param currentEntry
     *         entry to delete
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void deleteEntry(String currentEntry, AsyncCallback<E> callback)
            throws ServerErrorException;
    /**
     * read on entry by key from database.
     * @param entry
     *         database key
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void readEntry(String entry, AsyncCallback<E> callback)
            throws ServerErrorException;
    /**
     * read first database entry.
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void readFirstEntry(AsyncCallback<E> callback)
            throws ServerErrorException;

    /**
     * read previous entry.
     * @param currentEntry
     *         key of current entry
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void readPreviousEntry(String currentEntry, AsyncCallback<E> callback)
            throws ServerErrorException;
    /**
     * read next entry.
     * @param currentEntry
     *         key of current entry
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void readNextEntry(String currentEntry, AsyncCallback<E> callback)
            throws ServerErrorException;
    /**
     * read the last entry of the database.
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void readLastEntry(AsyncCallback<E> callback)
            throws ServerErrorException;

    /**
     * find a entry in the database, start with the first one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void findFirstEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, AsyncCallback<E> callback)
                    throws ServerErrorException;

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
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void findPreviousEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, String currentEntry,
            AsyncCallback<E> callback) throws ServerErrorException;
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
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void findNextEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, String currentEntry,
            AsyncCallback<E> callback) throws ServerErrorException;
    /**
     * find a entry in the database backward, start with the last one.
     * @param searchField
     *         database field to search for
     * @param searchMethodEntry
     *         method to use for searching
     * @param searchFieldEntry
     *         search text
     * @param callback
     *         class which gets back response
     * @throws ServerErrorException
     *          thrown on errors on server
     */
    void findLastEntry(String searchField, String searchMethodEntry,
            String searchFieldEntry, AsyncCallback<E> callback)
                    throws ServerErrorException;
}
