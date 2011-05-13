/**
 * This file is part of DBNavigationBar.
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
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *	Name		Date		Change
 */
package de.knightsoft.DBNavigationBar.server;

import de.knightsoft.DBNavigationBar.shared.Constants;


/**
 * 
 * <code>StringToSQL</code> is a class to convert a string to a SQL string
 * masking all non allowed signs to prevent against SQL-Injection
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-01-02
 */
public class StringToSQL {

    /**
     * The <code>convertStringPrepared</code> method replaces &lt;br&gt; with
     * LineFeed and the char 128 (stupid windows euro sign) to 164 
     *
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertStringPrepared( String JavaString
			) {
		return convertStringPrepared(JavaString, Constants.JDBCClassMySQL);
	}

    /**
     * The <code>convertStringPrepared</code> method replaces &lt;br&gt; with
     * LineFeed and the char 128 (stupid windows euro sign) to 164 
     *
     * @param JavaString
     *            Java string to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String convertStringPrepared(	String JavaString,
														String JDBCClass
			) {
		String SQLString	=	null;

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]		=	new char[ 2 * JavaString.length() ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
//					case '\n':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	'n';
//						break;
//					case '\t':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	't';
//						break;
					case '\r':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	'r';
						break;
//					case '\b':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	'b';
//						break;
					case '<':
						if((i+3)			<	JavaString.length() &&
							JavaStringTab[i+1]	==	'b' &&
							JavaStringTab[i+2]	==	'r' &&
							JavaStringTab[i+3]	==	'>') {
							i	+=	3;
//							SQLStringTab[j++]	=	'\\';
//							SQLStringTab[j++]	=	'n';
							SQLStringTab[j++]	=	'\n';
						} else
							SQLStringTab[j++] = JavaStringTab[i];
						break;
//					case '\\':
//					case '\'':
//					case '"':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	JavaStringTab[i];
//						break;
					case 128:
						SQLStringTab[j++]	=	'€';
						break;
					default:
						SQLStringTab[j++] = JavaStringTab[i];
						break;
				}
			}
			SQLString	= new String( SQLStringTab, 0, j );
		}

		return SQLString;
	}

    /**
     * The <code>convert</code> method converts a Java string to SQL
     *
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convert( String JavaString
			) {
		return convert(JavaString, Constants.JDBCClassMySQL);
	}

    /**
     * The <code>convert</code> method converts a Java string to SQL
     *
     * @param JavaString
     *            Java string to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String convert( String JavaString,
										String JDBCClass
			) {
		String convertedString	=	null;
		
		if( JDBCClass == null || JDBCClass.equals(Constants.JDBCClassMySQL))
			convertedString	=	convertMySQL(JavaString);
		else if( JDBCClass.equals(Constants.JDBCClassMSSQL) )
			convertedString	=	convertMSSQL(JavaString);
		else
			convertedString	=	convertMySQL(JavaString);
		return convertedString;
	}

    /**
     * The <code>convertMySQL</code> method converts a Java string to SQL
     *
     * @param JavaString
     *            Java string to convert MySQL Style
     * @return string as SQL
     */
	final static private String convertMySQL( String JavaString
			) {
		String SQLString	=	null;

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]		=	new char[ 2 + ( 2 * JavaString.length() ) ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;

			SQLStringTab[j++]	=	'\'';

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
					case '\n':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'n';
						break;
					case '\t':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	't';
						break;
					case '\r':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'r';
						break;
					case '\b':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	JavaString.length() &&
							JavaStringTab[i+1]	==	'b' &&
							JavaStringTab[i+2]	==	'r' &&
							JavaStringTab[i+3]	==	'>') {
							i	+=	3;
							SQLStringTab[j++]	=	'\\';
							SQLStringTab[j++]	=	'n';
						} else
							SQLStringTab[j++] = JavaStringTab[i];
						break;
					case '"':
					case '\\':
					case '\'':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	JavaStringTab[i];
						break;
					case 128:
						SQLStringTab[j++]	=	'€';
						break;
					default:
						SQLStringTab[j++] += JavaStringTab[i];
						break;
				}
			}
			SQLStringTab[j++]	=	'\'';

			SQLString	= new String( SQLStringTab, 0, j );
		}

		return SQLString;
	}

    /**
     * The <code>convertMSSQL</code> method converts a Java string to SQL
     *
     * @param JavaString
     *            Java string to convert MSSQL Style
     * @return string as SQL
     */
	final static private String convertMSSQL( String JavaString
			) {
		String SQLString	=	null;

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]		=	new char[ 2 + ( 2 * JavaString.length() ) ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;

			SQLStringTab[j++]	=	'\'';

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
					case '\n':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'n';
						break;
					case '\t':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	't';
						break;
					case '\r':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'r';
						break;
					case '\b':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	JavaString.length() &&
							JavaStringTab[i+1]	==	'b' &&
							JavaStringTab[i+2]	==	'r' &&
							JavaStringTab[i+3]	==	'>') {
							i	+=	3;
							SQLStringTab[j++]	=	'\\';
							SQLStringTab[j++]	=	'n';
						} else
							SQLStringTab[j++] = JavaStringTab[i];
						break;
					case '\'':
						SQLStringTab[j++]	=	JavaStringTab[i];
						SQLStringTab[j++]	=	JavaStringTab[i];
						break;
					case 128:
						SQLStringTab[j++]	=	'€';
						break;
					default:
						SQLStringTab[j++] += JavaStringTab[i];
						break;
				}
			}
			SQLStringTab[j++]	=	'\'';

			SQLString	= new String( SQLStringTab, 0, j );
		}

		return SQLString;
	}

    /**
     * The <code>convertString</code> is the same as convert
     *
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertString( String JavaString
			) {
		return convert(JavaString);
	}

    /**
     * The <code>convertString</code> is the same as convert
     *
     * @param JavaString
     *            Java string to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String convertString(	String JavaString,
												String JDBCClass
			) {
		return convert(JavaString, JDBCClass);
	}

    /**
     * The <code>convertNumber</code> method converts a Java string,
     * contains a number to SQL
     *
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertNumber( String JavaString
			) {
		return convertNumber(JavaString, Constants.JDBCClassMySQL);
	}

    /**
     * The <code>convertNumber</code> method converts a Java string,
     * contains a number to SQL
     *
     * @param JavaString
     *            Java string to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String convertNumber(	String JavaString,
												String JDBCClass
			) {
		String SQLString	=	null;

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]	=	new char[ JavaString.length() ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
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
						SQLStringTab[j++] += JavaStringTab[i];
						break;
					default:
						break;
				}
			}

			if( j > 0 )
				SQLString	= new String( SQLStringTab, 0, j );
		}

		return SQLString;
	}

    /**
     * The <code>SuchString</code> method is the same as <code>SearchString</code>
     *
     * @param DBFieldname
     *            database fieldname
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String SuchString(	String DBFieldname,
											String JavaString
			) {
		return SearchString(DBFieldname, JavaString);
	}

    /**
     * The <code>SearchString</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param DBFieldname
     *            database fieldname
     * @param JavaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String SearchString(String DBFieldname,
											String JavaString
			) {
		return SearchString(DBFieldname, JavaString, Constants.JDBCClassMySQL);
	}

    /**
     * The <code>SearchString</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param DBFieldname
     *            database fieldname
     * @param JavaString
     *            Java string to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String SearchString(String DBFieldname,
											String JavaString,
											String JDBCClass
			) {
		String convertedString	=	null;
		
		if( JDBCClass == null || JDBCClass.equals(Constants.JDBCClassMySQL))
			convertedString	=	SearchStringMySQL(DBFieldname, JavaString);
		else if( JDBCClass.equals(Constants.JDBCClassMSSQL) )
			convertedString	=	SearchStringMSSQL(DBFieldname, JavaString);
		else
			convertedString	=	SearchStringMySQL(DBFieldname, JavaString);
		return convertedString;
	}

    /**
     * The <code>SearchStringMySQL</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param DBFieldname
     *            database fieldname
     * @param JavaString
     *            Java string to convert
     * @return string as SQL MySQL style
     */
	final static private String SearchStringMySQL(String DBFieldname,
												String JavaString
			) {
		String SQLString	=	"";

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]	=	new char[ 2 + ( 2 * JavaString.length() ) ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;
			boolean	platzhalter	=	false;

			SQLStringTab[j++]	=	'\'';

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
					case '\n':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'n';
						break;
					case '\t':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	't';
						break;
					case '\r':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'r';
						break;
					case '\b':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	JavaString.length() &&
							JavaStringTab[i+1]	==	'b' &&
							JavaStringTab[i+2]	==	'r' &&
							JavaStringTab[i+3]	==	'>') {
							i	+=	3;
							SQLStringTab[j++]	=	'\\';
							SQLStringTab[j++]	=	'n';
						}
						else
							SQLStringTab[j++] = JavaStringTab[i];
						break;
					case '"':
					case '\\':
					case '\'':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '*':
						SQLStringTab[j++]	=	'%';
						platzhalter			=	true;
						break;
					case '?':
						SQLStringTab[j++]	=	'_';
						platzhalter			=	true;
						break;
					case 128:
						SQLStringTab[j++]	=	'€';
						break;
					default:
						SQLStringTab[j++] += JavaStringTab[i];
						break;
				}
			}
			SQLStringTab[j++]	=	'\'';

			SQLString	= new String( SQLStringTab, 0, j );
			if( platzhalter )
				SQLString	=	DBFieldname + " like " + SQLString;
			else
				SQLString	=	DBFieldname + "=" + SQLString;
		}

		return SQLString;
	}

    /**
     * The <code>SearchStringMSSQL</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param DBFieldname
     *            database fieldname
     * @param JavaString
     *            Java string to convert
     * @return string as SQL MSSQL style
     */
	final static private String SearchStringMSSQL(String DBFieldname,
												String JavaString
			) {
		String SQLString	=	"";

		if( JavaString != null && JavaString.length() > 0 ) {
			char SQLStringTab[]	=	new char[ 2 + ( 2 * JavaString.length() ) ];
			char JavaStringTab[]	=	JavaString.toCharArray();
			int j = 0;
			boolean	platzhalter	=	false;

			SQLStringTab[j++]	=	'\'';

			for(int i = 0; i < JavaString.length(); i++ ) {
				switch( JavaStringTab[i] ) {
					case '\n':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'n';
						break;
					case '\t':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	't';
						break;
					case '\r':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'r';
						break;
					case '\b':
						SQLStringTab[j++]	=	'\\';
						SQLStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	JavaString.length() &&
							JavaStringTab[i+1]	==	'b' &&
							JavaStringTab[i+2]	==	'r' &&
							JavaStringTab[i+3]	==	'>') {
							i	+=	3;
							SQLStringTab[j++]	=	'\\';
							SQLStringTab[j++]	=	'n';
						}
						else
							SQLStringTab[j++] = JavaStringTab[i];
						break;
					case '\'':
						SQLStringTab[j++]	=	JavaStringTab[i];
						SQLStringTab[j++]	=	JavaStringTab[i];
						break;
					case '*':
						SQLStringTab[j++]	=	'%';
						platzhalter			=	true;
						break;
					case '?':
						SQLStringTab[j++]	=	'_';
						platzhalter			=	true;
						break;
					case 128:
						SQLStringTab[j++]	=	'€';
						break;
					default:
						SQLStringTab[j++] += JavaStringTab[i];
						break;
				}
			}
			SQLStringTab[j++]	=	'\'';

			SQLString	= new String( SQLStringTab, 0, j );
			if( platzhalter )
				SQLString	=	DBFieldname + " like " + SQLString;
			else
				SQLString	=	DBFieldname + "=" + SQLString;
		}

		return SQLString;
	}

    /**
     * The <code>convertDatetime</code> method converts a datetime to SQL
     *
     * @param JavaDateTime
     *            Timestamp to convert
     * @param JDBC_CLASS
     *            name of the JDBC_CLASS to detect DataBase
     * @return string as SQL
     */
	final static public String convertDatetime( java.sql.Timestamp JavaDateTime,
										String JDBCClass
			) {
		String convertedString	=	null;
		
		if( JDBCClass == null || JDBCClass.equals(Constants.JDBCClassMySQL))
			convertedString	=	convertDatetimeMySQL(JavaDateTime);
		else if( JDBCClass.equals(Constants.JDBCClassMSSQL) )
			convertedString	=	convertDatetimeMSSQL(JavaDateTime);
		else
			convertedString	=	convertDatetimeMySQL(JavaDateTime);
		return convertedString;
	}

    /**
     * The <code>convertDatetimeMySQL</code> method converts a datetime to SQL
     *
     * @param JavaDateTime
     *            Timestamp to convert MySQL Style
     * @return string as SQL
     */
	final static private String convertDatetimeMySQL( java.sql.Timestamp JavaDateTime
			) {
		String SQLString	=	null;

		if( JavaDateTime != null ) {
			SQLString		=	"\'" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(JavaDateTime) + "\'";
		} else {
			SQLString		=	"null";
		}

		return SQLString;
	}

    /**
     * The <code>convertDatetimeMSSQL</code> method converts a datetime to SQL
     *
     * @param JavaDateTime
     *            Timestamp to convert MSSQL Style
     * @return string as SQL
     */
	final static private String convertDatetimeMSSQL( java.sql.Timestamp JavaDateTime
			) {
		String SQLString	=	null;

		if( JavaDateTime != null ) {
			SQLString		=	"CONVERT(DATETIME, '" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(JavaDateTime) + "', 121)";
		} else {
			SQLString		=	"null";
		}

		return SQLString;
	}

};
