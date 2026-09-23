package com.example.pliki.zad1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class RaportOPliku {

    public static void main(String[] args) throws IOException {
        // ścieżka z argumentu, a jak nie ma argumentu — plik domyślny
        Path plik = Path.of(args.length > 0 ? args[0] : "dane/zad1/artykul.txt");

        List<String> linie;
        try {
            linie = Files.readAllLines(plik, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            System.out.println("Nie ma takiego pliku: " + plik);
            return;
        }

        int slowa = 0;
        int znaki = 0;
        String najdluzsze = "";

        for (String linia : linie) {
            // TODO 1: dodaj długość linii do licznika znaków
            znaki += linia.length();
            // TODO 2: podziel linię na słowa (linia.split("\\s+")), pomijając puste kawałki
            String[] podzielone = linia.split("\\s+");
            // TODO 3: policz słowa i zapamiętaj najdłuższe (przy remisie wygrywa PIERWSZE)
            for (String slowo : podzielone) {
                if (slowo.isEmpty()) continue;
                slowa++;
                if (slowo.length() > najdluzsze.length()) {
                    najdluzsze = slowo;
                }
            }
        }

        String raport = """
                Plik: %s
                Liczba linii: %d
                Liczba słów: %d
                Liczba znaków: %d
                Najdłuższe słowo: %s
                """.formatted(plik, linie.size(), slowa, znaki,
                najdluzsze.isEmpty() ? "(brak)" : najdluzsze);

        System.out.print(raport);

        // TODO 4: zapisz raport obok pliku wejściowego jako raport.txt (UTF-8)
        //          podpowiedź: plik.resolveSibling("raport.txt") + Files.writeString
        Path wynik = plik.resolveSibling("raport.txt");
        Files.writeString(wynik, raport, StandardCharsets.UTF_8);
        System.out.println("Raport zapisany do: " + wynik);
    }
}