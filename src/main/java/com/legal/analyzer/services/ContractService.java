package com.legal.analyzer.services;

import com.legal.analyzer.models.Contract;
import com.legal.analyzer.models.User;
import com.legal.analyzer.repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    private final String SAVE_DIR = "C:\\Users\\Khushal Sharma\\Desktop\\SmartSecureLegalContractAnalyzer\\saved-details";

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }

    // Save full contract object + file
    public Contract saveContract(Contract contract, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            contract.setFileName(file.getOriginalFilename());

            try {
                File dir = new File(SAVE_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File savedFile = new File(dir, file.getOriginalFilename());
                FileOutputStream fos = new FileOutputStream(savedFile);
                fos.write(file.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace(); // log error
            }
        }

        return contractRepository.save(contract);
    }

    public void updateContract(Long id, String title, String content) {
        contractRepository.findById(id).ifPresent(contract -> {
            contract.setTitle(title);
            contract.setContent(content);
            contractRepository.save(contract);
        });
    }

    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    public List<Contract> searchContractByTitle(String keyword) {
        return contractRepository.findByTitleContainingIgnoreCase(keyword);
    }

    //  Fetch contracts for a specific user
    public List<Contract> getContractsByUser(User user) {
        return contractRepository.findByUser(user);
    }
}