package com.etstur.FileProcessing.dto;

import com.etstur.FileProcessing.model.FileInformation;


import lombok.Data;


@Data
public class FileDto {

	private long id;
    private String name;
    private String type;
    private String path;
    private long size;

    public FileDto(FileInformation file) {
        this.id = file.getId();
        this.name = file.getName();
        this.type = file.getType();
        this.path = file.getPath();
        this.size = file.getSize();
    }
}
