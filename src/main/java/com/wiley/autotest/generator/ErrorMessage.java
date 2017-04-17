package com.wiley.autotest.generator;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by vfedorenko on 05/04/2017.
 */

public interface ErrorMessage {

    String generate();

    default String generate(int methodNameIndex) {
        String nameOfMethodThatMeCall = getNameOfMethodThatMeCall(methodNameIndex);
        if (nameOfMethodThatMeCall.isEmpty()) {
            return "***Code issue during generating error message***";
        } else {
            return generateMessage(nameOfMethodThatMeCall);
        }
    }

    default String generateMessage(String nameOfMethodThatCalledMe) {
        String[] splitName = nameOfMethodThatCalledMe.split("(?<=[a-z])(?=[A-Z])");
        StringBuilder errorMessage = new StringBuilder();
        //make first letter of first word upper cased
        errorMessage.append(Character.toUpperCase(splitName[0].charAt(0))).append(splitName[0].substring(1));
        for (int i = 1; i < splitName.length; i++) {
            //make first letter of each of next words lower cased
            errorMessage.append(" ").append(Character.toLowerCase(splitName[i].charAt(0))).append(splitName[i].substring(1));
        }
        return errorMessage.toString() + " failed";
    }

    default String getNameOfMethodThatMeCall(int methodNameIndex) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 4) {
            return "";
        }
        Optional<StackTraceElement> first = Arrays.stream(stackTrace)
                .filter(stackTraceElement -> stackTraceElement.getClassName().toLowerCase().endsWith("page"))
                .findFirst();
        return first.map(StackTraceElement::getMethodName)
                .orElseGet(stackTrace[methodNameIndex]::getMethodName);
    }
}



