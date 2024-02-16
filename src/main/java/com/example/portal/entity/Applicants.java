package com.example.portal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="job_applicants")
public class Applicants {

    @Id

    @Column(nullable=false)
    private String applicants_email;
    @Column(nullable=false)
    private int job_id;
    public Applicants(String applicant_email, int job_id) {
        this.applicants_email = applicant_email;
        this.job_id = job_id;
    }
}
