package org.itmo;

public record Message (
    String text,
    long timestamp,
    String sender,
    String password
) {}

