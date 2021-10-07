package com.printer.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrintServiceTest {
    @Autowired
    private PrintService printService;

    @Test
    void process() {
    }

    @Test
    @DisplayName("의도한 정규식이 잘 동작되는지 확인")
    void applyRegEx_onlyNumber() {
        assertThat(this.printService.applyRegEx("A1234<#@", "[^0-9]")).isEqualTo("1234");
    }

    @Test
    @DisplayName("의도한 정규식이 잘 동작되는지 확인")
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
    void mergeLists() {
    }

    @Test
    void splitFullTextByPrintUnit() {
    }

    @Test
    void makeResult() {
    }
}