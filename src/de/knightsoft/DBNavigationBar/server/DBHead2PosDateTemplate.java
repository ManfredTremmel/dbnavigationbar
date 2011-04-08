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

	protected String readPos2SQL			= null;
    protected String invalidatePos2SQL		= null;
    protected String insertPos2SQL			= null;

    /**
     * Constructor, set up database connection
     * 
     * @param LookUpDataBase
 	 * @param SessionUser,
     * @param DataBaseTableName
     * @param KeyfieldName
     * @param insertHeadSQL
     * @param PosDataBaseTableName
     * @param PosKeyfieldName
     * @param PosinsertHeadSQL
     * @param Pos2DataBaseTableName
     * @param Pos2KeyfieldName
     * @param Pos2insertHeadSQL
     * @throws UnexpectedException
     */
    public DBHead2PosDateTemplate(
       		String LookUpDataBase,
       		String SessionUser,
    		String DataBaseTableName,
    		String KeyfieldName,
    		String insertHeadSQL,
    		String PosDataBaseTableName,
    		String PosKeyfieldName,
    		String PosinsertHeadSQL,
    		String Pos2DataBaseTableName,
    		String Pos2KeyfieldName,
    		String Pos2insertHeadSQL
    		) throws UnexpectedException {
		super(
	       		LookUpDataBase,
	       		SessionUser,
				DataBaseTableName,
				KeyfieldName,
				insertHeadSQL,

				PosDataBaseTableName,
				PosKeyfieldName,
				PosinsertHeadSQL
		);

		Connection ThisDataBase 	=	null;
		try {
			// connect to database
			InitialContext ic		=	new InitialContext();
	        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
	        ThisDataBase =	lDataSource.getConnection();
			ic.close();

			DataBaseDepending MyDataBaseDepending = new DataBaseDepending(ThisDataBase.getMetaData().getDatabaseProductName());

			this.readPos2SQL		=	
          		  "SELECT * "
    		  	+ "FROM   " + Pos2DataBaseTableName + " "
  				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
  				+ "  AND  " + KeyfieldName + " = ? "
  				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
  				+ "  AND  " + Constants.DBFieldGlobalDate_to + " > " + MyDataBaseDepending.getSQLTimeNow() + ";";


    		this.invalidatePos2SQL		=
				  "UPDATE " + Pos2DataBaseTableName + " "
				+ "SET    " + Constants.DBFieldGlobalDate_to + "=" + MyDataBaseDepending.getSQLTimeOutdate() + " "
				+ "WHERE  " + Constants.DBFieldGlobalMandator + " = ? "
	    		+ "  AND  " + KeyfieldName + " = ? "
	    		+ "  AND  " + Pos2KeyfieldName + " = ? "
				+ "  AND  " + Constants.DBFieldGlobalDate_from + " <= " + MyDataBaseDepending.getSQLTimeNow() + " "
				+ "  AND  " + Constants.DBFieldGlobalDate_to + "   > " + MyDataBaseDepending.getSQLTimeNow() + ";";

    		this.insertPos2SQL		=	Pos2insertHeadSQL;

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
	 * <code>fillInsertPos</code> fills the parameters of the insert prepared statement
	 * @param insertHeadSQLStatement
	 * @param Mandator
	 * @param User
	 * @param SaveEntry
	 * @param Delete
	 * @param PosNumber
	 */
	protected abstract void fillInsertPos2(
			PreparedStatement insertPos2SQLStatement,
			int Mandator,
			String User,
			E SaveEntry,
			boolean Delete,
			int Pos2Number) throws SQLException;

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
	 * @param CurrentEntry
	 */
	public E deleteEntry(String CurrentEntry) {
		E	ResultValue	=	null;				
		if( this.getUser() !=	null ) {
			int Mandator	=	this.getUser().getMandator();
			String User		=	this.getUser().getUser();

			Connection ThisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;
			PreparedStatement invalidatePosSQLStatement		=	null;
			PreparedStatement invalidatePos2SQLStatement	=	null;

			try {
				// connect to database
				InitialContext ic		=	new InitialContext();
		        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
		        ThisDataBase =	lDataSource.getConnection();
				ic.close();

				if( allowedToChange() ) {
					E DBEntry	=	this.readEntry(CurrentEntry);
					// invalidate head number
					if(invalidateHeadSQL != null) {
						invalidateHeadSQLStatement	=	ThisDataBase.prepareStatement(invalidateHeadSQL);
	        			invalidateHeadSQLStatement.clearParameters();
	        			invalidateHeadSQLStatement.setInt(1, Mandator);
	        			invalidateHeadSQLStatement.setString(2, CurrentEntry);
	        			invalidateHeadSQLStatement.executeUpdate();
		        		this.insertEntry( ThisDataBase, Mandator, User, DBEntry, true);
					}

					invalidatePosSQLStatement		=	ThisDataBase.prepareStatement(invalidatePosSQL);
					for(int i = 0; i < (DBEntry.getKeyPos() == null ? 0 : DBEntry.getKeyPos().length); i++ ) {
						invalidatePosSQLStatement.clearParameters();
						invalidatePosSQLStatement.setInt(1, Mandator);
						invalidatePosSQLStatement.setString(2, CurrentEntry);
						invalidatePosSQLStatement.setString(3, DBEntry.getKeyPos()[i]);
						invalidatePosSQLStatement.executeUpdate();
						this.insertPositionEntry(ThisDataBase, Mandator, User, DBEntry, true, i);
					}

					invalidatePos2SQLStatement		=	ThisDataBase.prepareStatement(invalidatePos2SQL);
					for(int i = 0; i < (DBEntry.getKeyPos2() == null ? 0 : DBEntry.getKeyPos2().length); i++ ) {
						invalidatePos2SQLStatement.clearParameters();
						invalidatePos2SQLStatement.setInt(1, Mandator);
						invalidatePos2SQLStatement.setString(2, CurrentEntry);
						invalidatePos2SQLStatement.setString(3, DBEntry.getKeyPos2()[i]);
						invalidatePos2SQLStatement.executeUpdate();
						this.insertPosition2Entry(ThisDataBase, Mandator, User, DBEntry, true, i);
					}
				}

				ResultValue	= readNextEntry(CurrentEntry);
		    	
	        } catch( Exception e ) {
	        	ResultValue	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null )
						invalidateHeadSQLStatement.close();
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidatePos2SQLStatement != null )
						invalidatePos2SQLStatement.close();
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
	 * <code>insertPosition2Entry</code> inserts a position into the database
	 * @param ThisDataBase 
	 * @param Mandator
	 * @param User
	 * @param SaveEntry
	 * @param Delete
	 * @param PosNumber
	 * @return effected database entries (should always be 1)
	 * @throws SQLException
	 */
	private int insertPosition2Entry( Connection ThisDataBase, int Mandator, String User, E SaveEntry,
			boolean Delete, int PosNumber
			) throws SQLException {
		int num = -1;
		PreparedStatement insertPos2SQLStatement		=	null;
		try {
			insertPos2SQLStatement		=	ThisDataBase.prepareStatement(insertPos2SQL);
			insertPos2SQLStatement.clearParameters();
			this.fillInsertPos2(insertPos2SQLStatement, Mandator, User, SaveEntry, Delete, PosNumber);
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
		PreparedStatement readPos2SQLStatement	=	null;
		try {
			super.readOneEntry(ThisDataBase, Mandator, Entry, thisEntry);

			if( thisEntry != null && thisEntry.getKeyCur() != null ) {
				readPos2SQLStatement	=	ThisDataBase.prepareStatement(readPos2SQL);
				readPos2SQLStatement.clearParameters();
				readPos2SQLStatement.setInt(1, Mandator);
				readPos2SQLStatement.setString(2, Entry);
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
	 * @param CurrentEntry
	 * 			entry that has to be saved
	 */
	public E saveEntry(E CurrentEntry) {
		if( this.getUser() !=	null ) {
			int Mandator	=	this.getUser().getMandator();
			String User		=	this.getUser().getUser();
			String SaveKeyString	=	CurrentEntry.getKeyCur();
			if( SaveKeyString		==	null || "".equals(SaveKeyString) )
				SaveKeyString		=	CurrentEntry.getKeyNew();
			Connection ThisDataBase =	null;
			PreparedStatement invalidateHeadSQLStatement	=	null;
			PreparedStatement invalidatePosSQLStatement		=	null;
			PreparedStatement invalidatePos2SQLStatement	=	null;

			try {
				if( allowedToChange() ) {
					E DBEntry = createInstance();
					// connect to database
					InitialContext ic		=	new InitialContext();
			        DataSource lDataSource	=	(DataSource)ic.lookup(LookUpDataBase);
			        ThisDataBase 			=	lDataSource.getConnection();
					ic.close();

					DBEntry	=	readOneEntry( ThisDataBase, Mandator, SaveKeyString, DBEntry );
					if((DBEntry != null) && (DBEntry.getKeyCur() == null))
						DBEntry	=	null;

					// Entry already exists in Database?
					if( !CurrentEntry.equals( DBEntry ) ) {
						invalidatePosSQLStatement		=	ThisDataBase.prepareStatement(invalidatePosSQL);
						invalidatePos2SQLStatement	=	ThisDataBase.prepareStatement(invalidatePos2SQL);

						if( !CurrentEntry.equalsEntry(DBEntry) && (invalidateHeadSQL != null) ) {
							// Invalidate old entry
							invalidateHeadSQLStatement	=	ThisDataBase.prepareStatement(invalidateHeadSQL);
		        			invalidateHeadSQLStatement.clearParameters();
		        			invalidateHeadSQLStatement.setInt(1, Mandator);
		        			invalidateHeadSQLStatement.setString(2, SaveKeyString);
		        			invalidateHeadSQLStatement.executeUpdate();

							// Insert new entry
							this.insertEntry( ThisDataBase, Mandator, User, CurrentEntry, false);

							CurrentEntry.setKeyCur(SaveKeyString);
						}
						// Positions
						// Take a look if position differ and invalidate old
			        	for(int i = 0;
			        		i < (((DBEntry == null) || (DBEntry.getKeyPos() == null)) ? 0 : DBEntry.getKeyPos().length);
			        		i++ ) {
			        		boolean isremoved	=	true;
			        		for( int j = 0;
			        			j < (CurrentEntry.getKeyPos() == null ?  0 : CurrentEntry.getKeyPos().length) && isremoved;
			        			j++ ) {
			        			if( DBEntry.getKeyPos()[i].equals(CurrentEntry.getKeyPos()[j]) )
			        				isremoved	=	false;
			        		}
			        		if( isremoved ) {
								invalidatePosSQLStatement.clearParameters();
								invalidatePosSQLStatement.setInt(1, Mandator);
								invalidatePosSQLStatement.setString(2, SaveKeyString);
								invalidatePosSQLStatement.setString(3, DBEntry.getKeyPos()[i]);
								invalidatePosSQLStatement.executeUpdate();
								this.insertPositionEntry(ThisDataBase, Mandator, User, DBEntry, true, i);
								// Invalidate old entry
			        		}
			        	}
						// Take a look if position differ and insert new
			        	for(int i = 0;
			        		i < (CurrentEntry.getKeyPos() == null ?  0 : CurrentEntry.getKeyPos().length);
			        		i++ ) {
			        		boolean isnew	=	true;
			            	for(int j = 0;
			            		j < (((DBEntry == null) || (DBEntry.getKeyPos() == null)) ? 0 : DBEntry.getKeyPos().length) && isnew;
			            		j++ ) {
			        			if( DBEntry.getKeyPos()[j].equals(CurrentEntry.getKeyPos()[i]) ) {
			        				isnew	=	false;
			        				// Entry already exists, look for changes
									if( !CurrentEntry.equalsPosition(DBEntry, i, j) ) {
										// Invalidate old entry
										invalidatePosSQLStatement.clearParameters();
										invalidatePosSQLStatement.setInt(1, Mandator);
										invalidatePosSQLStatement.setString(2, SaveKeyString);
										invalidatePosSQLStatement.setString(3, DBEntry.getKeyPos()[i]);
										invalidatePosSQLStatement.executeUpdate();
		
										this.insertPositionEntry(ThisDataBase, Mandator, User, CurrentEntry, false, i);
									}
			        			}
			            	}
			        		if( isnew ) {
			                	// Insert new position
								this.insertPositionEntry(ThisDataBase, Mandator, User, CurrentEntry, false, i);
			        		}
						}

						// Positions2
						// Take a look if position differ and invalidate old
			        	for(int i = 0;
			        		i < (((DBEntry == null) || (DBEntry.getKeyPos2() == null)) ? 0 : DBEntry.getKeyPos2().length);
			        		i++ ) {
			        		boolean isremoved	=	true;
			        		for( int j = 0;
			        			j < (CurrentEntry.getKeyPos2() == null ?  0 : CurrentEntry.getKeyPos2().length) && isremoved;
			        			j++ ) {
			        			if( DBEntry.getKeyPos2()[i].equals(CurrentEntry.getKeyPos2()[j]) )
			        				isremoved	=	false;
			        		}
			        		if( isremoved ) {
								invalidatePos2SQLStatement.clearParameters();
								invalidatePos2SQLStatement.setInt(1, Mandator);
								invalidatePos2SQLStatement.setString(2, SaveKeyString);
								invalidatePos2SQLStatement.setString(3, DBEntry.getKeyPos2()[i]);
								invalidatePos2SQLStatement.executeUpdate();
								this.insertPosition2Entry(ThisDataBase, Mandator, User, DBEntry, true, i);
								// Invalidate old entry
			        		}
			        	}
						// Take a look if position differ and insert new
			        	for(int i = 0;
			        		i < (CurrentEntry.getKeyPos2() == null ?  0 : CurrentEntry.getKeyPos2().length);
			        		i++ ) {
			        		boolean isnew	=	true;
			            	for(int j = 0;
			            		j < (((DBEntry == null) || (DBEntry.getKeyPos2() == null)) ? 0 : DBEntry.getKeyPos2().length) && isnew;
			            		j++ ) {
			        			if( DBEntry.getKeyPos2()[j].equals(CurrentEntry.getKeyPos2()[i]) ) {
			        				isnew	=	false;
			        				// Entry already exists, look for changes
									if( !CurrentEntry.equalsPosition2(DBEntry, i, j) ) {
										// Invalidate old entry
										invalidatePos2SQLStatement.clearParameters();
										invalidatePos2SQLStatement.setInt(1, Mandator);
										invalidatePos2SQLStatement.setString(2, SaveKeyString);
										invalidatePos2SQLStatement.setString(3, DBEntry.getKeyPos2()[i]);
										invalidatePos2SQLStatement.executeUpdate();
		
										this.insertPosition2Entry(ThisDataBase, Mandator, User, CurrentEntry, false, i);
									}
			        			}
			            	}
			        		if( isnew ) {
			                	// Insert new position
								this.insertPosition2Entry(ThisDataBase, Mandator, User, CurrentEntry, false, i);
			        		}
						}

						CurrentEntry.setKeyCur(SaveKeyString);
						this.FillMinMax(ThisDataBase, Mandator, CurrentEntry);
						CurrentEntry	=	readOneEntry( ThisDataBase, Mandator, SaveKeyString, CurrentEntry );
					}
				}

			} catch (Exception e) {
				CurrentEntry	=	null;
			} finally {
				try {
					if( invalidateHeadSQLStatement != null)
						invalidateHeadSQLStatement.close();
					if( invalidatePosSQLStatement != null )
						invalidatePosSQLStatement.close();
					if( invalidatePos2SQLStatement != null )
						invalidatePos2SQLStatement.close();
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
