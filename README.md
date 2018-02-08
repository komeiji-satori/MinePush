# MinePush
A Minecraft Server Push Message Queue Based On Redis

## Examples
```
<?php
$redis = new Redis();
$redis->connect("127.0.0.1", 6379);
$redis->lpush("message_queue", json_encode([
	'username' => $_GET['name'],
	'msg' => $_GET['msg'],
	'time' => time(),
]));
```
