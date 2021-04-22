(ns schoolloopd.notifier
  (:require [clojure.java.shell :as shell]))

;; TODO: put some common notification methods here

(defn calc-deltas [old new]
  (merge-with - new old))

(defn terminal-notifier-dispatch [title msg]
  (shell/sh "terminal-notifier"
            "-title" title
            "-message" msg
            "-appIcon" "https://www.freeiconspng.com/uploads/emergency-alert-icon-alert-icon-8.png"))

(defn notify [old new]
  (let [deltas (calc-deltas old new)
        changed (->> deltas (remove #(= 0 (second %))))]
    (println (pr-str changed))
    (terminal-notifier-dispatch
     "Schoolloop grades changed"
     (pr-str changed))))

(comment
  (notify {:a 1 :b 2} {:a 4 :b 3})
  (->> {:a 1 :b 2}
       str)
  )
