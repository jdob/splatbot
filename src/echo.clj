(require 'comm)

(defn echo [splatmsg]
  (println (str "ECHO :: " (:message splatmsg)))
)
