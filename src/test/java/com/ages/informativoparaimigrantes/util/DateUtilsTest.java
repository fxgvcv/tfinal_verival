package com.ages.informativoparaimigrantes.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DateUtilsTest {

    private DateUtils dateUtils = new DateUtils();

    @Test
    public void isStartBeforeEndDateShouldReturnTrue() {
        Date startDate = dateUtils.formatDate("01/01/2021");
        Date endDate = dateUtils.formatDate("01/01/2022");
        assertEquals(true, dateUtils.isStartBeforeEnd(startDate, endDate));
    }
}
