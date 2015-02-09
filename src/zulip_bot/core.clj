;; If using sandbox is giving security exceptions, you likely need a ~/.java.policy file.

(ns zulip-bot.core
  (:refer-clojure :exclude [filter into map merge partition partition-by reduce take])
  (:require [clojure-zulip.core :as zulip]
            [clojure.core.async :refer :all]
            [clojure.data.json :as json]
            [environ.core :refer [env]])
  (:use [clojail.core :only [sandbox]]
        [clojail.testers :only [secure-tester]]))

(def bot-attention-regex #"^!clj (.+)")
(def sb (sandbox secure-tester))

(def conn (zulip/connection (env :connection-info)))
(def new-channel (zulip/add-subscriptions conn ["bot-test" "clojure"]))

(defn process [events]
  (println events)
  (let [message (first (get events :events))
        message-body (get-in message [:message :content])
        message-subject (get-in message [:message :subject])
        channel (get-in message [:message :display_recipient])]
    (when-let [eval-text (second (re-find bot-attention-regex message-body))]
      (let [eval-input (read-string eval-text)
            eval-output (try (sb eval-input)
                             (catch Exception e (str "caught exception: " (.getMessage e))))]
          (zulip/send-stream-message conn channel message-subject eval-output))))) ;fixme


(defn init []
  (thread
    (loop []
      (let [register-chan (zulip/register conn ["message"])
            {:keys [:queue_id :max_message_id :last_event_id]} (<!! register-chan)
            message-chan (zulip/events conn queue_id last_event_id)]
        (println "loop")
        (when-let [messages (<!! message-chan)]
          (process messages)
          (recur))))))

(init)


