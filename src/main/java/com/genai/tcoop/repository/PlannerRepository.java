package com.genai.tcoop.repository;

import com.genai.tcoop.model.entity.Planner;
import com.genai.tcoop.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {

    List<Planner> findAllByUser(UserAccount user);
}
