package com.aiwei.fx.browser.client;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebEngine;

/**
 * <p>Title: FxContextMenu </p>
 * <p>Description: 右键菜单 </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/10/30 16:04
 */
public class FxContextMenu extends ContextMenu {

    private final MenuItem refreshMenuItem;
    /**
     * 私有构造函数
     */
    private FxContextMenu() {
        this.refreshMenuItem = new MenuItem("重新加载");
//        this.saveToMenuItem = new MenuItem("另存为");
        this.getItems().add(this.refreshMenuItem);
//        this.getItems().addAll(refreshMenuItem,saveToMenuItem);
    }


    /**
     * 绑定事件
     *
     * @param webEngine
     * @return
     */
    public FxContextMenu addAction(WebEngine webEngine) {
        this.refreshMenuItem.setOnAction(a -> webEngine.reload());
        return this;
    }

    /**
     * 获取实例
     *
     * @return FxContextMenu
     */
    public static FxContextMenu getInstance() {
        return BuildMenu.INSTANCE;
    }

    private static class BuildMenu {
        private static FxContextMenu INSTANCE = new FxContextMenu();
    }

}
