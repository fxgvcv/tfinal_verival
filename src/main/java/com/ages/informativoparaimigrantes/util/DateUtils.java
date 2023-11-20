package com.ages.informativoparaimigrantes.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date formatDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(date);}
        catch(Exception e) {
            return null;
        }
    }

    public static Boolean isStartBeforeEnd(Date startDate, Date endDate) {
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localStartDate.isBefore(localEndDate) || localStartDate.isEqual(localEndDate);
    }
}
