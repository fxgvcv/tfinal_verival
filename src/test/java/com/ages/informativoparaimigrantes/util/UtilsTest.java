package com.ages.informativoparaimigrantes.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UtilsTest {

    @Test
    public void emailValidationShouldSucceed(){
        assertTrue(Utils.emailIsValid("lucas@gmail.com"));
    }

    @Test
    public void emailValidationShouldFail(){
        assertTrue(Utils.emailIsValid("mail@edu.pucrs.br"));
    }
}
