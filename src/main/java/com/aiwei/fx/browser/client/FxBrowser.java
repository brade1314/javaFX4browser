package com.aiwei.fx.browser.client;

import javafx.collections.ListChangeListener.Change;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Title: FxBrowser </p>
 * <p>Description:  浏览器处理 </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/10/30 16:04
 */
public class FxBrowser extends Region {
    private Stage stage;
    private TabPane tabPane;
    private VBox viewContent;
    private final AtomicLong ids = new AtomicLong(1000);

    FxBrowser(Stage stage) {
        this.stage = stage;
        this.tabPane = new TabPane();
        this.initializeTabPane();
    }


    /**
     * 右键事件
     */
    private void createContextMenu() {
//        this.browser.setContextMenuEnabled(false);
//        this.browser.onContextMenuRequestedProperty().setValue(event -> {
//            FxContextMenu.getInstance().addAction(this.webEngine).show(this.stage, event.getScreenX(), event.getScreenY());
//        });
    }

    /**
     * 构建视图
     *
     * @param fxTabPane
     * @param webEngine
     */
    public void createView(TabPane fxTabPane, WebEngine webEngine) {
        FxMenuBar fxMenuBar = new FxMenuBar(this.stage, webEngine);
        this.viewContent = new VBox(fxMenuBar, fxTabPane);
        this.getChildren().add(this.viewContent);
    }

    /**
     * 初始化tab页
     *
     */
    private void initializeTabPane() {
        FxTab addNewTab = new FxTab("+").closable(false);
        FxTab defaultTab = new FxTab("新标签页").closable(false);
        this.createNewTab(defaultTab, null);
        tabPane.getTabs().addAll(addNewTab, defaultTab);
        tabPane.getSelectionModel().select(defaultTab);
        // 此listener一定要放在addAll和select后面，否则会事先触发，导致初始化时出现2个tab
        addNewTab.selectedProperty().addListener(e -> {
            FxTab fxTab = new FxTab("新标签页");
            this.createNewTab(fxTab, null);
            tabPane.getTabs().add(fxTab);
            tabPane.getSelectionModel().select(fxTab);
        });

    }

    private void createNewTab(FxTab tab, String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        FxToolBar toolBar = new FxToolBar(webEngine, tab, this::createNewTab, url);
        VBox tabContent = new VBox(toolBar, browser);

        tab.setId(String.valueOf(this.ids.incrementAndGet()));
        tab.setContent(tabContent);
        this.createView(tabPane, webEngine);
        // tab选中时再调用构建方法，重新构建，防止保存时依旧为最后一次打开tab页内容
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> this.createView(tabPane, webEngine));
        VBox.setVgrow(browser, Priority.ALWAYS);
        VBox.setVgrow(tabContent, Priority.ALWAYS);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }


    private void createToolBar(WebEngine webEngine) {
        // 历史记录
        final WebHistory history = webEngine.getHistory();

        history.getEntries().addListener((Change<? extends Entry> c) -> {
            System.out.println(" 历史记录：： " + c.toString());
            System.out.println(" 历史记录：： " + c.getList());
            history.getEntries().forEach(h -> System.out.println("---> : " + h.getUrl()));
            System.out.println("next: " + c.next());
            System.out.println("getTo: " + c.getTo());
            System.out.println("getFrom: " + c.getFrom());
            System.out.println("getCurrentIndex: " + history.getCurrentIndex());
            if (history.getCurrentIndex() < c.getList().size() - 1) {
                history.go(1);
            }
        });
    }


    @Override
    protected void layoutChildren() {
        layoutInArea(viewContent, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 1024;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 768;
    }

}
