package com.qainsights.jmeter.readme.readme;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReadMeSampler")
class ReadMeSamplerTest {

    private ReadMeSampler sampler;

    @BeforeEach
    void setUp() {
        sampler = new ReadMeSampler();
    }

    @Nested
    @DisplayName("constructor")
    class Constructor {

        @Test
        @DisplayName("sets default name to 'README Sampler'")
        void defaultName() {
            assertEquals("README Sampler", sampler.getName());
        }

        @Test
        @DisplayName("is disabled by default")
        void isDisabled() {
            assertFalse(sampler.isEnabled());
        }

        @Test
        @DisplayName("default markdown content is empty")
        void defaultContentIsEmpty() {
            assertEquals("", sampler.getMarkdownContent());
        }
    }

    @Nested
    @DisplayName("enabled state")
    class EnabledState {

        @Test
        @DisplayName("stays disabled when setEnabled(true) is called")
        void staysDisabledWhenEnabledSetTrue() {
            sampler.setEnabled(true);
            assertFalse(sampler.isEnabled());
        }

        @Test
        @DisplayName("stays disabled when setEnabled(false) is called")
        void staysDisabledWhenEnabledSetFalse() {
            sampler.setEnabled(false);
            assertFalse(sampler.isEnabled());
        }

        @Test
        @DisplayName("remains disabled across multiple toggles")
        void remainsDisabledAcrossMultipleToggles() {
            sampler.setEnabled(true);
            sampler.setEnabled(false);
            sampler.setEnabled(true);
            assertFalse(sampler.isEnabled());
        }
    }

    @Nested
    @DisplayName("markdown content")
    class MarkdownContent {

        @Test
        @DisplayName("roundtrips simple text")
        void roundtripsSimpleText() {
            sampler.setMarkdownContent("# Hello World");
            assertEquals("# Hello World", sampler.getMarkdownContent());
        }

        @Test
        @DisplayName("roundtrips multiline markdown")
        void roundtripsMultilineMarkdown() {
            String md = "# Title\n\n- item 1\n- item 2\n\n```java\nSystem.out.println();\n```";
            sampler.setMarkdownContent(md);
            assertEquals(md, sampler.getMarkdownContent());
        }

        @Test
        @DisplayName("roundtrips unicode and emoji")
        void roundtripsUnicodeAndEmoji() {
            String md = "## 你好 🚀 テスト σωστά";
            sampler.setMarkdownContent(md);
            assertEquals(md, sampler.getMarkdownContent());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("returns empty string for null or blank input")
        void returnsEmptyForNullOrBlank(String input) {
            sampler.setMarkdownContent(input);
            assertEquals("", sampler.getMarkdownContent());
        }

        @Test
        @DisplayName("overwrites previous content")
        void overwritesPreviousContent() {
            sampler.setMarkdownContent("first");
            sampler.setMarkdownContent("second");
            assertEquals("second", sampler.getMarkdownContent());
        }

        @Test
        @DisplayName("handles very long content")
        void handlesVeryLongContent() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10_000; i++) {
                sb.append("line ").append(i).append(": some markdown content here\n");
            }
            String longContent = sb.toString();
            sampler.setMarkdownContent(longContent);
            assertEquals(longContent, sampler.getMarkdownContent());
        }
    }

    @Nested
    @DisplayName("sample()")
    class Sample {

        @Test
        @DisplayName("returns successful result with null entry")
        void returnsSuccessfulResultWithNullEntry() {
            sampler.setMarkdownContent("# Test");
            SampleResult result = sampler.sample(null);

            assertTrue(result.isSuccessful());
            assertEquals("200", result.getResponseCode());
            assertEquals("README", result.getResponseMessage());
            assertEquals(SampleResult.TEXT, result.getDataType());
            assertEquals("# Test", result.getResponseDataAsString());
            assertTrue(result.getStartTime() > 0);
            assertTrue(result.getEndTime() >= result.getStartTime());
        }

        @Test
        @DisplayName("returns successful result with non-null entry")
        void returnsSuccessfulResultWithNonNullEntry() {
            sampler.setMarkdownContent("content");
            Entry entry = new Entry() {};
            SampleResult result = sampler.sample(entry);

            assertTrue(result.isSuccessful());
            assertEquals("content", result.getResponseDataAsString());
        }

        @Test
        @DisplayName("label equals default sampler name")
        void labelEqualsDefaultSamplerName() {
            SampleResult result = sampler.sample(null);
            assertEquals("README Sampler", result.getSampleLabel());
        }

        @Test
        @DisplayName("label reflects name change")
        void labelReflectsNameChange() {
            sampler.setName("Custom Name");
            SampleResult result = sampler.sample(null);
            assertEquals("Custom Name", result.getSampleLabel());
        }

        @Test
        @DisplayName("response code is 200")
        void responseCodeIs200() {
            assertEquals("200", sampler.sample(null).getResponseCode());
        }

        @Test
        @DisplayName("response message is README")
        void responseMessageIsReadme() {
            assertEquals("README", sampler.sample(null).getResponseMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"# markdown", ""})
        @NullAndEmptySource
        @DisplayName("always marks sample as successful regardless of content")
        void alwaysMarksSampleAsSuccessful(String content) {
            sampler.setMarkdownContent(content);
            assertTrue(sampler.sample(null).isSuccessful());
        }

        @Test
        @DisplayName("data type is TEXT")
        void dataTypeIsText() {
            assertEquals(SampleResult.TEXT, sampler.sample(null).getDataType());
        }

        @Test
        @DisplayName("response data is empty when content is null")
        void responseDataEmptyWhenContentNull() {
            sampler.setMarkdownContent(null);
            assertEquals("", sampler.sample(null).getResponseDataAsString());
        }

        @Test
        @DisplayName("response data is empty when content is blank")
        void responseDataEmptyWhenContentBlank() {
            sampler.setMarkdownContent("");
            assertEquals("", sampler.sample(null).getResponseDataAsString());
        }

        @Test
        @DisplayName("response data preserves UTF-8 characters")
        void responseDataPreservesUtf8Characters() {
            sampler.setMarkdownContent("café résumé");
            SampleResult result = sampler.sample(null);
            assertEquals("café résumé", result.getResponseDataAsString());
            assertEquals("UTF-8", result.getDataEncodingNoDefault());
        }

        @Test
        @DisplayName("timestamps have start before end")
        void timestampsHaveStartBeforeEnd() {
            SampleResult result = sampler.sample(null);
            assertTrue(result.getStartTime() > 0);
            assertTrue(result.getEndTime() >= result.getStartTime());
        }

        @Test
        @DisplayName("consecutive calls return independent results")
        void consecutiveCallsReturnIndependentResults() {
            sampler.setMarkdownContent("first");
            SampleResult r1 = sampler.sample(null);

            sampler.setMarkdownContent("second");
            SampleResult r2 = sampler.sample(null);

            assertEquals("first", r1.getResponseDataAsString());
            assertEquals("second", r2.getResponseDataAsString());
            assertNotSame(r1, r2);
            assertTrue(r2.getStartTime() >= r1.getStartTime());
        }
    }
}
