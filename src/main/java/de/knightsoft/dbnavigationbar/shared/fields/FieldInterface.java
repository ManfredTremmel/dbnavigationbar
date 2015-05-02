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

package de.knightsoft.dbnavigationbar.shared.fields;

import java.text.ParseException;

/**
 *
 * <code>FieldInterface</code> is a class to define a field.
 *
 * @param <E> field type
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface FieldInterface<E> {

  E getValue();

  /**
   * get default value.
   *
   * @return default value
   */
  E getDefaultValue();

  void setValue(final E pValue);

  void setValueObject(Object pValue);

  String getString();

  void setString(final String pString) throws ParseException;

  boolean isCanBeNull();

  int getMaxLength();

  /**
   * get is primary key.
   *
   * @return true if field is a primary key field.
   */
  boolean isPrimaryKey();

  /**
   * check if entry is ok.
   *
   * @return true if all is ok.
   */
  boolean isOK();
}
