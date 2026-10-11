package com.kmct.floodrelief;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceLogRepository extends JpaRepository<ResourceLog, Long> {
}