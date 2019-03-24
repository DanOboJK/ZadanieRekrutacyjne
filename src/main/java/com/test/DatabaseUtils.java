package com.test;

import java.sql.*;

public class DatabaseUtils {
    //pola poniżej są ustawione na stałe i służą do połączenia aplikacji z bazą danych
    public static final String URL = "jdbc:postgresql://localhost/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    //metoda tworząca całą strukture tabel w bazie danych, jeśli podczas wywoływania metody, w bazie danych znajdują się
    // już tabele o podanych nazwach to zostają najpierw usunięte a dopiero później utworzona zostaje struktura
    static void createDatabaseTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = conn.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS CONTACTS");
            statement.execute("DROP TABLE IF EXISTS CUSTOMERS");

            statement.execute("CREATE TABLE CUSTOMERS(\n" +
                    " id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
                    " name VARCHAR (250) NOT NULL,\n" +
                    " surname VARCHAR (250) NOT NULL,\n" +
                    " age VARCHAR(250),\n" +
                    " city VARCHAR (250)\n" +
                    ")");

            statement.execute("CREATE TABLE CONTACTS(\n" +
                    " id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
                    " id_customer INTEGER REFERENCES CUSTOMERS(id),\n" +
                    " type INTEGER NOT NULL,\n" +
                    " contact VARCHAR NOT NULL\n" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //w ciele tej metody określone jest w jaki sposób dane zapisywane są do tabeli customers. Następnie pobierane jest
    // nowo wygenerowane id osoby które zwracane po wykonaniu metody
    static Long insertPerson(Customer customer) {
        Long id = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement("INSERT INTO CUSTOMERS (name, surname, age, city) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getSurname());
            statement.setString(3, customer.getAge());
            statement.setString(4, customer.getCity());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    // podobnie jak w poprzedniej metodzie zawarte tu są dane konfiguracyjne oraz określony jest sposób dodawania danych
    // do tabeli contacts. Metoda przyjmuje jako parametry: obiekt contakt oraz id osoby której dotyczy ten obiect
    static void insertContact(Contact contact, Long customerId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement("INSERT INTO contacts (id_customer, type, contact) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, customerId);
            statement.setInt(2, contact.getType().typeNumber);
            statement.setString(3, contact.getContact());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
