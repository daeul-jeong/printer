package com.printer.app.service;

import com.printer.app.model.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrintServiceTest {
    @Autowired
    private PrintService printService;

    @Test
    void process() {
        Result result = this.printService.process("김#z0Aaa17bBZA?", 5);
        assertThat(result.getRemainder()).isEqualTo("z");
        assertThat(result.getUnit()).isEqualTo("A0A1a\n7aBbZ\n");
    }

    @Test
    @DisplayName("숫자만 남겨지는 함수가 잘 동작되는지 확인")
    void applyRegEx_onlyNumber() {
        assertThat(this.printService.applyRegEx("A1234<#@", "[^0-9]")).isEqualTo("1234");
    }

    @Test
    @DisplayName("알파벳만 남겨지는 함수가 잘 동작되는지 확인")
    void applyRegEx_onlyAlphabet() {
        assertThat(this.printService.applyRegEx("A1234*&^!", "[^a-zA-Z]")).isEqualTo("A");
    }

    @Test
    @DisplayName("오름순으로 정렬되는지 확인")
    void sortNumbers() {
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expected.add(String.valueOf(i));
        }
        List<String> actual = this.printService.sortNumbers("9126380475");
        for (int i = 0; i < 10; i++) {
            assertThat(actual.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    @DisplayName("ABCD 알파벳순으로 대문자 소문자 번갈아 나오는지 확인")
    void sortAlphabet() {
        List<String> expected = new ArrayList<>();
        expected.add("A");
        expected.add("b");
        expected.add("C");
        expected.add("c");
        expected.add("Z");
        expected.add("z");

        List<String> actual = this.printService.sortAlphabet("zZcCbA");
        for (int i = 0; i < expected.size(); i++) {
            assertThat(actual.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test
    @DisplayName("알파벳, 숫자가 번갈아 가며 나와야한다")
    void mergeLists() {
        List<String> alphabet = new ArrayList<>();
        alphabet.add("A");
        alphabet.add("B");
        alphabet.add("b");
        alphabet.add("C");
        List<String> number = new ArrayList<>();
        number.add("1");
        number.add("2");
        number.add("3");

        assertThat(this.printService.mergeLists(number, alphabet)).isEqualTo("A1B2b3C");
    }

    @Test
    @DisplayName("문자열을 주어진 숫자 크기로 쪼갠다")
    void splitFullTextByPrintUnit() {
        List<String> tokens = this.printService.splitFullTextByPrintUnit("1234567890", 3);
        assertThat(tokens.size()).isEqualTo(4);
        assertThat(tokens.get(tokens.size() - 1)).isEqualTo("0");
    }

    @Test
    @DisplayName("결과값 확인")
    void makeResult() {
        List<String> tokens = this.printService.splitFullTextByPrintUnit("1234567890", 3);
        Result result = this.printService.makeResult(tokens);
        assertThat(result.getRemainder()).isEqualTo("0");
        assertThat(result.getUnit()).isEqualTo("123\n456\n789\n");
    }
}