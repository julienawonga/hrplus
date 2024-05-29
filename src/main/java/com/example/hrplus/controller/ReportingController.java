package com.example.hrplus.controller;

import com.example.hrplus.model.Employee;
import com.example.hrplus.repository.EmployeeRepository;
import com.example.hrplus.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reporting")
public class ReportingController {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;


    @GetMapping({"", "/"})
    String getReporting(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "reporting/reporting";
    }

}
