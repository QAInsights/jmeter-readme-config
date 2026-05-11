package com.qainsights.jmeter.readme.readme;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadMeConfigElement extends ConfigTestElement {

    private static final Logger logger = LoggerFactory.getLogger(ReadMeConfigElement.class);
    private static final String MARKDOWN_CONTENT = "ReadmeConfigElement.content";

    public ReadMeConfigElement() {
        setName("README Config Element");
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(false);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setMarkdownContent(String content) {
        setProperty(new StringProperty(MARKDOWN_CONTENT, content));
    }

    public String getMarkdownContent() {
        return getPropertyAsString(MARKDOWN_CONTENT, "");
    }
}
