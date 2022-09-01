package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.*;
import com.wemarvel.wemarvel.model.dto.ReviewDTO;
import com.wemarvel.wemarvel.model.enums.ReviewType;
import com.wemarvel.wemarvel.repository.ReviewRepository;
import com.wemarvel.wemarvel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ComicService comicService;

    @Autowired
    private SeriesService seriesService;

    @Override
    public Review createReview(ReviewDTO review) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not logged in");
        Review newReview = new Review(null, user.getId(), review.getMarvelEntityId(),
                review.getType(), review.getRecommendation(), LocalDate.now(),
                review.getRating(), review.getText());
        return reviewRepository.save(newReview);
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(String username) {
        RegisteredUser user = registeredUserService.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        List<ReviewDTO> reviews = reviewRepository.findByOwnerId(user.getId());
        for(ReviewDTO review : reviews) {
            switch(review.getType()) {
                case CHARACTER:
                    MarvelCharacter character = characterService.getCharacter(review.getMarvelEntityId());
                    if(character == null) throw new EntityNotFoundException("Character not found");
                    review.setMarvelEntityTitle(character.getName());
                    review.setMarvelEntityThumbnail(character.getThumbnail());
                    break;
                case COMIC:
                    Comic comic = comicService.getById(review.getMarvelEntityId());
                    if(comic == null) throw new EntityNotFoundException("Comic not found");
                    review.setMarvelEntityTitle(comic.getTitle());
                    review.setMarvelEntityThumbnail(comic.getThumbnail());
                    break;
                case SERIES:
                     Series series = seriesService.getById(review.getMarvelEntityId());
                    if(series == null) throw new EntityNotFoundException("Series not found");
                    review.setMarvelEntityTitle(series.getTitle());
                    review.setMarvelEntityThumbnail(series.getThumbnail());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid review type");
            }
        }
        return reviews;
    }

    @Override
    public List<ReviewDTO> getReviews() {
        List<ReviewDTO> reviews = reviewRepository.getAll();
        for(ReviewDTO review : reviews) {
            switch(review.getType()) {
                case CHARACTER:
                    MarvelCharacter character = characterService.getCharacter(review.getMarvelEntityId());
                    if(character == null) throw new EntityNotFoundException("Character not found");
                    review.setMarvelEntityTitle(character.getName());
                    review.setMarvelEntityThumbnail(character.getThumbnail());
                    break;
                case COMIC:
                    Comic comic = comicService.getById(review.getMarvelEntityId());
                    if(comic == null) throw new EntityNotFoundException("Comic not found");
                    review.setMarvelEntityTitle(comic.getTitle());
                    review.setMarvelEntityThumbnail(comic.getThumbnail());
                    break;
                case SERIES:
                    Series series = seriesService.getById(review.getMarvelEntityId());
                    if(series == null) throw new EntityNotFoundException("Series not found");
                    review.setMarvelEntityTitle(series.getTitle());
                    review.setMarvelEntityThumbnail(series.getThumbnail());
                    break;
                default:
                    break;
            }
        }
        return reviews;
    }

    @Override
    public List<ReviewDTO> getReviewsByCharacterId(Long id) {
        return reviewRepository.findByCharacterId(id);
    }

    @Override
    public List<ReviewDTO> getReviewsByComicId(Long id) {
        return reviewRepository.findByComicId(id);
    }

    @Override
    public List<ReviewDTO> getReviewsBySeriesId(Long id) {
        return reviewRepository.findBySeriesId(id);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if(review == null) throw new EntityNotFoundException("Review not found");
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not logged in");
        if(!review.getOwnerId().equals(user.getId()) && !user.getRole().getAuthority().equals("ADMIN"))
            throw new IllegalArgumentException("User not authorized to delete this review");
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDTO getReviewByEntity(Long entityId) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not logged in");
        return reviewRepository.findByEntityAndUser(entityId, user.getId());
    }
}
