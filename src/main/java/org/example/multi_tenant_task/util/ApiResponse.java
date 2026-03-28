package org.example.multi_tenant_task.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "message", "data"})
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }


    private ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(String message, T data) {

        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {

        return new ApiResponse<>(true, "Request successful", data);
    }

    public static <T> ApiResponse<T> fail(String message, T data) {

        return new ApiResponse<>(false, message, data);
    }

    public static <T> ApiResponse<T> fail(String message) {

        return new ApiResponse<>(false, message);
    }

}
