# SimpleQueue - Queues player when the server is full

# [Download here](https://github.com/0blu/SimpleQueue/releases)

SimpleQueue is a plugin that will automatically queue players when the server is full.

There are also really neat features like a prioritized player list, reserved slots and allowing players to join the server even if there are no slots left.

You are also not even required to have a permission system to add prioritized players, see [Config](#config).

### Content
- [Screenshot](#screenshot)
- [Commands](#commands)
- [Permissions](#permissions)
- [Config](#config)

### Issue tracker: https://github.com/0blu/SimpleQueue/issues

## Screenshot
![QueuePreview](https://i.imgur.com/UQXZg5U.png)


## Commands
Everthing can also be read in the [plugin.yml](./src/main/java/resources/plugin.yml)
- [/sqversion](#sqversion)
- [/sqqueue](#sqqueue)
- [/sqlist](#sqlist)
- [/sqadd](#sqadd)
- [/sqremove](#sqremove)

### ``/sqversion``

**Usage**: ``/sqversion``<br />
**Permission**: [simplequeue.sqversion](#simplequeuesqversion)<br />
Displays version of SimpleQueue Plugin

### ``/sqqueue``

**Usage**: ``/sqqueue``<br />
**Permission**: [simplequeue.sqqueue](#simplequeuesqqueue)<br />
Lists current players in queue

### ``/sqlist``

**Usage**: ``/sqlis``<br />
**Permission**: [simplequeue.sqlist](#simplequeuesqlist)<br />
Lists all players in the prioritizedPlayers.yml file

### ``/sqadd``

**Usage**: ``/sqadd [player]``<br />
**Permission**: [simplequeue.sqadd](#simplequeuesqadd)<br />
Adds a player to the prioritizedPlayers.yml file

### ``/sqremove``

**Usage**: ``/sqremove [player]``<br />
**Permission**: [simplequeue.sqremove](#simplequeuesqremove)<br />
Removes a player from the prioritizedPlayers.yml file



## Permissions
- [simplequeue.ignoreslotlimit](#simplequeueignoreslotlimit)
- [simplequeue.prioritized](#simplequeueprioritized)
- [simplequeue.sqversion](#simplequeuesqversion)
- [simplequeue.sqqueue](#simplequeuesqqueue)
- [simplequeue.sqlist](#simplequeuesqlist)
- [simplequeue.sqadd](#simplequeuesqadd)
- [simplequeue.sqremove](#simplequeuesqremove)

### ``simplequeue.ignoreslotlimit``
Allows the user to connect to the server even if the slot limit is reached

### ``simplequeue.prioritized``
Puts the user in front of normal users in the queue and uses reserved slots

### ``simplequeue.sqversion``
Grants access to the [/sqversion](#sqversion) command - Displays version of SimpleQueue Plugin

### ``simplequeue.sqqueue``
Grants access to the [/sqqueue](#sqqueue) command - Lists current players in queue

### ``simplequeue.sqlist``
Grants access to the [/sqlist](#sqlist) command - Lists all players in the prioritizedPlayers.yml file

### ``simplequeue.sqadd``
Grants access to the [/sqadd](#sqadd) command - Adds a player to the prioritizedPlayers.yml file

### ``simplequeue.sqremove``
Grants access to the [/sqremove](#sqremove) command - Removes a player from the prioritizedPlayers.yml file


## Config

### ``config.yml``

Can also be read in the [config.yml](./src/main/java/resources/defaultConfig/config.yml)

#### ``reservedSlots``
```yaml
reservedSlots: 1
```

**Number**: How many slots should be saved for prioritized players.

#### ``allowPrioritizedPlayersViaConfig``
```yaml
allowPrioritizedPlayersViaConfig: true
```
**Boolean**: If prioritized players can be defined by the ``prioritizedPlayers.yml``



#### ``mustReconnectWithinSec``
```yaml
mustReconnectWithinSec: 120
```
**Number**: How long (in seconds) the place in the queue will be guaranteed.


#### ``kickMessageQueued``
```yaml
kickMessageQueued: |2-

  &cThe server is currently full!&r

  You are in the queue.
  Position &a%d/%d&r.
  &7Please reconnect within the next &6%d sec&7 to keep your position.&r

```
**String(Multiline)**: The message if a player connects but is still in the queue.

'``&``' can be used as an alternate color code

Meaning of the ``%d``:
1. players current queue position
2. the number of all users in the queue
3. ``mustReconnectWithinSec``

### ``prioritizedPlayers.yml``

Can also be read in the [config.yml](./src/main/java/resources/defaultConfig/prioritizedPlayers.yml)

#### ``prioritizedPlayers``
```
prioritizedPlayers: []
```

**List of OfflinePlayers**: List of offline players. Shouldn't be changed by hand! 

---

License: [Apache License 2.0](./LICENSE.txt)

