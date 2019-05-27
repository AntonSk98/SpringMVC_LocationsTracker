package com.internship.controllers.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafExample {
    @RequestMapping(value = "ui/index")
    public String index(Model model) {
        model.addAttribute("message", "Hi,my name is Anton!");
        return "index";
    } //простой пример отображения thymeleaf страницы

}
