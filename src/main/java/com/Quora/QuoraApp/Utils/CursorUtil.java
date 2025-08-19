package com.Quora.QuoraApp.Utils;

import java.time.LocalDateTime;

public class CursorUtil {

    public static boolean isValidCursor(String cursor){
        if(cursor==null || cursor.isEmpty())
            return false;
       try{
           LocalDateTime.parse(cursor);
           return true;
       }catch(Exception e){
           return false;
       }
    }
    public static LocalDateTime parseCursor(String cursor){
        return LocalDateTime.parse(cursor);
    }
}
