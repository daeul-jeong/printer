package com.printer.app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.goodoc.co.kr/login").get();

        String docHtml = doc.html();
        System.out.println(docHtml);

        String text = "김#z0Aaa17bBZA?";

        String onlyNumber = text.replaceAll("[^0-9]", "");

        System.out.println(onlyNumber);
        List<Integer> onlyNumbers = Arrays.stream(onlyNumber.split(""))
                .map(s -> Integer.parseInt(s)).sorted()
                .collect(Collectors.toList());
        List<String> newOnlyNumbers = onlyNumbers.stream()
                .map(s -> String.valueOf(s)).collect(Collectors.toList());
        System.out.println(newOnlyNumbers);


        String onlyAlpha = text.replaceAll("[^a-zA-Z]", "");
        List<String> onlyAlphas = Arrays.asList(onlyAlpha.split(""));
        Collections.sort(onlyAlphas, Collator.getInstance(Locale.ENGLISH));
        List<String> newOnlyAlphas = onlyAlphas.stream().map(e -> {
            int ascii = e.charAt(0);
            if (ascii >= 97 && ascii <= 122) {
                return e.toUpperCase();
            } else {
                return e.toLowerCase();
            }
        }).collect(Collectors.toList());

        System.out.println(newOnlyAlphas);

        int i = 0;
        int j = 0;
        List<String> merged_list = new ArrayList<String>();
        while (i < newOnlyAlphas.size() && j < newOnlyNumbers.size()) {
            merged_list.add(newOnlyAlphas.get(i));
            merged_list.add(newOnlyNumbers.get(j));
            i++;
            j++;
        }
        while (i < newOnlyAlphas.size()) {
            merged_list.add(newOnlyAlphas.get(i));
            i++;
        }
        while (j < newOnlyNumbers.size()) {
            merged_list.add(String.valueOf(newOnlyNumbers.get(j)));
            j++;
        }

        StringBuilder sb = new StringBuilder();
        Iterator iterator = merged_list.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
        }
        String fullText = sb.toString();
        int size = 100;

        List<String> tokens = new ArrayList<>();

        for (int start = 0; start < fullText.length(); start += size) {
            tokens.add(fullText.substring(start, Math.min(fullText.length(), start + size)));
        }

        for (String token : tokens) {
            System.out.println(token);
        }

    }
}
