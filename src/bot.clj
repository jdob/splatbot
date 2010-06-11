(import '(java.io BufferedReader InputStreamReader))
(import '(java.io PrintWriter OutputStreamWriter))
(import '(java.net Socket))

(require 'comm)
(require 'config)

(defn distribute [msg plugins]
  "Recursively distributes the message each plugin in the list"
  (if (> (count plugins) 0)
    ((first plugins) msg)
  )
  (if (> (count plugins) 1)
    (distribute msg (rest plugins))
  )
)

(defn listen [in out]
  "Repeatedly reads data from the socket and passes to the plugins for handling"
  (let [data (. in readLine)
        msg (struct splat-message data out)
        ]
    (if (. data startsWith "PING") (send-message out "PONG\r\n"))
    (distribute msg plugins)
  )
  (listen in out)
)

(defn connect [host port nick name channels]
  "Connects to the indicated IRC server, joining the specified channels"
  (let [socket (Socket. host port)
        out (PrintWriter. (OutputStreamWriter. (. socket getOutputStream)))
        in (BufferedReader. (InputStreamReader. (. socket getInputStream)))
       ]
    (send-message out (str "NICK " nick "\r\n"))
    (send-message out (str "USER " nick  " 0 * :" name  "\r\n"))
    (join-channels out channels)
    (listen in out)
  )
)
