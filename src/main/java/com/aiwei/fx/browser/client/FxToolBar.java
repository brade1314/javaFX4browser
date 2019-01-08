package com.aiwei.fx.browser.client;

import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <p>Title: FxToolBar </p>
 * <p>Description: 工具栏 </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/10/31 20:03
 */
class FxToolBar extends HBox{
    private static final String DEFAULT_URL = "https://www.baidu.com";
    private TextField searchBox;
    private WebEngine webEngine;
    private FxBrowserOb fxBrowser;
    private Tab tab;
    private String url;
    private final String SEARCH;

    FxToolBar(WebEngine webEngine, Tab tab, FxBrowserOb fxBrowser, String url) {
        this.url= Objects.isNull(url) ? DEFAULT_URL : url;
        this.webEngine = webEngine;
        this.tab = tab;
        this.fxBrowser = fxBrowser;
        this.init();
        this.initEvent();
        SEARCH = "https://www.baidu.com/s?wd=";
    }

    private void init() {
        Font font = Font.font("Timer New Roman", 14);
        this.searchBox = new TextField(this.url);
        searchBox.setFont(font);
        searchBox.setOnAction(e -> this.search(searchBox.getText().trim()));
        this.getChildren().addAll(this.searchBox);
        HBox.setHgrow(this.searchBox, Priority.ALWAYS);
    }

    private void initEvent(){
        this.search(this.url);
        // 修改输入栏的地址，也就是访问那个网站，这个地址栏显示那个网站的地址
        // locationProperty()是获得当前页面的url封装好的ReadOnlyStringProperty对象
        this.webEngine.locationProperty().addListener((observable, oldValue, newValue) -> this.searchBox.setText(newValue));
        // 设置标题栏为当前访问页面的标题。
        this.webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // 设置tab页标题
                this.tab.setText(this.webEngine.getTitle());
            }
        });

        this.webEngine.documentProperty().addListener((observable, oldValue, newValue) -> {
            Worker.State state = this.webEngine.getLoadWorker().getState();
            if (state != Worker.State.SUCCEEDED && Objects.isNull(newValue)) {
                return;
            }
            NodeList nodeList = newValue.getElementsByTagName("a");
            for (int i = 0; i < nodeList.getLength(); i++) {
                EventTarget eventTarget = (EventTarget) nodeList.item(i);
                eventTarget.addEventListener("click", (Event evt) -> {
                    HTMLAnchorElement anchorElement = (HTMLAnchorElement) evt.getCurrentTarget();
                    String href = anchorElement.getHref();
                    FxTab tab = new FxTab("新标签页");
                    this.fxBrowser.createTab(tab,href);
                    this.tab.getTabPane().getTabs().add(tab);
                    this.tab.getTabPane().getSelectionModel().select(tab);
                    evt.preventDefault();
                }, false);
            }
        });

    }

    /**
     * 搜索
     * @param text 文本信息
     */
    private void search(String text) {
        String urlRegExp = "((http|ftp|https)://)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-.,@?^=%&amp;:/~+#]*[\\w\\-@?^=%&amp;/~+#])?";
        Pattern p = Pattern.compile(urlRegExp);
        if (p.matcher(text).matches()) {
            String protocol = "^(http|ftp|https)://\\S*";
            if (!Pattern.compile(protocol).matcher(text).matches()) {
                text = "http://" + text;
            }
            webEngine.load(text);
            return;
        }
        if (!text.trim().isEmpty()) {
            webEngine.load(SEARCH + text);
        }
    }

}
