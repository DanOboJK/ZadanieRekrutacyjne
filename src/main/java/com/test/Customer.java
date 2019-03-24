package com.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {

    //W klasie znajdują się pola zawierające informacje o osobie
    private Long id;
    private String name;
    private String surname;
    //Pole age zostało ustawione jako String a nie int ze względu na to że ułatwiło zapis pola do bazy danych
    private String age;
    private String city;
}
