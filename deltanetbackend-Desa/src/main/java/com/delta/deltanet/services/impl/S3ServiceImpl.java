package com.delta.deltanet.services.impl;

import com.delta.deltanet.models.dao.IAdmFolderDao;
import com.delta.deltanet.models.entity.AdmFolder;
import com.delta.deltanet.models.entity.RepArchivoFormato;
import com.delta.deltanet.models.service.IRepArchivoFormatoService;
import com.delta.deltanet.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class S3ServiceImpl implements IS3Service {

    private final S3Client s3Client;
    
    @Autowired
    private IAdmFolderDao admFolderDAO;
    @Autowired
    private IRepArchivoFormatoService archivoFormatoService;

    @Autowired
    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    
    @Override
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile, String path, String nameFile) {
        String bucketName = path.trim().split("/")[0];
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + path + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            String fileOriginal = multipartFile.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(fileOriginal);
            Optional<RepArchivoFormato> fext = archivoFormatoService.buscaExt(extension);
            if (fext.isEmpty()) {
                response.put("message","La extension del archivo [" + extension + "] no se encuentra registrada.");
                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String fileName = (path + "/" + nameFile).replace(bucketName+"/","");
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
            
            response.put("message","Archivo [" + fileOriginal + "] subido satisfactoriamente");
            response.put("url",getObjectUrl(bucketName,fileName));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Error al subir archivo: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public ResponseEntity<?> downloadFile(String path, String filename) {
        String bucketName = path.trim().split("/")[0];
        String localFilePath;

        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + path + "] no existe");
            valida=false;
        }

        String objectKey = path+"/"+filename;
        cnt=existFile(objectKey);
        if(cnt==-1){
            response.put("message","El archivo [" + objectKey + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            objectKey = objectKey.replace(bucketName+"/","");
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            ResponseBytes<GetObjectResponse> responseResponseBytes = s3Client.getObjectAsBytes(objectRequest);

            byte[] data = responseResponseBytes.asByteArray();

            java.io.File myFile = new java.io.File("C:/Users/Public/Downloads/"+filename);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            os.close();
            response.put("message","Se descargo el archivo satisfactoriamente.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            response.put("message","Ocurrio un fallo en la descarga del archivo.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public ResponseEntity<?> copyFile(String filename, String pathSource, String pathTarget) {
        String bucketNameSrc = pathSource.trim().split("/")[0];
        String bucketNameTrg = pathTarget.trim().split("/")[0];
        Long tam = 0L;

        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(pathSource);
        if(cnt==-1){
            response.put("message","El bucket source no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + pathSource + "] no existe");
            valida=false;
        }
        cnt = existFolder(pathTarget);
        if(cnt==-1){
            response.put("message","El bucket target no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + pathTarget + "] no existe");
            valida=false;
        }
        cnt=existFile(pathSource+"/"+filename);
        if(cnt==0){
            response.put("message","El archivo a copiar [" + pathSource + "/" + filename + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            String rutaSrc = pathSource.replace(bucketNameSrc+"/","") + "/" + filename;
            String rutaTrg = pathTarget.replace(bucketNameTrg+"/","") + "/" + filename;
            if(pathSource==bucketNameSrc) rutaSrc = filename;
            
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(bucketNameSrc)
                    .sourceKey(rutaSrc)
                    .destinationBucket(bucketNameTrg)
                    .destinationKey(rutaTrg)
                    .acl("public-read")
                    .build();
            s3Client.copyObject(copyObjectRequest);
            
            response.put("message","Archivo [" + rutaTrg + "] copiado satisfactoriamente");
            response.put("url",getObjectUrl(bucketNameTrg,rutaTrg));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("message","Ocurrio un fallo en la copia del archivo.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public ResponseEntity<?> moveFile(String filename, String pathSource, String pathTarget) {
        String bucketNameSrc = pathSource.trim().split("/")[0];
        String bucketNameTrg = pathTarget.trim().split("/")[0];
        
        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(pathSource);
        if(cnt==-1){
            response.put("message","El bucket source no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + pathSource + "] no existe");
            valida=false;
        }
        cnt = existFolder(pathTarget);
        if(cnt==-1){
            response.put("message","El bucket target no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + pathTarget + "] no existe");
            valida=false;
        }
        cnt=existFile(pathSource+"/"+filename);
        if(cnt==0){
            response.put("message","El archivo a mover [" + pathSource + "/" + filename + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            String rutaSrc = pathSource.replace(bucketNameSrc+"/","") + "/" + filename;
            String rutaTrg = pathTarget.replace(bucketNameTrg+"/","") + "/" + filename;
            if(pathSource==bucketNameSrc) rutaSrc = filename;
            if(pathTarget==bucketNameTrg) rutaTrg = filename;
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(bucketNameSrc)
                    .sourceKey(rutaSrc)
                    .destinationBucket(bucketNameTrg)
                    .destinationKey(rutaTrg)
                    .acl("public-read")
                    .build();

            s3Client.copyObject(copyObjectRequest);
            
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketNameSrc)
                    .key(rutaSrc)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            response.put("message","Archivo [" + rutaTrg + "] se movio satisfactoriamente");
            response.put("url",getObjectUrl(bucketNameTrg,rutaTrg));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("message","Ocurrio un fallo al mover el archivo.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public ResponseEntity<?> deleteFile(String filename, String pathSource) {
        String bucketNameSrc = pathSource.trim().split("/")[0];

        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(pathSource);
        if(cnt==-1){
            response.put("message","El bucket source no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + pathSource + "] no existe");
            valida=false;
        }
        cnt=existFile(pathSource+"/"+filename);
        if(cnt==0){
            response.put("message","El archivo a eliminar [" + pathSource + "/" + filename + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            String rutaSrc = pathSource.replace(bucketNameSrc+"/","") + "/" + filename;

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketNameSrc)
                    .key(rutaSrc)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            response.put("message","Archivo [" + rutaSrc + "] se elimino satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("message","Ocurrio un fallo al eliminar el archivo.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public ResponseEntity<?> renameFile(String path, String filenameOri, String filenameNew) {
        String bucketName = path.trim().split("/")[0];

        Map<String, Object> response = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket source no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH [" + path + "] no existe");
            valida=false;
        }
        cnt=existFile(path + "/" + filenameOri);
        if(cnt==0){
            response.put("message","El archivo ["+ path+"/"+filenameOri +"] no existe,.");
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        try {
            String rutaSrc = path.replace(bucketName+"/","") + "/" + filenameOri;
            String rutaTrg = path.replace(bucketName+"/","") + "/" + filenameNew;
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(rutaSrc)
                    .destinationBucket(bucketName)
                    .destinationKey(rutaTrg)
                    .acl("public-read")
                    .build();

            s3Client.copyObject(copyObjectRequest);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(rutaSrc)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            response.put("message","Archivo [" + rutaSrc + "] se renombro a [" + rutaTrg + "] satisfactoriamente");
            response.put("url",getObjectUrl(bucketName,rutaTrg));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("message","Ocurrio un fallo al renombrar el archivo.");
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private static int countOccurrences(String str, char ch) {
        return str.length() - str.replace(String.valueOf(ch), "").length();
    }

    private int existFile(String folder){
        String bucketName = folder.trim().split("/")[0];
        String ruta = folder.replace(bucketName + "/", "");
        int nroFiles = 0;
        try {
            ListObjectsRequest request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(ruta).build();

            ListObjectsResponse response = s3Client.listObjects(request);
            List<S3Object> objects = response.contents();
            for (S3Object object : objects) {
                String item = object.key();
                nroFiles++;
            }
            return nroFiles;
        } catch (Exception e){
            return -1;
        }
    }

    @Override
    public int existFolder(String folder){
        String bucketName = folder;
        String ruta= "";
        int nroFolders = 0;
        int nroFiles = 0;
        try {
            String[] items = folder.split("/");
            if (items.length > 1) {
                bucketName = items[0];
                ruta = folder.replace(bucketName + "/", "");
            }
            if (!ruta.endsWith("/") && !ruta.isEmpty()) ruta = ruta + "/";
            if(items.length==1 && bucketName.endsWith("/")) bucketName=bucketName.replace("/","");


            ListObjectsRequest request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(ruta).build();

            ListObjectsResponse response = s3Client.listObjects(request);
            List<S3Object> objects = response.contents();
            ListIterator<S3Object> listIterator = objects.listIterator();
            int numInicio = countOccurrences(ruta,'/');
            while (listIterator.hasNext()) {
                S3Object object = listIterator.next();
                String item = object.key();
                int x = countOccurrences(item,'/') - numInicio;
                if(item.endsWith("/")){
                    if (x==1 || x==0) {
                        nroFolders++;
                    }
                } else {
                    if(x==0) {
                        nroFiles++;
                    }
                }
            }
            return nroFolders+nroFiles;
        } catch (Exception e){
            return -1;
        }
    }

    private String getObjectUrl(String Bucketname, String key){
        return String.format("https://%s.s3.amazonaws.com/%s",Bucketname,key);
    }

    @Override
    public ResponseEntity<?> readFolder(String folder) {
        Map<String, Object> response = new HashMap<>();
        int items = existFolder(folder);
        boolean soloBucket = countOccurrences(folder, '/') == 0;

        if(!soloBucket && countOccurrences(folder, '/') == 1 && folder.endsWith("/")) soloBucket = true;

        boolean valida = true;
        if(items==-1){
            response.put("message","No existe el bucket en la ruta [" + folder + "]");
            valida = false;
        }
        if(items==0){
            response.put("message","No existe la carpeta [" + folder + "]");
            valida = false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        else {
            if (!soloBucket) items--;
            response.put("message","La ruta [" + folder + "] existe");
            if(items==0) response.put("contenido","la carpeta se encuentra vacía");
            else response.put("contenido","[" + items + "] objetos (archivos y subcarpetas)");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> moveFolder(String directory, String folderOld, String folderNew) {
        Map<String, Object> rpta = new HashMap<>();
        String bucketNameSrc = folderOld.trim().split("/")[0];
        String bucketNameTrg = folderNew.trim().split("/")[0];
        boolean valida = true;

        int cnt = existFolder(folderOld);
        if(cnt==-1){
            rpta.put("message","El bucket origen no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH origen [" + folderOld + "] no existe");
            valida=false;
        }
        cnt = existFolder(folderNew);
        if(cnt==-1){
            rpta.put("message","El bucket destino no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH destino [" + folderNew + "] no existe");
            valida=false;
        }

        String Old = folderOld + "/" + directory;

        cnt = existFolder(Old);
        if(cnt==0){
            rpta.put("message","La ruta origen [" + Old + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(rpta, HttpStatus.UNPROCESSABLE_ENTITY);

        Old = Old.replace(bucketNameSrc+"/","");

        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketNameSrc)
                .prefix(Old).build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();
        ListIterator<S3Object> listIterator = objects.listIterator();
        String original = "";
        String folder_src = folderOld.replace(bucketNameSrc+"/","");
        String folder_trg = folderNew.replace(bucketNameTrg+"/","");
        folder_trg = folder_trg.replace(bucketNameTrg,"");
        String nuevo = "";

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            original = object.key();
            if (folder_trg.equals("")) nuevo = original.replace(folder_src + "/", folder_trg);
            else nuevo = original.replace(folder_src, folder_trg);

            CopyObjectRequest copia = CopyObjectRequest.builder()
                    .sourceBucket(bucketNameSrc)
                    .sourceKey(original)
                    .destinationBucket(bucketNameTrg)
                    .destinationKey(nuevo)
                    .build();

            s3Client.copyObject(copia);

            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketNameSrc)
                    .key(original)
                    .build();

            s3Client.deleteObject(delete);
        }

        rpta.put("message","Se movio la carpeta [" + directory + "] a [" + folderNew + "]");
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> openFolder(String path, String Directory) {
        Map<String, Object> response = new HashMap<>();
        String bucketName = path.trim().split("/")[0];
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta del PATH inicial [" + path + "] no existe");
            valida=false;
        }

        cnt=existFolder(path + "/" + Directory);
        if(cnt==0){
            response.put("message","El directorio [" + path + "/" + Directory + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        else {
            String ruta = (path + "/" + Directory + "/").trim().replace(bucketName+"/","");
            ListObjectsRequest request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(ruta).build();

            ListObjectsResponse listado = s3Client.listObjects(request);
            List<S3Object> objects = listado.contents();
            ListIterator<S3Object> listIterator = objects.listIterator();
            int numInicio = countOccurrences(ruta,'/');
            List<directorio> lstFinal = new ArrayList<>();
            while (listIterator.hasNext()) {
                directorio reg = new directorio();
                S3Object object = listIterator.next();
                String item = object.key();
                int x = countOccurrences(item,'/') - numInicio;
                if(item.endsWith("/")){
                    if (x==1 || x==0) {
                        reg.setElemento(item);
                        reg.setTipo("folder");
                        reg.setTam(0L);
                    }
                } else {
                    if(x==0) {
                        reg.setElemento(item);
                        reg.setTipo("file");
                        reg.setTam(object.size());
                    }
                }
                lstFinal.add(reg);
            }

            response.put("contenido",lstFinal);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> updateFolder(String path, String folderOld, String folderNew) {
        Map<String, Object> rpta = new HashMap<>();
        String bucketName = path.trim().split("/")[0];
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            rpta.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH inicial [" + path + "] no existe");
            valida=false;
        }

        String Old = path + "/" + folderOld;

        cnt = existFolder(Old);
        if(cnt==0){
            rpta.put("message","La ruta origen [" + Old + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(rpta, HttpStatus.UNPROCESSABLE_ENTITY);

        Old = Old.replace(bucketName+"/","");
        
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(Old).build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();
        ListIterator<S3Object> listIterator = objects.listIterator();
        String original = "";
        String nuevo = "";
        
        String[] items = folderNew.split("/");
        folderNew = items[items.length-1];
        
        //folderNew = folderNew.replace(bucketName+"/", "");
        //folderNew += "/";

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            original = object.key();
            nuevo = original.replace(folderOld, folderNew);

            CopyObjectRequest copia = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(original)
                    .destinationBucket(bucketName)
                    .destinationKey(nuevo)
                    .build();

            s3Client.copyObject(copia);

            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(original)
                    .build();

            s3Client.deleteObject(delete);
        }

        rpta.put("message","Se actualizo la carpeta a [" + folderNew + "]");
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFolder(String path, String folder) {
        Map<String, Object> rpta = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            rpta.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH inicial [" + path + "] no existe");
            valida=false;
        }

        String Old = path + "/" + folder;

        cnt = existFolder(Old);
        if(cnt==0){
            rpta.put("message","La ruta origen [" + Old + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(rpta, HttpStatus.UNPROCESSABLE_ENTITY);

        String bucketName = path.trim().split("/")[0];
        Old = Old.replace(bucketName+"/","");

        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(Old).build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();

        ListIterator<S3Object> listIterator = objects.listIterator();
        String original = "";

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            original = object.key();

            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(original)
                    .build();

            s3Client.deleteObject(delete);
        }

        rpta.put("message", "Se elimino la carpeta [" + path + "/" + folder + "]");
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createFolder(String path, String directory) {
        Map<String, Object> response = new HashMap<>();
        String bucketName = path;
        String folder = directory;
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta [" + path + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        String[] items = path.trim().split("/");
        if(items.length>1) {
            bucketName = items[0];
            folder = path.replace(bucketName+"/","") + "/" + directory;
        }

        if(!directory.endsWith("/")) folder = folder + "/";

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folder)
                    .build();
            s3Client.putObject(request, RequestBody.empty());
            
            //--Crea archivo readme.me
            String filename = "readme.me";
            MultipartFile multipartFile = new MockMultipartFile("sourceFile.tmp","Carpeta creada desde el API Service S3Cliente Backend Delta.".getBytes());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folder + filename)
                    .build();
            s3Client.putObject(putObjectRequest,RequestBody.fromBytes(multipartFile.getBytes()));

            response.put("message", "Carpeta: [" + folder + "] creada satisfactoriamente en el Bucket [" + bucketName + "]");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al crear la carpeta: " + folder + "en el Bucket [" + bucketName + "]");
            response.put("error ", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

	@Override
	public ResponseEntity<?> listado(String folder) {
		Map<String, Object> retorno = new HashMap<>();
		String bucketName = folder;
		String ruta= "";
		String[] steps;
		Date fecha;
		String dato;
		Instant instant;
		List<listContent> lista = new ArrayList<>();
		try {
			String[] items = folder.split("/");
			if (items.length > 1) {
				bucketName = items[0];
				ruta = folder.replace(bucketName + "/", "");
			}
			if (!ruta.endsWith("/") && !ruta.isEmpty()) ruta = ruta + "/";
			if(items.length==1 && bucketName.endsWith("/")) bucketName=bucketName.replace("/","");
			
			ListObjectsRequest request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(ruta).build();
			
			listContent item;
			
			ListObjectsResponse response = s3Client.listObjects(request);
			List<S3Object> objects = response.contents();
			ListIterator<S3Object> listIterator = objects.listIterator();
			int numInicio = countOccurrences(ruta,'/');
			while (listIterator.hasNext()) {
				S3Object object = listIterator.next();
				String filename = object.key();
				int x = countOccurrences(filename,'/') - numInicio;
				
				if(filename.endsWith("/")){
                    if (x==1 || x==0) {
                    	filename = filename.replace(ruta, "");
                    	if(filename.trim().length()==0) filename = "..";
                    	dato = object.lastModified().toString();
                    	instant = Instant.parse(dato);
                    	fecha = Date.from(instant);
                    	
                    	instant = fecha.toInstant();
                    	DateTimeFormatter formatter = DateTimeFormatter
                    			.ofPattern("yyyy-MM-dd HH:mm:ss")
                    			.withZone(ZoneId.systemDefault());
                    	
                    	item = new listContent();
        				item.setIsFolder(true);
        				item.setName(filename);
        				item.setSize(redondea(object.size().doubleValue()/1024));
        				item.setUltMod(formatter.format(instant));
        				lista.add(item);
                    }
                } else {
                    if(x==0) {
                    	filename = filename.replace(ruta, "");
                    	dato = object.lastModified().toString();
                    	instant = Instant.parse(dato);
                    	fecha = Date.from(instant);
                    	
                    	instant = fecha.toInstant();
                    	DateTimeFormatter formatter = DateTimeFormatter
                    			.ofPattern("yyyy-MM-dd HH:mm:ss")
                    			.withZone(ZoneId.systemDefault());
                    	item = new listContent();
        				item.setIsFolder(false);
        				item.setName(filename);
        				item.setSize(redondea(object.size().doubleValue()/1024));
        				item.setUltMod(formatter.format(instant));
        				lista.add(item);
                    }
                }
				
				//ruta = ruta.replace(bucketName + "/", "");
				
				
			}
			retorno.put("data", lista);
			return new ResponseEntity<>(retorno, HttpStatus.OK);
		} catch (Exception e) {
			retorno.put("message", "Error inesperado en la funcion index");
			retorno.put("debug", e.getMessage());
			return new ResponseEntity<>(retorno, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@Override
	public ResponseEntity<?> lstcarpetas(String bucket) {
		Map<String, Object> retorno = new HashMap<>();
		String bucketName = bucket;
		String ruta= "";
		String[] steps;
		List<listContent> lista = new ArrayList<>();
		try {
			String[] items = bucket.split("/");
			if (items.length > 1) {
				bucketName = items[0];
				ruta = bucket.replace(bucketName + "/", "");
			}
			if (!ruta.endsWith("/") && !ruta.isEmpty()) ruta = ruta + "/";
			if(items.length==1 && bucketName.endsWith("/")) bucketName=bucketName.replace("/","");
			
			ListObjectsRequest request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(ruta).build();
			
			listContent item;
			
			ListObjectsResponse response = s3Client.listObjects(request);
			List<S3Object> objects = response.contents();
			ListIterator<S3Object> listIterator = objects.listIterator();
			int numInicio = countOccurrences(ruta,'/');
			while (listIterator.hasNext()) {
				S3Object object = listIterator.next();
				String filename = object.key();
				int x = countOccurrences(filename,'/') - numInicio;
				
				if(filename.endsWith("/")){
                    if (x==1 || x==0) {
                    	Double tam = object.size().doubleValue()/1024;
                    	item = new listContent();
        				item.setIsFolder(true);
        				item.setName(filename);
        				item.setSize(redondea(object.size().doubleValue()/1024));
        				lista.add(item);
                    }
                }
			}
			retorno.put("listado", lista);
			return new ResponseEntity<>(retorno, HttpStatus.OK);
		} catch (Exception e) {
			retorno.put("message", "Error inesperado en la funcion index");
			retorno.put("debug", e.getMessage());
			return new ResponseEntity<>(retorno, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	private static double redondea(double x) {
        double y = (int)(x * 100D);
        double z = (int)Math.ceil(x * 1000D);
        if(z % 10D > 5D)
            y = (y + 1.0D) / 100D;
        else
            y /= 100D;
        return y;
    }
	
	private void saveRegistro(String carpeta, String fullpath, String usuario) {
		AdmFolder reg = new AdmFolder();
		reg.setIdpadre(0L);
		
		String[] items = fullpath.split("/");
		if(items.length>1) {
			String ruta = "";
			for(int i=0;i<items.length-1;i++) {
				ruta = ruta + items[i] + "/";
			}
			ruta = ruta.substring(0,ruta.length()-1);
			String folder = items[items.length-1];
			AdmFolder existe = admFolderDAO.buscaFolder(ruta, folder);
			if (existe == null) reg.setIdpadre(0L); else reg.setIdpadre(existe.getId());
		}
		
		reg.setNomfolder(carpeta);
		reg.setPathfolder(fullpath);
		reg.setEstado(1);
		reg.setCreuser(usuario);
		reg.setCredate(new Date());
		admFolderDAO.save(reg);
	}
	
	private void delRegistro(String carpeta, String fullpath, String usuario) {
		AdmFolder reg = admFolderDAO.buscaFolder(fullpath, carpeta);
		if (reg != null) {
			reg.setEstado(0);
			admFolderDAO.save(reg);
		}
		
		List<AdmFolder> carpetas = admFolderDAO.listaFolderes(fullpath+"/"+carpeta);
		for(AdmFolder item: carpetas) {
			item.setEstado(0);
			admFolderDAO.save(item);
		}
	}
	
	private void chgRegistro(String carpeta, String fullpath, String nuevo, String usuario) {
		if (carpeta.endsWith("/")) carpeta = carpeta.substring(0,carpeta.length()-1);
		if (nuevo.endsWith("/")) nuevo = nuevo.substring(0,nuevo.length()-1);
		
		AdmFolder reg = admFolderDAO.buscaFolder(fullpath, carpeta);
		if (reg != null) {
			reg.setNomfolder(nuevo);
			reg.setUpduser(usuario);
			reg.setUpddate(new Date());
			admFolderDAO.save(reg);
		}
		
		List<AdmFolder> carpetas = admFolderDAO.listaFolderes(fullpath+"/"+carpeta);
		for(AdmFolder item: carpetas) {
			String dir = item.getPathfolder().replace(carpeta, nuevo);
			item.setPathfolder(dir);
			reg.setUpduser(usuario);
			reg.setUpddate(new Date());
			admFolderDAO.save(item);
		}
	}
	
	private Long obtieneTam(String bucketname, String ruta) {
		Long tam = 0L;
		ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketname)
                .prefix(ruta).build();

        ListObjectsResponse lstFile = s3Client.listObjects(request);
        List<S3Object> objects = lstFile.contents();
        for (S3Object object : objects) {
            tam = object.size();
        }
        return tam;
	}

	@Override
	public ResponseEntity<?> createFolder(String path, String directory, String usuario) {
		Map<String, Object> response = new HashMap<>();
        String bucketName = path;
        String folder = directory;
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            response.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            response.put("message","La ruta [" + path + "] no existe");
            valida=false;
        }
        if (!valida) return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

        String[] items = path.trim().split("/");
        if(items.length>1) {
            bucketName = items[0];
            folder = path.replace(bucketName+"/","") + "/" + directory;
        }

        if(!directory.endsWith("/")) folder = folder + "/";

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folder)
                    .build();
            s3Client.putObject(request, RequestBody.empty());
            
            //--Crea registro en la DB
            saveRegistro(directory, path, usuario);

            //--Crea archivo readme.me
            String filename = "readme.me";
            MultipartFile multipartFile = new MockMultipartFile("sourceFile.tmp","Carpeta creada desde el API Service S3Cliente Backend Delta.".getBytes());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folder + filename)
                    .build();
            s3Client.putObject(putObjectRequest,RequestBody.fromBytes(multipartFile.getBytes()));

            response.put("message", "Carpeta: [" + folder + "] creada satisfactoriamente en el Bucket [" + bucketName + "]");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message","Error al crear la carpeta: " + folder + "en el Bucket [" + bucketName + "]");
            response.put("error ", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
	}

	@Override
	public ResponseEntity<?> deleteFolder(String path, String folder, String usuario) {
		Map<String, Object> rpta = new HashMap<>();
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            rpta.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH inicial [" + path + "] no existe");
            valida=false;
        }

        String Old = path + "/" + folder;

        cnt = existFolder(Old);
        if(cnt==0){
            rpta.put("message","La ruta origen [" + Old + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(rpta, HttpStatus.UNPROCESSABLE_ENTITY);

        String bucketName = path.trim().split("/")[0];
        Old = Old.replace(bucketName+"/","");

        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(Old).build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();

        ListIterator<S3Object> listIterator = objects.listIterator();
        String original = "";

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            original = object.key();

            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(original)
                    .build();

            s3Client.deleteObject(delete);
        }
        
        //Cambia estado en bd
        folder = folder.replace("/", "");
        delRegistro(folder, path, usuario);

        rpta.put("message", "Se elimino la carpeta [" + path + "/" + folder + "]");
        return new ResponseEntity<>(rpta, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateFolder(String path, String folderOld, String folderNew, String usuario) {
		Map<String, Object> rpta = new HashMap<>();
        String bucketName = path.trim().split("/")[0];
        boolean valida = true;

        int cnt = existFolder(path);
        if(cnt==-1){
            rpta.put("message","El bucket no existe");
            valida=false;
        }
        if(cnt==0){
            rpta.put("message","La ruta del PATH inicial [" + path + "] no existe");
            valida=false;
        }

        String Old = path + "/" + folderOld;

        cnt = existFolder(Old);
        if(cnt==0){
            rpta.put("message","La ruta origen [" + Old + "] no existe");
            valida=false;
        }

        if (!valida) return new ResponseEntity<>(rpta, HttpStatus.UNPROCESSABLE_ENTITY);

        Old = Old.replace(bucketName+"/","");
        
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(Old).build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();
        ListIterator<S3Object> listIterator = objects.listIterator();
        String original = "";
        String nuevo = "";
        
        String[] items = folderNew.split("/");
        folderNew = items[items.length-1] + "/";
        
        /*folderNew = folderNew.replace(bucketName+"/", "");
        if (!folderNew.endsWith("/")) folderNew += "/";*/

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            original = object.key();
            nuevo = original.replace(folderOld, folderNew);

            CopyObjectRequest copia = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(original)
                    .destinationBucket(bucketName)
                    .destinationKey(nuevo)
                    .build();

            s3Client.copyObject(copia);

            DeleteObjectRequest delete = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(original)
                    .build();

            s3Client.deleteObject(delete);
        }
        
        chgRegistro(folderOld,path,folderNew,usuario);

        rpta.put("message","Se actualizo la carpeta a [" + folderNew + "]");
        return new ResponseEntity<>(rpta, HttpStatus.OK);
	}
}

class directorio{
    private String elemento;
    private String tipo;
    private Long tam;

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getTam() {
        return tam;
    }

    public void setTam(Long tam) {
        this.tam = tam;
    }
}

class listContent {
	private String name;
	private Boolean isFolder;
	private Double size;
	private String ultMod;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsFolder() {
		return isFolder;
	}
	public void setIsFolder(Boolean isFolder) {
		this.isFolder = isFolder;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public String getUltMod() {
		return ultMod;
	}
	public void setUltMod(String ultMod) {
		this.ultMod = ultMod;
	}
	
}