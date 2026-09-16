package dio.projeto.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Investment {

    private final InvestmentId id;
    private final String description;
    private final long investmentAmount;
    private final Category category;

    public Investment(InvestmentId id, String description, long investmentAmount, Category category) {
        this.id = Objects.requireNonNull(id, "id obrigatorio");
        this.description = description;
        this.investmentAmount = investmentAmount;
        this.category = category;
    }

    public Investment(String description, long investmentAmount, Category category) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("descricao obrigatoria");
        }
        if (investmentAmount <= 0) {
            throw new IllegalArgumentException("valor deve ser positivo e expresso em centavos");
        }
        this.id = new InvestmentId();
        this.description = description.trim();
        this.investmentAmount = investmentAmount;
        this.category = Objects.requireNonNull(category, "categoria obrigatoria");
    }
}