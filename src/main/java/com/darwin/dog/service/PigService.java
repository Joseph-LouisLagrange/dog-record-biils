package com.darwin.dog.service;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface PigService {
    @PreAuthorize("hasPermission('*','Pig','create')")
    void create();

    @PreAuthorize("hasPermission(#ID,'Pig','delete')")
    void delete(long ID);

    @PreAuthorize("hasPermission(#ID,'Pig','read')")
    void getByID(Long ID);

    @PreAuthorize("hasPermission(#IDs,'Pig','update')")
    void update(long[] IDs);

    @PreAuthorize("hasPermission('*','Pig','delete')")
    void deleteAll();

    @PreAuthorize("hasPermission('*','Pig','read')")
    void getAll();
}
