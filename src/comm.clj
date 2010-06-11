(defstruct splat-message :message :out)

(defn send-message [out message]
  "Sends the given message to the IRC connection output stream."
  (println message)
  (. out print message)
  (. out flush)
)

(defn join-channel [out channel]
  "Joins the given channel"
  (let [msg (str "JOIN " channel "\r\n")]
   (send-message out msg)
  )
)

(defn join-channels [out channels]
  "Joins the channels in the given list"
  (join-channel out (first channels))
  (if (> (count channels) 1)
    (join-channels out (rest channels))
  )
)
