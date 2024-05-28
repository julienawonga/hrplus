package com.example.hrplus.controller;

import com.example.hrplus.model.Employee;
import com.example.hrplus.repository.EmployeeRepository;
import com.example.hrplus.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping({"", "/"})
    public String showSalaryPage(Model model){
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "salary/salary";
    }


    @GetMapping("/report")
    public void generateReport(HttpServletResponse response, @RequestParam Long matricule) throws JRException, IOException {
        byte[] reportBytes = employeeService.generateReport(matricule);

        // Set the response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=employeeReport.pdf");

        // Write the report bytes to the response output stream
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(reportBytes);
        outputStream.flush();
    }

    @GetMapping("/reports")
    public void downloadEmployeesReports(HttpServletResponse response) throws Exception {
        // Get all employees
        List<Employee> employees = employeeRepository.findAll();

        // Create a temporary directory to store individual employee reports
        File tempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "employee_reports");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        // Create a PDFMergerUtility to concatenate PDFs
        PDFMergerUtility pdfMerger = new PDFMergerUtility();

        // Iterate over each employee and generate their report
        for (Employee employee : employees) {
            // Generate the employee's report
            byte[] employeeReport = employeeService.generateReport(employee.getMatricule());

            // Save the employee's report to a temporary PDF file
            File tempFile = new File(tempDir, "report_" + employee.getMatricule() + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(employeeReport);
            }

            // Add the temporary PDF file to the PDFMergerUtility
            pdfMerger.addSource(tempFile);
        }

        // Create a ByteArrayOutputStream to hold the concatenated PDF bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Merge all temporary PDFs into a single PDF
        pdfMerger.setDestinationStream(outputStream);
        pdfMerger.mergeDocuments(null);

        // Set response headers for the PDF download
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=employeesReport.pdf");

        // Write the concatenated PDF bytes to the response output stream
        response.getOutputStream().write(outputStream.toByteArray());

        // Clean up: Delete temporary directory and files
        deleteTempDir(tempDir);
    }

    // Helper method to delete temporary directory and files
    private void deleteTempDir(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteTempDir(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }


}
