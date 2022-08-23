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

- '-sm' - the final position of the start point of the laser - position
- '-st' - duration of the movement of the start position - decimal number
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

## Build

You need Maven with GitHub access and a jdk.
If you don't know how to give GitHub access to maven,
check [this](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-with-a-personal-access-token)
.
If you don't know how to create a personal token,
check [this](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
.

- Use ``mvn clean install`` at the root of the project to build ShopChest artefact.
- After the build succeeded, the ShopChest.jar is found in the ``/plugin/target/`` folder.