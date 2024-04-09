package org.icmss.icmsscarservice.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BusinessException extends RuntimeException{

    private HttpStatus status;

    public BusinessException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

}
