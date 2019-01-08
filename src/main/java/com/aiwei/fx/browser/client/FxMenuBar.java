package com.aiwei.fx.browser.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Objects;

/**
 * <p>Title: FxMenuBar </p>
 * <p>Description:  菜单栏 </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/10/31 19:49
 */
class FxMenuBar extends MenuBar{
    private final Stage stage;
    private final WebEngine webEngine;

    FxMenuBar(Stage stage, WebEngine webEngine) {
        super();
        this.stage = stage;
        this.webEngine = webEngine;
        this.init();
    }

    private void init() {
        Menu menuFile = new Menu("文件");
        MenuItem saveItem = new MenuItem("保存");
        saveItem.setMnemonicParsing(true);
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveItem.setOnAction(this::saveAs);
        MenuItem exitItem = new MenuItem("退出");
        exitItem.setAccelerator(KeyCombination.keyCombination("Esc"));
        exitItem.setOnAction(e -> Platform.exit());
        menuFile.getItems().addAll(saveItem, exitItem);

        Menu menuEdit = new Menu("编辑");
        MenuItem settingItem = new MenuItem("设置");
        settingItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        settingItem.setOnAction(e -> {});
        menuEdit.getItems().add(settingItem);

        Menu menuHelp = new Menu("帮助");
        MenuItem aboutItem = new MenuItem("关于");
        aboutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        aboutItem.setOnAction(e -> {});
        menuHelp.getItems().add(aboutItem);
        this.getMenus().addAll(menuFile, menuEdit, menuHelp);
    }

    /**
     * 文件另存为
     *
     * @param actionEvent
     */
    private void saveAs(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("另存为");
        fileChooser.setInitialFileName(webEngine.getTitle());
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("网页", "全部");
        FileChooser.ExtensionFilter htmlFilter = new FileChooser.ExtensionFilter("网页", "仅html");
        fileChooser.getExtensionFilters().addAll(htmlFilter, allFilter);
        File file = fileChooser.showSaveDialog(this.stage);
        fileChooser.setInitialDirectory(file);
        if (Objects.isNull(file)) {
            return;
        }
        if (file.exists()) {
            file.delete();
        }
        /*
         *  也可以使用此方法调用js方式获取页面文本
         *   String html = webEngine.executeScript("document.documentElement.outerHTML").toString();
         *   new FileWriter.write(html);
         */
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(webEngine.getDocument()), new StreamResult(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
