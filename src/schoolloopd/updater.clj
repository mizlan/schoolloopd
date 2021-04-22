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

(defn load-save-cache
  "Load the cache, which should be a hashset. Assume anything other
  than a hashset is a failure."
  ([] (load-save-cache save-path))
  ([source]
   (let [obj (load-edn source)]
     (if (instance? clojure.lang.PersistentHashSet obj)
       obj
       nil))))
