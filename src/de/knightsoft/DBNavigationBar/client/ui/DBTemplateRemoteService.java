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

import com.google.gwt.user.client.rpc.RemoteService;

import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBase;

/**
 * 
 * The <code>RiPhoneTargetPlaceRemoteService</code> class is the synchronous
 * interface for the RiPhoneTargetPlace widget
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public abstract interface DBTemplateRemoteService<E extends DomainHeadDataBase> extends RemoteService {
	E saveEntry(E currentEntry);
	E deleteEntry(String currentEntry);
	E readEntry(String entry);
	E readFirstEntry();
	E readPreviousEntry(String currentEntry);
	E readNextEntry(String currentEntry);
	E readLastEntry();
	E findFirstEntry(String searchField, String searchMethodeEntry, String searchFieldEntry);
	E findPreviousEntry(String searchField, String searchMethodeEntry, String searchFieldEntry, String currentEntry);
	E findNextEntry(String searchField, String searchMethodeEntry, String searchFieldEntry, String currentEntry);
	E findLastEntry(String searchField, String searchMethodeEntry, String searchFieldEntry);

}
