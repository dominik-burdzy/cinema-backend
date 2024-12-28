package pl.dombur.cinema.domain.movie

import pl.dombur.cinema.infrastructure.external.omdb.MovieData

interface MovieDataProvider {
    fun getMovie(id: String): MovieData
}
