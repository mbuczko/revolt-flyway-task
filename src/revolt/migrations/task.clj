(ns revolt.migrations.task
  (:require [revolt.task :refer [Task create-task make-description]])
  (:import  [org.flywaydb.core Flyway]
            [org.flywaydb.core.internal.info MigrationInfoDumper]))

(defn init-flyway [{:keys [jdbc-url user password baseline-version target-version locations]} init-sqls]
  (if-not jdbc-url
    (println "No :jdbc-url provided in configuration")
    (-> (Flyway.)
        (doto (.setDataSource jdbc-url user password init-sqls))

        (cond-> baseline-version
          (doto (.setBaselineVersionAsString baseline-version)))

        (cond-> target-version
          (doto (.setTargetAsString target-version)))

        (cond-> (seq locations)
          (doto (.setLocations (into-array String locations)))))))

(defn clean [flyway]
  (. flyway clean))

(defn baseline [flyway]
  (. flyway baseline))

(defn validate [flyway]
  (. flyway validate))

(defn repair [flyway]
  (. flyway repair))

(defn migrate [flyway]
  (. flyway migrate))

(defn info [flyway]
  (println (MigrationInfoDumper/dumpToAsciiTable (.. flyway info all))))

(defmethod create-task ::flyway [_ opts classpaths target]
  (let [defaults  {:locations ["db/migrations"]}
        init-sqls (make-array String 0)]
    (reify Task
      (invoke [this input ctx]
        (let [options (merge defaults opts input)
              flyway  (init-flyway options init-sqls)]
          ((condp = (keyword (:action options))
             :clean    clean
             :migrate  migrate
             :validate validate
             :baseline baseline
             info)
           flyway)
          ctx))
      (describe [this]
        (make-description "Database migration actions" "Migrates database using Flyway migrations engine."
                          :jdbc-url "jdbc URL, like \"jdbc:postgresql://<host>:<port>/<database>?<key1>=<value1>&<key2>=<value2>...\""
                          :user "user to use in database connection"
                          :password "password to use in database connection"
                          :action "one of following:\n                         :migrate  - to apply pending database migrations\n                         :validate - to validate migration applied to database\n                         :clean    - to wipe configured schemas completely\n                         :baseline - to introduce Flyway to existing databases by baselining them at a specific version\n                         :info     - to see status of migrations"
                          :target-version "target version up to which Flyway should consider migrations when executing :migrate action"
                          :baseline-version "the version to tag an existing schema with when executing :baseline action"
                          :locations "comma-separated list of locations to scan recursively for migrations (defaults to [\"db/migrations\"])")))))
