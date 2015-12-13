/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.dbnavigationbar.shared.Constants;

import com.google.gwt.user.server.rpc.UnexpectedException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation. template for a simple database
 *
 * @param <E> Structure of the database
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadPosDateTemplateR<E extends DomainHeadPosDataBaseInt> extends AbstractDBTemplate<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -4140110717360730992L;

  /**
   * read a position.
   */
  private final String readPosSQL;
  /**
   * invalidate a position.
   */
  private final String invalidatePosSQL;
  /**
   * insert a position.
   */
  private final String insertPosSQL;

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   * @param pReadMinMaxSQL sql statement for min/max read
   * @param pReadNextSQL sql statement to read next key
   * @param pReadPrevSQL sql statement to read previous key
   * @param pReadHeadSQL sql statement to read head entry
   * @param pInvalidateHeadSQL sql statement to invalidate head entry
   * @param pReadPosSQL sql statement to read position entry
   * @param pInvalidatePosSQL sql statement to invalidate position entry
   */
  public AbstractDBHeadPosDateTemplateR(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pInsertPosSQL,

      final String pReadMinMaxSQL, final String pReadNextSQL, final String pReadPrevSQL, final String pReadHeadSQL,
      final String pInvalidateHeadSQL,

      final String pReadPosSQL, final String pInvalidatePosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pReadMinMaxSQL,
        pReadNextSQL, pReadPrevSQL, pReadHeadSQL, pInvalidateHeadSQL);

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(pLookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

        if (pReadPosSQL == null) {
          this.readPosSQL = null;
        } else {
          this.readPosSQL =
              pReadPosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }

        if (pInvalidatePosSQL == null) {
          this.invalidatePosSQL = null;
        } else {
          this.invalidatePosSQL =
              pInvalidatePosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }

        if (pInsertPosSQL == null) {
          this.insertPosSQL = null;
        } else {
          this.insertPosSQL =
              pInsertPosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
      }
      ic.close();
    } catch (final SQLException e) {
      throw new UnexpectedException(e.toString(), e);
    } catch (final NamingException e) {
      throw new UnexpectedException(e.toString(), e);
    }
  }

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pPosDataBaseTableName database table name (position)
   * @param pPosKeyfieldName key field of the database (position)
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   */
  public AbstractDBHeadPosDateTemplateR(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL) {
    this(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pInsertPosSQL,

    "SELECT MIN(" + pKeyFieldName + ") AS min, " //
        + "       MAX(" + pKeyFieldName + ") AS max " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "SELECT MIN(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " > ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "SELECT MAX(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " < ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "SELECT * " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "UPDATE " + pDataBaseTableName + " " //
        + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW()",

    "SELECT * " //
        + "FROM   " + pPosDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "UPDATE " + pPosDataBaseTableName + " " //
        + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + pPosKeyfieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW()");
  }

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pPosDataBaseTableName database table name (position)
   * @param pPosKeyfieldName key field of the database (position)
   * @param pInsertPosSQL sql statement to insert a new head entry (position)
   * @param pReadHeadSQL sql statement to read head entry
   * @param pReadPosSQL sql statement to read position entry
   */
  public AbstractDBHeadPosDateTemplateR(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL, final String pReadHeadSQL,
      final String pReadPosSQL) {
    this(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pInsertPosSQL,

    "SELECT MIN(" + pKeyFieldName + ") AS min, " //
        + "       MAX(" + pKeyFieldName + ") AS max " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "SELECT MIN(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " > ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    "SELECT MAX(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " < ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW()",

    pReadHeadSQL,

    "UPDATE " + pDataBaseTableName + " " //
        + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW()",

    pReadPosSQL,

    "UPDATE " + pPosDataBaseTableName + " " //
        + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? " //
        + "  AND  " + pPosKeyfieldName + " = ? " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() " //
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW()");
  }

  /**
   * <code>searchSQLSelect</code> setup a part of the sql statement to search for a user.
   *
   * @param pThisDataBase Database connection
   * @param pMinMax "MIN" or "MAX" used for PhoneNumber
   * @param pSearchField Field to search for
   * @param pSearchMethodeEntry compare method for search ("<", "<=", "=", ">", ">=" or "like")
   * @param pSearchFieldEntry value to search for
   * @param pDbKeyVGL compare method of phone number ("<", "<=", "=", ">", ">=" or "like")
   * @param pDbKey comparison number
   * @return SQL-String
   * @throws SQLException when error occurs
   */
  @Override
  protected final String searchSQLSelect(final Connection pThisDataBase, final String pMinMax, final String pSearchField,
      final String pSearchMethodeEntry, final String pSearchFieldEntry, final String pDbKeyVGL, final String pDbKey)
      throws SQLException {
    final int mandator = this.getUser().getMandator();
    final DataBaseDepending myDataBaseDepending = new DataBaseDepending(pThisDataBase.getMetaData().getDatabaseProductName());

    final int sqlLength = 85;
    final StringBuilder sqlString = new StringBuilder(sqlLength);
    sqlString.append( //
        "SELECT " + pMinMax + "(" + this.getKeyFieldName() + ") AS dbnumber " //
            + "FROM   " + this.getDataBaseTableName() + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " " //
            + " AND   " + this.getKeyFieldName() + " " + pDbKeyVGL + " " + StringToSQL.convertString(pDbKey, //
                pThisDataBase.getMetaData().getDatabaseProductName()) + " " //
            + " AND   " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
            + " AND   " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow() + " " //
            + " AND   ");

    if ("=".equals(pSearchMethodeEntry)) {
      sqlString.append(StringToSQL.searchString(pSearchField, pSearchFieldEntry, pThisDataBase.getMetaData()
          .getDatabaseProductName()));
    } else if ("like".equals(pSearchMethodeEntry)) {
      sqlString.append(StringToSQL.searchString(pSearchField, "*" + pSearchFieldEntry + "*", pThisDataBase.getMetaData()
          .getDatabaseProductName()));
    } else {
      sqlString.append(pSearchField + " " + pSearchMethodeEntry + " "
          + StringToSQL.convertString(pSearchFieldEntry, pThisDataBase.getMetaData().getDatabaseProductName()));
    }
    return sqlString.toString();
  }

  /**
   * <code>fillInsertPos</code> fills the parameters of the insert prepared statement.
   *
   * @param pInsertPosSQLStatement prepared statement for inserts
   * @param pMandator mandator number
   * @param pUser user name
   * @param pSaveEntry entry to save
   * @param pDelete delete or not delete
   * @param pPosNumber number of the position to save
   * @throws SQLException when error occurs
   */
  protected abstract void fillInsertPos(final PreparedStatement pInsertPosSQLStatement, final int pMandator,
      final String pUser, final E pSaveEntry, final boolean pDelete, final int pPosNumber) throws SQLException;

  /**
   * <code>fillPosFromResultSet</code> set the fields in thisEntry from the given resultSet.
   *
   * @param pResultPos ResultSet to read Entries from
   * @param pThisEntry Entry to fill
   * @return filled Entry
   * @throws SQLException when error occurs
   */
  protected abstract E fillPosFromResultSet(ResultSet pResultPos, E pThisEntry) throws SQLException;

  /**
   * <code>deleteEntry</code> deletes one entry from database.
   *
   * @param pCurrentEntry entry to delete
   * @return entry to display after deletion
   */
  @Override
  public final E deleteEntry(final String pCurrentEntry) {
    E resultValue = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser != null) {
      final int mandator = thisUser.getMandator();
      final String user = thisUser.getUser();

      try {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.getLookUpDataBase());
        try (final Connection thisDataBase = lDataSource.getConnection()) {

          if (this.allowedToChange()) {
            final E dbEntry = this.readEntry(pCurrentEntry);
            // invalidate head number
            if (this.getInvalidateHeadSQL() != null) {
              try (final PreparedStatement invalidateHeadSQLStatement =
                  thisDataBase.prepareStatement(this.getInvalidateHeadSQL())) {
                invalidateHeadSQLStatement.clearParameters();
                invalidateHeadSQLStatement.setInt(1, mandator);
                invalidateHeadSQLStatement.setString(2, pCurrentEntry);
                invalidateHeadSQLStatement.executeUpdate();
                this.insertEntry(thisDataBase, mandator, user, dbEntry, true);
              }
            }

            try (PreparedStatement invalidatePosSQLStatement = thisDataBase.prepareStatement(this.invalidatePosSQL)) {
              int numPos = 0;
              if (dbEntry.getKeyPos() != null) {
                numPos = dbEntry.getKeyPos().length;
              }
              for (int i = 0; i < numPos; i++) {
                int sqlPos = 1;
                invalidatePosSQLStatement.clearParameters();
                invalidatePosSQLStatement.setInt(sqlPos++, mandator);
                invalidatePosSQLStatement.setString(sqlPos++, pCurrentEntry);
                invalidatePosSQLStatement.setString(sqlPos++, dbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();
                this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
              }
            }
          }

          resultValue = this.readNextEntry(pCurrentEntry);
        }
        ic.close();
      } catch (final SQLException e) {
        resultValue = null;
      } catch (final NamingException e) {
        resultValue = null;
      }
    }
    return resultValue;
  }

  /**
   * <code>insertPositionEntry</code> inserts a position into the database.
   *
   * @param pThisDataBase database connection
   * @param pMandator mandator number
   * @param pUser user name
   * @param pSaveEntry entry to save
   * @param pDelete delete old entries
   * @param pPosNumber position number
   * @return effected database entries (should always be 1)
   * @throws SQLException when error occurs
   */
  protected final int insertPositionEntry(final Connection pThisDataBase, final int pMandator, final String pUser,
      final E pSaveEntry, final boolean pDelete, final int pPosNumber) throws SQLException {
    int num = -1;
    try (final PreparedStatement insertPosSQLStatement = pThisDataBase.prepareStatement(this.insertPosSQL)) {
      insertPosSQLStatement.clearParameters();
      this.fillInsertPos(insertPosSQLStatement, pMandator, pUser, pSaveEntry, pDelete, pPosNumber);
      num = insertPosSQLStatement.executeUpdate();
    }
    return num;
  }

  /**
   * <code>saveEntry</code> saves or inserts a entry to database.
   *
   * @param pCurrentEntry entry that has to be saved
   * @param pDbEntry entry from database to compare
   * @param pThisDataBase database connection
   * @param pMandator mandator number
   * @param pUser name of the user
   * @param pSaveKeyString key of the entry to save
   * @throws SQLException if something's going wrong
   */
  @Override
  protected final void saveEntry(final E pCurrentEntry, final E pDbEntry, final Connection pThisDataBase, final int pMandator,
      final String pUser, final String pSaveKeyString) throws SQLException {
    // Entry already exists in Database?
    if (!pCurrentEntry.equals(pDbEntry)) {

      try (final PreparedStatement invalidatePosSQLStatement = pThisDataBase.prepareStatement(this.invalidatePosSQL);
          final PreparedStatement invalidateHeadSQLStatement = pThisDataBase.prepareStatement(this.getInvalidateHeadSQL())) {

        if (!pCurrentEntry.equalsEntry(pDbEntry) && (this.getInvalidateHeadSQL() != null)) {
          // Invalidate old entry
          invalidateHeadSQLStatement.clearParameters();
          invalidateHeadSQLStatement.setInt(1, pMandator);
          invalidateHeadSQLStatement.setString(2, pSaveKeyString);
          invalidateHeadSQLStatement.executeUpdate();

          // Insert new entry
          this.insertEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false);

          pCurrentEntry.setKeyCur(pSaveKeyString);
        }
        // Positions
        // Take a look if position differ and invalidate old
        int numPos = 0;
        if (pCurrentEntry.getKeyPos() != null) {
          numPos = pCurrentEntry.getKeyPos().length;
        }
        int numDBPos = 0;
        if (pDbEntry != null && pDbEntry.getKeyPos() != null) {
          numDBPos = pDbEntry.getKeyPos().length;
        }

        for (int i = 0; i < numDBPos; i++) {
          boolean isremoved = true;
          for (int j = 0; j < numPos && isremoved; j++) {
            if (pDbEntry.getKeyPos()[i].equals(pCurrentEntry.getKeyPos()[j])) {
              isremoved = false;
            }
          }
          if (isremoved) {
            int invPos = 1;
            invalidatePosSQLStatement.clearParameters();
            invalidatePosSQLStatement.setInt(invPos++, pMandator);
            invalidatePosSQLStatement.setString(invPos++, pSaveKeyString);
            invalidatePosSQLStatement.setString(invPos++, pDbEntry.getKeyPos()[i]);
            invalidatePosSQLStatement.executeUpdate();
            this.insertPositionEntry(pThisDataBase, pMandator, pUser, pDbEntry, true, i);
            // Invalidate old entry
          }
        }
        // Take a look if position differ and insert new
        for (int i = 0; i < numPos; i++) {
          boolean isnew = true;
          for (int j = 0; j < numDBPos && isnew; j++) {
            if (pDbEntry.getKeyPos()[j].equals(pCurrentEntry.getKeyPos()[i])) {
              isnew = false;
              // Entry already exists, look for changes
              if (!pCurrentEntry.equalsPosition(pDbEntry, i, j)) {
                int invPos = 1;
                // Invalidate old entry
                invalidatePosSQLStatement.clearParameters();
                invalidatePosSQLStatement.setInt(invPos++, pMandator);
                invalidatePosSQLStatement.setString(invPos++, pSaveKeyString);
                invalidatePosSQLStatement.setString(invPos++, pDbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();

                this.insertPositionEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false, i);
              }
            }
          }
          if (isnew) {
            // Insert new position
            this.insertPositionEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false, i);
          }
        }
      }
    }
  }

  /**
   * get read pos sql string.
   *
   * @return readPosSQL
   */
  public final String getReadPosSQL() {
    return this.readPosSQL;
  }
}
