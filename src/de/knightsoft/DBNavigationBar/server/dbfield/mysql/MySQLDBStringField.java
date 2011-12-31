package de.knightsoft.DBNavigationBar.server.dbfield.mysql;

import de.knightsoft.DBNavigationBar.server.StringToSQL;
import de.knightsoft.DBNavigationBar.server.dbfield.DBStringField;
import de.knightsoft.DBNavigationBar.shared.Constants;
import de.knightsoft.DBNavigationBar.shared.fields.StringField;

/**
 *
 * <code>MySQLDBStringField</code> is a class to define a String field
 * including mysql specific parts.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-11-01
 */
public class MySQLDBStringField extends DBStringField {

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
     * @param setDBFieldName db field name
     * @param setField the field to depend on
     * @param setComment comment
     */
    public MySQLDBStringField(
            final String setDBFieldName,
            final StringField setField,
            final String setComment
            ) {
        super(setDBFieldName, setField, setComment);
    }

    @Override
    public final String buildSQLFieldString() {
        StringBuilder sqlString = new StringBuilder();
        sqlString.append("`" + StringToSQL.convert(this.getDBFieldName(),
                Constants.JDBC_CLASS_MYSQL) + "` ");
        if (this.getField().getMaxLength() > VARCHAR_MAX_LENGTH) {
            sqlString.append("text");
        } else {
            sqlString.append("varchar");
        }
        sqlString.append("(" + Integer.toString(this.getField().getMaxLength())
                + ")");
        if (this.getField().isCanBeNull()) {
            if (this.getField().getDefaultValue() == null) {
                sqlString.append(" DEFAULT NULL");
            } else {
                sqlString.append(" DEFAULT '"
                        + StringToSQL.convert(this.getField().getDefaultValue(),
                                Constants.JDBC_CLASS_MYSQL) + "'");
            }
        } else {
            sqlString.append(" NOT NULL");
            if (this.getField().getDefaultValue() != null) {
                sqlString.append(" DEFAULT '"
                        + StringToSQL.convert(this.getField().getDefaultValue(),
                                Constants.JDBC_CLASS_MYSQL) + "'");
            }
        }
        if (this.getComment() != null) {
            sqlString.append(" COMMENT '" + StringToSQL.convert(
                    this.getComment(), Constants.JDBC_CLASS_MYSQL) + "'");
        }
        return sqlString.toString();
    }

}
