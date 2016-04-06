package web.helpers;

import java.util.Arrays;

/**
 * Created by Mindaugas on 07/04/2016.
 */
public class ExceptionHelper {
    public static String exceptionStacktraceToString(Exception e)
    {
        return Arrays.toString(e.getStackTrace());
    }
}
