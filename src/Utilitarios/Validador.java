package Utilitarios;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {


    private static final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern PATRON_CORREO = Pattern.compile(REGEX_CORREO);

    public static boolean esCorreoValido(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = PATRON_CORREO.matcher(correo.trim());
        return matcher.matches();
    }
}