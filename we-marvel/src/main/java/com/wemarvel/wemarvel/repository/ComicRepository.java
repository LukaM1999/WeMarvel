package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicRepository extends PagingAndSortingRepository<Comic, Long> {

}
