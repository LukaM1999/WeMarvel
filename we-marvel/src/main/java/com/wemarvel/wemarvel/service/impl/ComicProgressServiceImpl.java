package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.ComicProgress;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.model.dto.ComicProgressDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import com.wemarvel.wemarvel.repository.ComicProgressRepository;
import com.wemarvel.wemarvel.service.ComicProgressService;
import com.wemarvel.wemarvel.service.ComicService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class ComicProgressServiceImpl implements ComicProgressService {

    @Autowired
    private ComicProgressRepository comicProgressRepository;

    @Autowired
    private ComicService comicService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Override
    public List<ComicProgressDTO> getSharedComics(String firstUsername, String secondUsername) {
        if(firstUsername.equals(secondUsername)) throw new IllegalArgumentException("Usernames must be different.");
        RegisteredUser firstUser = registeredUserService.getUserByUsername(firstUsername);
        if(firstUser == null) throw new UsernameNotFoundException("First user not found");
        RegisteredUser secondUser = registeredUserService.getUserByUsername(secondUsername);
        if(secondUser == null) throw new UsernameNotFoundException("Second user not found");
        List<ComicProgressDTO> sharedComics = comicProgressRepository.getSharedComics(firstUser.getId(), secondUser.getId());
        for(ComicProgressDTO comicProgress: sharedComics) {
            comicProgress.setFirstUsername(firstUser.getUsername());
            comicProgress.setSecondUsername(secondUser.getUsername());
        }
        return sharedComics;
    }

    @Override
    public List<ComicProgressDTO> getUniqueComics(String u1, String u2) {
        if(u1.equals(u2)) throw new IllegalArgumentException("Usernames must be different.");
        RegisteredUser firstUser = registeredUserService.getUserByUsername(u1);
        if(firstUser == null) throw new UsernameNotFoundException("First user not found");
        RegisteredUser secondUser = registeredUserService.getUserByUsername(u2);
        if(secondUser == null) throw new UsernameNotFoundException("Second user not found");
        List<ComicProgressDTO> uniqueComics = comicProgressRepository.getUniqueComics(firstUser.getId(), secondUser.getId());
        for(ComicProgressDTO comicProgress: uniqueComics) {
            comicProgress.setFirstUsername(firstUser.getUsername());
        }
        return uniqueComics;
    }

    @Override
    public List<ComicProgressDTO> getUserComics(String username) {
        RegisteredUser user = registeredUserService.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        List<ComicProgressDTO> comics = comicProgressRepository.getUserComics(user.getId());
        for(ComicProgressDTO progress : comics) {
            Comic comic = comicService.getById(progress.getComicId());
            if(comic == null) continue;
            progress.setComicPages(comic.getPageCount());
        }
        return comics;
    }

    @Override
    public ComicProgress create(ComicProgressDTO comicProgress) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not found");
        ComicProgress existing = comicProgressRepository.getByUserAndComic(user.getId(), comicProgress.getComicId());
        if(existing != null) {
            existing.setStatus(comicProgress.getFirstStatus());
            existing.setRating(comicProgress.getFirstRating());
            existing.setPagesRead(comicProgress.getFirstPagesRead());
            existing.setStartDate(comicProgress.getFirstStartDate());
            existing.setEndDate(comicProgress.getFirstEndDate());
            return comicProgressRepository.save(existing);
        }
        ComicProgress newComicProgress = new ComicProgress(null, user.getId(),
                comicProgress.getComicId(), comicProgress.getFirstStatus(), comicProgress.getFirstPagesRead(),
                comicProgress.getFirstRating(), comicProgress.getFirstStartDate(), comicProgress.getFirstEndDate());
        return comicProgressRepository.save(newComicProgress);
    }

    @Override
    public void delete(Long id) {
        comicProgressRepository.deleteById(id);
    }

    @Override
    public List<ComicProgressDTO> getUnreviewedComics() {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not found");
        List<ComicProgressDTO> comics = comicProgressRepository.getUnreviewedComics(user.getId());
        for(ComicProgressDTO progress : comics) {
            Comic comic = comicService.getById(progress.getComicId());
            if(comic == null) continue;
            progress.setComicPages(comic.getPageCount());
        }
        return comics;
    }

    @Override
    public List<SeriesDTO> getUnreviewedSeries() {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not logged in");
        return comicProgressRepository.getUnreviewedSeries(user.getId());
    }

    @Override
    public List<CharacterDTO> getUnreviewedCharacters() {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not logged in");
        return comicProgressRepository.getUnreviewedCharacters(user.getId());
    }

    @Override
    public ComicProgressDTO getUserComic(String username, Long comicId) {
        RegisteredUser user = registeredUserService.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("User not found");
        return comicProgressRepository.getUserComic(user.getId(), comicId);
    }
}
