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
 * Copyright (c) 2011-2012 RI Solutions GmbH
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.shared.fields;

import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * <code>StringField</code> is a class to define a String field.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-11-01
 */
public class StringField
    extends AbstractField<String>
    implements Serializable, FieldInterface<String> {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -7745497749409953830L;

    /**
     * regular expression to check the entry.
     */
    private final String regExCheck;

    /**
     * constructor.
     * @param setCanBeNull true if value allowed to be null
     * @param setPrimaryKey is primary key
     * @param setMaxLength maximum length of the value
     * @param setDefaultValue default value
     */
    public StringField(final boolean setCanBeNull,
            final boolean setPrimaryKey,
            final int setMaxLength,
            final String setDefaultValue) {
        super(setCanBeNull, setPrimaryKey, setMaxLength, setDefaultValue);
        this.regExCheck = null;
    }

    /**
     * constructor.
     * @param setCanBeNull true if value allowed to be null
     * @param setPrimaryKey is primary key
     * @param setMaxLength maximum length of the value
     * @param setDefaultValue default value
     * @param setRegExCheck regular expression to check file
     */
    public StringField(final boolean setCanBeNull,
            final boolean setPrimaryKey,
            final int setMaxLength,
            final String setDefaultValue,
            final String setRegExCheck) {
        super(setCanBeNull, setPrimaryKey, setMaxLength, setDefaultValue);
        this.regExCheck = setRegExCheck;
    }

    @Override
    public final String getString() {
        return super.getValue();
    }

    @Override
    public final void setString(final String sString) throws ParseException {
        super.setValue(sString);
    }

    @Override
    public final boolean isOK() {
        boolean checkOk = !super.checkNullError();
        if (checkOk) {
            if ((super.getValue() != null)
             && (super.getValue().length() > this.getMaxLength())) {
                checkOk = false;
            }
            if (checkOk
             && this.regExCheck != null
             && this.getValue() != null) {
                checkOk = this.getValue().matches(this.regExCheck);
            }
        }
        return checkOk;
    }
}
