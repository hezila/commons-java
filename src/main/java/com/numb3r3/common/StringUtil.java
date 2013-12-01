package com.numb3r3.common;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    static Pattern PUNCT = null;
    static String ALL_QUOTES = "\"“”’‘`'"; // 7 ?
    static String PUNCT_CHARS = ALL_QUOTES + "~\",;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^"; // add quotes?
    static String ALL_PUNCT = "[\\p{Punct}" + ALL_QUOTES + "]+";
    static String PUNCT_PATT = "^(?:[\\p{Punct}"+ALL_QUOTES+"]*)((?:.)|(?:[\\w ].*?[\\w ]))(?:[\\p{Punct}"+ALL_QUOTES+"]*)$";


    public static Set abbreviations = new HashSet(64);

    static {
        abbreviations.add("Adm.");
        abbreviations.add("Capt.");
        abbreviations.add("Cmdr.");
        abbreviations.add("Col.");
        abbreviations.add("Dr.");
        abbreviations.add("Gen.");
        abbreviations.add("Gov.");
        abbreviations.add("Lt.");
        abbreviations.add("Maj.");
        abbreviations.add("Messrs.");
        abbreviations.add("Mr.");
        abbreviations.add("Mrs.");
        abbreviations.add("Ms.");
        abbreviations.add("Prof.");
        abbreviations.add("Rep.");
        abbreviations.add("Reps.");
        abbreviations.add("Rev.");
        abbreviations.add("Sen.");
        abbreviations.add("Sens.");
        abbreviations.add("Sgt.");
        abbreviations.add("Sr.");
        abbreviations.add("St.");

        // abbreviated first names
        abbreviations.add("Benj.");
        abbreviations.add("Chas.");
        // abbreviations.add("Alex."); // dch

        // abbreviated months
        abbreviations.add("Jan.");
        abbreviations.add("Feb.");
        abbreviations.add("Mar.");
        abbreviations.add("Apr.");
        abbreviations.add("Mar.");
        abbreviations.add("Jun.");
        abbreviations.add("Jul.");
        abbreviations.add("Aug.");
        abbreviations.add("Sept.");
        abbreviations.add("Oct.");
        abbreviations.add("Nov.");
        abbreviations.add("Dec.");

        // other abbreviations
        abbreviations.add("a.k.a.");
        abbreviations.add("c.f.");
        abbreviations.add("i.e.");
        abbreviations.add("e.g.");
        abbreviations.add("vs.");
        abbreviations.add("v.");

        Set tmp = new HashSet(64);
        Iterator it = abbreviations.iterator();
        while (it.hasNext())
            tmp.add(((String) it.next()).toLowerCase());
        abbreviations.addAll(tmp);
    }

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

    /**
     * Returns true iff ALL characters in the string are punctuation
     *
     * @param s String to check
     * @return boolean
     */
    public static boolean isPunctuation(String s) {
        if (PUNCT == null)
            PUNCT = Pattern.compile(ALL_PUNCT);
        return PUNCT.matcher(s).matches();
    }

    /**
     * Joins array of word, similar to words.join(delim), but attempts to preserve punctuation position
     * unless the 'adjustPunctuationSpacing' flag is set to false
     *
     * @param arr the array to join
     * @return the joined array as a String
     */
    public static String untokenize
    (String[] arr, char delim, boolean adjustPunctuationSpacing) {
        //System.out.println("RiTa.untokenize("+RiTa.asList(arr)+") "+adjustPunctuationSpacing);

        if (arr == null || arr.length < 1) return "";

        if (adjustPunctuationSpacing) {

            String newStr = arr[0] != null ? arr[0] : "";
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] != null) {
                    if (!arr[i].matches("[,\\.\\;\\:\\?\\!" + ALL_QUOTES + "]+"))
                        newStr += delim;
                    newStr += arr[i];
                }
            }
            return newStr.trim();
        }

        return Joiner.on(delim).join(arr);
    }

    /**
     * Joins array of word, similar to words.join(delim), but attempts to preserve punctuation position
     * unless the 'adjustPunctuationSpacing' flag is set to false
     *
     * @param tokens the string tokens to join
     * @return the joined array as a String
     */
    public static String untokenize
    (List<String> tokens, char delim, boolean adjustPunctuationSpacing) {
        if (tokens == null || tokens.size() < 1) return "";

        if (adjustPunctuationSpacing) {

            String newStr = tokens.get(0) != null ? tokens.get(0) : "";
            for (int i = 1; i < tokens.size(); i++) {
                if (tokens.get(i) != null) {
                    if (!tokens.get(i).matches("[,\\.\\;\\:\\?\\!" + ALL_QUOTES + "]+"))
                        newStr += delim;
                    newStr += tokens.get(i);
                }
            }
            return newStr.trim();
        }

        return Joiner.on(delim).join(tokens);
    }

    /**
     * Trims punctuation from each side of the <code>token</code>
     * (does not trim whitespace or internal punctuation).
     */
    public static String trimPunctuation(String token)
    {
        if (token == null || token.length()<1) return token;

        // Note: needs to handle byte-order marks...
        if (punctPattern == null)
            punctPattern = Pattern.compile(PUNCT_PATT, Pattern.CASE_INSENSITIVE);

        Matcher m = punctPattern.matcher(token);
        boolean match = m.find();
        if (!match || m.groupCount() < 1) {
            System.err.println("[WARN] RiTa.trimPunctuation(): invalid regex state for String "
                    + "\n       '" + token + "', perhaps an unexpected byte-order mark?");
            return token;
        }

        return m.group(1);
    }  static Pattern punctPattern = null;

    public static String stripPunctuation(String phrase) {
        return stripPunctuation(phrase, null);
    }

    /**
     * Strips any punctuation characters from the String
     */
    public static String stripPunctuation(String phrase, char[] charsToIgnore)
    {
        if (phrase == null || phrase.length()<1)
            return "";

        StringBuilder sb = new StringBuilder();
        OUTER: for (int i = 0; i < phrase.length(); i++) {
            char c = phrase.charAt(i);
            //System.out.println("char: "+c+" "+Character.valueOf(c));
            if (charsToIgnore != null)  {
                for (int j = 0; j < charsToIgnore.length; j++) {
                    if (c == charsToIgnore[j]) {
                        sb.append(c);
                        continue OUTER;
                    }
                }
            }
            if (PUNCT_CHARS.indexOf(c) < 0)
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Removes white-space and line breaks from start and end of String
     * @param s String to be chomped
     * @return string without starting or ending white-space or line-breaks
     */
    public static String chomp(String s)
    {
        if (CHOMP == null)
            CHOMP = Pattern.compile("\\s+$|^\\s+");
        Matcher m = CHOMP.matcher(s);
        return m.replaceAll("");
    } static Pattern CHOMP;

    public static void main(String[] args) {
        String text = "hotel_13555_nlp.json 3.14";
        System.out.println(StringUtil.parseInt(text));

        List<String> tokens = Lists.newArrayList();
        tokens.add("word");
        tokens.add("hello");
        tokens.add(".");
        System.out.println(StringUtil.untokenize(tokens, ' ', true));
    }
}
