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
        String originText = null;
        // 텍스트 뽑을 타입 결정
        if (EXCEPT_HTML_TAG.equalsIgnoreCase(textType)) {
            originText = doc.text();
        } else {
            originText = doc.html();
        }

//
        String onlyNumber = applyRegEx(originText, ONLY_NUMBER_REGEX);
        List<String> newOnlyNumbers = sortNumbers(onlyNumber);

        String onlyAlpha = applyRegEx(originText, ONLY_ALPHABET_REGEX);
        List<String> newOnlyAlphas =  sortAlphabet(onlyAlpha);

        //알파벳, 숫자 순으로 섞어 하나의 리스트로 만들기
        String fullText = mergeLists(newOnlyNumbers, newOnlyAlphas);

        //입력 사이즈 대로 쪼개기
        List<String> tokens = splitFullTextByPrintUnit(fullText, printUnit);

        Result result = makeResult(tokens);
        return result;
    }

    String applyRegEx(String originText, String only_number_regex) {
        if(originText == null){
            throw new IllegalArgumentException();
        }
        return originText.replaceAll(only_number_regex, "");
    }

    Result makeResult(List<String> tokens) {
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

    List<String> splitFullTextByPrintUnit(String fullText, int printUnit) {
        List<String> tokens = new ArrayList<>();
        for (int start = 0; start < fullText.length(); start += printUnit) {
            tokens.add(fullText.substring(start, Math.min(fullText.length(), start + printUnit)));
        }
        return tokens;
    }

    String mergeLists(List<String> newOnlyNumbers, List<String> newOnlyAlphas) {
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

    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

    List<String> sortAlphabet(String onlyAlpha) {
        List<String> onlyAlphas = Arrays.asList(onlyAlpha.split(""));
        Collections.sort(onlyAlphas, ALPHABETICAL_ORDER);
        return onlyAlphas;
    }

    List<String> sortNumbers(String onlyNumber) {
        List<Integer> onlyNumbers = Arrays.stream(onlyNumber.split(""))
                .map(s -> Integer.parseInt(s)).sorted()
                .collect(Collectors.toList());
        return onlyNumbers.stream()
                .map(s -> String.valueOf(s)).collect(Collectors.toList());
    }
}
