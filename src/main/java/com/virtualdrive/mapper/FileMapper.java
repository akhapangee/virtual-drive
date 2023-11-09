package com.virtualdrive.mapper;

import com.virtualdrive.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteById(Integer fileId);

    @Select("SELECT * FROM FILES where userid = #{userId}")
    List<File> findAll(Integer userId);

    @Select("SELECT * FROM FILES where fileId = #{id}")
    File findById(Integer id);
    @Select("SELECT * FROM FILES where filename = #{filename} AND userid = #{userId}")
    File findByFileName(String filename, Integer userId);
}
