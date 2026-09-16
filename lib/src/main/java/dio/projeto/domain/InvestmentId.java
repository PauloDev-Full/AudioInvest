package dio.projeto.domain;

import java.util.Objects;
import java.util.UUID;

public record InvestmentId(UUID uuid) {

    public InvestmentId {
        Objects.requireNonNull(uuid, "uuid não pode ser nulo");
    }

    public InvestmentId() {
        this(UUID.randomUUID());
    }
}