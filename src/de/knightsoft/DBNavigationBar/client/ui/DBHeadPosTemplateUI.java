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
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 * --
 *  Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.ui;

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

import de.knightsoft.DBNavigationBar.client.Parent;
import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBase;
import de.knightsoft.DBNavigationBar.client.ui.widget.DBNaviBarWidgetConstants;

/**
 * The <code>DBHeadTemplateUI</code> class is a template
 * for database input mask.
 *
 * @param <E> Structure Type
 * @param <F> Parent panel
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-19
 */
public abstract class DBHeadPosTemplateUI<E extends DomainHeadPosDataBase,
    F extends Parent> extends BasicDBTemplateUI<E, F> {

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
     * @param parentwidget
     *         the parent widget, where this frame is displayed
     * @param widgetlist
     *         list of widgets to display
     * @param userdefinedfunction
     *         special function to include into the navigation bar
     */
    public DBHeadPosTemplateUI(
            final F parentwidget,
            final Widget[] widgetlist,
            final String userdefinedfunction) {

        super(parentwidget, widgetlist, userdefinedfunction);

        this.setNewPositionButton();

        this.rowToDelete    = -1;
        this.dialogYesNoBox =
                createYesNoDialogBox(this.getMyNavigationBar().getConstants());
        this.dialogYesNoBox.hide();
    }

    /**
     * set up a new position button.
     */
    protected final void setNewPositionButton() {
        if (this.getConstantsPos() == null) {
            this.setConstantsPos((DBHeadPosTemplateUIConstants)
                 GWT.create(DBHeadPosTemplateUIConstants.class));
        }

        if (this.newPositionButton == null) {
            this.newPositionButton =
                    new Button(getConstantsPos().addPositionButton(),
                new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                        fillPosition(
                                DBHeadPosTemplateUI.this.getPosTable()
                                .getRowCount(),
                                    null);
                      }
                });
        }
    }

    /**
     * set up position panel.
     * @param headerValues names of the header
     */
    protected final void setupPosPanel(final String[] headerValues) {
        this.setPosPanel(new VerticalPanel());
        this.setPosTable(new FlexTable());

        for (int i = 0; i < headerValues.length; i++) {
            this.getPosTable().setText(0, i, headerValues[i]);
            this.getPosTable().getCellFormatter()
                 .setStyleName(0, i, "thlistcentergrey");
        }

        this.getPosTable().setWidth("100%");
        this.getPosPanel().setHorizontalAlignment(
                HasHorizontalAlignment.ALIGN_CENTER);
        this.getPosPanel().setWidth("100%");
        this.getPosPanel().add(getPosTable());
    }

    /**
     * <code>fillEntry</code> is called, to display a user.
     *
     *  @param entry
     *          entry to display
     */
    @Override
    protected abstract void fillEntry(E entry);


    /**
     * <code>fillPositions</code> displays all Users.
     *
     *  @param entry where to take infos from
     */
    protected final void fillPositions(final E entry) {

        this.getPosTable().setVisible(false);
        if (entry.getKeyPos() != null) {

            for (int i = (this.getPosTable().getRowCount() - 1);
                 i > entry.getKeyPos().length && i > 0; i--) {
                this.getPosTable().removeRow(i);
            }

            for (int numRows = 0; numRows < entry.getKeyPos().length;
                 numRows++) {
                fillPosition(numRows + 1,
                             entry
                           );
            }
        } else {
            for (int i = (this.getPosTable().getRowCount() - 1); i > 0; i--) {
                this.getPosTable().removeRow(i);
            }
        }
        this.getPosTable().setVisible(true);
    }

    /**
     * <code>fillPosition</code> displays one position.
     *
     *  @param row to fill
     *  @param entry wher to take information from
     */
    protected abstract void fillPosition(
            final int row,
            final E entry
           );

    /**
     * <code>getDeleteButton</code> creates a Button to delete a row.
     *
     * @return PushButton
     */
    protected final PushButton getDeleteButton() {
        PushButton deleteButton =    new PushButton(
                new Image(DBHeadTemplateUI.IMAGES.deletePosition()),
                          new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                        PushButton sender = (PushButton) event.getSource();
                        for (int i = 1;
                             i < DBHeadPosTemplateUI.this.getPosTable()
                             .getRowCount(); i++) {
                            if (sender.equals(DBHeadPosTemplateUI.this
                               .getPosTable().getWidget(i,
                                 (getPosTable().getCellCount(i) - 1)))) {
                                DBHeadPosTemplateUI.this.rowToDelete = i;
                                DBHeadPosTemplateUI.this.dialogYesNoBox
                                      .center();
                                DBHeadPosTemplateUI.this.dialogYesNoBox.show();
                                i = DBHeadPosTemplateUI.this.getPosTable()
                                      .getRowCount();
                            }
                        }
                    }
                });
        return deleteButton;
    }


    /**
     * The Method <code>createYesNoDialogBox</code>
     * creates a Yes/No Dialog box.
     *
     * @param constants to take texts from
     * @return DialogBox
     */
    private DialogBox createYesNoDialogBox(
            final DBNaviBarWidgetConstants constants) {
        // Create a dialog box and set the caption text
        final DialogBox dialogBox = new DialogBox();
        dialogBox.ensureDebugId("yesNoDialogBox");
        dialogBox.setText(constants.deleteDialogHeader());

        // Create a table to layout the content
        VerticalPanel dialogContents = new VerticalPanel();
        dialogBox.setWidget(dialogContents);

        // Add some text to the top of the dialog
        HTML details = new HTML(constants.deleteDialogText());
        dialogContents.add(details);
        dialogContents.setHorizontalAlignment(
                HasHorizontalAlignment.ALIGN_CENTER);

        HorizontalPanel dialogButtons = new HorizontalPanel();
        // Add a yes button at the bottom of the dialog
        Button yesButton = new Button(constants.yes(),
            new ClickHandler() {
              @Override
            public void onClick(final ClickEvent event) {
                dialogBox.hide();
                DBHeadPosTemplateUI.this.getPosTable().removeRow(
                        DBHeadPosTemplateUI.this.rowToDelete);
              }
            });
        yesButton.setAccessKey(constants.yesKey().trim().charAt(0));
        dialogButtons.add(yesButton);
        dialogButtons.setCellHorizontalAlignment(yesButton,
                HasHorizontalAlignment.ALIGN_LEFT);
        // Add a no button at the bottom of the dialog
        Button noButton = new Button(constants.no(),
            new ClickHandler() {
              @Override
            public void onClick(final ClickEvent event) {
                dialogBox.hide();
              }
            });
        noButton.setAccessKey(constants.noKey().trim().charAt(0));
        dialogButtons.add(noButton);
        dialogButtons.setCellHorizontalAlignment(noButton,
                HasHorizontalAlignment.ALIGN_RIGHT);
        dialogButtons.setWidth("80%");
        dialogContents.add(dialogButtons);

        // Return the dialog box
        return dialogBox;
    }

    /**
     * get position table.
     * @return posTable
     */
    public final FlexTable getPosTable() {
        return this.posTable;
    }

    /**
     * set position table.
     * @param newPosTable
     *         position table
     */
    public final void setPosTable(final FlexTable newPosTable) {
        this.posTable = newPosTable;
    }

    /**
     * get new position button.
     * @return new position button
     */
    public final Button getNewPositionButton() {
        return newPositionButton;
    }

    /**
     * set new position button.
     * @param setPositionButton
     *         new position button
     */
    public final void setNewPositionButton(final Button setPositionButton) {
        this.newPositionButton = setPositionButton;
    }

    /**
     * get position panel.
     * @return posPanel
     */
    public final VerticalPanel getPosPanel() {
        return posPanel;
    }

    /**
     * set position panel.
     * @param newPosPanel
     *         position panel
     */
    public final void setPosPanel(final VerticalPanel newPosPanel) {
        this.posPanel = newPosPanel;
    }

    /**
     * get constants from positions.
     * @return constantsPos
     */
    public final DBHeadPosTemplateUIConstants getConstantsPos() {
        return constantsPos;
    }

    /**
     * set constants from positions.
     * @param newConstantsPos
     *         new constantsPos
     */
    public final void setConstantsPos(
            final DBHeadPosTemplateUIConstants newConstantsPos) {
        this.constantsPos = newConstantsPos;
    }
}
