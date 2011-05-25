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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.DBNavigationBar.client.Parent;
import de.knightsoft.DBNavigationBar.client.domain.DomainHeadDataBase;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;
import de.knightsoft.DBNavigationBar.client.ui.widget.DBNavigationBarWidget;

/**
 * 
 * The <code>DBHeadTemplateUI</code> class is a template for database
 * input mask
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-14
 */
public abstract class DBHeadTemplateUI<E extends DomainHeadDataBase, F extends Parent> extends BasicTemplateUI<F> implements AsyncCallback<E> {

	protected final DBNavigationBarWidget myNavigationBar;
	
	protected static final Images images = GWT.create(Images.class);

	protected boolean dosave = false;

	/**
	 * Pictures used
	 */
	public interface Images extends ClientBundle {
		@Source("DBHeadTemplateUI.png")
		ImageResource DBHeadTemplateUI();

		@Source("DeletePosition.png")
		ImageResource DeletePosition();
	}
	
	protected E dbEntry;

	protected final DBHeadTemplateUIConstants constants;
	
	protected final Widget[] widgetlist;

	/**
	 * Constructor
	 * 
	 * @param parentwidget
	 * 		the parent widget, where this frame is displayed
	 * @param widgetlist
	 * 		list of widgets to display
	 * @param userdefinedfunction
	 * 		special function to include into the navigation bar
	 */
	public DBHeadTemplateUI(
			F parentwidget,
			Widget[] widgetlist,
			String userdefinedfunction) {

		super(parentwidget);
		this.widgetlist		=	widgetlist;

		this.constants = (DBHeadTemplateUIConstants) GWT.create(DBHeadTemplateUIConstants.class);

		this.myNavigationBar = new DBNavigationBarWidget(
				getSearchFields(),
				getSearchFieldsDisplay(),
				userdefinedfunction
			);


		VerticalPanel myPanel = new VerticalPanel();
	    myPanel.setWidth("100%");
	    myPanel.setHeight("100%");
	    myPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

	    HorizontalPanel titlePanel = new HorizontalPanel();
	    titlePanel.setWidth("100%");
	    titlePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
	    String pageTitle	=	"<h1>" + getHeaderTitle() + "</h1>";
	    HTML htmlPageTitle	=	new HTML(pageTitle);
	    titlePanel.add(htmlPageTitle);

	    myPanel.add(titlePanel);

	    myPanel.add(this.myNavigationBar);

	    final FormPanel form = new FormPanel();

	    myPanel.add(this.createAndFormatContentPanel());

		form.add(myPanel);

		this.initWidget(form);

		this.myNavigationBar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DBHeadTemplateUI.this.dosave = false;
				final DBTemplateRemoteServiceAsync<E> service = getServiceFactory();
				switch(DBHeadTemplateUI.this.myNavigationBar.getButtonState()) {
					case NEW:
						newEntry();
						break;
					case DELETE:
						service.deleteEntry(DBHeadTemplateUI.this.myNavigationBar.getOldDBNumber(), DBHeadTemplateUI.this);
						break;
					case STOP:
						fillEntry(dbEntry);
						break;
					case FAST_BACK:
						service.readFirstEntry(DBHeadTemplateUI.this);
						break;
					case FAST_BACK_FIND:
						service.findFirstEntry(
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldField(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
								DBHeadTemplateUI.this);
						break;
					case BACK:
						service.readPreviousEntry(DBHeadTemplateUI.this.myNavigationBar.getOldDBNumber(), DBHeadTemplateUI.this);
						break;
					case BACK_FIND:
						service.findPreviousEntry(
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldField(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
								DBHeadTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
								DBHeadTemplateUI.this);
						break;
					case FORWARD:
						service.readNextEntry(DBHeadTemplateUI.this.myNavigationBar.getOldDBNumber(), DBHeadTemplateUI.this);
						break;
					case FORWARD_FIND:
						service.findNextEntry(
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldField(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
								DBHeadTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
								DBHeadTemplateUI.this);
						break;
					case FAST_FORWARD:
						service.readLastEntry(DBHeadTemplateUI.this);
						break;
					case FAST_FORWARD_FIND:
						service.findLastEntry(
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldField(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
								DBHeadTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
								DBHeadTemplateUI.this);
						break;
					case CHANGE:
						service.readEntry(DBHeadTemplateUI.this.myNavigationBar.getCurrentDBNumber(), DBHeadTemplateUI.this);
						break;
					case USER_DEFINED:
						userDefinedFunction();
						break;
					default:
						E saveentry = checkInput();
						if( saveentry != null ) {
							if( !saveentry.equals(dbEntry) ) {
								dosave = true;
								service.saveEntry(saveentry, DBHeadTemplateUI.this);
							}
						}
						break;
				}
			}
		});
	}

	/**
	 * get the service to use
	 */
	protected abstract DBTemplateRemoteServiceAsync<E> getServiceFactory();

	/**
	 * get fieldlist of search fields
	 * @return fieldlist
	 */
	protected abstract String[] getSearchFields();

	/**
	 * get fieldlist to display of search fields
	 * @return fieldlist to display
	 */
	protected abstract String[] getSearchFieldsDisplay();

	/**
	 * <code>checkInput</code> check the input
	 * 
	 * @return input data or null if something went wrong
	 */
	protected abstract E checkInput();

	/**
	 * <code>newEntry</code> creates a new entry
	 */
	protected void newEntry() {
		this.myNavigationBar.setNewEntry();
		this.dbEntry.setUpDefaultEntry();

		enableKeyField(true);

		this.fillEntry(this.dbEntry);
	}

	/**
	 * <code>createAndFormatContentPanel</code> create the panel
	 * 
	 * @return Panel
	 */
	protected abstract Panel createAndFormatContentPanel();

	
	/**
	 * <code>enableKeyField</code> enables/disables KeyField
	 * 
	 * @param enable
	 * 			true/false to enable/disable key for input
	 */
	protected abstract void enableKeyField( boolean enable );

	/**
	 * <code>UserDefinedFunction</code> can be used for additional
	 * functionality that can be used by a button in the navigation widget
	 */
	protected void userDefinedFunction() {
		// redefine if you want to include a user defined function
	}

	/**
	 * <code>onFailure</code> is called, when server side an
	 * exception is thrown
	 * 
	 *  @param caught
	 *  		the thrown exception 
	 */
	@Override
	public void onFailure(Throwable caught) {
		this.myNavigationBar.displayHint(caught.toString());
	}

	/**
	 * <code>onSuccess</code> is called, when server side call
	 * was successfully
	 * 
	 *  @param entry
	 *  		the returnvalue of the serverside function
	 */
	@Override
	public void onSuccess(E entry) {
		if( entry == null ) {
			this.parentwidget.cleanUp();
		} else if( entry.getKeyCur() == null ) {
			if( this.dbEntry != null)
				this.myNavigationBar.setDBMinMaxCurNumber(this.dbEntry.getKeyMin(), this.dbEntry.getKeyMax(), this.dbEntry.getKeyCur());
			this.myNavigationBar.displayHint(constants.searchErrorMessage());
		} else {
			this.dbEntry		=	entry;
			this.myNavigationBar.setDBMinMaxCurNumber(entry.getKeyMin(), entry.getKeyMax(), entry.getKeyCur());
			
			if( this.dosave )
				this.myNavigationBar.displayHint(constants.savedSuccessfully());
			else
				this.myNavigationBar.displayHint(null);

			if( entry.getKeyCur() == null || "".equals(entry.getKeyCur()) )
				enableKeyField(true);
			else
				enableKeyField(false);
			
			this.fillEntry(entry);
		}
	}

	/**
	 * <code>fillEntry</code> is called, to display a user
	 * 
	 *  @param entry
	 *  		entry to display
	 */
	protected abstract void fillEntry(E entry);


    /**
     * The Method <code>matchesMenu</code> looks if this UI is selected and
     * makes the necessary changes.
     * 
     * @param itemtext
     *            selected menu item
     * @param user
     *            user information about the currently logged in user
     * @return true if it is allowed for this user
     */
	@Override
	public boolean matchesMenu(
			String itemtext,
			DomainUser user) {
		boolean matches = super.matchesMenu(itemtext, user);
		if(	matches ) {
			if( allowedToChange(user) ) {
				this.myNavigationBar.setReadWrite();
			} else {
				this.myNavigationBar.setReadOnly();
			}
			if( this.myNavigationBar.getOldDBNumber() == null ) {
				final DBTemplateRemoteServiceAsync<E> service = getServiceFactory();
				service.readLastEntry(DBHeadTemplateUI.this);
			}
		}
		return matches;
	}
}
