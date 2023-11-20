package com.ages.informativoparaimigrantes.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils
{
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    //Copies all non-null properties from src to target. Useful for patch requests
    public static void copyNotNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static boolean cpfIsValid(String cpf)
    {
        return cpf != null && (cpf.length() == 11 && isNumeric(cpf));
    }

    public static boolean emailIsValid(String email){
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean cnpjIsValid(String cnpj){
        return cnpj != null && (cnpj.length() == 14 && isNumeric(cnpj));
    }

    public static boolean passwordIsValid(String password) {
        return password != null && password.length() > 7;
    }

    public static boolean isNumeric(String target){
        try {
            Long.parseLong(target);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
