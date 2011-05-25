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

import java.sql.Timestamp;

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
     * @param javaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertStringPrepared( String javaString
			) {
		return convertStringPrepared(javaString, Constants.JDBC_CLASS_MYSQL);
	}

    /**
     * The <code>convertStringPrepared</code> method replaces &lt;br&gt; with
     * LineFeed and the char 128 (stupid windows euro sign) to 164 
     *
     * @param javaString
     *            Java string to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String convertStringPrepared(	String javaString,
														String jdbcClass
			) {
		String sqlString	=	null;

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]		=	new char[ 2 * javaString.length() ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
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
						if((i+3)			<	javaString.length() &&
							javaStringTab[i+1]	==	'b' &&
							javaStringTab[i+2]	==	'r' &&
							javaStringTab[i+3]	==	'>') {
							i	+=	3;
//							SQLStringTab[j++]	=	'\\';
//							SQLStringTab[j++]	=	'n';
							sqlStringTab[j++]	=	'\n';
						} else
							sqlStringTab[j++] = javaStringTab[i];
						break;
//					case '\\':
//					case '\'':
//					case '"':
//						SQLStringTab[j++]	=	'\\';
//						SQLStringTab[j++]	=	JavaStringTab[i];
//						break;
					case 128:
						sqlStringTab[j++]	=	'€';
						break;
					default:
						sqlStringTab[j++] = javaStringTab[i];
						break;
				}
			}
			sqlString	= new String( sqlStringTab, 0, j );
		}

		return sqlString;
	}

    /**
     * The <code>convert</code> method converts a Java string to SQL
     *
     * @param javaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convert( String javaString
			) {
		return convert(javaString, Constants.JDBC_CLASS_MYSQL);
	}

    /**
     * The <code>convert</code> method converts a Java string to SQL
     *
     * @param javaString
     *            Java string to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String convert( String javaString,
										String jdbcClass
			) {
		String convertedString	=	null;
		
		if( jdbcClass == null || jdbcClass.equals(Constants.JDBC_CLASS_MYSQL))
			convertedString	=	convertMySQL(javaString);
		else if( jdbcClass.equals(Constants.JDBC_CLASS_MSSQL) )
			convertedString	=	convertMSSQL(javaString);
		else
			convertedString	=	convertMySQL(javaString);
		return convertedString;
	}

    /**
     * The <code>convertMySQL</code> method converts a Java string to SQL
     *
     * @param javaString
     *            Java string to convert MySQL Style
     * @return string as SQL
     */
	final static private String convertMySQL( String javaString
			) {
		String sqlString	=	null;

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]		=	new char[ 2 + ( 2 * javaString.length() ) ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;

			sqlStringTab[j++]	=	'\'';

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
					case '\n':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'n';
						break;
					case '\t':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	't';
						break;
					case '\r':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'r';
						break;
					case '\b':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	javaString.length() &&
							javaStringTab[i+1]	==	'b' &&
							javaStringTab[i+2]	==	'r' &&
							javaStringTab[i+3]	==	'>') {
							i	+=	3;
							sqlStringTab[j++]	=	'\\';
							sqlStringTab[j++]	=	'n';
						} else
							sqlStringTab[j++] = javaStringTab[i];
						break;
					case '"':
					case '\\':
					case '\'':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	javaStringTab[i];
						break;
					case 128:
						sqlStringTab[j++]	=	'€';
						break;
					default:
						sqlStringTab[j++] += javaStringTab[i];
						break;
				}
			}
			sqlStringTab[j++]	=	'\'';

			sqlString	= new String( sqlStringTab, 0, j );
		}

		return sqlString;
	}

    /**
     * The <code>convertMSSQL</code> method converts a Java string to SQL
     *
     * @param javaString
     *            Java string to convert MSSQL Style
     * @return string as SQL
     */
	final static private String convertMSSQL( String javaString
			) {
		String sqlString	=	null;

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]		=	new char[ 2 + ( 2 * javaString.length() ) ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;

			sqlStringTab[j++]	=	'\'';

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
					case '\n':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'n';
						break;
					case '\t':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	't';
						break;
					case '\r':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'r';
						break;
					case '\b':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	javaString.length() &&
							javaStringTab[i+1]	==	'b' &&
							javaStringTab[i+2]	==	'r' &&
							javaStringTab[i+3]	==	'>') {
							i	+=	3;
							sqlStringTab[j++]	=	'\\';
							sqlStringTab[j++]	=	'n';
						} else
							sqlStringTab[j++] = javaStringTab[i];
						break;
					case '\'':
						sqlStringTab[j++]	=	javaStringTab[i];
						sqlStringTab[j++]	=	javaStringTab[i];
						break;
					case 128:
						sqlStringTab[j++]	=	'€';
						break;
					default:
						sqlStringTab[j++] += javaStringTab[i];
						break;
				}
			}
			sqlStringTab[j++]	=	'\'';

			sqlString	= new String( sqlStringTab, 0, j );
		}

		return sqlString;
	}

    /**
     * The <code>convertString</code> is the same as convert
     *
     * @param javaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertString( String javaString
			) {
		return convert(javaString);
	}

    /**
     * The <code>convertString</code> is the same as convert
     *
     * @param javaString
     *            Java string to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String convertString(	String javaString,
												String jdbcClass
			) {
		return convert(javaString, jdbcClass);
	}

    /**
     * The <code>convertNumber</code> method converts a Java string,
     * contains a number to SQL
     *
     * @param javaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String convertNumber( String javaString
			) {
		return convertNumber(javaString, Constants.JDBC_CLASS_MYSQL);
	}

    /**
     * The <code>convertNumber</code> method converts a Java string,
     * contains a number to SQL
     *
     * @param javaString
     *            Java string to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String convertNumber(	String javaString,
												String jdbcClass
			) {
		String sqlString	=	null;

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]		=	new char[ javaString.length() ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
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
						sqlStringTab[j++] += javaStringTab[i];
						break;
					default:
						break;
				}
			}

			if( j > 0 )
				sqlString	= new String( sqlStringTab, 0, j );
		}

		return sqlString;
	}

    /**
     * The <code>searchString</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param dbFieldname
     *            database fieldname
     * @param javaString
     *            Java string to convert
     * @return string as SQL
     */
	final static public String searchString(String dbFieldname,
											String javaString
			) {
		return searchString(dbFieldname, javaString, Constants.JDBC_CLASS_MYSQL);
	}

    /**
     * The <code>SearchString</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param dbFieldname
     *            database fieldname
     * @param javaString
     *            Java string to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String searchString(String dbFieldname,
											String javaString,
											String jdbcClass
			) {
		String convertedString	=	null;
		
		if( jdbcClass == null || jdbcClass.equals(Constants.JDBC_CLASS_MYSQL))
			convertedString	=	searchStringMySQL(dbFieldname, javaString);
		else if( jdbcClass.equals(Constants.JDBC_CLASS_MSSQL) )
			convertedString	=	searchStringMSSQL(dbFieldname, javaString);
		else
			convertedString	=	searchStringMySQL(dbFieldname, javaString);
		return convertedString;
	}

    /**
     * The <code>searchStringMySQL</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param dbFieldname
     *            database fieldname
     * @param javaString
     *            Java string to convert
     * @return string as SQL MySQL style
     */
	final static private String searchStringMySQL(String dbFieldname,
												String javaString
			) {
		String sqlString	=	"";

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]		=	new char[ 2 + ( 2 * javaString.length() ) ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;
			boolean	wildcard	=	false;

			sqlStringTab[j++]	=	'\'';

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
					case '\n':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'n';
						break;
					case '\t':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	't';
						break;
					case '\r':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'r';
						break;
					case '\b':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)						<	javaString.length() &&
							javaStringTab[i+1]	==	'b' &&
							javaStringTab[i+2]	==	'r' &&
							javaStringTab[i+3]	==	'>') {
							i	+=	3;
							sqlStringTab[j++]	=	'\\';
							sqlStringTab[j++]	=	'n';
						}
						else
							sqlStringTab[j++] = javaStringTab[i];
						break;
					case '"':
					case '\\':
					case '\'':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	javaStringTab[i];
						break;
					case '*':
						sqlStringTab[j++]	=	'%';
						wildcard			=	true;
						break;
					case '?':
						sqlStringTab[j++]	=	'_';
						wildcard			=	true;
						break;
					case 128:
						sqlStringTab[j++]	=	'€';
						break;
					default:
						sqlStringTab[j++] += javaStringTab[i];
						break;
				}
			}
			sqlStringTab[j++]	=	'\'';

			sqlString	= new String( sqlStringTab, 0, j );
			if( wildcard )
				sqlString	=	dbFieldname + " like " + sqlString;
			else
				sqlString	=	dbFieldname + "=" + sqlString;
		}

		return sqlString;
	}

    /**
     * The <code>searchStringMSSQL</code> method prepares a string for
     * searching in a SQL database using = or like
     *
     * @param dbFieldname
     *            database fieldname
     * @param javaString
     *            Java string to convert
     * @return string as SQL MSSQL style
     */
	final static private String searchStringMSSQL(String dbFieldname,
												String javaString
			) {
		String sqlString	=	"";

		if( javaString != null && javaString.length() > 0 ) {
			char sqlStringTab[]	=	new char[ 2 + ( 2 * javaString.length() ) ];
			char javaStringTab[]	=	javaString.toCharArray();
			int j = 0;
			boolean	wildcard	=	false;

			sqlStringTab[j++]	=	'\'';

			for(int i = 0; i < javaString.length(); i++ ) {
				switch( javaStringTab[i] ) {
					case '\n':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'n';
						break;
					case '\t':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	't';
						break;
					case '\r':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'r';
						break;
					case '\b':
						sqlStringTab[j++]	=	'\\';
						sqlStringTab[j++]	=	'b';
						break;
					case '<':
						if((i+3)			<	javaString.length() &&
							javaStringTab[i+1]	==	'b' &&
							javaStringTab[i+2]	==	'r' &&
							javaStringTab[i+3]	==	'>') {
							i	+=	3;
							sqlStringTab[j++]	=	'\\';
							sqlStringTab[j++]	=	'n';
						}
						else
							sqlStringTab[j++] = javaStringTab[i];
						break;
					case '\'':
						sqlStringTab[j++]	=	javaStringTab[i];
						sqlStringTab[j++]	=	javaStringTab[i];
						break;
					case '*':
						sqlStringTab[j++]	=	'%';
						wildcard			=	true;
						break;
					case '?':
						sqlStringTab[j++]	=	'_';
						wildcard			=	true;
						break;
					case 128:
						sqlStringTab[j++]	=	'€';
						break;
					default:
						sqlStringTab[j++] += javaStringTab[i];
						break;
				}
			}
			sqlStringTab[j++]	=	'\'';

			sqlString	= new String( sqlStringTab, 0, j );
			if( wildcard )
				sqlString	=	dbFieldname + " like " + sqlString;
			else
				sqlString	=	dbFieldname + "=" + sqlString;
		}

		return sqlString;
	}

    /**
     * The <code>convertDatetime</code> method converts a datetime to SQL
     *
     * @param javaDateTime
     *            Timestamp to convert
     * @param jdbcClass
     *            name of the JDBC class to detect DataBase
     * @return string as SQL
     */
	final static public String convertDatetime( Timestamp javaDateTime,
										String jdbcClass
			) {
		String convertedString	=	null;
		
		if( jdbcClass == null || jdbcClass.equals(Constants.JDBC_CLASS_MYSQL))
			convertedString	=	convertDatetimeMySQL(javaDateTime);
		else if( jdbcClass.equals(Constants.JDBC_CLASS_MSSQL) )
			convertedString	=	convertDatetimeMSSQL(javaDateTime);
		else
			convertedString	=	convertDatetimeMySQL(javaDateTime);
		return convertedString;
	}

    /**
     * The <code>convertDatetimeMySQL</code> method converts a datetime to SQL
     *
     * @param javaDateTime
     *            Timestamp to convert MySQL Style
     * @return string as SQL
     */
	final static private String convertDatetimeMySQL( Timestamp javaDateTime
			) {
		String sqlString	=	null;

		if( javaDateTime != null ) {
			sqlString		=	"\'" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(javaDateTime) + "\'";
		} else {
			sqlString		=	"null";
		}

		return sqlString;
	}

    /**
     * The <code>convertDatetimeMSSQL</code> method converts a datetime to SQL
     *
     * @param javaDateTime
     *            Timestamp to convert MSSQL Style
     * @return string as SQL
     */
	final static private String convertDatetimeMSSQL( Timestamp javaDateTime
			) {
		String sqlString	=	null;

		if( javaDateTime != null ) {
			sqlString		=	"CONVERT(DATETIME, '" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(javaDateTime) + "', 121)";
		} else {
			sqlString		=	"null";
		}

		return sqlString;
	}

};
