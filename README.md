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

Notes:

- A position is built with 3 decimal numbers
- There is no order for arguments (i.e. '-d' argument can be before '-s' argument)
- Due to minecraft limitations, the maximum precision of the duration is only 1/20 sec

Example:

```
/laser -s 0 50 0 -e 10 60 10 -d 50 -t 10.2
```

This command will create a laser that start in (0;50;0) and end in (10;60;10), visible in a range of 50 blocks and will
disappear in 10.2 seconds.