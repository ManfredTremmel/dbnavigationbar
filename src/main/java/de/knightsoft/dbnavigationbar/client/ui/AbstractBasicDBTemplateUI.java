/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainDBBasics;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;
import de.knightsoft.dbnavigationbar.client.ui.widget.DBNaviBarWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * The <code>AbstractBasicDBTemplateUI</code> class is a template for database input mask.
 *
 * @param <E> data structure
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-14
 */
public abstract class AbstractBasicDBTemplateUI<E extends AbstractDomainDBBasics>
    extends AbstractBasicTemplateUI implements AsyncCallback<E> {

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
  public interface Images extends ClientBundle {
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
   * @param pParentwidget the parent widget, where this frame is displayed
   * @param pThisWidgetlist list of widgets to display
   * @param pUserdefinedfunction special function to include into the navigation bar
   */
  public AbstractBasicDBTemplateUI(final Widget[] pThisWidgetlist,
      final String pUserdefinedfunction) {

    super();
    this.maskSetUp = false;
    this.widgetlist = pThisWidgetlist;

    this.constants = (BasicDBTemplateUIConstants) GWT.create(BasicDBTemplateUIConstants.class);

    this.myNavigationBar = new DBNaviBarWidget(this.getSearchFields(),
        this.getSearchFieldsDisplay(), pUserdefinedfunction);
    this.form = new FormPanel();

    this.initWidget(this.form);

  }

  /**
   * set up the mask.
   *
   * @param pUser user information about the currently logged in user
   */
  @Override
  protected final void setUpMask(final AbstractDomainUser pUser) {
    if (!this.maskSetUp) {
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

      this.myNavigationBar.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(final ClickEvent pEvent) {
          AbstractBasicDBTemplateUI.this.dosave = false;
          final DBTemplateRemoteServiceAsync<E> service =
              AbstractBasicDBTemplateUI.this.getServiceFactory();

          switch (AbstractBasicDBTemplateUI.this.myNavigationBar.getButtonState()) {
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
              service.readNextEntry(AbstractBasicDBTemplateUI.this.myNavigationBar.getOldDBNumber(),
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
              service.readLastEntry(AbstractBasicDBTemplateUI.this);
              break;
            case FAST_FORWARD_FIND:
              service.findLastEntry(
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldField(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldMethode(),
                  AbstractBasicDBTemplateUI.this.myNavigationBar.getSearchFieldEntry(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case CHANGE:
              service.readEntry(AbstractBasicDBTemplateUI.this.myNavigationBar.getCurrentDBNumber(),
                  AbstractBasicDBTemplateUI.this);
              break;
            case USER_DEFINED:
              AbstractBasicDBTemplateUI.this.userDefinedFunction();
              break;
            default:
              final E saveentry = AbstractBasicDBTemplateUI.this.checkInput();
              if (saveentry != null && !saveentry.equals(AbstractBasicDBTemplateUI.this.dbEntry)) {
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
  protected final void newEntry() {
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
   * @param pEnable true/false to enable/disable key for input
   */
  protected abstract void enableKeyField(boolean pEnable);

  /**
   * <code>UserDefinedFunction</code> can be used for additional functionality that can be used by a
   * button in the navigation widget.
   */
  protected void userDefinedFunction() {
    // redefine if you want to include a user defined function
  }

  /**
   * <code>onFailure</code> is called, when server side an exception is thrown.
   *
   * @param pCaught the thrown exception
   */
  @Override
  public final void onFailure(final Throwable pCaught) {
    try {
      throw pCaught;
    } catch (final Throwable e) {
      this.myNavigationBar.displayHint(e.toString());
    }
  }

  /**
   * <code>onSuccess</code> is called, when server side call was successfully.
   *
   * @param pEntry the returnvalue of the serverside function
   */
  @Override
  public final void onSuccess(final E pEntry) {
    if (pEntry == null) {
      // no data
    } else if (pEntry.getKeyCur() == null) {
      if (this.dbEntry != null) {
        this.myNavigationBar.setDBMinMaxCurNumber(this.dbEntry.getKeyMin(),
            this.dbEntry.getKeyMax(), this.dbEntry.getKeyCur());
      }
      this.myNavigationBar.displayHint(this.constants.searchErrorMessage());
    } else {
      this.dbEntry = pEntry;
      this.myNavigationBar.setDBMinMaxCurNumber(pEntry.getKeyMin(), pEntry.getKeyMax(),
          pEntry.getKeyCur());
      if (pEntry.isReadOnly()) {
        this.myNavigationBar.setReadOnly();
      } else {
        this.myNavigationBar.setReadWrite();
      }

      if (this.dosave) {
        this.myNavigationBar.displayHint(this.constants.savedSuccessfully());
      } else {
        this.myNavigationBar.displayHint(null);
      }

      this.enableKeyField(StringUtils.isEmpty(pEntry.getKeyCur()));
      this.fillEntry(pEntry);
    }
  }

  /**
   * <code>fillEntry</code> is called, to display a user.
   *
   * @param pEntry entry to display
   */
  protected abstract void fillEntry(E pEntry);

  protected final DBNaviBarWidget getMyNavigationBar() {
    return this.myNavigationBar;
  }

  protected final boolean isDosave() {
    return this.dosave;
  }

  protected final void setDosave(final boolean pDosave) {
    this.dosave = pDosave;
  }

  protected final E getDbEntry() {
    return this.dbEntry;
  }

  protected final void setDbEntry(final E pDbEntry) {
    this.dbEntry = pDbEntry;
  }

  protected final BasicDBTemplateUIConstants getConstants() {
    return this.constants;
  }

  protected final Widget[] getWidgetlist() {
    return this.widgetlist;
  }

  public final boolean isMaskSetUp() {
    return this.maskSetUp;
  }

  @Override
  public void onReveal(final AbstractDomainUser pSessionUser) {
    super.onReveal(pSessionUser);
    this.setUpMask(pSessionUser);
    this.setFocusOnFirstWidget();
    if (this.allowedToChange(pSessionUser)) {
      this.myNavigationBar.setReadWrite();
    } else {
      this.myNavigationBar.setReadOnly();
    }
    if (this.myNavigationBar.getOldDBNumber() == null) {
      AbstractBasicDBTemplateUI.this.getServiceFactory()
          .readLastEntry(AbstractBasicDBTemplateUI.this);
    }
  }
}
