package dio.projeto.infrastructure.persistence.repository;

import dio.projeto.domain.Category;
import dio.projeto.infrastructure.persistence.entity.InvestmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface InvestmentEntityRepository extends CrudRepository<InvestmentEntity, UUID> {

    List<InvestmentEntity> findAllByCategory(Category category);
}