package br.ufrn.imd.otherssl.core;


import java.io.File;

/* Utils.java is used by FileChooserDemo2.java. */
public class Utils {

    public final static String arff = "arff";
    public final static String csv = "csv";

    // Get the extension of a file.
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}