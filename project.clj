(defproject clojarmeria "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [com.linecorp.armeria/armeria "0.87.0"]
                 #_[com.linecorp.armeria/armeria-grpc "0.87.0"]
                 [com.linecorp.armeria/armeria-logback "0.87.0"]
                 #_[com.linecorp.armeria/armeria-thrift "0.87.0"]
                 #_[ring/ring-json "0.4.0"]
                 [cheshire "5.8.1"]
                 [funcool/promissum "0.3.3"]
                 ]
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.8" "-source" "1.8"]
  :repl-options {:init-ns clojarmeria.core})
