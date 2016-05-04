/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.shared;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * <code>StringToHTML</code> is a class to convert a string to HTML.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public final class StringToHTML {

  /**
   * multiplicator maximum growth of the string.
   */
  private static final int MULTI = 8;
  /**
   * maximum length of the value not to encode.
   */
  private static final int MAX_LENGTH = 127;

  /**
   * conversion table with html translations.
   */
  private static final char[][] CONVERSION_TABLE = {{'¡', '&', 'i', 'e', 'x', 'c', 'l', ';'},
      {'¢', '&', 'c', 'e', 'n', 't', ';'}, {'£', '&', 'p', 'o', 'u', 'n', 'd', ';'},
      {'€', '&', 'e', 'u', 'r', 'o', ';'}, {'¥', '&', 'y', 'e', 'n', ';'},
      {'§', '&', 's', 'e', 'c', 't', ';'}, {'©', '&', 'c', 'o', 'p', 'y', ';'},
      {'ª', '&', 'o', 'r', 'd', 'f', ';'}, {'«', '&', 'l', 'a', 'q', 'u', 'o', ';'},
      {'¬', '&', 'n', 'o', 't', ';'}, {'­', '&', 's', 'h', 'y', ';'},
      {'®', '&', 'r', 'e', 'g', ';'}, {'¯', '&', 'm', 'a', 'c', 'r', ';'},
      {'°', '&', 'd', 'e', 'g', ';'}, {'±', '&', 'p', 'l', 'u', 's', 'm', 'n', ';'},
      {'²', '&', 's', 'u', 'p', '2', ';'}, {'³', '&', 's', 'u', 'p', '3', ';'},
      {'µ', '&', 'm', 'i', 'c', 'r', 'o', ';'}, {'¶', '&', 'p', 'a', 'r', 'a', ';'},
      {'·', '&', 'm', 'i', 'd', 'd', 'o', 't', ';'}, {'¹', '&', 's', 'u', 'p', '1', ';'},
      {'º', '&', 'o', 'r', 'd', 'm', ';'}, {'»', '&', 'r', 'a', 'q', 'u', 'o', ';'},
      {'¿', '&', 'i', 'q', 'u', 'e', 's', 't', ';'}, {'À', '&', 'A', 'g', 'r', 'a', 'v', 'e', ';'},
      {'Á', '&', 'A', 'a', 'c', 'u', 't', 'e', ';'}, {'Â', '&', 'A', 'c', 'i', 'r', 'c', ';'},
      {'Ã', '&', 'A', 't', 'i', 'l', 'd', 'e', ';'}, {'Ä', '&', 'A', 'u', 'm', 'l', ';'},
      {'Å', '&', 'A', 'r', 'i', 'n', 'g', ';'}, {'Æ', '&', 'A', 'E', 'l', 'i', 'g', ';'},
      {'Ç', '&', 'C', 'c', 'e', 'd', 'i', 'l', ';'}, {'È', '&', 'E', 'g', 'r', 'a', 'v', 'e', ';'},
      {'É', '&', 'E', 'a', 'c', 'u', 't', 'e', ';'}, {'Ê', '&', 'E', 'c', 'i', 'r', 'c', ';'},
      {'Ë', '&', 'E', 'u', 'm', 'l', ';'}, {'Ì', '&', 'I', 'g', 'r', 'a', 'v', 'e', ';'},
      {'Í', '&', 'I', 'a', 'c', 'u', 't', 'e', ';'}, {'Î', '&', 'I', 'c', 'i', 'r', 'c', ';'},
      {'Ï', '&', 'I', 'u', 'm', 'l', ';'}, {'Ð', '&', 'E', 'T', 'H', ';'},
      {'Ñ', '&', 'N', 't', 'i', 'l', 'd', 'e', ';'}, {'Ò', '&', 'O', 'g', 'r', 'a', 'v', 'e', ';'},
      {'Ó', '&', 'O', 'a', 'c', 'u', 't', 'e', ';'}, {'Ô', '&', 'O', 'c', 'i', 'r', 'c', ';'},
      {'Õ', '&', 'O', 't', 'i', 'l', 'd', 'e', ';'}, {'Ö', '&', 'O', 'u', 'm', 'l', ';'},
      {'×', '&', 't', 'i', 'm', 'e', 's', ';'}, {'Ø', '&', 'O', 's', 'l', 'a', 's', 'h', ';'},
      {'Ù', '&', 'U', 'g', 'r', 'a', 'v', 'e', ';'}, {'Ú', '&', 'U', 'a', 'c', 'u', 't', 'e', ';'},
      {'Û', '&', 'U', 'c', 'i', 'r', 'c', ';'}, {'Ü', '&', 'U', 'u', 'm', 'l', ';'},
      {'Ý', '&', 'Y', 'a', 'c', 'u', 't', 'e', ';'}, {'Þ', '&', 'T', 'H', 'O', 'R', 'N', ';'},
      {'ß', '&', 's', 'z', 'l', 'i', 'g', ';'}, {'à', '&', 'a', 'g', 'r', 'a', 'v', 'e', ';'},
      {'á', '&', 'a', 'a', 'c', 'u', 't', 'e', ';'}, {'â', '&', 'a', 'c', 'i', 'r', 'c', ';'},
      {'ã', '&', 'a', 't', 'i', 'l', 'd', 'e', ';'}, {'ä', '&', 'a', 'u', 'm', 'l', ';'},
      {'å', '&', 'a', 'r', 'i', 'n', 'g', ';'}, {'æ', '&', 'a', 'e', 'l', 'i', 'g', ';'},
      {'ç', '&', 'c', 'c', 'e', 'd', 'i', 'l', ';'}, {'è', '&', 'e', 'g', 'r', 'a', 'v', 'e', ';'},
      {'é', '&', 'e', 'a', 'c', 'u', 't', 'e', ';'}, {'ê', '&', 'e', 'c', 'i', 'r', 'c', ';'},
      {'ë', '&', 'e', 'u', 'm', 'l', ';'}, {'ì', '&', 'i', 'g', 'r', 'a', 'v', 'e', ';'},
      {'í', '&', 'i', 'a', 'c', 'u', 't', 'e', ';'}, {'î', '&', 'i', 'c', 'i', 'r', 'c', ';'},
      {'ï', '&', 'i', 'u', 'm', 'l', ';'}, {'ð', '&', 'e', 't', 'h', ';'},
      {'ñ', '&', 'n', 't', 'i', 'l', 'd', 'e', ';'}, {'ò', '&', 'o', 'g', 'r', 'a', 'v', 'e', ';'},
      {'ó', '&', 'o', 'a', 'c', 'u', 't', 'e', ';'}, {'ô', '&', 'o', 'c', 'i', 'r', 'c', ';'},
      {'õ', '&', 'o', 't', 'i', 'l', 'd', 'e', ';'}, {'ö', '&', 'o', 'u', 'm', 'l', ';'},
      {'÷', '&', 'd', 'i', 'v', 'i', 'd', 'e', ';'}, {'ø', '&', 'o', 's', 'l', 'a', 's', 'h', ';'},
      {'ù', '&', 'u', 'g', 'r', 'a', 'v', 'e', ';'}, {'ú', '&', 'u', 'a', 'c', 'u', 't', 'e', ';'},
      {'û', '&', 'u', 'c', 'i', 'r', 'c', ';'}, {'ü', '&', 'u', 'u', 'm', 'l', ';'},
      {'ý', '&', 'y', 'a', 'c', 'u', 't', 'e', ';'}, {'þ', '&', 't', 'h', 'o', 'r', 'n', ';'},
      {'ÿ', '&', 'y', 'u', 'm', 'l', ';'}, {'\"', '&', 'q', 'u', 'o', 't', ';'},
      {'&', '&', 'a', 'm', 'p', ';'}, {'<', '&', 'l', 't', ';'}, {'>', '&', 'g', 't', ';'},};

  /**
   * Private Constructor.
   */
  private StringToHTML() {
    super();
  }

  /**
   * The <code>convert</code> method converts the string to a table of dos charset bytes.
   *
   * @param pJavaString Java string to convert
   * @return string as HTML
   */
  public static String convert(final String pJavaString) {
    return StringToHTML.convert(pJavaString, false, true, true);
  }

  /**
   * The <code>convert</code> method converts the string to a table of dos charset bytes.
   *
   * @param pJavaString Java string to convert
   * @param pBlankConvert convert blank to &nbsp; (true/false)
   * @return string as HTML
   */
  public static String convert(final String pJavaString, final boolean pBlankConvert) {
    return StringToHTML.convert(pJavaString, pBlankConvert, true, true);
  }

  /**
   * The <code>convert</code> method converts the string to a table of dos charset bytes.
   *
   * @param pJavaString Java string to convert
   * @param pBlankConvert convert blank to &nbsp; (true/false)
   * @param pReturnwandeln convert linefeed to <br>
   *        (true/false)
   * @return string as HTML
   */
  public static String convert(final String pJavaString, final boolean pBlankConvert,
      final boolean pReturnwandeln) {
    return StringToHTML.convert(pJavaString, pBlankConvert, pReturnwandeln, true);
  }

  /**
   * The <code>convert</code> method converts the string to html equivalent.
   *
   * @param pJavaString Java string to convert
   * @param pBlankConvert convert blank to &nbsp; (true/false)
   * @param pReturnConvert convert linefeed to <br>
   *        (true/false)
   * @param pTagConvert < to &lt; and > to &gt; (true/false)
   * @return string as HTML
   */
  public static String convert(final String pJavaString, final boolean pBlankConvert,
      final boolean pReturnConvert, final boolean pTagConvert) {
    String htmlString = StringUtils.EMPTY;

    if (pJavaString != null && pJavaString.length() > 0) {
      final char[] htmlStringTab = new char[StringToHTML.MULTI * pJavaString.length()];
      final char[] javaStringTab = pJavaString.toCharArray();
      int pos = 0;

      for (int i = 0; i < pJavaString.length(); i++) {
        switch (javaStringTab[i]) {
          case '\"':
            if (pTagConvert) {
              htmlStringTab[pos++] = '&';
              htmlStringTab[pos++] = 'q';
              htmlStringTab[pos++] = 'u';
              htmlStringTab[pos++] = 'o';
              htmlStringTab[pos++] = 't';
              htmlStringTab[pos++] = ';';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '&':
            if (pTagConvert) {
              htmlStringTab[pos++] = '&';
              htmlStringTab[pos++] = 'a';
              htmlStringTab[pos++] = 'm';
              htmlStringTab[pos++] = 'p';
              htmlStringTab[pos++] = ';';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '<':
            if (pTagConvert) {
              htmlStringTab[pos++] = '&';
              htmlStringTab[pos++] = 'l';
              htmlStringTab[pos++] = 't';
              htmlStringTab[pos++] = ';';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '>':
            if (pTagConvert) {
              htmlStringTab[pos++] = '&';
              htmlStringTab[pos++] = 'g';
              htmlStringTab[pos++] = 't';
              htmlStringTab[pos++] = ';';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case ' ':
            if (pBlankConvert) {
              htmlStringTab[pos++] = '&';
              htmlStringTab[pos++] = 'n';
              htmlStringTab[pos++] = 'b';
              htmlStringTab[pos++] = 's';
              htmlStringTab[pos++] = 'p';
              htmlStringTab[pos++] = ';';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          case '\r':
            break;
          case '\n':
            if (pReturnConvert) {
              htmlStringTab[pos++] = '<';
              htmlStringTab[pos++] = 'b';
              htmlStringTab[pos++] = 'r';
              htmlStringTab[pos++] = '>';
            } else {
              htmlStringTab[pos++] = javaStringTab[i];
            }
            break;
          default:
            boolean found = false;
            for (int c = 0; c < StringToHTML.CONVERSION_TABLE.length && !found; c++) {
              if (StringToHTML.CONVERSION_TABLE[c][0] == javaStringTab[i]) {
                found = true;
                for (int p = 1; p < StringToHTML.CONVERSION_TABLE[c].length; p++) {
                  htmlStringTab[pos++] = StringToHTML.CONVERSION_TABLE[c][p];
                }
              }
            }
            if (!found) {
              if (javaStringTab[i] > StringToHTML.MAX_LENGTH) {
                final String dummy = Integer.toString(javaStringTab[i]);
                htmlStringTab[pos++] = '&';
                htmlStringTab[pos++] = '#';
                for (int k = 0; k < dummy.length(); k++) {
                  htmlStringTab[pos++] = dummy.charAt(k);
                }
                htmlStringTab[pos++] = ';';
              } else {
                htmlStringTab[pos++] = javaStringTab[i];
              }
            }
            break;
        }
      }

      htmlString = new String(htmlStringTab, 0, pos);
    }

    return htmlString;
  }
}
