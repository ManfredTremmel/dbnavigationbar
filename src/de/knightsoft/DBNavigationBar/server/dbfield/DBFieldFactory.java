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
 */
package de.knightsoft.DBNavigationBar.server.dbfield;

import de.knightsoft.DBNavigationBar.server.dbfield.mysql.MySQLDBIntegerField;
import de.knightsoft.DBNavigationBar.server.dbfield.mysql.MySQLDBStringField;
import de.knightsoft.DBNavigationBar.shared.Constants;
import de.knightsoft.DBNavigationBar.shared.fields.EMailField;
import de.knightsoft.DBNavigationBar.shared.fields.FieldInterface;
import de.knightsoft.DBNavigationBar.shared.fields.IntegerField;
import de.knightsoft.DBNavigationBar.shared.fields.StringField;

/**
*
 * The <code>DBFieldFactory</code> includes a static method to create
 * a DBField out of a field.
 *
 * @author Manfred Tremmel
 */
public final class DBFieldFactory {

    /**
     * private default constructor.
     */
    private DBFieldFactory() {
        super();
    }

    /**
     * build a dbField out of a field.
     *
     * @param dbDriver name of db driver
     * @param fieldName name of the field
     * @param fieldInterface the field itselve
     * @return DBFieldInterface
     */
    public static DBFieldInterface<?> createDBField(
            final String dbDriver,
            final String fieldName,
            final FieldInterface<?> fieldInterface) {
        DBFieldInterface<?> dbField = null;
        // MySQL setup
        if (Constants.JDBC_CLASS_MYSQL.equals(dbDriver)) {
            if (fieldInterface instanceof EMailField
             || fieldInterface instanceof StringField) {
                // eMail and string fields are mapped to db string field
                dbField = new MySQLDBStringField(
                        fieldName, (StringField) fieldInterface, null);
            } else if (fieldInterface instanceof IntegerField) {
                // integer field is mapped to db integer field
                dbField = new MySQLDBIntegerField(
                        fieldName, (IntegerField) fieldInterface, null, false);
            }
        }
        return dbField;
    }
}
