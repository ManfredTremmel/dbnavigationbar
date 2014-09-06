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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 RI Solutions GmbH
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
public final class StringToJSON
{

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
  private StringToJSON()
  {
    super();
  }

  /**
   * The <code>convert</code> method converts the string to
   * html equivalent.
   * 
   * @param javaString
   *        Java string to convert
   * @return string as JSON
   */
  public static String convert(final String javaString
      )
  {
    String jsonString = "";

    if (javaString != null && javaString.length() > 0)
    {
      final char[] jsonStringTab = new char[MULTI * javaString.length()];
      final char[] javaStringTab = javaString.toCharArray();
      int j = 0;

      for (int i = 0; i < javaString.length(); i++)
      {
        switch (javaStringTab[i])
        {
          case '\"':
          case '\\':
          case '/':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = javaStringTab[i];
            break;
          case '\b':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = 'b';
            break;
          case '\f':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = 'f';
            break;
          case '\n':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = 'n';
            break;
          case '\r':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = 'r';
            break;
          case '\t':
            jsonStringTab[j++] = '\\';
            jsonStringTab[j++] = 't';
            break;
          default:
            final int test = javaStringTab[i];
            if (test > MAX_LENGTH)
            {
              jsonStringTab[j++] = '\\';
              jsonStringTab[j++] = 'u';
              final String jasonHex =
                  String.format("%04x", Integer.valueOf(test));
              jsonStringTab[j++] = jasonHex.charAt(0);
              jsonStringTab[j++] = jasonHex.charAt(1);
              jsonStringTab[j++] = jasonHex.charAt(2);
              jsonStringTab[j++] = jasonHex.charAt(THREE);
            }
            else
            {
              jsonStringTab[j++] = javaStringTab[i];
            }
            break;
        }
      }

      jsonString = new String(jsonStringTab, 0, j);
    }

    return jsonString;
  }
};
