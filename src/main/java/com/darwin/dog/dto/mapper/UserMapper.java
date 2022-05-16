package com.darwin.dog.dto.mapper;

import com.darwin.dog.dto.out.UserOutDto;
import com.darwin.dog.po.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User userOutDtoToUser(UserOutDto userOutDto);

    UserOutDto userToUserOutDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserOutDto(UserOutDto userOutDto, @MappingTarget User user);
}
