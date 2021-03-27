package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    private Long credentialId;
    private String url;
    private String username;
    private String password;
    private String key;
    private Long userId;

}
