package com.example.hrplus.controller;

import com.example.hrplus.model.Employee;
import com.example.hrplus.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/", ""})
public class HomeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping()
    public String showHomePage(Model model){
        List<Employee> employees = employeeRepository.findAll();
        long finance = employeeRepository.countEmployeeByDepartement("Finance");
        long it = employeeRepository.countEmployeeByDepartement("IT");
        long rh = employeeRepository.countEmployeeByDepartement("RH");
        long rd = employeeRepository.countEmployeeByDepartement("R&D");
        model.addAttribute("employees",employees);
        model.addAttribute("finance",finance);
        model.addAttribute("it",it);
        model.addAttribute("rh",rh);
        model.addAttribute("rd",rd);
        return "home";
    }

}
