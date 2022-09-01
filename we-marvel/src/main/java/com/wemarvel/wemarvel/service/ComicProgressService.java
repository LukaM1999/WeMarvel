package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.ComicProgress;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.ComicProgressDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import org.hibernate.validator.constraints.br.CPF;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.xml.bind.Marshaller;
import java.util.List;

public interface ComicProgressService {
    List<ComicProgressDTO> getSharedComics(String firstUsername, String secondUsername);
    List<ComicProgressDTO> getUniqueComics(String u1, String u2);
    List<ComicProgressDTO> getUserComics(String username);
    ComicProgress create(ComicProgressDTO comicProgress);
    void delete(Long id);
    List<ComicProgressDTO> getUnreviewedComics();
    List<SeriesDTO> getUnreviewedSeries();
    List<CharacterDTO> getUnreviewedCharacters();
    ComicProgressDTO getUserComic(String username, Long comicId);
}
