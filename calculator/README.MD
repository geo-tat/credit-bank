# МС Калькулятор

## Api:
POST: /calculator/offers - расчёт возможных условий кредита. 
Request - LoanStatementRequestDto, response - List<LoanOfferDto>

POST: /calculator/calc - валидация присланных данных + скоринг данных + полный расчет параметров кредита. 
Request - ScoringDataDto, response CreditDto.

## Swagger doc:
*  doc http://localhost:8080/swagger-ui
*  OAS: http://localhost:8080/v3/api-docs