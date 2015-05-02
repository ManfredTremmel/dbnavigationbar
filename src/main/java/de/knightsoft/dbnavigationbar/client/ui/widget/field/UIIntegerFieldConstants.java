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
 * Copyright (c) 2012-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui.widget.field;

import com.google.gwt.i18n.client.Messages;

/**
 *
 * The <code>UIStringFieldConstants</code> class contains the messages UIString input field.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface UIIntegerFieldConstants extends Messages {

  /**
   * empty field error message.
   *
   * @param pFieldName name of the input field
   * @return error message to display
   */
  String emptyField(final String pFieldName);

  /**
   * not numeric error message.
   *
   * @param pFieldName name of the input field
   * @return error message to display
   */
  String notNumeric(final String pFieldName);

  /**
   * input out of range error message.
   *
   * @param pFieldName name of the input field
   * @param pMinEntry minimum allowed entry
   * @param pMaxEntry maximum allowed entry
   * @return error message to display
   */
  String outOfRange(final String pFieldName, final int pMinEntry, final int pMaxEntry);
}
