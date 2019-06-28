(ns clojarmeria.core
  (:require [cheshire.core :as json]
            [clojure.tools.logging :as log]
            [clojarmeria.armeria]
            [clojarmeria.routes :as routes]
            )
  (:import (com.linecorp.armeria.server ServerBuilder Server ServiceRequestContext)
           (com.linecorp.armeria.common HttpRequest HttpResponse HttpStatus MediaType)
           (io.netty.handler.codec.http QueryStringDecoder)
           (clojarmeria.armeria HttpServiceRecord)))

(def ^:dynamic server-instance (atom nil))

(defn stop-server []
  (when @server-instance
    (-> @server-instance .close #_.stop #_.get)
    (reset! server-instance nil)))

(defn config-http-services [^ServerBuilder sb routes]
  (doseq [route routes] (-> sb (.service ^String (:path route)
                                         (HttpServiceRecord. (:handler route)))))
  sb)

(defn start-server
  []
  (let [^Server server (-> (ServerBuilder.)
                           (.http 8080)
                           (config-http-services routes/routes)
                           #_(.service "/greet/:name"
                                     (HttpServiceRecord. (fn [^ServiceRequestContext ctx ^HttpRequest req]
                                                           (let [name (-> ctx (.pathParam "name"))
                                                                 query (-> ctx .query)
                                                                 content-type (-> req .contentType)
                                                                 agg-req (-> req .aggregate)
                                                                 parameters (-> (QueryStringDecoder. query false) .parameters)
                                                                 title (-> parameters (.get "title") first)]
                                                             (log/debug "Handling request ######")
                                                             (HttpResponse/of HttpStatus/OK MediaType/JSON_UTF_8
                                                                              (-> {:name name :title title} json/generate-string)))
                                                           ))
                                     )
                           (.build))]
    (reset! server-instance server)
    (-> server .start .get)))
