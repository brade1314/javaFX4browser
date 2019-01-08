package com.aiwei.fx.browser.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * <p>Title: FxWebView </p>
 * <p>Description:  主视图 </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/10/30 16:04
 */
public class FxWebView extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new FxBrowser(stage), Color.web("#666970")));
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
