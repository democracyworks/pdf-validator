(ns pdf-validator.known-errors)

(def ignorable-errors
  [["1.0.3" #"Syntax error, Name too long"]
   ["1.0.6" #"Syntax error, Float is too long or too small"]
   ["1.2.9" #"Body Syntax error, EmbeddedFile entry is present in a FileSpecification dictionary"]
   ["1.4" #"Trailer Syntax error, /XRef cross reference streams are not allowed"]
   ["1.4.7" #"Trailer Syntax error, EmbeddedFile entry is present in the Names dictionary"]
   ["1.4.8" #"Trailer Syntax error, A Catalog shall not contain the OCPProperties entry"]
   ["1.4.9" #"Outlines invalid, Last object on a level isn't the expected /Last"]
   ["1.4.9" #"Outlines invalid, The value of /Prev at .* doesn't point to previous object"]
   ["2.1.3" #"Invalid graphics object, The S entry of the OutputIntent isn't GTS_PDFA1"]
   ["2.1.7" #"Invalid graphics object, The Group dictionary hasn't Group as Type value"]
   ["2.2.1" #"Invalid graphics transparency, Group has a transparency S entry or the S entry is null"]
   ["2.2.2" #"Invalid graphics transparency, Soft Mask must be null or None"]
   ["2.3.2" #"Unexpected value for key in Graphic object definition, Unexpected 'true' value for 'Interpolate' Key"]
   ["2.4.1" #"Invalid Color space, The operator .* can't be used with .* Profile"]
   ["2.4.2" #"Invalid Color space, The operator .* can't be used with .* Profile"]
   ["2.4.3" #"Invalid Color space, The operator .* can't be used without Color Profile"]
   ["2.4.3" #"Invalid Color space, .* default for operator .* can't be used without Color Profile"]
   ["2.4.3" #"Invalid Color space, DestOutputProfile is missing"]
   ["3.1.1" #"Invalid Font definition, .*: some required fields are missing from the Font dictionary: firstChar, lastChar, widths."]
   ["3.1.3" #"Invalid Font definition, .*: FontFile entry is missing from FontDescriptor"]
   ["3.1.4" #"Invalid Font definition, .*: The Charset entry is missing for the Type1 Subset"]
   ["3.1.5" #"Invalid Font definition, .*: The Encoding is invalid for the NonSymbolic TTF"]
   ["3.1.5" #"Invalid Font definition, .*: Symbolic TrueType font has more than one 'cmap' entry"]
   ["3.1.9" #"Invalid Font definition, .*: mandatory CIDToGIDMap missing"]
   ["3.1.11" #"Invalid Font definition, .*: The CIDSet entry is missing for the Composite Subset"]
   ["3.3.1" #"Glyph error, The character code .* in the font program .* is missing from the Character Encoding"]
   ["4.1.2" #"Transparency error, CA entry in a ExtGState is invalid"]
   ["4.1.3" #"Transparency error, BlendMode value isn't valid (only Normal and Compatible are authorized)"]
   ["5.2.2" #"Forbidden field in an annotation definition, Flags of Link annotation are invalid"]
   ["6.2.1" #"Action is forbidden, Javascript entry is present in the Names dictionary"]
   ["7.1" #"Error on MetaData, Invalid array type"]
   ["7.1" #"Error on MetaData, Type 'originalDocumentID' not defined in http://ns.adobe.com/xap/1.0/sType/ResourceRef#"]
   ["7.1.1" #"Error on MetaData, No type defined for \{http://ns.adobe.com/xap/1.0/mm/\}subject"]
   ["7.2" #"Error on MetaData, Title present in the document catalog dictionary can't be found in XMP information"]
   ["7.3" #"Error on MetaData, Cannot find a definition for the namespace .*"]
   ["7.11" #"Error on MetaData, PDF/A identification schema .* is missing"]])

(def known-bad-errors
  [["5.2.2" #"Forbidden field in an annotation definition, Flags of Widget annotation are invalid"]
   ["5.3.1" #"Invalid field value in an annotation definition .*"]
   ["6.2.4" #"Action is forbidden, .* must not be used in a widget annotation"]
   ["6.2.5" #"Action is forbidden, The action ResetForm is forbidden"]])

(defn- matches? [[code details-regex] error]
  (and (= code (:code error))
       (re-matches details-regex (:details error))))

(defn ignorable? [error]
  (some #(matches? % error) ignorable-errors))

(defn known-bad? [error]
  (some #(matches? % error) known-bad-errors))
