package com.example.pliki.start;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Tworzy wszystkie pliki wejściowe do zadań 1–3. Uruchom RAZ, przed zadaniami. */
public class zad1 {

    public static void main(String[] args) throws IOException {
        // ── zadanie 1 ────────────────────────────────────────────────
        Files.createDirectories(Path.of("dane/zad1"));

        Files.writeString(Path.of("dane/zad1/artykul.txt"), """
                Java potrafi czytać pliki na wiele sposobów
                ale w codziennej pracy wystarczy Files i Path
                najtrudniejsze bywa kodowanie znaków
                """, StandardCharsets.UTF_8);

        Files.writeString(Path.of("dane/zad1/pusty.txt"), "", StandardCharsets.UTF_8);

        Files.writeString(Path.of("dane/zad1/remis.txt"), """
                abc def ghi
                abcdefghij klmnopqrst
                """, StandardCharsets.UTF_8);

        // ── zadanie 2 ────────────────────────────────────────────────
        Files.createDirectories(Path.of("dane/zad2"));

        Files.writeString(Path.of("dane/zad2/wazne.txt"), """
                Faktura FV/2026/001
                Kontrahent: Kowalski Sp. z o.o.
                Kwota: 12 345,67 zł
                Data wystawienia: 2026-01-15
                """, StandardCharsets.UTF_8);

        // ── zadanie 3 ────────────────────────────────────────────────
        Files.createDirectories(Path.of("dane/zad3"));

        Files.writeString(Path.of("dane/zad3/sprzedaz.csv"), """
                id,data,region,produkt,cena,ilosc
                1,2026-01-04,Polnoc,Laptop Dell,3499.99,2
                2,2026-01-05,Poludnie,Mysz Logitech,199.99,10
                3,2026-01-05,Polnoc,Klawiatura MX,499.99,4
                4,2026-01-06,Zachod,Monitor 27,1299.00
                5,2026-01-07,Poludnie,Sluchawki Sony,899.99,3
                6,2026-01-08,Zachod,Kamerka HD,349.50,abc
                7,2026-01-09,Polnoc,Dok USB-C,749.00,5

                8,2026-01-10,Wschod,Laptop Lenovo,4199.00,1
                9,2026-01-11,Poludnie,Podkladka,49.99,20
                10,2026-01-12,Wschod,Monitor 32,2199.00,2
                """, StandardCharsets.UTF_8);

        System.out.println("Gotowe. Pliki wejściowe leżą w " + Path.of("dane").toAbsolutePath());
    }
}