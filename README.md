# SurviveGame
## Main Idea
Playable sinble-player game. Objective is to survive and find the exit, but first you must go on an adventure to find the hidden key! Dodge enemies and search the dungeon for food to survive and get out!
Created a top-view, 2d platformer game using only Java and the Std Draw class from Princeton. Tile Engine and custom pictures used to represent moving characters.
![Gameplay](https://github.com/ryan-van/SurviveGame/images/gameplay.png)

## Implementation Concepts
### Randomized Map
Used a player input seed to randomly generate the map. Created random rooms and connected hallways to each room later on.
### Player and Enemies
Takes in keyboard input to move the character and programmed enemies to move randomly within a certain restricted room. Mouse hover to reveal certain statistics and information about board.
### Hunger and Food
Players move around and lose one tick of health each movement. Find food to replenish health.
### Door and Key
Escape from the dungeon by finding the randomly placed key and then finding the randomly placed door.
### Fog of War
Created fog of war through rendering each frame and saving text tiles to show previously seen lit up tiles. Ray Casting algorithm used to scan in a circle and reveal tiles based off of specific hunger level.
### Save File
Can save the file using Java Serializable class, saving each class as a text file that is replaced in the outermost folder.
