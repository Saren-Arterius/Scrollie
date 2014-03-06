package net.wtako.Scrollie.Utils;

import java.io.File;

public class FileTool {

    public static File getChildFile(File parent, String child, boolean ignoreCase) {
        try {
            if (ignoreCase) {
                for (File subFile: parent.listFiles()) {
                    if (subFile.getName().equalsIgnoreCase(child)) {
                        return subFile;
                    }
                }
            } else {
                for (File subFile: parent.listFiles()) {
                    if (subFile.getName().equals(child)) {
                        return subFile;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
