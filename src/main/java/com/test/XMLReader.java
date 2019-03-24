package com.test;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLReader {

    public static void xmlParser(String path) {
        try {
            //SAXParser pozwala na wczytywanie danych z pliku linia po lini bez konieczności wczytywania całego pliku do pamięci
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                HashMap<String, Boolean> elements = new HashMap<String, Boolean>();
                Customer person = new Customer();
                ArrayList<Contact> contacts = new ArrayList<>();
                boolean isContacts = false;

                //Nadpisanie metody startElement znajdującej się w klasie DefaultHandler
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    elements.put(qName, true);
                    if (qName.equalsIgnoreCase("contacts")) {
                        isContacts = true;
                    }
                }
                //Nadpisanie metody endElement znajdującej się w klasie DefaultHandler. Metoda ta wykonuje się po znalezieniu znaczników kończących person oraz contact
                @Override
                public void endElement(String uri, String localName, String qName) {
                    if (qName.equalsIgnoreCase("contacts")) {
                        isContacts = false;
                    }
                    if (qName.equalsIgnoreCase("person")) {
                        //Po znalezieniu znacznika person, do tabeli customers, zapisywane są główne dane dotyczące pojedynczej osoby,
                        // następnie pobierane jest id tej osoby oraz do tabeli contacts zapisywane są dane kontaktowe wraz z tym id
                        // przy użyciu metody insertContact znajdującej się w DatabaseUtils
                        final Long CustomerId = DatabaseUtils.insertPerson(person);
                        contacts.forEach(contact -> DatabaseUtils.insertContact(contact, CustomerId));
                        person = new Customer();
                        // w tym miejscu arrayList contact jest wyzerowana
                        contacts.clear();
                    }
                }

                @Override
                public void characters(char[] characters, int start, int length) {
                    // w tym miejscu przeszukiwany jest dokument, po znalezieniu odpowiednich znaczników przy pomocy setterów dla obiektu person
                    // ustawiane są jego pola.
                    if (elements.containsKey("name") && elements.get("name")) {
                        person.setName(new String(characters, start, length));
                        elements.put("name", false);
                    } else if (elements.containsKey("surname") && elements.get("surname")) {
                        person.setSurname(new String(characters, start, length));
                        elements.put("surname", false);
                    } else if (elements.containsKey("age") && elements.get("age")) {
                        person.setAge(new String(characters, start, length));
                        elements.put("age", false);
                    } else if (elements.containsKey("city") && elements.get("city")) {
                        person.setCity(new String(characters, start, length));
                        elements.put("city", false);
                        // podczas znalezienia znacznika dotyczącego kontaktu do arrayList contact zapisywany jest nowy Obiekt Contact zawierający numer określający
                        // typ kontaktu, oraz uzyskaną wartość
                    } else if (elements.containsKey("email") && elements.get("email")) {
                        contacts.add(new Contact(ContactType.EMAIL, new String(characters, start, length)));
                        elements.put("email", false);
                    } else if (elements.containsKey("phone") && elements.get("phone")) {
                        contacts.add(new Contact(ContactType.PHONE, new String(characters, start, length)));
                        elements.put("phone", false);
                    } else if (elements.containsKey("jabber") && elements.get("jabber")) {
                        contacts.add(new Contact(ContactType.JABBER, new String(characters, start, length)));
                        elements.put("jabber", false);
                        // W przypadku gdy nie rozpoznany jest typ kontaktu, uzyskana wartość przypisana jest do typu unknown
                    } else if (isContacts && !new String(characters, start, length).contains("\n")) {
                        contacts.add(new Contact(ContactType.UNKNOWN, new String(characters, start, length)));
                    }
                }
            };
            //wywołanie metody parse na pliku podanym za pomocą ścieżki
            saxParser.parse(path, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}