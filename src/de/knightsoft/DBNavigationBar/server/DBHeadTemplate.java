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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBase;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;
import de.knightsoft.DBNavigationBar.shared.Constants;

/**
 * 
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation
 * template for a simple database
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadTemplate<E extends DomainHeadDataBase> extends DBHeadDateTemplate<E> {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 6419810537457908964L;

	protected final String updateHeadSQL;

	
	/**
     * Constructor, set up database connection
     * 
     * @param type - class instance of E
     * @param lookUpDataBase
     * @param sessionUser
     * @param dataBaseTableName
     * @param keyFieldName
     * @param insertHeadSQL
     * @param updateHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String updateHeadSQL,
    		String readMinMaxSQL,
    		String readNextSQL,
    		String readPrevSQL,
    		String readHeadSQL,
    		String invalidateHeadSQL
    		) throws UnexpectedException {
		super(
				type,
	       		lookUpDataBase,
	       		sessionUser,
				dataBaseTableName,
				keyFieldName,
				insertHeadSQL,

				(readMinMaxSQL != null ? readMinMaxSQL :
				"SELECT MIN(" + keyFieldName + ") AS min, " +
				"       MAX(" + keyFieldName + ") AS max " +
				"FROM   " + dataBaseTableName + " " +
				"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? ;"),

				(readNextSQL != null ? readNextSQL :
				"SELECT MIN(" + keyFieldName + ") AS dbnumber " +
    			"FROM   " + dataBaseTableName + " " +
  				"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " +
  				"  AND  " + keyFieldName + " > ? ;"),

  				(readPrevSQL != null ? readPrevSQL :
  				"SELECT MAX(" + keyFieldName + ") AS dbnumber " +
  				"FROM   " + dataBaseTableName + " " +
				"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " +
				"  AND  " + keyFieldName + " < ? ;"),

				(readHeadSQL != null ? readHeadSQL :
				"SELECT * " +
		  	  	"FROM   " + dataBaseTableName + " " +
				"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " +
				"  AND  " + keyFieldName + " = ? ;"),

				(insertHeadSQL != null ? insertHeadSQL :
				"DELETE FROM " + dataBaseTableName + " " +
				"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " +
				"  AND  " + keyFieldName + " = ? ;")
		);

		this.updateHeadSQL		=	updateHeadSQL;
	}

	/**
     * Constructor, set up database connection
     * 
     * @param type - class instance of E
     * @param lookUpDataBase
     * @param sessionUser
     * @param dataBaseTableName
     * @param keyFieldName
     * @param insertHeadSQL
     * @param updateHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String updateHeadSQL
    		) throws UnexpectedException {
    	this(	type,
    			lookUpDataBase,
    			sessionUser,
    			dataBaseTableName,
    			keyFieldName,
    			insertHeadSQL,
    			updateHeadSQL,
    			null,
    			null,
    			null,
    			null,
    			null
    		);
    }

	/**
     * Constructor, set up database connection
     * 
     * @param type - class instance of E
     * @param lookUpDataBase
     * @param sessionUser
     * @param dataBaseTableName
     * @param keyFieldName
     * @param insertHeadSQL
     * @param updateHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String updateHeadSQL,
    		String readHeadSQL
    		) throws UnexpectedException {
    	this(	type,
    			lookUpDataBase,
    			sessionUser,
    			dataBaseTableName,
    			keyFieldName,
    			insertHeadSQL,
    			updateHeadSQL,
    			null,
    			null,
    			null,
    			readHeadSQL,
    			null
    		);
    }

	/**
	 * <code>fillUpdateHead</code> fills the parameters of the update prepared statement
	 * @param updateHeadSQLStatement
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 */
	protected abstract void fillUpdateHead(
			PreparedStatement updateHeadSQLStatement,
			int mandator,
			String user,
			E saveEntry) throws SQLException;

	/**
	 * <code>searchSQLSelect</code> setup a part of the sql statment to search for
	 * a user
	 * 
	 * @param thisDataBase
	 * 			Database connection
	 * @param minMax
	 * 			"MIN" or "MAX" used for PhoneNumber
	 * @param searchField
	 * 			Field to search for
	 * @param searchMethodeEntry
	 * 			compare method for search ("<", "<=", "=", ">", ">=" or "like")
	 * @param searchFieldEntry
	 * 			value to search for
	 * @param dbKeyVGL
	 * 			compare method of phone number ("<", "<=", "=", ">", ">=" or "like")
	 * @param dbKey
	 * 			comparison number
	 * @return SQL-String
	 * @throws Exception 
	 */
    @Override
	protected String searchSQLSelect(
			Connection thisDataBase,
			String minMax,
			String searchField,
			String searchMethodeEntry,
			String searchFieldEntry,
			String dbKeyVGL,
			String dbKey) throws Exception {
		int mandator	 	=	this.getUser().getMandator();

		String sqlString =
    			"SELECT " + minMax + "(" + this.keyFieldName + ") AS dbnumber "
			+	"FROM   " + this.dataBaseTableName + " "
			+	"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " "
			+	" AND   " + this.keyFieldName + " " + dbKeyVGL + " " + StringToSQL.convertString(dbKey, thisDataBase.getMetaData().getDatabaseProductName()) + " "
			+	" AND   ";

		if( "=".equals(searchMethodeEntry) )
			sqlString += StringToSQL.searchString(searchField, searchFieldEntry, thisDataBase.getMetaData().getDatabaseProductName());
		else if( "like".equals(searchMethodeEntry) )
			sqlString += StringToSQL.searchString(searchField, "*" + searchFieldEntry + "*", thisDataBase.getMetaData().getDatabaseProductName());
		else
			sqlString += searchField + " " + searchMethodeEntry + " " +  StringToSQL.convertString(searchFieldEntry, thisDataBase.getMetaData().getDatabaseProductName());
		return sqlString;
	}

	/**
	 * <code>saveEntry</code> saves or inserts a entry to database
	 * 
	 * @param currentEntry
	 * 			entry that has to be saved
	 */
    @Override
	public E saveEntry(E currentEntry) {
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			String user		=	thisUser.getUser();
			String saveKeyString	=	currentEntry.getKeyCur();
			if( saveKeyString == null || "".equals(saveKeyString) )
				saveKeyString		=	currentEntry.getKeyNew();
			Connection thisDataBase =	null;
			PreparedStatement updateHeadSQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E dbEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
			        thisDataBase			=	lDataSource.getConnection();
					ic.close();

					dbEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, dbEntry );
					if((dbEntry != null) && (dbEntry.getKeyCur() == null))
						dbEntry	=	null;

					if( dbEntry	==	null ) {
						// new Entry, insert a new one 
						this.insertEntry( thisDataBase, mandator, user, currentEntry, false);
					} else {
						// Entry already exists, update it, if necessary
						if( !currentEntry.equals( dbEntry ) ) {
							if( !currentEntry.equalsEntry(dbEntry) ) {
								// Invalidate old entry
								updateHeadSQLStatement	=	thisDataBase.prepareStatement(updateHeadSQL);
								updateHeadSQLStatement.clearParameters();
								this.fillUpdateHead(updateHeadSQLStatement, mandator, user, currentEntry);
								updateHeadSQLStatement.executeUpdate();
							}
						}
					}
					
					currentEntry.setKeyCur(saveKeyString);
					this.fillMinMax(thisDataBase, mandator, currentEntry);
					currentEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, currentEntry );
				}

			} catch (Exception e) {
				currentEntry	=	null;
			} finally {
				try {
					if( updateHeadSQLStatement != null )
						updateHeadSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		} else
			currentEntry	=	null;
		return currentEntry;
	}
}
