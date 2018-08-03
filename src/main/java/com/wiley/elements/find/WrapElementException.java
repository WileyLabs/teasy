package com.wiley.elements.find;

import com.wiley.utils.Report;

class WrapElementException extends RuntimeException {

    WrapElementException(Throwable cause) {
        super(cause.getClass().getName() + "Cannot create instance of TeasyElement.", cause);
        Report.jenkins("Cannot create instance of TeasyElement."
                + cause.getClass().getName(), cause);
    }
}
