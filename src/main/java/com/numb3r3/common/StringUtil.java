package com.numb3r3.common;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;
import java.util.Scanner;

public class StringUtil {

    public static String ZeroPad(int number, int width) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < width - Integer.toString(number).length(); i++)
            result.append("0");
        result.append(Integer.toString(number));

        return result.toString();
    }

    public static String copy(String x) {
        return x.substring(0);
    }

    public static List<Integer> parseInt(String text) {
        List<Integer> list = Lists.newArrayList();

        Scanner fi = new Scanner(text);
        // anything other than alphanumberic characters,
        // comma, dot or negative sign is skipped
        fi.useDelimiter("[^\\p{Alnum},\\.-]");
        while (fi.hasNext()) {
            if (fi.hasNextInt())
                list.add(fi.nextInt());
                // else if (fi.hasNextDouble())
                // System.out.println("Double: " + fi.nextDouble());
                // else if (fi.hasNext())
                // System.out.println("word: " + fi.next());
            else
                fi.next();
            //break;
        }
        return list;
    }

    public static String stripQuotes(String input) {
        if (input.startsWith("'") || input.startsWith("\"")) {
            return StringEscapeUtils.unescapeJava(input.substring(1, input.length() - 1));
        } else {
            return input;
        }
    }

    public static void main(String[] args) {
        String text = "hotel_13555_nlp.json 3.14";
        System.out.println(StringUtil.parseInt(text));
    }
}
