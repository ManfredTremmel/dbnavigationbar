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

import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;
import de.knightsoft.dbnavigationbar.shared.fields.IntegerField;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * <code>AbstractDBIntegerField</code> is a class to define a Integer field.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBIntegerField implements Serializable, DBFieldInterface<IntegerField> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 6152462550448644530L;

  /**
   * database field name.
   */
  private final String dbFieldName;

  /**
   * string field.
   */
  private final IntegerField field;

  /**
   * comment field.
   */
  private final String comment;

  /**
   * is this field auto incremental.
   */
  private final boolean autoIncremental;

  /**
   * constructor.
   *
   * @param pDBFieldName db field name
   * @param pField the field to depend on
   * @param pComment comment
   * @param pAutoIncremental auto increment field true/false
   */
  public AbstractDBIntegerField(final String pDBFieldName, final IntegerField pField, final String pComment,
      final boolean pAutoIncremental) {
    this.dbFieldName = pDBFieldName;
    this.field = pField;
    this.comment = pComment;
    this.field.setValue(this.field.getDefaultValue());
    this.autoIncremental = pAutoIncremental;
  }

  @Override
  public final int getFieldType() {
    return Types.INTEGER;
  }

  @Override
  public final String getDBFieldName() {
    return this.dbFieldName;
  }

  @Override
  public final IntegerField getField() {
    return this.field;
  }

  @Override
  public final boolean isAutoIncrement() {
    return this.autoIncremental;
  }

  @Override
  public final String getComment() {
    return this.comment;
  }

  @Override
  public abstract String buildSQLFieldString();

  @Override
  public final boolean fieldHasChanged(final ResultSetMetaData pRsmd) throws SQLException {
    boolean changed = true;
    boolean fieldFound = false;
    for (int i = 0; i < pRsmd.getColumnCount() && !fieldFound; i++) {
      if (pRsmd.getColumnName(i).equals(this.dbFieldName)) {
        fieldFound = true;
        if (pRsmd.getColumnType(i) == this.getFieldType()
            && (((pRsmd.isNullable(i) == ResultSetMetaData.columnNullable) && this.field.isCanBeNull()) || ((pRsmd
                .isNullable(i) == ResultSetMetaData.columnNoNulls) && !this.field.isCanBeNull()))
            && (pRsmd.getPrecision(i) == this.field.getMaxLength()) && (pRsmd.isAutoIncrement(i) == this.isAutoIncrement())) {
          changed = false;
        }
      }
    }
    return changed;
  }

  @Override
  public final void readFromResultSet(final ResultSet pResult, final FieldInterface<?> pFieldToFill) throws SQLException {
    this.readFromResultSet(pResult, this.dbFieldName, pFieldToFill);
  }

  @Override
  public final void readFromResultSet(final ResultSet pResult, final String pFieldName, final FieldInterface<?> pFieldToFill)
      throws SQLException {
    if (pFieldToFill instanceof IntegerField) {
      final int intResult = pResult.getInt(this.dbFieldName);
      if (pResult.wasNull()) {
        pFieldToFill.setValue(null);
      } else {
        ((IntegerField) pFieldToFill).setValue(Integer.valueOf(intResult));
      }
    }
  }

  @Override
  public final int addToPreparedStatement(final PreparedStatement pStatement, final int pPos,
      final FieldInterface<?> pFieldToSet) throws SQLException {
    if (pFieldToSet.getValue() == null) {
      pStatement.setNull(pPos, this.getFieldType());
    } else if (pFieldToSet instanceof IntegerField) {
      pStatement.setInt(pPos, ((IntegerField) pFieldToSet).getValue().intValue());
    } else {
      pStatement.setNull(pPos, this.getFieldType());
    }
    return pPos + 1;
  }

  @Override
  public final String preparedUpdatePart() {
    return this.dbFieldName + " = ?";
  }

  @Override
  public final String preparedInsertReadPart() {
    return this.dbFieldName;
  }

  @Override
  public final String preparedInsertValuesPart() {
    return "?";
  }
}
