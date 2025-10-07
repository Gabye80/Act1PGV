package org.example;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para contar las vocales.
 */

public final class Vocales {

    // Incluye aeiou con acentos y diéresis. Usa el UNICODE_CASE para que las mayúsculas se detecten bien.
    private static final Pattern VOCALES_PATTERN =
            Pattern.compile("[aeiouáéíóúäëïöü]", Pattern.UNICODE_CASE);

    /**
     * Convierte una cadena a minúsculas usando la localización española.
     * @param s cadena original.
     * @return la cadena en minúsculas.
     */

    public static String toLowerEsp(String s) {
        return s.toLowerCase(Locale.forLanguageTag("es"));
    }

    /**
     * Cuenta las vocales en la cadena.
     * @param s cadena.
     * @return int del total de vocales encontradas
     */
    public static int contarVocales(String s) {
        Matcher m = VOCALES_PATTERN.matcher(toLowerEsp(s));
        int total = 0;
        while (m.find()) total++;
        return total;
    }
}