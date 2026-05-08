package com.qainsights.jmeter.readme.readme;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadMeSampler extends AbstractSampler {

    private static final Logger logger = LoggerFactory.getLogger(ReadMeSampler.class);
    private static final String MARKDOWN_CONTENT = "ReadmeSampler.content";

    public ReadMeSampler() {
        setName("README Sampler");
        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(false);
    }

    public void setMarkdownContent(String content) {
        setProperty(new StringProperty(MARKDOWN_CONTENT, content));
    }

    public String getMarkdownContent() {
        return getPropertyAsString(MARKDOWN_CONTENT, "");
    }
    /**
     * Obtains statistics about the given Entry, and packages the information
     * into a SampleResult.
     *
     * @param e the Entry (TODO seems to be unused)
     * @return information about the sample
     */
    @Override
    public SampleResult sample(Entry e) {
        logger.debug("Sample method called with entry: {}", e);

        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();

        // No-op: README sampler never makes a real request
        result.setSuccessful(true);
        result.setResponseCode("200");
        result.setResponseMessage("README");
        result.setResponseData(getMarkdownContent(), "UTF-8");
        result.setDataType(SampleResult.TEXT);

        result.sampleEnd();
        return result;
    }
}
