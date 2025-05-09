package br.com.leomanzini.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold customer information"
)
public class CustomerDto {

    @Schema(example = "Jhon Doe")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    private String name;

    @Schema(example = "jhondoe@mail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(example = "1234567890")
    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "(^$|[0-9]{10,15})", message = "Mobile number should be valid")
    private String mobileNumber;
}
