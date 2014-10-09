package com.oleksiykovtun.douview.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point class
 */
public class App implements EntryPoint {

    /**
     * Entry point.
     */
    public void onModuleLoad() {
        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(10);

        panel.add(new Label("The REST service for getting data from DOU.ua forums."));

        RootPanel.get().add(panel);
    }

}