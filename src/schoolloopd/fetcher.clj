(ns schoolloopd.fetcher
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as str]))


(defn grade-info [x]
  (let [grades (-> (html/select x [:div.percent])
                   first
                   :content
                   first
                   str/trim
                   (str/split #"\n")
                   first)
        classes (->> (html/select x [:td.course])
                     (map :content)
                     first
                     second
                     :content
                     first)]
    [classes grades]))

(defn portal [{:keys [prefix username password]}]
  (let [base-url (format "https://%s.schoolloop.com/portal" prefix)
        resp (client/get (str base-url "/login"))
        data-id (-> resp
                    :body
                    (html/html-snippet)
                    (html/select [:input#form_data_id])
                    first
                    (get-in [:attrs :value]))
        payload {:login_name username
                 :password password
                 :event_override "login"
                 :form_data_id data-id}
        post-url (str base-url "/login?etarget=login_form")
        login-resp (client/post post-url {:cookies (:cookies resp)
                                          :form-params payload
                                          :redirect-strategy :lax})
        ]
    login-resp))

(defn fetch [opts]
  (let [p (portal opts)]
    (->> (map fetch/grade-info
              (-> p
                  :body
                  html/html-snippet
                  (html/select [:table.student_row])))
         flatten
         (apply hash-map))))
