package org.example.output


data class ActorsFilms(
    val actor: String,
    val movies: List<String> // works only with jackson annotation
) {
    override fun toString(): String = "ActorsFilms{actor='$actor', movies=$movies}"
}