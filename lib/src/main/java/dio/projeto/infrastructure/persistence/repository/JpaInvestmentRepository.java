package dio.projeto.infrastructure.persistence.repository;

import dio.projeto.domain.Category;
import dio.projeto.domain.Investment;
import dio.projeto.domain.InvestmentRepository;
import dio.projeto.infrastructure.persistence.entity.InvestmentEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class JpaInvestmentRepository implements InvestmentRepository {

    private final InvestmentEntityRepository investmentEntityRepository;

    public JpaInvestmentRepository(InvestmentEntityRepository investmentEntityRepository) {
        this.investmentEntityRepository = investmentEntityRepository;
    }

    @Override
    @Transactional
    public Investment save(Investment investment) {
        var entity = InvestmentEntity.from(investment);
        return investmentEntityRepository.save(entity).toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Investment> findAllByCategory(Category category) {
        return investmentEntityRepository.findAllByCategory(category)
                .stream()
                .map(InvestmentEntity::toDomain)
                .toList();
    }
}