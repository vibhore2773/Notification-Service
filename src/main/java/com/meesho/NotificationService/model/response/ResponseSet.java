package com.meesho.NotificationService.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSet {

    private ErrorClass error;
    private DataClass data;
    public ResponseSet(){}
    public ResponseSet(ErrorClass error) {
        this.error = error;
    }
    public ResponseSet(DataClass data) {
        this.data = data;
    }

    public ErrorClass getError() {
        return error;
    }

    public void setError(ErrorClass error) {
        this.error = error;
    }

    public DataClass getData() {
        return data;
    }

    public void setData(DataClass data) {
        this.data = data;
    }
}
