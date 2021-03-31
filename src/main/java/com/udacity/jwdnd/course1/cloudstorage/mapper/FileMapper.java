package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * from FILES where fileid = #{fileId}")
    File findFileById(Long fileId);

    @Select("SELECT * from FILES where userid = #{userId}")
    List<File> findFilesByUserId(Long userId);

    @Insert("INSERT into FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int createFile(File file);

    @Delete("DELETE from FILES where fileid = #{fileId}")
    int deleteFileById(Long fileId);

}
