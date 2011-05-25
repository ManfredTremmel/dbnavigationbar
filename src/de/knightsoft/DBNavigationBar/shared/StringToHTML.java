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
     * @param javaString
     *            Java string to convert
     * @return string as HTML
     */
	final static public String convert(	String javaString
			) {
		return convert( javaString, false, true, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param javaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @return string as HTML
     */
	final static public String convert(	String javaString,
											boolean blankwandeln
			) {
		return convert( javaString, blankwandeln, true, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
     *  
     * @param javaString
     *            Java string to convert
     * @param blankwandeln
     *            convert blank to &nbsp; (true/false)
     * @param returnwandeln
     *            convert linefeed to <br> (true/false)
     * @return string as HTML
     */
	final static public String convert(	String javaString,
											boolean blankwandeln,
											boolean returnwandeln
			) {
		return convert( javaString, blankwandeln, returnwandeln, true );
	}

    /**
     * The <code>convert</code> method converts the string to a
     * table of dos charset bytes
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
	final static public String convert(	String javaString,
											boolean blankwandeln,
											boolean returnwandeln,
											boolean tagswandeln
			) {
		String htmlString		=	"";

		if( javaString != null && javaString.length() > 0 ) {
			char htmlStringTab[]	=	new char[ ( 8 * javaString.length() ) ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
					case '¡':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'x';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case '¢':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	';';
						break;
					case '£':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	';';
						break;
					case '€':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	';';
						break;
					case '¥':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'y';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	';';
						break;
					case '§':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	';';
						break;
					case '©':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'y';
						htmlStringTab[j++]	=	';';
						break;
					case 'ª':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'f';
						htmlStringTab[j++]	=	';';
						break;
					case '«':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'q';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	';';
						break;
					case '¬':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	';';
						break;
					case '­':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'h';
						htmlStringTab[j++]	=	'y';
						htmlStringTab[j++]	=	';';
						break;
					case '®':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case '¯':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	';';
						break;
					case '°':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case '±':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	';';
						break;
					case '²':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'2';
						htmlStringTab[j++]	=	';';
						break;
					case '³':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'3';
						htmlStringTab[j++]	=	';';
						break;
					case 'µ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	';';
						break;
					case '¶':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	';';
						break;
					case '·':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	';';
						break;
					case '¹':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'p';
						htmlStringTab[j++]	=	'1';
						htmlStringTab[j++]	=	';';
						break;
					case 'º':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	';';
						break;
					case '»':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'q';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	';';
						break;
					case '¿':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'q';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	';';
						break;
					case 'À':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Á':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Â':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ã':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ä':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'Å':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case 'Æ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'A';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ç':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'C';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'È':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'É':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ê':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ë':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ì':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'I';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Í':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'I';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Î':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'I';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ï':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'I';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ð':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'E';
						htmlStringTab[j++]	=	'T';
						htmlStringTab[j++]	=	'H';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ñ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'N';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ò':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ó':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ô':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'Õ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ö':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case '×':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ø':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'h';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ù':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'U';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ú':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'U';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Û':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'U';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ü':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'U';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'Ý':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'Y';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'Þ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'T';
						htmlStringTab[j++]	=	'H';
						htmlStringTab[j++]	=	'O';
						htmlStringTab[j++]	=	'R';
						htmlStringTab[j++]	=	'N';
						htmlStringTab[j++]	=	';';
						break;
					case 'ß':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'z';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case 'à':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'á':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'â':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'ã':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ä':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'å':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case 'æ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	';';
						break;
					case 'ç':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'è':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'é':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ê':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'ë':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'ì':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'í':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'î':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'ï':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'ð':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'h';
						htmlStringTab[j++]	=	';';
						break;
					case 'ñ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ò':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ó':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ô':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'õ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ö':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case '÷':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'd';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ø':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	's';
						htmlStringTab[j++]	=	'h';
						htmlStringTab[j++]	=	';';
						break;
					case 'ù':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'g';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'v';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'ú':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'û':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'i';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	';';
						break;
					case 'ü':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case 'ý':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'y';
						htmlStringTab[j++]	=	'a';
						htmlStringTab[j++]	=	'c';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'e';
						htmlStringTab[j++]	=	';';
						break;
					case 'þ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	't';
						htmlStringTab[j++]	=	'h';
						htmlStringTab[j++]	=	'o';
						htmlStringTab[j++]	=	'r';
						htmlStringTab[j++]	=	'n';
						htmlStringTab[j++]	=	';';
						break;
					case 'ÿ':
						htmlStringTab[j++]	=	'&';
						htmlStringTab[j++]	=	'y';
						htmlStringTab[j++]	=	'u';
						htmlStringTab[j++]	=	'm';
						htmlStringTab[j++]	=	'l';
						htmlStringTab[j++]	=	';';
						break;
					case '\"':
						if( tagswandeln )
						{
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'q';
							htmlStringTab[j++]	=	'u';
							htmlStringTab[j++]	=	'o';
							htmlStringTab[j++]	=	't';
							htmlStringTab[j++]	=	';';
						}
						else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					case '&':
						if( tagswandeln ) {
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'a';
							htmlStringTab[j++]	=	'm';
							htmlStringTab[j++]	=	'p';
							htmlStringTab[j++]	=	';';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					case '<':
						if( tagswandeln ) {
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'l';
							htmlStringTab[j++]	=	't';
							htmlStringTab[j++]	=	';';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					case '>':
						if( tagswandeln ) {
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'g';
							htmlStringTab[j++]	=	't';
							htmlStringTab[j++]	=	';';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					case ' ':
						if( blankwandeln ) {
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'n';
							htmlStringTab[j++]	=	'b';
							htmlStringTab[j++]	=	's';
							htmlStringTab[j++]	=	'p';
							htmlStringTab[j++]	=	';';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					case '\r':
						break;
					case '\n':
						if( returnwandeln ) {
							htmlStringTab[j++]	=	'<';
							htmlStringTab[j++]	=	'b';
							htmlStringTab[j++]	=	'r';
							htmlStringTab[j++]	=	'>';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
					default:
						if( javaStringTab[i] > 255 ) {
							String dummy		=	Integer.toString( javaStringTab[i] );
							htmlStringTab[j++]	=	'&';
							htmlStringTab[j++]	=	'#';
							for( int k = 0; k < dummy.length(); k++ ) {
								htmlStringTab[j++]	=	dummy.charAt(k);
							}
							htmlStringTab[j++]	=	';';
						} else
							htmlStringTab[j++]	=	javaStringTab[i];
						break;
				}
			}

			htmlString	=	new String( htmlStringTab, 0, j );
		}

		return htmlString;
	}
};
