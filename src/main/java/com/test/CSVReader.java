package com.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
    public static void csvParser(String path) throws IOException {
        String line = "";
        String csvSplitBy = ",";
        //wczytywane jest linia danych z pliku podanego w ścieżce, następnie do StringList zapisywane są wyrazy które
        // powstały z podzielenia wiersza po kążdym przecinku
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                Customer customer = new Customer();
                // W tym miejscu do obiektu customer przypisywane są z listy data, podstawowe dane w kolejności
                // występowania na liście
                customer.setName(data[0]);
                customer.setSurname(data[1]);
                customer.setAge(data[2]);
                customer.setCity(data[3]);
                // w tym miejscu do bazy zapisywany jest utworzony obiekt customer i jednocześnie pobrane jest id tej osoby
                Long customerId = DatabaseUtils.insertPerson(customer);
                //utworzona zostaje ArrayList w której zapisywane będą obiekty Contact
                ArrayList<Contact> contacts = new ArrayList<>();
                //w tej pętli walidowane są dane, uznałem że:
                // 1. edres email musi zawierać w sobie @
                // 2. numer telefonu to 9 następujących po sobie cyfr, spacje zostają pomijane
                // 3. niestety nie wiem co to jest jabber więc uznałem że jest to ciąg liter
                // 4. w pozostałych przypadkach contakt zapisywany jest jako unknown
                for (int i = 4; i < data.length; i++) {
                    data[i] = data[i].replace(" ", "");
                    if (data[i].contains("@")) {
                        contacts.add(new Contact(ContactType.EMAIL, data[i]));
                    } else if (data[i].length() == 9 && data[i].matches("\\d+")) {
                        contacts.add(new Contact(ContactType.PHONE, data[i]));
                    }else if (data[i].matches("[a-zA-Z]+")) {
                        contacts.add(new Contact(ContactType.JABBER, data[i]));
                    }else {
                        contacts.add(new Contact(ContactType.UNKNOWN, data[i]));
                    }
                }
                //w tym miejscu znajdujące się w liście contacts obiekty contact są zapisywane do tabeli contacts
                contacts.forEach(contact -> DatabaseUtils.insertContact(contact, customerId));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
