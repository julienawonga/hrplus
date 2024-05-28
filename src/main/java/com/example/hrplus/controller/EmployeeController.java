package com.example.hrplus.controller;

import com.example.hrplus.dto.EmployeeDto;
import com.example.hrplus.model.Employee;
import com.example.hrplus.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping({"", "/"})
    public String showEmployeeList(Model model){
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "employees/index";
    }



    @GetMapping("/add")
    public String showAddPage(Model model){
        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto",employeeDto);
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "employees/addEmployee";
    }

    @PostMapping("/add")
    public String CreateEmployee(Model model, @Valid @ModelAttribute EmployeeDto employeeDto, BindingResult bindingResult){

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);

        if (employeeDto.getImageFile().isEmpty()){
            bindingResult.addError(new FieldError("employeeDto", "imageFile", "L'image est obligatoire"));
        }

        if (bindingResult.hasErrors()){
            return "employees/addEmployee";
        }

//        Save the image
        MultipartFile image = employeeDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }


        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        Employee employee = new Employee();
        employee.setNom(employeeDto.getNom());
        employee.setPrenom(employeeDto.getPrenom());
        employee.setDateEntree(employeeDto.getDateEntree());
        employee.setDepartement(employeeDto.getDepartement());
        employee.setNumeroSecSoc(employeeDto.getNumeroSecSoc());
        employee.setSalaireBaseJour(employeeDto.getSalaireBaseJour());
        employee.setNbAbsence(0);
        employee.setImageFileName(storageFileName);

        employeeRepository.save(employee);



        return "redirect:/employees/";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam long matricule){

        Optional<Employee> employee = employeeRepository.findByMatricule(matricule);

        if (employee.isPresent()){
            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("employees",employees);
            model.addAttribute("employee", employee.get());

            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setNom(employee.get().getNom());
            employeeDto.setPrenom(employee.get().getPrenom());
            employeeDto.setDateEntree(employee.get().getDateEntree());
            employeeDto.setDepartement(employee.get().getDepartement());
            employeeDto.setNumeroSecSoc(employee.get().getNumeroSecSoc());
            employeeDto.setSalaireBaseJour(employee.get().getSalaireBaseJour());
            employeeDto.setNbAbsence(employee.get().getNbAbsence());

            model.addAttribute("employeeDto", employeeDto);
            


            return "employees/editEmployee";
        } else {
            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("employees",employees);
            return "redirect:/employees";
        }

    }

    @GetMapping("/profile")
    public String showProfile(Model model, @RequestParam long matricule){

        Optional<Employee> employee = employeeRepository.findByMatricule(matricule);

        if (employee.isPresent()){
            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("employees",employees);
            model.addAttribute("employee", employee.get());

            return "employees/profileEmployee";
        } else {
            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("employees",employees);
            return "redirect:/employees";
        }

    }

    @PostMapping("/edit")
    public String editEmployee(Model model, @RequestParam long matricule, @Valid @ModelAttribute EmployeeDto employeeDto, BindingResult bindingResult){

          Optional<Employee> employee = employeeRepository.findByMatricule(matricule);

          if ( employee.isPresent() ){
              List<Employee> employees = employeeRepository.findAll();
              model.addAttribute("employees",employees);
                model.addAttribute("employee", employee.get());

                if(bindingResult.hasErrors()){
                    return "employees/editEmployee";
                }

                if(!employeeDto.getImageFile().isEmpty()){
                    // delete the image
                    String uploadDir = "public/images/";
                    Path oldImagePath = Paths.get(uploadDir + employee.get().getImageFileName());

                    try {
                        Files.delete(oldImagePath);
                    } catch (IOException e) {
                        System.out.println("Exception: " + e.getMessage() );
                    }

                    // save new image file
                    MultipartFile image = employeeDto.getImageFile();
                    Date createdAt = new Date();
                    String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                    try (InputStream inputStream = image.getInputStream()){
                        Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println("exception: " + e.getMessage());
                    }

                    employee.get().setImageFileName(storageFileName);

                }

              employee.get().setNom(employeeDto.getNom());
              employee.get().setPrenom(employeeDto.getPrenom());
              employee.get().setDateEntree(employeeDto.getDateEntree());
              employee.get().setDepartement(employeeDto.getDepartement());
              employee.get().setNumeroSecSoc(employeeDto.getNumeroSecSoc());
              employee.get().setSalaireBaseJour(employeeDto.getSalaireBaseJour());
              employee.get().setNbAbsence(employeeDto.getNbAbsence());

              employeeRepository.save(employee.get());

          } else {
              System.out.println("employee so not exist");
          }

        return "redirect:/employees";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam long matricule) {

        Optional<Employee> employee = employeeRepository.findByMatricule(matricule);

        if ( employee.isPresent() ) {

            // delete Image
            Path imagePath = Paths.get("public/images/" + employee.get().getImageFileName());

            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                System.out.println("Exception: " + e.getMessage());
            }

            // delete employee
            employeeRepository.delete(employee.get());

        }

        return "redirect:/employees";
    }


}
