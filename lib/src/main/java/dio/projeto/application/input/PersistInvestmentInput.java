package dio.projeto.application.input;

import dio.projeto.domain.Category;

public record PersistInvestmentInput(String description, long investmentAmount, Category category) {
}