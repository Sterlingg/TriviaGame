#!/bin/bash

./google_appengine/appcfg.py upload_data --config_file=TriviaServer/bulkloader.yaml --filename=TriviaServer/questions.csv --kind=TextQuestion --url=http://localhost:9090/_ah/remote_api
