from google.appengine.ext import db

def guestbook_key(guestbook_name=None):
    return db.Key.from_path('Guestbook', guestbook_name or 'default_guestbook')

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
