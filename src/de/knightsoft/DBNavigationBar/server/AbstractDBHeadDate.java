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
 * Copyright (c) 2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gwt.user.server.rpc.UnexpectedException;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

import de.knightsoft.DBNavigationBar.client.domain.AbstractDataBaseDomain;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;
import de.knightsoft.DBNavigationBar.client.domain.EnumerationState;
import de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService;
import de.knightsoft.DBNavigationBar.server.dbfield.DBFieldFactory;
import de.knightsoft.DBNavigationBar.server.dbfield.DBFieldInterface;
import de.knightsoft.DBNavigationBar.shared.Constants;
import de.knightsoft.DBNavigationBar.shared.fields.AbstractField;
import de.knightsoft.DBNavigationBar.shared.fields.FieldInterface;

/**
 * Abstract class to implement database calls for the mask.
 *
 * @author Manfred Tremmel
 *
 * @param <E> Type of the database domain
 * @param <F> Type of the keyField
 */
public abstract class AbstractDBHeadDate<E extends AbstractDataBaseDomain<F>,
    F extends AbstractField<?>> extends XsrfProtectedServiceServlet
    implements AbstractDBRemoteService<E> {

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
     * @param setDomainType - class instance of E (domain type)
     * @param setKeyType - class instance of F (key type)
     * @param dbDriver name of database driver
     * @param lookUpDataBaseSet key to get database connection from pool
     * @param dataBaseTableNameSet data base table name to set
     * @param sessionUserSet name of the session to store/read data from
     * @param dataBaseDomain data base domain
     */
    public AbstractDBHeadDate(
            final Class<E> setDomainType,
            final Class<F> setKeyType,
            final String dbDriver,
            final String lookUpDataBaseSet,
            final String dataBaseTableNameSet,
            final String sessionUserSet,
            final E dataBaseDomain
            ) {
        super();
        this.domainType = setDomainType;
        this.keyType = setKeyType;
        this.lookUpDataBase = lookUpDataBaseSet;
        this.dataBaseTableName = dataBaseTableNameSet;
        this.sessionUser = sessionUserSet;

        // build database field list out of the fields
        this.dbFieldList = new ArrayList<DBFieldInterface<?>>();
        DBFieldInterface<?> dbKeyFieldTmp = null;
        for (final Entry<String, FieldInterface<?>> entry
                : dataBaseDomain.getFieldMap().entrySet()) {
            final DBFieldInterface<?> newDBField = DBFieldFactory.createDBField(
                    dbDriver, entry.getKey(), entry.getValue());
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

            // build sql statement to read one entry by key.
            final StringBuilder readHeadSB = new StringBuilder();
            readHeadSB.append("SELECT ");
            boolean firstEntry = true;
            for (DBFieldInterface<?> dbField : this.dbFieldList) {
                if (!firstEntry) {
                    readHeadSB.append(", ");
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
            readHeadSB.append(";");
            this.readHeadSQL = readHeadSB.toString();

            // build sql statement to read min and max key entries.
            final StringBuilder readMinMaxSB = new StringBuilder();
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
            readMinMaxSB.append(";");
            this.readMinMaxSQL = readMinMaxSB.toString();

            // build sql statement to read next key entries.
            final StringBuilder readNextSB = new StringBuilder();
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
            readNextSB.append(";");
            this.readNextSQL = readNextSB.toString();

            // build sql statement to read previous key entries.
            final StringBuilder readPrevSB = new StringBuilder();
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
            readPrevSB.append(";");
            this.readPrevSQL = readPrevSB.toString();

            // build sql statement to insert a entry into db.
            final StringBuilder insertHeadSQLSB = new StringBuilder();
            insertHeadSQLSB.append("INSERT INTO ");
            insertHeadSQLSB.append(this.dataBaseTableName);
            insertHeadSQLSB.append(" (Mandator, Date_from, Date_to, Username");
            for (DBFieldInterface<?> dbField : this.dbFieldList) {
                insertHeadSQLSB.append(", ");
                insertHeadSQLSB.append(dbField.preparedInsertReadPart());
            }
            insertHeadSQLSB.append(" ) VALUES ( ?, ");
            insertHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
            insertHeadSQLSB.append(", if (? , ");
            insertHeadSQLSB.append(myDataBaseDepending.getSQLTimeNow());
            insertHeadSQLSB.append(", '9999-12-31 23:59:59.000'), ?");
            for (DBFieldInterface<?> dbField : this.dbFieldList) {
                insertHeadSQLSB.append(", ");
                insertHeadSQLSB.append(dbField.preparedInsertValuesPart());
            }
            insertHeadSQLSB.append(");");
            this.insertHeadSQL = insertHeadSQLSB.toString();

            // build sql statement to insert a entry into db.
            final StringBuilder invalidateHeadSQLSB = new StringBuilder();
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
            invalidateHeadSQLSB.append(";");
            this.invalidateHeadSQL = invalidateHeadSQLSB.toString();

        } catch (SQLException e) {
            throw new UnexpectedException(e.toString(), e.getCause());
        } catch (NamingException e) {
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
     * <code>allowedToSee</code> checks if user
     * is allowed to see entries.
     * @return allowance to see true/false
     */
    protected abstract boolean allowedToSee();

    /**
     * <code>allowedToChange</code> checks if user
     * is allowed to change entries.
     * @return allowance to change true/false
     */
    protected abstract boolean allowedToChange();

    /**
     * <code>createDomainInstance</code> creates a new Instance.
     * @return E
     */
    protected final E createDomainInstance() {
        try {
            return this.domainType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * <code>createKeyInstance</code> creates a new Instance.
     * @return F
     */
    protected final F createKeyInstance() {
        try {
            return this.keyType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        E returnEntry;
        if (thisEntry == null) {
            returnEntry = this.createDomainInstance();
            returnEntry.setState(EnumerationState.UNDEFINED);
        } else {
            returnEntry = thisEntry;
        }
        try {
            readMinMaxSQLStatement = thisDataBase.prepareStatement(
                    this.readMinMaxSQL);
            readMinMaxSQLStatement.clearParameters();
            readMinMaxSQLStatement.setInt(1, mandator);
            ResultSet result = readMinMaxSQLStatement.executeQuery();

            if (result.next()) {
                returnEntry.getKeyMin().setString(result.getString("min"));
                returnEntry.getKeyMax().setString(result.getString("max"));
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            returnEntry.setState(EnumerationState.SERVER_ERROR);
            returnEntry.setStateText(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
            returnEntry.setState(EnumerationState.SERVER_ERROR);
            returnEntry.setStateText(e.getMessage());
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
     * @param searchMethodeEntry
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
     * @throws SQLException if sql statement can not be created
     */
    protected final String searchSQLSelect(
            final Connection thisDataBase,
            final String minMax,
            final String searchField,
            final String searchMethodeEntry,
            final String searchFieldEntry,
            final String dbKeyVGL,
            final String dbKey) throws SQLException {
        int mandator         =    this.getUser().getMandator();
        DataBaseDepending myDataBaseDepending =
                new DataBaseDepending(thisDataBase.getMetaData()
                        .getDatabaseProductName());

        String sqlString =
              "SELECT " + minMax + "(" + this.dbKeyField.getDBFieldName()
                        + ") AS dbnumber "
            + "FROM   " + this.dataBaseTableName + " "
            + "WHERE  " + Constants.DB_FIELD_GLOBAL_MANDATOR + " = "
            + Integer.toString(mandator) + " "
            + " AND   " + this.dbKeyField.getDBFieldName() + " " + dbKeyVGL
            + " " + StringToSQL.convertString(dbKey,
                    thisDataBase.getMetaData().getDatabaseProductName()) + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_FROM + " <= "
                    + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND    " + Constants.DB_FIELD_GLOBAL_DATE_TO + "   > "
                    + myDataBaseDepending.getSQLTimeNow() + " "
            + " AND   ";

        if ("=".equals(searchMethodeEntry)) {
            sqlString += StringToSQL.searchString(searchField,
                    searchFieldEntry, thisDataBase.getMetaData()
                    .getDatabaseProductName());
        } else if ("like".equals(searchMethodeEntry)) {
            sqlString += StringToSQL.searchString(searchField,
                    "*" + searchFieldEntry + "*", thisDataBase
                    .getMetaData().getDatabaseProductName());
        } else {
            sqlString += searchField + " " + searchMethodeEntry
                    + " " +  StringToSQL.convertString(searchFieldEntry,
                       thisDataBase.getMetaData().getDatabaseProductName());
        }
        return sqlString;
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
     *             the Entry to read (keyfield)
     * @param thisEntry
     *             structure to be filled
     * @return the filled structure
     */
    protected final E readOneEntry(final Connection thisDataBase,
            final int mandator, final String entry,
            final E thisEntry) {
        PreparedStatement readHeadSQLStatement = null;
        if (thisEntry != null) {
            if (this.allowedToSee()) {
                try {
                    // read entry
                    readHeadSQLStatement =
                            thisDataBase.prepareStatement(this.readHeadSQL);
                    readHeadSQLStatement.clearParameters();
                    readHeadSQLStatement.setInt(1, mandator);
                    readHeadSQLStatement.setString(2, entry);
                    ResultSet resultHead = readHeadSQLStatement.executeQuery();

                    if (resultHead.next()) {
                        for (DBFieldInterface<?> dbField : this.dbFieldList) {
                            dbField.readFromResultSet(resultHead, thisEntry
                                .getFieldMap().get(dbField.getDBFieldName()));
                        }
                        thisEntry.getKeyCur().setString(entry);
                    } else {
                        thisEntry.setState(EnumerationState.READ_NOT_FOUND);
                    }
                    thisEntry.setIsReadOnly(!this.allowedToChange());
                } catch (SQLException e) {
                    e.printStackTrace();
                    thisEntry.setState(EnumerationState.SERVER_ERROR);
                    thisEntry.setStateText(e.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                    thisEntry.setState(EnumerationState.SERVER_ERROR);
                    thisEntry.setStateText(e.getMessage());
                } finally {
                    if (readHeadSQLStatement != null) {
                        try {
                            readHeadSQLStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                thisEntry.setState(EnumerationState.NOT_ALLOWED_TO_READ);
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #readEntry(java.lang.String)
     */
    @Override
    public final E readEntry(final String entry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator = thisUser.getMandator();
            Connection thisDataBase = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase =    lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                thisEntry = readOneEntry(thisDataBase, mandator, entry,
                        thisEntry);
                if (thisEntry.getState() == EnumerationState.UNDEFINED) {
                    thisEntry.setState(EnumerationState.READ_OK);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }


    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #findFirstEntry(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final E findFirstEntry(
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    // connect to database
                    InitialContext ic =    new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry = thisEntry.getKeyMin().getString();

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MIN", searchField, searchMethodEntry,
                            searchFieldEntry, ">=", newEntry);
                    if (sqlString == null) {
                        newEntry = null;
                    } else {
                        Statement statement = null;
                        ResultSet result    = null;
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
                        thisEntry.setState(
                                EnumerationState.SEARCH_NOT_FOUND);
                    } else {
                        thisEntry.setState(
                                EnumerationState.SEARCH_OK);
                        thisEntry = readOneEntry(thisDataBase,
                                mandator, newEntry, thisEntry);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #findLastEntry(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final E findLastEntry(
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readFirstEntry();
                } else {
                    // connect to database
                    InitialContext ic =    new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry = thisEntry.getKeyMin().getString();

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MAX", searchField, searchMethodEntry,
                            searchFieldEntry, "<=", newEntry);
                    if (sqlString == null) {
                        newEntry = null;
                    } else {
                        Statement statement = null;
                        ResultSet result    = null;
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
                        thisEntry.setState(
                                EnumerationState.SEARCH_NOT_FOUND);
                    } else {
                        thisEntry.setState(
                                EnumerationState.SEARCH_OK);
                        thisEntry = readOneEntry(thisDataBase,
                                mandator, newEntry, thisEntry);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #findNextEntry(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public final E findNextEntry(
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry,
            final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readNextEntry(currentEntry);
                } else {
                    // connect to database
                    InitialContext ic =    new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry = currentEntry;

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MIN", searchField, searchMethodEntry,
                            searchFieldEntry, ">", newEntry);
                    if (sqlString == null) {
                        newEntry = null;
                    } else {
                        Statement statement = null;
                        ResultSet result    = null;
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
                        thisEntry.setState(
                                EnumerationState.SEARCH_NOT_FOUND);
                    } else {
                        thisEntry.setState(
                                EnumerationState.SEARCH_OK);
                        thisEntry = readOneEntry(thisDataBase,
                                mandator, newEntry, thisEntry);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #findPreviousEntry(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public final E findPreviousEntry(
            final String searchField,
            final String searchMethodEntry,
            final String searchFieldEntry,
            final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;
            try {
                if (searchFieldEntry == null || "".equals(searchFieldEntry)) {
                    thisEntry = this.readPreviousEntry(currentEntry);
                } else {
                    // connect to database
                    InitialContext ic =    new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                    String newEntry = currentEntry;

                    String sqlString = this.searchSQLSelect(thisDataBase,
                            "MAX", searchField, searchMethodEntry,
                            searchFieldEntry, "<", newEntry);
                    if (sqlString == null) {
                        newEntry = null;
                    } else {
                        Statement statement = null;
                        ResultSet result    = null;
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
                        thisEntry.setState(
                                EnumerationState.SEARCH_NOT_FOUND);
                    } else {
                        thisEntry.setState(
                                EnumerationState.SEARCH_OK);
                        thisEntry = readOneEntry(thisDataBase,
                                mandator, newEntry, thisEntry);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #readFirstEntry()
     */
    @Override
    public final E readFirstEntry() {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator    =    thisUser.getMandator();
            Connection thisDataBase    =    null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                thisEntry.setState(EnumerationState.READ_OK);
                thisEntry = readOneEntry(thisDataBase, mandator,
                        thisEntry.getKeyMin().getString(), thisEntry);
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #readLastEntry()
     */
    @Override
    public final E readLastEntry() {
        E thisEntry = null;
        DomainUser thisUser    =    this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator = thisUser.getMandator();
            Connection thisDataBase = null;
            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                thisEntry.setState(EnumerationState.READ_OK);
                thisEntry = readOneEntry(thisDataBase, mandator,
                        thisEntry.getKeyMax().getString(), thisEntry);
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #readNextEntry(java.lang.String)
     */
    @Override
    public final E readNextEntry(final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator = thisUser.getMandator();
            Connection thisDataBase =    null;
            PreparedStatement readNextSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                String newEntryName = thisEntry.getKeyMax().getString();
                thisEntry.setState(EnumerationState.SEARCH_NOT_FOUND);
                if (currentEntry != null && !"".equals(currentEntry)) {
                    readNextSQLStatement = thisDataBase.prepareStatement(
                                    this.readNextSQL);
                    readNextSQLStatement.clearParameters();
                    readNextSQLStatement.setInt(1, mandator);
                    readNextSQLStatement.setString(2, currentEntry);
                    ResultSet result = readNextSQLStatement.executeQuery();

                    if (result.next()) {
                        newEntryName = result.getString("dbnumber");
                        thisEntry.setState(EnumerationState.READ_OK);
                    }
                    result.close();
                }
                thisEntry = readOneEntry(thisDataBase, mandator,
                        newEntryName, thisEntry);
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
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
        }
        return thisEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #readPreviousEntry(java.lang.String)
     */
    @Override
    public final E readPreviousEntry(final String currentEntry) {
        E thisEntry = null;
        DomainUser thisUser = this.getUser();
        if (thisUser ==    null) {
            thisEntry = this.createDomainInstance();
            thisEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator = thisUser.getMandator();
            Connection thisDataBase =    null;
            PreparedStatement readPrevSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                thisEntry = fillMinMax(thisDataBase, mandator, thisEntry);
                String newEntryName = thisEntry.getKeyMax().getString();
                thisEntry.setState(EnumerationState.SEARCH_NOT_FOUND);
                if (currentEntry != null && !"".equals(currentEntry)) {
                    readPrevSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.readPrevSQL);
                    readPrevSQLStatement.clearParameters();
                    readPrevSQLStatement.setInt(1, mandator);
                    readPrevSQLStatement.setString(2, currentEntry);
                    ResultSet result = readPrevSQLStatement.executeQuery();

                    if (result.next()) {
                        newEntryName = result.getString("dbnumber");
                        thisEntry.setState(EnumerationState.READ_OK);
                    }
                    result.close();
                }
                thisEntry = readOneEntry(thisDataBase, mandator,
                        newEntryName, thisEntry);
            } catch (SQLException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                thisEntry = this.createDomainInstance();
                thisEntry.setState(EnumerationState.SERVER_ERROR);
                thisEntry.setStateText(e.getMessage());
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
            int parmNum = 1;
            insertHeadSQLStatement.setInt(parmNum++, mandator);
            insertHeadSQLStatement.setBoolean(parmNum++, delete);
            insertHeadSQLStatement.setString(parmNum++, user);
            for (DBFieldInterface<?> dbField : this.dbFieldList) {
                parmNum = dbField.addToPreparedStatement(insertHeadSQLStatement,
                        parmNum, saveEntry.getFieldMap().get(
                                dbField.getDBFieldName()));
            }
            num = insertHeadSQLStatement.executeUpdate();
        } finally {
            if (insertHeadSQLStatement != null) {
                insertHeadSQLStatement.close();
            }
        }
        return num;
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
    protected final void saveEntry(
            final E currentEntry,
            final E dbEntry,
            final Connection thisDataBase,
            final int mandator,
            final String user,
            final String saveKeyString
           ) throws SQLException {
        // Entry already exists in Database?
        if (!currentEntry.equals(dbEntry)) {
            PreparedStatement invalidateHeadSQLStatement = null;

            try {
                // Invalidate old entry
                invalidateHeadSQLStatement =
                        thisDataBase.prepareStatement(
                                this.invalidateHeadSQL);
                invalidateHeadSQLStatement
                        .clearParameters();
                invalidateHeadSQLStatement
                        .setInt(1, mandator);
                invalidateHeadSQLStatement
                        .setString(2, saveKeyString);
                invalidateHeadSQLStatement
                        .executeUpdate();
            } finally {
                if (invalidateHeadSQLStatement != null) {
                    invalidateHeadSQLStatement.close();
                }
            }

            // Insert new entry
            this.insertEntry(thisDataBase,
                    mandator, user, currentEntry, false);
        }
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #saveEntry(de.knightsoft.DBNavigationBar.client.domain
     * .AbstractDataBaseDomain)
     */
    @Override
    public final E saveEntry(final E currentEntry) {
        DomainUser thisUser    =    this.getUser();
        E returnEntry = currentEntry;
        if (!returnEntry.isFieldSetOk()) {
            returnEntry.setState(EnumerationState.DATA_WRONG);
        } else if (thisUser ==    null) {
            returnEntry.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator = thisUser.getMandator();
            String user = thisUser.getUser();
            String saveKeyString = returnEntry.getKeyCur().getString();
            if (saveKeyString == null || "".equals(saveKeyString)) {
                saveKeyString = returnEntry.getKeyNew().getString();
            }

            Connection thisDataBase =    null;

            try {
                if (allowedToChange()) {
                    // connect to database
                    InitialContext ic = new InitialContext();
                    DataSource lDataSource =
                            (DataSource) ic.lookup(this.lookUpDataBase);
                    thisDataBase = lDataSource.getConnection();
                    ic.close();

                    E dbEntry = fillMinMax(thisDataBase, mandator, null);
                    dbEntry = readOneEntry(thisDataBase, mandator,
                            saveKeyString, dbEntry);
                    if ((dbEntry != null) && (dbEntry.getKeyCur() == null)) {
                        dbEntry    =    null;
                    }

                    returnEntry.setState(EnumerationState.WRITE_OK);
                    this.saveEntry(currentEntry, dbEntry, thisDataBase,
                            mandator, user, saveKeyString);

                    returnEntry.getKeyCur().setValue(returnEntry.getKeyNew());
                    this.fillMinMax(thisDataBase, mandator,
                            returnEntry);
                    returnEntry = readOneEntry(thisDataBase,
                            mandator, returnEntry.getKeyNew().getString(),
                            returnEntry);
                } else {
                    returnEntry.setState(EnumerationState.NOT_ALLOWED_TO_WRITE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                returnEntry = this.createDomainInstance();
                returnEntry.setState(EnumerationState.SERVER_ERROR);
                returnEntry.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                returnEntry = this.createDomainInstance();
                returnEntry.setState(EnumerationState.SERVER_ERROR);
                returnEntry.setStateText(e.getMessage());
            } finally {
                try {
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnEntry;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.ui.AbstractDBRemoteService
     * #deleteEntry(java.lang.String)
     */
    @Override
    public final E deleteEntry(final String currentEntry) {
        E resultValue = null;
        DomainUser thisUser  =    this.getUser();
        if (thisUser ==    null) {
            resultValue = this.createDomainInstance();
            resultValue.setState(EnumerationState.NOT_LOGGED_IN);
        } else {
            int mandator     =    thisUser.getMandator();
            String user      =    thisUser.getUser();
            Connection thisDataBase        =    null;
            PreparedStatement invalidateHeadSQLStatement = null;

            try {
                // connect to database
                InitialContext ic = new InitialContext();
                DataSource lDataSource =
                        (DataSource) ic.lookup(this.lookUpDataBase);
                thisDataBase = lDataSource.getConnection();
                ic.close();

                if (allowedToChange()) {
                    E dbEntry = this.readEntry(currentEntry);
                    // invalidate head number
                    invalidateHeadSQLStatement =
                            thisDataBase.prepareStatement(
                                    this.invalidateHeadSQL);
                    invalidateHeadSQLStatement.clearParameters();
                    invalidateHeadSQLStatement.setInt(1, mandator);
                    invalidateHeadSQLStatement.setString(2,
                            currentEntry);
                    invalidateHeadSQLStatement.executeUpdate();
                    this.insertEntry(thisDataBase, mandator,
                            user, dbEntry, true);
                    resultValue    = readNextEntry(currentEntry);
                    resultValue.setState(EnumerationState.DELETE_OK);
                } else {
                    resultValue    = readEntry(currentEntry);
                    resultValue.setState(EnumerationState.NOT_ALLOWED_TO_WRITE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                resultValue = this.createDomainInstance();
                resultValue.setState(EnumerationState.SERVER_ERROR);
                resultValue.setStateText(e.getMessage());
            } catch (NamingException e) {
                e.printStackTrace();
                resultValue = this.createDomainInstance();
                resultValue.setState(EnumerationState.SERVER_ERROR);
                resultValue.setStateText(e.getMessage());
            } finally {
                try {
                    if (invalidateHeadSQLStatement != null) {
                        invalidateHeadSQLStatement.close();
                    }
                    if (thisDataBase != null) {
                        thisDataBase.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultValue;
    }
}
