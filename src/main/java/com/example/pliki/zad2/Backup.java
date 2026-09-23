package com.example.pliki.zad2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Backup {

    // dwukropki są zabronione w nazwach plików na Windowsie — dlatego myślniki
    private static final DateTimeFormatter STEMPEL =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) throws IOException {
        Path zrodlo = Path.of("dane/zad2/wazne.txt");
        Path katalogKopii = Path.of("dane/zad2/backupy");

        // TODO 1: jeśli plik źródłowy nie istnieje — wypisz komunikat i zakończ (return),
        //          bez stack trace'a. Podpowiedź: Files.notExists(zrodlo)
        if (Files.notExists(zrodlo)) {
            System.out.println(zrodlo + " doesn't exist");
            return;
        }
        // TODO 2: utwórz katalog kopii, jeśli go nie ma (Files.createDirectories)
        Files.createDirectories(katalogKopii);

        // TODO 3: zbuduj nazwę kopii: wazne_yyyy-MM-dd_HH-mm-ss.txt
        //          LocalDateTime.now().format(STEMPEL)
        String nazwa = zrodlo.getFileName().toString();
        int kropka = nazwa.lastIndexOf('.');
        String nazwaBaza = kropka > 0 ? nazwa.substring(0, kropka) : nazwa;
        String rozszerzenie = kropka > 0 ? nazwa.substring(kropka) : "";
        String nazwaKopii = nazwaBaza + "_" + LocalDateTime.now().format(STEMPEL) + rozszerzenie;
        Path kopia = katalogKopii.resolve(nazwaKopii);

        // TODO 4: skopiuj plik (Files.copy)
        Files.copy(zrodlo, kopia);

        // TODO 5: wypisz ścieżkę BEZWZGLĘDNĄ kopii, rozmiary obu plików
        //          i wynik porównania treści: Files.mismatch(zrodlo, kopia) == -1
        System.out.println("Utworzono backup: " + kopia.toAbsolutePath());
        System.out.println("Rozmiar: " + Files.size(kopia) + " B (oryginal: " + Files.size(zrodlo) + " B)");
        System.out.println("Tresc identyczna? " + (Files.mismatch(zrodlo, kopia) == -1));
    }
}