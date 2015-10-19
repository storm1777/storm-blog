# storm-blog


An example blog built on Clojurescript's om and Datascript

## Overview

FIXME: Write a paragraph about the library/project and highlight its goals.
This is an example of an om and datascript project, using a datascript db as the OM application data root.
When changes are made to the db, om reacts and rebuilds the components. This allows for components to interact
solely with the database, and not with eachother, simplifying the construction of complex components that share data
with each other.

Additionally, it is planned to connect the in-browser datascript db to a server side datomic db, allowing server pushes
to the browser to automatically refresh.


## Setup

Content for the blog is held in edn files in the resources/edn directory.


To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2015 Stormnmos

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
