package dio.projeto.application;

import dio.projeto.application.output.InvestmentOutput;
import dio.projeto.domain.Category;
import dio.projeto.domain.InvestmentRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListInvestmentsByCategoryUseCase {

    private final InvestmentRepository investmentRepository;

    public ListInvestmentsByCategoryUseCase(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    @Tool(name = "list-investment-by-category", description = "Lista investimentos por categoria")
    public List<InvestmentOutput> execute(
            @ToolParam(description = "Categoria: FIXED_INCOME, EQUITIES ou SELIC_TREASURY")
            Category category) {

        return investmentRepository.findAllByCategory(category)
                .stream()
                .map(InvestmentOutput::from)
                .toList();
    }
}