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
package de.knightsoft.dbnavigationbar.server.dbfield;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;

/**
 * 
 * <code>DBFieldInterface</code> is a class to define a field.
 * 
 * @param <E>
 *        field type (KnightSoft DBNavigation field type)
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DBFieldInterface<E>
{

  /**
   * get db field type.
   * 
   * @return field type
   */
  int getFieldType();

  /**
   * get db field name.
   * 
   * @return database field name
   */
  String getDBFieldName();

  /**
   * get field.
   * 
   * @return field
   */
  E getField();

  /**
   * get is AutoIncrement.
   * 
   * @return true if field counts up itselve.
   */
  boolean isAutoIncrement();

  /**
   * get comment.
   * 
   * @return comment
   */
  String getComment();

  /**
   * build a sql string for create/alter table.
   * 
   * @return sql field string
   */
  String buildSQLFieldString();

  /**
   * check if field has changed.
   * 
   * @param rsmd
   *        metadata to take current db state from
   * @return true if field has changed
   * @throws SQLException
   *         if error occurs
   */
  boolean fieldHasChanged(final ResultSetMetaData rsmd) throws SQLException;

  /**
   * create part of prepared update statement for this field.
   * 
   * @return String with prepared part
   */
  String preparedUpdatePart();

  /**
   * create part of prepared insert or read statement for this field.
   * 
   * @return String with sql part
   */
  String preparedInsertReadPart();

  /**
   * create part of insert statement for this field.
   * 
   * @return String with sql part
   */
  String preparedInsertValuesPart();

  /**
   * read from ResultSet.
   * 
   * @param result
   *        the ResultSet to read from
   * @param fieldToFill
   *        the field to fill data into
   * @throws SQLException
   *         if read fails
   */
  void readFromResultSet(final ResultSet result,
      final FieldInterface<?> fieldToFill)
      throws SQLException;

  /**
   * read from ResultSet.
   * 
   * @param result
   *        the ResultSet to read from
   * @param fieldName
   *        name of the field to read
   * @param fieldToFill
   *        the field to fill data into
   * @throws SQLException
   *         if read fails
   */
  void readFromResultSet(final ResultSet result,
      final String fieldName, final FieldInterface<?> fieldToFill
      ) throws SQLException;

  /**
   * add value to a prepared statement.
   * 
   * @param statement
   *        statement to add.
   * @param pos
   *        position number
   * @param fieldToSet
   *        field to take entry from
   * @return new position number after adding to statement
   * @throws SQLException
   *         if set fails
   */
  int addToPreparedStatement(final PreparedStatement statement,
      final int pos, final FieldInterface<?> fieldToSet)
      throws SQLException;
}
