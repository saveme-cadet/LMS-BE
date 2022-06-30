package com.savelms.core.user.domain.repository.dto;

import com.savelms.core.SortTypeEnum;
import com.savelms.core.exception.QueryStringFormatException;
import com.savelms.core.user.UserSortFieldEnum;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSortRuleDto {

    private static final Set<String> fields = Set.of(UserSortFieldEnum.NICKNAME.getValue(), UserSortFieldEnum.CREATEDDATE.getValue());
    private static final Set<String> sortTypes = Set.of(SortTypeEnum.ASC.getValue(), SortTypeEnum.DESC.getValue());

    private String fieldName;
    private String sortType;

    public static UserSortRuleDto toUserSortRoleDto(String sortRule) {

        String[] split = sortRule.split(":");
        String field = split[0];
        String sortType = split[1];

        if (split.length != 2 ||
            !fields.contains(field) || !sortTypes.contains(sortType)) {
            throw new QueryStringFormatException("sort의 형식이 잘못되었습니다.");
        }


        return UserSortRuleDto.builder()
            .fieldName(field)
            .sortType(sortType)
            .build();
    }
}
