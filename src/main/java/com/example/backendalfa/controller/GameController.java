package com.example.backendalfa.controller;

import com.example.backendalfa.model.Game;
import com.example.backendalfa.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@RestController
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @PostConstruct
    public void fillDB() {
        if (gameRepository.count()==0){
            gameRepository.save(new Game("game0","Jk Rowling", new Date(),0));
            gameRepository.save(new Game("game1","Jk Rowing", new Date(),1));
            gameRepository.save(new Game("game2","Jk Bowling", new Date(),2));
        }
    }

    @GetMapping("games/{appId}")
    public Game getByAppId(@PathVariable Integer appId){
        return gameRepository.findGameByAppId(appId);
    }

    @GetMapping("games/developer/{developer}")
    public List<Game> getByDeveloper(@PathVariable String developer){
        return gameRepository.findGamesByDeveloper(developer);
    }

    @PostMapping("/games")
    public Game addGame(@RequestBody Game game){
        gameRepository.save(game);
        return game;
    }

    @PutMapping("/games")
    public Game updateGame(@RequestBody Game updatedGame){
        Game retrievedGame = gameRepository.findGameByAppId(updatedGame.getAppId());
        retrievedGame.setAppId(updatedGame.getAppId());
        retrievedGame.setDeveloper(updatedGame.getDeveloper());
        retrievedGame.setGameTitle(updatedGame.getGameTitle());
        retrievedGame.setReleaseDate(updatedGame.getReleaseDate());
        gameRepository.save(retrievedGame);
        return retrievedGame;
    }

    @DeleteMapping("/games/{appId}")
    public ResponseEntity deleteReview(@PathVariable Integer appId){
        Game game = gameRepository.findGameByAppId(appId);
        if(game!=null){
            gameRepository.delete(game);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
