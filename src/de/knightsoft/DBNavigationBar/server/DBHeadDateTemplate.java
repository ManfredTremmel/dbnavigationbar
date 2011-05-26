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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
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
public abstract class DBHeadDateTemplate<E extends DomainHeadDataBase> extends RemoteServiceServlet {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 3633734786925668260L;

	private final Class<E> type;

	protected final String dataBaseTableName;
	protected final String keyFieldName;

	protected final String readMinMaxSQL;
    protected final String readNextSQL;
    protected final String readPrevSQL;
    protected final String readHeadSQL;
    protected final String invalidateHeadSQL;
    protected final String insertHeadSQL;

    protected final String lookUpDataBase;
    protected final String sessionUser;

    /**
     * Constructor, set up database connection
     * 
     * @param type - class instance of E
     * @param lookUpDataBase
     * @param sessionUser
     * @param dataBaseTableName
     * @param keyFieldName
     * @param insertHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadDateTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
       	    String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String readMinMaxSQL,
    		String readNextSQL,
    		String readPrevSQL,
    		String readHeadSQL,
    		String invalidateHeadSQL
    		) throws UnexpectedException {
		this.type = type;

		this.dataBaseTableName		=	dataBaseTableName;
		this.keyFieldName			=	keyFieldName;
		this.sessionUser			=	sessionUser;
		this.lookUpDataBase			=	lookUpDataBase;
		Connection ThisDataBase 	=	null;

		try {
			// connect to database
			InitialContext ic		=	new InitialContext();
	        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
	        ThisDataBase 			=	lDataSource.getConnection();
			ic.close();

			DataBaseDepending MyDataBaseDepending = new DataBaseDepending(ThisDataBase.getMetaData().getDatabaseProductName());

    		this.readMinMaxSQL		=
    			(readMinMaxSQL != null ?
   					readMinMaxSQL :
    				  "SELECT MIN(" + this.keyFieldName + ") AS min, "
    				+ "       MAX(" + this.keyFieldName + ") AS max "
    				+ "FROM   " + this.dataBaseTableName + " "
    				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + MyDataBaseDepending.getSQLTimeNow() + ";");

    		this.readNextSQL		=
    			(readNextSQL != null ?
    				readNextSQL :
          		  	  "SELECT MIN(" + this.keyFieldName + ") AS dbnumber "
      				+ "FROM   " + this.dataBaseTableName + " "
    				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
    				+ "  AND  " + this.keyFieldName + " > ? "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + MyDataBaseDepending.getSQLTimeNow() + ";");

    		this.readPrevSQL		=
    			(readPrevSQL != null ?
    				readPrevSQL :
        		  	  "SELECT MAX(" + this.keyFieldName + ") AS dbnumber "
        			+ "FROM   " + this.dataBaseTableName + " "
      				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
      				+ "  AND  " + this.keyFieldName + " < ? "
      				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
      				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + MyDataBaseDepending.getSQLTimeNow() + ";");

    		this.readHeadSQL		=
    			(readHeadSQL != null ?
    				readHeadSQL :
            		  "SELECT * "
      		  	  	+ "FROM   " + this.dataBaseTableName + " "
    				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
    				+ "  AND  " + this.keyFieldName + " = ? "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + MyDataBaseDepending.getSQLTimeNow() + ";");

    		this.invalidateHeadSQL	=
    			(invalidateHeadSQL != null ?
    				invalidateHeadSQL :
					  "UPDATE " + this.dataBaseTableName + " "
					+ "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "=" + MyDataBaseDepending.getSQLTimeOutdate() + " "
					+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
    				+ "  AND  " + this.keyFieldName + " = ? "
					+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
					+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + MyDataBaseDepending.getSQLTimeNow() + ";");

    		this.insertHeadSQL		=	insertHeadSQL;

		} catch( Exception e ) {
			throw new UnexpectedException( e.toString(), e.getCause() );
		} finally {
			try {
				if( ThisDataBase != null )
					ThisDataBase.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

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
     * @throws UnexpectedException
     */
    public DBHeadDateTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
       	    String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL
    		) throws UnexpectedException {
    	this(	type,
    			lookUpDataBase,
    			sessionUser,
    			dataBaseTableName,
    			keyFieldName,
    			insertHeadSQL,
    			null,
    			null,
    			null,
    			null,
    			null
    			);
    }


	/**
	 * <code>fillInsertHead</code> fills the parameters of the insert prepared statement
	 * @param insertHeadSQLStatement
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 */
	protected abstract void fillInsertHead(
			PreparedStatement insertHeadSQLStatement,
			int mandator,
			String user,
			E saveEntry,
			boolean delete) throws SQLException;

	/**
	 * <code>allowedToSee</code> checks if user is allowed to see entries
	 */
	protected abstract boolean allowedToSee();

	/**
	 * <code>allowedToChange</code> checks if user is allowed to change entries
	 */
	protected abstract boolean allowedToChange();

	/**
	 * <code>createInstance</code> creates a new Instance
	 * @return E
	 */
	protected final E createInstance() {
		try {
			return this.type.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <code>fillHeadFromResultSet</code> set the fields in thisEntry from
	 * the given resultSet
	 * @param resultHead
	 * 			ResultSet to read Entries from
	 * @param thisEntry
	 * 			Entry to fill
	 * @return filled Entry
	 */
	protected abstract E fillHeadFromResultSet(ResultSet resultHead, E thisEntry) throws Exception;

	
	/**
     * <code>RiPhoneUser</code> is used to read the currently logged in user
     * 
     * @return logged in user
     */
	protected DomainUser getUser() {
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		
		DomainUser thisUser = null;
		if( session != null )
			thisUser = (DomainUser)session.getAttribute(this.sessionUser);

		return thisUser;
	}

	/**
	 * <code>FillMinMax</code> method is called to fill the min and max
	 * entries of this database table for navigation
	 * 
	 * @param thisDataBase
	 * 			Connection to the database
	 * @param mandator
	 * 			mandator to read from
	 * @param thisEntry
	 * 			structure to be filled with user data
	 * @return the filled structure
	 * @throws SQLException
	 */
	protected E fillMinMax(
			Connection thisDataBase,
			int mandator,
			E thisEntry
			) {
		PreparedStatement readMinMaxSQLStatement	=	null;
		try {
			readMinMaxSQLStatement	=	thisDataBase.prepareStatement(this.readMinMaxSQL);
			readMinMaxSQLStatement.clearParameters();
			readMinMaxSQLStatement.setInt( 1, mandator);
			ResultSet result = readMinMaxSQLStatement.executeQuery();

	        if( result.next() ) {
	        	thisEntry.setKeyMin(result.getString("min"));
	        	thisEntry.setKeyMax(result.getString("max"));
	        }
	        result.close();

        } catch( SQLException e ) {
        	thisEntry	=	null;
        } finally {
	        try {
	        	if( readMinMaxSQLStatement != null )
	        		readMinMaxSQLStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();;
			}
        }
		return thisEntry;
	}


	/**
	 * <code>deleteEntry</code> deletes one entry from database
	 * 
	 * @param currentEntry
	 */
	public E deleteEntry(String currentEntry) {
		E	ResultValue	=	null;				
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			String user		=	thisUser.getUser();
			Connection thisDataBase		=	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				if( allowedToChange() ) {
					E DBEntry	=	this.readEntry(currentEntry);
					// invalidate head number
					invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(this.invalidateHeadSQL);
        			invalidateHeadSQLStatement.clearParameters();
        			invalidateHeadSQLStatement.setInt(1, mandator);
        			invalidateHeadSQLStatement.setString(2, currentEntry);
        			invalidateHeadSQLStatement.executeUpdate();
	        		this.insertEntry( thisDataBase, mandator, user, DBEntry, true);
				}
				ResultValue	= readNextEntry(currentEntry);
	        } catch( Exception e ) {
	        	ResultValue	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ResultValue;
	}

	/**
	 * <code>searchSQLSelect</code> setup a part of the sql statement to search for
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
	protected String searchSQLSelect(
			Connection thisDataBase,
			String minMax,
			String searchField,
			String searchMethodeEntry,
			String searchFieldEntry,
			String dbKeyVGL,
			String dbKey) throws Exception {
		int mandator	 	=	this.getUser().getMandator();
		DataBaseDepending myDataBaseDepending = new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

		String sqlString =
    			"SELECT " + minMax + "(" + this.keyFieldName + ") AS dbnumber "
			+	"FROM   " + this.dataBaseTableName + " "
			+	"WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " "
			+	" AND   " + this.keyFieldName + " " + dbKeyVGL + " " + StringToSQL.convertString(dbKey, thisDataBase.getMetaData().getDatabaseProductName()) + " "
			+	" AND	" + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
			+	" AND	" + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow() + " "
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
	 * <code>findFirstEntry</code> is called to search for the first entry
	 * which fulfills the search parameters
	 * 
	 * @param searchField
	 * @param searchMethodeEntry
	 * @param searchFieldEntry
	 * @return the found entry or null if none is found
	 */
	public E findFirstEntry(String searchField,
			String searchMethodeEntry, String searchFieldEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;
			try {
				if( searchFieldEntry == null || "".equals(searchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
			        thisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		            String newEntry		=	thisEntry.getKeyMin();

		            String sqlString	=	this.searchSQLSelect(thisDataBase, "MIN", searchField, searchMethodeEntry, searchFieldEntry, ">=", newEntry);
		            if( sqlString != null ) {
			            ResultSet result	=	thisDataBase.createStatement().executeQuery(sqlString);
			            if( result.next() )
			            	newEntry	=	result.getString("dbnumber");
			            else
			            	newEntry	=	null;
			            result.close();
		            } else
		            	newEntry	=	null;

		            if( newEntry !=	null )
		            	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
				thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
    	return thisEntry;
	}

	/**
	 * <code>findLastEntry</code> is called to search for the last entry
	 * which fulfills the search parameters
	 * 
	 * @param searchField
	 * @param searchMethodeEntry
	 * @param searchFieldEntry
	 * @return the found entry or null if none is found
	 */
	public E findLastEntry(String searchField,
			String searchMethodeEntry, String searchFieldEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;
			try {
				if( searchFieldEntry == null || "".equals(searchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
			        thisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		            String newEntry		=	thisEntry.getKeyMax();

		            String sqlString	=	this.searchSQLSelect(thisDataBase, "MAX", searchField, searchMethodeEntry, searchFieldEntry, "<=", newEntry);
		            if( sqlString != null ) {
			            ResultSet result	=	thisDataBase.createStatement().executeQuery(sqlString);
			            if( result.next() )
			            	newEntry	=	result.getString("dbnumber");
			            else
			            	newEntry	=	null;
			            result.close();
		            } else
		            	newEntry	=	null;

		            if( newEntry !=	null )
		            	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
    	return thisEntry;
	}

	/**
	 * <code>findNextEntry</code> is called to search for the next entry
	 * which fulfills the search parameters
	 * 
	 * @param searchField
	 * @param searchMethodeEntry
	 * @param searchFieldEntry
	 * @param currentEntry
	 * @return the found entry or null if none is found
	 */
	public E findNextEntry(String searchField,
			String searchMethodeEntry, String searchFieldEntry,
			String currentEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;	
			try {
				if( searchFieldEntry == null || "".equals(searchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
			        thisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		            String newEntry =	currentEntry;

		            String sqlString	=	this.searchSQLSelect(thisDataBase, "MIN", searchField, searchMethodeEntry, searchFieldEntry, ">", newEntry);
		            if( sqlString != null ) {
			            ResultSet result	=	thisDataBase.createStatement().executeQuery(sqlString);
			            if( result.next() )
			            	newEntry	=	result.getString("dbnumber");
			            else
			            	newEntry	=	null;
			            result.close();
		            } else
		            	newEntry	=	null;

		            if( newEntry !=	null )
		            	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
    	return thisEntry;
	}

	/**
	 * <code>findPreviousEntry</code> is called to search for the previous entry
	 * which fulfills the search parameters
	 * 
	 * @param searchField
	 * @param searchMethodeEntry
	 * @param searchFieldEntry
	 * @param currentEntry
	 * @return the found entry or null if none is found
	 */
	public E findPreviousEntry(String searchField,
			String searchMethodeEntry, String searchFieldEntry,
			String currentEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;	
			try {
				if( searchFieldEntry == null || "".equals(searchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource		=	(DataSource)ic.lookup(this.lookUpDataBase);
			        thisDataBase = lDataSource.getConnection();
					ic.close();

		            thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		            String newEntry =	currentEntry;

		            String sqlString	=	this.searchSQLSelect(thisDataBase, "MAX", searchField, searchMethodeEntry, searchFieldEntry, "<", newEntry);
		            if( sqlString != null ) {
			            ResultSet result	=	thisDataBase.createStatement().executeQuery(sqlString);
			            if( result.next() )
			            	newEntry	=	result.getString("dbnumber");
			            else
			            	newEntry	=	null;
			            result.close();
		            } else
		            	newEntry	=	null;

		            if( newEntry !=	null )
		            	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
    	return thisEntry;
	}
	
	/**
	 * <code>readOneEntry</code> is used to read a given entry
	 * from database
	 * 
	 * @param thisDataBase
	 * 			Database Connection
	 * @param mandator
	 * 			mandator is a keyfield
	 * @param entry
	 * 			the Entry to read
	 * @param thisEntry
	 * 			structure to be filled
	 * @return the filled structure
	 * @throws SQLException
	 */
	protected E readOneEntry(Connection thisDataBase, int mandator, String entry, E thisEntry) {
		PreparedStatement readHeadSQLStatement	=	null;
		try {
			if( thisEntry != null && allowedToSee() ) {
				thisEntry.setIsReadOnly(!allowedToChange());
				thisEntry.setKeyCur(entry);
				readHeadSQLStatement	=	thisDataBase.prepareStatement(readHeadSQL);
    			readHeadSQLStatement.clearParameters();
    			readHeadSQLStatement.setInt(1, mandator);
    			readHeadSQLStatement.setString(2, entry);
    			ResultSet resultHead	=	readHeadSQLStatement.executeQuery();
				if( resultHead.next() ) {
					thisEntry = fillHeadFromResultSet(resultHead, thisEntry);
				} else
					thisEntry.setKeyCur(null);
				resultHead.close();
			} else
				thisEntry	=	null;
		} catch( Exception nef ) {
			thisEntry	=	null;
		} finally {
			try {
				if( readHeadSQLStatement != null )
					readHeadSQLStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();;
			}
		}
		return thisEntry;
	}

	/**
	 * <code>readEntry</code> is used to read a given entry
	 * from database
	 * 
	 * @param entry
	 * 			the entry to read
	 * @return the filled structure
	 */
	public E readEntry(String entry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;	

			try {
				thisEntry = createInstance();
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase =	lDataSource.getConnection();
				ic.close();
				if( thisEntry	!=	null )
					thisEntry	=	readOneEntry( thisDataBase, mandator, entry, thisEntry );
				if( thisEntry	!=	null )
					thisEntry	=	fillMinMax( thisDataBase, mandator, thisEntry );
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>readFirstEntry</code> is used to read the first entry
	 * from database
	 * 
	 * @return the filled structure
	 */
	public E readFirstEntry() {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			Connection thisDataBase	=	null;

			try {
				thisEntry = createInstance();
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		        if( thisEntry	!=	null )
		        	thisEntry	=	readOneEntry( thisDataBase, mandator, thisEntry.getKeyMin(), thisEntry );
		        else
		        	thisEntry	=	null;
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>readLastEntry</code> is used to read the last entry
	 * from database
	 * 
	 * @return the filled structure
	 */
	public E readLastEntry() {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			thisEntry				=	createInstance();
			Connection thisDataBase =	null;
			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		        if( thisEntry	!=	null )
		        	thisEntry	=	readOneEntry( thisDataBase, mandator, thisEntry.getKeyMax(), thisEntry );
		        else
		        	thisEntry	=	null;
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>readNextEntry</code> is used to read the next entry
	 * from database
	 * 
	 * @param currentEntry
	 * 			the currently displayed entry
	 * @return the filled structure
	 */
	public E readNextEntry(String currentEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			thisEntry				=	createInstance();
			Connection thisDataBase =	null;
			PreparedStatement readNextSQLStatement			=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		        String newEntryName = thisEntry.getKeyMax();
		        if( thisEntry != null ) {
		        	if( currentEntry != null && !"".equals(currentEntry)) {
		        		readNextSQLStatement			=	thisDataBase.prepareStatement(this.readNextSQL);
	        			readNextSQLStatement.clearParameters();
	        			readNextSQLStatement.setInt( 1, mandator);
	        			readNextSQLStatement.setString( 2, currentEntry);
	        			ResultSet result = readNextSQLStatement.executeQuery();

				        if( result.next() )
				        	newEntryName	=	result.getString("dbnumber");
				        result.close();
		        	}
		        	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntryName, thisEntry );
		        }
		    } catch( Exception e ) {
		    	thisEntry	=	null;
			} finally {
				try {
					if( readNextSQLStatement != null )
						readNextSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>readPreviousEntry</code> is used to read the previous entry
	 * from database
	 * 
	 * @param currentEntry
	 * 			the currently displayed entry
	 * @return the filled user structure
	 */
	public E readPreviousEntry(String currentEntry) {
		E thisEntry = null;
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			thisEntry				=	createInstance();
			Connection thisDataBase	=	null;
			PreparedStatement readPrevSQLStatement			=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = fillMinMax( thisDataBase, mandator, thisEntry );
		        String newEntryName = thisEntry.getKeyMin();
		        if( thisEntry != null ) {
		        	if( currentEntry != null && !"".equals(currentEntry)) {
		        		readPrevSQLStatement			=	thisDataBase.prepareStatement(this.readPrevSQL);
	        			readPrevSQLStatement.clearParameters();
	        			readPrevSQLStatement.setInt( 1, mandator);
	        			readPrevSQLStatement.setString( 2, currentEntry);
	        			ResultSet result = readPrevSQLStatement.executeQuery();

				        if( result.next() )
				        	newEntryName	=	result.getString("dbnumber");
				        result.close();
		        	}
		        	thisEntry	=	readOneEntry( thisDataBase, mandator, newEntryName, thisEntry );
		        }
		    } catch( Exception e ) {
		    	thisEntry	=	null;
			} finally {
				try {
					if( readPrevSQLStatement != null )
						readPrevSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>insertEntry</code> inserts a entry into the database
	 * 
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 * @return effected database entries (should always be 1)
	 * @throws SQLException
	 */
	protected int insertEntry( Connection thisDataBase, int mandator, String user, E saveEntry, boolean delete
			) throws SQLException {
		int num = -1;
		PreparedStatement insertHeadSQLStatement		=	null;
		try {
			insertHeadSQLStatement		=	thisDataBase.prepareStatement(insertHeadSQL);
			insertHeadSQLStatement.clearParameters();
			fillInsertHead(insertHeadSQLStatement, mandator, user, saveEntry, delete);
	 		num = insertHeadSQLStatement.executeUpdate();
		} finally {
			if( insertHeadSQLStatement != null )
				insertHeadSQLStatement.close();
		}
		return num;
	}

	/**
	 * <code>saveEntry</code> saves or inserts a entry to database
	 * 
	 * @param currentEntry
	 * 			entry that has to be saved
	 */
	public E saveEntry(E currentEntry) {
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			String user		=	thisUser.getUser();
			String saveKeyString	=	currentEntry.getKeyCur();
			if( saveKeyString == null || "".equals(saveKeyString) )
				saveKeyString		=	currentEntry.getKeyNew();

			Connection thisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E dbEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
			        thisDataBase			=	lDataSource.getConnection();
					ic.close();

					dbEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, dbEntry );
					if((dbEntry != null) && (dbEntry.getKeyCur() == null))
						dbEntry	=	null;

					// Entry already exists in Database?
					if( !currentEntry.equals( dbEntry ) ) {
						if( !currentEntry.equalsEntry(dbEntry) ) {
							// Invalidate old entry
							invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(this.invalidateHeadSQL);
		        			invalidateHeadSQLStatement.clearParameters();
		        			invalidateHeadSQLStatement.setInt(1, mandator);
		        			invalidateHeadSQLStatement.setString(2, saveKeyString);
		        			invalidateHeadSQLStatement.executeUpdate();

							// Insert new entry
							this.insertEntry( thisDataBase, mandator, user, currentEntry, false);

							currentEntry.setKeyCur(saveKeyString);
							this.fillMinMax(thisDataBase, mandator, currentEntry);
							currentEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, currentEntry );
						}
					}
				}
			} catch (Exception e) {
				currentEntry	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else
			currentEntry	=	null;
		return currentEntry;
	}
}
