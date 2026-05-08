package com.qainsights.jmeter.readme.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A simplified DocumentListener that allows using a lambda/Runnable for document changes.
 * This avoids the boilerplate of implementing all three DocumentListener methods when
 * you only care that something changed.
 */
public class SimpleDocumentListener implements DocumentListener {

    private final Runnable callback;

    public SimpleDocumentListener(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        callback.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        callback.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        callback.run();
    }
}
