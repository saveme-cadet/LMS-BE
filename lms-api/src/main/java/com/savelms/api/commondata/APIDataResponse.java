package com.savelms.api.commondata;

import com.savelms.api.constant.ErrorCode;
import com.savelms.api.exception.error.dto.APIErrorResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIDataResponse<T> extends APIErrorResponse {

    private final T data;

    private APIDataResponse (T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    private APIDataResponse () {
        super(false, ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage());
        this.data = null;
    }

    public static <T> APIDataResponse <T> of (T data) {
        return new APIDataResponse<>(data);
    }

    public static <T> APIDataResponse <T> empty() {
        return new APIDataResponse<>(null);
    }

    public static <T> APIDataResponse <T> badRequest() {
        return new APIDataResponse<>();
    }
}
