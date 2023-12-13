package com.mediscan.backend.Components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
    private String OTP;
    private String email;
}
