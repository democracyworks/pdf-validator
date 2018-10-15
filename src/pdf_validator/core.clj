(ns pdf-validator.core
  "PDF Validator

  Usage: lein run -- [options] filename...

  Options:

    -v    List simple error details
    -vv   List full error details
    -vvv  List each error individually"
  (:require [pdf-validator.validate :as validate]
            [clojure.string :as str]))

(defn print-simple-table
  [headers fmt rows]
  (println (apply format fmt headers))
  (doseq [row rows]
    (println (apply format fmt (map #(or (row %) "") headers)))))

(defn format-errors
  "Sort, group, and count errors in preparation for printing."
  [{:keys [map-fn group-fn]} errors]
  (if (and (= identity map-fn)
           (= identity group-fn))
    errors
    (->> errors
         (map map-fn)
         (group-by group-fn)
         (map (fn [[_ e-list]] (assoc (first e-list) :count (count e-list))))
         (sort-by (juxt :page :code :details)))))

(defn simplify-details
  "Change the `:details` key of an error to be the part before the comma."
  [e]
  (update e :details #(or (re-find #"^[^,]+" %) %)))

(defn headers->fmt [headers]
  (let [widths {:page "5"
                :code "-5"
                :category "-12"
                :count "6"}]
    (->> headers
         (map #(str "%" (widths %) "s"))
         (str/join " | "))))

(def default-formats
  {:group-fn identity
   :map-fn identity
   :headers [:page :code :count :details]})

(def verbosity-formats
  {0 {:map-fn simplify-details
      :group-fn :category
      :headers [:page :category :count]}
   1 {:map-fn simplify-details
      :group-fn :details
      :headers [:page :category :code :count :details]}
   2 {:group-fn (juxt :page :code :details)
      :headers [:page :code :count :details]}
   3 {:headers [:page :code :details]}})

(defn output-format [level]
  (let [f (merge default-formats (verbosity-formats level))]
    (assoc f :fmt (headers->fmt (:headers f)))))

(defn print-validation
  "Print validation result as a table of errors."
  ([result]
   (print-validation result 0))
  ([{:keys [filename valid? errors]} verbosity]
   (if valid?
     (println filename " is a valid PDF/A-1b file")
     (let [{:keys [headers fmt] :as output-fmt} (output-format verbosity)
           formatted-errors (format-errors output-fmt errors)]
       (println filename "is not valid," (count errors) "errors:")
       (print-simple-table headers fmt formatted-errors)))))

(defn validate!
  "Validate a PDF and print a table of errors.

  `verbosity` should be between 0 and 3"
  ([filename]
   (validate! filename 0))
  ([filename verbosity]
   (print-validation (validate/validate-pdf filename) (min verbosity 3))))

(defn help []
  (->> 'pdf-validator.core
       find-ns
       meta
       :doc
       println))

(defn flags->verbosity [flags]
  (->> flags
       (keep (partial re-find #"v+"))
       (map count)
       (reduce +)
       (min 3)))

(defn -main [& args]
  (let [verbosity-flags (filter (partial re-find #"^-+v+$") args)
        files (remove (set verbosity-flags) args)
        verbosity (flags->verbosity verbosity-flags)]
    (when (or (empty? files)
              (some (partial re-find #"^-") files))
      (help)
      (System/exit 1))
    (run! #(validate! % verbosity) files)))
