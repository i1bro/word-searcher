package com.example.wordsearcher

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


@Controller
class HtmlController {
    @Autowired
    private val context: ApplicationContext? = null

    @GetMapping
    fun search(model: Model): String {
        model["has_results"] = false
        return "search"
    }

    data class QueryContainer(val query: String)

    @PostMapping
    fun results(@ModelAttribute("query") formData: QueryContainer, model: Model): String {
        model["has_results"] = true
        val trie = context!!.getBean("getTrie", Trie::class.java)
        val results = trie.getFilesWithWords(formData.query.split(' ', ','))
        model["results"] = results
        return "search"
    }
}