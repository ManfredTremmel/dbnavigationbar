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

import de.knightsoft.DBNavigationBar.client.domain.DomainHead2PosDataBase;
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
public abstract class DBHead2PosDateTemplate<E extends DomainHead2PosDataBase>
	extends DBHeadPosDateTemplate<E> {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 3633734786925668260L;

	protected final String readPos2SQL;
    protected final String invalidatePos2SQL;
    protected final String insertPos2SQL;

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
     * @param posinsertHeadSQL
     * @param pos2DataBaseTableName
     * @param pos2KeyfieldName
     * @param pos2insertHeadSQL
     * @throws UnexpectedException
     */
    public DBHead2PosDateTemplate(
    		Class<E> type,
       		String lookUpDataBase,
       		String sessionUser,
    		String dataBaseTableName,
    		String keyFieldName,
    		String insertHeadSQL,
    		String posDataBaseTableName,
    		String posKeyfieldName,
    		String posinsertHeadSQL,
    		String pos2DataBaseTableName,
    		String pos2KeyfieldName,
    		String pos2insertHeadSQL
    		) throws UnexpectedException {
		super(
				type,
	       		lookUpDataBase,
	       		sessionUser,
				dataBaseTableName,
				keyFieldName,
				insertHeadSQL,

				posDataBaseTableName,
				posKeyfieldName,
				posinsertHeadSQL
		);

		Connection thisDataBase 	=	null;
		try {
			// connect to database
			InitialContext ic		=	new InitialContext();
	        DataSource lDataSource	=	(DataSource)ic.lookup(lookUpDataBase);
	        thisDataBase =	lDataSource.getConnection();
			ic.close();

			DataBaseDepending myDataBaseDepending = new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

			this.readPos2SQL		=	
          		  "SELECT * "
    		  	+ "FROM   " + pos2DataBaseTableName + " "
  				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
  				+ "  AND  " + keyFieldName + " = ? "
  				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
  				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + myDataBaseDepending.getSQLTimeNow() + ";";


    		this.invalidatePos2SQL		=
				  "UPDATE " + pos2DataBaseTableName + " "
				+ "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "=" + myDataBaseDepending.getSQLTimeOutdate() + " "
				+ "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
	    		+ "  AND  " + keyFieldName + " = ? "
	    		+ "  AND  " + pos2KeyfieldName + " = ? "
				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
				+ "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow() + ";";

    		this.insertPos2SQL		=	pos2insertHeadSQL;

		} catch( Exception e ) {
			throw new UnexpectedException( e.toString(), e.getCause() );
		} finally {
			try {
				if( thisDataBase != null )
					thisDataBase.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}



	/**
	 * <code>fillInsertPos</code> fills the parameters of the insert prepared statement
	 * @param insertPos2SQLStatement
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 * @param pos2Number
	 */
	protected abstract void fillInsertPos2(
			PreparedStatement insertPos2SQLStatement,
			int mandator,
			String user,
			E saveEntry,
			boolean delete,
			int pos2Number) throws SQLException;

	/**
	 * <code>fillPosFromResultSet</code> set the fields in thisEntry from
	 * the given resultSet
	 * @param resultPos2
	 * 			ResultSet to read Entries from
	 * @param thisEntry
	 * 			Entry to fill
	 * @return filled Entry
	 */
	protected abstract E fillPos2FromResultSet(ResultSet resultPos2, E thisEntry) throws Exception;


	/**
	 * <code>deleteEntry</code> deletes one entry from database
	 * 
	 * @param currentEntry
	 */
	@Override
	public E deleteEntry(String currentEntry) {
		E	resultValue	=	null;				
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			String user		=	thisUser.getUser();

			Connection thisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;
			PreparedStatement invalidatePosSQLStatement		=	null;
			PreparedStatement invalidatePos2SQLStatement	=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
		        thisDataBase =	lDataSource.getConnection();
				ic.close();

				if( allowedToChange() ) {
					E dbEntry	=	this.readEntry(currentEntry);
					// invalidate head number
					if(this.invalidateHeadSQL != null) {
						invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(this.invalidateHeadSQL);
	        			invalidateHeadSQLStatement.clearParameters();
	        			invalidateHeadSQLStatement.setInt(1, mandator);
	        			invalidateHeadSQLStatement.setString(2, currentEntry);
	        			invalidateHeadSQLStatement.executeUpdate();
		        		this.insertEntry( thisDataBase, mandator, user, dbEntry, true);
					}

					invalidatePosSQLStatement		=	thisDataBase.prepareStatement(this.invalidatePosSQL);
					for(int i = 0; i < (dbEntry.getKeyPos() == null ? 0 : dbEntry.getKeyPos().length); i++ ) {
						invalidatePosSQLStatement.clearParameters();
						invalidatePosSQLStatement.setInt(1, mandator);
						invalidatePosSQLStatement.setString(2, currentEntry);
						invalidatePosSQLStatement.setString(3, dbEntry.getKeyPos()[i]);
						invalidatePosSQLStatement.executeUpdate();
						this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
					}

					invalidatePos2SQLStatement		=	thisDataBase.prepareStatement(this.invalidatePos2SQL);
					for(int i = 0; i < (dbEntry.getKeyPos2() == null ? 0 : dbEntry.getKeyPos2().length); i++ ) {
						invalidatePos2SQLStatement.clearParameters();
						invalidatePos2SQLStatement.setInt(1, mandator);
						invalidatePos2SQLStatement.setString(2, currentEntry);
						invalidatePos2SQLStatement.setString(3, dbEntry.getKeyPos2()[i]);
						invalidatePos2SQLStatement.executeUpdate();
						this.insertPosition2Entry(thisDataBase, mandator, user, dbEntry, true, i);
					}
				}

				resultValue	= readNextEntry(currentEntry);
		    	
	        } catch( Exception e ) {
	        	resultValue	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidatePos2SQLStatement != null )
						invalidatePos2SQLStatement.close();
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
	 * <code>insertPosition2Entry</code> inserts a position into the database
	 * @param thisDataBase 
	 * @param mandator
	 * @param user
	 * @param saveEntry
	 * @param delete
	 * @param posNumber
	 * @return effected database entries (should always be 1)
	 * @throws SQLException
	 */
	private int insertPosition2Entry( Connection thisDataBase, int mandator, String user, E saveEntry,
			boolean delete, int posNumber
			) throws SQLException {
		int num = -1;
		PreparedStatement insertPos2SQLStatement		=	null;
		try {
			insertPos2SQLStatement		=	thisDataBase.prepareStatement(this.insertPos2SQL);
			insertPos2SQLStatement.clearParameters();
			this.fillInsertPos2(insertPos2SQLStatement, mandator, user, saveEntry, delete, posNumber);
			num =  insertPos2SQLStatement.executeUpdate();
		} finally {
			if( insertPos2SQLStatement != null )
				insertPos2SQLStatement.close();
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
	@Override
	protected E readOneEntry(Connection thisDataBase, int mandator, String entry, E thisEntry) {
		PreparedStatement readPos2SQLStatement	=	null;
		try {
			super.readOneEntry(thisDataBase, mandator, entry, thisEntry);

			if( thisEntry != null && thisEntry.getKeyCur() != null ) {
				readPos2SQLStatement	=	thisDataBase.prepareStatement(this.readPos2SQL);
				readPos2SQLStatement.clearParameters();
				readPos2SQLStatement.setInt(1, mandator);
				readPos2SQLStatement.setString(2, entry);
				ResultSet resultPos2	=	readPos2SQLStatement.executeQuery();
				thisEntry = fillPos2FromResultSet(resultPos2, thisEntry);
				resultPos2.close();
			}
		} catch( Exception nef ) {
			thisEntry	=	null;
		} finally {
			try {
				if( readPos2SQLStatement != null )
					readPos2SQLStatement.close();
			} catch (SQLException e) {
				// ignore;
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
	@Override
	public E saveEntry(E currentEntry) {
		DomainUser thisUser	=	this.getUser();	
		if( thisUser !=	null ) {
			int mandator	=	thisUser.getMandator();
			String user		=	thisUser.getUser();
			String saveKeyString	=	currentEntry.getKeyCur();
			if( saveKeyString		==	null || "".equals(saveKeyString) )
				saveKeyString		=	currentEntry.getKeyNew();
			Connection thisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;
			PreparedStatement invalidatePosSQLStatement		=	null;
			PreparedStatement invalidatePos2SQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E dbEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(this.lookUpDataBase);
			        thisDataBase 			=	lDataSource.getConnection();
					ic.close();

					dbEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, dbEntry );
					if((dbEntry != null) && (dbEntry.getKeyCur() == null))
						dbEntry	=	null;

					// Entry already exists in Database?
					if( !currentEntry.equals( dbEntry ) ) {
						invalidatePosSQLStatement	=	thisDataBase.prepareStatement(this.invalidatePosSQL);
						invalidatePos2SQLStatement	=	thisDataBase.prepareStatement(this.invalidatePos2SQL);

						if( !currentEntry.equalsEntry(dbEntry) && (this.invalidateHeadSQL != null) ) {
							// Invalidate old entry
							invalidateHeadSQLStatement	=	thisDataBase.prepareStatement(this.invalidateHeadSQL);
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

						// Positions2
						// Take a look if position differ and invalidate old
			        	for(int i = 0;
			        		i < (((dbEntry == null) || (dbEntry.getKeyPos2() == null)) ? 0 : dbEntry.getKeyPos2().length);
			        		i++ ) {
			        		boolean isremoved	=	true;
			        		for( int j = 0;
			        			j < (currentEntry.getKeyPos2() == null ?  0 : currentEntry.getKeyPos2().length) && isremoved;
			        			j++ ) {
			        			if( dbEntry.getKeyPos2()[i].equals(currentEntry.getKeyPos2()[j]) )
			        				isremoved	=	false;
			        		}
			        		if( isremoved ) {
								invalidatePos2SQLStatement.clearParameters();
								invalidatePos2SQLStatement.setInt(1, mandator);
								invalidatePos2SQLStatement.setString(2, saveKeyString);
								invalidatePos2SQLStatement.setString(3, dbEntry.getKeyPos2()[i]);
								invalidatePos2SQLStatement.executeUpdate();
								this.insertPosition2Entry(thisDataBase, mandator, user, dbEntry, true, i);
								// Invalidate old entry
			        		}
			        	}
						// Take a look if position differ and insert new
			        	for(int i = 0;
			        		i < (currentEntry.getKeyPos2() == null ?  0 : currentEntry.getKeyPos2().length);
			        		i++ ) {
			        		boolean isnew	=	true;
			            	for(int j = 0;
			            		j < (((dbEntry == null) || (dbEntry.getKeyPos2() == null)) ? 0 : dbEntry.getKeyPos2().length) && isnew;
			            		j++ ) {
			        			if( dbEntry.getKeyPos2()[j].equals(currentEntry.getKeyPos2()[i]) ) {
			        				isnew	=	false;
			        				// Entry already exists, look for changes
									if( !currentEntry.equalsPosition2(dbEntry, i, j) ) {
										// Invalidate old entry
										invalidatePos2SQLStatement.clearParameters();
										invalidatePos2SQLStatement.setInt(1, mandator);
										invalidatePos2SQLStatement.setString(2, saveKeyString);
										invalidatePos2SQLStatement.setString(3, dbEntry.getKeyPos2()[i]);
										invalidatePos2SQLStatement.executeUpdate();
		
										this.insertPosition2Entry(thisDataBase, mandator, user, currentEntry, false, i);
									}
			        			}
			            	}
			        		if( isnew ) {
			                	// Insert new position
								this.insertPosition2Entry(thisDataBase, mandator, user, currentEntry, false, i);
			        		}
						}

						currentEntry.setKeyCur(saveKeyString);
						this.fillMinMax(thisDataBase, mandator, currentEntry);
						currentEntry	=	readOneEntry( thisDataBase, mandator, saveKeyString, currentEntry );
					}
				}

			} catch (Exception e) {
				currentEntry	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null)
						invalidateHeadSQLStatement.close();
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidatePos2SQLStatement != null )
						invalidatePos2SQLStatement.close();
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
