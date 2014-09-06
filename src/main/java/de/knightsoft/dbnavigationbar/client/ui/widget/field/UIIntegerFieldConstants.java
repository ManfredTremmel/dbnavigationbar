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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client.ui.widget.field;

import com.google.gwt.i18n.client.Messages;

/**
 * 
 * The <code>UIStringFieldConstants</code> class contains the messages UIString
 * input field.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface UIIntegerFieldConstants extends Messages
{

  /**
   * empty field error message.
   * 
   * @param fieldName
   *        name of the input field
   * @return error message to display
   */
  String emptyField(final String fieldName);

  /**
   * not numeric error message.
   * 
   * @param fieldName
   *        name of the input field
   * @return error message to display
   */
  String notNumeric(final String fieldName);

  /**
   * input out of range error message.
   * 
   * @param fieldName
   *        name of the input field
   * @param minEntry
   *        minimum allowed entry
   * @param maxEntry
   *        maximum allowed entry
   * @return error message to display
   */
  String outOfRange(final String fieldName, final int minEntry,
      final int maxEntry);
}
