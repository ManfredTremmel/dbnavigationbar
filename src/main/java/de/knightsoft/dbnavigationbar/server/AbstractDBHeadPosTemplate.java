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
import de.knightsoft.dbnavigationbar.client.domain.DomainHeadPosDataBaseInt;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The <code>RiPhoneDBHeadPosTemplate</code> class is the server side implementation template for a simple database.
 *
 * @param <E> DataBase structure type
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadPosTemplate<E extends DomainHeadPosDataBaseInt> extends AbstractDBHeadPosTemplateRS<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -8763724693538834151L;

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
  public AbstractDBHeadPosTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pUpdateHeadSQL,
      final String pPosDataBaseTableName, final String pPosKeyfieldName, final String pInsertPosSQL, //
      final String pUpdatePosSQL) {
    super(pType, pLookUpDataBase, pSessionUser, pDataBaseTableName, pKeyFieldName, pInsertHeadSQL, pUpdateHeadSQL,
        pPosDataBaseTableName, pPosKeyfieldName, pInsertPosSQL, pUpdatePosSQL);
  }

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

            try (final PreparedStatement invalidatePosSQLStat = thisDataBase.prepareStatement(this.getInvalidatePosSQL())) {
              int numPos = 0;
              if (dbEntry.getKeyPos() != null) {
                numPos = dbEntry.getKeyPos().length;
              }
              for (int i = 0; i < numPos; i++) {
                int sqlPos = 1;
                invalidatePosSQLStat.clearParameters();
                invalidatePosSQLStat.setInt(sqlPos++, mandator);
                invalidatePosSQLStat.setString(sqlPos++, pCurrentEntry);
                invalidatePosSQLStat.setString(sqlPos++, dbEntry.getKeyPos()[i]);
                invalidatePosSQLStat.executeUpdate();
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
        try (final PreparedStatement readPosSQLStatement = pThisDataBase.prepareStatement(this.getReadPosSQL())) {
          readPosSQLStatement.clearParameters();
          readPosSQLStatement.setInt(1, pMandator);
          readPosSQLStatement.setString(2, pEntry);
          try (final ResultSet resultPos = readPosSQLStatement.executeQuery()) {
            returnEntry = this.fillPosFromResultSet(resultPos, returnEntry);
          }
        }
      }
    } catch (final Exception nef) {
      returnEntry = null;
    }
    return returnEntry;
  }
}
