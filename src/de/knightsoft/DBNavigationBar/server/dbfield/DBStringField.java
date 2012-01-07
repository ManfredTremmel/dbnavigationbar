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
package de.knightsoft.DBNavigationBar.server.dbfield;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import de.knightsoft.DBNavigationBar.shared.fields.StringField;

/**
 *
 * <code>DBStringField</code> is a class to define a String field.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-11-01
 */
public abstract class DBStringField
    implements Serializable, DBFieldInterface<StringField> {

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
    private final StringField field;

    /**
     * comment field.
     */
    private final String comment;

    /**
     * constructor.
     * @param setDBFieldName db field name
     * @param setField the field to depend on
     * @param setComment comment
     */
    public DBStringField(
            final String setDBFieldName,
            final StringField setField,
            final String setComment
            ) {
        this.dbFieldName = setDBFieldName;
        this.field = setField;
        this.comment = setComment;
        this.field.setValue(this.field.getDefaultValue());
    }

    @Override
    public final int getFieldType() {
        return Types.VARCHAR;
    }

    @Override
    public final String getDBFieldName() {
        return this.dbFieldName;
    }

    @Override
    public final StringField getField() {
        return this.field;
    }

    @Override
    public final boolean isAutoIncrement() {
        return false;
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
                 && (rsmd.getPrecision(i) == this.field.getMaxLength())) {
                    changed = false;
                }
            }
        }
        return changed;
    }

    @Override
    public final void readFromResultSet(final ResultSet result
            ) throws SQLException {
        this.field.setValue(result.getString(this.dbFieldName));
    }

    @Override
    public final void addToPreparedStatement(final PreparedStatement statement,
            final int pos) throws SQLException {
        statement.setString(pos, this.field.getValue());
    }
}
