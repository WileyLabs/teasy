package com.wiley.holders;

import com.wiley.assertions.TeasyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorsHolder {

    private static ThreadLocal<Map<String, List<TeasyError>>> holder = ThreadLocal.withInitial(HashMap::new);

    public static List<TeasyError> getErrorListForTest(String testName) {
        return holder.get().get(testName);
    }

    public static synchronized void addError(String testName, TeasyError error) {
        List<TeasyError> errorListForTest = getErrorListForTest(testName);
        if (errorListForTest == null) {
            errorListForTest = new ArrayList<>();
        }
        errorListForTest.add(error);

        holder.get().put(testName, errorListForTest);
    }
}
