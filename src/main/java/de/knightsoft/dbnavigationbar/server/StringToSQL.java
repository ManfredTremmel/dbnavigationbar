/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.shared.Constants;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 *
 * <code>StringToSQL</code> is a class to convert a string to a SQL string masking all non allowed signs to prevent against
 * SQL-Injection.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public final class StringToSQL {

  /**
   * Euro sign in windows 1252 character set.
   */
  private static final int EURO_SIGN = 128;

  /**
   * number three, stupid definition to fix warning.
   */
  private static final int THREE = 3;

  /**
   * Constructor.
   */
  private StringToSQL() {}

  /**
   * The <code>convertStringPrepared</code> method replaces &lt;br&gt; with LineFeed and the char 128 (stupid windows euro sign)
   * to 164.
   *
   * @param pJavaString Java string to convert
   * @return string as SQL
   */
  public static String convertStringPrepared(final String pJavaString) {
    return convertStringPrepared(pJavaString, Constants.JDBC_CLASS_MYSQL);
  }

  /**
   * The <code>convertStringPrepared</code> method replaces &lt;br&gt; with LineFeed and the char 128 (stupid windows euro sign)
   * to 164.
   *
   * @param pJavaString Java string to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String convertStringPrepared(final String pJavaString, final String pJdbcClass) {
    String sqlString = null;

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[2 * pJavaString.length()];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
        // case '\n':
        // SQLStringTab[j++] = '\\';
        // SQLStringTab[j++] = 'n';
        // break;
        // case '\t':
        // SQLStringTab[j++] = '\\';
        // SQLStringTab[j++] = 't';
        // break;
          case '\r':
            // SQLStringTab[j++] = '\\';
            // SQLStringTab[j++] = 'r';
            break;
          // case '\b':
          // SQLStringTab[j++] = '\\';
          // SQLStringTab[j++] = 'b';
          // break;
          case '<':
            if ((i + THREE) < pJavaString.length() && javaStringTab[i + 1] == 'b' && javaStringTab[i + 2] == 'r'
                && javaStringTab[i + THREE] == '>') {
              i += THREE;
              // SQLStringTab[j++] = '\\';
              // SQLStringTab[j++] = 'n';
              sqlStringTab[pos++] = '\n';
            } else {
              sqlStringTab[pos++] = javaStringTab[i];
            }
            break;
          // case '\\':
          // case '\'':
          // case '"':
          // SQLStringTab[j++] = '\\';
          // SQLStringTab[j++] = JavaStringTab[i];
          // break;
          case EURO_SIGN:
            sqlStringTab[pos++] = '€';
            break;
          default:
            sqlStringTab[pos++] = javaStringTab[i];
            break;
        }
      }
      sqlString = new String(sqlStringTab, 0, pos);
    }

    return sqlString;
  }

  /**
   * The <code>convert</code> method converts a Java string to SQL.
   *
   * @param pJavaString Java string to convert
   * @return string as SQL
   */
  public static String convert(final String pJavaString) {
    return convert(pJavaString, Constants.JDBC_CLASS_MYSQL);
  }

  /**
   * The <code>convert</code> method converts a Java string to SQL.
   *
   * @param pJavaString Java string to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String convert(final String pJavaString, final String pJdbcClass) {
    String convertedString = null;

    if (pJdbcClass == null || pJdbcClass.equals(Constants.JDBC_CLASS_MYSQL)) {
      convertedString = convertMySQL(pJavaString);
    } else if (pJdbcClass.equals(Constants.JDBC_CLASS_MSSQL)) {
      convertedString = convertMSSQL(pJavaString);
    } else {
      convertedString = convertMySQL(pJavaString);
    }
    return convertedString;
  }

  /**
   * The <code>convertMySQL</code> method converts a Java string to SQL.
   *
   * @param pJavaString Java string to convert MySQL Style
   * @return string as SQL
   */
  private static String convertMySQL(final String pJavaString) {
    String sqlString = null;

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[2 + (2 * pJavaString.length())];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      sqlStringTab[pos++] = '\'';

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\n':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'n';
            break;
          case '\t':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 't';
            break;
          case '\r':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'r';
            break;
          case '\b':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'b';
            break;
          case '<':
            if ((i + THREE) < pJavaString.length() && javaStringTab[i + 1] == 'b' && javaStringTab[i + 2] == 'r'
                && javaStringTab[i + THREE] == '>') {
              i += THREE;
              sqlStringTab[pos++] = '\\';
              sqlStringTab[pos++] = 'n';
            } else {
              sqlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '"':
          case '\\':
          case '\'':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = javaStringTab[i];
            break;
          case EURO_SIGN:
            sqlStringTab[pos++] = '€';
            break;
          default:
            sqlStringTab[pos++] += javaStringTab[i];
            break;
        }
      }
      sqlStringTab[pos++] = '\'';

      sqlString = new String(sqlStringTab, 0, pos);
    }

    return sqlString;
  }

  /**
   * The <code>convertMSSQL</code> method converts a Java string to SQL.
   *
   * @param pJavaString Java string to convert MSSQL Style
   * @return string as SQL
   */
  private static String convertMSSQL(final String pJavaString) {
    String sqlString = null;

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[2 + (2 * pJavaString.length())];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      sqlStringTab[pos++] = '\'';

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\n':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'n';
            break;
          case '\t':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 't';
            break;
          case '\r':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'r';
            break;
          case '\b':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'b';
            break;
          case '<':
            if ((i + THREE) < pJavaString.length() && javaStringTab[i + 1] == 'b' && javaStringTab[i + 2] == 'r'
                && javaStringTab[i + THREE] == '>') {
              i += THREE;
              sqlStringTab[pos++] = '\\';
              sqlStringTab[pos++] = 'n';
            } else {
              sqlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '\'':
            sqlStringTab[pos++] = javaStringTab[i];
            sqlStringTab[pos++] = javaStringTab[i];
            break;
          case EURO_SIGN:
            sqlStringTab[pos++] = '€';
            break;
          default:
            sqlStringTab[pos++] += javaStringTab[i];
            break;
        }
      }
      sqlStringTab[pos++] = '\'';

      sqlString = new String(sqlStringTab, 0, pos);
    }

    return sqlString;
  }

  /**
   * The <code>convertString</code> is the same as convert.
   *
   * @param pJavaString Java string to convert
   * @return string as SQL
   */
  public static String convertString(final String pJavaString) {
    return convert(pJavaString);
  }

  /**
   * The <code>convertString</code> is the same as convert.
   *
   * @param pJavaString Java string to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String convertString(final String pJavaString, final String pJdbcClass) {
    return convert(pJavaString, pJdbcClass);
  }

  /**
   * The <code>convertNumber</code> method converts a Java string, contains a number to SQL.
   *
   * @param pJavaString Java string to convert
   * @return string as SQL
   */
  public static String convertNumber(final String pJavaString) {
    return convertNumber(pJavaString, Constants.JDBC_CLASS_MYSQL);
  }

  /**
   * The <code>convertNumber</code> method converts a Java string, contains a number to SQL.
   *
   * @param pJavaString Java string to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String convertNumber(final String pJavaString, final String pJdbcClass) {
    String sqlString = null;

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[pJavaString.length()];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':
          case '.':
            sqlStringTab[pos++] += javaStringTab[i];
            break;
          default:
            break;
        }
      }

      if (pos > 0) {
        sqlString = new String(sqlStringTab, 0, pos);
      }
    }

    return sqlString;
  }

  /**
   * The <code>searchString</code> method prepares a string for searching in a SQL database using = or like.
   *
   * @param pDbFieldname database fieldname
   * @param pJavaString Java string to convert
   * @return string as SQL
   */
  public static String searchString(final String pDbFieldname, final String pJavaString) {
    return searchString(pDbFieldname, pJavaString, Constants.JDBC_CLASS_MYSQL);
  }

  /**
   * The <code>SearchString</code> method prepares a string for searching in a SQL database using = or like.
   *
   * @param pDbFieldname database fieldname
   * @param pJavaString Java string to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String searchString(final String pDbFieldname, final String pJavaString, final String pJdbcClass) {
    String convertedString = null;

    if (pJdbcClass == null || pJdbcClass.equals(Constants.JDBC_CLASS_MYSQL)) {
      convertedString = searchStringMySQL(pDbFieldname, pJavaString);
    } else if (pJdbcClass.equals(Constants.JDBC_CLASS_MSSQL)) {
      convertedString = searchStringMSSQL(pDbFieldname, pJavaString);
    } else {
      convertedString = searchStringMySQL(pDbFieldname, pJavaString);
    }
    return convertedString;
  }

  /**
   * The <code>searchStringMySQL</code> method prepares a string for searching in a SQL database using = or like.
   *
   * @param pDbFieldname database fieldname
   * @param pJavaString Java string to convert
   * @return string as SQL MySQL style
   */
  public static String searchStringMySQL(final String pDbFieldname, final String pJavaString) {
    String sqlString = "";

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[2 + (2 * pJavaString.length())];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;
      boolean wildcard = false;

      sqlStringTab[pos++] = '\'';

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\n':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'n';
            break;
          case '\t':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 't';
            break;
          case '\r':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'r';
            break;
          case '\b':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'b';
            break;
          case '<':
            if ((i + THREE) < pJavaString.length() && javaStringTab[i + 1] == 'b' && javaStringTab[i + 2] == 'r'
                && javaStringTab[i + THREE] == '>') {
              i += THREE;
              sqlStringTab[pos++] = '\\';
              sqlStringTab[pos++] = 'n';
            } else {
              sqlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '"':
          case '\\':
          case '\'':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = javaStringTab[i];
            break;
          case '*':
            sqlStringTab[pos++] = '%';
            wildcard = true;
            break;
          case '?':
            sqlStringTab[pos++] = '_';
            wildcard = true;
            break;
          case EURO_SIGN:
            sqlStringTab[pos++] = '€';
            break;
          default:
            sqlStringTab[pos++] += javaStringTab[i];
            break;
        }
      }
      sqlStringTab[pos++] = '\'';

      if (wildcard) {
        sqlString = pDbFieldname + " like " + new String(sqlStringTab, 0, pos);
      } else {
        sqlString = pDbFieldname + "=" + new String(sqlStringTab, 0, pos);
      }
    }

    return sqlString;
  }

  /**
   * The <code>searchStringMSSQL</code> method prepares a string for searching in a SQL database using = or like.
   *
   * @param pDbFieldname database fieldname
   * @param pJavaString Java string to convert
   * @return string as SQL MSSQL style
   */
  public static String searchStringMSSQL(final String pDbFieldname, final String pJavaString) {
    String sqlString = "";

    if (StringUtils.isNotEmpty(pJavaString)) {
      final char[] sqlStringTab = new char[2 + (2 * pJavaString.length())];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;
      boolean wildcard = false;

      sqlStringTab[pos++] = '\'';

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\n':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'n';
            break;
          case '\t':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 't';
            break;
          case '\r':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'r';
            break;
          case '\b':
            sqlStringTab[pos++] = '\\';
            sqlStringTab[pos++] = 'b';
            break;
          case '<':
            if ((i + THREE) < pJavaString.length() && javaStringTab[i + 1] == 'b' && javaStringTab[i + 2] == 'r'
                && javaStringTab[i + THREE] == '>') {
              i += THREE;
              sqlStringTab[pos++] = '\\';
              sqlStringTab[pos++] = 'n';
            } else {
              sqlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '\'':
            sqlStringTab[pos++] = javaStringTab[i];
            sqlStringTab[pos++] = javaStringTab[i];
            break;
          case '*':
            sqlStringTab[pos++] = '%';
            wildcard = true;
            break;
          case '?':
            sqlStringTab[pos++] = '_';
            wildcard = true;
            break;
          case EURO_SIGN:
            sqlStringTab[pos++] = '€';
            break;
          default:
            sqlStringTab[pos++] += javaStringTab[i];
            break;
        }
      }
      sqlStringTab[pos++] = '\'';

      if (wildcard) {
        sqlString = pDbFieldname + " like " + new String(sqlStringTab, 0, pos);
      } else {
        sqlString = pDbFieldname + "=" + new String(sqlStringTab, 0, pos);
      }
    }

    return sqlString;
  }

  /**
   * The <code>convertDatetime</code> method converts a datetime to SQL.
   *
   * @param pJavaDateTime Timestamp to convert
   * @param pJdbcClass name of the JDBC class to detect DataBase
   * @return string as SQL
   */
  public static String convertDatetime(final Timestamp pJavaDateTime, final String pJdbcClass) {
    String convertedString = null;

    if (pJdbcClass == null || pJdbcClass.equals(Constants.JDBC_CLASS_MYSQL)) {
      convertedString = convertDatetimeMySQL(pJavaDateTime);
    } else if (pJdbcClass.equals(Constants.JDBC_CLASS_MSSQL)) {
      convertedString = convertDatetimeMSSQL(pJavaDateTime);
    } else {
      convertedString = convertDatetimeMySQL(pJavaDateTime);
    }
    return convertedString;
  }

  /**
   * The <code>convertDatetimeMySQL</code> method converts a datetime to SQL.
   *
   * @param pJavaDateTime Timestamp to convert MySQL Style
   * @return string as SQL
   */
  private static String convertDatetimeMySQL(final Timestamp pJavaDateTime) {
    String sqlString = null;

    if (pJavaDateTime == null) {
      sqlString = "null";
    } else {
      sqlString = "\'" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(pJavaDateTime) + "\'";
    }

    return sqlString;
  }

  /**
   * The <code>convertDatetimeMSSQL</code> method converts a datetime to SQL.
   *
   * @param pJavaDateTime Timestamp to convert MSSQL Style
   * @return string as SQL
   */
  private static String convertDatetimeMSSQL(final Timestamp pJavaDateTime) {
    String sqlString = null;

    if (pJavaDateTime == null) {
      sqlString = "null";
    } else {
      sqlString =
          "CONVERT(DATETIME, '" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(pJavaDateTime) + "', 121)";
    }

    return sqlString;
  }

}
