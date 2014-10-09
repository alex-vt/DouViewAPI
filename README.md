##DouView

The REST service for getting data from DOU.ua forums.

####Available Online:

http://douview.appspot.com/


###Usage

This REST service produces text/xml.

To get 5 topics recently cached by the service, perform a GET request:

	http://douview.appspot.com/cache/5-recent-topics

To get directly from DOU.ua and cache 3 recently active topics, perform a GET request:

	http://douview.appspot.com/direct/3-topics

To clear the cache, perform a DELETE request:

	http://douview.appspot.com/cache/clear


###Running the service at local port 8181

You need Java 1.7+ and Maven 3+. Run

	git clone https://github.com/oleksiykovtun/DouView.git
    cd DouView
	mvn appengine:devserver

###Deploying the application to the Google App Engine cloud

After specifying application ID in `src/main/webapp/WEB-INF/appengine-web.xml`, run

    mvn appengine:update
