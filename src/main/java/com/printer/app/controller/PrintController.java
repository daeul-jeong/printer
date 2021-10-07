package com.printer.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class PrintController {

    @RequestMapping("/")
    public String index(
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "textType", required = false) String textType,
            @RequestParam(value = "printUnit", required = false) String printUnit,
            Model model
    ) {

        model.addAttribute("url", url);
        model.addAttribute("textType", textType);
        model.addAttribute("printUnit", printUnit);
        log.info("@@@@@@ " + url, textType, printUnit);
        return "/index";
    }


}
