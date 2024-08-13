package ru.neoflex.calculator.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "loan")
public class LoanProperties {
    private BigDecimal baseRate;
    private BigDecimal insuranceDiscount;
    private BigDecimal salaryDiscount;
    private BigDecimal monthlyInsuranceRate;
    private BigDecimal dependentAmount;
    private BigDecimal maritalStatusSingle;
    private BigDecimal maritalStatusWidowed;
}

