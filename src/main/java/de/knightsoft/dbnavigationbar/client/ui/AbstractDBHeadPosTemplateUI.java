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

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainHeadPosDB;
import de.knightsoft.dbnavigationbar.client.ui.widget.DBNaviBarWidgetConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The <code>AbstractDBHeadTemplateUI</code> class is a template for database input mask.
 *
 * @param <E> Structure Type
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-19
 */
public abstract class AbstractDBHeadPosTemplateUI<E extends AbstractDomainHeadPosDB>
    extends AbstractBasicDBTemplateUI<E> {

  /**
   * constantsPos.
   */
  private DBHeadPosTemplateUIConstants constantsPos;

  /**
   * new position button.
   */
  private Button newPositionButton;

  /**
   * position panel.
   */
  private VerticalPanel posPanel;

  /**
   * position table.
   */
  private FlexTable posTable;

  /**
   * number of the row to delete.
   */
  private int rowToDelete;

  /**
   * yes/no dialog box.
   */
  private final DialogBox dialogYesNoBox;

  /**
   * Constructor.
   *
   * @param pParentwidget the parent widget, where this frame is displayed
   * @param pWidgetlist list of widgets to display
   * @param pUserdefinedfunction special function to include into the navigation bar
   */
  public AbstractDBHeadPosTemplateUI(final Widget[] pWidgetlist,
      final String pUserdefinedfunction) {

    super(pWidgetlist, pUserdefinedfunction);

    this.setNewPositionButton();

    this.rowToDelete = -1;
    this.dialogYesNoBox = this.createYesNoDialogBox(this.getMyNavigationBar().getConstants());
    this.dialogYesNoBox.hide();
  }

  /**
   * set up a new position button.
   */
  protected final void setNewPositionButton() {
    if (this.getConstantsPos() == null) {
      this.setConstantsPos(
          (DBHeadPosTemplateUIConstants) GWT.create(DBHeadPosTemplateUIConstants.class));
    }

    if (this.newPositionButton == null) {
      this.newPositionButton =
          new Button(this.getConstantsPos().addPositionButton(), new ClickHandler() {
            @Override
            public void onClick(final ClickEvent pEvent) {
              AbstractDBHeadPosTemplateUI.this
                  .fillPosition(AbstractDBHeadPosTemplateUI.this.getPosTable().getRowCount(), null);
            }
          });
    }
  }

  /**
   * set new position button.
   *
   * @param pNewPositionButton new position button
   */
  public final void setNewPositionButton(final Button pNewPositionButton) {
    this.newPositionButton = pNewPositionButton;
  }

  /**
   * set up position panel.
   *
   * @param pHeaderValues names of the header
   */
  protected final void setupPosPanel(final String[] pHeaderValues) {
    this.setPosPanel(new VerticalPanel());
    this.setPosTable(new FlexTable());

    for (int i = 0; i < pHeaderValues.length; i++) {
      this.getPosTable().setText(0, i, pHeaderValues[i]);
      this.getPosTable().getCellFormatter().setStyleName(0, i, "thlistcentergrey");
    }

    this.getPosTable().setWidth("100%");
    this.getPosPanel().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    this.getPosPanel().setWidth("100%");
    this.getPosPanel().add(this.getPosTable());
  }

  /**
   * <code>fillEntry</code> is called, to display a user.
   *
   * @param pEntry entry to display
   */
  @Override
  protected abstract void fillEntry(E pEntry);

  /**
   * <code>fillPositions</code> displays all Users.
   *
   * @param pEntry where to take infos from
   */
  protected final void fillPositions(final E pEntry) {

    this.getPosTable().setVisible(false);
    if (pEntry.getKeyPos() == null) {
      for (int i = this.getPosTable().getRowCount() - 1; i > 0; i--) {
        this.getPosTable().removeRow(i);
      }
    } else {
      for (int i = this.getPosTable().getRowCount() - 1; i > pEntry.getKeyPos().length
          && i > 0; i--) {
        this.getPosTable().removeRow(i);
      }

      for (int numRows = 0; numRows < pEntry.getKeyPos().length; numRows++) {
        this.fillPosition(numRows + 1, pEntry);
      }
    }
    this.getPosTable().setVisible(true);
  }

  /**
   * <code>fillPosition</code> displays one position.
   *
   * @param pRow to fill
   * @param pEntry wher to take information from
   */
  protected abstract void fillPosition(final int pRow, final E pEntry);

  /**
   * <code>getDeleteButton</code> creates a Button to delete a row.
   *
   * @return PushButton
   */
  protected final PushButton getDeleteButton() {
    final PushButton deleteButton = new PushButton(
        new Image(AbstractBasicDBTemplateUI.IMAGES.deletePosition()), new ClickHandler() {
          @Override
          public void onClick(final ClickEvent pEvent) {
            final PushButton sender = (PushButton) pEvent.getSource();
            for (int i = 1; i < AbstractDBHeadPosTemplateUI.this.getPosTable().getRowCount(); i++) {
              if (sender.equals(AbstractDBHeadPosTemplateUI.this.getPosTable().getWidget(i,
                  AbstractDBHeadPosTemplateUI.this.getPosTable().getCellCount(i) - 1))) {
                AbstractDBHeadPosTemplateUI.this.rowToDelete = i;
                AbstractDBHeadPosTemplateUI.this.dialogYesNoBox.center();
                AbstractDBHeadPosTemplateUI.this.dialogYesNoBox.show();
                i = AbstractDBHeadPosTemplateUI.this.getPosTable().getRowCount();
              }
            }
          }
        });
    return deleteButton;
  }

  /**
   * The Method <code>createYesNoDialogBox</code> creates a Yes/No Dialog box.
   *
   * @param pConstants to take texts from
   * @return DialogBox
   */
  private DialogBox createYesNoDialogBox(final DBNaviBarWidgetConstants pConstants) {
    // Create a dialog box and set the caption text
    final DialogBox dialogBox = new DialogBox();
    dialogBox.ensureDebugId("yesNoDialogBox");
    dialogBox.setText(pConstants.deleteDialogHeader());

    // Create a table to layout the content
    final VerticalPanel dialogContents = new VerticalPanel();
    dialogBox.setWidget(dialogContents);

    // Add some text to the top of the dialog
    final HTML details = new HTML(pConstants.deleteDialogText());
    dialogContents.add(details);
    dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    final HorizontalPanel dialogButtons = new HorizontalPanel();
    // Add a yes button at the bottom of the dialog
    final Button yesButton = new Button(pConstants.yes(), new ClickHandler() {
      @Override
      public void onClick(final ClickEvent pEvent) {
        dialogBox.hide();
        AbstractDBHeadPosTemplateUI.this.getPosTable()
            .removeRow(AbstractDBHeadPosTemplateUI.this.rowToDelete);
      }
    });
    yesButton.setAccessKey(pConstants.yesKey().trim().charAt(0));
    dialogButtons.add(yesButton);
    dialogButtons.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_LEFT);
    // Add a no button at the bottom of the dialog
    final Button noButton = new Button(pConstants.no(), new ClickHandler() {
      @Override
      public void onClick(final ClickEvent pEvent) {
        dialogBox.hide();
      }
    });
    noButton.setAccessKey(pConstants.noKey().trim().charAt(0));
    dialogButtons.add(noButton);
    dialogButtons.setCellHorizontalAlignment(noButton, HasHorizontalAlignment.ALIGN_RIGHT);
    dialogButtons.setWidth("80%");
    dialogContents.add(dialogButtons);

    // Return the dialog box
    return dialogBox;
  }

  /**
   * get position table.
   *
   * @return posTable
   */
  public final FlexTable getPosTable() {
    return this.posTable;
  }

  /**
   * set position table.
   *
   * @param pPosTable position table
   */
  public final void setPosTable(final FlexTable pPosTable) {
    this.posTable = pPosTable;
  }

  /**
   * get new position button.
   *
   * @return new position button
   */
  public final Button getNewPositionButton() {
    return this.newPositionButton;
  }

  /**
   * get position panel.
   *
   * @return posPanel
   */
  public final VerticalPanel getPosPanel() {
    return this.posPanel;
  }

  /**
   * set position panel.
   *
   * @param pPosPanel position panel
   */
  public final void setPosPanel(final VerticalPanel pPosPanel) {
    this.posPanel = pPosPanel;
  }

  /**
   * get constants from positions.
   *
   * @return constantsPos
   */
  public final DBHeadPosTemplateUIConstants getConstantsPos() {
    return this.constantsPos;
  }

  /**
   * set constants from positions.
   *
   * @param pConstantsPos new constantsPos
   */
  public final void setConstantsPos(final DBHeadPosTemplateUIConstants pConstantsPos) {
    this.constantsPos = pConstantsPos;
  }
}
