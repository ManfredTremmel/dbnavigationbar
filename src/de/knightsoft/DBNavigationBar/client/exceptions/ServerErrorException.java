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
 * The <code>ServerErrorException</code> is thrown, when a error on server
 * occurs.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class ServerErrorException extends Exception implements Serializable {

    /**
     * serial version uid.
     */
    private static final long serialVersionUID = -4741605534011831259L;
    /**
     * error message.
     */
    private final String message;

    /**
     * default constructor.
     */
    public ServerErrorException() {
        super();
        this.message = "unknown error";
    }

    /**
     * constructor with given message.
     * @param messageInit a message to set.
     */
    public ServerErrorException(final String messageInit) {
        super();
        this.message = messageInit;
    }

    /**
     * constructor with given message.
     * @param exception a message to set.
     */
    public ServerErrorException(final Exception exception) {
        super();
        this.message = exception.getMessage();
    }

    /**
     * @return the message
     */
    @Override
    public final String getMessage() {
        return this.message;
    }
}
