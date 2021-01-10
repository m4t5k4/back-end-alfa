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
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private GameRepository gameRepository;


    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenGame_whenGetGameByAppId_thenReturnJsonReview() throws Exception {
        Game game1 = new Game(1,"titel1", "developer1", new Date());
        given(gameRepository.findGameByAppId(1)).willReturn(game1);

        mockMvc.perform(get("/games/{appId}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameTitle", is("titel1")))
                .andExpect(jsonPath("$.developer", is("developer1")))
                .andExpect(jsonPath("$.appId", is(1)));
        //date
    }

    @Test
    public void givenGames_whenGetGamesByDeveloper_thenReturnJsonReviews() throws Exception {
        Game game1 = new Game(1,"titel1", "developer1", new Date());
        Game game2 = new Game(2,"titel2", "developer1", new Date());
        List<Game> gameList = new ArrayList<>();
        gameList.add(game1);
        gameList.add(game2);

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
        Game game4 = new Game(4,"posttitel", "developer2", new Date(0));

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

        Game game1 = new Game(1,"updatedtitel", "developer2", new Date(0));

        given(gameRepository.findGameByAppId(1)).willReturn(game1);

        mockMvc.perform(put("/games")
                .content(mapper.writeValueAsString(game1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameTitle", is("updatedtitel")))
                .andExpect(jsonPath("$.developer", is("developer2")))
                .andExpect(jsonPath("$.appId", is(1)));
        //date
    }

    @Test
    public void givenGame_whenDeleteGame_thenStatusOk() throws Exception {

        Game GameToBeDeleted = new Game(3,"titel3", "developer2", new Date());

        given(gameRepository.findGameByAppId(3)).willReturn(GameToBeDeleted);
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
