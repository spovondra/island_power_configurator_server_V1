package com.islandpower.configurator.exceptions;

/**
 * Výjimka označující, že projekt nebyl nalezen.
 */
public class ProjectNotFoundException extends RuntimeException {

    /**
     * Konstruktor výjimky s ID projektu.
     * @param projectId ID projektu, který nebyl nalezen.
     */
    public ProjectNotFoundException(String projectId) {
        super("Projekt nebyl nalezen: " + projectId);
    }
}
