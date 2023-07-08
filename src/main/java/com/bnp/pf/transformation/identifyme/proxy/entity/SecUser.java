package com.bnp.pf.transformation.identifyme.proxy.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class SecUser {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
}
