package com.test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AplikacjaDoParsowania {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        DatabaseUtils.createDatabaseTables();
        //Użytkownik w tym miejscu podaje ścieżke do pliku
        System.out.println("Podaj ścieżke do pliku: ");
        String path = scanner.nextLine();
        if (new File(path).isFile()) {
            String extension = "";
            //W tym miejscu wyodrębnione zostaje rozszerzenie pliku
            int i = path.lastIndexOf('.');
            if (i > 0) {
                extension = path.substring(i + 1);
            }
            //Jeśli znaleziono rozszeżenie xml to wywołana zostaje metoda parsująca xml, jeśli rozszeżenie to csv lub txt to metoda parsująca csv.
            // W pozostałych przypadkach wyświetlony jest komunikat
            if (extension.equalsIgnoreCase("xml")) {
                XMLReader.xmlParser(path);
            } else if (extension.equalsIgnoreCase("csv") || extension.equalsIgnoreCase("txt")) {
                CSVReader.csvParser(path);
            } else {
                System.out.println("Zly format pliku");
            }
        }else {
            System.out.println("Podano ścieżke do złego pliku");
        }
    }
}
