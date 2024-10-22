package com.linkease.controller;

import com.linkease.domain.Config;
import com.linkease.repository.ConfigTypeRepository;
import com.linkease.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/configs")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;
    private final ConfigTypeRepository configTypeRepository;

    @GetMapping
    public String getAllConfigs(Model model) {
        model.addAttribute("configs", configService.getAllConfigs());
        return "configs/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("config", new Config());
        model.addAttribute("configTypes", configTypeRepository.findAll());  // Adding config types to the model
        return "configs/create";
    }

    @PostMapping("/create")
    public String createConfig(@ModelAttribute Config config) {
        configService.createConfig(config);
        return "redirect:/configs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Config config = configService.getConfigById(id);
        model.addAttribute("config", config);
        model.addAttribute("configTypes", configTypeRepository.findAll());  // Adding config types to the model
        return "configs/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateConfig(@PathVariable Long id, @ModelAttribute Config config) {
        configService.updateConfig(id, config);
        return "redirect:/configs";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfig(@PathVariable Long id) {
        configService.deleteConfig(id);
        return "redirect:/configs";
    }
}
