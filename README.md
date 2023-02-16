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
* The following JSON pattern can be used with POST to create a new Player.<br>
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
* The following pattern can be used with POST to create a new Score.<br>
```
{
    "playerId": "2",
    "points": 745
}
```
* Using GET on `com.staffbase.communigame/allscoresof/{name}` returns a list with all scores of a specific name. <br>
* Using GET on `com.staffbase.communigame/scores/top20` returns a list of the 20 best scores. <br> 

The tables will be linked so that the name corresponding to the playerId will be used.<br>
This also means that, when posting a new score, a check must be done if a name already exists.<br>
If not, the name will be added to the player table and will be given an id.<br>
When a player is deleted, all scores corresponding to it must be deleted.<br>
<details>
<summary>To Do</summary>
<del>*  How, when the scores are called for the scoreboard, will the names
    corresponding to the playerIds be called and displayed?</del> <br>
* When posting new score, automatically look up playerId corresponding to new name. <br>
</details>