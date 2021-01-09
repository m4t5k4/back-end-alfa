package com.example.backendalfa.repository;

import com.example.backendalfa.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String>{
    List<Game> findGamesByDeveloper(String developer);
    Game findGameByAppId(Integer appId);
}
