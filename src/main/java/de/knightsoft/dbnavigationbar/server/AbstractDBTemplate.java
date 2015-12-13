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
import de.knightsoft.dbnavigationbar.client.domain.DomainDataBaseInterface;

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

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side implementation template for a simple database.
 *
 * @param <E> structure
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBTemplate<E extends DomainDataBaseInterface> extends XsrfProtectedServiceServlet implements
    DBTemplateInterface<E> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 6860424692927760239L;

  /**
   * class type.
   */
  private final Class<E> type;

  /**
   * name of the database table.
   */
  private final String dataBaseTableName;
  /**
   * name of the keyfield in the database.
   */
  private final String keyFieldName;

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
   * sql statement to read head entry.
   */
  private final String readHeadSQL;
  /**
   * sql statement to invalidate head entry.
   */
  private final String invalidateHeadSQL;
  /**
   * sql statement to insert a new head entry.
   */
  private final String insertHeadSQL;

  /**
   * database look up.
   */
  private final String lookUpDataBase;
  /**
   * name to store user data in the session.
   */
  private final String sessionUser;

  /**
   * Constructor, set up database connection.
   *
   * @param pType - class instance of E
   * @param pLookUpDataBase look up of the data base
   * @param pSessionUser user session key
   * @param pDataBaseTableName database table name
   * @param pKeyFieldName key field of the database
   * @param pInsertHeadSQL sql statement to insert a new head entry
   * @param pReadMinMaxSQL sql statement for min/max read
   * @param pReadNextSQL sql statement to read next key
   * @param pReadPrevSQL sql statement to read previous key
   * @param pReadHeadSQL sql statement to read head entry
   * @param pInvalidateHeadSQL sql statement to invalidate head entry
   */
  public AbstractDBTemplate(final Class<E> pType, final String pLookUpDataBase, final String pSessionUser,
      final String pDataBaseTableName, final String pKeyFieldName, final String pInsertHeadSQL, final String pReadMinMaxSQL,
      final String pReadNextSQL, final String pReadPrevSQL, final String pReadHeadSQL, final String pInvalidateHeadSQL) {
    super();
    this.type = pType;
    this.lookUpDataBase = pLookUpDataBase;
    this.dataBaseTableName = pDataBaseTableName;
    this.keyFieldName = pKeyFieldName;
    this.sessionUser = pSessionUser;

    try {
      // connect to database
      final InitialContext ic = new InitialContext();
      final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
      try (final Connection thisDataBase = lDataSource.getConnection()) {

        final DataBaseDepending myDataBaseDepending =
            new DataBaseDepending(thisDataBase.getMetaData().getDatabaseProductName());

        if (pReadMinMaxSQL == null) {
          this.readMinMaxSQL = null;
        } else {
          this.readMinMaxSQL =
              pReadMinMaxSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
        if (pReadNextSQL == null) {
          this.readNextSQL = null;
        } else {
          this.readNextSQL =
              pReadNextSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
        if (pReadPrevSQL == null) {
          this.readPrevSQL = null;
        } else {
          this.readPrevSQL =
              pReadPrevSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
        if (pReadHeadSQL == null) {
          this.readHeadSQL = null;
        } else {
          this.readHeadSQL =
              pReadHeadSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
        if (pInvalidateHeadSQL == null) {
          this.invalidateHeadSQL = null;
        } else {
          this.invalidateHeadSQL =
              pInvalidateHeadSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
                  myDataBaseDepending.getSQLTimeNow());
        }
        if (pInsertHeadSQL == null) {
          this.insertHeadSQL = null;
        } else {
          this.insertHeadSQL =
              pInsertHeadSQL.replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate()).replace("NOW()",
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
   * <code>createInstance</code> creates a new Instance.
   *
   * @return E
   */
  protected final E createInstance() {
    try {
      return this.type.newInstance();
    } catch (final InstantiationException e) {
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <code>fillHeadFromResultSet</code> set the fields in thisEntry from the given resultSet.
   *
   * @param pResultHead ResultSet to read Entries from
   * @param pThisEntry Entry to fill
   * @return filled Entry
   * @throws SQLException when error occurs
   */
  protected abstract E fillHeadFromResultSet(ResultSet pResultHead, E pThisEntry) throws SQLException;

  /**
   * <code>RiPhoneUser</code> is used to read the currently logged in user.
   *
   * @return logged in user
   */
  protected final AbstractDomainUser getUser() {
    final HttpSession session = this.getThreadLocalRequest().getSession(true);

    AbstractDomainUser thisUser = null;
    if (session != null) {
      thisUser = (AbstractDomainUser) session.getAttribute(this.sessionUser);
    }

    return thisUser;
  }

  /**
   * <code>FillMinMax</code> method is called to fill the min and max entries of this database table for navigation.
   *
   * @param pThisDataBase Connection to the database
   * @param pMandator mandator to read from
   * @param pThisEntry structure to be filled with user data
   * @return the filled structure
   */
  protected final E fillMinMax(final Connection pThisDataBase, final int pMandator, final E pThisEntry) {
    E returnEntry = pThisEntry;
    try (final PreparedStatement readMinMaxSQLStatement = pThisDataBase.prepareStatement(this.readMinMaxSQL)) {
      readMinMaxSQLStatement.clearParameters();
      readMinMaxSQLStatement.setInt(1, pMandator);
      try (final ResultSet result = readMinMaxSQLStatement.executeQuery()) {

        if (result.next()) {
          returnEntry.setKeyMin(result.getString("min"));
          returnEntry.setKeyMax(result.getString("max"));
        }
      }

    } catch (final SQLException e) {
      returnEntry = null;
    }
    return returnEntry;
  }

  /**
   * <code>searchSQLSelect</code> setup a part of the sql statement to search for a user.
   *
   * @param pDataBase Database connection
   * @param pMinMax "MIN" or "MAX" used for PhoneNumber
   * @param pSearchField Field to search for
   * @param pSearchMethodEntry compare method for search ("<", "<=", "=", ">", ">=" or "like")
   * @param pSearchFieldEntry value to search for
   * @param pDbKeyVGL compare method of phone number ("<", "<=", "=", ">", ">=" or "like")
   * @param pDbKey comparison number
   * @return SQL-String
   * @throws SQLException when error occurs
   */
  protected abstract String searchSQLSelect(final Connection pDataBase, final String pMinMax, final String pSearchField,
      final String pSearchMethodEntry, final String pSearchFieldEntry, final String pDbKeyVGL, final String pDbKey)
      throws SQLException;

  /**
   * <code>findFirstEntry</code> is called to search for the first entry which fulfills the search parameters.
   *
   * @param pSearchField input field to search for
   * @param pSearchMethodEntry input search method
   * @param pSearchFieldEntry input of search field entry field
   * @return the found entry or null if none is found
   */
  @Override
  public final E findFirstEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      try {
        if (pSearchFieldEntry == null || "".equals(pSearchFieldEntry)) {
          thisEntry = this.readFirstEntry();
        } else {
          thisEntry = this.createInstance();
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);

          try (final Connection thisDataBase = lDataSource.getConnection()) {
            thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
            String newEntry = thisEntry.getKeyMin();

            final String sqlString =
                this.searchSQLSelect(thisDataBase, "MIN", pSearchField, pSearchMethodEntry, pSearchFieldEntry, ">=", newEntry);
            if (sqlString == null) {
              newEntry = null;
            } else {
              Statement statement = null;
              ResultSet result = null;
              try {
                statement = thisDataBase.createStatement();
                result = statement.executeQuery(sqlString);
                if (result.next()) {
                  newEntry = result.getString("dbnumber");
                } else {
                  newEntry = null;
                }
              } finally {
                if (result != null) {
                  result.close();
                }
                if (statement != null) {
                  statement.close();
                }
              }
            }

            if (newEntry == null) {
              thisEntry.setKeyCur(null);
            } else {
              thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
            }
          }
          ic.close();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>findLastEntry</code> is called to search for the last entry which fulfills the search parameters.
   *
   * @param pSearchField input field to search for
   * @param pSearchMethodEntry input search method
   * @param pSearchFieldEntry input of search field entry field
   * @return the found entry or null if none is found
   */
  @Override
  public final E findLastEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      try {
        if (StringUtils.isEmpty(pSearchFieldEntry)) {
          thisEntry = this.readFirstEntry();
        } else {
          thisEntry = this.createInstance();
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
          try (final Connection thisDataBase = lDataSource.getConnection()) {

            thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
            String newEntry = thisEntry.getKeyMax();

            final String sqlString =
                this.searchSQLSelect(thisDataBase, "MAX", pSearchField, pSearchMethodEntry, pSearchFieldEntry, "<=", newEntry);
            if (sqlString == null) {
              newEntry = null;
            } else {
              try (final Statement statement = thisDataBase.createStatement()) {
                try (final ResultSet result = statement.executeQuery(sqlString)) {
                  if (result.next()) {
                    newEntry = result.getString("dbnumber");
                  } else {
                    newEntry = null;
                  }
                }
              }
            }

            if (newEntry == null) {
              thisEntry.setKeyCur(null);
            } else {
              thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
            }
          }
          ic.close();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>findNextEntry</code> is called to search for the next entry which fulfills the search parameters.
   *
   * @param pSearchField input field to search for
   * @param pSearchMethodEntry input search method
   * @param pSearchFieldEntry input of search field entry field
   * @param pCurrentEntry key of the current entry
   * @return the found entry or null if none is found
   */
  @Override
  public final E findNextEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry,
      final String pCurrentEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      try {
        if (pSearchFieldEntry == null || "".equals(pSearchFieldEntry)) {
          thisEntry = this.readFirstEntry();
        } else {
          thisEntry = this.createInstance();
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
          try (final Connection thisDataBase = lDataSource.getConnection()) {

            thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
            String newEntry = pCurrentEntry;

            final String sqlString =
                this.searchSQLSelect(thisDataBase, "MIN", pSearchField, pSearchMethodEntry, pSearchFieldEntry, ">", newEntry);
            if (sqlString == null) {
              newEntry = null;
            } else {
              Statement statement = null;
              ResultSet result = null;
              try {
                statement = thisDataBase.createStatement();
                result = statement.executeQuery(sqlString);
                if (result.next()) {
                  newEntry = result.getString("dbnumber");
                } else {
                  newEntry = null;
                }
              } finally {
                if (result != null) {
                  result.close();
                }
                if (statement != null) {
                  statement.close();
                }
              }
            }

            if (newEntry == null) {
              thisEntry.setKeyCur(null);
            } else {
              thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
            }
          }
          ic.close();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>findPreviousEntry</code> is called to search for the previous entry which fulfills the search parameters.
   *
   * @param pSearchField input field to search for
   * @param pSearchMethodEntry input search method
   * @param pSearchFieldEntry input of search field entry field
   * @param pCurrentEntry key of the current entry
   * @return the found entry or null if none is found
   */
  @Override
  public final E findPreviousEntry(final String pSearchField, final String pSearchMethodEntry, final String pSearchFieldEntry,
      final String pCurrentEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      try {
        if (pSearchFieldEntry == null || "".equals(pSearchFieldEntry)) {
          thisEntry = this.readFirstEntry();
        } else {
          thisEntry = this.createInstance();
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
          try (final Connection thisDataBase = lDataSource.getConnection()) {

            thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
            String newEntry = pCurrentEntry;

            final String sqlString =
                this.searchSQLSelect(thisDataBase, "MAX", pSearchField, pSearchMethodEntry, pSearchFieldEntry, "<", newEntry);
            if (sqlString == null) {
              newEntry = null;
            } else {
              Statement statement = null;
              ResultSet result = null;
              try {
                statement = thisDataBase.createStatement();
                result = statement.executeQuery(sqlString);
                if (result.next()) {
                  newEntry = result.getString("dbnumber");
                } else {
                  newEntry = null;
                }
              } finally {
                if (result != null) {
                  result.close();
                }
                if (statement != null) {
                  statement.close();
                }
              }
            }

            if (newEntry == null) {
              thisEntry.setKeyCur(null);
            } else {
              thisEntry = this.readOneEntry(thisDataBase, mandator, newEntry, thisEntry);
            }
          }
          ic.close();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read
   * @param pThisEntry structure to be filled
   * @return the filled structure
   */
  protected final E readHeadEntry(final Connection pDataBase, final int pMandator, final String pEntry, final E pThisEntry) {
    E returnEntry = pThisEntry;
    try {
      if (pThisEntry != null && this.allowedToSee()) {
        returnEntry.setIsReadOnly(!this.allowedToChange());
        returnEntry.setKeyCur(pEntry);
        try (final PreparedStatement readHeadSQLStatement = pDataBase.prepareStatement(this.readHeadSQL)) {
          readHeadSQLStatement.clearParameters();
          readHeadSQLStatement.setInt(1, pMandator);
          readHeadSQLStatement.setString(2, pEntry);
          try (final ResultSet resultHead = readHeadSQLStatement.executeQuery()) {
            if (resultHead.next()) {
              returnEntry = this.fillHeadFromResultSet(resultHead, returnEntry);
            } else {
              returnEntry.setKeyCur(null);
            }
          }
        }
      } else {
        returnEntry = null;
      }
    } catch (final SQLException e) {
      e.printStackTrace();
      returnEntry = null;
    }
    return returnEntry;
  }

  /**
   * <code>readOneEntry</code> is used to read a given entry from database.
   *
   * @param pDataBase Database Connection
   * @param pMandator mandator is a keyfield
   * @param pEntry the Entry to read
   * @param pThisEntry structure to be filled
   * @return the filled structure
   */
  protected abstract E readOneEntry(final Connection pDataBase, final int pMandator, final String pEntry, final E pThisEntry);

  /**
   * <code>readEntry</code> is used to read a given entry from database.
   *
   * @param pEntry the entry to read
   * @return the filled structure
   */
  @Override
  public final E readEntry(final String pEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();

      try {
        thisEntry = this.createInstance();
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {
          if (thisEntry != null) {
            thisEntry = this.readOneEntry(thisDataBase, mandator, pEntry, thisEntry);
          }
          if (thisEntry != null) {
            thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          }
        }
        ic.close();
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>readFirstEntry</code> is used to read the first entry from database.
   *
   * @return the filled structure
   */
  @Override
  public final E readFirstEntry() {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();

      try {
        thisEntry = this.createInstance();
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {
          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          if (thisEntry == null) {
            thisEntry = null;
          } else {
            thisEntry = this.readOneEntry(thisDataBase, mandator, thisEntry.getKeyMin(), thisEntry);
          }
        }
        ic.close();
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>readLastEntry</code> is used to read the last entry from database.
   *
   * @return the filled structure
   */
  @Override
  public final E readLastEntry() {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      thisEntry = this.createInstance();
      try {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {
          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          if (thisEntry != null) {
            thisEntry = this.readOneEntry(thisDataBase, mandator, thisEntry.getKeyMax(), thisEntry);
          }
        }
        ic.close();
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>readNextEntry</code> is used to read the next entry from database.
   *
   * @param pCurrentEntry the currently displayed entry
   * @return the filled structure
   */
  @Override
  public final E readNextEntry(final String pCurrentEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      thisEntry = this.createInstance();

      try {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);

        try (final Connection thisDataBase = lDataSource.getConnection()) {

          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          if (thisEntry != null) {
            String newEntryName = thisEntry.getKeyMax();
            if (StringUtils.isNotEmpty(pCurrentEntry)) {
              try (final PreparedStatement readNextSQLStatement = thisDataBase.prepareStatement(this.readNextSQL)) {
                readNextSQLStatement.clearParameters();
                readNextSQLStatement.setInt(1, mandator);
                readNextSQLStatement.setString(2, pCurrentEntry);
                try (final ResultSet result = readNextSQLStatement.executeQuery()) {
                  if (result.next()) {
                    newEntryName = result.getString("dbnumber");
                  }
                }
              }
            }
            thisEntry = this.readOneEntry(thisDataBase, mandator, newEntryName, thisEntry);
          }
        }
        ic.close();
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>readPreviousEntry</code> is used to read the previous entry from database.
   *
   * @param pCurrentEntry the currently displayed entry
   * @return the filled user structure
   */
  @Override
  public final E readPreviousEntry(final String pCurrentEntry) {
    E thisEntry = null;
    final AbstractDomainUser thisUser = this.getUser();
    if (thisUser == null) {
      thisEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      thisEntry = this.createInstance();

      try {
        // connect to database
        final InitialContext ic = new InitialContext();
        final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
        try (final Connection thisDataBase = lDataSource.getConnection()) {

          thisEntry = this.fillMinMax(thisDataBase, mandator, thisEntry);
          if (thisEntry != null) {
            String newEntryName = thisEntry.getKeyMin();
            if (StringUtils.isNotEmpty(pCurrentEntry)) {
              try (final PreparedStatement readPrevSQLStatement = thisDataBase.prepareStatement(this.readPrevSQL)) {
                readPrevSQLStatement.clearParameters();
                readPrevSQLStatement.setInt(1, mandator);
                readPrevSQLStatement.setString(2, pCurrentEntry);
                try (final ResultSet result = readPrevSQLStatement.executeQuery()) {
                  if (result.next()) {
                    newEntryName = result.getString("dbnumber");
                  }
                }
              }
            }
            thisEntry = this.readOneEntry(thisDataBase, mandator, newEntryName, thisEntry);
          }
        }
        ic.close();
      } catch (final SQLException e) {
        e.printStackTrace();
        thisEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        thisEntry = null;
      }
    }
    return thisEntry;
  }

  /**
   * <code>insertEntry</code> inserts a entry into the database.
   *
   * @param pDataBase existing databas connection
   * @param pMandator mandator number
   * @param pUser username which saves
   * @param pSaveEntry entry to save
   * @param pDelete true if older entries should be deleted
   * @return effected database entries (should always be 1)
   * @throws SQLException when sql error occours
   */
  protected final int insertEntry(final Connection pDataBase, final int pMandator, final String pUser, final E pSaveEntry,
      final boolean pDelete) throws SQLException {
    int num = -1;
    try (final PreparedStatement insertHeadSQLStatement = pDataBase.prepareStatement(this.insertHeadSQL)) {
      insertHeadSQLStatement.clearParameters();
      this.fillInsertHead(insertHeadSQLStatement, pMandator, pUser, pSaveEntry, pDelete);
      num = insertHeadSQLStatement.executeUpdate();
    }
    return num;
  }

  /**
   * blank to null is a method to set texts to null if no entry is inside.
   *
   * @param pSaveText text to save
   * @return updated text
   */
  protected final String blankToNull(final String pSaveText) {
    if (StringUtils.isEmpty(pSaveText)) {
      return null;
    } else {
      return pSaveText;
    }
  }

  /**
   * <code>saveEntry</code> saves or inserts a entry to database.
   *
   * @param pCurrentEntry entry that has to be saved
   * @return entry after saving
   */
  @Override
  public final E saveEntry(final E pCurrentEntry) {
    final AbstractDomainUser thisUser = this.getUser();
    E returnEntry = pCurrentEntry;
    if (thisUser == null) {
      returnEntry = null;
    } else {
      final int mandator = thisUser.getMandator();
      final String user = thisUser.getUser();
      String saveKeyString = returnEntry.getKeyCur();
      if (saveKeyString == null || "".equals(saveKeyString)) {
        saveKeyString = returnEntry.getKeyNew();
      }

      try {
        if (this.allowedToChange()) {
          E dbEntry = this.createInstance();
          // connect to database
          final InitialContext ic = new InitialContext();
          final DataSource lDataSource = (DataSource) ic.lookup(this.lookUpDataBase);
          try (final Connection thisDataBase = lDataSource.getConnection()) {

            dbEntry = this.readOneEntry(thisDataBase, mandator, saveKeyString, dbEntry);
            if ((dbEntry != null) && (dbEntry.getKeyCur() == null)) {
              dbEntry = null;
            }

            this.saveEntry(pCurrentEntry, dbEntry, thisDataBase, mandator, user, saveKeyString);

            returnEntry.setKeyCur(returnEntry.getKeyNew());
            this.fillMinMax(thisDataBase, mandator, returnEntry);
            returnEntry = this.readOneEntry(thisDataBase, mandator, returnEntry.getKeyNew(), returnEntry);
          }
          ic.close();
        }
      } catch (final SQLException e) {
        e.printStackTrace();
        returnEntry = null;
      } catch (final NamingException e) {
        e.printStackTrace();
        returnEntry = null;
      }
    }
    return returnEntry;
  }

  /**
   * <code>saveEntry</code> saves or inserts a entry to database.
   *
   * @param pCurrentEntry entry that has to be saved
   * @param pDbEntry entry from database to compare
   * @param pDataBase database connection
   * @param pMandator mandator number
   * @param pUser name of the user
   * @param pSaveKeyString key of the entry to save
   * @throws SQLException if something's going wrong
   */
  protected abstract void saveEntry(final E pCurrentEntry, final E pDbEntry, final Connection pDataBase, final int pMandator,
      final String pUser, final String pSaveKeyString) throws SQLException;

  /**
   * get database table name.
   *
   * @return the dataBaseTableName
   */
  protected final String getDataBaseTableName() {
    return this.dataBaseTableName;
  }

  /**
   * get key field name.
   *
   * @return the keyFieldName
   */
  protected final String getKeyFieldName() {
    return this.keyFieldName;
  }

  /**
   * get read min/max sql string.
   *
   * @return the readMinMaxSQL
   */
  protected final String getReadMinMaxSQL() {
    return this.readMinMaxSQL;
  }

  /**
   * get read next sql string.
   *
   * @return the readNextSQL
   */
  protected final String getReadNextSQL() {
    return this.readNextSQL;
  }

  /**
   * get read previous sql string.
   *
   * @return the readPrevSQL
   */
  protected final String getReadPrevSQL() {
    return this.readPrevSQL;
  }

  /**
   * get read head sql string.
   *
   * @return the readHeadSQL
   */
  protected final String getReadHeadSQL() {
    return this.readHeadSQL;
  }

  /**
   * get invalidate head sql string.
   *
   * @return the invalidateHeadSQL
   */
  protected final String getInvalidateHeadSQL() {
    return this.invalidateHeadSQL;
  }

  /**
   * get insert head sql string.
   *
   * @return the insertHeadSQL
   */
  protected final String getInsertHeadSQL() {
    return this.insertHeadSQL;
  }

  /**
   * get lookup database.
   *
   * @return the lookUpDataBase
   */
  protected final String getLookUpDataBase() {
    return this.lookUpDataBase;
  }
}
