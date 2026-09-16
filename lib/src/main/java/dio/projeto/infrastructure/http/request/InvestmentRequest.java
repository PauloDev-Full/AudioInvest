package dio.projeto.infrastructure.http.request;

import dio.projeto.application.input.PersistInvestmentInput;
import dio.projeto.domain.Category;

public record InvestmentRequest(String description, Category category, long amount) {

    public PersistInvestmentInput toInput() {
        return new PersistInvestmentInput(description, amount, category);
    }
}