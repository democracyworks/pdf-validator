(ns pdf-validator.validate
  "PDF validation functions

  This is a rough translation of the example code found in the PDFBox docs:
  https://pdfbox.apache.org/1.8/cookbook/pdfavalidation.html"
  (:import [org.apache.pdfbox.preflight.parser PreflightParser]
           [org.apache.pdfbox.preflight.exception SyntaxValidationException]))

(defn validate-pdf* [filename]
  (try
    (let [parser (doto (PreflightParser. filename) .parse)
          document (doto (.getPreflightDocument parser) .validate)
          result (.getResult document)]
      (.close document)
      result)
    (catch SyntaxValidationException e
      (.getResult e))))

(defn code->category [code]
  (case (subs code 0 1)
    "1" "Syntax"
    "2" "Graphic"
    "3" "Font"
    "4" "Transparency"
    "5" "Annotation"
    "6" "Action"
    "7" "Metadata"
    "Unknown"))

(defn validate-pdf
  "Validate a pdf, returning a map of information.

  Keys:

  `:filename` -- the filename that was validated

  `:valid?`   -- `true` or `false`

  `:errors`   -- a seq of errors (if any)

  Error keys:

  `:page`     -- the page where this error occurred (if known)

  `:code`     -- the error code (`X.Y.Z`) where `X` is the category, `Y` is the
  sub-category, and `Z` is the specific cause.

  `:category` -- the main error category

  `:details`  -- textual description of the error
  "
  [filename]
  (let [result (validate-pdf* filename)]
    {:filename filename
     :valid? (.isValid result)
     :errors (map (fn [e]
                    {:code (.getErrorCode e)
                     :category (code->category (.getErrorCode e))
                     :details (.getDetails e)
                     :page (some->> (.getPageNumber e) inc)
                     :hash-code (java.util.UUID/randomUUID)})
                  (.getErrorsList result))}))
