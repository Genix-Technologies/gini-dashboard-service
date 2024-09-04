package gini.ginidashboardservice.repositories;

import gini.ginidashboardservice.models.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
}