package com.example.hrplus.model;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="employees_table")
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matricule;

    @Column(name="nom")
    private String nom;



    @Column(name="prenom")
    private String prenom;

    @Column(name="date_entree")
    private LocalDate dateEntree;

    @Column(name="departement")
    private String departement;

    @Column(name="numero_sec_soc")
    private long numeroSecSoc;

    @Column(name="salaire_base_jour")
    private double salaireBaseJour;

    @Column(name="nb_absence")
    private int nbAbsence = 0;

    @Column(name="image_file_name")
    private String imageFileName;

    public Employee(String nom, String prenom, LocalDate dateEntree, String departement, long numeroSecSoc, double salaireBaseJour, String imageFileName) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateEntree = dateEntree;
        this.departement = departement;
        this.numeroSecSoc = numeroSecSoc;
        this.salaireBaseJour = salaireBaseJour;
        this.imageFileName = imageFileName;
    }
}


    