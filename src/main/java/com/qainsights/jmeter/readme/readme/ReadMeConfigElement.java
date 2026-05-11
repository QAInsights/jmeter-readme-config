package com.qainsights.jmeter.readme.readme;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadMeConfigElement extends ConfigTestElement {

    private static final String MARKDOWN_CONTENT = "ReadmeConfigElement.content";

    public ReadMeConfigElement() {
        setName("README Config Element");
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    public void setMarkdownContent(String content) {
        setProperty(new StringProperty(MARKDOWN_CONTENT, content));
    }

    public String getMarkdownContent() {
        return getPropertyAsString(MARKDOWN_CONTENT, "");
    }
}
