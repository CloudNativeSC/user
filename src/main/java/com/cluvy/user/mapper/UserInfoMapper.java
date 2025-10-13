package com.cluvy.user.mapper;

import com.cluvy.user.dto.UserInfoResponse;
import com.cluvy.user.dto.UserUpdateRequest;
import com.cluvy.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserInfoMapper {

    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    // 필드명이 같으면 자동 매핑됨.
    // 다르면 @Mapping으로 수동 지정 가능.
    @Mapping(target = "gender", expression = "java(user.getGender() != null ? user.getGender().name() : null)")
    @Mapping(target = "authType", expression = "java(user.getAuthType() != null ? user.getAuthType().name() : null)")
    @Mapping(target = "status", expression = "java(user.getStatus() != null ? user.getStatus().name() : null)")
    @Mapping(target = "timezone", expression = "java(user.getTimezone() != null ? user.getTimezone().name() : null)")
    @Mapping(target = "subscriptionType", expression = "java(user.getSubscriptionType() != null ? user.getSubscriptionType().name() : null)")
    UserInfoResponse toUserInfoResponse(User user);

    @InheritConfiguration
    void update(UserUpdateRequest dto, @MappingTarget User entity);
}
