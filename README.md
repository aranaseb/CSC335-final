# CSC335-final
Software Overview:
  Our project follows an MVC design, where the model stores gamestate by tracking the position and values of tiles, and the
  controller provides game logic, such as merging and score. The view consists of GUI, which provides a menu to the player
  on launch and Window2048, which provides a visual design for the board and controls to the player; it also includes
  Leaderboard, which tracks scores achieved by players and stores them in a text file.

  Board.java:
    This file tracks gamestate by using a 2D array of integers to track game tiles. It also keeps track of which tiles are
    empty to simplify logic for merging and creating random tiles. It provides a setter and getter for positions on the board
    as well as a method that moves a tile from one position to another. It also provides a size method, a method to tell if
    the board is full, and a method to show if 2048 is contained. Finally, it provides a method to return a random empty
    index within the board.

  Controller.java:
    This file handles game logic, including merging, score, game status, and placing random tiles. It provides methods for
    passing important information, like size and the tile at an index, upwards. It also contains a method to shift all the tiles
    up, down, left, or right, handling all of the logic involved with merging along the way. Lastly, it provides a method which
    tells the user whether they have won, lost, or if there are still possible moves on the board.

  Direction.java:
    This file simply provides an enum describing the requested direction of a move. It is used in some of the logic checks
    involved in merging tiles.

  GameStatus.java
    This file provides an enum describing the state of the game, that is whether the player has won, lost, or the game is still
    in progress.

  Leaderboard.java:
    This file contains methods allowing the user to store a sorted list of scores, which is preserved between launches of the
    program. It's constructor reads in a file of scores and creates a sorted list of them. It provides a method to add a score
    after a game is finished, and then there is a method to save the new scores back into the file after the program has finished.

  GUI.java:
    This file contains a main menu for the user to select between viewing the leaderboard and playing the game. When play is
    selected, it prompts the user for a variable board size, and launches the game. When leaderboard is selected, it loads
    the information from leaderboard.txt and displays it to the user, showing how their previous scores match up against each
    other and the scores provided.
    
  Window2048.java
    This file contains the visual design of the game, initializing a Christmas-themed board and allowing the user to play with
    both WASD and the arrow keys. After winning or losing a game, it returns to the main menu.

Instructions:
  The main menu is launched by running GUI.java. Pressing leaderboard will show you the top ten scores, just ours if you haven't
  played a game yet. From this menu, there is a back button to return to the main menu. Pressing play prompts you to select a size
  for the board. The slider can be used to make this selection. After pressing okay, the game will launch, giving you an
  interface on which the game can be played. You can use the WASD keys, or the arrow keys. Once you run out of moves, or make
  2048 on the board, the game will return to the main menu, and put your score into the leaderboard.
