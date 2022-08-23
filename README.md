# SimpleLaser

A lightweight spigot plugin to create per-player guardian laser in minecraft

## Command

There is only one command : /laser
<br>
It works with named arguments.
<br>
Arguments are : (Schema : 'argument-name' - description - argument type)

- '-s' - the start point of the laser - position
- '-e' - the end point of the laser - position
- '-d' - the distance where players see the laser - integer
- '-t' - duration of the laser in seconds (-1 for infinite duration) - decimal number

Optional arguments are:
- '-em' - the final position of the end point of the laser - position
- '-et' - duration of the movement of the end position - decimal number

Notes:

- A position is built with 3 decimal numbers
- There is no order for arguments (i.e. '-d' argument can be before '-s' argument)
- Due to minecraft limitations, the maximum precision of the duration is only 1/20 sec
- To move a position you have to specify a movement position AND a duration. 

Example:

```
/laser -s 0 50 0 -e 10 60 10 -d 50 -t 10.2
```

This command will create a laser that start in (0;50;0) and end in (10;60;10), visible in a range of 50 blocks and will
disappear in 10.2 seconds.

```
/laser -s 0 50 0 -e 10 60 10 -d 50 -t 10.2 -em 10 50 0 -et 10
```

This command will create a laser that start in (0;50;0) and end in (10;60;10), visible in a range of 50 blocks and will
disappear in 10.2 seconds. The end point will move to (10;50;0) in 10 seconds.