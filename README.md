[![Clojars Project](https://img.shields.io/clojars/v/defunkt/revolt-flyway-task.svg)](https://clojars.org/defunkt/revolt-flyway-task)

# Database migrations with Flyway and Revolt

Assuming `resources/db/migrations/postgres` to be a path where flyway migration files are located.

## deps.edn

``` clojure
{:paths ["resources"]
 :aliases {:dev {:extra-deps  {defunkt/revolt {:mvn/version "0.1.5"}
                               defunkt/revolt-flyway-task {:mvn/version "0.0.1"}
                               org.postgresql/postgresql {:mvn/version "42.1.4"}}
                 :main-opts   ["-m" "revolt.bootstrap"]}}}
```

## revolt.edn

``` clojure
:revolt.migrations.task/flyway {:jdbc-url "jdbc:postgresql://localhost:5432/template1"
                                 :locations ["db/migrations/postgres"]
                                 :user "postgres"
                                 :action :info}
```

## using a task with REPL

`clj -A:dev -p rebel`

``` clojure
(require '[revolt.task :as t])
(require '[revolt.migrations.task :as migrations])
(t/require-task ::migrations/flyway)
```

``` clojure
;; to see migrations status
(flyway)

;; to apply pending migrations
(flyway {:action :migrate})

;; to clean a schema
(flyway {:action :clean})
```

## using a task from command-line

`clj -A:dev -t revolt.migrations.task/flyway`

`clj -A:dev -t revolt.migrations.task/flyway:action=migrate`

