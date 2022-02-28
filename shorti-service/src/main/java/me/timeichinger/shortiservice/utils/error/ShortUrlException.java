package me.timeichinger.shortiservice.utils.error;

import lombok.Getter;

@Getter
public class ShortUrlException extends Exception{

    private int statusCode;

    public ShortUrlException(ErrorCode errorCode) {
        super(errorCode.getErrorDescription());
        this.statusCode = errorCode.getStatusCode();
    }

}
