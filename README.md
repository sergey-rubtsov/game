The prototype of online multiplayer Tic-tac-toe game, but with a little twist:
the size of the play field is configurable between 3x3 and 10x10.
Also the symbols (usually O and X) are configurable. 
And also it should be for 3 players instead of just 2 (the third player is AI).

To play, first run ServerApplication and then two instances of ClientApplication.

Server url also should be configured.

The solution uses Swing as frontend and Spring Web Sockets as backend application.

To configure project don't forget to enable Lombok processing.
To play build server application with gradle.
And run server application with gradle task 'bootRun'.
Then run two ClientApplication instances (allow parallel run if needed) and play game.