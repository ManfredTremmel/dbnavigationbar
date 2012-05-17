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
 * Copyright (c) 2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server.dbfield;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import de.knightsoft.DBNavigationBar.shared.fields.IntegerField;

/**
 *
 * <code>DBIntegerField</code> is a class to define a Integer field.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2012-05-17
 */
public abstract class DBIntegerField
    implements Serializable, DBFieldInterface<IntegerField> {

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
     * @param setDBFieldName db field name
     * @param setField the field to depend on
     * @param setComment comment
     * @param setAutoIncremental auto increment field true/false
     */
    public DBIntegerField(
            final String setDBFieldName,
            final IntegerField setField,
            final String setComment,
            final boolean setAutoIncremental
            ) {
        this.dbFieldName = setDBFieldName;
        this.field = setField;
        this.comment = setComment;
        this.field.setValue(this.field.getDefaultValue());
        this.autoIncremental = setAutoIncremental;
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
    public final boolean fieldHasChanged(final ResultSetMetaData rsmd)
            throws SQLException {
        boolean changed = true;
        boolean fieldFound = false;
        for (int i = 0; i < rsmd.getColumnCount() && !fieldFound; i++) {
            if (rsmd.getColumnName(i).equals(this.dbFieldName)) {
                fieldFound = true;
                if (rsmd.getColumnType(i) == this.getFieldType()
                 && (((rsmd.isNullable(i) == ResultSetMetaData.columnNullable)
                   && this.field.isCanBeNull())
                  || ((rsmd.isNullable(i) == ResultSetMetaData.columnNoNulls)
                   && !this.field.isCanBeNull()))
                 && (rsmd.getPrecision(i) == this.field.getMaxLength())
                 && (rsmd.isAutoIncrement(i) == this.isAutoIncrement())) {
                    changed = false;
                }
            }
        }
        return changed;
    }

    @Override
    public final void readFromResultSet(final ResultSet result
            ) throws SQLException {
        final int intResult = result.getInt(this.dbFieldName);
        if (result.wasNull()) {
            this.field.setValue(null);
        } else {
            this.field.setValue(Integer.valueOf(intResult));
        }
    }

    @Override
    public final void addToPreparedStatement(final PreparedStatement statement,
            final int pos) throws SQLException {
        if (this.field.getValue() == null) {
            statement.setNull(pos, this.getFieldType());
        } else {
            statement.setInt(pos, this.field.getValue());
        }
    }
}
