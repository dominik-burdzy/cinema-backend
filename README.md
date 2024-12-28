# cinema-backend

Backend project to support a small cinema, which only plays movies from the Fast & Furious franchise.

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
