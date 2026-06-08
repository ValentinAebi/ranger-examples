package org.example.ic7;


public class IC7 {
    
    public static String removeSubstring(String original, String removed) {
        int i = original.indexOf(removed);
        if (i != -1) {
            return original.substring(0, i) + original.substring(i + removed.length());
        }
        return original;
    }

}
