package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * view manager to manage all different views and updates views
 */
public class ViewManager implements ViewManagerModelListener {
    private final CardLayout cardLayout;
    private final ViewManagerModel viewManagerModel;
    private final JPanel views;

    /**
     *
     * @param views - JPanel with all views
     * @param cardLayout - layout to switch between
     * @param viewManagerModel - model that tracks our views
     */
    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    /**
     * Tells cardLayout to display the panel/view
     */
    @Override
    public void viewChange() {
        cardLayout.show(views, viewManagerModel.getCurrentViewName());
    }
}
