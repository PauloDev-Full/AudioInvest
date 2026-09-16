package dio.projeto.infrastructure.http.response;

import dio.projeto.application.output.InvestmentOutput;

import java.math.BigDecimal;

public record InvestmentResponse(String id, String category, String description, BigDecimal amount) {

    public static InvestmentResponse from(InvestmentOutput output) {
        return new InvestmentResponse(output.id(), output.category(), output.description(), output.value());
    }
}