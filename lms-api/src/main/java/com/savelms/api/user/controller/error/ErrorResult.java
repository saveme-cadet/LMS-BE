package com.savelms.api.user.controller.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResult {

    private String message;
}
