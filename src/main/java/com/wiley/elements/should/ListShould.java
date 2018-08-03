package com.wiley.elements.should;

import java.util.List;

public interface ListShould extends Should {

    void haveTexts(List<String> texts);

    void haveSize(int expectedSize);
}
