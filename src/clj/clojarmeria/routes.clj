(ns clojarmeria.routes
  (:require [cheshire.core :as json]
            [clojure.tools.logging :as log]
            [promissum.core :as p])
  (:import (com.linecorp.armeria.common HttpStatus HttpResponse HttpRequest AggregatedHttpRequest MediaType)
           (io.netty.handler.codec.http QueryStringDecoder)
           (com.linecorp.armeria.server ServiceRequestContext)
           (java.util.concurrent CompletableFuture)
           (clojarmeria.support FormParams)))

(defn form-params [^AggregatedHttpRequest agg-req content-type]
  (FormParams/formParams agg-req content-type))

(defn json-response [^HttpStatus status m]
  (log/debug "Writing JSON:" m)
  (HttpResponse/of
    status
    MediaType/JSON_UTF_8
    (-> m json/generate-string)))

(defn read-form-params [v]
  {:first (-> v (.getAll "first"))
   :last  (-> v (.getAll "last"))})

(def ^:dynamic routes
  [{:path    "/greet/:name/:id"
    :handler (fn [^ServiceRequestContext ctx ^HttpRequest req]
               (let [method (-> ctx .method .name)
                     name (-> ctx (.pathParam "name"))
                     id (-> ctx (.pathParam "id"))
                     query (-> ctx .query)
                     content-type (-> req .contentType)
                     ^CompletableFuture agg-f (-> req .aggregate)
                     parameters (-> (QueryStringDecoder. query false) .parameters)
                     title (-> parameters (.get "title") first)
                     sex (-> parameters (.get "sex") first)
                     age (-> parameters (.get "age") first Integer/parseInt)
                     #_post-params #_(-> agg-f (p/then #(form-params % content-type)))]
                 (log/debug "Handling request with fresh new routes ######")
                 (HttpResponse/from
                   (-> agg-f
                       (p/then #(form-params % content-type))
                       (p/then
                         (fn [v] (json-response HttpStatus/OK
                                                (merge {:method   method
                                                        :id       id
                                                        :name     name
                                                        :addition {:title title :age age :sex sex}}
                                                       (read-form-params v)))))
                       (p/catch #(do (log/error % "I made a ERROR !!!")
                                     (json-response HttpStatus/INTERNAL_SERVER_ERROR
                                                    {:status 500 :reason (-> % .getMessage)})))))))}])
