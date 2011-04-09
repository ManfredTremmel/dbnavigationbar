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
 *	Name		Date		Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * The <code>RiPhoneDomaneHeadDateBase</code> class is a exchange structure
 * between the client and the servlet on the server
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainHeadDataBase implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -2178996035568385436L;

	private Boolean IsReadOnly;
	private String KeyMin;
	private String KeyMax;
	private String KeyCur;

	/**
	 * Constructor, setup a empty entry
	 */
	public DomainHeadDataBase() {
		this.setUpDefaultEntry();
		this.KeyMin				=	null;
		this.KeyMax				=	null;
	}

	/**
	 * Copy Constructor, creates a new user with the same
	 * entries as the one who's given as parameter
	 * 
	 * @param CopyEntry entry to copy
	 */
	public DomainHeadDataBase(DomainHeadDataBase CopyEntry) {
		if( CopyEntry == null ) {
			this.setUpDefaultEntry();
			this.KeyMin					=	null;
			this.KeyMax					=	null;
		} else {
			this.IsReadOnly				=	CopyEntry.IsReadOnly;
			this.KeyMin					=	CopyEntry.KeyMin;
			this.KeyMax					=	CopyEntry.KeyMax;
			this.KeyCur					=	CopyEntry.KeyCur;
		}
	}
	
	/**
	 * Destructor
	 */
	protected void finalize() throws Throwable {
		this.KeyMin				=	null;
		this.KeyMax				=	null;
		setUpDefaultEntry();
		super.finalize();
	}

	/**
	 * get IsReadOnly
	 * 
     * @return IsReadOnly
	 */
	public boolean getIsReadOnly() {
		return this.IsReadOnly;
	}

	/**
	 * set IsReadOnly
	 * 
     * @param IsReadOnly
	 */
	public void setIsReadOnly(boolean IsReadOnly) {
		this.IsReadOnly = IsReadOnly;
	}

	/**
	 * set IsReadOnly
	 * 
     * @param IsReadOnly
	 */
	public void setIsReadOnly(Boolean IsReadOnly) {
		this.IsReadOnly = IsReadOnly;
	}

	/**
	 * get KeyMin
	 * 
     * @return KeyMin
	 */
	public String getKeyMin() {
		return this.KeyMin;
	}

	/**
	 * set KeyMin
	 * 
     * @param KeyMin
	 */
	public void setKeyMin(String KeyMin) {
		this.KeyMin = KeyMin;
	}

	/**
	 * get KeyMax
	 * 
     * @return KeyMax
	 */
	public String getKeyMax() {
		return this.KeyMax;
	}

	/**
	 * set KeyMax
	 * 
     * @param KeyMax
	 */
	public void setKeyMax(String KeyMax) {
		this.KeyMax = KeyMax;
	}

	/**
	 * get KeyCur
	 * 
     * @return KeyCur
	 */
	public String getKeyCur() {
		return this.KeyCur;
	}

	/**
	 * set KeyCur
	 * 
     * @param KeyCur
	 */
	public void setKeyCur(String KeyCur) {
		this.KeyCur = KeyCur;
	}

	/**
	 * get KeyNew
	 * @return Key of new Entry
	 */
	public abstract String getKeyNew();


	/**
	 * equals compares two entries
	 * 
	 * @param thisString a string to compare with vglString
	 * @param vglString entry to compare with thisString
	 * @return true if both contain the same entries, otherwise false
	 */
	protected boolean StringEquals(String thisString, String vglString) {
		return (thisString == null ? "" : thisString).equals((vglString == null ? "" : vglString));
	}

	/**
	 * equals compares two entries
	 * 
	 * @param thisEntry entry to compare with vglEntry
	 * @param vglEntry entry to compare with thisEntry
	 * @return true if both contain the same entries, otherwise false
	 */
	protected boolean DateEquals(Date thisEntry, Date vglEntry) {
		return (thisEntry == null ? "" : thisEntry).equals((vglEntry == null ? "" : vglEntry));
	}

	/**
	 * equals compares two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public boolean equals(DomainHeadDataBase vglEntry) {
		boolean isequal		=	true;
		isequal	&=	equalsEntry(vglEntry);
		return isequal;
	}

	/**
	 * equals compares two entries
	 * 
	 * @param obj entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!this.getClass().getName().equals(obj.getClass().getName())) {
			return false;
		}
		final DomainHeadDataBase other = (DomainHeadDataBase) obj;
		return this.equals(other);
	}

	/**
	 * equalsEntry compares the head part of two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public abstract boolean equalsEntry(DomainHeadDataBase vglEntry);

	/**
	 * set up a default entries
	 * 
	 */
	public void setUpDefaultEntry() {
		this.IsReadOnly					=	false;
		this.KeyCur						=	null;
	}
}
