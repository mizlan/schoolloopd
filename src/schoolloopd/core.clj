(ns schoolloopd.core
  (:require [schoolloopd.defaults :as d]
            [schoolloopd.fetcher :as fetch]
            [schoolloopd.updater :as upd]
            [schoolloopd.notifier :as n]))

(defn check
  ([] (check {:prefix d/sl-prefix
              :username d/sl-username
              :password d/sl-password}))
  ([opts]
   (let [existing (upd/load-data)
         data (fetch/fetch opts)]
     (when-not (= existing data)
       (n/notify {:old existing
                  :new data}))
     (upd/save-data data))))

(comment
  (while true
    (check)
    (Thread/sleep 60000))
  )
