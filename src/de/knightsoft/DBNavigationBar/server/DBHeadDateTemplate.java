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

	protected String DataBaseTableName	= null;
	protected String KeyfieldName		= null;

	protected String readMinMaxSQL		= null;
    protected String readNextSQL		= null;
    protected String readPrevSQL		= null;
    protected String readHeadSQL		= null;
    protected String invalidateHeadSQL	= null;
    protected String insertHeadSQL		= null;

    protected String LookUpDataBase	= null;
    protected String SessionUser		= null;

    /**
     * Constructor, set up database connection
     * 
     * @param LookUpDataBase
     * @param SessionUser
     * @param DataBaseTableName
     * @param KeyfieldName
     * @param insertHeadSQL
     * @throws UnexpectedException
     */
    public DBHeadDateTemplate(
       		String LookUpDataBase,
       		String SessionUser,
       	    String DataBaseTableName,
    		String KeyfieldName,
    		String insertHeadSQL
    		) throws UnexpectedException {
		super();

		this.DataBaseTableName		=	DataBaseTableName;
		this.KeyfieldName			=	KeyfieldName;
		this.SessionUser			=	SessionUser;
		this.LookUpDataBase			=	LookUpDataBase;
		Connection ThisDataBase 	=	null;

		try {
			// connect to database
			InitialContext ic		=	new InitialContext();
	        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
	        ThisDataBase 			=	lDataSource.getConnection();
			ic.close();

			DataBaseDepending MyDataBaseDepending = new DataBaseDepending(ThisDataBase.getMetaData().getDatabaseProductName());

    		this.readMinMaxSQL		=
    				  "SELECT MIN(" + this.KeyfieldName + ") AS min, "
    				+ "       MAX(" + this.KeyfieldName + ") AS max "
    				+ "FROM   " + this.DataBaseTableName + " "
    				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
    				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.readNextSQL		=
          		  	  "SELECT MIN(" + this.KeyfieldName + ") AS dbnumber "
      				+ "FROM   " + this.DataBaseTableName + " "
    				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
    				+ "  AND  " + this.KeyfieldName + " > ? "
    				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.readPrevSQL		=
        		  	  "SELECT MAX(" + this.KeyfieldName + ") AS dbnumber "
        			+ "FROM   " + this.DataBaseTableName + " "
      				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
      				+ "  AND  " + this.KeyfieldName + " < ? "
      				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
      				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.readHeadSQL		=
            		  "SELECT * "
      		  	  	+ "FROM   " + this.DataBaseTableName + " "
    				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
    				+ "  AND  " + this.KeyfieldName + " = ? "
    				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
    				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.invalidateHeadSQL	=
					  "UPDATE " + this.DataBaseTableName + " "
					+ "SET    " + Constants.DBFieldGlobalDate_to + "=" + MyDataBaseDepending.getSQLTimeOutdate() + " "
					+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
    				+ "  AND  " + this.KeyfieldName + " = ? "
					+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
					+ "  AND  " + Constants.DBFieldGlobalDate_to + "   > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.insertHeadSQL		=	insertHeadSQL;

		} catch( Exception e ) {
			throw new UnexpectedException( e.toString(), e.getCause() );
		} finally {
			try {
				if( ThisDataBase != null )
					ThisDataBase.close();
			} catch (SQLException e) {
				// ignore
			}
		}

	}


	/**
	 * <code>fillInsertHead</code> fills the parameters of the insert prepared statement
	 * @param insertHeadSQLStatement
	 * @param Mandator
	 * @param User
	 * @param SaveEntry
	 * @param Delete
	 */
	protected abstract void fillInsertHead(
			PreparedStatement insertHeadSQLStatement,
			int Mandator,
			String User,
			E SaveEntry,
			boolean Delete) throws SQLException;

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
	protected abstract E createInstance();

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
			thisUser = (DomainUser)session.getAttribute(SessionUser);

		return thisUser;
	}

	/**
	 * <code>FillMinMax</code> method is called to fill the min and max
	 * entries of this database table for navigation
	 * 
	 * @param ThisDataBase
	 * 			Connection to the database
	 * @param Mandator
	 * 			mandator to read from
	 * @param thisEntry
	 * 			structure to be filled with user data
	 * @return the filled structure
	 * @throws SQLException
	 */
	protected E FillMinMax(
			Connection ThisDataBase,
			int Mandator,
			E thisEntry
			) {
		PreparedStatement readMinMaxSQLStatement	=	null;
		try {
			readMinMaxSQLStatement	=	ThisDataBase.prepareStatement(this.readMinMaxSQL);
			readMinMaxSQLStatement.clearParameters();
			readMinMaxSQLStatement.setInt( 1, Mandator);
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
				// ignore;
			}
        }
		return thisEntry;
	}


	/**
	 * <code>deleteEntry</code> deletes one entry from database
	 * 
	 * @param CurrentEntry
	 */
	public E deleteEntry(String CurrentEntry) {
		E	ResultValue	=	null;				
		if( this.getUser() !=	null ) {
			int Mandator	=	this.getUser().getMandator();
			String User		=	this.getUser().getUser();
			Connection ThisDataBase		=	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase			=	lDataSource.getConnection();
				ic.close();

				if( allowedToChange() ) {
					E DBEntry	=	this.readEntry(CurrentEntry);
					// invalidate head number
					invalidateHeadSQLStatement	=	ThisDataBase.prepareStatement(invalidateHeadSQL);
        			invalidateHeadSQLStatement.clearParameters();
        			invalidateHeadSQLStatement.setInt(1, Mandator);
        			invalidateHeadSQLStatement.setString(2, CurrentEntry);
        			invalidateHeadSQLStatement.executeUpdate();
	        		this.insertEntry( ThisDataBase, Mandator, User, DBEntry, true);
				}
				ResultValue	= readNextEntry(CurrentEntry);
	        } catch( Exception e ) {
	        	ResultValue	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		return ResultValue;
	}

	/**
	 * <code>searchSQLSelect</code> setup a part of the sql statement to search for
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
		DataBaseDepending MyDataBaseDepending = new DataBaseDepending(ThisDataBase.getMetaData().getDatabaseProductName());

		String SQLString =
    			"SELECT " + MinMax + "(" + this.KeyfieldName + ") AS dbnumber "
			+	"FROM   " + this.DataBaseTableName + " "
			+	"WHERE  " + Constants.DBFieldGlobalMandator + " = " + Integer.toString(Mandator) + " "
			+	" AND   " + this.KeyfieldName + " " + DBKeyVGL + " " + StringToSQL.convertString(DBKey, ThisDataBase.getMetaData().getDatabaseProductName()) + " "
			+	" AND	" + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
			+	" AND	" + Constants.DBFieldGlobalDate_to + "   > " + MyDataBaseDepending.getSQLTimeNow() + " "
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
	 * <code>findFirstEntry</code> is called to search for the first entry
	 * which fulfills the search parameters
	 * 
	 * @param SearchField
	 * @param SearchMethodeEntry
	 * @param SearchFieldEntry
	 * @return the found entry or null if none is found
	 */
	public E findFirstEntry(String SearchField,
			String SearchMethodeEntry, String SearchFieldEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;
			try {
				if( SearchFieldEntry == null || "".equals(SearchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		            String NewEntry		=	thisEntry.getKeyMin();

		            String SQLString	=	this.searchSQLSelect(ThisDataBase, "MIN", SearchField, SearchMethodeEntry, SearchFieldEntry, ">=", NewEntry);
		            if( SQLString != null ) {
			            ResultSet result	=	ThisDataBase.createStatement().executeQuery(SQLString);
			            if( result.next() )
			            	NewEntry	=	result.getString("dbnumber");
			            else
			            	NewEntry	=	null;
			            result.close();
		            } else
		            	NewEntry	=	null;

		            if( NewEntry !=	null )
		            	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
				thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param SearchField
	 * @param SearchMethodeEntry
	 * @param SearchFieldEntry
	 * @return the found entry or null if none is found
	 */
	public E findLastEntry(String SearchField,
			String SearchMethodeEntry, String SearchFieldEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;
			try {
				if( SearchFieldEntry == null || "".equals(SearchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		            String NewEntry		=	thisEntry.getKeyMax();

		            String SQLString	=	this.searchSQLSelect(ThisDataBase, "MAX", SearchField, SearchMethodeEntry, SearchFieldEntry, "<=", NewEntry);
		            if( SQLString != null ) {
			            ResultSet result	=	ThisDataBase.createStatement().executeQuery(SQLString);
			            if( result.next() )
			            	NewEntry	=	result.getString("dbnumber");
			            else
			            	NewEntry	=	null;
			            result.close();
		            } else
		            	NewEntry	=	null;

		            if( NewEntry !=	null )
		            	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param SearchField
	 * @param SearchMethodeEntry
	 * @param SearchFieldEntry
	 * @param CurrentEntry
	 * @return the found entry or null if none is found
	 */
	public E findNextEntry(String SearchField,
			String SearchMethodeEntry, String SearchFieldEntry,
			String CurrentEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;	
			try {
				if( SearchFieldEntry == null || "".equals(SearchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase			=	lDataSource.getConnection();
					ic.close();

		            thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		            String NewEntry =	CurrentEntry;

		            String SQLString	=	this.searchSQLSelect(ThisDataBase, "MIN", SearchField, SearchMethodeEntry, SearchFieldEntry, ">", NewEntry);
		            if( SQLString != null ) {
			            ResultSet result	=	ThisDataBase.createStatement().executeQuery(SQLString);
			            if( result.next() )
			            	NewEntry	=	result.getString("dbnumber");
			            else
			            	NewEntry	=	null;
			            result.close();
		            } else
		            	NewEntry	=	null;

		            if( NewEntry !=	null )
		            	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param SearchField
	 * @param SearchMethodeEntry
	 * @param SearchFieldEntry
	 * @param CurrentEntry
	 * @return the found entry or null if none is found
	 */
	public E findPreviousEntry(String SearchField,
			String SearchMethodeEntry, String SearchFieldEntry,
			String CurrentEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;	
			try {
				if( SearchFieldEntry == null || "".equals(SearchFieldEntry)) 
					thisEntry = this.readFirstEntry();
				else {
					thisEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource		=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase = lDataSource.getConnection();
					ic.close();

		            thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		            String NewEntry =	CurrentEntry;

		            String SQLString	=	this.searchSQLSelect(ThisDataBase, "MAX", SearchField, SearchMethodeEntry, SearchFieldEntry, "<", NewEntry);
		            if( SQLString != null ) {
			            ResultSet result	=	ThisDataBase.createStatement().executeQuery(SQLString);
			            if( result.next() )
			            	NewEntry	=	result.getString("dbnumber");
			            else
			            	NewEntry	=	null;
			            result.close();
		            } else
		            	NewEntry	=	null;

		            if( NewEntry !=	null )
		            	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntry, thisEntry );
		            else
		            	thisEntry.setKeyCur(null);
				}
	        } catch( Exception e ) {
	        	thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param ThisDataBase
	 * 			Database Connection
	 * @param Mandator
	 * 			mandator is a keyfield
	 * @param Entry
	 * 			the Entry to read
	 * @param thisEntry
	 * 			structure to be filled
	 * @return the filled structure
	 * @throws SQLException
	 */
	protected E readOneEntry(Connection ThisDataBase, int Mandator, String Entry, E thisEntry) {
		PreparedStatement readHeadSQLStatement	=	null;
		try {
			if( thisEntry != null && allowedToSee() ) {
				thisEntry.setIsReadOnly(!allowedToChange());
				thisEntry.setKeyCur(Entry);
				readHeadSQLStatement	=	ThisDataBase.prepareStatement(readHeadSQL);
    			readHeadSQLStatement.clearParameters();
    			readHeadSQLStatement.setInt(1, Mandator);
    			readHeadSQLStatement.setString(2, Entry);
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
				// ignore;
			}
		}
		return thisEntry;
	}

	/**
	 * <code>readEntry</code> is used to read a given entry
	 * from database
	 * 
	 * @param Entry
	 * 			the entry to read
	 * @return the filled structure
	 */
	public E readEntry(String Entry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;	

			try {
				thisEntry = createInstance();
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase =	lDataSource.getConnection();
				ic.close();
				if( thisEntry	!=	null )
					thisEntry	=	readOneEntry( ThisDataBase, Mandator, Entry, thisEntry );
				if( thisEntry	!=	null )
					thisEntry	=	FillMinMax( ThisDataBase, Mandator, thisEntry );
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			Connection ThisDataBase	=	null;

			try {
				thisEntry = createInstance();
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		        if( thisEntry	!=	null )
		        	thisEntry	=	readOneEntry( ThisDataBase, Mandator, thisEntry.getKeyMin(), thisEntry );
		        else
		        	thisEntry	=	null;
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			thisEntry				=	createInstance();
			Connection ThisDataBase =	null;
			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		        if( thisEntry	!=	null )
		        	thisEntry	=	readOneEntry( ThisDataBase, Mandator, thisEntry.getKeyMax(), thisEntry );
		        else
		        	thisEntry	=	null;
			} catch (Exception e) {
				thisEntry	=	null;
			} finally {
				try {
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param CurrentEntry
	 * 			the currently displayed entry
	 * @return the filled structure
	 */
	public E readNextEntry(String CurrentEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			thisEntry				=	createInstance();
			Connection ThisDataBase =	null;
			PreparedStatement readNextSQLStatement			=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		        String NewEntryName = thisEntry.getKeyMax();
		        if( thisEntry != null ) {
		        	if( CurrentEntry != null && !"".equals(CurrentEntry)) {
		        		readNextSQLStatement			=	ThisDataBase.prepareStatement(readNextSQL);
	        			readNextSQLStatement.clearParameters();
	        			readNextSQLStatement.setInt( 1, Mandator);
	        			readNextSQLStatement.setString( 2, CurrentEntry);
	        			ResultSet result = readNextSQLStatement.executeQuery();

				        if( result.next() )
				        	NewEntryName	=	result.getString("dbnumber");
				        result.close();
		        	}
		        	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntryName, thisEntry );
		        }
		    } catch( Exception e ) {
		    	thisEntry	=	null;
			} finally {
				try {
					if( readNextSQLStatement != null )
						readNextSQLStatement.close();
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
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
	 * @param CurrentEntry
	 * 			the currently displayed entry
	 * @return the filled user structure
	 */
	public E readPreviousEntry(String CurrentEntry) {
		E thisEntry = null;
		if( this.getUser() !=	null ) {
			int Mandator			=	this.getUser().getMandator();
			thisEntry				=	createInstance();
			Connection ThisDataBase	=	null;
			PreparedStatement readPrevSQLStatement			=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase			=	lDataSource.getConnection();
				ic.close();

				thisEntry = FillMinMax( ThisDataBase, Mandator, thisEntry );
		        String NewEntryName = thisEntry.getKeyMin();
		        if( thisEntry != null ) {
		        	if( CurrentEntry != null && !"".equals(CurrentEntry)) {
		        		readPrevSQLStatement			=	ThisDataBase.prepareStatement(readPrevSQL);
	        			readPrevSQLStatement.clearParameters();
	        			readPrevSQLStatement.setInt( 1, Mandator);
	        			readPrevSQLStatement.setString( 2, CurrentEntry);
	        			ResultSet result = readPrevSQLStatement.executeQuery();

				        if( result.next() )
				        	NewEntryName	=	result.getString("dbnumber");
				        result.close();
		        	}
		        	thisEntry	=	readOneEntry( ThisDataBase, Mandator, NewEntryName, thisEntry );
		        }
		    } catch( Exception e ) {
		    	thisEntry	=	null;
			} finally {
				try {
					if( readPrevSQLStatement != null )
						readPrevSQLStatement.close();
					if( ThisDataBase != null )
						ThisDataBase.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		} else
			thisEntry	=	null;
		return thisEntry;
	}

	/**
	 * <code>insertEntry</code> inserts a entry into the database
	 * 
	 * @param Mandator
	 * @param User
	 * @param SaveEntry
	 * @param Delete
	 * @return effected database entries (should always be 1)
	 * @throws SQLException
	 */
	protected int insertEntry( Connection ThisDataBase, int Mandator, String User, E SaveEntry, boolean Delete
			) throws SQLException {
		int num = -1;
		PreparedStatement insertHeadSQLStatement		=	null;
		try {
			insertHeadSQLStatement		=	ThisDataBase.prepareStatement(insertHeadSQL);
			insertHeadSQLStatement.clearParameters();
			fillInsertHead(insertHeadSQLStatement, Mandator, User, SaveEntry, Delete);
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
			PreparedStatement invalidateHeadSQLStatement	=	null;

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

					// Entry already exists in Database?
					if( !CurrentEntry.equals( DBEntry ) ) {
						if( !CurrentEntry.equalsEntry(DBEntry) ) {
							// Invalidate old entry
							invalidateHeadSQLStatement	=	ThisDataBase.prepareStatement(invalidateHeadSQL);
		        			invalidateHeadSQLStatement.clearParameters();
		        			invalidateHeadSQLStatement.setInt(1, Mandator);
		        			invalidateHeadSQLStatement.setString(2, SaveKeyString);
		        			invalidateHeadSQLStatement.executeUpdate();

							// Insert new entry
							this.insertEntry( ThisDataBase, Mandator, User, CurrentEntry, false);

							CurrentEntry.setKeyCur(SaveKeyString);
							this.FillMinMax(ThisDataBase, Mandator, CurrentEntry);
							CurrentEntry	=	readOneEntry( ThisDataBase, Mandator, SaveKeyString, CurrentEntry );
						}
					}
				}
			} catch (Exception e) {
				CurrentEntry	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
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
