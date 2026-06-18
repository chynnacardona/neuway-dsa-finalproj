package com.dsaa.neuway2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Sort location by distance (from hashMap) based on start/curLocation inputted
public class InsertionSortDist {
    public static List<Map.Entry<List<String>, Integer>> sort(Map<List<String>, Integer> allDist)
    {
        List<Map.Entry<List<String>, Integer>> list = new ArrayList<>(allDist.entrySet());

        int n = allDist.size();
        for (int i = 1; i < n; ++i) {
            Map.Entry<List<String>, Integer> key = list.get(i); 
            int j = i - 1;

            while (j >= 0 && list.get(j).getValue() > key.getValue()) { //get value by index
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        print(list);
        return list;
    }

    static void print(List<Map.Entry<List<String>, Integer>> list) {
        for (Map.Entry<List<String>, Integer> listPrint : list) {
            List<String> key = listPrint.getKey();
            Integer value = listPrint.getValue();

            System.out.println(key + " : " + value);
        }
    }

}