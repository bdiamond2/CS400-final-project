# CS400-final-project
This is my final project from CS400: Programming III, which I took at UW-Madison in 2022 with Daniel Finer.

## Run
To start the game, use the provided Makefile and run `make` or `make run` from the command line. When you're done, you can run `make clean` to remove compiled Java class files.

## Description
The program is a text-based game called "Escape from Quarkslurp Alpha," in which the player has to find the shortest path between two worlds in each level.

> You are Captain Josephine Mue'mah, a fugitive who has just
escaped the brutal mining world of Quarkslurp Alpha.
> 
> Paramilitary forces of the evil Musk Corporation are in
pursuit, and will shoot down your ship once they are within
range.
> 
> Your mission is to evade the authorities throughout several
hostile sectors until you can reach the safety of your home
system. Each system has a warp gate at the end that will lead
you to the next system. Jumping from planet to planet, you
must choose the shortest route through the sector in order to
avoid being intercepted. If you choose a suboptimal route,
you will face certain death.
 
The levels are fetched from an external data source (and are thus user-programmable), so the game uses Dijkstra's Algorithm to determine if the player's chosen path is the shortest possible.

## Remarks
All game data, including location names and descriptions, level designs, and the prologue crawl are stored in the `/data` directory. The answer key for the game levels is provided there in `Answer key.txt`.

It's worth noting that the format I used for storing and fetching the data was...entirely improvised and homegrown. So if I were making this game today, I would probably use some standard config format like JSON or YAML.

## Attribution
All source code and data files were written from scratch by me with a couple exceptions:

- **CS400Graph.java**: This is a Java class taken from a completed homework assignment that contained "starter code" provided by the instructor, so only part of this was written by me.
- **GraphADT.java**: This is a Java interface that was provided to the class, so I don't believe I wrote any of this.

The inspiration for level 3 can be found here:
[The Universe I, II & III | Tim and Eric Awesome Show, Great Job! | Adult Swim](https://www.youtube.com/watch?v=FYJ1dbyDcrI)
