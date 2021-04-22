(ns schoolloopd.notifier
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]))

;; TODO: put some common notification methods here

(defn calc-deltas [old new]
  (merge-with - new old))

(defn terminal-notifier-escape [text]
  (-> text
      ;; (str/replace "[" "\\[")
      ;; (str/replace "]" "\\]")
      (str/replace "(" "\\(")
      ;; (str/replace ")" "\\)")
      ))

(defn terminal-notifier-dispatch [title msg]
  (shell/sh "terminal-notifier"
            "-title" title
            "-message" (terminal-notifier-escape msg)
            "-appIcon" "https://www.freeiconspng.com/uploads/emergency-alert-icon-alert-icon-8.png"))

(defn notify [{:keys [old new]}]
  (let [deltas (calc-deltas old new)
        changed (->> deltas (remove #(= 0 (second %))))]
    (println (pr-str changed))
    (terminal-notifier-dispatch
     "Schoolloop grades changed"
     (pr-str changed))))
