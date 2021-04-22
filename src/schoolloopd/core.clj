(ns schoolloopd.core
  (:require [schoolloopd.defaults :as defaults]
            [schoolloopd.fetcher :as fetch]
            [schoolloopd.updater :as upd]))

(comment
  (fetch/fetch {:prefix defaults/sl-prefix
                :username defaults/sl-username
                :password defaults/sl-password})
  )
