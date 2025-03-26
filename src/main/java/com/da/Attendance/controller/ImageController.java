package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.Image.SaveImageResponse;
import com.da.Attendance.model.Image;
import com.da.Attendance.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/image")
@CrossOrigin("*")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> addImage(@RequestParam MultipartFile file){
        try{
            Image image = imageService.addImage(file);
            return ResponseEntity.ok(new ApiResponse("Save image success", image));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Save image failed " + e.getMessage(), null)
            );
        }
    }
    @PostMapping("/upload-multi")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files){
        try{
            List<SaveImageResponse> list = imageService.listImage(files);
            return ResponseEntity.ok(new ApiResponse("Save images success", list));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Save images failed " + e.getMessage(), null)
            );
        }
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<?> download(@PathVariable String name){
        try{
            Image image = imageService.getImageByFileName(name);
            if(image == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                new ApiResponse("Image not found ", null)
                        );
            }
            ByteArrayResource resource = new ByteArrayResource(image.getImage());

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                    new ApiResponse("Save image failed " + e.getMessage(), null)
            );
        }
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@RequestParam MultipartFile file, @PathVariable String id){
        try{
            SaveImageResponse saveImageResponse = imageService.updateImage(file, id);
            return ResponseEntity.ok(new ApiResponse("Update image success", saveImageResponse));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Update image failed " + e.getMessage(), null)
            );
        }
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id){
        try{
            imageService.deleteById(id);
            return ResponseEntity.ok(new ApiResponse("Delete image success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Delete image failed " + e.getMessage(), null)
            );
        }
    }
}
