package com.example.converter.repositories;

import com.example.converter.models.History;
import com.example.converter.models.Valute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Long> {

}
