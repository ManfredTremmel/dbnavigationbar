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

/**
 *
 * <code>EMailField</code> is a class to define a EMail field.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2012-05-17
 */
public class EMailField extends StringField {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1141933082549174323L;
    /**
     * maximum length of a e-mail.
     */
    private static final int EMAIL_MAX_LENGTH = 255;

    /**
     * constructor.
     * @param setCanBeNull true if value allowed to be null
     * @param setPrimaryKey is primary key
     * @param setDefaultValue default value
     */
    public EMailField(final boolean setCanBeNull,
            final boolean setPrimaryKey,
            final String setDefaultValue) {
        super(setCanBeNull, setPrimaryKey, EMAIL_MAX_LENGTH, setDefaultValue,
                "^(([A-Za-z0-9]+_+)|([A-Za-z0-9]+\\-+)|([A-Za-z0-9]+\\.+)|"
                + "([A-Za-z0-9]+\\++))*[A-Za-z0-9]+@((\\w+\\-+)|(\\w+\\.))"
                + "*\\w{1,63}\\.[a-zA-Z]{2,6}$");
    }

}
