package com.aiwei.fx.browser.client;

/**
 * <p>Title: FxBrowserOb </p>
 * <p>Description:  </p>
 * <p>Company: aiWei </p>
 *
 * @author zzhengmin
 * @date 2018/11/3 15:22
 */
@FunctionalInterface
public interface FxBrowserOb {

    /**
     * 新建tab
     * @param tab
     * @param url
     */
    void createTab(FxTab tab, String url);
}
