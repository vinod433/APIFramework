package stepDefinations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.Location;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class StepDefination extends Utils
{
	ResponseSpecification responsespec;
	RequestSpecification res;
	Response response;
	static String place_id;
	TestDataBuild data=new TestDataBuild();
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_place_payload_with(String name, String language, String address) throws IOException
	{
	     res=given().spec(requestSpecification())
	    .body(data.addPlacePayLoad(name,language,address));
	}
	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method)
	{
		APIResources apiResource=APIResources.valueOf(resource);
		System.out.println(apiResource.getResource());
		
		responsespec=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		if(method.equalsIgnoreCase("POST"))
	        response=res.when().post(apiResource.getResource());
		else if(method.equalsIgnoreCase("Get"))
			response=res.when().get(apiResource.getResource());
			
	}
	@Then("the API call is success with status code {int}")
	public void the_api_call_is_success_with_status_code(Integer int1)
	{
	   assertEquals(response.getStatusCode(), 200);
	}
	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyvalue, String Expectedvalue)
	{
		
		
		assertEquals(getJsonPath(response, keyvalue), Expectedvalue);  
	}
	@Then("Verify place_Id created maps to {string} using {string}")
	public void verify_place_id_created_maps_to_using(String ExpectedName, String resource) throws IOException
	{
		place_id= getJsonPath(response, "place_id");
	    res=given().spec(requestSpecification()).queryParam("place_id", place_id);
	    user_calls_with_http_request(resource,"GET");
	    String ActualName= getJsonPath(response, "name");
	    assertEquals(ActualName, ExpectedName);
	}
	@Given("DeletePlace Payload")
	public void delete_place_payload() throws IOException 
	{
	    res=given().spec(requestSpecification()).body(data.deletePlacePayload(place_id));
	}


}
