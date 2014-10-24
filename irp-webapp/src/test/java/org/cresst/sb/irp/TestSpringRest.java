package org.cresst.sb.irp;

import java.util.Arrays;
import java.util.List;

import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.domain.scoring.TDSReport;
import org.cresst.sb.irp.domain.student.Student;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Ignore
public class TestSpringRest {

	public static final String SERVER_URI = "http://localhost:8080/";

	@Test
	public void testManifestRest() {
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		HttpHeaders requestHeaders=new HttpHeaders();
		ResponseEntity<Manifest> responseEntity = restTemplate
				.exchange(SERVER_URI+"/manifest", HttpMethod.GET, new HttpEntity<Object>(requestHeaders),
						Manifest.class);
		
		restTemplate = new RestTemplate();
		ResponseEntity<Manifest.Resources[]> responseEntityArray = restTemplate
				.exchange(SERVER_URI+"/manifest/resources", HttpMethod.GET
						, new HttpEntity<Object>(requestHeaders), Manifest.Resources[].class);
		List<Manifest.Resources> listResources = Arrays.asList(responseEntityArray.getBody());
		
		restTemplate = new RestTemplate();
		Manifest.Resources.Resource resource = restTemplate.getForObject(SERVER_URI
				+ "/manifest/resources/item-187-174", Manifest.Resources.Resource.class);
		
		restTemplate = new RestTemplate();
		Manifest.Metadata metadata = restTemplate.getForObject(SERVER_URI
				+ "/manifest/metadata", Manifest.Metadata.class);
		
		restTemplate = new RestTemplate();
		String organizations = restTemplate.getForObject(SERVER_URI
				+ "/manifest/organizations", String.class);
		
	}

	@Test
	public void testItemRest(){
		/****** not all items services tested *******/
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		Itemrelease.Item item = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174", Itemrelease.Item.class);
		
		restTemplate = new RestTemplate();
		ItemAttribute itemAttribute = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/attribute", ItemAttribute.class);
		
		restTemplate = new RestTemplate();
		Itemrelease.Item.Attriblist attriblist = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/attriblist", Itemrelease.Item.Attriblist.class);
		
		restTemplate = new RestTemplate();
		Itemrelease.Item.Resourceslist resourceslist = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/resourceslist", Itemrelease.Item.Resourceslist.class);
		
		restTemplate = new RestTemplate();
		Itemrelease.Item.Content[] contentArray = restTemplate.getForObject(SERVER_URI
				+ "/items/item/174/content", Itemrelease.Item.Content[].class);
		List<Itemrelease.Item.Content> listContent = Arrays.asList(contentArray);
	}

	@Test
	public void testStudentRest(){
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

	@Test
	public void testScoringRest(){
		/****** not all scoring services tested *******/
		
		RestTemplate restTemplate;
		restTemplate = new RestTemplate();
		Test test = restTemplate.getForObject(SERVER_URI
				+ "/scoring/test", Test.class);
		
		restTemplate = new RestTemplate();
		TDSReport.Examinee examinee = restTemplate.getForObject(SERVER_URI
				+ "/scoring/examinee", TDSReport.Examinee.class);
		
		restTemplate = new RestTemplate();
		TDSReport.Opportunity opportunity = restTemplate.getForObject(SERVER_URI
				+ "/scoring/opportunity", TDSReport.Opportunity.class);
		
	}
}
