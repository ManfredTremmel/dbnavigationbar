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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.domain.DomainHeadPosDataBaseInt;
import de.knightsoft.dbnavigationbar.shared.Constants;

/**
 * 
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server
 * side implementation.
 * template for a simple database
 * 
 * @param <E>
 *        Structure of the database
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadPosDateTemplateR<E extends DomainHeadPosDataBaseInt>
    extends AbstractDBTemplate<E>
{

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
   * @param setType
   *        - class instance of E
   * @param setLookUpDataBase
   *        look up of the data base
   * @param setSessionUser
   *        user session key
   * @param setDataBaseTableName
   *        database table name
   * @param setKeyFieldName
   *        key field of the database
   * @param setInsertHeadSQL
   *        sql statement to insert a new head entry
   * @param setInsertPosSQL
   *        sql statement to insert a new head entry (position)
   * @param setReadMinMaxSQL
   *        sql statement for min/max read
   * @param setReadNextSQL
   *        sql statement to read next key
   * @param setReadPrevSQL
   *        sql statement to read previous key
   * @param setReadHeadSQL
   *        sql statement to read head entry
   * @param setInvalidateHeadSQL
   *        sql statement to invalidate head entry
   * @param setReadPosSQL
   *        sql statement to read position entry
   * @param setInvalidatePosSQL
   *        sql statement to invalidate position entry
   */
  public AbstractDBHeadPosDateTemplateR(
      final Class<E> setType,
      final String setLookUpDataBase,
      final String setSessionUser,
      final String setDataBaseTableName,
      final String setKeyFieldName,
      final String setInsertHeadSQL,
      final String setInsertPosSQL,

      final String setReadMinMaxSQL,
      final String setReadNextSQL,
      final String setReadPrevSQL,
      final String setReadHeadSQL,
      final String setInvalidateHeadSQL,

      final String setReadPosSQL,
      final String setInvalidatePosSQL)
  {
    super(setType,
        setLookUpDataBase,
        setSessionUser,
        setDataBaseTableName,
        setKeyFieldName,
        setInsertHeadSQL,
        setReadMinMaxSQL,
        setReadNextSQL,
        setReadPrevSQL,
        setReadHeadSQL,
        setInvalidateHeadSQL);

    try
    {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource =
          (DataSource) ic.lookup(setLookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection())
      {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

        if (setReadPosSQL == null)
        {
          this.readPosSQL = null;
        }
        else
        {
          this.readPosSQL = setReadPosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
              .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
        }

        if (setInvalidatePosSQL == null)
        {
          this.invalidatePosSQL = null;
        }
        else
        {
          this.invalidatePosSQL = setInvalidatePosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
              .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
        }

        if (setInsertPosSQL == null)
        {
          this.insertPosSQL = null;
        }
        else
        {
          this.insertPosSQL = setInsertPosSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
              .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
        }
      }
      ic.close();
    }
    catch (final SQLException e)
    {
      throw new UnexpectedException(e.toString(), e);
    }
    catch (final NamingException e)
    {
      throw new UnexpectedException(e.toString(), e);
    }
  }

  /**
   * Constructor, set up database connection.
   * 
   * @param setType
   *        - class instance of E
   * @param setLookUpDataBase
   *        look up of the data base
   * @param setSessionUser
   *        user session key
   * @param setDataBaseTableName
   *        database table name
   * @param setKeyFieldName
   *        key field of the database
   * @param setInsertHeadSQL
   *        sql statement to insert a new head entry
   * @param setPosDataBaseTableName
   *        database table name (position)
   * @param setPosKeyfieldName
   *        key field of the database (position)
   * @param setInsertPosSQL
   *        sql statement to insert a new head entry (position)
   */
  public AbstractDBHeadPosDateTemplateR(
      final Class<E> setType,
      final String setLookUpDataBase,
      final String setSessionUser,
      final String setDataBaseTableName,
      final String setKeyFieldName,
      final String setInsertHeadSQL,
      final String setPosDataBaseTableName,
      final String setPosKeyfieldName,
      final String setInsertPosSQL)
  {
    this(setType,
        setLookUpDataBase,
        setSessionUser,
        setDataBaseTableName,
        setKeyFieldName,
        setInsertHeadSQL,
        setInsertPosSQL,

        "SELECT MIN(" + setKeyFieldName + ") AS min, "
            + "       MAX(" + setKeyFieldName + ") AS max "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "SELECT MIN(" + setKeyFieldName + ") AS dbnumber "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " > ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "SELECT MAX(" + setKeyFieldName + ") AS dbnumber "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " < ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "SELECT * "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "UPDATE " + setDataBaseTableName + " "
            + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();",

        "SELECT * "
            + "FROM   " + setPosDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "UPDATE " + setPosDataBaseTableName + " "
            + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + setPosKeyfieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();");
  }

  /**
   * Constructor, set up database connection.
   * 
   * @param setType
   *        - class instance of E
   * @param setLookUpDataBase
   *        look up of the data base
   * @param setSessionUser
   *        user session key
   * @param setDataBaseTableName
   *        database table name
   * @param setKeyFieldName
   *        key field of the database
   * @param setInsertHeadSQL
   *        sql statement to insert a new head entry
   * @param setPosDataBaseTableName
   *        database table name (position)
   * @param setPosKeyfieldName
   *        key field of the database (position)
   * @param setInsertPosSQL
   *        sql statement to insert a new head entry (position)
   * @param setReadHeadSQL
   *        sql statement to read head entry
   * @param setReadPosSQL
   *        sql statement to read position entry
   */
  public AbstractDBHeadPosDateTemplateR(
      final Class<E> setType,
      final String setLookUpDataBase,
      final String setSessionUser,
      final String setDataBaseTableName,
      final String setKeyFieldName,
      final String setInsertHeadSQL,
      final String setPosDataBaseTableName,
      final String setPosKeyfieldName,
      final String setInsertPosSQL,
      final String setReadHeadSQL,
      final String setReadPosSQL)
  {
    this(setType,
        setLookUpDataBase,
        setSessionUser,
        setDataBaseTableName,
        setKeyFieldName,
        setInsertHeadSQL,
        setInsertPosSQL,

        "SELECT MIN(" + setKeyFieldName + ") AS min, "
            + "       MAX(" + setKeyFieldName + ") AS max "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "SELECT MIN(" + setKeyFieldName + ") AS dbnumber "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " > ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        "SELECT MAX(" + setKeyFieldName + ") AS dbnumber "
            + "FROM   " + setDataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " < ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > NOW();",

        setReadHeadSQL,

        "UPDATE " + setDataBaseTableName + " "
            + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + " = OUTDATE() "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();",

        setReadPosSQL,

        "UPDATE " + setPosDataBaseTableName + " "
            + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= OUTDATE() "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
            + "  AND  " + setKeyFieldName + " = ? "
            + "  AND  " + setPosKeyfieldName + " = ? "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= NOW() "
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();");
  }

  /**
   * <code>searchSQLSelect</code> setup a part of the
   * sql statement to search for a user.
   * 
   * @param thisDataBase
   *        Database connection
   * @param minMax
   *        "MIN" or "MAX" used for PhoneNumber
   * @param searchField
   *        Field to search for
   * @param searchMethodeEntry
   *        compare method for search
   *        ("<", "<=", "=", ">", ">=" or "like")
   * @param searchFieldEntry
   *        value to search for
   * @param dbKeyVGL
   *        compare method of phone number
   *        ("<", "<=", "=", ">", ">=" or "like")
   * @param dbKey
   *        comparison number
   * @return SQL-String
   * @throws SQLException
   *         when error occurs
   */
  @Override
  protected final String searchSQLSelect(
      final Connection thisDataBase,
      final String minMax,
      final String searchField,
      final String searchMethodeEntry,
      final String searchFieldEntry,
      final String dbKeyVGL,
      final String dbKey) throws SQLException
  {
    final int mandator = this.getUser().getMandator();
    final DataBaseDepending myDataBaseDepending = new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

    final StringBuilder sqlString = new StringBuilder();
    sqlString.append(
        "SELECT " + minMax + "(" + this.getKeyFieldName() + ") AS dbnumber "
            + "FROM   " + this.getDataBaseTableName() + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " "
            + " AND   " + this.getKeyFieldName() + " " + dbKeyVGL + " " + StringToSQL.convertString(dbKey,
                thisDataBase.getMetaData().getDatabaseProductName()) + " "
            + " AND   " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND   " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND   ");

    if ("=".equals(searchMethodeEntry))
    {
      sqlString.append(StringToSQL.searchString(searchField, searchFieldEntry,
          thisDataBase.getMetaData().getDatabaseProductName()));
    }
    else if ("like".equals(searchMethodeEntry))
    {
      sqlString.append(StringToSQL.searchString(searchField, "*" + searchFieldEntry + "*",
          thisDataBase.getMetaData().getDatabaseProductName()));
    }
    else
    {
      sqlString.append(searchField + " " + searchMethodeEntry + " " + StringToSQL.convertString(searchFieldEntry,
          thisDataBase.getMetaData().getDatabaseProductName()));
    }
    return sqlString.toString();
  }

  /**
   * <code>fillInsertPos</code> fills the parameters of the insert
   * prepared statement.
   * 
   * @param insertPosSQLStatement
   *        prepared statement for inserts
   * @param mandator
   *        mandator number
   * @param user
   *        user name
   * @param saveEntry
   *        entry to save
   * @param delete
   *        delete or not delete
   * @param posNumber
   *        number of the position to save
   * @throws SQLException
   *         when error occurs
   */
  protected abstract void fillInsertPos(
      final PreparedStatement insertPosSQLStatement,
      final int mandator,
      final String user,
      final E saveEntry,
      final boolean delete,
      final int posNumber) throws SQLException;

  /**
   * <code>fillPosFromResultSet</code> set the fields in thisEntry from
   * the given resultSet.
   * 
   * @param resultPos
   *        ResultSet to read Entries from
   * @param thisEntry
   *        Entry to fill
   * @return filled Entry
   * @throws SQLException
   *         when error occurs
   */
  protected abstract E fillPosFromResultSet(ResultSet resultPos,
      E thisEntry) throws SQLException;

  /**
   * <code>deleteEntry</code> deletes one entry from database.
   * 
   * @param currentEntry
   *        entry to delete
   * @return entry to display after deletion
   */
  @Override
  public final E deleteEntry(final String currentEntry)
  {
    E resultValue = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser != null)
    {
      final int mandator = thisUser.getMandator();
      final String user = thisUser.getUser();

      try
      {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource =
            (DataSource) ic.lookup(this.getLookUpDataBase());
        try (final Connection thisDataBase = lDataSource.getConnection())
        {

          if (this.allowedToChange())
          {
            final E dbEntry = this.readEntry(currentEntry);
            // invalidate head number
            if (this.getInvalidateHeadSQL() != null)
            {
              try (final PreparedStatement invalidateHeadSQLStatement =
                  thisDataBase.prepareStatement(this.getInvalidateHeadSQL()))
              {
                invalidateHeadSQLStatement.clearParameters();
                invalidateHeadSQLStatement.setInt(1, mandator);
                invalidateHeadSQLStatement.setString(2, currentEntry);
                invalidateHeadSQLStatement.executeUpdate();
                this.insertEntry(thisDataBase, mandator, user, dbEntry, true);
              }
            }

            try (PreparedStatement invalidatePosSQLStatement = thisDataBase.prepareStatement(this.invalidatePosSQL))
            {
              int numPos = 0;
              if (dbEntry.getKeyPos() != null)
              {
                numPos = dbEntry.getKeyPos().length;
              }
              for (int i = 0; i < numPos; i++)
              {
                int sqlPos = 1;
                invalidatePosSQLStatement.clearParameters();
                invalidatePosSQLStatement.setInt(sqlPos++, mandator);
                invalidatePosSQLStatement.setString(sqlPos++,
                    currentEntry);
                invalidatePosSQLStatement.setString(sqlPos++,
                    dbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();
                this.insertPositionEntry(thisDataBase, mandator,
                    user, dbEntry, true, i);
              }
            }
          }

          resultValue = this.readNextEntry(currentEntry);
        }
        ic.close();
      }
      catch (final SQLException e)
      {
        resultValue = null;
      }
      catch (final NamingException e)
      {
        resultValue = null;
      }
    }
    return resultValue;
  }

  /**
   * <code>insertPositionEntry</code> inserts a position into the database.
   * 
   * @param thisDataBase
   *        database connection
   * @param mandator
   *        mandator number
   * @param user
   *        user name
   * @param saveEntry
   *        entry to save
   * @param delete
   *        delete old entries
   * @param posNumber
   *        position number
   * @return effected database entries (should always be 1)
   * @throws SQLException
   *         when error occurs
   */
  protected final int insertPositionEntry(
      final Connection thisDataBase,
      final int mandator,
      final String user, final E saveEntry,
      final boolean delete, final int posNumber
      ) throws SQLException
  {
    int num = -1;
    try (final PreparedStatement insertPosSQLStatement = thisDataBase.prepareStatement(this.insertPosSQL))
    {
      insertPosSQLStatement.clearParameters();
      this.fillInsertPos(insertPosSQLStatement, mandator, user, saveEntry, delete, posNumber);
      num = insertPosSQLStatement.executeUpdate();
    }
    return num;
  }

  /**
   * <code>saveEntry</code> saves or inserts a
   * entry to database.
   * 
   * @param currentEntry
   *        entry that has to be saved
   * @param dbEntry
   *        entry from database to compare
   * @param thisDataBase
   *        database connection
   * @param mandator
   *        mandator number
   * @param user
   *        name of the user
   * @param saveKeyString
   *        key of the entry to save
   * @throws SQLException
   *         if something's going wrong
   */
  @Override
  protected final void saveEntry(
      final E currentEntry,
      final E dbEntry,
      final Connection thisDataBase,
      final int mandator,
      final String user,
      final String saveKeyString
      ) throws SQLException
  {
    // Entry already exists in Database?
    if (!currentEntry.equals(dbEntry))
    {

      try (final PreparedStatement invalidatePosSQLStatement = thisDataBase.prepareStatement(this.invalidatePosSQL);
          final PreparedStatement invalidateHeadSQLStatement = thisDataBase.prepareStatement(this.getInvalidateHeadSQL()))
      {

        if (!currentEntry.equalsEntry(dbEntry)
            && (this.getInvalidateHeadSQL() != null))
        {
          // Invalidate old entry
          invalidateHeadSQLStatement.clearParameters();
          invalidateHeadSQLStatement.setInt(1, mandator);
          invalidateHeadSQLStatement.setString(2, saveKeyString);
          invalidateHeadSQLStatement.executeUpdate();

          // Insert new entry
          this.insertEntry(thisDataBase, mandator, user, currentEntry, false);

          currentEntry.setKeyCur(saveKeyString);
        }
        // Positions
        // Take a look if position differ and invalidate old
        int numPos = 0;
        if (currentEntry.getKeyPos() != null)
        {
          numPos = currentEntry.getKeyPos().length;
        }
        int numDBPos = 0;
        if (dbEntry != null && dbEntry.getKeyPos() != null)
        {
          numDBPos = dbEntry.getKeyPos().length;
        }

        for (int i = 0; i < numDBPos; i++)
        {
          boolean isremoved = true;
          for (int j = 0; j < numPos && isremoved; j++)
          {
            if (dbEntry.getKeyPos()[i].equals(
                currentEntry.getKeyPos()[j]))
            {
              isremoved = false;
            }
          }
          if (isremoved)
          {
            int invPos = 1;
            invalidatePosSQLStatement.clearParameters();
            invalidatePosSQLStatement.setInt(invPos++, mandator);
            invalidatePosSQLStatement.setString(invPos++, saveKeyString);
            invalidatePosSQLStatement.setString(invPos++, dbEntry.getKeyPos()[i]);
            invalidatePosSQLStatement.executeUpdate();
            this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
            // Invalidate old entry
          }
        }
        // Take a look if position differ and insert new
        for (int i = 0; i < numPos; i++)
        {
          boolean isnew = true;
          for (int j = 0; j < numDBPos && isnew; j++)
          {
            if (dbEntry.getKeyPos()[j].equals(currentEntry.getKeyPos()[i]))
            {
              isnew = false;
              // Entry already exists, look for changes
              if (!currentEntry.equalsPosition(dbEntry,
                  i, j))
              {
                int invPos = 1;
                // Invalidate old entry
                invalidatePosSQLStatement.clearParameters();
                invalidatePosSQLStatement.setInt(invPos++, mandator);
                invalidatePosSQLStatement.setString(invPos++, saveKeyString);
                invalidatePosSQLStatement.setString(invPos++, dbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();

                this.insertPositionEntry(thisDataBase, mandator, user, currentEntry, false, i);
              }
            }
          }
          if (isnew)
          {
            // Insert new position
            this.insertPositionEntry(thisDataBase, mandator, user, currentEntry, false, i);
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
  public final String getReadPosSQL()
  {
    return this.readPosSQL;
  }
}
