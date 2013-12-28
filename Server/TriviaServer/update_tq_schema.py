import logging
import models
import random
from google.appengine.ext import deferred
from google.appengine.ext import db

BATCH_SIZE = 100  # ideal batch size may vary based on entity size.

def UpdateSchema(cursor=None, num_updated=0):
    tq_query = models.TextQuestion.all()
    cat_query = models.Category.all()

    tq_query.filter("rand =", None)

    if cursor:
        query.with_cursor(cursor)

    to_put = []
    old_cat_list = [cat.category for cat in cat_query]
    new_cat_list = []
    for tq in tq_query.fetch(limit=BATCH_SIZE):
        # In this example, the default values of 0 for num_votes and avg_rating
        # are acceptable, so we don't need this loop.  If we wanted to manually
        # manipulate property values, it might go something like this:
        if ((tq.category not in new_cat_list) and tq.category != ""):
            new_cat_list.append(tq.category)

        tq.rand = random.random()
        tq.put()
        to_put.append(tq)

    for new_cat in new_cat_list:
        if(new_cat not in old_cat_list):
            to_put.append(models.Category(category=new_cat))

    if to_put:
        db.put(to_put)
        num_updated += len(to_put)
        logging.debug(
            'Put %d entities to Datastore for a total of %d',
            len(to_put), num_updated)
        deferred.defer(
            UpdateSchema, cursor=tq_query.cursor(), num_updated=num_updated)
        deferred.defer(
            UpdateSchema, cursor=cat_query.cursor(), num_updated=num_updated)
    else:
        logging.debug(
            'UpdateSchema complete with %d updates!', num_updated)
