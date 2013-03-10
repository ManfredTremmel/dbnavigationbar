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
import de.knightsoft.dbnavigationbar.client.domain.DomainHead2PosDataBaseInt;
import de.knightsoft.dbnavigationbar.shared.Constants;

/**
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server
 * side implementation template for a simple database.
 * 
 * @param <E>
 *        Structure of the database
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHead2PosDateTemplate<E extends DomainHead2PosDataBaseInt>
    extends AbstractDBTemplate<E>
{

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
   * @param setPos2DataBaseTableName
   *        database table name (second position)
   * @param setPos2KeyfieldName
   *        key field of the database (second position)
   * @param setPos2insertHeadSQL
   *        sql statement to insert a new head entry (second position)
   */
  public AbstractDBHead2PosDateTemplate(
      final Class<E> setType,
      final String setLookUpDataBase,
      final String setSessionUser,
      final String setDataBaseTableName,
      final String setKeyFieldName,
      final String setInsertHeadSQL,
      final String setPosDataBaseTableName,
      final String setPosKeyfieldName,
      final String setInsertPosSQL,
      final String setPos2DataBaseTableName,
      final String setPos2KeyfieldName,
      final String setPos2insertHeadSQL)
  {
    super(setType,
        setLookUpDataBase,
        setSessionUser,
        setDataBaseTableName,
        setKeyFieldName,
        setInsertHeadSQL,

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
            + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > NOW();");

    try
    {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(setLookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection())
      {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData()
                .getDatabaseProductName());

        this.readPosSQL =
            "SELECT * "
                + "FROM   " + setPosDataBaseTableName + " "
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
                + "  AND  " + setKeyFieldName + " = ? "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                + myDataBaseDepending.getSQLTimeNow() + " "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > "
                + myDataBaseDepending.getSQLTimeNow() + ";";

        this.invalidatePosSQL =
            "UPDATE " + setPosDataBaseTableName + " "
                + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "= "
                + myDataBaseDepending.getSQLTimeOutdate() + " "
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
                + "  AND  " + setKeyFieldName + " = ? "
                + "  AND  " + setPosKeyfieldName + " = ? "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                + myDataBaseDepending.getSQLTimeNow() + " "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > "
                + myDataBaseDepending.getSQLTimeNow() + ";";

        this.insertPosSQL = setInsertPosSQL
            .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
            .replace("NOW()", myDataBaseDepending.getSQLTimeNow());

        this.readPos2SQL =
            "SELECT * "
                + "FROM   " + setPos2DataBaseTableName + " "
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
                + "  AND  " + setKeyFieldName + " = ? "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                + myDataBaseDepending.getSQLTimeNow() + " "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + " > "
                + myDataBaseDepending.getSQLTimeNow() + ";";

        this.invalidatePos2SQL =
            "UPDATE " + setPos2DataBaseTableName + " "
                + "SET    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "="
                + myDataBaseDepending.getSQLTimeOutdate() + " "
                + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = ? "
                + "  AND  " + setKeyFieldName + " = ? "
                + "  AND  " + setPos2KeyfieldName + " = ? "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                + myDataBaseDepending.getSQLTimeNow() + " "
                + "  AND  " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > "
                + myDataBaseDepending.getSQLTimeNow() + ";";

        this.insertPos2SQL = setPos2insertHeadSQL
            .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
            .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
      }
      ic.close();
    }
    catch (final Exception e)
    {
      throw new UnexpectedException(e.toString(), e);
    }
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
        "SELECT " + minMax + "(" + this.getKeyFieldName()
            + ") AS dbnumber "
            + "FROM   " + this.getDataBaseTableName() + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = "
            + Integer.toString(mandator) + " "
            + " AND   " + this.getKeyFieldName() + " " + dbKeyVGL
            + " " + StringToSQL.convertString(dbKey,
                thisDataBase.getMetaData().getDatabaseProductName()) + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
            + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > "
            + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND   ");

    if ("=".equals(searchMethodeEntry))
    {
      sqlString.append(StringToSQL.searchString(searchField,
          searchFieldEntry, thisDataBase.getMetaData()
              .getDatabaseProductName()));
    }
    else if ("like".equals(searchMethodeEntry))
    {
      sqlString.append(StringToSQL.searchString(searchField,
          "*" + searchFieldEntry + "*", thisDataBase
              .getMetaData().getDatabaseProductName()));
    }
    else
    {
      sqlString.append(searchField + " " + searchMethodeEntry
          + " " + StringToSQL.convertString(searchFieldEntry,
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
   * @param pos2Number
   *        number of the position to save
   * @throws SQLException
   *         when error occurs
   */
  protected abstract void fillInsertPos2(
      final PreparedStatement insertPosSQLStatement,
      final int mandator,
      final String user,
      final E saveEntry,
      final boolean delete,
      final int pos2Number) throws SQLException;

  /**
   * <code>fillPosFromResultSet</code> set the fields in thisEntry from
   * the given resultSet.
   * 
   * @param resultPos2
   *        ResultSet to read Entries from
   * @param thisEntry
   *        Entry to fill
   * @return filled Entry
   * @throws SQLException
   *         when error occurs
   */
  protected abstract E fillPos2FromResultSet(ResultSet resultPos2,
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
        final DataSource lDataSource = (DataSource) ic.lookup(this.getLookUpDataBase());
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

            try (final PreparedStatement invalidatePosSQLStatement =
                thisDataBase.prepareStatement(this.invalidatePosSQL))
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
                invalidatePosSQLStatement.setString(sqlPos++, currentEntry);
                invalidatePosSQLStatement.setString(sqlPos++, dbEntry.getKeyPos()[i]);
                invalidatePosSQLStatement.executeUpdate();
                this.insertPositionEntry(thisDataBase, mandator, user, dbEntry, true, i);
              }
            }

            try (final PreparedStatement invalidatePos2SQLStatement =
                thisDataBase.prepareStatement(this.invalidatePos2SQL))
            {
              int numPos2 = 0;
              if (dbEntry.getKeyPos2() != null)
              {
                numPos2 = dbEntry.getKeyPos2().length;
              }
              for (int i = 0; i < numPos2; i++)
              {
                int sqlPos = 1;
                invalidatePos2SQLStatement.clearParameters();
                invalidatePos2SQLStatement.setInt(sqlPos++, mandator);
                invalidatePos2SQLStatement.setString(sqlPos++,
                    currentEntry);
                invalidatePos2SQLStatement.setString(sqlPos++,
                    dbEntry.getKeyPos2()[i]);
                invalidatePos2SQLStatement.executeUpdate();
                this.insertPosition2Entry(thisDataBase, mandator,
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
    try (PreparedStatement insertPosSQLStatement = thisDataBase.prepareStatement(this.insertPosSQL))
    {
      insertPosSQLStatement.clearParameters();
      this.fillInsertPos(insertPosSQLStatement, mandator, user, saveEntry, delete, posNumber);
      num = insertPosSQLStatement.executeUpdate();
    }
    return num;
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
  protected final int insertPosition2Entry(
      final Connection thisDataBase,
      final int mandator,
      final String user, final E saveEntry,
      final boolean delete, final int posNumber
      ) throws SQLException
  {
    int num = -1;
    try (final PreparedStatement insertPos2SQLStatement = thisDataBase.prepareStatement(this.insertPos2SQL))
    {
      insertPos2SQLStatement.clearParameters();
      this.fillInsertPos2(insertPos2SQLStatement, mandator, user, saveEntry, delete, posNumber);
      num = insertPos2SQLStatement.executeUpdate();
    }
    return num;
  }

  /**
   * <code>readOneEntry</code> is used to read a given entry
   * from database.
   * 
   * @param thisDataBase
   *        Database Connection
   * @param mandator
   *        mandator is a keyfield
   * @param entry
   *        the Entry to read
   * @param thisEntry
   *        structure to be filled
   * @return the filled structure
   * @throws SQLException
   */
  @Override
  protected final E readOneEntry(final Connection thisDataBase,
      final int mandator, final String entry, final E thisEntry)
  {
    E returnEntry = thisEntry;
    try
    {
      returnEntry = super.readHeadEntry(thisDataBase, mandator, entry, returnEntry);

      if (returnEntry != null && returnEntry.getKeyCur() != null)
      {
        try (final PreparedStatement readPosSQLStatement = thisDataBase.prepareStatement(this.readPosSQL))
        {
          readPosSQLStatement.clearParameters();
          readPosSQLStatement.setInt(1, mandator);
          readPosSQLStatement.setString(2, entry);
          try (final ResultSet resultPos = readPosSQLStatement.executeQuery())
          {
            returnEntry = this.fillPosFromResultSet(resultPos, returnEntry);
          }
        }
      }

      if (returnEntry != null && returnEntry.getKeyCur() != null)
      {
        try (final PreparedStatement readPos2SQLStatement = thisDataBase.prepareStatement(this.readPos2SQL))
        {
          readPos2SQLStatement.clearParameters();
          readPos2SQLStatement.setInt(1, mandator);
          readPos2SQLStatement.setString(2, entry);

          try (final ResultSet resultPos2 = readPos2SQLStatement.executeQuery())
          {
            returnEntry = this.fillPos2FromResultSet(resultPos2, thisEntry);
          }
        }
      }
    }
    catch (final SQLException nef)
    {
      returnEntry = null;
    }
    return returnEntry;
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
          final PreparedStatement invalidatePos2SQLStatement = thisDataBase.prepareStatement(this.invalidatePos2SQL))
      {
        if (!currentEntry.equalsEntry(dbEntry)
            && (this.getInvalidateHeadSQL() != null))
        {
          // Invalidate old entry
          try (final PreparedStatement invalidateHeadSQLStatement =
              thisDataBase.prepareStatement(this.getInvalidateHeadSQL()))
          {
            invalidateHeadSQLStatement.clearParameters();
            invalidateHeadSQLStatement.setInt(1, mandator);
            invalidateHeadSQLStatement.setString(2, saveKeyString);
            invalidateHeadSQLStatement.executeUpdate();
            // Insert new entry
            this.insertEntry(thisDataBase, mandator, user, currentEntry, false);
            currentEntry.setKeyCur(saveKeyString);
          }
        }
        // Position - Take a look if position differ and invalidate old
        int numPos = 0, numDBPos = 0;
        if (currentEntry.getKeyPos() != null)
        {
          numPos = currentEntry.getKeyPos().length;
        }
        if (dbEntry != null && dbEntry.getKeyPos() != null)
        {
          numDBPos = dbEntry.getKeyPos().length;
        }

        for (int i = 0; i < numDBPos; i++)
        {
          boolean isremoved = true;
          for (int j = 0; j < numPos && isremoved; j++)
          {
            isremoved = !(dbEntry.getKeyPos()[i].equals(currentEntry.getKeyPos()[j]));
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
              if (!currentEntry.equalsPosition(dbEntry, i, j))
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
        // Positions2
        int numPos2 = 0, numDBPos2 = 0;
        if (currentEntry.getKeyPos2() != null)
        {
          numPos2 = currentEntry.getKeyPos2().length;
        }
        if (dbEntry != null && dbEntry.getKeyPos2() != null)
        {
          numDBPos2 = dbEntry.getKeyPos2().length;
        }
        for (int i = 0; i < numDBPos2; i++)
        {
          boolean isremoved = true;
          for (int j = 0; j < numPos2 && isremoved; j++)
          {
            isremoved = !(dbEntry.getKeyPos2()[i].equals(currentEntry.getKeyPos2()[j]));
          }
          if (isremoved)
          {
            int invPos = 1;
            invalidatePos2SQLStatement.clearParameters();
            invalidatePos2SQLStatement.setInt(invPos++, mandator);
            invalidatePos2SQLStatement.setString(invPos++, saveKeyString);
            invalidatePos2SQLStatement.setString(invPos++, dbEntry.getKeyPos2()[i]);
            invalidatePos2SQLStatement.executeUpdate();
            this.insertPosition2Entry(thisDataBase, mandator, user, dbEntry, true, i);
          }
        }
        for (int i = 0; i < numPos2; i++)
        {
          boolean isnew = true;
          for (int j = 0; j < numDBPos2 && isnew; j++)
          {
            if (dbEntry.getKeyPos2()[j].equals(currentEntry.getKeyPos2()[i]))
            {
              isnew = false;
              if (!currentEntry.equalsPosition2(dbEntry, i, j))
              {
                int invPos = 1;
                invalidatePos2SQLStatement.clearParameters();
                invalidatePos2SQLStatement.setInt(invPos++, mandator);
                invalidatePos2SQLStatement.setString(invPos++, saveKeyString);
                invalidatePos2SQLStatement.setString(invPos++, dbEntry.getKeyPos2()[i]);
                invalidatePos2SQLStatement.executeUpdate();
                this.insertPosition2Entry(thisDataBase, mandator, user, currentEntry, false, i);
              }
            }
          }
          if (isnew)
          {
            this.insertPosition2Entry(thisDataBase, mandator, user, currentEntry, false, i);
          }
        }
      }
    }
  }
}
