package com.islandpower.configurator.exceptions;

/**
 * Výjimka označující, že invertor nebyl nalezen.
 */
public class InverterNotFoundException extends RuntimeException {

    /**
     * Konstruktor výjimky s ID invertoru.
     * @param inverterId ID invertoru, který nebyl nalezen.
     */
    public InverterNotFoundException(String inverterId) {
        super("Invertor nebyl nalezen: " + inverterId);
    }
}
