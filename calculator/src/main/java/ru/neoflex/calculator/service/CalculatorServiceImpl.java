package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.exception.LoanDeniedException;
import ru.neoflex.calculator.properties.LoanProperties;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
@Slf4j
public class CalculatorServiceImpl implements CalculatorService {
    private final LoanProperties properties;

    @Override
    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto dto) {
        log.info("Начался расчёт возможных условий кредита");
        List<LoanOfferDto> offers = Stream.of(false, true)
                .flatMap(insurance -> Stream.of(false, true)
                        .map(salaryClient -> createLoanOffer(dto, insurance, salaryClient)))
                .sorted(Comparator.comparing(LoanOfferDto::getRate))
                .collect(Collectors.toList());
        log.info("Кредитные предложения составлены");
        log.info("Тело ответа: {}", offers);
        return offers;
    }

    @Override
    public CreditDto calculateCreditParameters(ScoringDataDto dto) {
        log.info("Выполняется полный расчет параметров кредита");
        deniedValidation(dto);
        BigDecimal baseRate = properties.getBaseRate();
        BigDecimal currentRate = scoring(dto, baseRate);
        BigDecimal monthlyPayment = createMonthlyPayment(
                currentRate,
                dto.getAmount(),
                dto.getTerm(),
                dto.getIsInsuranceEnabled());
        BigDecimal psk = monthlyPayment.multiply(BigDecimal.valueOf(dto.getTerm()));
        List<PaymentScheduleElementDto> paymentSchedule = getPaymentsSchedule(dto.getTerm(),
                monthlyPayment,
                dto.getAmount(),
                currentRate,
                dto.getIsInsuranceEnabled());

        return CreditDto.builder()
                .amount(dto.getAmount())
                .rate(currentRate)
                .term(dto.getTerm())
                .monthlyPayment(monthlyPayment)
                .psk(psk)
                .isSalaryClient(dto.getIsSalaryClient())
                .isInsuranceEnabled(dto.getIsInsuranceEnabled())
                .paymentSchedule(paymentSchedule)
                .build();
    }

    private List<PaymentScheduleElementDto> getPaymentsSchedule(Integer term,
                                                                BigDecimal monthlyPayment,
                                                                BigDecimal loanAmount,
                                                                BigDecimal currentRate,
                                                                Boolean isInsuranceEnable) {
        BigDecimal monthlyRate = currentRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal remainingDebt = loanAmount;
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt
                    .multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment
                    .subtract(interestPayment)
                    .subtract(createInsuranceCost(loanAmount, isInsuranceEnable, term))     //  вычитаем страховку
                    .setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);
            if (i == term && remainingDebt.compareTo(BigDecimal.ZERO) != 0.00) {
                remainingDebt = remainingDebt.setScale(0, RoundingMode.HALF_UP);
            }
            schedule.add(PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(LocalDate.now().plusMonths(i))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build());
            log.info("Составлен график платежей");
        }
        return schedule;
    }

    private BigDecimal scoring(ScoringDataDto dto, BigDecimal baseRate) {
        if (dto.getIsSalaryClient()) {
            log.info("Условие: зарплатный клиент");
            baseRate = baseRate.subtract(properties.getSalaryDiscount());
        }
        if (dto.getIsInsuranceEnabled()) {
            log.info("Условие: страховка");
            baseRate = baseRate.subtract(properties.getInsuranceDiscount());
        }
        if (dto.getDependentAmount() > 1) {
            log.info("Условие: иждивенцы");
            baseRate = baseRate.subtract(properties.getDependentAmount());
        }
        if (dto.getMaritalStatus().equals(MaritalStatus.SINGLE)) {
            log.info("Условие: семейное положение(холост)");
            baseRate = baseRate.add(properties.getMaritalStatusSingle());
        }
        if (dto.getMaritalStatus().equals(MaritalStatus.WIDOWED)) {
            log.info("Условие: семейное положение(вдова/ец)");
            baseRate = baseRate.add(properties.getMaritalStatusWidowed());
        }
        log.info("Произведен скоринг клиента");
        return baseRate;
    }

    private void deniedValidation(ScoringDataDto dto) {
        int age = Period.between(dto.getBirthdate(), LocalDate.now()).getYears();
        if (dto.getEmployment().getEmploymentStatus().equals(EmploymentStatus.UNEMPLOYED)) {
            log.info("Не пройдено условие по рабочему статусу");
            throw new LoanDeniedException("Loan denied. Customer is unemployed");
        }
        if (age < 21 || age > 60) {
            log.info("Не пройдено условие по возрастным ограничениям.");
            throw new LoanDeniedException("Loan denied. Customer's age is invalid");
        }
        if (dto.getAmount().compareTo(dto.getEmployment().getSalary().multiply(BigDecimal.valueOf(25))) >= 0) {
            log.info("Не пройдено условие по зарплате.");
            throw new LoanDeniedException("Loan denied. Customer's salary is not enough");
        }
        if (dto.getEmployment().getWorkExperienceTotal() < 24) {
            log.info("Не пройдено условие общего стажа работы.");
            throw new LoanDeniedException("Loan denied. Customer's total work experience less then 24 months");
        }
        if (dto.getEmployment().getWorkExperienceCurrent() < 6) {
            log.info("Не пройдено условие текущего стажа работы.");
            throw new LoanDeniedException("Loan denied. Customer's current work experience less then 6 months");
        }
        log.info("Пройдена проверка на отказ в кредите");
    }

    private LoanOfferDto createLoanOffer(LoanStatementRequestDto dto, boolean insurance, boolean salaryClient) {
        BigDecimal rate = properties.getBaseRate();    // базовая ставка


        if (insurance) {
            rate = rate.subtract(properties.getInsuranceDiscount());
        }

        if (salaryClient) {
            rate = rate.subtract(properties.getSalaryDiscount());
        }
        BigDecimal monthlyPayment = createMonthlyPayment(rate, dto.getAmount(), dto.getTerm(), insurance);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(dto.getTerm()), MathContext.DECIMAL128);
        return LoanOfferDto.builder()
                .term(dto.getTerm())
                .isSalaryClient(salaryClient)
                .isInsuranceEnabled(insurance)
                .statementId(UUID.randomUUID())
                .requestedAmount(dto.getAmount())
                .monthlyPayment(monthlyPayment)
                .totalAmount(totalAmount)
                .rate(rate)
                .build();
    }

    private BigDecimal createMonthlyPayment(BigDecimal rate, BigDecimal loanAmount, int term, Boolean insurance) {

        BigDecimal annuityCoefficient = getAnnuityCoefficient(rate, term);
        BigDecimal insurancePayment = createInsuranceCost(loanAmount, insurance, term);
        BigDecimal monthlyPayment = loanAmount.multiply(annuityCoefficient).add(insurancePayment);
        log.info("Рассчитан ежемесячный платеж");
        return monthlyPayment.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getAnnuityCoefficient(BigDecimal rate, int term) {
        BigDecimal hundredthMonthlyRate = rate
                .divide(BigDecimal.valueOf(12), MathContext.DECIMAL128)
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL128);   //  1/100 часть месячной процентной ставки
        BigDecimal denominator = (BigDecimal.ONE.add(hundredthMonthlyRate)).pow(term)
                .subtract(BigDecimal.ONE);
        return hundredthMonthlyRate.divide(denominator, MathContext.DECIMAL128).add(hundredthMonthlyRate);
    }

    private BigDecimal createInsuranceCost(BigDecimal amount, boolean isInsuranceEnable, int term) {
        BigDecimal insurancePayment = BigDecimal.ZERO;
        if (isInsuranceEnable) {
            log.info("Добавление страховочных выплат");
            BigDecimal insuranceRate = properties.getMonthlyInsuranceRate();
            insurancePayment = amount.multiply(insuranceRate)
                    .divide(BigDecimal.valueOf(term), MathContext.DECIMAL128)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return insurancePayment;
    }
}

