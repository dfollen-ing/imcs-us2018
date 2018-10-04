package com.ing.imc.resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class Response<P> {
    private String errorMessage;
    private P payload;

    Response(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    Response(P payload) {
        this.payload = payload;
    }
}
