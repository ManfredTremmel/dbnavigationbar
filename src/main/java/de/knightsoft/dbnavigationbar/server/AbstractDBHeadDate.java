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
 * Copyright (c) 2012-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.server;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDataBaseDomain;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService;
import de.knightsoft.dbnavigationbar.server.dbfield.DBFieldFactory;
import de.knightsoft.dbnavigationbar.server.dbfield.DBFieldInterface;
import de.knightsoft.dbnavigationbar.shared.Constants;
import de.knightsoft.dbnavigationbar.shared.exceptions.DeleteDataBaseException;
import de.knightsoft.dbnavigationbar.shared.exceptions.EmptyDataBaseException;
import de.knightsoft.dbnavigationbar.shared.exceptions.EntryNotFoundException;
import de.knightsoft.dbnavigationbar.shared.exceptions.InvalidDataException;
import de.knightsoft.dbnavigationbar.shared.exceptions.SaveDataBaseException;
import de.knightsoft.dbnavigationbar.shared.exceptions.ServerErrorException;
import de.knightsoft.dbnavigationbar.shared.exceptions.UserNotAllowedException;
import de.knightsoft.dbnavigationbar.shared.exceptions.UserNotLoggedInException;
import de.knightsoft.dbnavigationbar.shared.fields.AbstractField;
import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

import org.apache.commons.lang3.StringUtils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Abstract class to implement database calls for the mask.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 *
 * @param <E> Type of the database domain
 * @param <F> Type of the key field
 */
public abstract class AbstractDBHeadDate<E extends AbstractDataBaseDomain, F extends AbstractField<?>> extends
    XsrfProtectedServiceServlet implements AbstractDBRemoteService<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -5162184667431287748L;

  /**
   * class domainType.
   */
  private final Class<E> domainType;
  /**
   * class type of the key field.
   */
  private final Class<F> keyType;
  /**
   * name of the database table.
   */
  private final String dataBaseTableName;
  /**
   * database look up.
   */
  private final String lookUpDataBase;
  /**
   * name to store user data in the session.
   */
  private final String sessionUser;
  /**
   * list of database fields.
   */
  private final List<DBFieldInterface<?>> dbFieldList;
  /**
   * primary key field.
   */
  private final DBFieldInterface<?> dbKeyField;
  /**
   * sql statement to read head entry.
   */
  private final String readHeadSQL;

  /**
   * sql statement for min/max read.
   */
  private final String readMinMaxSQL;
  /**
   * sql statement to read next key.
   */
  private final String readNextSQL;
  /**
   * sql statement to read previous key.
   */
  private final String readPrevSQL;
  /**
   * sql statement to insert a new head entry.
   */
  private final String insertHeadSQL;
  /**
   * sql statement to invalidate head entry.
   */
  private final String invalidateHeadSQL;

  /**
   * constructor.
   *
   * @param pDomainType - class instance of E (domain type)
   * @param pKeyType - class instance of F (key type)
   * @param pDbDriver name of database driver
   * @param pLookUpDataBase key to get database connection from pool
   * @param pDataBaseTableName data base table name to set
   * @param pSessionUser name of the session to store/read data from
   * @param pDataBaseDomain data base domain
   */
  public AbstractDBHeadDate(final Class<E> pDomainType, final Class<F> pKeyType, final String pDbDriver,
      final String pLookUpDataBase, final String pDataBaseTableName, final String pSessionUser, final E pDataBaseDomain) {
    super();
    this.domainType = pDomainType;
    this.keyType = pKeyType;
    this.lookUpDataBase = pLookUpDataBase;
    this.dataBaseTableName = pDataBaseTableName;
    this.sessionUser = pSessionUser;

    // build database field list out of the fields
    this.dbFieldList = new ArrayList<DBFieldInterface<?>>(pDataBaseDomain.getFieldMap().size());
    DBFieldInterface<?> dbKeyFieldTmp = null;
    for (final Entry<String, FieldInterface<?>> entry : pDataBaseDomain.getFieldMap().entrySet()) {
      final DBFieldInterface<?> newDBField = DBFieldFactory.createDBField(pDbDriver, entry.getKey(), entry.getValue());
      this.dbFieldList.add(newDBField);
      if (entry.getValue().isPrimaryKey()) {
        dbKeyFieldTmp = newDBField;
      }
    }
    // without any key field this doesn't work, stop.
    if (dbKeyFieldTmp == null) {
      throw new RuntimeException("missing keyfield in field list");
    }
    this.dbKeyField = dbKeyFieldTmp;

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

        // build sql statement to read one entry by key.
        final int sqlLength = 77;
        final StringBuilder readHeadSB = new StringBuilder(sqlLength);
        readHeadSB.append("SELECT ");
        boolean firstEntry = true;
        for (final DBFieldInterface<?> dbField : this.dbFieldList) {
          if (!firstEntry) {
            readHeadSB.append(", ");
            firstEntry = false;
          }
          readHeadSB.append(dbField.preparedInsertReadPart());
        }
        readHeadSB.append(" FROM ");
        readHeadSB.append(this.dataBaseTableName);
        readHeadSB.append(" WHERE Mandator = ? AND ");
        readHeadSB.append(this.dbKeyField.getDBFieldName());
        readHeadSB.append(" = ?");
        readHeadSB.append(" AND Date_from <= ");
        readHeadSB.append(myDataBaseDepending.getSQLTimeNow());
        readHeadSB.append(" AND Date_to > ");
        readHeadSB.append(myDataBaseDepending.getSQLTimeNow());
        readHeadSB.append(';');
        this.readHeadSQL = readHeadSB.toString();

        // build sql statement to read min and max key entries.
        final int sqlLengthMinMax = 94;
        final StringBuilder readMinMaxSB = new StringBuilder(sqlLengthMinMax);
        readMinMaxSB.append("SELECT MIN(");
        readMinMaxSB.append(dbKeyFieldTmp.getDBFieldName());
        readMinMaxSB.append(") AS min, ");
        readMinMaxSB.append(" MAX(");
        readMinMaxSB.append(dbKeyFieldTmp.getDBFieldName());
        readMinMaxSB.append(") AS max FROM ");
        readMinMaxSB.append(this.dataBaseTableName);
        readMinMaxSB.append(" WHERE Mandator = ? ");
        readMinMaxSB.append(" AND Date_from <= ");
        readMinMaxSB.append(myDataBaseDepending.getSQLTimeNow());
        readMinMaxSB.append(" AND Date_to > ");
        readMinMaxSB.append(myDataBaseDepending.getSQLTimeNow());
        readMinMaxSB.append(';');
        this.readMinMaxSQL = readMinMaxSB.toString();

        // build sql statement to read next key entries.
        final int sqlLengthNext = 93;
        final StringBuilder readNextSB = new StringBuilder(sqlLengthNext);
        readNextSB.append("SELECT MIN(");
        readNextSB.append(dbKeyFieldTmp.getDBFieldName());
        readNextSB.append(") AS dbnumber ");
        readNextSB.append("FROM ");
        readNextSB.append(this.dataBaseTableName);
        readNextSB.append(" WHERE Mandator = ? ");
        readNextSB.append(" AND ");
        readNextSB.append(this.dbKeyField.getDBFieldName());
        readNextSB.append(" > ?");
        readNextSB.append(" AND Date_from <= ");
        readNextSB.append(myDataBaseDepending.getSQLTimeNow());
        readNextSB.append(" AND Date_to > ");
        readNextSB.append(myDataBaseDepending.getSQLTimeNow());
        readNextSB.append(';');
        this.readNextSQL = readNextSB.toString();

        // build sql statement to read previous key entries.
        final int sqlLengthPrev = 85;
        final StringBuilder readPrevSB = new StringBuilder(sqlLengthPrev);
        readPrevSB.append("SELECT MAX(");
        readPrevSB.append(dbKeyFieldTmp.getDBFieldName());
        readPrevSB.append(") AS dbnumber ");
        readPrevSB.append("FROM ");
        readPrevSB.append(this.dataBaseTableName);
        readPrevSB.append(" WHERE Mandator = ? ");
        readPrevSB.append(" AND ");
        readPrevSB.append(this.dbKeyField.getDBFieldName());
        readPrevSB.append(" < ?");
        readPrevSB.append(" AND Date_from <= ");
        readPrevSB.append(myDataBaseDepending.getSQLTimeNow());
        readPrevSB.append(" AND Date_to > ");
        readPrevSB.append(myDataBaseDepending.getSQLTimeNow());
        readPrevSB.append(';');
        this.readPrevSQL = readPrevSB.toString();

        // build sql statement to insert a entry into db.
        final int sqlLengthInsert = 114;
        final StringBuilder insertHeadSQLSB = new StringBuilder(sqlLengthInsert);
        insertHeadSQLSB.append("INSERT INTO ");
        insertHeadSQLSB.append(this.dataBaseTableName);
        insertHeadSQLSB.append(" (Mandator, Date_from, Date_to, Username");
        for (final DBFieldInterface<?> dbField : this.dbFieldList) {
          insertHeadSQLSB.append(", ");
          insertHeadSQLSB.append(dbField.preparedInsertReadPart());
        }
        insertHeadSQLSB.append(" ) VALUES ( ?, ");
        insertHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
        insertHeadSQLSB.append(", if (? , ");
        insertHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
        insertHeadSQLSB.append(", '9999-12-31 23:59:59.000'), ?");
        for (final DBFieldInterface<?> dbField : this.dbFieldList) {
          insertHeadSQLSB.append(", ");
          insertHeadSQLSB.append(dbField.preparedInsertValuesPart());
        }
        insertHeadSQLSB.append(");");
        this.insertHeadSQL = insertHeadSQLSB.toString();

        // build sql statement to insert a entry into db.
        final int sqlLengthInvalidate = 85;
        final StringBuilder invalidateHeadSQLSB = new StringBuilder(sqlLengthInvalidate);
        invalidateHeadSQLSB.append("UPDATE ");
        invalidateHeadSQLSB.append(this.dataBaseTableName);
        invalidateHeadSQLSB.append(" SET Date_to = ");
        invalidateHeadSQLSB.append(myDataBaseDepending.getSQLTimeOutdate());
        invalidateHeadSQLSB.append(" WHERE Mandator = ? ");
        invalidateHeadSQLSB.append(" AND ");
        invalidateHeadSQLSB.append(this.dbKeyField.getDBFieldName());
        invalidateHeadSQLSB.append(" < ?");
        invalidateHeadSQLSB.append(" AND Date_from <= ");
        invalidateHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
        invalidateHeadSQLSB.append(" AND Date_to > ");
        invalidateHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
        invalidateHeadSQLSB.append(';');
        this.invalidateHeadSQL = invalidateHeadSQLSB.toString();
      }
      ic.close();
    } catch (final SQLException e) {
      throw new UnexpectedException(e.toString(), e);
    } catch (final NamingException e) {
      throw new UnexpectedException(e.toString(), e);
    }
  }

  /**
   * <code>RiPhoneUser</code> is used to read the currently logged in user.
   *
   * @return logged in user
   * @throws UserNotLoggedInException if user is not logged in
   */
  protected final AbstractDomainUser getUser() throws UserNotLoggedInException {
    final HttpSession session = this.getThreadLocalRequest().getSession(true);

    AbstractDomainUser thisUser = null;
    if (session == null) {
      throw new UserNotLoggedInException();
    } else {
      thisUser = (AbstractDomainUser) session.getAttribute(this.sessionUser);
      if (thisUser == null) {
        throw new UserNotLoggedInException();
      }
    }

    return thisUser;
  }

  /**
   * <code>allowedToSee</code> checks if user is allowed to see entries.
   *
   * @return allowance to see true/false
   */
  protected abstract boolean allowedToSee();

  /**
   * <code>allowedToChange</code> checks if user is allowed to change entries.
   *
   * @return allowance to change true/false
   */
  protected abstract boolean allowedToChange();

  /**
   * <code>createDomainInstance</code> creates a new Instance.
   *
   * @return E
   */
  protected final E createDomainInstance() {
    try {
      return this.domainType.newInstance();
    } catch (final InstantiationException e) {
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <code>createKeyInstance</code> creates a new Instance.
   *
   * @return F
   */
  protected final F createKeyInstance() {
    try {
      return this.keyType.newInstance();
    } catch (final InstantiationException e) {
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <code>FillMinMax</code> method is called to fill the min and max entries of this database table for navigation.
   *
   * @param pDataBase Connection to the database
   * @param pMandator mandator to read from
   * @param pThisEntry structure to be filled with user data
   * @return the filled structure
   * @throws ServerErrorException thrown on errors on server
   */
  protected final E fillMinMax(final Connection pDataBase, final int pMandator, //
      final E pThisEntry) throws ServerErrorException {
    E returnEntry;
    if (pThisEntry == null) {
      returnEntry = this.createDomainInstance();
    } else {
      returnEntry = pThisEntry;
    }
    try (final PreparedStatement readMinMaxSQLStatement = pDataBase.prepareStatement(this.readMinMaxSQL)) {
      readMinMaxSQLStatement.clearParameters();
      readMinMaxSQLStatement.setInt(1, pMandator);
      try (final ResultSet result = readMinMaxSQLStatement.executeQuery()) {
        if (result.next()) {
          returnEntry.setKeyMin(result.getString("min"));
          returnEntry.setKeyMax(result.getString("max"));
          if (returnEntry.getKeyMax() == null) {
            // no entries in database
            throw new EmptyDataBaseException();
          }
        } else {
          throw new EmptyDataBaseException();
        }
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return returnEntry;
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
   * @throws SQLException if sql statement can not be created
   * @throws UserNotLoggedInException if user is not logged in
   */
  protected final String searchSQLSelect(final Connection pThisDataBase, final String pMinMax, final String pSearchField,
      final String pSearchMethodeEntry, final String pSearchFieldEntry, final String pDbKeyVGL, final String pDbKey)
      throws SQLException, UserNotLoggedInException {
    final int mandator = this.getUser().getMandator();
    final DataBaseDepending myDataBaseDepending = new DataBaseDepending(pThisDataBase.getMetaData().getDatabaseProductName());

    final int sqlLength = 87;
    final StringBuilder sqlString = new StringBuilder(sqlLength);
    sqlString.append( //
        "SELECT " + pMinMax + "(" + this.dbKeyField.getDBFieldName() + ") AS dbnumber " //
            + "FROM   " + this.dataBaseTableName + " " //
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = " + Integer.toString(mandator) + " " //
            + " AND   " + this.dbKeyField.getDBFieldName() + " " + pDbKeyVGL //
            + " " + StringToSQL.convertString(pDbKey, pThisDataBase.getMetaData().getDatabaseProductName()) + " " //
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= " + myDataBaseDepending.getSQLTimeNow() + " " //
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > " + myDataBaseDepending.getSQLTimeNow() + " " //
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
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pThisDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read (keyfield)
   * @param pThisEntry structure to be filled
   * @return the filled structure
   * @throws ServerErrorException thrown on errors on server
   */
  protected final E readOneEntry(final Connection pThisDataBase, final int pMandator, final String pEntry, final E pThisEntry)
      throws ServerErrorException {
    if (pThisEntry != null) {
      if (this.allowedToSee()) {
        try (final PreparedStatement readHeadSQLStatement = pThisDataBase.prepareStatement(this.readHeadSQL)) {
          // read entry
          readHeadSQLStatement.clearParameters();
          readHeadSQLStatement.setInt(1, pMandator);
          readHeadSQLStatement.setString(2, pEntry);
          try (final ResultSet resultHead = readHeadSQLStatement.executeQuery()) {
            if (resultHead.next()) {
              // read is ok, fill fields
              for (final DBFieldInterface<?> dbField : this.dbFieldList) {
                dbField.readFromResultSet(resultHead, pThisEntry.getFieldMap().get(dbField.getDBFieldName()));
              }
              pThisEntry.setKeyCur(pEntry);
            } else {
              // entry was not found
              throw new EntryNotFoundException(pEntry, EntryNotFoundException.ReadTyp.READ_EQUAL);
            }
          }
          pThisEntry.setIsReadOnly(!this.allowedToChange());
        } catch (final SQLException e) {
          e.printStackTrace();
          throw new ServerErrorException(e);
        }
      } else {
        // user is not allowed to read entry
        throw new UserNotAllowedException();
      }
    }
    return pThisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #readEntry(java.lang.String)
   */
  @Override
  public final E readEntry(final String pEntry) throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {
        thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
        thisEntry = this.readOneEntry(thisDataBase, mandator, pEntry, thisEntry);
      }
      ic.close();
    } catch (final SQLException e) {
      e.printStackTrace();
      thisEntry = this.createDomainInstance();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      thisEntry = this.createDomainInstance();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #findFirstEntry(java.lang.String, java.lang.String,
   * java.lang.String)
   */
  @Override
  public final E findFirstEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry)
      throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();
    try {
      if (StringUtils.isEmpty(pSearchFieldEntry)) {
        thisEntry = this.readFirstEntry();
      } else {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {

          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          String newEntry = thisEntry.getKeyMin();

          final String sqlString =
              this.searchSQLSelect(thisDataBase, "MIN", pSearchField, pSearchMethodEntry, pSearchFieldEntry, ">=", newEntry);
          try (final Statement statement = thisDataBase.createStatement()) {
            try (final ResultSet result = statement.executeQuery(sqlString)) {
              if (result.next()) {
                newEntry = result.getString("dbnumber");
                if (newEntry == null) {
                  // entry not found
                  throw new EntryNotFoundException(null, EntryNotFoundException.ReadTyp.SEARCH);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(null, EntryNotFoundException.ReadTyp.SEARCH);
              }
            }
          }

          thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
        }
        ic.close();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #findLastEntry(java.lang.String, java.lang.String,
   * java.lang.String)
   */
  @Override
  public final E findLastEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry)
      throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();
    try {
      if (StringUtils.isEmpty(pSearchFieldEntry)) {
        thisEntry = this.readFirstEntry();
      } else {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {
          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          String newEntry = thisEntry.getKeyMin();

          final String sqlString =
              this.searchSQLSelect(thisDataBase, "MAX", pSearchField, pSearchMethodEntry, pSearchFieldEntry, "<=", newEntry);
          try (final Statement statement = thisDataBase.createStatement()) {
            try (final ResultSet result = statement.executeQuery(sqlString)) {
              if (result.next()) {
                newEntry = result.getString("dbnumber");
                if (newEntry == null) {
                  // entry not found
                  throw new EntryNotFoundException(null, EntryNotFoundException.ReadTyp.SEARCH);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(null, EntryNotFoundException.ReadTyp.SEARCH);
              }
            }
          }

          thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
        }
        ic.close();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      thisEntry = this.createDomainInstance();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #findNextEntry(java.lang.String, java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public final E findNextEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry,
      final String pCurrentEntry) throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();
    try {
      if (StringUtils.isEmpty(pSearchFieldEntry)) {
        thisEntry = this.readNextEntry(pCurrentEntry);
      } else {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {

          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          String newEntry = pCurrentEntry;

          final String sqlString =
              this.searchSQLSelect(thisDataBase, "MIN", pSearchField, pSearchMethodEntry, pSearchFieldEntry, ">", newEntry);
          try (final Statement statement = thisDataBase.createStatement()) {
            try (final ResultSet result = statement.executeQuery(sqlString)) {
              if (result.next()) {
                newEntry = result.getString("dbnumber");
                if (newEntry == null) {
                  // entry not found
                  throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.SEARCH_NEXT);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.SEARCH_NEXT);
              }
            }
          }

          thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
        }
        ic.close();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #findPreviousEntry(java.lang.String, java.lang.String,
   * java.lang.String, java.lang.String)
   */
  @Override
  public final E findPreviousEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry,
      final String pCurrentEntry) throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();
    try {
      if (StringUtils.isEmpty(pSearchFieldEntry)) {
        thisEntry = this.readPreviousEntry(pCurrentEntry);
      } else {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {

          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          String newEntry = pCurrentEntry;

          final String sqlString =
              this.searchSQLSelect(thisDataBase, "MAX", pSearchField, pSearchMethodEntry, pSearchFieldEntry, "<", newEntry);
          try (final Statement statement = thisDataBase.createStatement()) {
            try (final ResultSet result = statement.executeQuery(sqlString)) {
              if (result.next()) {
                newEntry = result.getString("dbnumber");
                if (newEntry == null) {
                  // entry not found
                  throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.SEARCH_PREVIOUS);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.SEARCH_PREVIOUS);
              }
            }
          }

          thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
        }
        ic.close();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #readFirstEntry()
   */
  @Override
  public final E readFirstEntry() throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {
        thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
        thisEntry = this.readOneEntry(thisDataBase, mandator, thisEntry.getKeyMin(), thisEntry);
      }
      ic.close();
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final EntryNotFoundException e) {
      throw new EmptyDataBaseException(); // NOPMD, no need for trace
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #readLastEntry()
   */
  @Override
  public final E readLastEntry() throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();
    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {
        thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
        thisEntry = this.readOneEntry(thisDataBase, mandator, thisEntry.getKeyMax(), thisEntry);
      }
      ic.close();
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final EntryNotFoundException e) {
      throw new EmptyDataBaseException(); // NOPMD, no need for trace
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #readNextEntry(java.lang.String)
   */
  @Override
  public final E readNextEntry(final String pCurrentEntry) throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {
        thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
        String newEntryName = thisEntry.getKeyMax();
        if (StringUtils.isEmpty(pCurrentEntry)) {
          thisEntry = this.readFirstEntry();
        } else {
          try (final PreparedStatement readNextSQLStatement = thisDataBase.prepareStatement(this.readNextSQL)) {
            readNextSQLStatement.clearParameters();
            readNextSQLStatement.setInt(1, mandator);
            readNextSQLStatement.setString(2, pCurrentEntry);
            try (final ResultSet result = readNextSQLStatement.executeQuery()) {
              if (result.next()) {
                newEntryName = result.getString("dbnumber");
                if (newEntryName == null) {
                  // entry not found
                  throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.READ_NEXT);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.READ_NEXT);
              }
            }
          }
        }
        thisEntry = this.readOneEntry(thisDataBase, mandator, newEntryName, thisEntry);
      }
      ic.close();
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #readPreviousEntry(java.lang.String)
   */
  @Override
  public final E readPreviousEntry(final String pCurrentEntry) throws ServerErrorException {
    E thisEntry = null;
    final int mandator = this.getUser().getMandator();

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {
        thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
        String newEntryName = thisEntry.getKeyMax();
        if (StringUtils.isEmpty(pCurrentEntry)) {
          thisEntry = this.readLastEntry();
        } else {
          try (final PreparedStatement readPrevSQLStatement = thisDataBase.prepareStatement(this.readPrevSQL)) {
            readPrevSQLStatement.clearParameters();
            readPrevSQLStatement.setInt(1, mandator);
            readPrevSQLStatement.setString(2, pCurrentEntry);
            try (final ResultSet result = readPrevSQLStatement.executeQuery()) {
              if (result.next()) {
                newEntryName = result.getString("dbnumber");
                if (newEntryName == null) {
                  // entry not found
                  throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.READ_PREVIOUS);
                }
              } else {
                // entry not found
                throw new EntryNotFoundException(pCurrentEntry, EntryNotFoundException.ReadTyp.READ_PREVIOUS);
              }
            }
          }
        }
        thisEntry = this.readOneEntry(thisDataBase, mandator, newEntryName, thisEntry);
      }
      ic.close();
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return thisEntry;
  }

  /**
   * <code>insertEntry</code> inserts a entry into the database.
   *
   * @param pThisDataBase existing databas connection
   * @param pMandator mandator number
   * @param pUser username which saves
   * @param pSaveEntry entry to save
   * @param pDelete true if older entries should be deleted
   * @return effected database entries (should always be 1)
   * @throws SQLException when sql error occours
   */
  protected final int insertEntry(final Connection pThisDataBase, final int pMandator, final String pUser, final E pSaveEntry,
      final boolean pDelete) throws SQLException {
    int num = -1;
    try (final PreparedStatement insertHeadSQLStatement = pThisDataBase.prepareStatement(this.insertHeadSQL)) {
      insertHeadSQLStatement.clearParameters();
      int parmNum = 1;
      insertHeadSQLStatement.setInt(parmNum++, pMandator);
      insertHeadSQLStatement.setBoolean(parmNum++, pDelete);
      insertHeadSQLStatement.setString(parmNum++, pUser);
      for (final DBFieldInterface<?> dbField : this.dbFieldList) {
        parmNum =
            dbField.addToPreparedStatement(insertHeadSQLStatement, parmNum,
                pSaveEntry.getFieldMap().get(dbField.getDBFieldName()));
      }
      num = insertHeadSQLStatement.executeUpdate();
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
  protected final void saveEntry(final E pCurrentEntry, final E pDbEntry, final Connection pThisDataBase, final int pMandator,
      final String pUser, final String pSaveKeyString) throws SQLException {
    // Entry already exists in Database?
    if (!pCurrentEntry.equals(pDbEntry)) {
      try (final PreparedStatement invalidateHeadSQLStatement = pThisDataBase.prepareStatement(this.invalidateHeadSQL)) {
        invalidateHeadSQLStatement.clearParameters();
        invalidateHeadSQLStatement.setInt(1, pMandator);
        invalidateHeadSQLStatement.setString(2, pSaveKeyString);
        invalidateHeadSQLStatement.executeUpdate();
      }

      // Insert new entry
      this.insertEntry(pThisDataBase, pMandator, pUser, pCurrentEntry, false);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #saveEntry(de.knightsoft.dbnavigationbar.client.domain
   * .AbstractDataBaseDomain)
   */
  @Override
  public final E saveEntry(final E pCurrentEntry) throws ServerErrorException {
    final AbstractDomainUser thisUser = this.getUser();
    E returnEntry = pCurrentEntry;
    if (returnEntry.isFieldSetOk()) {
      final int mandator = thisUser.getMandator();
      final String user = thisUser.getUser();
      String saveKeyString = returnEntry.getKeyCur();
      if (StringUtils.isEmpty(saveKeyString)) {
        saveKeyString = returnEntry.getKeyNew();
      }

      try {
        if (this.allowedToChange()) {
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
          try (final Connection thisDataBase = lDataSource.getConnection()) {
            E dbEntry;
            try {
              dbEntry = this.fillMinMax(thisDataBase, mandator, null);
              dbEntry = this.readOneEntry(thisDataBase, mandator, saveKeyString, dbEntry);
            } catch (final EmptyDataBaseException e) {
              dbEntry = null;
            } catch (final EntryNotFoundException e) {
              dbEntry = null;
            }

            this.saveEntry(pCurrentEntry, dbEntry, thisDataBase, mandator, user, saveKeyString);

            returnEntry.setKeyCur(returnEntry.getKeyNew());
            this.fillMinMax(thisDataBase, mandator, returnEntry);
            returnEntry = this.readOneEntry(thisDataBase, mandator, returnEntry.getKeyNew(), returnEntry);
          }
          ic.close();
        } else {
          throw new UserNotAllowedException();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        throw new SaveDataBaseException(e);
      } catch (final NamingException e) {
        e.printStackTrace();
        throw new ServerErrorException(e);
      } catch (final EmptyDataBaseException e) {
        e.printStackTrace();
        throw new ServerErrorException(e);
      } catch (final EntryNotFoundException e) {
        e.printStackTrace();
        throw new ServerErrorException(e);
      }
    } else {
      throw new InvalidDataException();
    }
    return returnEntry;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.ui.AbstractDBRemoteService #deleteEntry(java.lang.String)
   */
  @Override
  public final E deleteEntry(final String pCurrentEntry) throws ServerErrorException {
    E resultValue = null;
    final AbstractDomainUser thisUser = this.getUser();
    final int mandator = thisUser.getMandator();
    final String user = thisUser.getUser();

    try {
      if (this.allowedToChange()) {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {
          E dbEntry;
          try {
            dbEntry = this.readEntry(pCurrentEntry);
          } catch (final EmptyDataBaseException e) {
            dbEntry = null;
          } catch (final EntryNotFoundException e) {
            dbEntry = null;
          }
          // invalidate head number
          try (final PreparedStatement invalidateHeadSQLStatement = thisDataBase.prepareStatement(this.invalidateHeadSQL)) {
            invalidateHeadSQLStatement.clearParameters();
            invalidateHeadSQLStatement.setInt(1, mandator);
            invalidateHeadSQLStatement.setString(2, pCurrentEntry);
            invalidateHeadSQLStatement.executeUpdate();
          }
          this.insertEntry(thisDataBase, mandator, user, dbEntry, true);
          try {
            resultValue = this.readNextEntry(pCurrentEntry);
          } catch (final EntryNotFoundException e) {
            resultValue = this.readLastEntry();
          }
        }
        ic.close();
      } else {
        throw new UserNotAllowedException();
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      throw new DeleteDataBaseException(e);
    } catch (final NamingException e) {
      e.printStackTrace();
      throw new ServerErrorException(e);
    }
    return resultValue;
  }
}
