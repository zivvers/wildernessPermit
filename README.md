# albumBuyerCrawler

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
sudo docker exec -it $(sudo docker ps --filter NAME=albumbuyercrawler_worker -aq) /bin/sh
```

Useful maven commands:
- compile: `mvn scala:compile`
- (compile and) run using argument supplied website: ` mvn scala:run -DaddArgs=<website>`
