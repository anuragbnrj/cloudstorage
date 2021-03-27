package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private Long fileId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private Long userId;
    private byte[] fileData;

}
