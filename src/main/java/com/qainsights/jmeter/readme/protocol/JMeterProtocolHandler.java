package com.qainsights.jmeter.readme.protocol;


import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class JMeterProtocolHandler {
    public static final String PROTOCOL = "jmeter://";

    /**
     * Parse href and navigate to the matching JMeter tree node.
     * Must be called on the Event Dispatch Thread.
     */
    public static void handle(String href) {
        if (href == null || !href.startsWith(PROTOCOL)) return;

        String raw  = href.substring(PROTOCOL.length());
        String name = URLDecoder.decode(raw, StandardCharsets.UTF_8);

        SwingUtilities.invokeLater(() -> navigate(name));
    }

    /**
     * Walk the JMeterTreeModel and select the first node whose
     * testname matches the given name (case-insensitive).
     */
    public static boolean navigate(String targetName) {
        GuiPackage gui = GuiPackage.getInstance();
        if (gui == null) return false;

        JMeterTreeModel model = gui.getTreeModel();
        JMeterTreeNode root   = (JMeterTreeNode) model.getRoot();

        JMeterTreeNode match  = findNode(root, targetName.trim());
        if (match == null) return false;

        TreePath path = new TreePath(match.getPath());
        JTree tree    = gui.getMainFrame().getTree();

        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
        gui.updateCurrentNode();
        gui.getMainFrame().repaint();

        return true;
    }

    // -------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------

    private static JMeterTreeNode findNode(JMeterTreeNode node, String name) {
        if (node.getName().equalsIgnoreCase(name)) return node;

        Enumeration<?> children = node.children();
        while (children.hasMoreElements()) {
            JMeterTreeNode child  = (JMeterTreeNode) children.nextElement();
            JMeterTreeNode result = findNode(child, name);
            if (result != null) return result;
        }
        return null;
    }
}
