package com.mediscan.backend.Components;

import java.util.Date;
import java.lang.Boolean;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {
    private ObjectId id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String verificationCode;
    private Boolean enabled;
    private Date createdTime;

}
