package com.example.backendalfa.controller;

import com.example.backendalfa.model.Game;
import com.example.backendalfa.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("games/game/{gameId}")
    public Game getByAppId(@PathVariable Integer gameId){
        return gameRepository.findGameByAppId(gameId);
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
}
