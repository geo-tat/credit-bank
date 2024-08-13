package ru.neoflex.deal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.PassportDto;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Client {

    @Id
    @UuidGenerator
    @Column(name = "client_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private int dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "passport_data", columnDefinition = "jsonb")
    private PassportDto passportDtoData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "employment_data", columnDefinition = "jsonb")
    private EmploymentDto employmentData;

    @Column(name = "account_number")
    private String accountNumber;
}