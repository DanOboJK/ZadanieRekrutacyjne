package com.test;

public enum ContactType {
    // w klasie ContactType znajdują się enumy do których przypisane są wartości numeryczne
    UNKNOWN(0),
    EMAIL(1),
    PHONE(2),
    JABBER(3);

    int typeNumber;

    ContactType(int typeNumber) {
        this.typeNumber = typeNumber;
    }
}
