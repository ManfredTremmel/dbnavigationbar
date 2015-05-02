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

package de.knightsoft.dbnavigationbar.shared;

/**
 *
 * The <code>Constants</code> contains the fixed fields.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class Constants {

  /**
   * use a connection pool or directly connect.
   */
  public static final boolean USE_CONNECTION_POOL = true;

  /**
   * jdbc class for mysql.
   */
  public static final String JDBC_CLASS_MYSQL = "com.mysql.jdbc.Driver";
  /**
   * jdbc class for mysql (older version).
   */
  public static final String JDBC_CLASS_MYSQL_OLD = "org.gjt.mm.mysql.Driver";
  /**
   * jdbc class for mssql.
   */
  public static final String JDBC_CLASS_MSSQL = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

  /**
   * database field name for mandator field.
   */
  public static final String DB_FIELD_GLOBAL_MANDATOR = "Mandator";

  /**
   * database field name for username field.
   */
  public static final String DB_FIELD_GLOBAL_USER = "Username";

  /**
   * database field name for date from field.
   */
  public static final String DB_FIELD_GLOBAL_DATE_FROM = "Date_from";

  /**
   * database field name for date to field.
   */
  public static final String DB_FIELD_GLOBAL_DATE_TO = "Date_to";

  /**
   * database field name for mandator field.
   */
  public static final String DB_FIELD_MANDATOR = DB_FIELD_GLOBAL_MANDATOR;

  /**
   * private constructor.
   */
  protected Constants() {
    super();
  }
}
