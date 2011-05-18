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
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBase;
import de.knightsoft.DBNavigationBar.shared.Constants;

/**
 * 
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation
 * template for a simple database
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-08
 */
public abstract class DBHeadPosDateTemplate<E extends DomainHeadPosDataBase>
	extends DBHeadDateTemplate<E> {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 3633734786925668260L;

	protected final String readPosSQL;
    protected final String invalidatePosSQL;
    protected final String insertPosSQL;

    /**
     * Constructor, set up database connection
     * 
     * @param type - class instance of E
     * @param lookUpDataBase
     * @param sessionUser
     * @param dataBaseTableName
     * @param keyFieldName
     * @param insertHeadSQL
     * @param posDataBaseTableName
     * @param posKeyfieldName
     * @param insertPosSQL
     * @throws UnexpectedException
     */
    public DBHeadPosDateTemplate(
    		Class<E> type,
    		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String posDataBaseTableName,
    		String posKeyfieldName,
    		String insertPosSQL,

    		String readMinMaxSQL,
    		String readNextSQL,
    		String readPrevSQL,
    		String readHeadSQL,
    		String invalidateHeadSQL,
    		
    		String readPosSQL,
    	    String invalidatePosSQL
    		) throws UnexpectedException {
		super(  type,
				lookUpDataBase,
	       		sessionUser,
				dataBaseTableName,
				keyFieldName,
				insertHeadSQL,
				readMinMaxSQL,
				readNextSQL,
				readPrevSQL,
				readHeadSQL,
				invalidateHeadSQL
			);

		Connection ThisDataBase		=	null;
		try {
			// connect to database
			InitialContext ic		=	new InitialContext();
	        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
	        ThisDataBase 			=	lDataSource.getConnection();
			ic.close();

			DataBaseDepending myDataBaseDepending = new DataBaseDepending(ThisDataBase.getMetaData().getDatabaseProductName());

			this.readPosSQL		=
				(readPosSQL != null ?
					readPosSQL :
          		  "SELECT * "
    		  	+ "FROM   " + posDataBaseTableName + " "
  				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
  				+ "  AND  " + keyFieldName + " = ? "
  				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
  				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + myDataBaseDepending.getSQLTimeNow() + "; ");


    		this.invalidatePosSQL		=
    			(invalidatePosSQL != null ?
    				invalidatePosSQL :
				  "UPDATE " + posDataBaseTableName + " "
				+ "SET    " + Constants.DBFieldGlobalDate_to + "=" + myDataBaseDepending.getSQLTimeOutdate() + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
	    		+ "  AND  " + keyFieldName + " = ? "
	    		+ "  AND  " + posKeyfieldName + " = ? "
				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
				+ "  AND  " + Constants.DBFieldGlobalDate_to + "   > " + myDataBaseDepending.getSQLTimeNow() + ";");

    		this.insertPosSQL		=	insertPosSQL;

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
     * @param posDataBaseTableName
     * @param posKeyfieldName
     * @param insertPosSQL
     * @throws UnexpectedException
     */
    public DBHeadPosDateTemplate(
    		Class<E> type,
    		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String posDataBaseTableName,
    		String posKeyfieldName,
    		String insertPosSQL
			) throws UnexpectedException {
    	this(	type,
    			lookUpDataBase,
    			sessionUser,
    			dataBaseTableName,
    			keyFieldName,
    			insertHeadSQL,
    			posDataBaseTableName,
    			posKeyfieldName,
    			insertPosSQL,
    			null,
    			null,
    			null,
    			null,
    			null,
    			null,
    			null);
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
     * @param posDataBaseTableName
     * @param posKeyfieldName
     * @param insertPosSQL
     * @throws UnexpectedException
     */
    public DBHeadPosDateTemplate(
    		Class<E> type,
    		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String posDataBaseTableName,
    		String posKeyfieldName,
    		String insertPosSQL,
    		String readHeadSQL,
    		String readPosSQL
    		) throws UnexpectedException {
    	this(	type,
    			lookUpDataBase,
    			sessionUser,
    			dataBaseTableName,
    			keyFieldName,
    			insertHeadSQL,
    			posDataBaseTableName,
    			posKeyfieldName,
    			insertPosSQL,
    			null,
    			null,
    			null,
    			readHeadSQL,
    			null,
    			readPosSQL,
    			null);
    }


	/**
	 * <code>fillInsertPos</code> fills the parameters of the insert prepared statement
	 * @param insertPosSQLStatement
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 * @param posNumber
	 */
	protected abstract void fillInsertPos(
			PreparedStatement insertPosSQLStatement,
			int mandator,
			String user,
			E saveEntry,
			boolean delete,
			int posNumber) throws SQLException;

	/**
	 * <code>fillPosFromResultSet</code> set the fields in thisEntry from
	 * the given resultSet
	 * @param resultPos
	 * 			ResultSet to read Entries from
	 * @param thisEntry
	 * 			Entry to fill
	 * @return filled Entry
	 */
	protected abstract E fillPosFromResultSet(ResultSet resultPos, E thisEntry) throws Exception;


	/**
	 * <code>deleteEntry</code> deletes one entry from database
	 * 
	 * @param currentEntry
	 */
	public E deleteEntry(String currentEntry) {
		E	resultValue	=	null;				
		if( this.getUser() !=	null ) {
			int mandator	=	this.getUser().getMandator();
			String user		=	this.getUser().getUser();
			Connection thisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;
			PreparedStatement invalidatePosSQLStatement		=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
		        thisDataBase			=	lDataSource.getConnection();
				ic.close();

				if( allowedToChange() ) {
					E dbEntry	=	this.readEntry(currentEntry);
					// invalidate head number
					if(invalidateHeadSQL != null) {
						invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(invalidateHeadSQL);
	        			invalidateHeadSQLStatement.clearParameters();
	        			invalidateHeadSQLStatement.setInt(1, mandator);
	        			invalidateHeadSQLStatement.setString(2, currentEntry);
	        			invalidateHeadSQLStatement.executeUpdate();
		        		this.insertEntry( thisDataBase, mandator, user, dbEntry, true);
					}

					invalidatePosSQLStatement		=	thisDataBase.prepareStatement(invalidatePosSQL);
					for(int i = 0; i < (dbEntry.getKeyPos() == null ? 0 : dbEntry.getKeyPos().length); i++ ) {
						invalidatePosSQLStatement.clearParameters();
						invalidatePosSQLStatement.setInt(1, mandator);
						invalidatePosSQLStatement.setString(2, currentEntry);
						invalidatePosSQLStatement.setString(3, dbEntry.getKeyPos()[i]);
						invalidatePosSQLStatement.executeUpdate();
						this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
					}
				}

				resultValue	= readNextEntry(currentEntry);
	        } catch( Exception e ) {
	        	resultValue	=	null;
			} finally {
				try {
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( thisDataBase != null )
						thisDataBase.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		return resultValue;
	}


	/**
	 * <code>insertPositionEntry</code> inserts a position into the database
	 * @param thisDataBase 
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 * @param posNumber
	 * @return effected database entries (should always be 1)
	 * @throws SQLException
	 */
	protected int insertPositionEntry( Connection thisDataBase, int mandator, String user, E saveEntry,
			boolean delete, int posNumber
			) throws SQLException {
		int num = -1;
		PreparedStatement insertPosSQLStatement		=	null;
		try {
			insertPosSQLStatement	=	thisDataBase.prepareStatement(insertPosSQL);
			insertPosSQLStatement.clearParameters();
			this.fillInsertPos(insertPosSQLStatement, mandator, user, saveEntry, delete, posNumber);
			num =  insertPosSQLStatement.executeUpdate();
		} finally {
			if( insertPosSQLStatement != null )
				insertPosSQLStatement.close();
		}
		return num;
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
		PreparedStatement readPosSQLStatement	=	null;
		try {
			super.readOneEntry(thisDataBase, mandator, entry, thisEntry);

			if( thisEntry != null && thisEntry.getKeyCur() != null ) {
				readPosSQLStatement	=	thisDataBase.prepareStatement(readPosSQL);
				readPosSQLStatement.clearParameters();
				readPosSQLStatement.setInt(1, mandator);
				readPosSQLStatement.setString(2, entry);
				ResultSet resultPos	=	readPosSQLStatement.executeQuery();
				thisEntry = fillPosFromResultSet(resultPos, thisEntry);
				resultPos.close();
			}
		} catch( Exception nef ) {
			thisEntry	=	null;
		} finally {
			try {
				if( readPosSQLStatement != null )
					readPosSQLStatement.close();
			} catch (SQLException e) {
				// ignore
			}
		}
		return thisEntry;
	}

	/**
	 * <code>saveEntry</code> saves or inserts a entry to database
	 * 
	 * @param currentEntry
	 * 			entry that has to be saved
	 */
	public E saveEntry(E currentEntry) {
		if( this.getUser() !=	null ) {
			int mandator	=	this.getUser().getMandator();
			String user		=	this.getUser().getUser();
			String saveKeyString	=	currentEntry.getKeyCur();
			if( saveKeyString == null || "".equals(saveKeyString) )
				saveKeyString		=	currentEntry.getKeyNew();
			Connection thisDataBase =	null;
			PreparedStatement invalidatePosSQLStatement		=	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E dbEntry = (E) createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
			        thisDataBase 			=	lDataSource.getConnection();
					ic.close();

					dbEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, dbEntry );
					if((dbEntry != null) && (dbEntry.getKeyCur() == null))
						dbEntry	=	null;

					// Entry already exists in Database?
					if( !currentEntry.equals( dbEntry ) ) {
						invalidatePosSQLStatement		=	thisDataBase.prepareStatement(invalidatePosSQL);

						if( !currentEntry.equalsEntry(dbEntry) && (invalidateHeadSQL != null) ) {
							// Invalidate old entry
							invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(invalidateHeadSQL);
		        			invalidateHeadSQLStatement.clearParameters();
		        			invalidateHeadSQLStatement.setInt(1, mandator);
		        			invalidateHeadSQLStatement.setString(2, saveKeyString);
		        			invalidateHeadSQLStatement.executeUpdate();

							// Insert new entry
							this.insertEntry( thisDataBase, mandator, user, currentEntry, false);

							currentEntry.setKeyCur(saveKeyString);
						}
						// Positions
						// Take a look if position differ and invalidate old
			        	for(int i = 0;
			        		i < (((dbEntry == null) || (dbEntry.getKeyPos() == null)) ? 0 : dbEntry.getKeyPos().length);
			        		i++ ) {
			        		boolean isremoved	=	true;
			        		for( int j = 0;
			        			j < (currentEntry.getKeyPos() == null ?  0 : currentEntry.getKeyPos().length) && isremoved;
			        			j++ ) {
			        			if( dbEntry.getKeyPos()[i].equals(currentEntry.getKeyPos()[j]) )
			        				isremoved	=	false;
			        		}
			        		if( isremoved ) {
								invalidatePosSQLStatement.clearParameters();
								invalidatePosSQLStatement.setInt(1, mandator);
								invalidatePosSQLStatement.setString(2, saveKeyString);
								invalidatePosSQLStatement.setString(3, dbEntry.getKeyPos()[i]);
								invalidatePosSQLStatement.executeUpdate();
								this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
								// Invalidate old entry
			        		}
			        	}
						// Take a look if position differ and insert new
			        	for(int i = 0;
			        		i < (currentEntry.getKeyPos() == null ?  0 : currentEntry.getKeyPos().length);
			        		i++ ) {
			        		boolean isnew	=	true;
			            	for(int j = 0;
			            		j < (((dbEntry == null) || (dbEntry.getKeyPos() == null)) ? 0 : dbEntry.getKeyPos().length) && isnew;
			            		j++ ) {
			        			if( dbEntry.getKeyPos()[j].equals(currentEntry.getKeyPos()[i]) ) {
			        				isnew	=	false;
			        				// Entry already exists, look for changes
									if( !currentEntry.equalsPosition(dbEntry, i, j) ) {
										// Invalidate old entry
										invalidatePosSQLStatement.clearParameters();
										invalidatePosSQLStatement.setInt(1, mandator);
										invalidatePosSQLStatement.setString(2, saveKeyString);
										invalidatePosSQLStatement.setString(3, dbEntry.getKeyPos()[i]);
										invalidatePosSQLStatement.executeUpdate();
		
										this.insertPositionEntry(thisDataBase, mandator, user, currentEntry, false, i);
									}
			        			}
			            	}
			        		if( isnew ) {
			                	// Insert new position
								this.insertPositionEntry(thisDataBase, mandator, user, currentEntry, false, i);
			        		}
						}

						currentEntry.setKeyCur(saveKeyString);
						this.FillMinMax(thisDataBase, mandator, currentEntry);
						currentEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, currentEntry );
					}
				}
			} catch (Exception e) {
				currentEntry	=	null;
			} finally {
				try {
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
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
