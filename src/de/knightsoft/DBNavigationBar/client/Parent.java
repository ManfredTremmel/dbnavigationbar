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
package de.knightsoft.DBNavigationBar.client;

import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.knightsoft.DBNavigationBar.client.domain.DomainUser;

/**
 * 
 * The <code>Parent</code> class is the entry point class which
 * defines <code>onModuleLoad()</code> and is executed when the
 * website is loeded. Just a template that has to be implemented
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public abstract class Parent implements EntryPoint {

	public VerticalPanel mainPanel;
	protected Tree navTree;

	/**
	 * Remember given parameters in url
	 */
	public HashMap<String, String> ParamHash	= null;
	
	/**
	 * Get the data of the logged in user
	 * @return User
	 */
	public abstract DomainUser getUser();

	/**
	 * get Copyright String
	 * @return Copyright String
	 */
	protected abstract String getCopyrightString();

	/**
	 * show the login panel
	 */
	protected abstract void ShowLoginPanel();

	/**
	 * read the logged in user
	 */
	protected abstract void ReadLoginUser();

	/**
	 * check if login window matches menu entry
	 * @param page
	 * @param currentUser
	 */
	protected abstract boolean LoginMatchesMenu(String page, DomainUser currentUser);

	/**
	 * get the Image of the Application
	 * @return Image
	 */
	protected abstract Image getApplicationImage();

	/**
	 * get the Application Title
	 * @return Application Title
	 */
	protected abstract String getApplicationTitle();

    /**
     * The <code>menuFind</code> method checks if a given menu entry or
     * history selection belongs to one of the subpages of RiPhone
     * 
     * @param itemtext
     *            Name of the selection
     * @param currentUser
     *            The logged in user, null if non is logged in
     * @return true if a entry matches, false if non is found
     */
	public abstract boolean menuFind(
			String itemtext,
			DomainUser currentUser );
	
    /**
     * The <code>cleanUp</code> method can be called if user
     * logs off or session is timed out
     * 
     */
	public abstract void cleanUp();

    /**
     * The <code>buildNavTree</code> method builds the navigation
     * tree
     * 
     * @param user
     *            The logged in user, null if non is logged in
     * @return Navigation tree
     */
	public abstract Tree buildNavTree( DomainUser user );

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// Create the constants
		DomainUser currentUser = getUser();

		// Get the title from the internationalized constants
		navTree	=	new Tree();
		navTree =	buildNavTree(currentUser);

		// Horizontal Panel, left navigation, right content
		SplitLayoutPanel hPanel = new SplitLayoutPanel();
		hPanel.setSize("100%", "100%");

		//ScrollPanel navScrollPanel = new ScrollPanel();
		//VerticalPanel navVPanel = new VerticalPanel();
		DockLayoutPanel navVPanel	=	new DockLayoutPanel(Unit.EM);
		navVPanel.setSize("100%", "100%");
		//navVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);

		setupNavPanelLogo(navVPanel);
		setupNavPanelCopyRight(navVPanel);
		setupNavPanelTree(navVPanel, navTree);

		//navScrollPanel.add(navVPanel);
		//hPanel.addWest(navScrollPanel, 250);
		hPanel.addWest(navVPanel, 250);

		ScrollPanel mainScrollPanel = new ScrollPanel();
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		mainPanel.getElement().setId("main");

		this.ParamHash		=	new HashMap<String, String>();
		String URLString	=	Window.Location.getHref();
		if( URLString.indexOf('#') >= 0 ) {
			URLString		=	URLString.substring(URLString.indexOf('#')+1);
			String[] historyPares = URLString.split(";");
			for( int i = 0; i < historyPares.length ; i++ ) {
				String[] tokenPare = historyPares[i].split("=");
				if( tokenPare.length == 2 ) {
					this.ParamHash.put(tokenPare[0], URL.decode(tokenPare[1]));
				}
			}
		}

		if( currentUser == null ) {
			ShowLoginPanel();
			ReadLoginUser();
		} else {
			String page	=	ParamHash.get("page");
			if( !this.menuFind(page, currentUser) )
				ShowLoginPanel();
		}
			

		mainScrollPanel.add(mainPanel);
		hPanel.add(mainScrollPanel);

		// Add image and button to the RootPanel
		RootPanel.get().add(hPanel);

		// Selection handler to handle clicks on the navigation tree
		navTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				String itemtext		=	item.getText();
				String itemtitle	=	item.getTitle();
				if( itemtitle != null && itemtitle.length() > 6 ) {
					if( ParamHash	==	null )
						ParamHash	=	new HashMap<String, String>();
					ParamHash.put("period", itemtitle.substring(0, 6));
				}
				DomainUser currentUser = getUser();
				menuFind(itemtext, currentUser);
			}
			
		});

		// Handler to handle page changes, to get back and forward button
		// work even in AJAX application
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				DomainUser currentUser = getUser();
				String historyToken = event.getValue();
				String[] historyPares = historyToken.split(";");
				ParamHash		=	new HashMap<String, String>();
				for( int i = 0; i < historyPares.length; i++ ) {
					String[] tokenPare = historyPares[i].split("=");
					if( tokenPare.length == 2 ) {
						ParamHash.put(tokenPare[0], URL.decode(tokenPare[1]));
					}
				}
				String page	=	ParamHash.get("page");
				if( currentUser != null ) {
					menuFind(page, currentUser);
				} else {
					mainPanel.clear();
					LoginMatchesMenu(page, currentUser);
				}
			}
		});

	}

    /**
     * The <code>getNavTree</code> method returns the navigation
     * tree, without building it once again
     * 
     * @return Navigation tree
     */
	public Tree getNavTree() {
		return navTree;
	}

	/**
	 * Create the logo
	 * 
	 * @param constants the constant values to use
	 * @param navVPanel to add the logo
	 */
	protected void setupNavPanelLogo(
			DockLayoutPanel navVPanel) {
		String Title		=	getApplicationTitle();
		VerticalPanel LogoPanel = new VerticalPanel();
		// Get the title from the internationalized constants
		String pageTitle	=	"<h1>" + Title + "</h1>";
		HTML htmlPageTitle	=	new HTML(pageTitle);
		Image img = getApplicationImage();
		img.getElement().setId("pc-template-img");
		img.setAltText(Title);
		
		// Add the title and some images to the title bar
		LogoPanel.add(img);
		LogoPanel.setCellHorizontalAlignment(img, VerticalPanel.ALIGN_CENTER );
		LogoPanel.setCellVerticalAlignment(img, VerticalPanel.ALIGN_TOP);
		LogoPanel.add(htmlPageTitle);
		LogoPanel.setCellHorizontalAlignment(htmlPageTitle, VerticalPanel.ALIGN_CENTER );
		LogoPanel.setCellVerticalAlignment(htmlPageTitle, VerticalPanel.ALIGN_TOP);
		LogoPanel.setWidth("100%");
		LogoPanel.setBorderWidth(0);
		LogoPanel.setSpacing(0);
		
		navVPanel.addNorth(LogoPanel, 12);
	}

	/**
	 * Create the title bar at the top of the application.
	 * 
	 * @param constants the constant values to use
	 * @param navTree to add
	 */
	protected void setupNavPanelTree(
			DockLayoutPanel navVPanel,
			Tree navTree) {
		// Add the title and some images to the title bar
		ScrollPanel NavScrollerPanel	=	new ScrollPanel();
		NavScrollerPanel.add(navTree);
		NavScrollerPanel.setSize("100%", "100%");

		navVPanel.add(NavScrollerPanel);
	}

	/**
	 * Create the title bar at the top of the application.
	 * 
	 * @param navVPanel to add the CopyRight entry
	 */
	protected void setupNavPanelCopyRight(
			DockLayoutPanel navVPanel) {
		// Get the title from the internationalized constants
		VerticalPanel CopyrightPanel	=	new VerticalPanel();
		HTML htmlCopyright				=	new HTML(getCopyrightString());
		CopyrightPanel.add(htmlCopyright);
		CopyrightPanel.setCellHorizontalAlignment(htmlCopyright, VerticalPanel.ALIGN_CENTER );
		CopyrightPanel.setCellVerticalAlignment(htmlCopyright, VerticalPanel.ALIGN_TOP);
		CopyrightPanel.setWidth("100%");

		// Add the title and some images to the title bar
		navVPanel.addSouth(CopyrightPanel, 1.5);
	}
}
