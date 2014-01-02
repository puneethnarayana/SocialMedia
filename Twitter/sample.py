import json
import csv
tweets = []

for line in open('total_tweets.txt'):
  try: 
    tweets.append(json.loads(line))
  except:
    pass
#print len(tweets)
#tweet = tweets[0]
#print tweet
#print tweet.keys()
#ids = []
#for tweet in tweets:
#  ids.append(tweet.get('id_str'))
#tweet.get('text').encode('utf-8')
#ids = [tweet.get('id_str') for tweet in tweets]
#texts = [tweet.get('text') for tweet in tweets]
#times = [tweet.get('created_at') for tweet in tweets]
#trial=ids[0]
#print trial
#for key in tweet.keys():
 #  print key
  # print tweet.get(key)
#if key != 'retweeted_status' and key != 'user':
    #with open('twitterdata.csv','w',newline='') as csvfile:
writer=csv.writer(open('twitterdata.csv','w'),delimiter = ' ')
for tweet in tweets: 
   for key in tweet.keys():
	#if key != 'retweeted_status' and key != 'user':
         if key == 'text': 
            writer.writerow([tweet.get('text').encode('utf-8').strip(),tweet.get('id'),tweet.get('favorited'),tweet.get('created_at')])		
        

