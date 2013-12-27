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

from Crypto.Cipher import AES
from google.appengine.ext import db
from google.appengine.api import memcache
from google.appengine.api import users

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

class Category(db.Model):
    category = db.StringProperty()

    def to_dict(self):
        return dict([(c, unicode(getattr(self,c))) for c in self.properties()])
    
class TextQuestion(db.Model):
    category = db.StringProperty()
    question = db.StringProperty(multiline=True)
    distractorA = db.StringProperty(multiline=True)
    distractorB = db.StringProperty(multiline=True)
    distractorC = db.StringProperty(multiline=True)
    answer = db.StringProperty(multiline=True)
    rand = db.FloatProperty()

    def to_dict(self):
        return dict([(q, unicode(getattr(self,q))) for q in self.properties()])

def guestbook_key(guestbook_name=None):
    return db.Key.from_path('Guestbook', guestbook_name or 'default_guestbook')

# MainPage: The root page.
class MainPage(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        template = jinja_environment.get_template('index.html')
        self.response.out.write(template.render(template_values))


# ParseTKQuestions: Adds random float to all TextQuestion entities, and updates the available categories.
#                  
class ParseTKQuestions(webapp2.RequestHandler):
    def get(self):

        questions_query = TextQuestion.all()
        category_query = Category.all()

        # Stores all categories used by questions in new_cat_list.
        new_cat_list = []
        for q in questions_query:
            if (q.category not in new_cat_list and q.category != ""):
                new_cat_list.append(q.category)
                
        # Get a list of all the categories currently in the db.
        old_cat_list = []
        for cat in category_query:
            old_cat_list.append(cat.category)
            
        # Checks if the category is already in the database, and puts it in if it isn't already in the
        # category db.
        for new_cat in new_cat_list:
            if(new_cat not in old_cat_list):
                result_category = Category(category=new_cat)
                result_category.put()

        # Add a random float to each element, so we can choose random questions.
        questions_query.filter("rand =",None)

        for q in questions_query:
            q.rand=random.random()
            q.put()

        questions = questions_query.fetch(800)
        template_values = {
            'questions': questions
        }
        self.response.out.write(str(questions))

# getQuestion: Returns 25 questions in JSON format encrypted with AES-CBC 128 bit.
class getQuestion(webapp2.RequestHandler):
    def get(self):
        rand_num = random.random()

        if(self.request.get("category") == "" or (self.request.get("category") == "All")):
            questions_query = TextQuestion.all().order('rand').filter('rand >=',rand_num)
        else:
            questions_query = TextQuestion.all().order('rand').filter('rand >=',rand_num).filter("category =",self.request.get("category"))

#        questions_query = TextQuestion.all()
        questions = questions_query.fetch(15)

        jsonstr = json.dumps([q.to_dict() for q in questions])
        self.response.out.write(jsonstr)

# getQuestion: Returns 25 questions in JSON format encrypted with AES-CBC 128 bit.
class getCategory(webapp2.RequestHandler):
    def get(self):
        category_query = Category.all()
        categories = category_query.fetch(25)

        jsonstr = json.dumps([c.to_dict() for c in categories])
        self.response.out.write(jsonstr)

app = webapp2.WSGIApplication([('/', MainPage),
#                               ('/database',Database),  
                               ('/getquestion',getQuestion),
                               ('/getcategory',getCategory),
                               ('/parsetk',ParseTKQuestions)
                               ],
                              debug=True)

