package com.savelms.api.user.controller.dto;

import com.savelms.core.user.AttendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeAttendStatusRequest {

    AttendStatus attendStatus;
}
