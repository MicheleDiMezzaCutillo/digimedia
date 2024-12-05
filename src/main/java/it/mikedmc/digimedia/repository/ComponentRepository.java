package it.mikedmc.digimedia.repository;

import it.mikedmc.digimedia.model.Category;
import it.mikedmc.digimedia.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ComponentRepository extends JpaRepository<Component,Long> {
}
