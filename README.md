## Table of Contents

- [Introduction](#Introduction)
- [Components](#components)
    * [KalahGameController](#KalahGameController)
    * [KalahGameService](#KalahGameService)    
    * [KalahGame](#KalahGame)    
    * [KalahGameTo](#KalahGameTo)    
    * [KalahGameMapper](#KalahGameMapper)    
    * [KalahGameRepository](#KalahGameRepository)    
    * [LockProvider](#LockProvider)    

- [HowToUse](#howtouse)    
- [Docker](#docker)

- [Build](#build)

- [WishList](#wishlist)


## Introduction

This project provides two end points(REST API) to implement the game of [Kalah](https://en.wikipedia.org/wiki/Kalah).

I have used MySql for this project as persistence layer, however considering the usecase, NoSqls are also applicable.
We need to consider the experts when we want to add a new technology, Mongo DB is another option here(but we need to make sure we understand it and we have experts to support us)

Also Redis has been used as a distributed lock provider.

By default server comes up on port 8080, and you need to use dock-compose to bring the MySql and Redis up.

to improve maintainability and Readability of the code SOLID principals, Design patterns(DI, IOC, Domain/Service/Repository(DDD)) have been applied.

Below I describe the components that has been used in this project from a top down view. 

## Components

Single responsibility and interface segregation has been considered on designing components. So each does single job.
Also dependencies are injected using Spring framework. Data Transfer Object(DTO) pattern has been used to separate inside model from outside.
Here are the main components of Kalah game service
 
#### * KalahGameController

 KalahGameController provides REST API to create game and move the game forward  
    
 KalahGameController provides these endpoints:
    
    POST:     /games
    
    PUT:      /{gameId}/pits/{pitId}
          
#### * KalahGameService

Kalah game service is the logic of the application. It persists KalahGame using KalahGameRepository.
Kalah game service does the move operation in a lock block that is provided by LockProvider interface.
LockProvider ensures that we won't have race condition and kalahGame won't be inconsistent.    
    
#### * KalahGame

  KalahGame is the main domain and aggregate of the kalah game service project.

#### * KalahGameTo

  KalahGameTo is [**Data Transfer Object(DTO)**](https://en.wikipedia.org/wiki/Data_transfer_object) for the KalahGame domain model.

#### * KalahGameMapper

   Maps KalahGame domains to transfer object(KalahGameTo).
   
#### * KalahGameRepository
    
   Is repository of kalah game objects. Does the main CRUD operations on the persistence layer.

#### * LockProvider
    
   LockProvider provides interface for the lock providers.
   
   RedissionLockProvider runs the kalahGameFunc in a distributed lock provided by [Redis](https://redis.io/), here
   the RedissonClient has been used to connect to Redis. 
   

## HowToUse

To use this application make sure docker-compose is up see [here](#docker).

You can use this command to create a game:
        
    curl -X POST http://127.0.0.1:8080/games

Also this one for example to move the game to next state:

    curl -X PUT http://127.0.0.1:8080/games/1/pits/1
    
## Docker 

To start MySql and Redis cd to docker folder and then run this command: 

    $ ./docker-compose -up

    
## Build 

Gradle is on charge for this project.
To build this project on the root of the project run this command:

    $ ./gradlew build
    
## WishList

* API documentation

    We can use Swagger for API documentation

* Investigate for a NoSql
    
    The scenario matches well for a NoSql like MongoDB. Let's talk for Cons. and Pros.    

* Keep history of movements