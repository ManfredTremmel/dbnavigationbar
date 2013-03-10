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
import de.knightsoft.dbnavigationbar.server.dbfield.AbstractDBStringField;
import de.knightsoft.dbnavigationbar.shared.Constants;
import de.knightsoft.dbnavigationbar.shared.fields.StringField;

/**
 * 
 * <code>MySQLDBStringField</code> is a class to define a String field
 * including mysql specific parts.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class MySQLDBStringField extends AbstractDBStringField
{

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 1666587104422659337L;

  /**
   * maximum length of a varchar mysql field.
   */
  private static final int VARCHAR_MAX_LENGTH = 65532;

  /**
   * constructor.
   * 
   * @param setDBFieldName
   *        db field name
   * @param setField
   *        the field to depend on
   * @param setComment
   *        comment
   */
  public MySQLDBStringField(
      final String setDBFieldName,
      final StringField setField,
      final String setComment)
  {
    super(setDBFieldName, setField, setComment);
  }

  @Override
  public final String buildSQLFieldString()
  {
    final StringBuilder sqlString = new StringBuilder();
    sqlString.append("`" + StringToSQL.convert(this.getDBFieldName(),
        Constants.JDBC_CLASS_MYSQL) + "` ");
    if (this.getField().getMaxLength() > VARCHAR_MAX_LENGTH)
    {
      sqlString.append("text");
    }
    else
    {
      sqlString.append("varchar");
    }
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
        sqlString.append(" DEFAULT '"
            + StringToSQL.convert(this.getField().getDefaultValue(),
                Constants.JDBC_CLASS_MYSQL) + "'");
      }
    }
    else
    {
      sqlString.append(" NOT NULL");
      if (this.getField().getDefaultValue() != null)
      {
        sqlString.append(" DEFAULT '"
            + StringToSQL.convert(this.getField().getDefaultValue(),
                Constants.JDBC_CLASS_MYSQL) + "'");
      }
    }
    if (this.getComment() != null)
    {
      sqlString.append(" COMMENT '" + StringToSQL.convert(
          this.getComment(), Constants.JDBC_CLASS_MYSQL) + "'");
    }
    return sqlString.toString();
  }

}
