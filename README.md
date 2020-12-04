# Merlin Chacko

Mancala Game Implementation

I have implemented the mancala game using Java and Spring boot for the backend api and angular for the frontend.
From the UI, you can add 2 player details and start the game. This will calls a rest api to create a game in the h2 database. While creating game, first player is set as the initial player turn. 
So first player got the chance to start moving the stones. Also 14(0-13) pits are created in createGame api.
Once the game created based on the player turns each player can sow the stones. This will call another rest api for sowing the stones.
After all stones are sowed for any player, game will be over.

## How to run
Run backend and frontend separately
##### For backend 
 * cd mancala-game
 * gradlew bootRun
##### For frontend 
 * cd mancala-app
 * npm install
 * npm start
 
Now, you can open http://localhost:4200 page and play the game!

## Improvements
* Error handling in frontend
* Add integration/end to end tests
* Dockerize frontend and backend
* Improve swagger documentation

 
    

