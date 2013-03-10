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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client;

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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.ui.BasicTemplateUIInterface;

/**
 * 
 * The <code>AbstractParent</code> class is the entry point class which
 * defines <code>onModuleLoad()</code> and is executed when the
 * website is loeded. Just a template that has to be implemented.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractParent implements EntryPoint
{

  /**
   * navigation panel width.
   */
  public static final int NAV_WIDTH = 250;

  /**
   * item title check length.
   */
  public static final int TITLE_CHECK_LENGTH = 6;

  /**
   * logo panel height.
   */
  public static final int LOGO_PANEL_HEIGHT = 12;

  /**
   * copyright panel height.
   */
  public static final double COPYRIGHT_PANEL_HEIGHT = 1.5;

  /**
   * mainPanel on the web site.
   */
  private ScrollPanel mainPanel;

  /**
   * split layout panel left navigation right content.
   */
  private final SplitLayoutPanel hPanel = new SplitLayoutPanel();

  /**
   * navigation tree.
   */
  private final Tree navTree = new Tree();

  /**
   * Remember given parameters in url.
   */
  private HashMap<String, String> paramHash = null;

  /**
   * Get the data of the logged in user.
   * 
   * @return User
   */
  public abstract AbstractDomainUser getUser();

  /**
   * get Copyright String.
   * 
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
   * 
   * @param page
   *        page the compare
   * @param currentUser
   *        current logged in user
   * @return true/false
   */
  protected abstract boolean loginMatchesMenu(String page,
      AbstractDomainUser currentUser);

  /**
   * get the Image of the Application.
   * 
   * @return Image
   */
  protected abstract Image getApplicationImage();

  /**
   * get the Application Title.
   * 
   * @return Application Title
   */
  protected abstract String getApplicationTitle();

  /**
   * The <code>menuFind</code> method checks if a given menu entry or
   * history selection belongs to one of the subpages of RiPhone.
   * 
   * @param itemtext
   *        Name of the selection
   * @param currentUser
   *        The logged in user, null if non is logged in
   * @return true if a entry matches, false if non is found
   */
  public abstract boolean menuFind(
      String itemtext,
      AbstractDomainUser currentUser);

  /**
   * The <code>cleanUp</code> method can be called if user
   * logs off or session is timed out.
   * 
   * @param text
   *        to display
   */
  public abstract void cleanUp(String text);

  /**
   * The <code>buildNavTree</code> method builds the navigation
   * tree.
   * 
   * @param user
   *        The logged in user, null if non is logged in
   * @return Navigation tree
   */
  public abstract Tree buildNavTree(AbstractDomainUser user);

  /**
   * This is the entry point method.
   */
  public final void onModuleLoadBase()
  {

    // Get the title from the internationalized constants
    this.buildNavTree(null);

    Window.enableScrolling(false);
    Window.setMargin("0px");

    // Horizontal Panel, left navigation, right content
    this.hPanel.setSize("100%", "100%");

    // ScrollPanel navScrollPanel = new ScrollPanel();
    final DockLayoutPanel navVPanel = new DockLayoutPanel(Unit.EM);
    navVPanel.setSize("100%", "100%");

    this.setupNavPanelLogo(navVPanel);
    this.setupNavPanelCopyRight(navVPanel);
    this.setupNavPanelTree(navVPanel, this.navTree);

    this.hPanel.addWest(navVPanel, NAV_WIDTH);

    this.mainPanel = new ScrollPanel();
    this.mainPanel.setSize("100%", "100%");
    this.mainPanel.getElement().setId("main");

    this.paramHash = new HashMap<String, String>();
    String urlString = Window.Location.getHref();
    if (urlString.indexOf('#') >= 0)
    {
      urlString = urlString.substring(
          urlString.indexOf('#') + 1);
      final String[] historyPares = urlString.split(";");
      for (final String historyPare : historyPares)
      {
        final String[] tokenPare = historyPare.split("=");
        if (tokenPare.length == 2)
        {
          this.paramHash.put(tokenPare[0],
              URL.decode(tokenPare[1]));
        }
      }
    }

    this.readLoginUser();

    final String page = this.paramHash.get("page");
    if (!this.menuFind(page, null))
    {
      this.showLoginPanel();
    }

    this.hPanel.add(this.mainPanel);

    // Add image and button to the RootPanel
    RootLayoutPanel.get().add(this.hPanel);

    // Selection handler to handle clicks on the navigation tree
    this.navTree.addSelectionHandler(
        new SelectionHandler<TreeItem>()
        {
          @Override
          public void onSelection(final SelectionEvent<TreeItem> event)
          {
            final TreeItem item = event.getSelectedItem();
            String itemtext = item.getText();
            if (itemtext != null)
            {
              itemtext = itemtext.trim();
            }
            final String itemtitle = item.getTitle();
            if (itemtitle != null
                && itemtitle.length() > TITLE_CHECK_LENGTH)
            {
              if (AbstractParent.this.paramHash == null)
              {
                AbstractParent.this.paramHash =
                    new HashMap<String, String>();
              }
              AbstractParent.this.paramHash.put("period",
                  itemtitle.substring(0, TITLE_CHECK_LENGTH));
            }
            final AbstractDomainUser currentUser =
                AbstractParent.this.getUser();
            AbstractParent.this.menuFind(itemtext, currentUser);
          }

        });

    // Handler to handle page changes, to get back and
    // forward button work even in AJAX application.
    History.addValueChangeHandler(new ValueChangeHandler<String>()
    {
      @SuppressWarnings("unchecked")
      @Override
      public void onValueChange(
          final ValueChangeEvent<String> event)
      {
        final AbstractDomainUser currentUser = AbstractParent.this.getUser();
        final String historyToken = event.getValue();
        final String[] historyPares = historyToken.split(";");
        final HashMap<String, String> oldParamHash =
            AbstractParent.this.paramHash;
        AbstractParent.this.paramHash =
            new HashMap<String, String>(oldParamHash.size());
        for (final String historyPare : historyPares)
        {
          final String[] tokenPare = historyPare.split("=");
          if (tokenPare.length == 2)
          {
            AbstractParent.this.paramHash.put(tokenPare[0],
                URL.decode(tokenPare[1]));
          }
        }
        final String page = AbstractParent.this.paramHash.get("page");
        String oldPage = null;
        if (AbstractParent.this.mainPanel.getWidget() != null
            && (AbstractParent.this.mainPanel.getWidget()
            instanceof BasicTemplateUIInterface))
        {
          oldPage = ((BasicTemplateUIInterface<AbstractParent>)
              AbstractParent.this.mainPanel.getWidget())
                  .getMenuText();
        }
        if ((oldPage == null
            || !AbstractParent.this.paramHash.equals(oldParamHash))
            && !AbstractParent.this.menuFind(page, currentUser))
        {
          AbstractParent.this.mainPanel.clear();
          AbstractParent.this.loginMatchesMenu(page, null);
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
  public final Tree getNavTree()
  {
    return this.navTree;
  }

  /**
   * return the mainPanel.
   * 
   * @return main panel
   */
  public final ScrollPanel getMainPanel()
  {
    return this.mainPanel;
  }

  /**
   * return the split layout panel.
   * 
   * @return the hPanel
   */
  public final SplitLayoutPanel gethPanel()
  {
    return this.hPanel;
  }

  /**
   * return the paramHash.
   * 
   * @return hash parameter table
   */
  public final HashMap<String, String> getParamHash()
  {
    return this.paramHash;
  }

  /**
   * Create the logo.
   * 
   * @param navVPanel
   *        to add the logo
   */
  protected final void setupNavPanelLogo(final DockLayoutPanel navVPanel)
  {
    final String title = this.getApplicationTitle();
    final FlexTable logoPanel = new FlexTable();
    final FlexCellFormatter fcf = logoPanel.getFlexCellFormatter();
    // Get the title from the internationalized constants
    final String pageTitle = "<h1>" + title + "</h1>";
    final Image img = this.getApplicationImage();
    img.getElement().setId("pc-template-img");
    img.setAltText(title);
    final HTML htmlPageTitle = new HTML(pageTitle);

    // Add the title and some images to the title bar
    logoPanel.setWidget(0, 0, img);
    fcf.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
    fcf.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
    logoPanel.setWidget(1, 0, htmlPageTitle);
    fcf.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
    fcf.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
    logoPanel.setSize("100%", "100%");
    logoPanel.setBorderWidth(0);

    navVPanel.addNorth(logoPanel, LOGO_PANEL_HEIGHT);

  }

  /**
   * Create the title bar at the top of the application.
   * 
   * @param navVPanel
   *        the panel to add the navTree
   * @param newNavTree
   *        to add
   */
  protected final void setupNavPanelTree(
      final DockLayoutPanel navVPanel,
      final Tree newNavTree)
  {
    // Add the title and some images to the title bar
    final ScrollPanel navScrollerPanel = new ScrollPanel();
    navScrollerPanel.add(newNavTree);
    navScrollerPanel.setSize("100%", "100%");

    navVPanel.add(navScrollerPanel);
  }

  /**
   * Create the title bar at the top of the application.
   * 
   * @param navVPanel
   *        to add the CopyRight entry
   */
  protected abstract void setupNavPanelCopyRight(
      final DockLayoutPanel navVPanel);
}
