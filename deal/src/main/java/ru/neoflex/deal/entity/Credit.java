package ru.neoflex.deal.entity;

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
import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credit")
@Builder
@Getter
@Setter
@ToString
public class Credit {

    @Id
    @UuidGenerator
    @Column(name = "credit_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "term", nullable = false)
    private int term;

    @Column(name = "monthly_payment", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(name = "rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(name = "psk", nullable = false, precision = 10, scale = 2)
    private BigDecimal psk;

    @Column(name = "payment_schedule", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Column(name = "insurance_enabled", nullable = false)
    private boolean insuranceEnabled;

    @Column(name = "salary_client", nullable = false)
    private boolean salaryClient;

    @Column(name = "credit_status", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
