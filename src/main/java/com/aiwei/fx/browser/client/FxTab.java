package com.aiwei.fx.browser.client;

import javafx.scene.control.Tab;

/**
 * <p>Title: FxTab </p>
 * <p>Description:  </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/11/2 16:01
 */
public class FxTab extends Tab {


    FxTab(String text) {
        super(text);
    }

    FxTab closable(boolean isClosable){
        this.setClosable(isClosable);
        return this;
    }
    public FxTab text(String text){
        this.setText(text);
        return this;
    }


}
