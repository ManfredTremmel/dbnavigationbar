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
 * Copyright (c) 2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

/**
 *
 * The <code>EnumerationState</code> enumeration contains the different
 * states of a DataBaseResult.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-06-02
 */
public enum EnumerationState {
    /**
     * state is not defined.
     */
    UNDEFINED,
    /**
     * Reading a entry was OK.
     */
    READ_OK,
    /**
     * Writing a entry was OK.
     */
    WRITE_OK,
    /**
     * Deleting a entry was OK.
     */
    DELETE_OK,
    /**
     * Searching has found an entry.
     */
    SEARCH_OK,
    /**
     * The wanted entry was not found.
     */
    READ_NOT_FOUND,
    /**
     * The search request found no result.
     */
    SEARCH_NOT_FOUND,
    /**
     * The entry is locked by another user.
     */
    ENTRY_LOCKED,
    /**
     * Server error, something went wrong on server.
     */
    SERVER_ERROR,
    /**
     * Not allowed to read the entry.
     */
    NOT_ALLOWED_TO_READ,
    /**
     * Not allowed to write the entry.
     */
    NOT_ALLOWED_TO_WRITE,
    /**
     * the given data are wrong.
     */
    DATA_WRONG,
    /**
     * user is not logged in, maybe session timed out.
     */
    NOT_LOGGED_IN,
}
