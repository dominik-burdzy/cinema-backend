# Fast & Furious Cinema API

This project provides the backend API for a small cinema exclusively playing movies from the **Fast & Furious** franchise. The API is designed to support both internal cinema operations and customer-facing functionalities.

---

## Features

### Internal (Admin) Features:
1. **Update Showtimes and Prices**: Allows cinema owners to modify show schedules and ticket prices for their movie catalog.

### Customer (Moviegoer) Features:
1. **Fetch Movie Showtimes**: Retrieve the show schedule for all movies.
2. **Fetch Movie Details**: Access detailed information about a specific movie (e.g., title, description, release date, rating, IMDb rating, runtime) via the OMDb API.
3. **Submit Movie Reviews**: Leave a 1-10 star rating and optional comments for any movie.

### Technical Features:
1. **Persistence Layer**: Saves showtimes, prices, reviews, and other data in a database.
2. **API Documentation**: The API is documented using the OpenAPI/Swagger standard.
3. **Third-Party API Integration**: Fetches movie details from the Open Movie Database (OMDb) API.

---

## Movie Catalog
The following movies are supported in this system:

| Title                                 | IMDb ID     |
|---------------------------------------|-------------|
| The Fast and the Furious              | tt0232500   |
| 2 Fast 2 Furious                      | tt0322259   |
| The Fast and the Furious: Tokyo Drift | tt0463985   |
| Fast & Furious                        | tt1013752   |
| Fast Five                             | tt1596343   |
| Fast & Furious 6                      | tt1905041   |
| Furious 7                             | tt2820852   |
| The Fate of the Furious               | tt4630562   |
| F9: The Fast Saga                     | tt5433138   |

---

## API Endpoints

### Admin Endpoints:
- **Create Show**:
  - `POST /api/v1/internal/shows`

- **Update Show Details**:
  - `PUT /api/v1/internal/shows/{referenceId}`

### Customer Endpoints:
- **Fetch list of Shows**:
  - `GET /api/v1/public/shows`

- **Fetch Show Details**:
  - `GET /api/v1/public/shows/{referenceId}`

- **Fetch list of Movies**:
  - `GET /api/v1/public/movies`

- **Fetch Movie Details**:
  - `GET /api/v1/public/movies/{referenceId}`

- **Submit Movie Review**:
  - `POST /api/v1/public/movies/{referenceId}/ratings`

---

## OMDb API Integration
To fetch movie details, the system integrates with the OMDb API.

You can use the following API key for local tests:
`e4f33820`

---

## Local configuration

### Configuration file

Create a copy of [application-local.template.yml](./src/main/resources/application-local.template.yml) and rename it to
`application-local.yml`. This configuration file is loaded while bootstrapping and contains required liquibase config.

In the `application-local.yml` file, you need to set the OMDb API key:
```yaml
client:
  omdb:
    apiKey: "xxx"
```

### Prerequisites

To run this project, you need to have the following installed:
* [Docker](https://docs.docker.com/get-docker/)
* [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)

### Running tests

To run the tests, execute the following command:

```shell
make test
```

### Database

Set up your PostgreSQL database with `make prepare_db`. You need to have docker app running.

### Running the project

To run the project, execute the following command:

```shell
make start.local
```

### Calling the API

In [http](./http) directory you can find files with examples of API calls.
