package com.etstur.FileProcessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etstur.FileProcessing.model.FileInformation;

@Repository
public interface FileRepository extends JpaRepository<FileInformation,Long>{

}
