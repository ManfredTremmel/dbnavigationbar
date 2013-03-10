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
 * --
 * Name Date Change
 */
package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.dbnavigationbar.client.AbstractParent;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainDBBasics;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.ui.widget.DBNaviBarWidget;

/**
 * 
 * The <code>AbstractBasicDBTemplateUI</code> class is a template for database
 * input mask.
 * 
 * @param <E>
 *        data structure
 * @param <F>
 *        parent widget
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-14
 */
public abstract class AbstractBasicDBTemplateUI<E extends AbstractDomainDBBasics, F extends AbstractParent>
    extends AbstractBasicTemplateUI<F> implements AsyncCallback<E>
{

  /**
   * images.
   */
  protected static final Images IMAGES = GWT.create(Images.class);

  /**
   * navigation bar.
   */
  private final DBNaviBarWidget myNavigationBar;

  /**
   * form panel.
   */
  private final FormPanel form;

  /**
   * do save.
   */
  private boolean dosave = false;

  /**
   * Pictures used.
   */
  public interface Images extends ClientBundle
  {
    /**
     * db head picture.
     * 
     * @return image
     */
    @Source("BasicDBTemplateUI.png")
    ImageResource dbHeadTemplateUI();

    /**
     * delete position picture.
     * 
     * @return image
     */
    @Source("DeletePosition.png")
    ImageResource deletePosition();
  }

  /**
   * db entry.
   */
  private E dbEntry;

  /**
   * constants.
   */
  private final BasicDBTemplateUIConstants constants;

  /**
   * widget list.
   */
  private final Widget[] widgetlist;

  /**
   * mask is set up.
   */
  private boolean maskSetUp;

  /**
   * Constructor.
   * 
   * @param parentwidget
   *        the parent widget, where this frame is displayed
   * @param thisWidgetlist
   *        list of widgets to display
   * @param userdefinedfunction
   *        special function to include into the navigation bar
   */
  public AbstractBasicDBTemplateUI(
      final F parentwidget,
      final Widget[] thisWidgetlist,
      final String userdefinedfunction)
  {

    super(parentwidget);
    this.maskSetUp = false;
    this.widgetlist = thisWidgetlist;

    this.constants = (BasicDBTemplateUIConstants) GWT.create(BasicDBTemplateUIConstants.class);

    this.myNavigationBar = new DBNaviBarWidget(this.getSearchFields(), this.getSearchFieldsDisplay(), userdefinedfunction);
    this.form = new FormPanel();

    this.initWidget(this.form);

  }

  /**
   * set up the mask.
   * 
   * @param user
   *        user information about the currently logged in user
   */
  @Override
  protected final void setUpMask(final AbstractDomainUser user)
  {
    if (!this.maskSetUp)
    {
      final VerticalPanel myPanel = new VerticalPanel();
      myPanel.setWidth("100%");
      myPanel.setHeight("100%");
      myPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

      final HorizontalPanel titlePanel = new HorizontalPanel();
      titlePanel.setWidth("100%");
      titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      final String pageTitle = "<h1>" + this.getHeaderTitle() + "</h1>";
      final HTML htmlPageTitle = new HTML(pageTitle);
      titlePanel.add(htmlPageTitle);

      myPanel.add(titlePanel);

      myPanel.add(this.myNavigationBar);

      myPanel.add(this.createAndFormatContentPanel());

      this.form.add(myPanel);

      this.myNavigationBar.addClickHandler(new ClickHandler()
      {
        @Override
        public void onClick(final ClickEvent event)
        {
          AbstractBasicDBTemplateUI.this.dosave = false;
          final DBTemplateRemoteServiceAsync<E> service = AbstractBasicDBTemplateUI.this.getServiceFactory();

          switch (AbstractBasicDBTemplateUI.this.myNavigationBar.getButtonState())
          {
            case NEW:
              AbstractBasicDBTemplateUI.this.newEntry();
              break;
            case DELETE:
              service.deleteEntry(AbstractBasicDBTemplateUI.this.myNavigationBar.getOldDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case STOP:
              AbstractBasicDBTemplateUI.this.fillEntry(AbstractBasicDBTemplateUI.this.dbEntry);
              break;
            case FAST_BACK:
              service.readFirstEntry(AbstractBasicDBTemplateUI.this);
              break;
            case FAST_BACK_FIND:
              service.findFirstEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldField(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case BACK:
              service.readPreviousEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getOldDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case BACK_FIND:
              service.findPreviousEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldField(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case FORWARD:
              service.readNextEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getOldDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case FORWARD_FIND:
              service.findNextEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldField(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case FAST_FORWARD:
              service.readLastEntry(
                  AbstractBasicDBTemplateUI.this);
              break;
            case FAST_FORWARD_FIND:
              service.findLastEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldField(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case CHANGE:
              service.readEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case USER_DEFINED:
              AbstractBasicDBTemplateUI.this.userDefinedFunction();
              break;
            default:
              final E saveentry = AbstractBasicDBTemplateUI.this.checkInput();
              if (saveentry != null
                  && !saveentry.equals(AbstractBasicDBTemplateUI.this.dbEntry))
              {
                AbstractBasicDBTemplateUI.this.dosave = true;
                service.saveEntry(saveentry, AbstractBasicDBTemplateUI.this);
              }
              break;
          }
        }
      });
    }
    this.maskSetUp = true;
  }

  /**
   * get the service to use.
   * 
   * @return new service
   */
  protected abstract DBTemplateRemoteServiceAsync<E> getServiceFactory();

  /**
   * get fieldlist of search fields.
   * 
   * @return fieldlist
   */
  protected abstract String[] getSearchFields();

  /**
   * get fieldlist to display of search fields.
   * 
   * @return fieldlist to display
   */
  protected abstract String[] getSearchFieldsDisplay();

  /**
   * <code>checkInput</code> check the input.
   * 
   * @return input data or null if something went wrong
   */
  protected abstract E checkInput();

  /**
   * <code>newEntry</code> creates a new entry.
   */
  protected final void newEntry()
  {
    this.myNavigationBar.setNewEntry();
    this.dbEntry.setUpDefaultEntry();

    this.enableKeyField(true);

    this.fillEntry(this.dbEntry);
  }

  /**
   * <code>createAndFormatContentPanel</code> create the panel.
   * 
   * @return Panel
   */
  protected abstract Panel createAndFormatContentPanel();

  /**
   * <code>enableKeyField</code> enables/disables KeyField.
   * 
   * @param enable
   *        true/false to enable/disable key for input
   */
  protected abstract void enableKeyField(boolean enable);

  /**
   * <code>UserDefinedFunction</code> can be used for additional
   * functionality that can be used by a button in the navigation widget.
   */
  protected void userDefinedFunction()
  {
    // redefine if you want to include a user defined function
  }

  /**
   * <code>onFailure</code> is called, when server side an
   * exception is thrown.
   * 
   * @param caught
   *        the thrown exception
   */
  @Override
  public final void onFailure(final Throwable caught)
  {
    try
    {
      throw caught;
    }
    catch (final RpcTokenException e)
    {
      this.getParentwidget().cleanUp(e.getLocalizedMessage());
    }
    catch (final Throwable e)
    {
      this.myNavigationBar.displayHint(e.toString());
    }
  }

  /**
   * <code>onSuccess</code> is called, when server side call
   * was successfully.
   * 
   * @param entry
   *        the returnvalue of the serverside function
   */
  @Override
  public final void onSuccess(final E entry)
  {
    if (entry == null)
    {
      this.getParentwidget().cleanUp(null);
    }
    else if (entry.getKeyCur() == null)
    {
      if (this.dbEntry != null)
      {
        this.myNavigationBar.setDBMinMaxCurNumber(
            this.dbEntry.getKeyMin(),
            this.dbEntry.getKeyMax(),
            this.dbEntry.getKeyCur());
      }
      this.myNavigationBar.displayHint(this.constants.searchErrorMessage());
    }
    else
    {
      this.dbEntry = entry;
      this.myNavigationBar.setDBMinMaxCurNumber(entry.getKeyMin(), entry.getKeyMax(), entry.getKeyCur());
      if (entry.isReadOnly())
      {
        this.myNavigationBar.setReadOnly();
      }
      else
      {
        this.myNavigationBar.setReadWrite();
      }

      if (this.dosave)
      {
        this.myNavigationBar.displayHint(this.constants.savedSuccessfully());
      }
      else
      {
        this.myNavigationBar.displayHint(null);
      }

      this.enableKeyField(entry.getKeyCur() == null || "".equals(entry.getKeyCur()));
      this.fillEntry(entry);
    }
  }

  /**
   * <code>fillEntry</code> is called, to display a user.
   * 
   * @param entry
   *        entry to display
   */
  protected abstract void fillEntry(E entry);

  /**
   * The Method <code>matchesMenu</code> looks if this UI is selected and
   * makes the necessary changes.
   * 
   * @param itemtext
   *        selected menu item
   * @param user
   *        user information about the currently logged in user
   * @return true if it is allowed for this user
   */
  @Override
  public final boolean matchesMenu(
      final String itemtext,
      final AbstractDomainUser user)
  {
    final boolean matches = super.matchesMenuSimple(itemtext, user);
    if (matches)
    {
      if (this.allowedToChange(user))
      {
        this.myNavigationBar.setReadWrite();
      }
      else
      {
        this.myNavigationBar.setReadOnly();
      }
      if (this.myNavigationBar.getOldDBNumber() == null)
      {
        final DBTemplateRemoteServiceAsync<E> service = this.getServiceFactory();
        service.readLastEntry(AbstractBasicDBTemplateUI.this);
      }
    }
    return matches;
  }

  /**
   * get navigation bar.
   * 
   * @return myNavigationBar
   */
  protected final DBNaviBarWidget getMyNavigationBar()
  {
    return this.myNavigationBar;
  }

  /**
   * get dosave.
   * 
   * @return dosave
   */
  protected final boolean isDosave()
  {
    return this.dosave;
  }

  /**
   * set dosave.
   * 
   * @param newDosave
   *        new entry for dosave
   */
  protected final void setDosave(final boolean newDosave)
  {
    this.dosave = newDosave;
  }

  /**
   * get db entry.
   * 
   * @return dbEntry
   */
  protected final E getDbEntry()
  {
    return this.dbEntry;
  }

  /**
   * set db entry.
   * 
   * @param newDbEntry
   *        new database entry
   */
  protected final void setDbEntry(final E newDbEntry)
  {
    this.dbEntry = newDbEntry;
  }

  /**
   * get constants.
   * 
   * @return constants
   */
  protected final BasicDBTemplateUIConstants getConstants()
  {
    return this.constants;
  }

  /**
   * get widget list.
   * 
   * @return widgetlist
   */
  protected final Widget[] getWidgetlist()
  {
    return this.widgetlist;
  }

  /**
   * @return the maskSetUp
   */
  public final boolean isMaskSetUp()
  {
    return this.maskSetUp;
  }
}
