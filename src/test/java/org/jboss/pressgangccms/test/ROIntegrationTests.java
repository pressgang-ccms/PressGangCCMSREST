package org.jboss.pressgangccms.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * A collection of tests designed to test the functionality of the REST interface. The results are benchmarked: see
 * http://labs.carrotsearch.com/junit-benchmarks-tutorial.html. You can then use https://github.com/ryt/githtml to
 * view the results directly from GitHub.
 * 
 * @author Matthew Casperson
 * 
 */
@AxisRange(min = 0, max = 20)
@BenchmarkHistoryChart(filePrefix = "benchmarks", labelWith = LabelType.TIMESTAMP, maxRuns = 356)
public class ROIntegrationTests extends AbstractBenchmark implements TestBase
{
	/**
	 * Tests the root URL of the REST interface, which should return a HTTP code of 200. If not, the HAProxy will not recognise the web app.
	 */
	@Test
	public void getRESTInfo()
	{
		RestAssured.reset();
		RestAssured.baseURI = "http://devrest-pressgangccms.rhcloud.com";
		RestAssured.port = 80;

		expect().statusCode(200).when().get("/");
	}

	/**
	 * Tests an unexpanded collection of tags
	 * 
	 * @param path
	 *            The REST endpoint path
	 * @param collectionName
	 *            The name of the collection holding the entities
	 */
	private void getCollection(final String path, final String collectionName)
	{
		final Map<String, String> env = System.getenv();
		if (env.containsKey(RESTPASS) && env.containsKey(RESTUSER))
		{
			final String restUser = env.get(RESTUSER);
			final String restPass = env.get(RESTPASS);

			RestAssured.reset();
			RestAssured.baseURI = "http://devrest-pressgangccms.rhcloud.com";
			RestAssured.authentication = basic(restUser, restPass);
			RestAssured.urlEncodingEnabled = true;
			RestAssured.port = 80;

			/* Test the response */
			final Response res = get(path);
			assertEquals(200, res.getStatusCode());

			final String json = res.asString();
			final JsonPath jp = new JsonPath(json);

			assertNull(jp.get("size"));
			assertNull(jp.get("endExpandIndex"));
			assertNull(jp.get("startExpandIndex"));
			assertNull(jp.get("items"));
			assertTrue(collectionName.equals(jp.getString("expand")));
		}
	}

	/**
	 * This method is used to test the validity of a collection of entities
	 * 
	 * @param path
	 *            The REST endpoint path
	 * @param expandString
	 *            The expansion string
	 * @param collections
	 *            The collections held by each of the entities in the collection
	 * @param entityName
	 *            The name of the entity
	 * @param collectionName
	 *            The name of the collection holding the entities
	 */
	@SuppressWarnings("rawtypes")
	private void getExpandedCollection(final String path, final String expandString, final String[] collections, final String entityName, final String collectionName)
	{
		final Map<String, String> env = System.getenv();
		if (env.containsKey(RESTPASS) && env.containsKey(RESTUSER))
		{
			final String restUser = env.get(RESTUSER);
			final String restPass = env.get(RESTPASS);

			RestAssured.reset();
			RestAssured.baseURI = "http://devrest-pressgangccms.rhcloud.com";
			RestAssured.authentication = basic(restUser, restPass);
			RestAssured.urlEncodingEnabled = true;
			RestAssured.port = 80;

			/* Test the response */
			final Response res = given().param("expand", expandString).get(path);
			assertEquals(200, res.getStatusCode());

			final String json = res.asString();
			final JsonPath jp = new JsonPath(json);

			assertEquals(jp.getInt("endExpandIndex"), jp.getInt("size"));
			assertEquals(jp.getInt("startExpandIndex"), 0);
			assertTrue(collectionName.equals(jp.getString("expand")));

			final List<Map> items = jp.getList("items", Map.class);

			assertNotNull(items);
			assertTrue(items.size() == jp.getInt("size"));

			/* Loop over each entity in the collection */
			for (final Map item : items)
			{
				assertNotNull(item.get("id"));
				assertTrue(item.get("id").toString().matches("\\d+"));
				assertNull(item.get("revision"));
				assertNull(item.get("configuredParameters"));
				assertEquals(item.get("selfLink"), RestAssured.baseURI + "/1/" + entityName + "/get/json/" + item.get("id"));
				assertEquals(item.get("editLink"), RestAssured.baseURI + "/1/" + entityName + "/put/json/" + item.get("id"));
				assertEquals(item.get("deleteLink"), RestAssured.baseURI + "/1/" + entityName + "/delete/json/" + item.get("id"));
				assertEquals(item.get("addLink"), RestAssured.baseURI + "/1/" + entityName + "/post/json");
				assertFalse((Boolean) item.get("addItem"));
				assertFalse((Boolean) item.get("removeItem"));
				assertNotNull(item.get("name"));

				final List expand = (List) item.get("expand");

				assertNotNull(expand);

				/* Every collection should be listed in the expand list */
				assertTrue(expand.size() == collections.length);

				/* Make sure each collection is initilized as an empty collection */
				for (final String subCollectionName : collections)
				{
					assertTrue(expand.contains(subCollectionName));

					final Map collection = (Map) item.get(subCollectionName);

					assertNotNull(collection);
					assertNull(collection.get("size"));
					assertEquals(collection.get("expand"), subCollectionName);
					assertNull(collection.get("startExpandIndex"));
					assertNull(collection.get("endExpandIndex"));
					assertNull(collection.get("items"));
				}
			}
		}
	}

	@Test
	public void testGetTags()
	{
		final String path = "/1/tags/get/json/all";
		final String collectionName = "tags";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetCategories()
	{
		final String path = "/1/categories/get/json/all";
		final String collectionName = "categories";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetProjects()
	{
		final String path = "/1/projects/get/json/all";
		final String collectionName = "projects";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetTopics()
	{
		final String path = "/1/topics/get/json/all";
		final String collectionName = "topics";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetTranslatedTopics()
	{
		final String path = "/1/translatedtopics/get/json/all";
		final String collectionName = "translatedtopics";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetImages()
	{
		final String path = "/1/images/get/json/all";
		final String collectionName = "images";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetUsers()
	{
		final String path = "/1/users/get/json/all";
		final String collectionName = "users";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetBlobConstants()
	{
		final String path = "/1/blobconstants/get/json/all";
		final String collectionName = "blobconstants";
		getCollection(path, collectionName);
	}

	@Test
	public void testGetStringConstants()
	{
		final String path = "/1/stringconstants/get/json/all";
		final String collectionName = "stringconstants";
		getCollection(path, collectionName);
	}

	/**
	 * Tests an expanded collection of tags
	 */
	@Test
	public void getGetTagsExpanded()
	{
		/* The list of collections held by this entity */
		final String[] collections = new String[]
		{ "categories", "parentTags", "childTags", "projects", "properties", "revisions" };

		final String path = "/1/tags/get/json/all";

		final String expandString = "{\"branches\":[{\"trunk\":{\"name\":\"tags\",\"showSize\":true}}]}";

		getExpandedCollection(path, expandString, collections, "tag", "tags");
	}

	/**
	 * Tests an expanded collection of topics
	 */
	@Test
	public void getGetTopicsExpanded()
	{
		/* The list of collections held by this entity */
		final String[] collections = new String[]
		{ "categories", "parentTags", "childTags", "projects", "properties", "revisions" };

		final String path = "/1/topics/get/json/all";

		final String expandString = "{\"branches\":[{\"trunk\":{\"name\":\"topics\",\"showSize\":true}}]}";

		getExpandedCollection(path, expandString, collections, "topic", "topics");
	}

	/**
	 * Tests an expanded collection of categories
	 */
	@Test
	public void getGetCategoriesExpanded()
	{
		/* The list of collections held by this entity */
		final String[] collections = new String[]
		{ "tags", "revisions" };

		final String path = "/1/categories/get/json/all";

		final String expandString = "{\"branches\":[{\"trunk\":{\"name\":\"categories\",\"showSize\":true}}]}";

		getExpandedCollection(path, expandString, collections, "category", "categories");
	}

	/**
	 * Tests an expanded collection of projects
	 */
	@Test
	public void getGetProjectsExpanded()
	{
		/* The list of collections held by this entity */
		final String[] collections = new String[]
		{ "tags", "revisions" };

		final String path = "/1/projects/get/json/all";

		final String expandString = "{\"branches\":[{\"trunk\":{\"name\":\"projects\",\"showSize\":true}}]}";

		getExpandedCollection(path, expandString, collections, "project", "projects");
	}
}
