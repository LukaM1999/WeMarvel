package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Review;
import com.wemarvel.wemarvel.model.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
   Review createReview(ReviewDTO review);
   List<ReviewDTO> getReviewsByUser(String username);
   List<ReviewDTO> getReviews();
   List<ReviewDTO> getReviewsByCharacterId(Long id);
   List<ReviewDTO> getReviewsByComicId(Long id);
   List<ReviewDTO> getReviewsBySeriesId(Long id);
   void deleteReview(Long id);
    ReviewDTO getReviewByEntity(Long entityId);
}
