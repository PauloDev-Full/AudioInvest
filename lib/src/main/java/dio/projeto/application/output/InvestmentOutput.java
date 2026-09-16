package dio.projeto.application.output;

import dio.projeto.domain.Investment;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record InvestmentOutput(String id, String description, String category, BigDecimal value) {

    private static final BigDecimal CENTS_PER_UNIT = BigDecimal.valueOf(100);

    public static InvestmentOutput from(Investment investment) {
        return new InvestmentOutput(
                investment.getId().uuid().toString(),
                investment.getDescription(),
                investment.getCategory().name(),
                toCurrency(investment.getInvestmentAmount()));
    }

    private static BigDecimal toCurrency(long amountInCents) {
        return BigDecimal.valueOf(amountInCents)
                .divide(CENTS_PER_UNIT, 2, RoundingMode.HALF_UP);
    }
}