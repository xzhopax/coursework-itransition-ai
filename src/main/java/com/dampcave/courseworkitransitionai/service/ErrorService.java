package com.dampcave.courseworkitransitionai.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ErrorService {

    public boolean ifHasErrors(BindingResult bindingResult){
     return bindingResult.hasErrors();
    }
}
