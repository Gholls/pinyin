package com.gholl.pinyin;

/**
 * Created by gholl on 2016/6/12.
 * Email:slgogo@foxmail.com
 */
public class Example {
    private String name;
    private String attribute;
    private String exampleText;
    private boolean showPy = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getExampleText() {
        return exampleText;
    }

    public void setExampleText(String exampleText) {
        this.exampleText = exampleText;
    }

    public boolean isShowPy() {
        return showPy;
    }

    public void setShowPy(boolean showPy) {
        this.showPy = showPy;
    }
}
