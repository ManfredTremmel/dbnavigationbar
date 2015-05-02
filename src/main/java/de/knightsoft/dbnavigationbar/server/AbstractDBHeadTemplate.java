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
import de.knightsoft.dbnavigationbar.client.domain.DomainHeadDataBaseInterface;
import de.knightsoft.dbnavigationbar.shared.Constants;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation template for a simple database.
 *
 * @param <E> DataBase structure type
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadTemplate<E extends DomainHeadDataBaseInterface> extends AbstractDBTemplate<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 6419810537457908964L;

  /**
   * SQL string to update database.
   */
  private final String updateHeadSQL;

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
   * @param pReadMinMaxSQL sql statement to read min and max entry key
   * @param pReadNextSQL sql statement to read next entry
   * @param pReadPrevSQL sql statement to read previous entry
   * @param pReadHeadSQL sql statement to read the head data
   * @param pInvalidateHeadSQL sql statement to invalidate/delete a entry
   */
  public AbstractDBHeadTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pUpdateHeadSQL,
      final String pReadMinMaxSQL, final String pReadNextSQL, final String pReadPrevSQL, final String pReadHeadSQL,
      final String pInvalidateHeadSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pReadMinMaxSQL,
        pReadNextSQL, pReadPrevSQL, pReadHeadSQL, pInvalidateHeadSQL);

    this.updateHeadSQL = pUpdateHeadSQL;
  }

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
   */
  public AbstractDBHeadTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pUpdateHeadSQL) {
    this(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pUpdateHeadSQL,

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
  }

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
   * @param pReadHeadSQL sql statement to read the head data
   */
  public AbstractDBHeadTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pUpdateHeadSQL,
      final String pReadHeadSQL) {
    this(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pUpdateHeadSQL,

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

    pReadHeadSQL,

    "DELETE FROM " + pDataBaseTableName + " " //
        + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? " //
        + "  AND  " + pKeyFieldName + " = ? ");
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

    final int sqlLength = 60;
    final StringBuilder sqlString = new StringBuilder(sqlLength);
    sqlString.append( //
        "SELECT " + pMinMax + "(" + this.getKeyFieldName() + ") AS dbnumber " //
            + "FROM   " + this.getDataBaseTableName() + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " " //
            + " AND   " + this.getKeyFieldName() + " " + pDbKeyVGL + " " + StringToSQL.convertString(pDbKey, //
                pDataBase.getMetaData().getDatabaseProductName()) + " " //
            + " AND   ");

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
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read
   * @param pThisEntry structure to be filled
   * @return the filled structure
   */
  @Override
  protected final E readOneEntry(final Connection pDataBase, final int pMandator, final String pEntry, final E pThisEntry) {
    return super.readHeadEntry(pDataBase, pMandator, pEntry, pThisEntry);
  }

  /**
   * <code>deleteEntry</code> deletes one entry from database.
   *
   * @param pCurrentEntry key of the current entry
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
            try (final PreparedStatement invalidateHeadSQLStatement =
                thisDataBase.prepareStatement(this.getInvalidateHeadSQL())) {
              invalidateHeadSQLStatement.clearParameters();
              invalidateHeadSQLStatement.setInt(1, mandator);
              invalidateHeadSQLStatement.setString(2, pCurrentEntry);
              invalidateHeadSQLStatement.executeUpdate();
              this.insertEntry(thisDataBase, mandator, user, dbEntry, true);
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
      if ((pDbEntry == null) || (pDbEntry.getKeyCur() == null)) {
        // new Entry, insert a new one
        this.insertEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false);
      } else {
        // Entry already exists, update it, if necessary
        if (!pCurrentEntry.equals(pDbEntry) && !pCurrentEntry.equalsEntry(pDbEntry)) {
          // Invalidate old entry
          try (final PreparedStatement updateHeadSQLStatement = pThisDataBase.prepareStatement(this.updateHeadSQL)) {
            updateHeadSQLStatement.clearParameters();
            this.fillUpdateHead(updateHeadSQLStatement, pMandator, pUser, pCurrentEntry);
            updateHeadSQLStatement.executeUpdate();
          }
        }
      }
    }
  }
}
