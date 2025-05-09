package br.com.leomanzini.cards.utils;

import br.com.leomanzini.cards.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static ResponseEntity<ResponseDto> created(String message, Long id) {
        String returnMessage = String.format(message, id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto("201", returnMessage));
    }

    public static ResponseEntity<ResponseDto> ok(String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", message));
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
