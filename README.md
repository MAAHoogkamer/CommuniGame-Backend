# CommuniGame-Backend

## For testing purposes `localhost:8080` will be used instead of `com.staffbase.communigame`

There are 2 kinds of models; `Player` and `Score`. <br>
* All players can be called through GET `com.staffbase.communigame/players`<br> 
* A specific player can be called through GET `com.staffbase.communigame/players/1` and looks like (in JSON): 
````
{
    "id": "1",
    "name": "Rosa"
}
````
* Use he following JSON pattern with POST on `com.staffbase.communigame/players` to create a new Player.<br>
```
{
    "name": "Ernst"
}
```
* All scores can be called through GET `com.staffbase.communigame/scores`<br>
* A specific score can be called through GET `com.staffbase.communigame/scores/{id}` and looks like (in JSON):

````
{
    "id": "1",
    "playerId": "1",
    "points": "1233"
}
````
* Use the following JSON pattern with POST on `com.staffbase.communigame/scores` to create a new Score.<br>
```
{
    "playerId": "2",
    "points": 745
}
```
* Using GET on `com.staffbase.communigame/allscoresof/{name}` returns a list with all scores of a specific name. <br>
* Using GET on `com.staffbase.communigame/scores/top10` returns a list of the 10 best scores. <br> 

The tables will be linked so that the name corresponding to the playerId will be used.<br>
This also means that, when posting a new score, a check must be done if a name already exists.<br>
If not, the name will be added to the player table and will be given an id.<br>
When a player is deleted, all scores corresponding to it must be deleted.<br>
<details>
<summary>To Do</summary>
* !!!! CORS restrictions mudt be updated !!!! <br>
<del>*  How, when the scores are called for the scoreboard, will the names
    corresponding to the playerIds be called and displayed?</del> <br>
* When posting new score, automatically look up playerId corresponding to new name. <br>
</details>
<details>
<summary>Service interface and classes</summary>
The interface is like a contract between the implementation and the outside world. 
Its purpose is to define the methods that will be available to the outside world and the inputs and outputs 
of those methods, without actually providing the implementation details.

By separating the interface from the implementation, you can change the implementation without affecting the 
outside world, as long as the new implementation still adheres to the interface contract. This is called the
"dependency inversion principle".

In other words, by having a separate implementation class, you can change the implementation details without affecting 
the code that uses the interface. It also makes it easier to test the implementation separately, as you can test each
method in isolation without worrying about the implementation details of other methods.
</details>