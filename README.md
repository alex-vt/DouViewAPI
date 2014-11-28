##DouViewAPI

The REST API for viewing data from DOU.ua forums.

###Usage

This REST service produces application/json.

To get 5 topics recently cached by the service, perform a GET request:

	http://dou-view-api.appspot.com/cache/forums/5-recent-topics

To get directly from DOU.ua and to cache 3 recently active topics, perform a GET request:

	http://dou-view-api.appspot.com/forums/3-topics

To clear the cache, perform a DELETE request:

	http://dou-view-api.appspot.com/cache/forums/clear


###Running the service at local port 8181

You need Java 1.7+ and Maven 3+. Run

	git clone https://github.com/oleksiykovtun/DouViewAPI.git
    cd DouViewAPI
	mvn appengine:devserver

###Deploying the application to the Google App Engine cloud

After specifying application ID in `src/main/webapp/WEB-INF/appengine-web.xml`, run

    mvn appengine:update
