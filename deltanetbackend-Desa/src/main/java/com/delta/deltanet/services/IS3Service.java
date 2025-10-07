package com.delta.deltanet.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IS3Service {
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile, String path, String nameFile);
    public ResponseEntity<?> downloadFile(String path, String filename);
    public ResponseEntity<?> copyFile(String filename, String pathSource, String pathTarget);
    public ResponseEntity<?> moveFile(String filename, String pathSource, String pathTarget);
    public ResponseEntity<?> deleteFile(String filename, String pathSource);
    public ResponseEntity<?> renameFile(String path, String filenameOri, String filenameNew);


    public ResponseEntity<?> updateFolder(String path,String folderOld, String folderNew);
    public ResponseEntity<?> updateFolder(String path,String folderOld, String folderNew, String usuario);
    public ResponseEntity<?> deleteFolder(String path, String folder);
    public ResponseEntity<?> deleteFolder(String path, String folder, String usuario);

    public ResponseEntity<?> createFolder(String path, String directory);
    public ResponseEntity<?> createFolder(String path, String directory, String usuario);
    public ResponseEntity<?> readFolder(String path);
    public ResponseEntity<?> moveFolder(String directory,String folderOld, String folderNew);
    public ResponseEntity<?> openFolder(String path, String Directory);
    public int existFolder(String folder);
    
    public ResponseEntity<?> listado(String folder);
    public ResponseEntity<?> lstcarpetas(String bucketname);
}
