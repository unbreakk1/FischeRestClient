package org.example.fischerestclient;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class AsterixController
{
    private final RestTemplate restTemplate = new RestTemplate(); // TODO replace with "WebClient" later

    @GetMapping
    public List<Map<String,Object>> getAllCharacters()
    {
        String url = "https://rickandmortyapi.com/api/character";
        List<Map<String,Object>> characters = new ArrayList<>();
        while(url != null)
        {
            Map response = restTemplate.getForObject(url, Map.class);

            if(response != null)
            {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                for (Map<String, Object> character : results)
                {
                    characters.add(Map.of(
                            "id", character.get("id"),
                            "name", character.get("name"),
                            "species", character.get("species")));

                }
                Map<String, Object> info = (Map<String, Object>) response.get("info");
                url = (String) info.get("next");
            }
            else
                url = null;
       }
        return characters;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getCharacterById(@PathVariable int id)
    {
        String url = "https://rickandmortyapi.com/api/character/" + id;

        Map<String, Object> character = restTemplate.getForObject(url, Map.class);

        return Map.of(
                "id", character.get("id"),
                "name", character.get("name"),
                "species", character.get("species")
        );
    }

    @GetMapping("/status")
    public List<Map<String, Object>> getCharactersByStatus(@RequestParam String status)
    {
        String url = "https://rickandmortyapi.com/api/character?status=" + status;
        List<Map<String, Object>> filteredCharacters = new ArrayList<>();

        while (url != null)
        {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null)
            {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                for (Map<String, Object> character : results)
                {
                    if (status.equalsIgnoreCase((String) character.get("status")))
                        filteredCharacters.add(character);

                }

                Map<String, Object> info = (Map<String, Object>) response.get("info");
                url = (String) info.get("next");
            } else {
                url = null;
            }
        }

        return filteredCharacters;

    }

    @GetMapping("/species-statistic")
    public int getLivingCharactersBySpecies(@RequestParam String species)
    {
        String url = "https://rickandmortyapi.com/api/character?status=alive";
        int count = 0;

        while (url != null)
        {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null)
            {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                for (Map<String, Object> character : results)
                {
                    if (species.equalsIgnoreCase((String) character.get("species")))
                        count++;

                }

                Map<String, Object> info = (Map<String, Object>) response.get("info");
                url = (String) info.get("next");
            }
            else
                url = null;
        }

        return count;
    }
}
