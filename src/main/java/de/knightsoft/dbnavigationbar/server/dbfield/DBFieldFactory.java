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

package de.knightsoft.dbnavigationbar.server.dbfield;

import de.knightsoft.dbnavigationbar.server.dbfield.mysql.MySQLDBIntegerField;
import de.knightsoft.dbnavigationbar.server.dbfield.mysql.MySQLDBStringField;
import de.knightsoft.dbnavigationbar.shared.Constants;
import de.knightsoft.dbnavigationbar.shared.fields.EMailField;
import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;
import de.knightsoft.dbnavigationbar.shared.fields.IntegerField;
import de.knightsoft.dbnavigationbar.shared.fields.StringField;

/**
 *
 * The <code>DBFieldFactory</code> includes a static method to create a DBField out of a field.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
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
   * @param pDbDriver name of db driver
   * @param pFieldName name of the field
   * @param pFieldInterface the field itselve
   * @return DBFieldInterface
   */
  public static DBFieldInterface<?> createDBField(final String pDbDriver, final String pFieldName,
      final FieldInterface<?> pFieldInterface) {
    DBFieldInterface<?> dbField = null;
    // MySQL setup
    if (Constants.JDBC_CLASS_MYSQL.equals(pDbDriver)) {
      if (pFieldInterface instanceof EMailField || pFieldInterface instanceof StringField) {
        // eMail and string fields are mapped to db string field
        dbField = new MySQLDBStringField(pFieldName, (StringField) pFieldInterface, null);
      } else if (pFieldInterface instanceof IntegerField) {
        // integer field is mapped to db integer field
        dbField = new MySQLDBIntegerField(pFieldName, (IntegerField) pFieldInterface, null, false);
      }
    }
    return dbField;
  }
}
