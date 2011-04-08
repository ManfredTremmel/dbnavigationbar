/**
 * This file is part of RiPhone.
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
 * Copyright (c) 2010 RI Solutions GmbH
 *
 * --
 *	Name		Date		Change
 */
package de.knightsoft.DBNavigationBar.shared;

/**
 * 
 * <code>StringToHTML</code> is a class to convert a string to HTML
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-01-02
 */
public class StringToHTML
{
    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param JavaString
     *            Java string to convert
     * @return string as HTML
     */
	final static public String convert(	String JavaString
			) {
		return convert( JavaString, false, true, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param JavaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @return string as HTML
     */
	final static public String convert(	String JavaString,
											boolean blankwandeln
			) {
		return convert( JavaString, blankwandeln, true, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param JavaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @param returnwandeln
     *            convert linefeed to <br> (true/false)
     * @return string as HTML
     */
	final static public String convert(	String JavaString,
											boolean blankwandeln,
											boolean returnwandeln
			) {
		return convert( JavaString, blankwandeln, returnwandeln, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param JavaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @param returnwandeln
     *            convert linefeed to <br> (true/false)
     * @param tagswandeln
     *            < to &lt; and > to &gt; (true/false)
     * @return string as HTML
     */
	final static public String convert(	String JavaString,
											boolean blankwandeln,
											boolean returnwandeln,
											boolean tagswandeln
			) {
		String HTMLString		=	"";

		if( JavaString != null && JavaString.length() > 0 ) {
			char HTMLStringTab[]	=	new char[ ( 8 * JavaString.length() ) ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
					case '¡':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'x';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case '¢':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	';';
						break;
					case '£':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	';';
						break;
					case '€':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	';';
						break;
					case '¥':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'y';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	';';
						break;
					case '§':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	';';
						break;
					case '©':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'y';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ª':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'f';
						HTMLStringTab[j++]	=	';';
						break;
					case '«':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'q';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	';';
						break;
					case '¬':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	';';
						break;
					case '­':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'h';
						HTMLStringTab[j++]	=	'y';
						HTMLStringTab[j++]	=	';';
						break;
					case '®':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case '¯':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	';';
						break;
					case '°':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case '±':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	';';
						break;
					case '²':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'2';
						HTMLStringTab[j++]	=	';';
						break;
					case '³':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'3';
						HTMLStringTab[j++]	=	';';
						break;
					case 'µ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	';';
						break;
					case '¶':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	';';
						break;
					case '·':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	';';
						break;
					case '¹':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'p';
						HTMLStringTab[j++]	=	'1';
						HTMLStringTab[j++]	=	';';
						break;
					case 'º':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	';';
						break;
					case '»':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'q';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	';';
						break;
					case '¿':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'q';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	';';
						break;
					case 'À':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Á':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Â':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ã':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ä':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Å':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Æ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'A';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ç':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'C';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'È':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'É':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ê':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ë':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ì':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'I';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Í':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'I';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Î':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'I';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ï':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'I';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ð':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'E';
						HTMLStringTab[j++]	=	'T';
						HTMLStringTab[j++]	=	'H';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ñ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'N';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ò':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ó':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ô':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Õ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ö':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case '×':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ø':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'h';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ù':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'U';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ú':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'U';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Û':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'U';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ü':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'U';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Ý':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'Y';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'Þ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'T';
						HTMLStringTab[j++]	=	'H';
						HTMLStringTab[j++]	=	'O';
						HTMLStringTab[j++]	=	'R';
						HTMLStringTab[j++]	=	'N';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ß':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'z';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case 'à':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'á':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'â':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ã':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ä':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'å':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case 'æ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ç':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'è':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'é':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ê':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ë':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ì':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'í':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'î':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ï':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ð':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'h';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ñ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ò':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ó':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ô':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'õ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ö':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case '÷':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'd';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ø':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	's';
						HTMLStringTab[j++]	=	'h';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ù':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'g';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'v';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ú':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'û':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'i';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ü':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ý':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'y';
						HTMLStringTab[j++]	=	'a';
						HTMLStringTab[j++]	=	'c';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'e';
						HTMLStringTab[j++]	=	';';
						break;
					case 'þ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	't';
						HTMLStringTab[j++]	=	'h';
						HTMLStringTab[j++]	=	'o';
						HTMLStringTab[j++]	=	'r';
						HTMLStringTab[j++]	=	'n';
						HTMLStringTab[j++]	=	';';
						break;
					case 'ÿ':
						HTMLStringTab[j++]	=	'&';
						HTMLStringTab[j++]	=	'y';
						HTMLStringTab[j++]	=	'u';
						HTMLStringTab[j++]	=	'm';
						HTMLStringTab[j++]	=	'l';
						HTMLStringTab[j++]	=	';';
						break;
					case '\"':
						if( tagswandeln )
						{
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'q';
							HTMLStringTab[j++]	=	'u';
							HTMLStringTab[j++]	=	'o';
							HTMLStringTab[j++]	=	't';
							HTMLStringTab[j++]	=	';';
						}
						else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '&':
						if( tagswandeln ) {
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'a';
							HTMLStringTab[j++]	=	'm';
							HTMLStringTab[j++]	=	'p';
							HTMLStringTab[j++]	=	';';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '<':
						if( tagswandeln ) {
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'l';
							HTMLStringTab[j++]	=	't';
							HTMLStringTab[j++]	=	';';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '>':
						if( tagswandeln ) {
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'g';
							HTMLStringTab[j++]	=	't';
							HTMLStringTab[j++]	=	';';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					case ' ':
						if( blankwandeln ) {
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'n';
							HTMLStringTab[j++]	=	'b';
							HTMLStringTab[j++]	=	's';
							HTMLStringTab[j++]	=	'p';
							HTMLStringTab[j++]	=	';';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '\r':
						break;
					case '\n':
						if( returnwandeln ) {
							HTMLStringTab[j++]	=	'<';
							HTMLStringTab[j++]	=	'b';
							HTMLStringTab[j++]	=	'r';
							HTMLStringTab[j++]	=	'>';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
					default:
						if( JavaStringTab[i] > 255 ) {
							String Dummy		=	Integer.toString( (int)JavaStringTab[i] );
							HTMLStringTab[j++]	=	'&';
							HTMLStringTab[j++]	=	'#';
							for( int k = 0; k < Dummy.length(); k++ ) {
								HTMLStringTab[j++]	=	Dummy.charAt(k);
							}
							HTMLStringTab[j++]	=	';';
						} else
							HTMLStringTab[j++]	=	JavaStringTab[i];
						break;
				}
			}

			HTMLString	=	new String( HTMLStringTab, 0, j );
		}

		return HTMLString;
	}
};
