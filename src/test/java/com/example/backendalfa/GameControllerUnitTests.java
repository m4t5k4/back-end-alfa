package com.example.backendalfa;


import com.example.backendalfa.model.Game;
import com.example.backendalfa.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    private Game game1 = new Game("titel1", "developer1", new Date(), 1);
    private Game game2 = new Game("titel2", "developer1", new Date(), 2);
    Game GameToBeDeleted = new Game("titel3", "developer2", new Date(), 3);

    @BeforeEach
    public void beforeAllTests() {
        gameRepository.deleteAll();
        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(GameToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
        gameRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenGame_whenGetGameByAppId_thenReturnJsonReview() throws Exception {

        given(gameRepository.findGameByAppId(1)).willReturn(game1);

        mockMvc.perform(get("/games/{appId}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameTitle", is("titel1")))
                .andExpect(jsonPath("$.developer", is("developer1")))
                .andExpect(jsonPath("$.appId", is(3)));
        //date
    }

    @Test
    public void givenGames_whenGetGamesByDeveloper_thenReturnJsonReviews() throws Exception {

        List<Game> gameList = new ArrayList<>();
        gameList.add(game1);
        gameList.add(game1);

        given(gameRepository.findGamesByDeveloper("developer1")).willReturn(gameList);

        mockMvc.perform(get("/games/developer/{developer}", "developer1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gameTitle", is("titel1")))
                .andExpect(jsonPath("$[0].developer", is("developer1")))
                .andExpect(jsonPath("$[0].appId", is(1)))
                .andExpect(jsonPath("$[1].gameTitle", is("titel2")))
                .andExpect(jsonPath("$[1].developer", is("developer1")))
                .andExpect(jsonPath("$[1].appId", is(2)));
    }
    //hier
    @Test
    public void whenPostGame_thenReturnJsonReview() throws Exception {
        Game game4 = new Game("posttitel", "developer2", new Date(0), 4);

        mockMvc.perform(post("/games")
                .content(mapper.writeValueAsString(game4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameTitle", is("posttitel")))
                .andExpect(jsonPath("$.developer", is("developer2")))
                .andExpect(jsonPath("$.appId", is(4)));
        //date
    }

    @Test
    public void givenGame_whenPutGame_thenReturnJsonReview() throws Exception {

        Game game1 = new Game("updatedtitel", "developer2", new Date(0), 1);

        given(gameRepository.findGameByAppId(1)).willReturn(game1);

        mockMvc.perform(put("/games")
                .content(mapper.writeValueAsString(game1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameTitle", is("updatedtitel")))
                .andExpect(jsonPath("$.developer", is("developer2")))
                .andExpect(jsonPath("$.appId", is(4)));
        //date
    }

    @Test
    public void givenGame_whenDeleteGame_thenStatusOk() throws Exception {


        given(gameRepository.findGameByAppId(1)).willReturn(GameToBeDeleted);
        mockMvc.perform(delete("/games/{appId}", 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoGame_whenDeleteGame_thenStatusNotFound() throws Exception {

        given(gameRepository.findGameByAppId(1)).willReturn(null);
        mockMvc.perform(delete("/games/{appId}", 999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
