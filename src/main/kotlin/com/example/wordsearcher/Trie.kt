package com.example.wordsearcher

import java.io.Reader

// Suggest we have real text and not file of random symbols.
// Then the trie won't be very deep and doesn't really need to be compressed
class Trie {
    // It's better to store files' indexes in this list instead of their names
    private val files = ArrayList<String>()

    class Node {
        // list of files that have a word ending in this node
        private val fileIds = ArrayList<Int>()
        private val children = HashMap<Char, Node>()

        fun getFileIds(): List<Int> = fileIds

        fun walk(inputC: Char, createNodes: Boolean = true): Node? {
            val c = inputC.lowercaseChar()
            if(createNodes && !children.containsKey(c)) {
                children[c] = Node()
            }
            // not null if createNodes is true
            return children[c]
        }

        fun addFile(id: Int) = fileIds.add(id)
    }

    private val root = Node()

    private fun getFilesWithWord(word: String): List<Int> {
        var curNode = root
        for(c in word) {
            // We shouldn't create new nodes when executing searches
            curNode = curNode.walk(c, createNodes = false)?: return emptyList()
        }
        return curNode.getFileIds()
    }

    fun processFile(fileName: String, reader: Reader) {
        var curNode = root
        var c = reader.read()
        while(c != -1) {
            if(c.toChar().isLetter()) {
                curNode = curNode.walk(c.toChar())!!
            } else if(curNode != root) {
                curNode.addFile(files.size)
                curNode = root
            }
            c = reader.read()
        }
        if(curNode != root) {
            curNode.addFile(files.size)
        }
        files.add(fileName)
    }

    fun getFilesWithWords(words: List<String>): List<String> {
        // Could actually sort the words and go up only to common ancestor of adjacent words,
        // but it isn't much of an optimization on a real text
        return words.flatMap { word -> getFilesWithWord(word) }.distinct().sorted().map { id -> files[id] }
    }
}