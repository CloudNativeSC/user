package com.cluvy.user.exception.handler;

import com.cluvy.user.exception.GeneralException;
import com.cluvy.user.response.BaseErrorCode;

public class GeneralHandler extends GeneralException {
    public GeneralHandler(BaseErrorCode code) {
        super(code);
    }
}
