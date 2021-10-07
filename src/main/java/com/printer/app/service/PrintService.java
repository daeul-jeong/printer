package com.printer.app.service;

import com.printer.app.model.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrintService {
    private final String EXCEPT_HTML_TAG = "exceptHtmlTag";
    private final String ONLY_NUMBER_REGEX = "[^0-9]";
    private final String ONLY_ALPHABET_REGEX = "[^a-zA-Z]";

    public Result process(String url, String textType, int printUnit) throws IOException {
        if(url == null){
            return new Result();
        }
        Document doc = Jsoup.connect(url).get();
        String text = null;
        // 텍스트 뽑을 타입 결정
        if (EXCEPT_HTML_TAG.equalsIgnoreCase(textType)) {
            text = doc.text();
        } else {
            text = doc.html();
        }

        String onlyNumber = text.replaceAll(ONLY_NUMBER_REGEX, "");
        List<String> newOnlyNumbers = sortNumbers(onlyNumber);

        String onlyAlpha = text.replaceAll(ONLY_ALPHABET_REGEX, "");
        List<String> newOnlyAlphas =  sortAlphabet(onlyAlpha);

        //알파벳, 숫자 순으로 섞어 하나의 리스트로 만들기
        String fullText = mergeLists(newOnlyNumbers, newOnlyAlphas);

        //입력 사이즈 대로 쪼개기
        List<String> tokens = splitFullTextByPrintUnit(fullText, printUnit);

        Result result = makeResult(tokens);
        return result;
    }

    private Result makeResult(List<String> tokens) {
        Result.ResultBuilder result = Result.builder();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String token : tokens) {
            if(tokens.size() - 1 == index){
                result.remainder(token);
                continue;
            }
            sb.append(token).append("\n");
            index++;
        }
        result.unit(sb.toString());
        return result.build();
    }

    private List<String> splitFullTextByPrintUnit(String fullText, int printUnit) {
        List<String> tokens = new ArrayList<>();
        for (int start = 0; start < fullText.length(); start += printUnit) {
            tokens.add(fullText.substring(start, Math.min(fullText.length(), start + printUnit)));
        }
        return tokens;
    }

    private String mergeLists(List<String> newOnlyNumbers, List<String> newOnlyAlphas) {
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

        return fullText;
    }

    private List<String> sortAlphabet(String onlyAlpha) {
        List<String> onlyAlphas = Arrays.asList(onlyAlpha.split(""));
        Collections.sort(onlyAlphas, Collator.getInstance(Locale.ENGLISH));
        return onlyAlphas.stream().map(e -> {
            int ascii = e.charAt(0);
            if (ascii >= 97 && ascii <= 122) {
                return e.toUpperCase();
            } else {
                return e.toLowerCase();
            }
        }).collect(Collectors.toList());
    }

    private List<String> sortNumbers(String onlyNumber) {
        List<Integer> onlyNumbers = Arrays.stream(onlyNumber.split(""))
                .map(s -> Integer.parseInt(s)).sorted()
                .collect(Collectors.toList());
        return onlyNumbers.stream()
                .map(s -> String.valueOf(s)).collect(Collectors.toList());
    }
}
