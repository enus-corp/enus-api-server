package com.enus.newsletter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.enus.newsletter.db.entity.UserEntity;
import com.enus.newsletter.model.dto.UserDTO;

@Mapper
public interface IUserMapper {
    @Mapping(source="id", target="userId")
    UserDTO toDTO(UserEntity entity);
}
