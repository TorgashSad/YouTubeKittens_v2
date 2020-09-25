# YouTubeKittens_v2
This project implements a Telegram bot that send cute animal videos to the client.
YouTube Data API and Telegram Bot API are used in this application
Principally, commands are implemented using two enums impleneting the same interface Command
The first enum SystemCommands is used for system commands like /help, the second one UserCommands is used for user commands, implemented as buttons on a reply keyboard
Every YouTube query is supplemented with a random adjective from the list describing an animal. This is done for sake of diversity of videos.
