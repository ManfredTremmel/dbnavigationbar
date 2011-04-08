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
package de.knightsoft.DBNavigationBar.shared;

/**
 * 
 * The <code>Konstanten</code> contains the fixed fields
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public class Constants {
    public static final boolean UseConnectionPool		=	true;
    

	final static public String JDBCClassMySQL				=	"com.mysql.jdbc.Driver";
	final static public String JDBCClassMySQL_OLD			=	"org.gjt.mm.mysql.Driver";
	final static public String JDBCClassMSSQL				=	"com.microsoft.jdbc.sqlserver.SQLServerDriver";

    public static final String DBFieldGlobalMandator		=	"Mandator";
    public static final String DBFieldGlobalUser			=	"Username";
    public static final String DBFieldGlobalDate_from		=	"Date_from";
    public static final String DBFieldGlobalDate_to		=	"Date_to";

    public static final String DBFieldMandator			=	DBFieldGlobalMandator;
};
