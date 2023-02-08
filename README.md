# CommuniGame-Backend

There are 2 kinds of models; `Player` and `Score`. <br>
Player can be called through `com.example/players/` and looks like (in JSON): 
````
{
    "id": "1",
    "name": "Rosa"
}
````
This pattern, without the id when using a DB, can be used with POST to create a new Player.<br>

<br>
Score can be called through `com.example/scores/{id}` and looks like (in JSON):

````
{
    "id": "1",
    "playerId": "1",
    "points": "1233"
}
````
This pattern can be used with POST to create a new Score.<br>
The tables will be linked so that the name corresponding to the playerId will be used.<br>
This also means that, when posting a new score, a check must be done if a name already exists.<br>
If not, the name will be added to the player table and will be given an id.<br>
When a player is deleted, all scores corresponding to it must be deleted.<br>
<details>
<summary>To Do</summary>
*  
</details>