(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.9.0"]
                 [org.flywaydb/flyway-core "5.1.1"]
                 [defunkt/revolt "0.1.6"]
                 [adzerk/bootlaces "0.1.13" :scope "test"]])

;; to check the newest versions:
;; boot -d boot-deps ancient

(def +version+ "0.0.1")

(require
 '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)

(task-options!
 pom {:project 'defunkt/revolt-flyway-task
      :version +version+
      :description "Flyway-based migrations task"
      :url "https://github.com/mbuczko/revolt-flyway-task"
      :scm {:url "https://github.com/mbuczko/revolt-flyway-task"}})
