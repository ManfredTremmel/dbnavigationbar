/**
 * This file is part of DBNavigation.
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
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBase;

/**
 * 
 * The <code>DBTemplateRemoteServiceAsync</code> class is the asynchronous
 * interface template for DataBase Head
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public abstract interface DBTemplateRemoteServiceAsync<E extends DomainHeadDataBase> {
	void saveEntry(E CurrentEntry, AsyncCallback<E> callback);
	void deleteEntry(String CurrentEntry, AsyncCallback<E> callback);
	void readEntry(String Entry, AsyncCallback<E> callback);
	void readFirstEntry(AsyncCallback<E> callback);
	void readPreviousEntry(String CurrentEntry, AsyncCallback<E> callback);
	void readNextEntry(String CurrentEntry, AsyncCallback<E> callback);
	void readLastEntry(AsyncCallback<E> callback);
	void findFirstEntry(String SearchField, String SearchMethodeEntry, String SearchFieldEntry, AsyncCallback<E> callback);
	void findPreviousEntry(String SearchField, String SearchMethodeEntry, String SearchFieldEntry, String CurrentEntry, AsyncCallback<E> callback);
	void findNextEntry(String SearchField, String SearchMethodeEntry, String SearchFieldEntry, String CurrentEntry, AsyncCallback<E> callback);
	void findLastEntry(String SearchField, String SearchMethodeEntry, String SearchFieldEntry, AsyncCallback<E> callback);

}
