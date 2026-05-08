package com.qainsights.jmeter.readme.readme;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReadMeMarkdownRenderer {

    private static final Logger logger = LoggerFactory.getLogger(ReadMeMarkdownRenderer.class);

    public ReadMeMarkdownRenderer() {
        logger.debug("ReadMeMarkdownRenderer instantiated");
    }

    private static final String GITHUB_CSS =
            """
                    <style>
                      body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif; font-size: 13px; line-height: 1.4; color: #1f2328; background: #ffffff; padding: 6px 10px; }
                      h1, h2, h3, h4, h5, h6 { margin-top: 10px; margin-bottom: 4px; font-weight: 600; line-height: 1.25; border-bottom: 1px solid #d0d7de; padding-bottom: 0.2em; }
                      h1 { font-size: 1.4em; }
                      h2 { font-size: 1.2em; }
                      h3 { font-size: 1em; }
                      p { margin-top: 0; margin-bottom: 6px; }
                      a { color: #0969da; text-decoration: none; }
                      a:hover { text-decoration: underline; }
                      code { font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace; padding: .2em .4em; border-radius: 6px; font-size: 85%; }
                      pre { padding: 16px; border-radius: 6px; border: 1px solid #d0d7de; overflow-x: auto; font-size: 85%; line-height: 1.45; margin-top: 0; margin-bottom: 16px; }
                      pre code { background: none; padding: 0; border-radius: 0; font-size: 100%; }
                      blockquote { border-left: 0.25em solid #d0d7de; padding: 0 0.8em; color: #656d76; margin: 0 0 6px 0; }
                      table { border-collapse: collapse; margin-bottom: 8px; width: 100%; }
                      table th, table td { border: 1px solid #d0d7de; padding: 4px 10px; }
                      table th { background: #f6f8fa; font-weight: 600; }
                      table tr:nth-child(even) { background: #f6f8fa; }
                      ul, ol { padding-left: 1.5em; margin-top: 0; margin-bottom: 6px; }
                      li { margin-bottom: 0; }
                      img { max-width: 100%; }
                      hr { border: 0; height: 2px; background: #d0d7de; margin: 10px 0; }
                    </style>""";

    public String render(String markdown) {
        logger.debug("Rendering markdown, input length: {}", markdown != null ? markdown.length() : 0);
        List<Extension> extensions = List.of(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TaskListItemsExtension.create()
        );
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(markdown);
        logger.debug("Markdown parsed successfully");
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String bodyHtml = renderer.render(document);
        int tableIdx = bodyHtml.indexOf("<table");
        if (tableIdx >= 0) {
            int endIdx = bodyHtml.indexOf("</table>", tableIdx);
            logger.debug("Table HTML found at index {}: {}", tableIdx,
                    endIdx >= 0 ? bodyHtml.substring(tableIdx, endIdx + 8) : bodyHtml.substring(tableIdx));
        } else {
            logger.debug("NO <table> tag found in generated HTML. Full body HTML:\n{}", bodyHtml);
        }

        bodyHtml = bodyHtml
                .replace("<table>", "<table border=\"1\" cellpadding=\"6\" cellspacing=\"0\">")
                .replace("<th ", "<th bgcolor=\"#f6f8fa\" style=\"border:1px solid #d0d7de\" ")
                .replace("<th>", "<th bgcolor=\"#f6f8fa\" style=\"border:1px solid #d0d7de\">")
                .replace("<td ", "<td style=\"border:1px solid #d0d7de\" ")
                .replace("<td>", "<td style=\"border:1px solid #d0d7de\">")
                .replace("<thead>", "")
                .replace("</thead>", "")
                .replace("<tbody>", "")
                .replace("</tbody>", "");
        int transformedTableIdx = bodyHtml.indexOf("<table");
        if (transformedTableIdx >= 0) {
            int transformedEndIdx = bodyHtml.indexOf("</table>", transformedTableIdx);
            logger.debug("Transformed table HTML: {}", transformedEndIdx >= 0
                    ? bodyHtml.substring(transformedTableIdx, transformedEndIdx + 8)
                    : bodyHtml.substring(transformedTableIdx));
        }

        String html = "<html><head>" + GITHUB_CSS + "</head><body>" + bodyHtml + "</body></html>";
        logger.debug("Markdown rendered to HTML, output length: {}", html.length());
        return html;
    }


}
