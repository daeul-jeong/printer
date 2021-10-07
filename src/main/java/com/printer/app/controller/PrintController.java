package com.printer.app.controller;

import com.printer.app.model.Result;
import com.printer.app.service.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@Slf4j
public class PrintController {

    PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @RequestMapping("/")
    public String index(
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "textType", required = false) String textType,
            @RequestParam(value = "printUnit", required = false, defaultValue = "0") Integer printUnit,
            Model model) throws IOException {

        if(url == null){
            model.addAttribute("result", new Result());
            return "/index";
        }
        log.info(">>> request : url:{},textType:{},printUnit:{}" + url, textType, printUnit);
        String originText = this.printService.getText(url, textType);
        Result result = this.printService.process(originText, printUnit);
        model.addAttribute("result", result);
        log.info(result.toString());
        return "/index";
    }


}
