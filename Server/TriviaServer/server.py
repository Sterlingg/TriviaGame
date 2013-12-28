import cgi
import random
import json
import cStringIO
import datetime
import logging
import urllib
import webapp2
import jinja2
import os
import csv
import string
import base64
import time

import models
import update_tq_schema

from Crypto.Cipher import AES
from google.appengine.ext import db
from google.appengine.api import memcache
from google.appengine.api import users
from google.appengine.ext import deferred

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

# MainPage: The root page.
class MainPage(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        template = jinja_environment.get_template('index.html')
        self.response.out.write(template.render(template_values))

# ParseTKQuestions: Adds random float to all TextQuestion entities, and updates the available categories.
class ParseTKQuestions(webapp2.RequestHandler):
    def get(self):
        deferred.defer(update_tq_schema.UpdateSchema)
        self.response.out.write('Schema migration successfully initiated.')

# getQuestion: Returns 25 questions in JSON format encrypted with AES-CBC 128 bit.
class getQuestion(webapp2.RequestHandler):
    def get(self):
        rand_num = random.random()

        if(self.request.get("category") == "" or (self.request.get("category") == "All")):
            questions_query = models.TextQuestion.all().order('rand').filter('rand >=',rand_num)
        else:
            questions_query = models.TextQuestion.all().order('rand').filter('rand >=',rand_num).filter("category =",self.request.get("category"))

        questions = questions_query.fetch(15)

        jsonstr = json.dumps([q.to_dict() for q in questions])
        self.response.out.write(jsonstr)

# getQuestion: Returns 25 questions in JSON format encrypted with AES-CBC 128 bit.
class getCategory(webapp2.RequestHandler):
    def get(self):
        category_query = models.Category.all()
        categories = category_query.fetch(25)

        jsonstr = json.dumps([c.to_dict() for c in categories])
        self.response.out.write(jsonstr)

app = webapp2.WSGIApplication([('/', MainPage),
                               ('/getquestion',getQuestion),
                               ('/getcategory',getCategory),
                               ('/parsetk',ParseTKQuestions)
                               ],
                              debug=True)

