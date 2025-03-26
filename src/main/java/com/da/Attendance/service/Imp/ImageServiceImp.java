package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.response.Image.SaveImageResponse;
import com.da.Attendance.model.Image;
import com.da.Attendance.repository.ImageRepository;
import com.da.Attendance.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImp implements ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image getImageById(String id) {
        Optional<Image> image = imageRepository.findById(id);
        if(image.isEmpty()){
            throw new RuntimeException("image not found");
        }
        return image.get();
    }

    @Override
    public Image getImageByFileName(String name) {
        return imageRepository.findByFileName(name);
    }

    @Override
    public Image addImage(MultipartFile file) {
        Image image = new Image();
        try {
            String fileName = UUID.randomUUID().toString();
            image.setFileName(fileName);
            image.setContentType(file.getContentType());
            image.setImage(file.getBytes());
            String buildDownloadUrl = "/api/v1/image/download/";
            String downloadUrl = buildDownloadUrl+image.getFileName();
            image.setUrl(downloadUrl);
            imageRepository.save(image);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return image;
    }

    @Override
    public List<SaveImageResponse> listImage(List<MultipartFile> images) {
        List<SaveImageResponse> list = new ArrayList<>();
        for(MultipartFile i : images){
            try {
                Image image = new Image();
                String fileName = UUID.randomUUID().toString();
                image.setFileName(fileName);
                image.setContentType(i.getContentType());
                image.setImage(i.getBytes());
                String buildDownloadUrl = "/api/v1/image/download/";
                String downloadUrl = buildDownloadUrl+image.getFileName();
                image.setUrl(downloadUrl);
                imageRepository.save(image);
                SaveImageResponse saveImageResponse = new SaveImageResponse(
                        image.getId(), image.getFileName(), image.getUrl()
                );
                list.add(saveImageResponse);
            } catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return list;
    }

    @Override
    public void deleteById(String id) {
        imageRepository.deleteById(id);
    }

    @Override
    public SaveImageResponse updateImage(MultipartFile image, String id){
        Image image1 = getImageById(id);
        SaveImageResponse saveImageResponse = new SaveImageResponse();
        try{
            String fileName = UUID.randomUUID().toString();
            image1.setFileName(fileName);
            image1.setContentType(image.getContentType());
            image1.setImage(image.getBytes());
            String buildDownloadUrl = "/api/v1/image/download/";
            String downloadUrl = buildDownloadUrl+image1.getFileName();
            image1.setUrl(downloadUrl);

            imageRepository.save(image1);
            saveImageResponse.setId(image1.getId());
            saveImageResponse.setName(image1.getFileName());
            saveImageResponse.setUrl(image1.getUrl());
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return saveImageResponse;
    }
}
