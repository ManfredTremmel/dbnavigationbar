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
 */
package de.knightsoft.DBNavigationBar.client.exceptions;

import java.io.Serializable;

/**
 *
 * The <code>EntryNotFoundException</code> is thrown, when a entry which should
 * be read from database could not be found.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class EntryNotFoundException extends ServerErrorException
    implements Serializable {
    /**
     * serial version uid.
     */
    private static final long serialVersionUID = -6326451380001601358L;

    /**
     *
     * The <code>ReadTyp</code> defines which way the read was done.
     *
     * @author Manfred Tremmel
     * @version $Rev$, $Date$
     */
   public enum ReadTyp {
       /**
        * read the exact key.
        */
       READ_EQUAL,
       /**
        * read the next key.
        */
       READ_NEXT,
       /**
        * read the previous key.
        */
       READ_PREVIOUS,
       /**
        * search was not successfully.
        */
       SEARCH,
       /**
        * search from given key on was not successfully.
        */
       SEARCH_NEXT,
       /**
        * search before given key on was not successfully.
        */
       SEARCH_PREVIOUS
   }

    /**
     * key that could not be found or was base for search.
     */
    private String key;

    /**
     * defines which way the read was done.
     */
    private ReadTyp readTyp;

    /**
     * default constructor.
     */
    public EntryNotFoundException() {
        super();
    }

    /**
     * constructor with initial data.
     *
     * @param keyInit initial key to set
     * @param readTypInit initial read type to set
     */
    public EntryNotFoundException(
            final String keyInit,
            final ReadTyp readTypInit) {
        super();
        this.key = keyInit;
        this.readTyp = readTypInit;
    }

    /**
     * @return the key
     */
    public final String getKey() {
        return this.key;
    }

    /**
     * @param keySet the key to set
     */
    public final void setKey(final String keySet) {
        this.key = keySet;
    }

    /**
     * @return the readTyp
     */
    public final ReadTyp getReadTyp() {
        return this.readTyp;
    }

    /**
     * @param readTypSet the readTyp to set
     */
    public final void setReadTyp(final ReadTyp readTypSet) {
        this.readTyp = readTypSet;
    }
}
