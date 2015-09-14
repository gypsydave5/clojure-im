(ns instant-messenger.core
  (require [clojure.java.io :as io])
  (import [java.io StringWriter]
          [java.net Socket])
  (:gen-class))

(defn send-request
  "Sends an HTTP GET request to the specified host, port, and path"
  [host port path]
  (with-open [sock (Socket. host port)
              writer (io/writer sock)
              reader (io/reader sock)
              response (StringWriter.)]
    (.append writer (str "GET " path "\n"))
    (.flush writer)
    (io/copy reader response)
    (str response)))

(defn receive
  "Read a line of textual data from the given socket"
  [socket]
  (.readLine (io/reader socket)))

(defn send
  "Send the given string message out over the given socket"
  [socket msg]
  (let [writer (io/writer socket)]
      (.write writer msg)
      (.flush writer)))

;; (defn serve [port handler]
;;   (with-open [server-sock (ServerSocket. port)
;;               sock (.accept server-sock)]
;;     (let [msg-in (receive sock)
;;           msg-out (handler msg-in)]
;;       (send sock msg-out))))

(defn keep-reading-lines [writer]
  (let [line (read-line)]
    (.append writer line)
    (.flush writer)
    (when (not= line "exit")
      (recur writer))))

(defn -main [& [host port]]
  (prn host port)
  (with-open [sock (Socket. host (Integer. port))
              writer (io/writer sock)
              reader (io/reader sock)
              response (StringWriter.)]
    (keep-reading-lines writer)))
