package com.wiley.autotest.spring;

/**
 * Creation date: 29.01.2016
 *
 * @author <a href='mailto:mmukhoyan@wiley.com'>Maxim Mukhoyan</a>
 * @version 1.0
 */

public interface StagePropertySelector {

    String getPropertyForDefault(String key);

    String getPropertyForEdugenQA(String key);

    String getPropertyForStgQA(String key);
}
