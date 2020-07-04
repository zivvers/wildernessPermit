# wildernessPermit

Build project:
```
sudo docker-compose build
```

Run project (detached):
```
sudo docker-compose up -d
```

Connect to worker (for development purposes, e.g. run maven commands):
```
sudo docker exec -it $(sudo docker ps --filter NAME=wildernesspermit__worker -aq) /bin/sh
```

Kill the worker:
```
sudo docker kill $(sudo docker ps -aq --filter NAME=wildernesspermit_worker -aq)
```


Useful maven commands:
- compile: `mvn scala:compile`

_below is not currently operational_
- (compile and) run using argument supplied website: ` mvn scala:run -DaddArgs=<website>`
