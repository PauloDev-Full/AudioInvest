package dio.projeto.application;

import dio.projeto.application.input.PersistInvestmentInput;
import dio.projeto.application.output.InvestmentOutput;
import dio.projeto.domain.Category;
import dio.projeto.domain.Investment;
import dio.projeto.domain.InvestmentRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class PersistInvestmentUseCase {

    private final InvestmentRepository investmentRepository;

    public PersistInvestmentUseCase(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }

    public InvestmentOutput execute(PersistInvestmentInput input) {
        var investment = investmentRepository.save(
                new Investment(input.description(), input.investmentAmount(), input.category()));

        return InvestmentOutput.from(investment);
    }

    @Tool(name = "persist-investment", description = "Persiste um novo investimento financeiro")
    public InvestmentOutput persist(
            @ToolParam(description = "Descricao do investimento, ex.: 'CDB do Banco Inter'")
            String description,
            @ToolParam(description = "Valor investido em CENTAVOS. R$ 1.500,00 deve ser enviado como 150000")
            long investmentAmount,
            @ToolParam(description = "Categoria: FIXED_INCOME, EQUITIES ou SELIC_TREASURY")
            Category category) {

        return execute(new PersistInvestmentInput(description, investmentAmount, category));
    }
}