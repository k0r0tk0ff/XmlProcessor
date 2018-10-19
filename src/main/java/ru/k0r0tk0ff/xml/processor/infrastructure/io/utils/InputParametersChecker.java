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

    private final static String[] validInputParamForFirstParam = new String[] {"-L","-U","-G", "-C"};

    public static void ParametersCheck(String firstParameter, String secondParameter){

        if(!isParameterCorrect(firstParameter)) {
            System.out.println("First parameter incorrect! See readme.txt");
            System.exit(1);
        }

        if(!firstParameter.equals("-C")){
            if(!isFilenameCorrect(secondParameter)){
                System.out.println("Second parameter incorrect! Use digital, alphabetical symbols and point for file name.");
                System.exit(1);
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
