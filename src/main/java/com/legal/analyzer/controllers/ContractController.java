package com.legal.analyzer.controllers;

import com.legal.analyzer.models.Contract;
import com.legal.analyzer.models.User;
import com.legal.analyzer.services.ContractService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    // ✅ Web UI - show all contracts or search results
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String home(@RequestParam(required = false) String keyword, Model model) {
        List<Contract> contracts;
        if (keyword != null && !keyword.isEmpty()) {
            contracts = contractService.searchContractByTitle(keyword);
        } else {
            contracts = contractService.getAllContracts();
        }
        model.addAttribute("contracts", contracts);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // ✅ REST API - return all contracts as JSON
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/api")
    @ResponseBody
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    // ✅ Web UI - save contract with file, expiry date, and dummy user
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveContract(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expiryDate
    ) {
        // Dummy user (replace later with authenticated user)
        User dummyUser = new User();
        dummyUser.setId(1L);
        dummyUser.setUsername("khushal");

        Contract contract = new Contract();
        contract.setTitle(title);
        contract.setContent(content);
        contract.setExpiryDate(expiryDate);
        contract.setFileName(file != null ? file.getOriginalFilename() : null);
        contract.setUser(dummyUser);

        contractService.saveContract(contract, file); // updated method handles file save
        return "redirect:/contracts";
    }

    // ✅ REST API - save contract from JSON
    @PostMapping(path = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Contract saveContractApi(@RequestBody Contract contract) {
        return contractService.saveContract(contract);
    }

    // ✅ View single contract
    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String viewContract(@PathVariable Long id, Model model) {
        Contract contract = contractService.getContractById(id).orElse(null);
        model.addAttribute("contract", contract);
        return "view";
    }

    // ✅ Update contract
    @PostMapping("/update/{id}")
    public String updateContract(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam String content) {
        contractService.updateContract(id, title, content);
        return "redirect:/contracts/" + id;
    }

    // ✅ Delete contract
    @PostMapping("/delete/{id}")
    public String deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return "redirect:/contracts";
    }

    // ✅ Export contract to PDF (with attached file content)
    @GetMapping("/{id}/pdf")
    public void exportContractToPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Contract contract = contractService.getContractById(id).orElse(null);
        if (contract == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contract not found");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=contract_" + id + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font bodyFont = new Font(Font.HELVETICA, 12);

        document.add(new Paragraph("Contract Details", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Title: " + contract.getTitle(), bodyFont));
        document.add(new Paragraph("Content: " + contract.getContent(), bodyFont));
        document.add(new Paragraph("Created At: " + contract.getCreatedAt(), bodyFont));
        if (contract.getExpiryDate() != null) {
            document.add(new Paragraph("Expiry Date: " + contract.getExpiryDate(), bodyFont));
        }

        if (contract.getFileName() != null) {
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Attached File: " + contract.getFileName(), bodyFont));

            String basePath = "C:/Users/Khushal Sharma/Desktop/SmartSecureLegalContractAnalyzer/saved-details/";
            File file = new File(basePath + contract.getFileName());
            if (file.exists()) {
                String fileName = file.getName().toLowerCase();

                if (fileName.endsWith(".txt")) {
                    document.add(new Paragraph("Attached File Content:", titleFont));
                    document.add(new Paragraph(" "));

                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        document.add(new Paragraph(line, bodyFont));
                    }
                    reader.close();
                } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
                    document.add(new Paragraph("Attached Image:", titleFont));
                    document.add(new Paragraph(" "));

                    Image image = Image.getInstance(file.getAbsolutePath());
                    image.scaleToFit(400, 400);
                    image.setAlignment(Image.ALIGN_CENTER);
                    document.add(image);
                } else {
                    document.add(new Paragraph("⚠️ Cannot display this file type in PDF.", bodyFont));
                }
            } else {
                document.add(new Paragraph("⚠️ Attached file not found.", bodyFont));
            }
        }

        document.close();
    }
}