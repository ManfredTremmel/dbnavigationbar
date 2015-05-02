/**
 * This file is part of DBNavigation.
 *
 * RiPhone is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RiPhone. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.domain.DomainHead2PosDataBaseInt;
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
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation template for a simple database.
 *
 * @param <E> Structure of the database
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHead2PosDateTemplate<E extends DomainHead2PosDataBaseInt> extends AbstractDBTemplate<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 3633734786925668260L;

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
   * read a second position.
   */
  private final String readPos2SQL;
  /**
   * invalidate a second position.
   */
  private final String invalidatePos2SQL;
  /**
   * insert a second position.
   */
  private final String insertPos2SQL;

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
   * @param pPos2DataBaseTableName database table name (second position)
   * @param pPos2KeyfieldName key field of the database (second position)
   * @param pPos2insertHeadSQL sql statement to insert a new head entry (second position)
   */
  public AbstractDBHead2PosDateTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL,
      final String pPos2DataBaseTableName, final String pPos2KeyfieldName, final String pPos2insertHeadSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL,

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
        + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW()");

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(pLookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

        this.readPosSQL = //
            "SELECT * " //
                + "FROM   " + pPosDataBaseTableName + " " //
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
                + "  AND  " + pKeyFieldName + " = ? " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + myDataBaseDepending.getSQLTimeNow();

        this.invalidatePosSQL = //
            "UPDATE " + pPosDataBaseTableName + " " //
                + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= " + myDataBaseDepending.getSQLTimeOutdate() + " " //
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
                + "  AND  " + pKeyFieldName + " = ? " //
                + "  AND  " + pPosKeyfieldName + " = ? " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow();

        this.insertPosSQL =
            pInsertPosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                myDataBaseDepending.getSQLTimeNow());

        this.readPos2SQL = //
            "SELECT * " //
                + "FROM   " + pPos2DataBaseTableName + " " //
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
                + "  AND  " + pKeyFieldName + " = ? " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > " + myDataBaseDepending.getSQLTimeNow();

        this.invalidatePos2SQL = //
            "UPDATE " + pPos2DataBaseTableName + " " //
                + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "=" + myDataBaseDepending.getSQLTimeOutdate() + " " //
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
                + "  AND  " + pKeyFieldName + " = ? " //
                + "  AND  " + pPos2KeyfieldName + " = ? " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow();

        this.insertPos2SQL =
            pPos2insertHeadSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                myDataBaseDepending.getSQLTimeNow());
      }
      ic.close();
    } catch (final Exception e) {
      throw new UnexpectedException(e.toString(), e);
    }
  }

  /**
   * <code>searchSQLSelect</code> setup a part of the sql statement to search for a user.
   *
   * @param pDataBase Database connection
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
  protected final String searchSQLSelect(final Connection pDataBase, final String pMinMax, final String pSearchField,
      final String pSearchMethodeEntry, final String pSearchFieldEntry, final String pDbKeyVGL, final String pDbKey)
      throws SQLException {
    final int mandator = this.getUser().getMandator();
    final DataBaseDepending myDataBaseDepending = new DataBaseDepending(pDataBase.getMetaData().getDatabaseProductName());

    final int sqlStringLengthMin = 87;
    final StringBuilder sqlString = new StringBuilder(sqlStringLengthMin);
    sqlString.append("SELECT ").append(pMinMax).append('(').append(this.getKeyFieldName()).append(") AS dbnumber ")
        .append("FROM   ").append(this.getDataBaseTableName()).append(' ').append("WHERE  ")
        .append(Constants.DB_FIELD_GLOBAL_MANDATOR).append(" = ").append(Integer.toString(mandator)).append(' ')
        .append(" AND   ").append(this.getKeyFieldName()).append(' ').append(pDbKeyVGL).append(' ')
        .append(StringToSQL.convertString(pDbKey, pDataBase.getMetaData().getDatabaseProductName())).append(' ')
        .append(" AND   ").append(Constants.DB_FIELD_GLOBAL_DATE_FROM).append(" <= ")
        .append(myDataBaseDepending.getSQLTimeNow()).append(' ').append(" AND   ").append(Constants.DB_FIELD_GLOBAL_DATE_TO)
        .append("   > ").append(myDataBaseDepending.getSQLTimeNow()).append(' ').append(" AND   ");

    if ("=".equals(pSearchMethodeEntry)) {
      sqlString.append(StringToSQL.searchString(pSearchField, pSearchFieldEntry, pDataBase.getMetaData()
          .getDatabaseProductName()));
    } else if ("like".equals(pSearchMethodeEntry)) {
      sqlString.append(StringToSQL.searchString(pSearchField, "*" + pSearchFieldEntry + "*", pDataBase.getMetaData()
          .getDatabaseProductName()));
    } else {
      sqlString.append(pSearchField + " " + pSearchMethodeEntry + " "
          + StringToSQL.convertString(pSearchFieldEntry, pDataBase.getMetaData().getDatabaseProductName()));
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
   * <code>fillInsertPos</code> fills the parameters of the insert prepared statement.
   *
   * @param pInsertPosSQLStatement prepared statement for inserts
   * @param pMandator mandator number
   * @param pUser user name
   * @param pSaveEntry entry to save
   * @param pDelete delete or not delete
   * @param pPos2Number number of the position to save
   * @throws SQLException when error occurs
   */
  protected abstract void fillInsertPos2(final PreparedStatement pInsertPosSQLStatement, final int pMandator,
      final String pUser, final E pSaveEntry, final boolean pDelete, final int pPos2Number) throws SQLException;

  /**
   * <code>fillPosFromResultSet</code> set the fields in thisEntry from the given resultSet.
   *
   * @param pResultPos2 ResultSet to read Entries from
   * @param pThisEntry Entry to fill
   * @return filled Entry
   * @throws SQLException when error occurs
   */
  protected abstract E fillPos2FromResultSet(ResultSet pResultPos2, E pThisEntry) throws SQLException;

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

            try (final PreparedStatement invalidatePosSQLStatement = thisDataBase.prepareStatement(this.invalidatePosSQL)) {
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

            try (final PreparedStatement invalidatePos2SQLStatement = thisDataBase.prepareStatement(this.invalidatePos2SQL)) {
              int numPos2 = 0;
              if (dbEntry.getKeyPos2() != null) {
                numPos2 = dbEntry.getKeyPos2().length;
              }
              for (int i = 0; i < numPos2; i++) {
                int sqlPos = 1;
                invalidatePos2SQLStatement.clearParameters();
                invalidatePos2SQLStatement.setInt(sqlPos++, mandator);
                invalidatePos2SQLStatement.setString(sqlPos++, pCurrentEntry);
                invalidatePos2SQLStatement.setString(sqlPos++, dbEntry.getKeyPos2()[i]);
                invalidatePos2SQLStatement.executeUpdate();
                this.insertPosition2Entry(thisDataBase, mandator, user, dbEntry, true, i);
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
    try (PreparedStatement insertPosSQLStatement = pThisDataBase.prepareStatement(this.insertPosSQL)) {
      insertPosSQLStatement.clearParameters();
      this.fillInsertPos(insertPosSQLStatement, pMandator, pUser, pSaveEntry, pDelete, pPosNumber);
      num = insertPosSQLStatement.executeUpdate();
    }
    return num;
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
  protected final int insertPosition2Entry(final Connection pThisDataBase, final int pMandator, final String pUser,
      final E pSaveEntry, final boolean pDelete, final int pPosNumber) throws SQLException {
    int num = -1;
    try (final PreparedStatement insertPos2SQLStatement = pThisDataBase.prepareStatement(this.insertPos2SQL)) {
      insertPos2SQLStatement.clearParameters();
      this.fillInsertPos2(insertPos2SQLStatement, pMandator, pUser, pSaveEntry, pDelete, pPosNumber);
      num = insertPos2SQLStatement.executeUpdate();
    }
    return num;
  }

  /**
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pThisDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read
   * @param pThisEntry structure to be filled
   * @return the filled structure
   */
  @Override
  protected final E readOneEntry(final Connection pThisDataBase, final int pMandator, final String pEntry, final E pThisEntry) {
    E returnEntry = pThisEntry;
    try {
      returnEntry = super.readHeadEntry(pThisDataBase, pMandator, pEntry, returnEntry);

      if (returnEntry != null && returnEntry.getKeyCur() != null) {
        try (final PreparedStatement readPosSQLStatement = pThisDataBase.prepareStatement(this.readPosSQL)) {
          readPosSQLStatement.clearParameters();
          readPosSQLStatement.setInt(1, pMandator);
          readPosSQLStatement.setString(2, pEntry);
          try (final ResultSet resultPos = readPosSQLStatement.executeQuery()) {
            returnEntry = this.fillPosFromResultSet(resultPos, returnEntry);
          }
        }
      }

      if (returnEntry != null && returnEntry.getKeyCur() != null) {
        try (final PreparedStatement readPos2SQLStatement = pThisDataBase.prepareStatement(this.readPos2SQL)) {
          readPos2SQLStatement.clearParameters();
          readPos2SQLStatement.setInt(1, pMandator);
          readPos2SQLStatement.setString(2, pEntry);

          try (final ResultSet resultPos2 = readPos2SQLStatement.executeQuery()) {
            returnEntry = this.fillPos2FromResultSet(resultPos2, pThisEntry);
          }
        }
      }
    } catch (final SQLException nef) {
      returnEntry = null;
    }
    return returnEntry;
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
          final PreparedStatement invalidatePos2SQLStatement = pThisDataBase.prepareStatement(this.invalidatePos2SQL)) {
        if (!pCurrentEntry.equalsEntry(pDbEntry) && (this.getInvalidateHeadSQL() != null)) {
          // Invalidate old entry
          try (final PreparedStatement invalidateHeadSQLStat = pThisDataBase.prepareStatement(this.getInvalidateHeadSQL())) {
            invalidateHeadSQLStat.clearParameters();
            invalidateHeadSQLStat.setInt(1, pMandator);
            invalidateHeadSQLStat.setString(2, pSaveKeyString);
            invalidateHeadSQLStat.executeUpdate();
            // Insert new entry
            this.insertEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false);
            pCurrentEntry.setKeyCur(pSaveKeyString);
          }
        }
        // Position - Take a look if position differ and invalidate old
        int numPos = 0;
        int numDBPos = 0;
        if (pCurrentEntry.getKeyPos() != null) {
          numPos = pCurrentEntry.getKeyPos().length;
        }
        if (pDbEntry != null && pDbEntry.getKeyPos() != null) {
          numDBPos = pDbEntry.getKeyPos().length;
        }

        for (int i = 0; i < numDBPos; i++) {
          boolean isremoved = true;
          for (int j = 0; j < numPos && isremoved; j++) {
            isremoved = !(pDbEntry.getKeyPos()[i].equals(pCurrentEntry.getKeyPos()[j]));
          }
          if (isremoved) {
            int invPos = 1;
            invalidatePosSQLStatement.clearParameters();
            invalidatePosSQLStatement.setInt(invPos++, pMandator);
            invalidatePosSQLStatement.setString(invPos++, pSaveKeyString);
            invalidatePosSQLStatement.setString(invPos++, pDbEntry.getKeyPos()[i]);
            invalidatePosSQLStatement.executeUpdate();
            this.insertPositionEntry(pThisDataBase, pMandator, pUser, pDbEntry, true, i);
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
        // Positions2
        int numPos2 = 0;
        int numDBPos2 = 0;
        if (pCurrentEntry.getKeyPos2() != null) {
          numPos2 = pCurrentEntry.getKeyPos2().length;
        }
        if (pDbEntry != null && pDbEntry.getKeyPos2() != null) {
          numDBPos2 = pDbEntry.getKeyPos2().length;
        }
        for (int i = 0; i < numDBPos2; i++) {
          boolean isremoved = true;
          for (int j = 0; j < numPos2 && isremoved; j++) {
            isremoved = !(pDbEntry.getKeyPos2()[i].equals(pCurrentEntry.getKeyPos2()[j]));
          }
          if (isremoved) {
            int invPos = 1;
            invalidatePos2SQLStatement.clearParameters();
            invalidatePos2SQLStatement.setInt(invPos++, pMandator);
            invalidatePos2SQLStatement.setString(invPos++, pSaveKeyString);
            invalidatePos2SQLStatement.setString(invPos++, pDbEntry.getKeyPos2()[i]);
            invalidatePos2SQLStatement.executeUpdate();
            this.insertPosition2Entry(pThisDataBase, pMandator, pUser, pDbEntry, true, i);
          }
        }
        for (int i = 0; i < numPos2; i++) {
          boolean isnew = true;
          for (int j = 0; j < numDBPos2 && isnew; j++) {
            if (pDbEntry.getKeyPos2()[j].equals(pCurrentEntry.getKeyPos2()[i])) {
              isnew = false;
              if (!pCurrentEntry.equalsPosition2(pDbEntry, i, j)) {
                int invPos = 1;
                invalidatePos2SQLStatement.clearParameters();
                invalidatePos2SQLStatement.setInt(invPos++, pMandator);
                invalidatePos2SQLStatement.setString(invPos++, pSaveKeyString);
                invalidatePos2SQLStatement.setString(invPos++, pDbEntry.getKeyPos2()[i]);
                invalidatePos2SQLStatement.executeUpdate();
                this.insertPosition2Entry(pThisDataBase, pMandator, pUser, pCurrentEntry, false, i);
              }
            }
          }
          if (isnew) {
            this.insertPosition2Entry(pThisDataBase, pMandator, pUser, pCurrentEntry, false, i);
          }
        }
      }
    }
  }
}
