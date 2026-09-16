package dio.projeto.domain;

import java.util.List;

public interface InvestmentRepository {

    Investment save(Investment investment);

    List<Investment> findAllByCategory(Category category);
}