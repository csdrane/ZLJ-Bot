# ZLJ Bot

ZLJ Bot is Zulip chatbot. ZLJ Bot will safely evaluate Clojure code prefixed by `!clj`.

## Usage

Prior to running ZLJ Bot, you must provide your Zulip API key. You can most easily add this information by creating a profiles.clj at the base project directory. Inside, save the following:

```
{:dev {:env {:connection-info {:username "Your-Username"
                               :api-key "Your-API-Key"}}}}
```

Run ZLJ Bot by executing core.clj.

## Safety

ZLJ Bot uses [Clojail](https://github.com/Raynes/clojail) to sandbox user input. Clojail carries the following warning.

```

We can't promise that clojail will be entirely secure at any given time. Clojail uses a blacklist-based sandbox by default, so there will almost always be little holes in the sandbox. Fortunately, that only applies to the Clojure side of things. Since clojail is backed up by the JVM sandbox, even if the Clojure sandbox is broken, I/O still can't be done. Even if clojail breaks, the breaker can't wipe your system unless he has broken the JVM sandbox, in which case he has worked hard and earned his prize.

```

## License

This project is covered under the MIT License. See LICENSE for more information.
