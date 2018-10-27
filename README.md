# berrysweeper

Development Mode:
```
lein run server-headless
```

For production deployment, use ring uberjar instead of regular uberjar:
```
heroku config:set -a berrysweeper LEIN_BUILD_TASK="ring uberjar"
```
