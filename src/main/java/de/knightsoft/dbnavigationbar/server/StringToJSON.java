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
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

/**
 *
 * <code>StringToJSON</code> is a class to convert a string to JSON.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public final class StringToJSON {

  /**
   * multiplicator maximum growth of the string.
   */
  private static final int MULTI = 6;
  /**
   * maximum length of the value not to encode.
   */
  private static final int MAX_LENGTH = 127;
  /**
   * three.
   */
  private static final int THREE = 3;

  /**
   * Private Constructor.
   */
  private StringToJSON() {
    super();
  }

  /**
   * The <code>convert</code> method converts the string to html equivalent.
   *
   * @param pJavaString Java string to convert
   * @return string as JSON
   */
  public static String convert(final String pJavaString) {
    String jsonString = "";

    if (pJavaString != null && pJavaString.length() > 0) {
      final char[] jsonStringTab = new char[MULTI * pJavaString.length()];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\"':
          case '\\':
          case '/':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = javaStringTab[i];
            break;
          case '\b':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = 'b';
            break;
          case '\f':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = 'f';
            break;
          case '\n':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = 'n';
            break;
          case '\r':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = 'r';
            break;
          case '\t':
            jsonStringTab[pos++] = '\\';
            jsonStringTab[pos++] = 't';
            break;
          default:
            final int test = javaStringTab[i];
            if (test > MAX_LENGTH) {
              jsonStringTab[pos++] = '\\';
              jsonStringTab[pos++] = 'u';
              final String jasonHex = String.format("%04x", Integer.valueOf(test));
              jsonStringTab[pos++] = jasonHex.charAt(0);
              jsonStringTab[pos++] = jasonHex.charAt(1);
              jsonStringTab[pos++] = jasonHex.charAt(2);
              jsonStringTab[pos++] = jasonHex.charAt(THREE);
            } else {
              jsonStringTab[pos++] = javaStringTab[i];
            }
            break;
        }
      }

      jsonString = new String(jsonStringTab, 0, pos);
    }

    return jsonString;
  }
}
