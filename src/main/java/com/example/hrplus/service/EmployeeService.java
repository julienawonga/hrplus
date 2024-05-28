package com.example.hrplus.service;

import com.example.hrplus.model.Employee;
import com.example.hrplus.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;


    // Simplified version of the generateReport method
    public byte[] generateReport(long id) throws JRException {
        // Retrieve the employee by ID
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        // Check if the employee exists
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            // Load the report template
            InputStream reportTemplate = getClass().getResourceAsStream("/reports/employe.jrxml");

            // Compile the report template
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Convert the employee to a JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(employee));

            Period period = Period.between(employee.getDateEntree(), LocalDate.now());
            String anciennete = period.getYears() + " an(s) et " + period.getMonths() + " moi(s)";
            Integer jt = 26 - employee.getNbAbsence();

            Double sb = employee.getSalaireBaseJour() * jt;
            Double r1 = sb + 500;
            Double ir = r1 * 0.2;
            Double r2 = r1 - ir;
            Double r3 = r2 - 200;
            Double rnet = r3 - 300;


            HashMap<String, Object> params = new HashMap<>();
            params.put("pd", LocalDate.now());
            params.put("pf", LocalDate.now());
            params.put("pp", LocalDate.now());
            params.put("pm", "virement");
            params.put("jt", jt);
            params.put("anciennete", anciennete);

            params.put("sb", Math.floor(sb * 100) / 100);
            params.put("ir", Math.floor(ir * 100) / 100);
            params.put("r1", Math.floor(r1 * 100) / 100);
            params.put("r2", Math.floor(r2 * 100) / 100);
            params.put("r3", Math.floor(r3 * 100) / 100);
            params.put("rnet", Math.floor(rnet * 100) / 100);

            // Generate the report using the compiled template and data source
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Export the report to a byte array (PDF format)
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } else {
            // Handle case where employee with given ID does not exist
            System.out.println("Employee with id " + id + " does not exist");
            return null;
        }
    }



}
