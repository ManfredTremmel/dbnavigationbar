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
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.util.HashMap;

import de.knightsoft.DBNavigationBar.shared.fields.FieldInterface;

/**
 *
 * The <code>AbstractDataBaseDomain</code> is a abstract implementation
 * of data base domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 * @param <E> Type of the keyField
 */
public abstract class AbstractDataBaseDomain<E>
    implements InterfaceDataBase<E> {

    /**
     * read only entry.
     */
    private boolean readOnly;

    /**
     * state of the entry.
     */
    private EnumerationState enumerationState;

    /**
     * additional text to state.
     */
    private String stateText;

    /**
     * lowest key in database.
     */
    private final FieldInterface<E> keyMin;

    /**
     * highest key in database.
     */
    private final FieldInterface<E> keyMax;

    /**
     * current key of the entry.
     */
    private final FieldInterface<E> keyCur;

    /**
     * map of fields.
     */
    private HashMap<String, FieldInterface<?>> fieldMap;

    /**
     * default constructor.
     * @param keyType the type of the key field
     * @throws IllegalAccessException if instantiation fails
     * @throws InstantiationException if instantiation fails
     */
    public AbstractDataBaseDomain(
            final Class<FieldInterface<E>> keyType)
                    throws InstantiationException, IllegalAccessException {
        super();
        this.readOnly = false;
        this.enumerationState = EnumerationState.UNDEFINED;
        this.stateText = null;
        this.keyMin = keyType.newInstance();
        this.keyMax = keyType.newInstance();
        this.keyCur = keyType.newInstance();
        this.fieldMap = new HashMap<String, FieldInterface<?>>();
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getIsReadOnly()
     */
    @Override
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setIsReadOnly(boolean)
     */
    @Override
    public final void setIsReadOnly(final boolean newIsReadOnly) {
        this.readOnly = newIsReadOnly;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setIsReadOnly(java.lang.Boolean)
     */
    @Override
    public final void setIsReadOnly(final Boolean newIsReadOnly) {
        if (newIsReadOnly == null) {
            this.readOnly = false;
        } else {
            this.readOnly = newIsReadOnly.booleanValue();
        }
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getState()
     */
    @Override
    public final EnumerationState getState() {
        return this.enumerationState;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setState(de.knightsoft.DBNavigationBar
     *  .client.domain.EnumerationState)
     */
    @Override
    public final void setState(final EnumerationState newState) {
        this.enumerationState = newState;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getStateText()
     */
    @Override
    public final String getStateText() {
        return this.stateText;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setStateText(java.lang.String)
     */
    @Override
    public final void setStateText(final String newStateText) {
        this.stateText = newStateText;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getKeyMin()
     */
    @Override
    public final FieldInterface<E> getKeyMin() {
        return this.keyMin;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setKeyMin(de.knightsoft.DBNavigationBar.shared
     *  .fields.FieldInterface)
     */
    @Override
    public final void setKeyMin(final FieldInterface<E> newKeyMin) {
        this.keyMin.setValue(newKeyMin.getValue());
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getKeyMax()
     */
    @Override
    public final FieldInterface<E> getKeyMax() {
        return this.keyMax;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setKeyMax(de.knightsoft.DBNavigationBar.shared
     *  .fields.FieldInterface)
     */
    @Override
    public final void setKeyMax(final FieldInterface<E> newKeyMax) {
        this.keyMax.setValue(newKeyMax.getValue());
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getKeyCur()
     */
    @Override
    public final FieldInterface<E> getKeyCur() {
        return this.keyCur;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setKeyCur(de.knightsoft.DBNavigationBar.shared
     *  .fields.FieldInterface)
     */
    @Override
    public final void setKeyCur(final FieldInterface<E> newKeyCur) {
        this.keyCur.setValue(newKeyCur.getValue());
    }

    /*
     * (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getKeyNew()
     */
    @Override
    public final E getKeyNew() {
        if (this.keyCur == null) {
            return null;
        } else {
            return this.keyCur.getDefaultValue();
        }
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setFildMap(java.util.HashMap)
     */
    @Override
    public final void setFildMap(
            final HashMap<String, FieldInterface<?>> newFieldMap) {
        this.fieldMap = newFieldMap;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#getFieldMap()
     */
    @Override
    public final HashMap<String, FieldInterface<?>> getFieldMap() {
        return this.fieldMap;
    }

    /*
     * (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#addUpdateFieldEntry(java.lang.String,
     *  de.knightsoft.DBNavigationBar.shared.fields.FieldInterface)
     */
    @Override
    public final void addUpdateFieldEntry(final String fieldName,
            final FieldInterface<?> entry) {
        this.fieldMap.put(fieldName, entry);
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *   .InterfaceDataBase#getFieldEntry(java.lang.String)
     */
    @Override
    public final FieldInterface<?> getFieldEntry(final String fieldName) {
        return this.fieldMap.get(fieldName);
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#isFieldSetOkSimple()
     */
    @Override
    public final boolean isFieldSetOkSimple() {
        boolean ok = true;
        for (FieldInterface<?> testEntry : this.fieldMap.values()) {
            if (!testEntry.isOK()) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#setUpDefaultEntry()
     */
    @Override
    public final void setUpDefaultEntry() {
        for (FieldInterface<?> entry : this.fieldMap.values()) {
            entry.setValueObject(entry.getDefaultValue());
        }
    }

    /* (non-Javadoc)
     * @see de.knightsoft.DBNavigationBar.client.domain
     *  .InterfaceDataBase#equalsEntry(de.knightsoft.DBNavigationBar.client
     *  .domain.InterfaceDataBase)
     */
    @Override
    public final boolean equalsEntry(final InterfaceDataBase<E> vglEntry) {
        boolean entriesAreEqual = true;
        entriesAreEqual &= (this.readOnly == vglEntry.isReadOnly());
        entriesAreEqual &= (this.enumerationState == vglEntry.getState());
        entriesAreEqual &= objectEquals(this.stateText,
                vglEntry.getStateText());
        entriesAreEqual &= objectEquals(this.keyCur.getValue(),
                vglEntry.getKeyCur().getValue());
        entriesAreEqual &= (this.fieldMap.size()
                == vglEntry.getFieldMap().size());
        if (entriesAreEqual) {
            for (String key : this.fieldMap.keySet()) {
                entriesAreEqual &= objectEquals(this.getFieldEntry(key),
                        vglEntry.getFieldEntry(key));
            }
        }
        return entriesAreEqual;
    }


    /**
     * objectEquals compares if two objects are equal.
     *
     * @param thisObject a string to compare with vglString
     * @param compareObject entry to compare with thisString
     * @return true if both contain the same entries, otherwise false
     */
    protected final boolean objectEquals(final Object thisObject,
          final Object compareObject) {
        if (thisObject == compareObject) { // NOPMD we really want same
            return true;
        }
        if (thisObject == null || compareObject == null) {
            return false;
        }
        return thisObject.equals(compareObject);
    }
}
