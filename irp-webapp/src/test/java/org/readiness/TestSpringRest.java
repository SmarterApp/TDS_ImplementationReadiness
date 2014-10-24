package org.readiness;

import java.util.Arrays;
import java.util.List;

import org.readiness.items.domain.ItemAttribute;
import org.readiness.items.domain.Itemrelease.Item;
import org.readiness.items.domain.Itemrelease.Item.Attriblist;
import org.readiness.items.domain.Itemrelease.Item.Content;
import org.readiness.items.domain.Itemrelease.Item.Resourceslist;
import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Metadata;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;
import org.readiness.scoring.domain.TDSReport.Examinee;
import org.readiness.scoring.domain.TDSReport.Opportunity;
import org.readiness.scoring.domain.TDSReport.Test;
import org.readiness.student.domain.Student;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TestSpringRest {

	public static final String SERVER_URI = "http://149.142.253.240:8090/IReadinessPackage";

	public static void main(String[] args) {
		testManifestRest();
		testItemRest();
		testStudentRest();
		testScoringRest();
	}

	private static void testManifestRest() {
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		HttpHeaders requestHeaders=new HttpHeaders();
		ResponseEntity<Manifest> responseEntity = restTemplate
				.exchange(SERVER_URI+"/manifest", HttpMethod.GET, new HttpEntity<Object>(requestHeaders),
						Manifest.class);
		
		restTemplate = new RestTemplate();
		ResponseEntity<Resources[]> responseEntityArray = restTemplate
				.exchange(SERVER_URI+"/manifest/resources", HttpMethod.GET
						, new HttpEntity<Object>(requestHeaders), Resources[].class);
		List<Resources> listResources = Arrays.asList(responseEntityArray.getBody());
		
		restTemplate = new RestTemplate();
		Resource resource = restTemplate.getForObject(SERVER_URI
				+ "/manifest/resources/item-187-174", Resource.class);
		
		restTemplate = new RestTemplate();
		Metadata metadata = restTemplate.getForObject(SERVER_URI
				+ "/manifest/metadata", Metadata.class);
		
		restTemplate = new RestTemplate();
		String organizations = restTemplate.getForObject(SERVER_URI
				+ "/manifest/organizations", String.class);
		
	}

	private static void testItemRest(){
		/****** not all items services tested *******/
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		Item item = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174", Item.class);
		
		restTemplate = new RestTemplate();
		ItemAttribute itemAttribute = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/attribute", ItemAttribute.class);
		
		restTemplate = new RestTemplate();
		Attriblist attriblist = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/attriblist", Attriblist.class);
		
		restTemplate = new RestTemplate();
		Resourceslist resourceslist = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/resourceslist", Resourceslist.class);
		
		restTemplate = new RestTemplate();
		Content[] contentArray = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/content", Content[].class);
		List<Content> listContent = Arrays.asList(contentArray);
		
		System.out.println("end of testItemRest");
	}
	
	private static void testStudentRest(){
		/****** not all student services tested *******/
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		Student[] studentArray = restTemplate.getForObject(SERVER_URI
				+ "/students", Student[].class);
		List<Student> listStudent = Arrays.asList(studentArray);
		
		restTemplate = new RestTemplate();
		Student student = restTemplate.getForObject(SERVER_URI
				+ "/students/524335", Student.class);
		
	}
	
	private static void testScoringRest(){
		/****** not all scoring services tested *******/
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		Test test = restTemplate.getForObject(SERVER_URI
				+ "/scoring/test", Test.class);
		
		restTemplate = new RestTemplate();
		Examinee examinee = restTemplate.getForObject(SERVER_URI
				+ "/scoring/examinee", Examinee.class);
		
		restTemplate = new RestTemplate();
		Opportunity opportunity = restTemplate.getForObject(SERVER_URI
				+ "/scoring/opportunity", Opportunity.class);
		
		System.out.println("end of testScoringRest");
	}
}
