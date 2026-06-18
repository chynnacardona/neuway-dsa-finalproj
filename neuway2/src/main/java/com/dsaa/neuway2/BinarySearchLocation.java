package com.dsaa.neuway2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarySearchLocation {
    //static String[] loc = {"Gate", "IS Blg", "Library", "Main Blg", "PSB", "Secret Garden","SOM"};
    static List<String> binarySearch(List<String> loc, String prefix) {
    prefix = prefix.toUpperCase(); // normalize

    List<String> lowerLoc = new ArrayList<>();
    for (String s : loc) lowerLoc.add(s.toUpperCase());

    int l = 0, h = lowerLoc.size() - 1;
    int firstMatchIndex = -1;

    while (l <= h) {
        int m = l + (h - l) / 2;
        String cur = lowerLoc.get(m);

        if (cur.startsWith(prefix)) {
            firstMatchIndex = m;
            h = m - 1;
        } else if (cur.compareTo(prefix) < 0) {
            l = m + 1;
        } else {
            h = m - 1;
        }
    }

    if (firstMatchIndex == -1) return Collections.emptyList();

    // Collect all matches
    List<String> matches = new ArrayList<>();

    // left
    for (int i = firstMatchIndex; i >= 0 && lowerLoc.get(i).startsWith(prefix); i--) {
        matches.add(0, loc.get(i)); // original casing
    }
    // right
    for (int i = firstMatchIndex + 1; i < loc.size() && lowerLoc.get(i).startsWith(prefix); i++) {
        matches.add(loc.get(i));
    }

    return matches;
}

}

