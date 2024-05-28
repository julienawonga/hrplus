package com.example.hrplus.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto {
    @NotEmpty(message = "Le nom est obligatoire")
    private String nom;
    @NotEmpty(message = "Le prenom est obligatoire")
    private String prenom;
    @PastOrPresent(message = "La date doit etre du pasée ou present")
    @NotNull(message = "La date est obligatoire")
    private LocalDate dateEntree;
    @NotEmpty(message = "Le departement est obligatoire")
    private String departement;

    private long numeroSecSoc;
    @Min(value = 0, message = "Le salaire de base par jour doit être supérieur ou égal à 0")
    private double salaireBaseJour;
    @Min(value=0, message = "Le nombre d absence ne peut pas etre inferrieure a 0")
    @Max(value=26, message = "Le nombre d absence ne peut pas depasser 26")
    private int nbAbsence;
    private MultipartFile imageFile;
}
