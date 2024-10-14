package gini.ginidashboardservice.repositories;

import gini.ginidashboardservice.models.ClientProfileV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProfileV2Repository extends JpaRepository<ClientProfileV2, Long> {
}
