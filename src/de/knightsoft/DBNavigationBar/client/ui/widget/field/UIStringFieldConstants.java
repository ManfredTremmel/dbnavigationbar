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
 * Copyright (c) 2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.ui.widget.field;

import com.google.gwt.i18n.client.Messages;

/**
 *
 * The <code>UIStringFieldConstants</code> class contains the messages UIString
 * input field.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2012-05-17
 */
public interface UIStringFieldConstants extends Messages {

    /**
     * empty field error message.
     * @param fieldName name of the input field
     * @return error message to display
     */
    String emptyField(final String fieldName);

    /**
     * input error message.
     * @param fieldName name of the input field
     * @return error message to display
     */
    String formatError(final String fieldName);
}
