# pdf-validator

A PDF validation tool using PDFBox

## Usage

### From the command line

```bash
# Simple validation (grouped by error category and page)
lein run -- some.pdf

# Group results by page and error details
lein run -- -vv some.pdf

# Show all errors indivudually
lein run -- -vvv some.pdf
```

### From a REPL

```clj
;; Simple validation
pdf-validator.core=> (validate! "some.pdf")
some.pdf is not valid, 372 errors:
:page | :category    | :count
      | Syntax       |      1
      | Annotation   |     15
      | Metadata     |      1
    1 | Graphic      |    353
    3 | Transparency |      2

;; Verbosity level 1
pdf-validator.core=> (validate! "some.pdf" 1)
some.pdf is not valid, 372 errors:
:page | :category    | :code | :count | :details
      | Syntax       | 1.4   |      1 | Trailer Syntax error
      | Annotation   | 5.3.1 |     15 | Invalid field value in an annotation definition
      | Metadata     | 7.1   |      1 | Error on MetaData
    1 | Graphic      | 2.4.3 |    284 | Invalid Color space
    3 | Graphic      | 2.2.1 |     69 | Invalid graphics transparency
    3 | Transparency | 4.1.2 |      2 | Transparency error
```

## License

Copyright © 2018 Democracy Works

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
