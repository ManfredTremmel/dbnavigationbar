/**
 * This file is part of DBNavigationBar.
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
package de.knightsoft.DBNavigationBar.server;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.knightsoft.DBNavigationBar.client.domain.DomainDataBaseInterface;

/**
 * The <code>AbstractDBTemplate</code> class is the interface for the
 * database templates.
 * @param <E> DataBase Domain structure
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DBTemplateInterface<E extends DomainDataBaseInterface> {

    /**
     * <code>fillInsertHead</code> fills the parameters
     * of the insert prepared statement.
     * @param insertHeadSQLStatement prepared statement
     * @param mandator number
     * @param user name
     * @param saveEntry entry to save
     * @param delete entry
     * @throws SQLException if insert fails
     */
    void fillInsertHead(
            final PreparedStatement insertHeadSQLStatement,
            final int mandator,
            final String user,
            final E saveEntry,
            final boolean delete) throws SQLException;

    /**
     * <code>allowedToSee</code> checks if user
     * is allowed to see entries.
     * @return allowance to see true/false
     */
    boolean allowedToSee();

    /**
     * <code>allowedToChange</code> checks if user
     * is allowed to change entries.
     * @return allowance to change true/false
     */
    boolean allowedToChange();

    /**
     * <code>deleteEntry</code> deletes one entry from database.
     *
     * @param currentEntry the entry to delete
     * @return entry to display after one is deleted
     */
    E deleteEntry(final String currentEntry);

    /**
     * <code>findFirstEntry</code> is called to search for the
     * first entry which fulfills the search parameters.
     *
     * @param searchField field to search for
     * @param searchMethodeEntry method to use
     * @param searchFieldEntry entry to search for
     * @return the found entry or null if none is found
     */
    E findFirstEntry(final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry);

    /**
     * <code>findLastEntry</code> is called to search for the
     * last entry which fulfills the search parameters.
     *
     * @param searchField field to search for
     * @param searchMethodeEntry method to use
     * @param searchFieldEntry entry to search for
     * @return the found entry or null if none is found
     */
    E findLastEntry(final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry);

    /**
     * <code>findNextEntry</code> is called to search for the
     * next entry which fulfills the search parameters.
     *
     * @param searchField field to search for
     * @param searchMethodeEntry method to use
     * @param searchFieldEntry entry to search for
     * @param currentEntry the currently displayed entry
     * @return the found entry or null if none is found
     */
    E findNextEntry(final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry,
            final String currentEntry);

    /**
     * <code>findPreviousEntry</code> is called to search for the
     * previous entry which fulfills the search parameters.
     *
     * @param searchField field to search for
     * @param searchMethodeEntry method to use
     * @param searchFieldEntry entry to search for
     * @param currentEntry the currently displayed entry
     * @return the found entry or null if none is found
     */
    E findPreviousEntry(final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry,
            final String currentEntry);

    /**
     * <code>readEntry</code> is used to read a
     * given entry from database.
     *
     * @param entry
     *             the entry to read
     * @return the filled structure
     */
    E readEntry(final String entry);

    /**
     * <code>readFirstEntry</code> is used to read the
     * first entry from database.
     *
     * @return the filled structure
     */
    E readFirstEntry();

    /**
     * <code>readLastEntry</code> is used to read the
     * last entry from database.
     *
     * @return the filled structure
     */
    E readLastEntry();

    /**
     * <code>readNextEntry</code> is used to read the
     * next entry from database.
     *
     * @param currentEntry
     *             the currently displayed entry
     * @return the filled structure
     */
    E readNextEntry(final String currentEntry);

    /**
     * <code>readPreviousEntry</code> is used to read
     * the previous entry from database.
     *
     * @param currentEntry
     *             the currently displayed entry
     * @return the filled user structure
     */
    E readPreviousEntry(final String currentEntry);

    /**
     * <code>saveEntry</code> saves or inserts a
     * entry to database.
     *
     * @param currentEntry
     *             entry that has to be saved
     * @return entry after saving
     */
    E saveEntry(final E currentEntry);

}
