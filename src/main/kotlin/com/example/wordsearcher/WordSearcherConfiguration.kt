package com.example.wordsearcher

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class WordSearcherConfiguration {

    @Bean
    fun getTrie(): Trie {
        val trie = Trie()
        // empty trie if FILE_PATH is not specified or incorrect
        val filePath = System.getenv("FILE_PATH")?: return trie
        File(filePath).listFiles()?.forEach {
            trie.processFile(it.name, it.bufferedReader())
        } ?: return trie
        return trie
    }
}