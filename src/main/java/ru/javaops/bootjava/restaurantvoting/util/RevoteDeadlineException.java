package ru.javaops.bootjava.restaurantvoting.util;

public class RevoteDeadlineException extends RuntimeException {
    public RevoteDeadlineException() {
        super("Revoting after 11:00 is forbidden");
    }
}
