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
package de.knightsoft.DBNavigationBar.shared.fields;

import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * <code>AbstractField</code> is a class to define a field.
 * @param <E> field type
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2012-05-17
 */
public abstract class AbstractField<E>
    implements Serializable, FieldInterface<E> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 3989246926036547251L;

    /**
     * value.
     */
    private E value;

    /**
     * default value.
     */
    private final E defaultValue;

    /**
     * value can be null.
     */
    private final boolean canBeNull;

    /**
     * primary key.
     */
    private final boolean primaryKey;

    /**
     * maximum length of the String.
     */
    private final int maxLength;

    /**
     * constructor.
     * @param setCanBeNull true if value can be null, otherwise false.
     * @param setPrimaryKey true if this field is a primary key field.
     * @param setMaxLength maximum length of the value
     * @param setDefaultValue default value
     */
    public AbstractField(final boolean setCanBeNull,
            final boolean setPrimaryKey,
            final int setMaxLength,
            final E setDefaultValue) {
        this.canBeNull = setCanBeNull;
        this.primaryKey = setPrimaryKey;
        this.maxLength = setMaxLength;
        this.defaultValue = setDefaultValue;
    }

    @Override
    public final E getValue() {
        return this.value;
    }

    @Override
    public final E getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public final void setValue(final E sValue) {
        this.value = sValue;
    }

    @Override
    public abstract String getString();

    @Override
    public abstract void setString(final String sString) throws ParseException;

    @Override
    public final boolean isCanBeNull() {
        return this.canBeNull;
    }

    @Override
    public final int getMaxLength() {
        return this.maxLength;
    }

    @Override
    public final boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * check if value is null and null is not allowed.
     * @return true if a null error exist, otherwise false.
     */
    protected final boolean checkNullError() {
        return ((this.value == null) && !this.canBeNull);
    }

    /**
     * check if entry is ok.
     * @return true if all is ok.
     */
    @Override
    public abstract boolean isOK();
}
