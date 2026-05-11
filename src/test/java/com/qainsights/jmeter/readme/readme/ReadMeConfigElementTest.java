package com.qainsights.jmeter.readme.readme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReadMeConfigElement")
class ReadMeConfigElementTest {

    private ReadMeConfigElement configElement;

    @BeforeEach
    void setUp() {
        configElement = new ReadMeConfigElement();
    }

    @Nested
    @DisplayName("constructor")
    class Constructor {

        @Test
        @DisplayName("sets default name to 'README Config Element'")
        void defaultName() {
            assertEquals("README Config Element", configElement.getName());
        }

        @Test
        @DisplayName("is enabled by default")
        void isEnabled() {
            assertTrue(configElement.isEnabled());
        }

        @Test
        @DisplayName("default markdown content is empty")
        void defaultContentIsEmpty() {
            assertEquals("", configElement.getMarkdownContent());
        }
    }

    @Nested
    @DisplayName("enabled state")
    class EnabledState {

        @Test
        @DisplayName("can be disabled via setEnabled(false)")
        void canBeDisabled() {
            configElement.setEnabled(false);
            assertFalse(configElement.isEnabled());
        }

        @Test
        @DisplayName("toggles correctly across multiple calls")
        void togglesCorrectly() {
            configElement.setEnabled(false);
            assertFalse(configElement.isEnabled());
            configElement.setEnabled(true);
            assertTrue(configElement.isEnabled());
            configElement.setEnabled(false);
            assertFalse(configElement.isEnabled());
        }
    }

    @Nested
    @DisplayName("markdown content")
    class MarkdownContent {

        @Test
        @DisplayName("roundtrips simple text")
        void roundtripsSimpleText() {
            configElement.setMarkdownContent("# Hello World");
            assertEquals("# Hello World", configElement.getMarkdownContent());
        }

        @Test
        @DisplayName("roundtrips multiline markdown")
        void roundtripsMultilineMarkdown() {
            String md = "# Title\n\n- item 1\n- item 2\n\n```java\nSystem.out.println();\n```";
            configElement.setMarkdownContent(md);
            assertEquals(md, configElement.getMarkdownContent());
        }

        @Test
        @DisplayName("roundtrips unicode and emoji")
        void roundtripsUnicodeAndEmoji() {
            String md = "## 你好 🚀 テスト σωστά";
            configElement.setMarkdownContent(md);
            assertEquals(md, configElement.getMarkdownContent());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("returns empty string for null or blank input")
        void returnsEmptyForNullOrBlank(String input) {
            configElement.setMarkdownContent(input);
            assertEquals("", configElement.getMarkdownContent());
        }

        @Test
        @DisplayName("overwrites previous content")
        void overwritesPreviousContent() {
            configElement.setMarkdownContent("first");
            configElement.setMarkdownContent("second");
            assertEquals("second", configElement.getMarkdownContent());
        }

        @Test
        @DisplayName("handles very long content")
        void handlesVeryLongContent() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10_000; i++) {
                sb.append("line ").append(i).append(": some markdown content here\n");
            }
            String longContent = sb.toString();
            configElement.setMarkdownContent(longContent);
            assertEquals(longContent, configElement.getMarkdownContent());
        }
    }
}
