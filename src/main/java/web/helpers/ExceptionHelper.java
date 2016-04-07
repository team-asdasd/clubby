package web.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by Mindaugas on 07/04/2016.
 */
public class ExceptionHelper {
    public static String exceptionStacktraceToString(Exception e)
    {
        String exception = "";
        if( e instanceof InvocationTargetException){
            Throwable ex =  ((InvocationTargetException) e).getTargetException();
            exception = ex.getMessage() + " " + Arrays.toString(ex.getStackTrace());
        }

        exception += e.getMessage() + " " + Arrays.toString(e.getStackTrace());
        return exception;
    }
}
