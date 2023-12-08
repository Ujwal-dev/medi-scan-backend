package com.mediscan.backend.Components;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    private ObjectId id;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
