/**
 * This file is part of knightsoft db navigation.
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
 * Copyright (c) 2011 RI Solutions GmbH
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.shared.fields;

import java.text.ParseException;

/**
 *
 * <code>FieldInterface</code> is a class to define a field.
 * @param <E> field type
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-11-01
 */
public interface FieldInterface<E> {

    /**
     * @return the value
     */
    E getValue();

    /**
     * get default value.
     * @return default value
     */
    E getDefaultValue();

    /**
     * @param sValue the value to set
     */
    void setValue(final E sValue);

    /**
     * get value as String.
     * @return value as String
     */
    String getString();

    /**
     * set value as String.
     * @param sString string to set
     * @throws ParseException if format can't be transformed to native format
     */
    void setString(final String sString) throws ParseException;

    /**
     * @return the canBeNull
     */
    boolean isCanBeNull();

    /**
     * @return the maxLength
     */
    int getMaxLength();

    /**
     * get is primary key.
     * @return true if field is a primary key field.
     */
    boolean isPrimaryKey();

    /**
     * check if entry is ok.
     * @return true if all is ok.
     */
    boolean isOK();
}
