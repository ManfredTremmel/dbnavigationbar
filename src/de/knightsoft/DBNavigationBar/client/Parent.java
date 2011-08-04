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
 *  Name        Date        Change
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
 * website is loeded. Just a template that has to be implemented.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-13
 */
public abstract class Parent implements EntryPoint {

    /**
     * navigation panel width.
     */
    public static final int NAV_WIDTH = 250;

    /**
     * item title check length.
     */
    public static final int TITLE_CHECK_LENGTH = 6;

    /**
     * logo panel higth.
     */
    public static final int LOGO_PANEL_HIGTH = 12;

    /**
     * copyright panel higth.
     */
    public static final double COPYRIGHT_PANEL_HIGTH = 1.5;

    /**
     * mainPanel on the web site.
     */
    private VerticalPanel mainPanel;

    /**
     * navigation tree.
     */
    private Tree navTree;

    /**
     * Remember given parameters in url.
     */
    private HashMap<String, String> paramHash = null;

    /**
     * Get the data of the logged in user.
     * @return User
     */
    public abstract DomainUser getUser();

    /**
     * get Copyright String.
     * @return Copyright String
     */
    protected abstract String getCopyrightString();

    /**
     * show the login panel.
     */
    protected abstract void showLoginPanel();

    /**
     * read the logged in user.
     */
    protected abstract void readLoginUser();

    /**
     * check if login window matches menu entry.
     * @param page
     *             page the compare
     * @param currentUser
     *             current logged in user
     * @return true/false
     */
    protected abstract boolean loginMatchesMenu(String page,
            DomainUser currentUser);

    /**
     * get the Image of the Application.
     * @return Image
     */
    protected abstract Image getApplicationImage();

    /**
     * get the Application Title.
     * @return Application Title
     */
    protected abstract String getApplicationTitle();

    /**
     * The <code>menuFind</code> method checks if a given menu entry or
     * history selection belongs to one of the subpages of RiPhone.
     *
     * @param itemtext
     *            Name of the selection
     * @param currentUser
     *            The logged in user, null if non is logged in
     * @return true if a entry matches, false if non is found
     */
    public abstract boolean menuFind(
            String itemtext,
            DomainUser currentUser);

    /**
     * The <code>cleanUp</code> method can be called if user
     * logs off or session is timed out.
     *
     */
    public abstract void cleanUp();

    /**
     * The <code>buildNavTree</code> method builds the navigation
     * tree.
     *
     * @param user
     *            The logged in user, null if non is logged in
     * @return Navigation tree
     */
    public abstract Tree buildNavTree(DomainUser user);

    /**
     * This is the entry point method.
     */
    public final void onModuleLoadBase() {

        // Create the constants
        DomainUser currentUser = getUser();

        // Get the title from the internationalized constants
        this.navTree    =    buildNavTree(currentUser);

        // Horizontal Panel, left navigation, right content
        SplitLayoutPanel hPanel = new SplitLayoutPanel();
        hPanel.setSize("100%", "100%");

        //ScrollPanel navScrollPanel = new ScrollPanel();
        //VerticalPanel navVPanel = new VerticalPanel();
        DockLayoutPanel navVPanel = new DockLayoutPanel(Unit.EM);
        navVPanel.setSize("100%", "100%");
        //navVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);

        setupNavPanelLogo(navVPanel);
        setupNavPanelCopyRight(navVPanel);
        setupNavPanelTree(navVPanel, this.navTree);

        //navScrollPanel.add(navVPanel);
        //hPanel.addWest(navScrollPanel, NAV_WIDTH);
        hPanel.addWest(navVPanel, NAV_WIDTH);

        ScrollPanel mainScrollPanel = new ScrollPanel();
        this.mainPanel = new VerticalPanel();
        this.mainPanel.setWidth("100%");
        this.mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        this.mainPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        this.mainPanel.getElement().setId("main");

        this.paramHash        =    new HashMap<String, String>();
        String urlString    =    Window.Location.getHref();
        if (urlString.indexOf('#') >= 0) {
            urlString        =    urlString.substring(
                    urlString.indexOf('#') + 1);
            String[] historyPares = urlString.split(";");
            for (int i = 0; i < historyPares.length; i++) {
                String[] tokenPare = historyPares[i].split("=");
                if (tokenPare.length == 2) {
                    this.paramHash.put(tokenPare[0],
                            URL.decode(tokenPare[1]));
                }
            }
        }

        if (currentUser == null) {
            showLoginPanel();
            readLoginUser();
        } else {
            String page    =    this.paramHash.get("page");
            if (!this.menuFind(page, currentUser)) {
                showLoginPanel();
            }
        }


        mainScrollPanel.add(this.mainPanel);
        hPanel.add(mainScrollPanel);

        // Add image and button to the RootPanel
        RootPanel.get().add(hPanel);

        // Selection handler to handle clicks on the navigation tree
        this.navTree.addSelectionHandler(
                new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(
                    final SelectionEvent<TreeItem> event) {
                TreeItem item = event.getSelectedItem();
                String itemtext        =    item.getText();
                String itemtitle    =    item.getTitle();
                if (itemtitle != null
                 && itemtitle.length() > TITLE_CHECK_LENGTH) {
                    if (Parent.this.paramHash == null) {
                        Parent.this.paramHash =
                                new HashMap<String, String>();
                    }
                    Parent.this.paramHash.put("period",
                            itemtitle.substring(0, TITLE_CHECK_LENGTH));
                }
                DomainUser currentUser = getUser();
                menuFind(itemtext, currentUser);
            }

        });

        // Handler to handle page changes, to get back and
        // forward button work even in AJAX application.
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(
                    final ValueChangeEvent<String> event) {
                DomainUser currentUser = getUser();
                String historyToken = event.getValue();
                String[] historyPares = historyToken.split(";");
                Parent.this.paramHash = new HashMap<String, String>();
                for (int i = 0; i < historyPares.length; i++) {
                    String[] tokenPare = historyPares[i].split("=");
                    if (tokenPare.length == 2) {
                        Parent.this.paramHash.put(tokenPare[0],
                                URL.decode(tokenPare[1]));
                    }
                }
                String page = Parent.this.paramHash.get("page");
                if (currentUser != null) {
                    menuFind(page, currentUser);
                } else {
                    Parent.this.mainPanel.clear();
                    loginMatchesMenu(page, null);
                }
            }
        });

    }

    /**
     * The <code>getNavTree</code> method returns the
     * navigation tree, without building it once again.
     *
     * @return Navigation tree
     */
    public final Tree getNavTree() {
        return this.navTree;
    }

    /**
     * return the mainPanel.
     * @return main panel
     */
    public final VerticalPanel getMainPanel() {
        return this.mainPanel;
    }

    /**
     * return the paramHash.
     * @return hash parameter table
     */
    public final HashMap<String, String> getParamHash() {
        return this.paramHash;
    }

    /**
     * Create the logo.
     *
     * @param navVPanel to add the logo
     */
    protected final void setupNavPanelLogo(
            final DockLayoutPanel navVPanel) {
        String title        =    getApplicationTitle();
        VerticalPanel logoPanel = new VerticalPanel();
        // Get the title from the internationalized constants
        String pageTitle    =    "<h1>" + title + "</h1>";
        HTML htmlPageTitle    =    new HTML(pageTitle);
        Image img = getApplicationImage();
        img.getElement().setId("pc-template-img");
        img.setAltText(title);

        // Add the title and some images to the title bar
        logoPanel.add(img);
        logoPanel.setCellHorizontalAlignment(img,
                VerticalPanel.ALIGN_CENTER);
        logoPanel.setCellVerticalAlignment(img,
                VerticalPanel.ALIGN_TOP);
        logoPanel.add(htmlPageTitle);
        logoPanel.setCellHorizontalAlignment(htmlPageTitle,
                VerticalPanel.ALIGN_CENTER);
        logoPanel.setCellVerticalAlignment(htmlPageTitle,
                VerticalPanel.ALIGN_TOP);
        logoPanel.setWidth("100%");
        logoPanel.setBorderWidth(0);
        logoPanel.setSpacing(0);

        navVPanel.addNorth(logoPanel, LOGO_PANEL_HIGTH);
    }

    /**
     * Create the title bar at the top of the application.
     *
     * @param navVPanel the panel to add the navTree
     * @param newNavTree to add
     */
    protected final void setupNavPanelTree(
            final DockLayoutPanel navVPanel,
            final Tree newNavTree) {
        // Add the title and some images to the title bar
        ScrollPanel navScrollerPanel    =    new ScrollPanel();
        navScrollerPanel.add(newNavTree);
        navScrollerPanel.setSize("100%", "100%");

        navVPanel.add(navScrollerPanel);
    }

    /**
     * Create the title bar at the top of the application.
     *
     * @param navVPanel to add the CopyRight entry
     */
    protected abstract void setupNavPanelCopyRight(
            final DockLayoutPanel navVPanel);
}
