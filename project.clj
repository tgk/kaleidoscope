(defproject kaleidoscope "0.1.0"
  :description "A predicate dispatch library for Clojure."
  :url "http://github.com/tgk/kaleidoscope"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [com.keminglabs/cljx "0.3.0"]
            [lein-release "1.0.5"]]
  :lein-release {:scm :git
                 :deploy-via :shell
                 :shell ["lein" "deploy" "clojars"]}
  :jar-exclusions [#"\.cljx"]
  :cljx {:builds [{:source-paths ["src/"]
                   :output-path "target/generated"
                   :rules :clj}

                  {:source-paths ["src/"]
                   :output-path "target/generated"
                   :rules :cljs}]}
  :hooks [cljx.hooks]
  :source-paths ["src"  "target/generated"]
  :cljsbuild {:builds [{:source-paths ["target/generated"]
                        :compiler {:output-to "resources/public/build/deps.js"
                                   :output-dir "resources/public/build"
                                   :optimizations :none}}]})
