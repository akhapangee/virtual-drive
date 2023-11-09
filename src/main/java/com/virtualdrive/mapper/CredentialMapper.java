package com.virtualdrive.mapper;

import com.virtualdrive.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url,username,key,password,userid) VALUES(#{url},#{username},#{key},#{password},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Select("SELECT * FROM CREDENTIALS where credentialId = #{id}")
    Credential findById(Integer id);

    @Select("SELECT * FROM CREDENTIALS where userid = #{userId}")
    List<Credential> getAllCredentials(Integer userId);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, key =#{key},password=#{password} WHERE credentialId=#{credentialId}")
    void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    void delete(Integer credentialId);
}
