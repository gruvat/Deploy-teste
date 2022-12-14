package com.ciandt.summit.bootcamp2022.repository;

import com.ciandt.summit.bootcamp2022.entity.Music;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MusicRepositoryTest {

    @Autowired
    private MusicRepository musicRepository;
    Set<Music> musics;

    @Nested
    @DisplayName(value = "Test method findMusicsByMusicsAndArtistsName")
    public class FindMusicsByMusicsAndArtistsName {
        @DisplayName(value = "Find music")
        @ParameterizedTest(name = "By full name {0}")
        @ValueSource(strings = {"\"Cassius\" Love Vs. \"Sonny\" Wilson", "313"})
        public void testFindMusicsByFullName(String name) {
            musics = musicRepository.findMusicsByMusicsAndArtistsName(name);
            musics.forEach(m -> assertEquals(name, m.getName(),
                    () -> "Should return music by full name"));
        }

        @DisplayName("Find multiple musics")
        @ParameterizedTest(name = "By part of the name {0}")
        @ValueSource(strings = {"Reminiscing", "Help!"})
        public void testFindMultipleMusicsByPartOfTheName(String name) {
            musics = musicRepository.findMusicsByMusicsAndArtistsName(name);

            musics.forEach(m -> assertTrue(m.getName().toLowerCase().contains(name.toLowerCase()),
                    () -> "Should return multiple musics by part of the name"));
        }

        @DisplayName("Find multiple musics and return in order")
        @ParameterizedTest(name = "By name or part of the name {0}")
        @ValueSource(strings = {"Love", "Help!"})
        public void testFindMultipleMusicsOrderedByNameOrPartOfTheName(String name) {
            musics = musicRepository.findMusicsByMusicsAndArtistsName(name);

            Set<Music> musicsOrdered = musics.stream()
                    .sorted(Comparator.comparing(m -> m.getName().toLowerCase()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            assertEquals(musics,
                    musicsOrdered,
                    () -> "Should return a set of musics ordered by name");
        }

        @DisplayName("Find musics ignoring case")
        @ParameterizedTest(name = "with name {0}")
        @ValueSource(strings = {"falling like rain", "STRAWBERRY FIELDS FOREVER"})
        public void testFindMusicsByNameIgnoringCase(String name) {
            musics = musicRepository.findMusicsByMusicsAndArtistsName(name);

            musics.forEach(m ->
                    assertEquals(name.toLowerCase(),
                            m.getName().toLowerCase(), () -> "Should return music ignoring case"));
        }

        @DisplayName("Find non-existing musics")
        @ParameterizedTest(name = "With invalid name {0}")
        @ValueSource(strings = {"INVALID_NAME", "$@#%25"})
        public void testFindWithNoReturn(String name) {
            musics = musicRepository.findMusicsByMusicsAndArtistsName(name);

            assertEquals(0, musics.size(), () -> "Shouldn't return any musics");
        }

        @DisplayName("Find musics with empty parameter")
        @Test
        public void testReturnIfParameterIsEmpty() {
            musics = musicRepository.findMusicsByMusicsAndArtistsName("");
            assertNotEquals(0, musics.size(), () -> "Should return all musics");
        }
    }

    @Nested
    @DisplayName(value = "Test method findMusicsByPlaylistId")

    public class FindMusicsByPlaylistId {

        @Autowired
        private PlaylistRepository playlistRepository;

        @DisplayName("Find all musics by playlistId")
        @ParameterizedTest(name = "with playlistId {0}")
        @ValueSource(strings = {"92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void testFindMusicsByPlaylistId(String playlistId) {
            Set<Music> musicsByPlaylistFromPlaylistRepository = playlistRepository.findById(playlistId).get().getMusics();
            Set<Music> musicsByPlaylistFromMusicRepository = musicRepository.findMusicsByPlaylistId(playlistId);

            assertEquals(musicsByPlaylistFromPlaylistRepository, musicsByPlaylistFromMusicRepository,
                    () -> "Should contain the same musics");
            assertEquals(musicsByPlaylistFromPlaylistRepository.size(), musicsByPlaylistFromMusicRepository.size(),
                    () -> "Should contain the same number of musics");

        }

        @DisplayName("Find musics by playlistId ordered by artist's name and music's name")
        @ParameterizedTest(name = "with playlistId {0}")
        @ValueSource(strings = {"92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed"})
        public void testFindOrderedMusicsByPlaylistId(String playlistId) {
            Set<Music> musics = musicRepository.findMusicsByPlaylistId(playlistId);

            Set<Music> musicsOrdered = musics.stream()
                    .sorted(Comparator.comparing(m -> m.getName().toLowerCase()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            assertEquals(musicsOrdered, musics,
                    () -> "Should contain the musics ordered by artist's name and music's name");
        }
    }
}
