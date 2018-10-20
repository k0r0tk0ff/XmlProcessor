package ru.k0r0tk0ff.xml.processor.infrastructure.io.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */
public class InputParametersChecker {

    private final static String[] validInputParamForFirstParam = new String[] {"-S","-G", "-C"};

    public static void ParametersCheck(String[] args){

        if(!isParameterCorrect(args[0])) {
            System.out.println("First parameter incorrect! See readme.txt");
            System.exit(1);
        }

        if (args.length == 2) {
            if (!args[0].equals("-C")) {
                if (!isFilenameCorrect(args[1])) {
                    System.out.println("Second parameter incorrect! Use digital, alphabetical symbols and point for file name.");
                    System.exit(1);
                }
            }
        }
    }

    private static boolean isParameterCorrect(String parameter){
        return Arrays.stream(validInputParamForFirstParam)
                .anyMatch(x -> x.equals(parameter));
    }

    private static boolean isFilenameCorrect(String fileName) {
        Pattern pattern = Pattern.compile("[^\\*\\|\\\\\\:\\\"<>\\?\\/]");
        Matcher matcher = pattern.matcher(fileName);
        //return !matcher.find();
        return matcher.find();
    }
}
