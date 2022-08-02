package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Review;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

}
