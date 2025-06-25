package ru.job4j.accidents.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;

    @GetMapping("/create")
    public String viewCreateAccident(Model model) {
        model.addAttribute("rules", ruleService.findAll());
        model.addAttribute("types", accidentTypeService.findAll());
        return "/accidents/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest request) {
        AccidentType type = accidentTypeService.get(accident.getType().getId());
        accident.setType(type);

        String[] rIds = request.getParameterValues("rIds");
        if (rIds != null) {
            List<Integer> ids = Arrays.stream(rIds)
                    .map(Integer::parseInt)
                    .toList();

            accident.setRules(ruleService.findByIds(ids));
        }

        accidentService.save(accident);
        return "redirect:/index";
    }

    @GetMapping("/edit")
    public String viewEditAccident(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.get(id));
        model.addAttribute("rules", ruleService.findAll());
        model.addAttribute("types", accidentTypeService.findAll());
        return "/accidents/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Accident accident, HttpServletRequest request) {
        AccidentType type = accidentTypeService.get(accident.getType().getId());
        accident.setType(type);

        String[] rIds = request.getParameterValues("rIds");
        if (rIds != null) {
            List<Integer> ids = Arrays.stream(rIds)
                    .map(Integer::parseInt)
                    .toList();

            accident.setRules(ruleService.findByIds(ids));
        }

        accidentService.update(accident);
        return "redirect:/index";
    }

}
