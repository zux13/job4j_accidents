package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;

    @GetMapping("/create")
    public String viewCreateAccident(Model model) {
        model.addAttribute("types", accidentTypeService.findAll());
        return "/accidents/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        AccidentType type = accidentTypeService.get(accident.getType().getId());
        accident.setType(type);
        accidentService.save(accident);
        return "redirect:/index";
    }

    @GetMapping("/edit")
    public String viewEditAccident(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.get(id));
        model.addAttribute("types", accidentTypeService.findAll());
        return "/accidents/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Accident accident) {
        AccidentType type = accidentTypeService.get(accident.getType().getId());
        accident.setType(type);
        accidentService.update(accident);
        return "redirect:/index";
    }

}
