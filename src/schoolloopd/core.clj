(ns schoolloopd.core
  (:require [clj-http.client :as client]
            [schoolloopd.defaults :as defaults]
            [schoolloopd.fetcher :as fetch]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [schoolloopd.updater :as upd]))

(comment
  (fetch/fetch {:prefix defaults/sl-prefix
                :username defaults/sl-username
                :password defaults/sl-password})
  )
