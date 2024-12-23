package pl.dombur.cinema.infrastructure.repository

import org.springframework.stereotype.Repository
import pl.dombur.cinema.common.persistence.BaseRepository
import pl.dombur.cinema.infrastructure.persistence.MovieEntity

@Repository
interface MovieRepository : BaseRepository<MovieEntity>
