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

	protected String updateHeadSQL		= null;

	/**
	 * <code>fillUpdateHead</code> fills the parameters of the update prepared statement
	 * @param updateHeadSQLStatement
	 * @param Mandator
	 * @param User
	 * @param SaveEntry
	 */
	protected abstract void fillUpdateHead(
			PreparedStatement updateHeadSQLStatement,
			int Mandator,
			String User,
			E SaveEntry) throws SQLException;

	/**
     * Constructor, set up database connection
     * 
     * @param LookUpDataBase
     * @param SessionUser
     * @param DataBaseTableName
     * @param KeyfieldName
     * @param insertHeadSQL
     * @param updateHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadTemplate(
       		String LookUpDataBase,
       		String SessionUser,
    		String DataBaseTableName,
    		String KeyfieldName,
    		String insertHeadSQL,
    		String updateHeadSQL
    		) throws UnexpectedException {
		super(
	       		LookUpDataBase,
	       		SessionUser,
				DataBaseTableName,
				KeyfieldName,
				insertHeadSQL
		);

		this.readMinMaxSQL		=
				  "SELECT MIN(" + this.KeyfieldName + ") AS min, "
				+ "       MAX(" + this.KeyfieldName + ") AS max "
				+ "FROM   " + this.DataBaseTableName + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? ;";

		this.readNextSQL		=
      		  	  "SELECT MIN(" + this.KeyfieldName + ") AS dbnumber "
  				+ "FROM   " + this.DataBaseTableName + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
				+ "  AND  " + this.KeyfieldName + " > ? ;";

		this.readPrevSQL		=
    		  	  "SELECT MAX(" + this.KeyfieldName + ") AS dbnumber "
    			+ "FROM   " + this.DataBaseTableName + " "
  				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
  				+ "  AND  " + this.KeyfieldName + " < ? ;";

		this.readHeadSQL		=
        		  "SELECT * "
  		  	  	+ "FROM   " + this.DataBaseTableName + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
				+ "  AND  " + this.KeyfieldName + " = ? ;";

		this.invalidateHeadSQL	=
				  "DELETE FROM " + this.DataBaseTableName + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
				+ "  AND  " + this.KeyfieldName + " = ? ;";
		
		this.updateHeadSQL		=	updateHeadSQL;
	}

	/**
	 * <code>searchSQLSelect</code> setup a part of the sql statment to search for
	 * a user
	 * 
	 * @param ThisDataBase
	 * 			Database connection
	 * @param MinMax
	 * 			"MIN" or "MAX" used for PhoneNumber
	 * @param SearchField
	 * 			Field to search for
	 * @param SearchMethodeEntry
	 * 			compare method for search ("<", "<=", "=", ">", ">=" or "like")
	 * @param SearchFieldEntry
	 * 			value to search for
	 * @param DBKeyVGL
	 * 			compare method of phone number ("<", "<=", "=", ">", ">=" or "like")
	 * @param DBKey
	 * 			comparison number
	 * @return SQL-String
	 * @throws Exception 
	 */
	protected String searchSQLSelect(
			Connection ThisDataBase,
			String MinMax,
			String SearchField,
			String SearchMethodeEntry,
			String SearchFieldEntry,
			String DBKeyVGL,
			String DBKey) throws Exception {
		int Mandator	 	=	this.getUser().getMandator();

		String SQLString =
    			"SELECT " + MinMax + "(" + this.KeyfieldName + ") AS dbnumber "
			+	"FROM   " + this.DataBaseTableName + " "
			+	"WHERE  " + Constants.DBFieldGlobalMandator + " = " + Integer.toString(Mandator) + " "
			+	" AND   " + this.KeyfieldName + " " + DBKeyVGL + " " + StringToSQL.convertString(DBKey, ThisDataBase.getMetaData().getDatabaseProductName()) + " "
			+	" AND   ";

		if( "=".equals(SearchMethodeEntry) )
			SQLString += StringToSQL.SearchString(SearchField, SearchFieldEntry, ThisDataBase.getMetaData().getDatabaseProductName());
		else if( "like".equals(SearchMethodeEntry) )
			SQLString += StringToSQL.SearchString(SearchField, "*" + SearchFieldEntry + "*", ThisDataBase.getMetaData().getDatabaseProductName());
		else
			SQLString += SearchField + " " + SearchMethodeEntry + " " +  StringToSQL.convertString(SearchFieldEntry, ThisDataBase.getMetaData().getDatabaseProductName());
		return SQLString;
	}

	/**
	 * <code>saveEntry</code> saves or inserts a entry to database
	 * 
	 * @param CurrentEntry
	 * 			entry that has to be saved
	 */
	public E saveEntry(E CurrentEntry) {
		if( this.getUser() !=	null ) {
			int Mandator	=	this.getUser().getMandator();
			String User		=	this.getUser().getUser();
			String SaveKeyString	=	CurrentEntry.getKeyCur();
			if( SaveKeyString == null || "".equals(SaveKeyString) )
				SaveKeyString		=	CurrentEntry.getKeyNew();
			Connection ThisDataBase =	null;
			PreparedStatement updateHeadSQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E DBEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase			=	lDataSource.getConnection();
					ic.close();

					DBEntry	=	readOneEntry( ThisDataBase, Mandator, SaveKeyString, DBEntry );
					if((DBEntry != null) && (DBEntry.getKeyCur() == null))
						DBEntry	=	null;

					if( DBEntry	==	null ) {
						// new Entry, insert a new one 
						this.insertEntry( ThisDataBase, Mandator, User, CurrentEntry, false);
					} else {
						// Entry already exists, update it, if necessary
						if( !CurrentEntry.equals( DBEntry ) ) {
							if( !CurrentEntry.equalsEntry(DBEntry) ) {
								// Invalidate old entry
								updateHeadSQLStatement	=	ThisDataBase.prepareStatement(updateHeadSQL);
								updateHeadSQLStatement.clearParameters();
								this.fillUpdateHead(updateHeadSQLStatement, Mandator, User, CurrentEntry);
								updateHeadSQLStatement.executeUpdate();
							}
						}
					}
					
					CurrentEntry.setKeyCur(SaveKeyString);
					this.FillMinMax(ThisDataBase, Mandator, CurrentEntry);
					CurrentEntry	=	readOneEntry( ThisDataBase, Mandator, SaveKeyString, CurrentEntry );
				}

			} catch (Exception e) {
				CurrentEntry	=	null;
			} finally {
				try {
					if( updateHeadSQLStatement != null )
						updateHeadSQLStatement.close();
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		} else
			CurrentEntry	=	null;
		return CurrentEntry;
	}
}
