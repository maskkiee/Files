package com.example.pliki.zad3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportCsv {

    record Sprzedaz(int id, String data, String region, String produkt, double cena, int ilosc) {
        double wartosc() {
            return cena * ilosc;
        }
    }

    static int pominietych = 0;

    /**
     * Parsuje jedną linię CSV na Sprzedaz.
     * Zły wiersz => log na System.err + Optional.empty() (program leci dalej).
     */
    static Optional<Sprzedaz> parsuj(String linia, int nrLinii) {
        // TODO 1: split(",", -1); jeśli pól != 6 -> log na stderr, pominietych++, Optional.empty()
        String[] p = linia.split(",", -1);
        if (p.length != 6) {
            System.err.printf("linia %d: oczekiwano 6 pól, jest %d -> pomijam%n",
                    nrLinii, p.length);
            pominietych++;
            return Optional.empty();
        }
        // TODO 2: w try/catch (NumberFormatException) zbuduj Optional.of(new Sprzedaz(...))
        //          format logu: "linia %d: oczekiwano 6 pól, jest %d -> pomijam%n"
        //                       "linia %d: zła liczba (%s) -> pomijam%n"
        try {
            int id = Integer.parseInt(p[0].trim());
            String data = p[1].trim();
            String region = p[2].trim();
            String produkt = p[3].trim();
            double cena = Double.parseDouble(p[4].trim());
            int ilosc = Integer.parseInt(p[5].trim());
            return Optional.of(new Sprzedaz(id, data, region, produkt, cena, ilosc));
        } catch (NumberFormatException e) {
            System.err.printf("linia %d: zła liczba (%s) -> pomijam%n",
                    nrLinii, e.getMessage());
            pominietych++;
            return Optional.empty();
        }
    }

    public static void main(String[] args) throws IOException {
        Path zrodlo = Path.of("dane/zad3/sprzedaz.csv");

        // TODO 3: wczytaj LENIWIE (Files.lines + try-with-resources), pomijając nagłówek
        //          i puste linie; złe wiersze wypadną same przez flatMap(...stream())
        int[] nr = {0};
        List<Sprzedaz> dane = List.of();
        try (Stream<String> linie = Files.lines(zrodlo, StandardCharsets.UTF_8)) {
            dane = linie.peek(l -> nr[0]++)
                    .skip(1)
                    .filter(l -> !l.trim().isEmpty())
                    .flatMap(l -> parsuj(l, nr[0]).stream())
                    .toList();
        }
        System.out.println("Wczytano: " + dane.size() + " wierszy, pominięto: " + pominietych);

        // TODO 4: policz sumę wartości i liczbę transakcji w każdym regionie
        //          (Collectors.groupingBy + summingDouble / counting)
        Map<String, Double> wgRegionu = dane.stream()
                .collect(Collectors.groupingBy(Sprzedaz::region,
                        Collectors.summingDouble(Sprzedaz::wartosc)));

        Map<String, Long> liczbaTransakcji = dane.stream()
                .collect(Collectors.groupingBy(Sprzedaz::region,
                        Collectors.counting()));

        // TODO 5: posortuj regiony malejąco po wartości i zbuduj wiersze "region,wartosc,transakcje"
        //          format kwoty: "%.2f"
        List<String> wiersze = wgRegionu.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(e -> String.format("%s,%.2f,%d",
                        e.getKey(), e.getValue(), liczbaTransakcji.get(e.getKey())))
                .toList();

        System.out.println("--- raport ---");
        wiersze.forEach(w -> System.out.println("  " + w));

        // TODO 6: znajdź najlepszą pojedynczą transakcję (max po wartosc())
        //          i wypisz: "Najlepsza transakcja: #%d %s (%.2f zł)%n"
        Sprzedaz najlepsza = dane.stream()
                .max(Comparator.comparingDouble(Sprzedaz::wartosc))
                .orElseThrow();
        System.out.printf("Najlepsza transakcja: #%d %s (%.2f zl)%n",
                najlepsza.id(), najlepsza.produkt(), najlepsza.wartosc());

        // TODO 7: zapisz raport ATOMOWO: writeString do pliku .tmp, potem Files.move
        //          z REPLACE_EXISTING + ATOMIC_MOVE na dane/zad3/raport-regiony.csv
        Path cel = Path.of("dane/zad3/raport-regiony.csv");
        Path tmp = Path.of("dane/zad3/raport-regiony.csv.tmp");
        String tresc = Stream.concat(Stream.of("region,wartosc,transakcje"), wiersze.stream())
                .collect(Collectors.joining("\n", "", "\n"));
        Files.writeString(tmp, tresc, StandardCharsets.UTF_8);
        Files.move(tmp, cel,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
        System.out.println("Zapisano " + cel);
    }
}