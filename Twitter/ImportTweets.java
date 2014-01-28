/**
 *	puneeth_nn
 *  Jan 28, 2014
 *  3:11:35 PM
 */
package com.np.social;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jetty.util.ajax.JSON;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//Twitter 1.0 feed grab

public class ImportTweets {

	public static void main(String[] args) throws IOException, ParseException{
		final String screenName = args.length > 0 ? args[0] :"MongoDB";
		
		List<DBObject> tweets = getLatestTweets(screenName);
		
		MongoClient client = new MongoClient();
		
		DBCollection tweetsCollection = client.getDB("course").getCollection("twitter");
		
		for(DBObject cur : tweets){
			massageTweetID(cur);
			massageTweet(cur);
			tweetsCollection.insert(cur);
		}
		
		System.out.println("Tweet Count : "+tweetsCollection.count());
		
		client.close();
	}

	/**
	 * @param cur
	 * @throws ParseException 
	 */
	private static void massageTweet(final DBObject cur) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM d H:m:s Z y");
		cur.put("created_at", fmt.parse((String) cur.get("created_at")));
		
		DBObject userDoc = (DBObject) cur.get("user");
		Iterator<String> keyIterator = userDoc.keySet().iterator();
		
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			if (!(key.equals("id")||key.equals("name")||key.equals("screen_name"))) {
				keyIterator.remove();
			}
		}
	}

	/**
	 * @param cur
	 */
	private static void massageTweetID(final DBObject cur) {
		// TODO Auto-generated method stub
		Object id = cur.get("id");
		cur.removeField("id");
		cur.put("_id", id);
	}

	/**
	 * @param screenName
	 * @return
	 * @throws IOException 
	 */
	private static List<DBObject> getLatestTweets(String screenName) throws IOException {
		// TODO Auto-generated method stub
		URL uri = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name="+screenName+"&include_rts=1");
		
		InputStream is = uri.openStream();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int retVal;
		while((retVal=is.read())!=-1){
			os.write(retVal);
					}
		final String tweetString = os.toString();
		
		return (List<DBObject>) JSON.parse(tweetString);
	}
	
}
