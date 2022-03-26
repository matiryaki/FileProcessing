package com.etstur.FileProcessing.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.etstur.FileProcessing.model.FileInformation;
import com.etstur.FileProcessing.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository repository;

    public FileInformation store(MultipartFile file, String path) throws IOException {
        return repository.save(FileInformation.builder()
                        .name(file.getOriginalFilename())
                        .type(FilenameUtils.getExtension(file.getOriginalFilename()))
                        .file(file.getBytes())
                        .size(file.getSize())
                        .path(path)
                .build());
    }

    public FileInformation update(MultipartFile file, String path, long id) throws IOException {
        return repository.save(FileInformation.builder()
                        .id(id)
                        .name(file.getOriginalFilename())
                        .type(FilenameUtils.getExtension(file.getOriginalFilename()))
                        .file(file.getBytes())
                        .size(file.getSize())
                        .path(path)
                .build());
    }

    public List<FileInformation> findAllFiles() {
        return repository.findAll();
    }

    public Optional<FileInformation> findById(Long id) {
        return repository.findById(id);
    }


    public void delete(FileInformation file) {
        repository.delete(file);
    }

}
