/**
 * This file is part of knightsoft db navigation.
 * 
 * DBNavigationBar is free software: you can redistribute it and/or modify
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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * <code>UIFieldInterface</code> is a class to define a UI field.
 * 
 * @param <E>
 *        field type (Java Class type)
 * @param <F>
 *        field type (KnightSoft DBNavigation field type)
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface UIFieldInterface<E, F>
{
  /**
   * get field.
   * 
   * @return field
   */
  F getField();

  /**
   * set base value (to check changes against) and value.
   * 
   * @param newValue
   *        new value
   */
  void setBaseValue(E newValue);

  /**
   * set base value (to check changes against) and value.
   * 
   * @param newValueField
   *        new value
   */
  void setBaseValueField(F newValueField);

  /**
   * set value.
   * 
   * @param newValue
   *        new value
   */
  void setValue(E newValue);

  /**
   * set value.
   * 
   * @param newValueField
   *        new value
   */
  void setValueField(F newValueField);

  /**
   * test if value has changed.
   * 
   * @return true if value has changed
   */
  boolean valueHasChanged();

  /**
   * check if value is ok.
   * 
   * @return true if value is ok
   */
  boolean valueIsOk();

  /**
   * get error message.
   * 
   * @return error message or null, if no error occurs
   */
  String getErrorMessage();

  /**
   * set localized text for label.
   * 
   * @param labelText
   *        localized text
   */
  void setLabelEntry(String labelText);

  /**
   * get label for widget.
   * 
   * @return label
   */
  Label getLabel();

  /**
   * get widget.
   * 
   * @return widget
   */
  Widget getWidget();
}
