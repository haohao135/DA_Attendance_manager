package com.da.Attendance.service;

import com.da.Attendance.dto.response.Image.SaveImageResponse;
import com.da.Attendance.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ImageService {
    Image getImageById(String id);
    Image getImageByFileName(String name);
    Image addImage(MultipartFile file);
    List<SaveImageResponse> listImage(List<MultipartFile> images);
    void deleteById(String id);
    SaveImageResponse updateImage(MultipartFile image, String id) throws IOException, SQLException;
}
