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

    private static final String BASE_API_URL = "https://rickandmortyapi.com/api";
    private final RestTemplate restTemplate = new RestTemplate(); // TODO replace with WebClient later.

    @GetMapping("/characters")
    public List<Map<String, Object>> getAllCharacters()
    {
        return fetchCharactersPaginated(BASE_API_URL + "/character");
    }

    @GetMapping("/characters/{id}")
    public Map<String, Object> getCharacterById(@PathVariable int id)
    {
        String url = BASE_API_URL + "/character/" + id;
        Map<String, Object> character = restTemplate.getForObject(url, Map.class);
        return Map.of(
                "id", character.get("id"),
                "name", character.get("name"),
                "species", character.get("species")
        );
    }

    @GetMapping("/by-status")
    public List<Map<String, Object>> getCharactersByStatus(@RequestParam String status)
    {
        return fetchCharactersPaginated(BASE_API_URL + "/character?status=" + status);
    }

    @GetMapping("/statistics/species")
    public int getLivingCharactersBySpecies(@RequestParam String species)
    {
        String url = BASE_API_URL + "/character?status=alive";
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

    private List<Map<String, Object>> fetchCharactersPaginated(String initialUrl)
    {
        String url = initialUrl;
        List<Map<String, Object>> characters = new ArrayList<>();

        while (url != null)
        {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null)
            {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                for (Map<String, Object> character : results)
                {
                    characters.add(Map.of(
                            "id", character.get("id"),
                            "name", character.get("name"),
                            "species", character.get("species")
                    ));
                }
                Map<String, Object> info = (Map<String, Object>) response.get("info");
                url = (String) info.get("next");
            } else
                url = null;

        }
        return characters;
    }

}
