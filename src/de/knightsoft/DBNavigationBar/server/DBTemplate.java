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
 *  Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

import de.knightsoft.DBNavigationBar.client.domain.DomainDataBaseInterface;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;

/**
 *
 * The <code>RiPhoneDBHeadDateTemplate</code> class is the server side
 * implementation template for a simple database.
 *
 * @param <E> structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-07-23
 */
public abstract class DBTemplate<E extends DomainDataBaseInterface>
    extends XsrfProtectedServiceServlet
    implements DBTemplateInterface<E> {

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
     * @param setType - class instance of E
     * @param setLookUpDataBase
     *          look up of the data base
     * @param setSessionUser
     *          user session key
     * @param setDataBaseTableName
     *          database table name
     * @param setKeyFieldName
     *          key field of the database
     * @param setInsertHeadSQL
     *          sql statement to insert a new head entry
     * @param setReadMinMaxSQL
     *          sql statement for min/max read
     * @param setReadNextSQL
     *          sql statement to read next key
     * @param setReadPrevSQL
     *          sql statement to read previous key
     * @param setReadHeadSQL
     *          sql statement to read head entry
     * @param setInvalidateHeadSQL
     *          sql statement to invalidate head entry
     */
    public DBTemplate(
            final Class<E> setType,
            final String setLookUpDataBase,
            final String setSessionUser,
            final String setDataBaseTableName,
            final String setKeyFieldName,
            final String setInsertHeadSQL,
            final String setReadMinMaxSQL,
            final String setReadNextSQL,
            final String setReadPrevSQL,
            final String setReadHeadSQL,
            final String setInvalidateHeadSQL
          ) {
        this.type               = setType;
        this.lookUpDataBase     = setLookUpDataBase;
        this.dataBaseTableName  = setDataBaseTableName;
        this.keyFieldName       = setKeyFieldName;
        this.sessionUser        = setSessionUser;
        Connection thisDataBase = null;

        try {
            // connect to database
            InitialContext ic       =    new InitialContext();
            DataSource lDataSource  =    (DataSource) ic.lookup(
                    this.lookUpDataBase);
            thisDataBase            =    lDataSource.getConnection();
            ic.close();

            DataBaseDepending myDataBaseDepending =
                    new DataBaseDepending(thisDataBase.getMetaData()
                            .getDatabaseProductName());

            if (setReadMinMaxSQL == null) {
                this.readMinMaxSQL = null;
            } else {
                this.readMinMaxSQL = setReadMinMaxSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
            if (setReadNextSQL == null) {
                this.readNextSQL = null;
            } else {
                this.readNextSQL = setReadNextSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
            if (setReadPrevSQL == null) {
                this.readPrevSQL = null;
            } else {
                this.readPrevSQL        = setReadPrevSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
            if (setReadHeadSQL == null) {
                this.readHeadSQL = null;
            } else {
                this.readHeadSQL = setReadHeadSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
            if (setInvalidateHeadSQL == null) {
                this.invalidateHeadSQL = null;
            } else {
                this.invalidateHeadSQL = setInvalidateHeadSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
            if (setInsertHeadSQL == null) {
                this.insertHeadSQL = null;
            } else {
                this.insertHeadSQL = setInsertHeadSQL
                .replace("OUTDATE()", myDataBaseDepending.getSQLTimeOutdate())
                .replace("NOW()", myDataBaseDepending.getSQLTimeNow());
            }
        } catch (Exception e) {
            throw new UnexpectedException(e.toString(), e.getCause());
        } finally {
            try {
                if (thisDataBase != null) {
                    thisDataBase.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * <code>createInstance</code> creates a new Instance.
     * @return E
     */
    protected final E createInstance() {
        try {
            return this.type.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <code>fillHeadFromResultSet</code> set the fields in thisEntry from
     * the given resultSet.
     * @param resultHead
     *             ResultSet to read Entries from
     * @param thisEntry
     *             Entry to fill
     * @return filled Entry
     * @throws Exception when error occurs
     */
    protected abstract E fillHeadFromResultSet(ResultSet resultHead,
            E thisEntry) throws Exception;


    /**
     * <code>RiPhoneUser</code> is used to read the currently logged in user.
     *
     * @return logged in user
     */
    protected final DomainUser getUser() {
        HttpSession session =
                this.getThreadLocalRequest().getSession(true);

        DomainUser thisUser = null;
        if (session != null) {
            thisUser =
                (DomainUser) session.getAttribute(this.sessionUser);
        }

        return thisUser;
    }

    /**
     * <code>FillMinMax</code> method is called to fill the min and max
     * entries of this database table for navigation.
     *
     * @param thisDataBase
     *             Connection to the database
     * @param mandator
     *             mandator to read from
     * @param thisEntry
     *             structure to be filled with user data
     * @return the filled structure
     * @throws SQLException
     */
    protected final E fillMinMax(
            final Connection thisDataBase,
            final int mandator,
            final E thisEntry
          ) {
        PreparedStatement readMinMaxSQLStatement    =    null;
        E returnEntry = thisEntry;
        try {
            readMinMaxSQLStatement = thisDataBase.prepareStatement(
                    this.readMinMaxSQL);
            readMinMaxSQLStatement.clearParameters();
            readMinMaxSQLStatement.setInt(1, mandator);
            ResultSet result = readMinMaxSQLStatement.executeQuery();

            if (result.next()) {
                returnEntry.setKeyMin(result.getString("min"));
                returnEntry.setKeyMax(result.getString("max"));
            }
            result.close();

        } catch (SQLException e) {
            returnEntry    =    null;
        } finally {
            try {
                if (readMinMaxSQLStatement != null) {
                    readMinMaxSQLStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnEntry;
    }



    /**
     * <code>searchSQLSelect</code> setup a part of the
     * sql statement to search for a user.
     *
     * @param thisDataBase
     *             Database connection
     * @param minMax
     *             "MIN" or "MAX" used for PhoneNumber
     * @param searchField
     *             Field to search for
     * @param searchMethodEntry
     *             compare method for search
     *             ("<", "<=", "=", ">", ">=" or "like")
     * @param searchFieldEntry
     *             value to search for
     * @param dbKeyVGL
     *             compare method of phone number
     *             ("<", "<=", "=", ">", ">=" or "like")
     * @param dbKey
     *             comparison number
     * @return SQL-String
     * @throws Exception when error occurs
     */
    protected abstract String searchSQLSelect(
            final Connection thisDataBase,
            final String minMax,
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry,
            final String dbKeyVGL,
            final String dbKey) throws Exception;

    /**
     * <code>findFirstEntry</code> is called to search for the
     * first entry which fulfills the search parameters.
     *
     * @param searchField
     *         input field to search for
     * @param searchMethodEntry
     *         input search method
     * @param searchFieldEntry
     *         input of search field entry field
     * @return the found entry or null if none is found
     */
    @Override
    public final E findFirstEntry(
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser !=    null) {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    thisEntry = createInstance();
                    // connect to database
                    InitialContext ic =    new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry        =    thisEntry.getKeyMin();

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MIN", searchField, searchMethodEntry,
                            searchFieldEntry, ">=", newEntry);
                    if (sqlString != null) {
                        Statement statement = null;
                        ResultSet result    = null;
                        try {
                            statement = thisDataBase.createStatement();
                            result = statement.executeQuery(sqlString);
                            if (result.next()) {
                                newEntry    =    result.getString("dbnumber");
                            } else {
                                newEntry    =    null;
                            }
                        } finally {
                            if (result != null) {
                                result.close();
                            }
                            if (statement != null) {
                                statement.close();
                            }
                        }
                    } else {
                        newEntry    =    null;
                    }

                    if (newEntry != null) {
                        thisEntry = readOneEntry(thisDataBase,
                                mandator, newEntry, thisEntry);
                    } else {
                        thisEntry.setKeyCur(null);
                    }
                }
            } catch (Exception e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>findLastEntry</code> is called to search for the
     * last entry which fulfills the search parameters.
     *
     * @param searchField
     *         input field to search for
     * @param searchMethodEntry
     *         input search method
     * @param searchFieldEntry
     *         input of search field entry field
     * @return the found entry or null if none is found
     */
    @Override
    public final E findLastEntry(final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser != null) {
            int mandator = thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null
                 || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    thisEntry = createInstance();
                    // connect to database
                    InitialContext ic = new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(this.lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry         = thisEntry.getKeyMax();

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MAX", searchField, searchMethodEntry,
                            searchFieldEntry, "<=", newEntry);
                    if (sqlString != null) {
                        Statement statement = null;
                        ResultSet result    = null;
                        try {
                            statement = thisDataBase.createStatement();
                            result = statement.executeQuery(sqlString);
                            if (result.next()) {
                                newEntry    =    result.getString("dbnumber");
                            } else {
                                newEntry    =    null;
                            }
                        } finally {
                            if (result != null) {
                                result.close();
                            }
                            if (statement != null) {
                                statement.close();
                            }
                        }
                    } else {
                        newEntry    =    null;
                    }

                    if (newEntry != null) {
                        thisEntry = readOneEntry(thisDataBase, mandator,
                                newEntry, thisEntry);
                    } else {
                        thisEntry.setKeyCur(null);
                    }
                }
            } catch (Exception e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>findNextEntry</code> is called to search for the
     * next entry which fulfills the search parameters.
     *
     * @param searchField
     *         input field to search for
     * @param searchMethodEntry
     *         input search method
     * @param searchFieldEntry
     *         input of search field entry field
     * @param currentEntry
     *         key of the current entry
     * @return the found entry or null if none is found
     */
    @Override
    public final E findNextEntry(final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry,
            final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser != null) {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null
                 || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    thisEntry = createInstance();
                    // connect to database
                    InitialContext ic  =    new InitialContext();
                    DataSource lDataSource  =
                            (DataSource) ic.lookup(this.lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry =    currentEntry;

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MIN", searchField, searchMethodEntry,
                            searchFieldEntry, ">", newEntry);
                    if (sqlString != null) {
                        Statement statement = null;
                        ResultSet result    = null;
                        try {
                            statement = thisDataBase.createStatement();
                            result = statement.executeQuery(sqlString);
                            if (result.next()) {
                                newEntry    =    result.getString("dbnumber");
                            } else {
                                newEntry    =    null;
                            }
                        } finally {
                            if (result != null) {
                                result.close();
                            }
                            if (statement != null) {
                                statement.close();
                            }
                        }
                    } else {
                        newEntry    =    null;
                    }

                    if (newEntry != null) {
                        thisEntry = readOneEntry(thisDataBase, mandator,
                                newEntry, thisEntry);
                    } else {
                        thisEntry.setKeyCur(null);
                    }
                }
            } catch (Exception e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>findPreviousEntry</code> is called to search for the
     * previous entry which fulfills the search parameters.
     *
     * @param searchField
     *         input field to search for
     * @param searchMethodEntry
     *         input search method
     * @param searchFieldEntry
     *         input of search field entry field
     * @param currentEntry
     *         key of the current entry
     * @return the found entry or null if none is found
     */
    @Override
    public final E findPreviousEntry(final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry,
            final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser != null) {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase = null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    thisEntry = createInstance();
                    // connect to database
                    InitialContext ic = new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(this.lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry =    currentEntry;

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MAX", searchField, searchMethodEntry,
                            searchFieldEntry, "<", newEntry);
                    if (sqlString != null) {
                        Statement statement = null;
                        ResultSet result    = null;
                        try {
                            statement = thisDataBase.createStatement();
                            result = statement.executeQuery(sqlString);
                            if (result.next()) {
                                newEntry    =    result.getString("dbnumber");
                            } else {
                                newEntry    =    null;
                            }
                        } finally {
                            if (result != null) {
                                result.close();
                            }
                            if (statement != null) {
                                statement.close();
                            }
                        }
                    } else {
                        newEntry    =    null;
                    }

                    if (newEntry != null) {
                        thisEntry = readOneEntry(thisDataBase, mandator,
                                newEntry, thisEntry);
                    } else {
                        thisEntry.setKeyCur(null);
                    }
                }
            } catch (Exception e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>readOneEntry</code> is used to read a
     * given entry from database.
     *
     * @param thisDataBase
     *             Database Connection
     * @param mandator
     *             mandator is a keyfield
     * @param entry
     *             the Entry to read
     * @param thisEntry
     *             structure to be filled
     * @return the filled structure
     * @throws SQLException
     */
    protected final E readHeadEntry(final Connection thisDataBase,
            final int mandator, final String entry,
            final E thisEntry) {
        PreparedStatement readHeadSQLStatement    =    null;
        E returnEntry = thisEntry;
        try {
            if (thisEntry != null && allowedToSee()) {
                returnEntry.setIsReadOnly(!allowedToChange());
                returnEntry.setKeyCur(entry);
                readHeadSQLStatement =
                        thisDataBase.prepareStatement(readHeadSQL);
                readHeadSQLStatement.clearParameters();
                readHeadSQLStatement.setInt(1, mandator);
                readHeadSQLStatement.setString(2, entry);
                ResultSet resultHead =
                        readHeadSQLStatement.executeQuery();
                if (resultHead.next()) {
                    returnEntry = fillHeadFromResultSet(resultHead,
                            returnEntry);
                } else {
                    returnEntry.setKeyCur(null);
                }
                resultHead.close();
            } else {
                returnEntry    =    null;
            }
        } catch (Exception nef) {
            returnEntry    =    null;
        } finally {
            try {
                if (readHeadSQLStatement != null) {
                    readHeadSQLStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnEntry;
    }

    /**
     * <code>readOneEntry</code> is used to read a
     * given entry from database.
     *
     * @param thisDataBase
     *             Database Connection
     * @param mandator
     *             mandator is a keyfield
     * @param entry
     *             the Entry to read
     * @param thisEntry
     *             structure to be filled
     * @return the filled structure
     * @throws SQLException
     */
    protected abstract E readOneEntry(final Connection thisDataBase,
            final int mandator, final String entry,
            final E thisEntry);

    /**
     * <code>readEntry</code> is used to read a
     * given entry from database.
     *
     * @param entry
     *             the entry to read
     * @return the filled structure
     */
    @Override
    public final E readEntry(final String entry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser != null) {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;

            try {
                thisEntry = createInstance();
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase =    lDataSource.getConnection();
                ic.close();
                if (thisEntry    !=    null) {
                    thisEntry    =    readOneEntry(thisDataBase,
                            mandator, entry, thisEntry);
                }
                if (thisEntry    !=    null) {
                    thisEntry    =    fillMinMax(thisDataBase,
                            mandator, thisEntry);
                }
            } catch (SQLException e) {
                thisEntry    =    null;
            } catch (NamingException e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>readFirstEntry</code> is used to read the
     * first entry from database.
     *
     * @return the filled structure
     */
    @Override
    public final E readFirstEntry() {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser !=    null) {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;

            try {
                thisEntry = createInstance();
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                if (thisEntry    !=    null) {
                    thisEntry    =    readOneEntry(thisDataBase,
                            mandator, thisEntry.getKeyMin(), thisEntry);
                } else {
                    thisEntry    =    null;
                }
            } catch (SQLException e) {
                thisEntry    =    null;
            } catch (NamingException e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>readLastEntry</code> is used to read the
     * last entry from database.
     *
     * @return the filled structure
     */
    @Override
    public final E readLastEntry() {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser !=    null) {
            int mandator = thisUser.getMandator();
            thisEntry = createInstance();
            Connection thisDataBase = null;
            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase,
                        mandator, thisEntry);
                if (thisEntry    !=    null) {
                    thisEntry    =    readOneEntry(thisDataBase,
                            mandator, thisEntry.getKeyMax(), thisEntry);
                } else {
                    thisEntry    =    null;
                }
            } catch (Exception e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>readNextEntry</code> is used to read the
     * next entry from database.
     *
     * @param currentEntry
     *             the currently displayed entry
     * @return the filled structure
     */
    @Override
    public final E readNextEntry(final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser !=    null) {
            int mandator = thisUser.getMandator();
            thisEntry = createInstance();
            Connection thisDataBase =    null;
            PreparedStatement readNextSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase,
                        mandator, thisEntry);
                if (thisEntry != null) {
                    String newEntryName = thisEntry.getKeyMax();
                    if (currentEntry != null && !"".equals(currentEntry)) {
                        readNextSQLStatement =
                                thisDataBase.prepareStatement(
                                        this.readNextSQL);
                        readNextSQLStatement.clearParameters();
                        readNextSQLStatement.setInt(1, mandator);
                        readNextSQLStatement.setString(2, currentEntry);
                        ResultSet result =
                                readNextSQLStatement.executeQuery();

                        if (result.next()) {
                            newEntryName =
                                    result.getString("dbnumber");
                        }
                        result.close();
                    }
                    thisEntry = readOneEntry(thisDataBase, mandator,
                            newEntryName, thisEntry);
                }
            } catch (SQLException e) {
                thisEntry    =    null;
            } catch (NamingException e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (readNextSQLStatement != null) {
                        readNextSQLStatement.close();
                    }
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>readPreviousEntry</code> is used to read
     * the previous entry from database.
     *
     * @param currentEntry
     *             the currently displayed entry
     * @return the filled user structure
     */
    @Override
    public final E readPreviousEntry(final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser !=    null) {
            int mandator = thisUser.getMandator();
            thisEntry = createInstance();
            Connection thisDataBase = null;
            PreparedStatement readPrevSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase,
                        mandator, thisEntry);
                if (thisEntry != null) {
                    String newEntryName = thisEntry.getKeyMin();
                    if (currentEntry != null
                    && !"".equals(currentEntry)) {
                        readPrevSQLStatement =
                                thisDataBase.prepareStatement(
                                        this.readPrevSQL);
                        readPrevSQLStatement.clearParameters();
                        readPrevSQLStatement.setInt(1, mandator);
                        readPrevSQLStatement.setString(2, currentEntry);
                        ResultSet result = readPrevSQLStatement.executeQuery();

                        if (result.next()) {
                            newEntryName =
                                    result.getString("dbnumber");
                        }
                        result.close();
                    }
                    thisEntry = readOneEntry(thisDataBase,
                            mandator, newEntryName, thisEntry);
                }
            } catch (SQLException e) {
                thisEntry    =    null;
            } catch (NamingException e) {
                thisEntry    =    null;
            } finally {
                try {
                    if (readPrevSQLStatement != null) {
                        readPrevSQLStatement.close();
                    }
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thisEntry    =    null;
        }
        return thisEntry;
    }

    /**
     * <code>insertEntry</code> inserts a entry
     * into the database.
     *
     * @param thisDataBase
     *         existing databas connection
     * @param mandator
     *         mandator number
     * @param user
     *         username which saves
     * @param saveEntry
     *         entry to save
     * @param delete
     *         true if older entries should be deleted
     * @return effected database entries (should always be 1)
     * @throws SQLException when sql error occours
     */
    protected final int insertEntry(final Connection thisDataBase,
            final int mandator, final String user,
            final E saveEntry, final boolean delete
           ) throws SQLException {
        int num = -1;
        PreparedStatement insertHeadSQLStatement = null;
        try {
            insertHeadSQLStatement =
                    thisDataBase.prepareStatement(this.insertHeadSQL);
            insertHeadSQLStatement.clearParameters();
            fillInsertHead(insertHeadSQLStatement, mandator,
                    user, saveEntry, delete);
             num = insertHeadSQLStatement.executeUpdate();
        } finally {
            if (insertHeadSQLStatement != null) {
                insertHeadSQLStatement.close();
            }
        }
        return num;
    }

    /**
     * blank to null is a method to set texts to null if no entry
     * is inside.
     * @param saveText text to save
     * @return updated text
     */
    protected final String blankToNull(final String saveText) {
        if (saveText == null || "".equals(saveText)) {
            return null;
        } else {
            return saveText;
        }
    }

    /**
     * <code>saveEntry</code> saves or inserts a
     * entry to database.
     *
     * @param currentEntry
     *             entry that has to be saved
     * @return entry after saving
     */
    @Override
    public final E saveEntry(final E currentEntry) {
        DomainUser thisUser    =    this.getUser();
        E returnEntry = currentEntry;
        if (thisUser !=    null) {
            int mandator = thisUser.getMandator();
            String user = thisUser.getUser();
            String saveKeyString = returnEntry.getKeyCur();
            if (saveKeyString == null || "".equals(saveKeyString)) {
                saveKeyString = returnEntry.getKeyNew();
            }

            Connection thisDataBase =    null;

            try {
                if (allowedToChange()) {
                    E dbEntry = createInstance();
                    // connect to database
                    InitialContext ic = new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(this.lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    dbEntry = readOneEntry(thisDataBase, mandator,
                            saveKeyString, dbEntry);
                    if ((dbEntry != null) && (dbEntry.getKeyCur() == null)) {
                        dbEntry    =    null;
                    }

                    this.saveEntry(currentEntry, dbEntry, thisDataBase,
                            mandator, user, saveKeyString);

                    returnEntry.setKeyCur(returnEntry.getKeyNew());
                    this.fillMinMax(thisDataBase, mandator,
                            returnEntry);
                    returnEntry = readOneEntry(thisDataBase,
                            mandator, returnEntry.getKeyNew(), returnEntry);
                }
            } catch (SQLException e) {
                returnEntry    =    null;
            } catch (NamingException e) {
                returnEntry    =    null;
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            returnEntry    =    null;
        }
        return returnEntry;
    }

    /**
     * <code>saveEntry</code> saves or inserts a
     * entry to database.
     *
     * @param currentEntry
     *             entry that has to be saved
     * @param dbEntry
     *             entry from database to compare
     * @param thisDataBase
     *             database connection
     * @param mandator
     *             mandator number
     * @param user
     *             name of the user
     * @param saveKeyString
     *             key of the entry to save
     * @throws SQLException if something's going wrong
     */
    protected abstract void saveEntry(
            final E currentEntry,
            final E dbEntry,
            final Connection thisDataBase,
            final int mandator,
            final String user,
            final String saveKeyString
           ) throws SQLException;

    /**
     * get database table name.
     * @return the dataBaseTableName
     */
    protected final String getDataBaseTableName() {
        return dataBaseTableName;
    }


    /**
     * get key field name.
     * @return the keyFieldName
     */
    protected final String getKeyFieldName() {
        return keyFieldName;
    }


    /**
     * get read min/max sql string.
     * @return the readMinMaxSQL
     */
    protected final String getReadMinMaxSQL() {
        return readMinMaxSQL;
    }


    /**
     * get read next sql string.
     * @return the readNextSQL
     */
    protected final String getReadNextSQL() {
        return readNextSQL;
    }


    /**
     * get read previous sql string.
     * @return the readPrevSQL
     */
    protected final String getReadPrevSQL() {
        return readPrevSQL;
    }


    /**
     * get read head sql string.
     * @return the readHeadSQL
     */
    protected final String getReadHeadSQL() {
        return readHeadSQL;
    }


    /**
     * get invalidate head sql string.
     * @return the invalidateHeadSQL
     */
    protected final String getInvalidateHeadSQL() {
        return invalidateHeadSQL;
    }


    /**
     * get insert head sql string.
     * @return the insertHeadSQL
     */
    protected final String getInsertHeadSQL() {
        return insertHeadSQL;
    }


    /**
     * get lookup database.
     * @return the lookUpDataBase
     */
    protected final String getLookUpDataBase() {
        return lookUpDataBase;
    }
}
