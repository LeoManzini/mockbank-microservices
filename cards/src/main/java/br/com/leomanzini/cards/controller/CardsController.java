package br.com.leomanzini.cards.controller;

import br.com.leomanzini.cards.dto.CardsDto;
import br.com.leomanzini.cards.dto.ResponseDto;
import br.com.leomanzini.cards.service.CardsServiceInterface;
import br.com.leomanzini.cards.utils.CardsConstants;
import br.com.leomanzini.cards.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "ManziniBank Cards",
        description = "API for cards resources"
)
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardsController {

    private final CardsServiceInterface cardsService;

    @Operation(
            summary = "Create card",
            description = "Creates a mocked card based on a mocked customer mobile number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@Parameter(
            name = "mobileNumber",
            description = "Mocked mobile number to create a mocked card",
            required = true,
            example = "1234567890")
            @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        Long cardId = cardsService.createCard(mobileNumber);
        return ResponseBuilder.created(CardsConstants.MESSAGE_201, cardId);
    }

    @Operation(
            summary = "Fetch card details",
            description = "Fetch a specific card detail"
    )
    @GetMapping
    public ResponseEntity<CardsDto> fetchCardDetails(@Parameter(
            name = "mobileNumber",
            description = "Mobile number to search for mock card",
            required = true,
            example = "1234567890")@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") String mobileNumber) {
        CardsDto cardsDto = cardsService.fetchCard(mobileNumber);
        return ResponseEntity.ok(cardsDto);
    }
}
