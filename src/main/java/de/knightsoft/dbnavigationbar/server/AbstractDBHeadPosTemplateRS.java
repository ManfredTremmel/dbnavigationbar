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

import de.knightsoft.dbnavigationbar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.dbnavigationbar.shared.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The <code>RiPhoneDBHeadPosTemplate</code> class is the server side implementation template for a simple database. The same as
 * AbstractDBHeadPosTemplate but without read and save implementations.
 *
 * @param <E> DataBase structure type
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadPosTemplateRS<E extends DomainHeadPosDataBaseInt> extends AbstractDBTemplate<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 5037203911862183068L;

  /**
   * SQL string to update database.
   */
  private final String updateHeadSQL;

  /**
   * sql statment to read a position.
   */
  private final String readPosSQL;

  /**
   * sql statment to invalidate/delete a position.
   */
  private final String invalidatePosSQL;

  /**
   * sql statment to insert a position.
   */
  private final String insertPosSQL;

  /**
   * sql statment to update a position.
   */
  private final String updatePosSQL;

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase Data source for lookup
   * @param pSessionUser name of the session where user data are saved
   * @param pDataBaseTableName data base table name
   * @param pKeyFieldName name of the key field in the database
   * @param pInsertHeadSQL insert sql statement
   * @param pUpdateHeadSQL update sql statement
   * @param pPosDataBaseTableName data base table name position
   * @param pPosKeyfieldName name of the key field in the database position
   * @param pInsertPosSQL insert sql statement position
   * @param pUpdatePosSQL update sql statement position
   */
  public AbstractDBHeadPosTemplateRS(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pUpdateHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL, //
      final String pUpdatePosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL,

    "SELECT MIN(" + pKeyFieldName + ") AS min, " //
        + "       MAX(" + pKeyFieldName + ") AS max " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? ",

    "SELECT MIN(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " > ? ",

    "SELECT MAX(" + pKeyFieldName + ") AS dbnumber " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " < ? ",

    "SELECT * " //
        + "FROM   " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? ",

    "DELETE FROM " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? ");

    this.readPosSQL = //
        "SELECT * " //
            + "FROM   " + pPosDataBaseTableName + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
            + "  AND  " + pKeyFieldName + " = ? ";

    this.invalidatePosSQL = //
        "DELETE FROM " + pPosDataBaseTableName + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
            + "  AND  " + pKeyFieldName + " = ? " //
            + "  AND  " + pPosKeyfieldName + " = ? ";

    this.updateHeadSQL = pUpdateHeadSQL;
    this.insertPosSQL = pInsertPosSQL;
    this.updatePosSQL = pUpdatePosSQL;
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

    final int sqlLength = 60;
    final StringBuilder sqlString = new StringBuilder(sqlLength);
    sqlString.append( //
        "SELECT " + pMinMax + "(" + this.getKeyFieldName() + ") AS dbnumber " //
            + "FROM   " + this.getDataBaseTableName() + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " " //
            + " AND   " + this.getKeyFieldName() + " " + pDbKeyVGL //
            + " " + StringToSQL.convertString(pDbKey, pThisDataBase.getMetaData().getDatabaseProductName()) + " " //
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
   * getUpdateHeadSQL.
   *
   * @return updateHeadSQL sql statement
   */
  public final String getUpdateHeadSQL() {
    return this.updateHeadSQL;
  }

  /**
   * <code>fillUpdateHead</code> fills the parameters of the update prepared statement.
   *
   * @param pUpdateHeadSQLStatement update sql statement
   * @param pMandator mandator number
   * @param pUser user name
   * @param pSaveEntry entry to save
   * @throws SQLException if fill up fails
   */
  protected abstract void fillUpdateHead(PreparedStatement pUpdateHeadSQLStatement, int pMandator, String pUser, E pSaveEntry)
      throws SQLException;

  /**
   * <code>fillUpdatePos</code> fills the parameters of the update prepared statement.
   *
   * @param pUpdatePosSQLStatement update sql statement position
   * @param pMandator mandator number
   * @param pUser user name
   * @param pSaveEntry entry to save from
   * @param pPosNumber position number to save
   * @throws SQLException when sql error occurs
   */
  protected abstract void fillUpdatePos(PreparedStatement pUpdatePosSQLStatement, int pMandator, String pUser, E pSaveEntry,
      int pPosNumber) throws SQLException;

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
      int numPos = 0;
      if (pCurrentEntry.getKeyPos() != null) {
        numPos = pCurrentEntry.getKeyPos().length;
      }
      int numDbPos = 0;
      if (pDbEntry != null && pDbEntry.getKeyPos() != null) {
        numDbPos = pDbEntry.getKeyPos().length;
      }
      if ((pDbEntry == null) || (pDbEntry.getKeyCur() == null)) {
        // new Entry, insert a new one
        this.insertEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false);
        for (int i = 0; i < numPos; i++) {
          this.insertPositionEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false, i);
        }
      } else {
        // Entry already exists, update it, if necessary
        if (!pCurrentEntry.equals(pDbEntry)) {
          if (!pCurrentEntry.equalsEntry(pDbEntry) && (this.updateHeadSQL != null)) {
            // Invalidate old entry
            try (final PreparedStatement updateHeadSQLStatement = pThisDataBase.prepareStatement(this.updateHeadSQL)) {
              updateHeadSQLStatement.clearParameters();
              this.fillUpdateHead(updateHeadSQLStatement, pMandator, pUser, pCurrentEntry);
              updateHeadSQLStatement.executeUpdate();
            }
            pCurrentEntry.setKeyCur(pSaveKeyString);
          }

          // Positions
          try (final PreparedStatement invalidatePosSQLStatement = pThisDataBase.prepareStatement(this.getInvalidatePosSQL());
              final PreparedStatement updatePosSQLStatement = pThisDataBase.prepareStatement(this.updatePosSQL)) {
            // Take a look if position differ and invalidate old
            for (int i = 0; i < numDbPos; i++) {
              boolean isremoved = true;
              for (int j = 0; j < numPos && isremoved; j++) {
                if (pDbEntry.getKeyPos()[i].equals(pCurrentEntry.getKeyPos()[j])) {
                  isremoved = false;
                }
              }
              if (isremoved) {
                int sqlPos = 1;
                invalidatePosSQLStatement.clearParameters();
                invalidatePosSQLStatement.setInt(sqlPos++, pMandator);
                invalidatePosSQLStatement.setString(sqlPos++, pSaveKeyString);
                invalidatePosSQLStatement.setString(sqlPos++, pDbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();
                this.insertPositionEntry(pThisDataBase, pMandator, pUser, pDbEntry, true, i);
                // Invalidate old entry
              }
            }
            // Take a look if position differ, update them und
            // insert new
            for (int i = 0; i < numPos; i++) {
              boolean isnew = true;
              for (int j = 0; j < numDbPos && isnew; j++) {
                if (pDbEntry.getKeyPos()[j].equals(pCurrentEntry.getKeyPos()[i])) {
                  isnew = false;
                  // Entry already exists, look for
                  // changes
                  if (!pCurrentEntry.equalsPosition(pDbEntry, i, j)) {
                    // Invalidate old entry
                    updatePosSQLStatement.clearParameters();
                    this.fillUpdatePos(updatePosSQLStatement, pMandator, pUser, pCurrentEntry, i);
                    updatePosSQLStatement.executeUpdate();
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
    }
  }

  /**
   * get invalidate pos sql string.
   *
   * @return invalidatePosSQL
   */
  public final String getInvalidatePosSQL() {
    return this.invalidatePosSQL;
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
