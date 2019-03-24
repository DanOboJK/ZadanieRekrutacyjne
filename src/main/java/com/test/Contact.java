package com.test;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Contact {

    // w tej klasie znajdują się pola zapisywane w tabeli contacts
    private Integer id_customer;
    private ContactType type;
    private String contact;

    public Contact(ContactType type, String contact) {
        this.type = type;
        this.contact = contact;
    }
}
