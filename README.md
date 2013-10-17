tttaas
======

**T**ic **T**ac **T**oe **a**s **a** **S**ervice. Because it is a light-hearted way to tinker with some tech that I want to learn.

## So what is this?

This is a straight-forward [Dropwizard](http://www.dropwizard.io/) service which provides an API for playing games of tic tac toe. You can create a new game and then make moves until the game is complete. It's basically a transactional service wrapped around "business" logic, whereby "business" logic I mean the rule that three-in-a-row is a win.

## Alright, what's the API then?

You can create a new game by `POST`ing to `/v1/game`, like so:

```
$ curl -X POST "http://localhost:8080/v1/game"
{"id":1,"state":"ONGOING","board":{"squares":[[null,null,null],[null,null,null],[null,null,null]]},"moves":[]}
```

You can get information about a specific game by `GET`ting `/v1/game/:gameId`, like this:

```
$ curl -X GET "http://localhost:8080/v1/game/12"
{"id":12,"state":"ONGOING","board":{"squares":[[null,null,null],[null,"X",null],[null,"O",null]]},"moves":[{"gameId":1,"player":"X","x":1,"y":1},{"gameId":1,"player":"O","x":1,"y":2}]}
```

And you can make moves by `POST`ing to `/v1/move/:gameId`, as such:

```
curl -X POST "http://localhost:8080/v1/move/12" --header "Content-Type: application/json" --header "Accept: application/json" --data '{"gameId": 12, "player": "O", "x": 2, "y": 1}'
{"id":1,"state":"ONGOING","board":{"squares":[[null,null,null],["X","X","O"],[null,"O",null]]},"moves":[{"gameId":1,"player":"X","x":1,"y":1},{"gameId":1,"player":"O","x":1,"y":2}]}
```

## How do I become Tic Tac Toe?

There's a sample instance that you're welcome to play with at `http://tttaas.herokuapp.com`.

If you'd like to stand up your own instance, you can do the following:

1. Clone this repository: `git clone git@github.com:jessex/tttaas.git`
2. Build it with Maven: `mvn clean verify`
3. Run the server (make sure to use the correctly versioned jar): `java -jar tttaas-service/target/tttaas-service-0.0.1-SNAPSHOT.jar server tttaas-service/src/main/resources/config/config.yml`

## License

Do as you wish. Enjoy.
