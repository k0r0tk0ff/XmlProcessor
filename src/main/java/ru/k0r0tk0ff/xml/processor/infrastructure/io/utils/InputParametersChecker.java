package ru.k0r0tk0ff.xml.processor.utils.input;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by korotkov_a_a on 19.10.2018.
 */

public class InputParametersChecker {

    private final static String[] validInputParamForFirstParam = new String[] {"-s","-u", "-c"};

    public static void parametersCheck(String[] args) throws ParametersCheckException {

        if(!isParameterCorrect(args[0])) {
            throw new ParametersCheckException("First parameter incorrect! See readme.txt");
        }

        if (args.length == 2) {
            if (!args[0].equals("-c")) {
                if (args[0].equals("-u")) {
                    if (!isFileNameCorrect(args[1])) {
                        throw new ParametersCheckException("Incorrect file name for upload file.");
                    }
                }
                if (args[0].equals("-s")) {
                    if (!Files.isRegularFile(Paths.get(args[1]))) {
                        throw new ParametersCheckException("Incorrect file name or file not exist!");
                    }
                }
            }
        }
    }

    private static boolean isParameterCorrect(String parameter){
        return Arrays.stream(validInputParamForFirstParam)
                .anyMatch(x -> x.equals(parameter));
    }

    private static boolean isFileNameCorrect(String fileName) {
        Pattern pattern = Pattern.compile("[^\\*\\|\\\\\\:\\\"<>\\?\\/]");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.find();
    }
}
