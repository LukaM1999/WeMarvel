package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Review;
import com.wemarvel.wemarvel.model.dto.ReviewDTO;
import com.wemarvel.wemarvel.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("")
    public Review createReview(@RequestBody ReviewDTO review) {
        return reviewService.createReview(review);
    }

    @GetMapping("/user/{username}")
    public List<ReviewDTO> getReviewsByUser(@PathVariable String username) {
        return reviewService.getReviewsByUser(username);
    }

    @GetMapping("/{entityId}")
    public ReviewDTO getReviewByEntity(@PathVariable Long entityId) {
        return reviewService.getReviewByEntity(entityId);
    }

    @GetMapping("")
    public List<ReviewDTO> getReviews() {
        return reviewService.getReviews();
    }

    @GetMapping("/character/{id}")
    public List<ReviewDTO> getReviewsByCharacterId(@PathVariable Long id) {
        return reviewService.getReviewsByCharacterId(id);
    }

    @GetMapping("/comic/{id}")
    public List<ReviewDTO> getReviewsByComicId(@PathVariable Long id) {
        return reviewService.getReviewsByComicId(id);
    }

    @GetMapping("/series/{id}")
    public List<ReviewDTO> getReviewsBySeriesId(@PathVariable Long id) {
        return reviewService.getReviewsBySeriesId(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
