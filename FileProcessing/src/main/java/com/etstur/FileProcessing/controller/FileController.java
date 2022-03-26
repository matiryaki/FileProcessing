package com.etstur.FileProcessing.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.etstur.FileProcessing.dto.FileDto;
import com.etstur.FileProcessing.model.FileInformation;
import com.etstur.FileProcessing.service.FileService;

import lombok.RequiredArgsConstructor;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")

public class FileController {

	private FileService service;
	
	
	@Value("${SAVE_DIRECTORY}")
    public String PATH;
	
	@GetMapping("/findall")
    public ResponseEntity<List<FileDto>> findAllFiles() {
        return ResponseEntity.ok().body(service.findAllFiles().stream().map(FileDto::new).collect(Collectors.toList()));
    }
	
	@GetMapping("/findbyid")
    public ResponseEntity<FileDto> findFile(@RequestParam long id) {
        FileInformation file = service.findById(id).get();
        return ResponseEntity.ok().body(new FileDto(file));
    }
	
    @PostMapping(value ="/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(@RequestBody MultipartFile file) {
        String filePath = PATH  + file.getOriginalFilename();
        try(OutputStream out = new FileOutputStream(new File(filePath))) {

            if(isValidExtension(file)) {
                out.write(file.getBytes());
                FileInformation fileInform = service.store(file, filePath);
                return ResponseEntity.status(HttpStatus.OK).body(new FileDto(fileInform));
            } else {
                throw new Exception("Error occured at " + filePath);
            }

        } catch (Exception e) {
            throw new RuntimeException( "File could not be uploaded " + file.getOriginalFilename());
        }
    }
	
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> updateFile(@PathVariable(value = "id") long id, @RequestParam MultipartFile file) throws IOException {
        String filePath = PATH + file.getOriginalFilename();
        FileInformation fileInform = service.findById(id).get();
        File existingFile = new File(fileInform.getPath());
        existingFile.delete();
        try(OutputStream out = new FileOutputStream(new File(filePath))) {
            if(isValidExtension(file)) {
                out.write(file.getBytes());
                FileInformation newFileInform = service.update(file, filePath, id);
                return ResponseEntity.status(HttpStatus.OK).body(new FileDto(newFileInform));
            } else {
                throw new Exception("Error occured at " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("File could not be updated " + file.getOriginalFilename());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable(value = "id") long id) throws IOException {
    	FileInformation fileInform = service.findById(id).get();
        File file = new File(fileInform.getPath());
        file.delete();
        service.delete(fileInform);
        return ResponseEntity.ok().body("File is successfully deleted: " + fileInform.getName());
    }
    
    private boolean isValidExtension(MultipartFile file) {
        List<String> extensions = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());
        return extensions.contains(extension);
    }
}
