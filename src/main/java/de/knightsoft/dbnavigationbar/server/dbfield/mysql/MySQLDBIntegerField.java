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
package de.knightsoft.dbnavigationbar.server.dbfield.mysql;

import de.knightsoft.dbnavigationbar.server.StringToSQL;
import de.knightsoft.dbnavigationbar.server.dbfield.AbstractDBIntegerField;
import de.knightsoft.dbnavigationbar.shared.Constants;
import de.knightsoft.dbnavigationbar.shared.fields.IntegerField;

/**
 * 
 * <code>MySQLDBIntegerField</code> is a class to define a Integer field
 * including mysql specific parts.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class MySQLDBIntegerField extends AbstractDBIntegerField
{

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 1666587104422659337L;

  /**
   * constructor.
   * 
   * @param setDBFieldName
   *        db field name
   * @param setField
   *        the field to depend on
   * @param setComment
   *        comment
   * @param setAutoIncremental
   *        auto increment field true/false
   */
  public MySQLDBIntegerField(
      final String setDBFieldName,
      final IntegerField setField,
      final String setComment,
      final boolean setAutoIncremental)
  {
    super(setDBFieldName, setField, setComment, setAutoIncremental);
  }

  @Override
  public final String buildSQLFieldString()
  {
    final int sqlLength = 47;
    final StringBuilder sqlString = new StringBuilder(sqlLength);
    sqlString.append("`" + StringToSQL.convert(this.getDBFieldName(),
        Constants.JDBC_CLASS_MYSQL) + "` ");
    sqlString.append("integer");
    sqlString.append("(" + Integer.toString(this.getField().getMaxLength())
        + ")");
    if (this.getField().isCanBeNull())
    {
      if (this.getField().getDefaultValue() == null)
      {
        sqlString.append(" DEFAULT NULL");
      }
      else
      {
        sqlString.append(" DEFAULT ").append(this.getField().getDefaultValue()
            .toString());
      }
    }
    else
    {
      sqlString.append(" NOT NULL");
      if (this.getField().getDefaultValue() != null)
      {
        sqlString.append(" DEFAULT ").append(this.getField().getDefaultValue()
            .toString());
      }
    }
    if (this.isAutoIncrement())
    {
      sqlString.append(" AUTO_INCREMENT");

    }
    if (this.getComment() != null)
    {
      sqlString.append(" COMMENT '" + StringToSQL.convert(
          this.getComment(), Constants.JDBC_CLASS_MYSQL) + "'");
    }
    return sqlString.toString();
  }

}
