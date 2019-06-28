(ns clojarmeria.armeria
  (:import (com.linecorp.armeria.server HttpService ServiceRequestContext)
           (com.linecorp.armeria.common HttpRequest SerializationFormat HttpResponse)
           ))

(defrecord HttpServiceRecord [f]
  HttpService
  (^HttpResponse serve [_this ^ServiceRequestContext ctx ^HttpRequest req]
    (try (f ctx req)
         (finally (let [lb (-> ctx .logBuilder)]
                    (when (not (-> lb .isRequestContentDeferred))
                      (-> lb (.requestContent nil nil)))
                    (-> lb (.serializationFormat SerializationFormat/NONE)))))))
