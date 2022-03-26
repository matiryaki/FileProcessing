package com.etstur.FileProcessing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@AllArgsConstructor
@Data
@Builder
@Table(name = "file")
public class FileInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", unique = true, nullable = false)
    private String name;
	@Column(name = "size", nullable = false)
    private long size;
	@Column(name = "type", nullable = false)
    private String type;
	@Column(name = "path", nullable = false)
    private String path;
    @Lob
    private byte[] file;
}
