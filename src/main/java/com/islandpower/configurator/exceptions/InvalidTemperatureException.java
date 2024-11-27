package com.islandpower.configurator.exceptions;

/**
 * Výjimka označující, že byla zadána neplatná teplota.
 */
public class InvalidTemperatureException extends RuntimeException {

    /**
     * Konstruktor výjimky s neplatnou teplotou.
     * @param temperature Zadaná neplatná hodnota teploty.
     */
    public InvalidTemperatureException(double temperature) {
        super("Neplatná teplota: " + temperature + ". Povolené hodnoty: 25 °C, 40 °C, 65 °C.");
    }
}
