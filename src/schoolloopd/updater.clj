(ns schoolloopd.updater
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]))

(def save-path (fs/expand-home "~/.schoolloopd_info"))

(defn serialize-to-file
  [obj file-path]
  (with-open [wr (io/writer file-path)]
    (.write wr (pr-str obj))))

(defn save-data [data]
  (serialize-to-file data save-path))

(defn load-edn
  "General utility function to load edn from a source"
  ([source] (load-edn source false))
  ([source debug-log]
   (try
     (with-open [r (io/reader source)]
       (edn/read (java.io.PushbackReader. r)))
     (catch java.io.IOException e
       (if debug-log
         (printf "Could not open %s: %s\n" source (.getMessage e)))
       nil)
     (catch RuntimeException e
       (if debug-log
         (printf "Parse error for %s: %s\n" source (.getMessage e)))
       nil))))

(defn load-data
  "Load the cache, which should be a map. Assume anything other
  than a map is a failure."
  ([] (load-data save-path))
  ([source]
   (let [obj (load-edn source)]
     (if (instance? clojure.lang.PersistentHashMap obj)
       obj
       nil))))
