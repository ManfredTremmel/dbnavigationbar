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
package de.knightsoft.DBNavigationBar.shared;

/**
 *
 * <code>StringToHTML</code> is a class to convert a string to HTML.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-01-02
 */
public final class StringToHTML {

    /**
     * multiplicator maximum growth of the string.
     */
    private static final int MULTI         =   8;
    /**
     * maximum length of the value not to encode.
     */
    private static final int MAX_LENGTH    =   127;

    /**
     * conversion table with html translations.
     */
    private static final char[][] CONVERSION_TABLE =
        {
            {'¡', '&', 'i', 'e', 'x', 'c', 'l', ';'},
            {'¢', '&', 'c', 'e', 'n', 't', ';'},
            {'£', '&', 'p', 'o', 'u', 'n', 'd', ';'},
            {'€', '&', 'e', 'u', 'r', 'o', ';'},
            {'¥', '&', 'y', 'e', 'n', ';'},
            {'§', '&', 's', 'e', 'c', 't', ';'},
            {'©', '&', 'c', 'o', 'p', 'y', ';'},
            {'ª', '&', 'o', 'r', 'd', 'f', ';'},
            {'«', '&', 'l', 'a', 'q', 'u', 'o', ';'},
            {'¬', '&', 'n', 'o', 't', ';'},
            {'­', '&', 's', 'h', 'y', ';'},
            {'®', '&', 'r', 'e', 'g', ';'},
            {'¯', '&', 'm', 'a', 'c', 'r', ';'},
            {'°', '&', 'd', 'e', 'g', ';'},
            {'±', '&', 'p', 'l', 'u', 's', 'm', 'n', ';'},
            {'²', '&', 's', 'u', 'p', '2', ';'},
            {'³', '&', 's', 'u', 'p', '3', ';'},
            {'µ', '&', 'm', 'i', 'c', 'r', 'o', ';'},
            {'¶', '&', 'p', 'a', 'r', 'a', ';'},
            {'·', '&', 'm', 'i', 'd', 'd', 'o', 't', ';'},
            {'¹', '&', 's', 'u', 'p', '1', ';'},
            {'º', '&', 'o', 'r', 'd', 'm', ';'},
            {'»', '&', 'r', 'a', 'q', 'u', 'o', ';'},
            {'¿', '&', 'i', 'q', 'u', 'e', 's', 't', ';'},
            {'À', '&', 'A', 'g', 'r', 'a', 'v', 'e', ';'},
            {'Á', '&', 'A', 'a', 'c', 'u', 't', 'e', ';'},
            {'Â', '&', 'A', 'c', 'i', 'r', 'c', ';'},
            {'Ã', '&', 'A', 't', 'i', 'l', 'd', 'e', ';'},
            {'Ä', '&', 'A', 'u', 'm', 'l', ';'},
            {'Å', '&', 'A', 'r', 'i', 'n', 'g', ';'},
            {'Æ', '&', 'A', 'E', 'l', 'i', 'g', ';'},
            {'Ç', '&', 'C', 'c', 'e', 'd', 'i', 'l', ';'},
            {'È', '&', 'E', 'g', 'r', 'a', 'v', 'e', ';'},
            {'É', '&', 'E', 'a', 'c', 'u', 't', 'e', ';'},
            {'Ê', '&', 'E', 'c', 'i', 'r', 'c', ';'},
            {'Ë', '&', 'E', 'u', 'm', 'l', ';'},
            {'Ì', '&', 'I', 'g', 'r', 'a', 'v', 'e', ';'},
            {'Í', '&', 'I', 'a', 'c', 'u', 't', 'e', ';'},
            {'Î', '&', 'I', 'c', 'i', 'r', 'c', ';'},
            {'Ï', '&', 'I', 'u', 'm', 'l', ';'},
            {'Ð', '&', 'E', 'T', 'H', ';'},
            {'Ñ', '&', 'N', 't', 'i', 'l', 'd', 'e', ';'},
            {'Ò', '&', 'O', 'g', 'r', 'a', 'v', 'e', ';'},
            {'Ó', '&', 'O', 'a', 'c', 'u', 't', 'e', ';'},
            {'Ô', '&', 'O', 'c', 'i', 'r', 'c', ';'},
            {'Õ', '&', 'O', 't', 'i', 'l', 'd', 'e', ';'},
            {'Ö', '&', 'O', 'u', 'm', 'l', ';'},
            {'×', '&', 't', 'i', 'm', 'e', 's', ';'},
            {'Ø', '&', 'O', 's', 'l', 'a', 's', 'h', ';'},
            {'Ù', '&', 'U', 'g', 'r', 'a', 'v', 'e', ';'},
            {'Ú', '&', 'U', 'a', 'c', 'u', 't', 'e', ';'},
            {'Û', '&', 'U', 'c', 'i', 'r', 'c', ';'},
            {'Ü', '&', 'U', 'u', 'm', 'l', ';'},
            {'Ý', '&', 'Y', 'a', 'c', 'u', 't', 'e', ';'},
            {'Þ', '&', 'T', 'H', 'O', 'R', 'N', ';'},
            {'ß', '&', 's', 'z', 'l', 'i', 'g', ';'},
            {'à', '&', 'a', 'g', 'r', 'a', 'v', 'e', ';'},
            {'á', '&', 'a', 'a', 'c', 'u', 't', 'e', ';'},
            {'â', '&', 'a', 'c', 'i', 'r', 'c', ';'},
            {'ã', '&', 'a', 't', 'i', 'l', 'd', 'e', ';'},
            {'ä', '&', 'a', 'u', 'm', 'l', ';'},
            {'å', '&', 'a', 'r', 'i', 'n', 'g', ';'},
            {'æ', '&', 'a', 'e', 'l', 'i', 'g', ';'},
            {'ç', '&', 'c', 'c', 'e', 'd', 'i', 'l', ';'},
            {'è', '&', 'e', 'g', 'r', 'a', 'v', 'e', ';'},
            {'é', '&', 'e', 'a', 'c', 'u', 't', 'e', ';'},
            {'ê', '&', 'e', 'c', 'i', 'r', 'c', ';'},
            {'ë', '&', 'e', 'u', 'm', 'l', ';'},
            {'ì', '&', 'i', 'g', 'r', 'a', 'v', 'e', ';'},
            {'í', '&', 'i', 'a', 'c', 'u', 't', 'e', ';'},
            {'î', '&', 'i', 'c', 'i', 'r', 'c', ';'},
            {'ï', '&', 'i', 'u', 'm', 'l', ';'},
            {'ð', '&', 'e', 't', 'h', ';'},
            {'ñ', '&', 'n', 't', 'i', 'l', 'd', 'e', ';'},
            {'ò', '&', 'o', 'g', 'r', 'a', 'v', 'e', ';'},
            {'ó', '&', 'o', 'a', 'c', 'u', 't', 'e', ';'},
            {'ô', '&', 'o', 'c', 'i', 'r', 'c', ';'},
            {'õ', '&', 'o', 't', 'i', 'l', 'd', 'e', ';'},
            {'ö', '&', 'o', 'u', 'm', 'l', ';'},
            {'÷', '&', 'd', 'i', 'v', 'i', 'd', 'e', ';'},
            {'ø', '&', 'o', 's', 'l', 'a', 's', 'h', ';'},
            {'ù', '&', 'u', 'g', 'r', 'a', 'v', 'e', ';'},
            {'ú', '&', 'u', 'a', 'c', 'u', 't', 'e', ';'},
            {'û', '&', 'u', 'c', 'i', 'r', 'c', ';'},
            {'ü', '&', 'u', 'u', 'm', 'l', ';'},
            {'ý', '&', 'y', 'a', 'c', 'u', 't', 'e', ';'},
            {'þ', '&', 't', 'h', 'o', 'r', 'n', ';'},
            {'ÿ', '&', 'y', 'u', 'm', 'l', ';'},
            {'\"', '&', 'q', 'u', 'o', 't', ';'},
            {'&', '&', 'a', 'm', 'p', ';'},
            {'<', '&', 'l', 't', ';'},
            {'>', '&', 'g', 't', ';'},
        };

    /**
     * Private Constructor.
     */
    private StringToHTML() {
        super();
    }

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes.
     *
     * @param javaString
     *            Java string to convert
     * @return string as HTML
     */
    public static String convert(final String javaString) {
        return convert(javaString, false, true, true);
    }

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes.
     *
     * @param javaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @return string as HTML
     */
    public static String convert(final String javaString,
                                       final boolean blankwandeln
          ) {
        return convert(javaString, blankwandeln, true, true);
    }

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes.
     *
     * @param javaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @param returnwandeln
     *            convert linefeed to <br> (true/false)
     * @return string as HTML
     */
    public static String convert(final String javaString,
                                 final boolean blankwandeln,
                                 final boolean returnwandeln
          ) {
        return convert(javaString, blankwandeln, returnwandeln, true);
    }

    /**
     * The <code>convert</code> method converts the string to
     * html equivalent.
     *
     * @param javaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @param returnwandeln
     *            convert linefeed to <br> (true/false)
     * @param tagswandeln
     *            < to &lt; and > to &gt; (true/false)
     * @return string as HTML
     */
    public static String convert(final String javaString,
                                 final boolean blankwandeln,
                                 final boolean returnwandeln,
                                 final boolean tagswandeln
          ) {
        String htmlString       =   "";

        if (javaString != null && javaString.length() > 0) {
            char[] htmlStringTab = new char[(MULTI * javaString.length())];
            char[] javaStringTab = javaString.toCharArray();
            int j = 0;

            for (int i = 0; i < javaString.length(); i++) {
                switch (javaStringTab[i]) {
                case '\"':
                    if (tagswandeln) {
                        htmlStringTab[j++]    =    '&';
                        htmlStringTab[j++]    =    'q';
                        htmlStringTab[j++]    =    'u';
                        htmlStringTab[j++]    =    'o';
                        htmlStringTab[j++]    =    't';
                        htmlStringTab[j++]    =    ';';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                case '&':
                    if (tagswandeln) {
                        htmlStringTab[j++]    =    '&';
                        htmlStringTab[j++]    =    'a';
                        htmlStringTab[j++]    =    'm';
                        htmlStringTab[j++]    =    'p';
                        htmlStringTab[j++]    =    ';';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                case '<':
                    if (tagswandeln) {
                        htmlStringTab[j++]    =    '&';
                        htmlStringTab[j++]    =    'l';
                        htmlStringTab[j++]    =    't';
                        htmlStringTab[j++]    =    ';';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                case '>':
                    if (tagswandeln) {
                        htmlStringTab[j++]    =    '&';
                        htmlStringTab[j++]    =    'g';
                        htmlStringTab[j++]    =    't';
                        htmlStringTab[j++]    =    ';';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                case ' ':
                    if (blankwandeln) {
                        htmlStringTab[j++]    =    '&';
                        htmlStringTab[j++]    =    'n';
                        htmlStringTab[j++]    =    'b';
                        htmlStringTab[j++]    =    's';
                        htmlStringTab[j++]    =    'p';
                        htmlStringTab[j++]    =    ';';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                case '\r':
                    break;
                case '\n':
                    if (returnwandeln) {
                        htmlStringTab[j++]    =    '<';
                        htmlStringTab[j++]    =    'b';
                        htmlStringTab[j++]    =    'r';
                        htmlStringTab[j++]    =    '>';
                    } else {
                        htmlStringTab[j++]    =    javaStringTab[i];
                    }
                    break;
                default:
                    boolean found = false;
                    for (int c = 0; c < CONVERSION_TABLE.length && !found;
                            c++) {
                        if (CONVERSION_TABLE[c][0] == javaStringTab[i]) {
                            found = true;
                            for (int p = 1; p < CONVERSION_TABLE[c].length;
                                 p++) {
                               htmlStringTab[j++] = CONVERSION_TABLE[c][p];
                            }
                        }
                    }
                    if (!found) {
                        if (javaStringTab[i] > MAX_LENGTH) {
                            String dummy = Integer.toString(javaStringTab[i]);
                            htmlStringTab[j++]    =    '&';
                            htmlStringTab[j++]    =    '#';
                            for (int k = 0; k < dummy.length(); k++) {
                                htmlStringTab[j++]    =    dummy.charAt(k);
                            }
                            htmlStringTab[j++]    =    ';';
                        } else {
                            htmlStringTab[j++]    =    javaStringTab[i];
                        }
                    }
                    break;
                }
            }

            htmlString    =    new String(htmlStringTab, 0, j);
        }

        return htmlString;
    }
};
