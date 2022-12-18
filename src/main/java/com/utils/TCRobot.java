package com.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class TCRobot extends Robot {
    public TCRobot() throws AWTException {
    }

    public void pasteClipboard() {
        this.keyPress(17);
        this.keyPress(86);
        this.delay(50);
        this.keyRelease(86);
        this.keyRelease(17);
    }

    public void type(String text) {
        this.writeToClipboard(text);
        this.pasteClipboard();
    }

    private void writeToClipboard(String s) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, (ClipboardOwner)null);
    }
}
