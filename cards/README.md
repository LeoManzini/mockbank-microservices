## Application setup

### 1. application.yml

For the application to work, you need to set up the `application.yml` file. You can find a sample configuration in the `src/main/resources/application.yml` file. Copy it to your own `application.yml` file and modify the values as needed.
To work with database and JPA, you need to set up the following properties in your `application.yml` file:

```yaml
# Embedded server configuration
server:
  port: 8080
spring:
  application:
    name: accounts
  # H2 configuration
  datasource:
    url: jdbc:h2:mem:accounts
    driverClassName: org.h2.Driver
    username: sa
    password: ''
  h2:
    console:
      enabled: true
  # JPA configuration
  jpa:
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
        format_sql: true
        ddl:
          auto: update
```

### 2. H2

H2 is an open-source lightweight Java database that can be embedded in Java applications. It is often used for development and testing purposes due to its simplicity and ease of use.

H2 supports both in-memory and file-based databases, making it a versatile choice for various scenarios.

### 3. Spring boot handling database

With the database configured on the `application.yml` file, you can run the application, and it will create an instance for H2 database automatically, setting the database as defined on `application.yml`. 
In this scenario, you can access the H2 console at `http://localhost:8080/h2-console` with the credentials also defined in the `application.yml` file.

When running the application, the spring boot identifies if exists in the `src/main/resources` folder a file called `schema.sql` the spring boot will execute the script according to ddl-auto property.
The same happens for the `data.sql` file, which will be executed after the `schema.sql` file.

### 4. MappedSuperclass

The `@MappedSuperclass` annotation is used in JPA (Java Persistence API) to indicate that a class is a superclass for entities but should not be treated as an entity itself. 
It allows you to define common properties and behavior that can be inherited by multiple entity classes without creating a separate table for the superclass.

Useful for audit fields, such as `createdAt`, `updatedAt`, `createdBy`, and `updatedBy`. Or for common fields that are shared across multiple entities, such as `id`, `createdAt`, and `updatedAt`.

Even for common heritage, such as `Person` and `Employee`, where `Person` is a superclass that contains common properties like `name`, `email`, and `phoneNumber`, while `Employee` is a subclass that adds specific properties like `employeeId` and `department`.

```java
@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
// We don't use @Data from Lombok because it generates equals and hashCode methods based on all fields, 
// which is not desired in this case.
public class BaseEntity {

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, updatable = false)
    private String createdBy;
    // This field is not insertable, is only updated
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    @Column(insertable = false)
    private String updatedBy;
}
```

### 5. Generic response DTOs

For mantain a standard in the project, and keeps returning a clean output for the clients, we build two default responses, to return when is not a resource being requested.

```java
// SUCCESS 
@Data  
@AllArgsConstructor  
public class ResponseDto {  
  
    private String statusCode;  
    private String statusMessage;  
}

// ERROR
@Data  
@AllArgsConstructor  
public class ErrorResponseDto {  
  
    private String apiPath;  
    private HttpStatus errorCode;  
    private String errorMessage;  
    private LocalDateTime errorTime;  
}
```

This works fine because of `ResponseBuilder` class, that returns the success scenarios:

```java
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
```

And the `GlobalExceptionHandler` that returns the error scenarios:

```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // This one is an implementation to handle with DTOs validation
    // Returns the map with fieldName: Message from the validators on DTO
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorsList = ex.getBindingResult().getAllErrors();

        validationErrorsList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
```

### 6. Business logic implementation

To reduce code coupling, all the services in this microsservice, implements an interface. To make the Dependency Inversion from SOLID acronym came true.

```java
public interface AccountsServiceInterface {

    /**
     * @param customerDto - CustomerDto Object
     * @return - Created account Number
     */
    Long createAccount(CustomerDto customerDto);
    // ...
}

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsServiceInterface {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public Long createAccount(CustomerDto customerDto) {
        // ...
    }
    //...
}
```

### 7. Auditing metadata

JPA has some implementations to make it easy for the audit data, we have to be sure of the activation of this resource

```java
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}
```

Passing our bean that have our auditor obtain implemented:

```java
@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
	    // TODO: Get the logged user from Spring Security
        return Optional.of("ACCOUNT_MS");
    }
}
```

By activating the audit, we already can use the `@CreatedDate` and `@LastModifiedDate`, but not the ones depending on the logged user, because of this we build the `AuditorAwareImpl`.

And so, enable in our audit entity:

```java
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}
```

The columns created by the audit, marked as not null and not updatable, to be filled only on insertion. And the updated by being nullable and not insertable, being only inserted on update operations.
