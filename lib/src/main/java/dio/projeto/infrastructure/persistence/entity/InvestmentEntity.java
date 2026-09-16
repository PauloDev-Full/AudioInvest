package dio.projeto.infrastructure.persistence.entity;

import dio.projeto.domain.Category;
import dio.projeto.domain.Investment;
import dio.projeto.domain.InvestmentId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "investment")
@Getter
@Setter
@NoArgsConstructor
public class InvestmentEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private long investmentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Transient
    private boolean isNew = true;

    public InvestmentEntity(UUID id, String description, long investmentAmount, Category category) {
        this.id = id;
        this.description = description;
        this.investmentAmount = investmentAmount;
        this.category = category;
    }

    public static InvestmentEntity from(Investment investment) {
        return new InvestmentEntity(
                investment.getId().uuid(),
                investment.getDescription(),
                investment.getInvestmentAmount(),
                investment.getCategory());
    }

    public Investment toDomain() {
        return new Investment(
                new InvestmentId(this.id),
                this.description,
                this.investmentAmount,
                this.category);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestmentEntity other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}